package com.ironhack.midterm_project.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm_project.classes.Address;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.controller.dto.transactions.ThirdPartyTransactionDTO;
import com.ironhack.midterm_project.controller.dto.transactions.TransferDTO;
import com.ironhack.midterm_project.model.accounts.Checking;
import com.ironhack.midterm_project.model.accounts.CreditCard;
import com.ironhack.midterm_project.model.accounts.Savings;
import com.ironhack.midterm_project.model.accounts.StudentChecking;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.Admin;
import com.ironhack.midterm_project.model.users.Role;
import com.ironhack.midterm_project.model.users.ThirdParty;
import com.ironhack.midterm_project.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerImplTest {

    @Autowired
    private MockMvc mockMvc; //Simular peticiones HTTP
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    Role adminRole, accountHolderRole;
    Admin admin;
    AccountHolder accountHolder1, accountHolder2;
    Checking checking;
    CreditCard creditCard;
    Savings savings;
    StudentChecking studentChecking;
    Address address1, address2;
    ThirdParty thirdParty;

    @BeforeEach
    void setUp() {
        adminRole = new Role("ADMIN");
        accountHolderRole = new Role("ACCOUNT_HOLDER");
        admin = new Admin("Alba", passwordEncoder.encode("1234"), adminRole);
        address1 = new Address("Rambla", 2, "Barcelona", 17, "Spain");
        accountHolder1 = new AccountHolder("Lia",passwordEncoder.encode("1234"),
                accountHolderRole,Date.valueOf("2002-12-15"),address1);
        address2 = new Address("Salvador", 10,"La Habana",
                25,"Cuba");
        accountHolder2 = new AccountHolder("Fran", passwordEncoder.encode("1234"),
                accountHolderRole,Date.valueOf("1985-06-12"), address2,address2);
        checking = new Checking(new Money(BigDecimal.valueOf(500)),accountHolder2,
                UUID.nameUUIDFromBytes(("1234").getBytes()).toString(), LocalDate.of(2022,6,1));
        studentChecking = new StudentChecking(new Money(BigDecimal.valueOf(500)),accountHolder1,accountHolder2,
                UUID.nameUUIDFromBytes(("1234").getBytes()).toString(),LocalDate.of(2022,7,1));
        creditCard = new CreditCard(new Money(BigDecimal.valueOf(1000)),accountHolder1,
                UUID.nameUUIDFromBytes(("1234").getBytes()).toString(),new Money(BigDecimal.valueOf(200)),
                BigDecimal.valueOf(0.2),LocalDate.of(2022,7,1));
        savings = new Savings(new Money(BigDecimal.valueOf(1500)),accountHolder1,accountHolder2,
                UUID.nameUUIDFromBytes(("1234").getBytes()).toString(), new Money(BigDecimal.valueOf(150)),
                BigDecimal.valueOf(0.0025),LocalDate.of(2021,6,1));
        thirdParty = new ThirdParty(UUID.nameUUIDFromBytes(("1234").getBytes()).toString(),"Zara");

        roleRepository.saveAll(List.of(adminRole,accountHolderRole));
        userRepository.saveAll(List.of(admin,accountHolder1,accountHolder2));
        accountRepository.saveAll(List.of(checking,studentChecking,creditCard,savings));
        thirdPartyRepository.save(thirdParty);
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void transferFoundings() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        TransferDTO transferDTO = new TransferDTO(new Money(BigDecimal.valueOf(10)),"Fran",savings.getId());
        String body = objectMapper.writeValueAsString(transferDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/my-accounts/"+studentChecking.getId()+"/transfer").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isAccepted())
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Transfer done"));
        // Compruebo que se haya guardado en la base de datos
        assertEquals(new Money(BigDecimal.valueOf(490)),
                accountRepository.findById(studentChecking.getId()).get().getBalance());
        assertEquals(new Money(BigDecimal.valueOf(1510)),
                accountRepository.findById(savings.getId()).get().getBalance());
    }

    @Test
    void transferFoundings_FromWrongAccount() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        TransferDTO transferDTO = new TransferDTO(new Money(BigDecimal.valueOf(10)),"Fran",checking.getId());
        String body = objectMapper.writeValueAsString(transferDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/my-accounts/6/transfer").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void transferFoundings_ToWrongAccountId() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        TransferDTO transferDTO = new TransferDTO(new Money(BigDecimal.valueOf(10)),"Fran",7L);
        String body = objectMapper.writeValueAsString(transferDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/my-accounts/"+studentChecking.getId()+"/transfer").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void transferFoundings_ToWrongAccountName() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        TransferDTO transferDTO = new TransferDTO(new Money(BigDecimal.valueOf(10)),"Pep", checking.getId());
        String body = objectMapper.writeValueAsString(transferDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/my-accounts/"+studentChecking.getId()+"/transfer").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void transferFoundings_NoSufficientFoundingd() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        TransferDTO transferDTO = new TransferDTO(new Money(BigDecimal.valueOf(600)),"Fran",checking.getId());
        String body = objectMapper.writeValueAsString(transferDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/my-accounts/"+studentChecking.getId()+"/transfer").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    void thirdPartyRefund() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO =
                new ThirdPartyTransactionDTO(new Money(BigDecimal.valueOf(10)),checking.getId(),
                        "1234");
        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/third-party/1234/refund")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isAccepted())
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Refund done"));
        // Compruebo que se haya guardado en la base de datos
        assertEquals(new Money(BigDecimal.valueOf(510)),
                accountRepository.findById(checking.getId()).get().getBalance());
    }

    @Test
    void thirdPartyRefund_FromWrongThirdParty() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO =
                new ThirdPartyTransactionDTO(new Money(BigDecimal.valueOf(10)),checking.getId(),
                        "1234");
        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/third-party/hashedKey/refund")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void thirdPartyRefund_ToWrongAccountId() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO =
                new ThirdPartyTransactionDTO(new Money(BigDecimal.valueOf(10)),7L,
                        "1234");
        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/third-party/1234/refund")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void thirdPartyRefund_ToWrongAccountSecretKey() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO =
                new ThirdPartyTransactionDTO(new Money(BigDecimal.valueOf(10)), checking.getId(),
                        "1233");
        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/third-party/1234/refund")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    void thirdPartyDischarge() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO =
                new ThirdPartyTransactionDTO(new Money(BigDecimal.valueOf(260)),checking.getId(),
                        "1234");
        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/third-party/1234/discharge")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isAccepted())
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Discharge done"));
        // Compruebo que se haya guardado en la base de datos
        assertEquals(new Money(BigDecimal.valueOf(200)),
                accountRepository.findById(checking.getId()).get().getBalance());
    }

    @Test
    void thirdPartyDischarge_FromWrongThirdParty() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO =
                new ThirdPartyTransactionDTO(new Money(BigDecimal.valueOf(10)),checking.getId(),
                        "1234");
        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/third-party/hashedKey/discharge")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void thirdPartyDischarge_ToWrongAccountId() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO =
                new ThirdPartyTransactionDTO(new Money(BigDecimal.valueOf(10)),7L,
                        "1234");
        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/third-party/1234/discharge")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void thirdPartyDischarge_ToWrongAccountSecretKey() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO =
                new ThirdPartyTransactionDTO(new Money(BigDecimal.valueOf(10)), checking.getId(),
                        "1233");
        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/third-party/1234/discharge")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isConflict())
                .andReturn();
    }
}