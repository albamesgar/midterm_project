package com.ironhack.midterm_project.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm_project.classes.Address;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.controller.dto.CreditCardDTO;
import com.ironhack.midterm_project.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.midterm_project.controller.dto.TransferDTO;
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
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerImplTest {

    @Autowired
    private MockMvc mockMvc; //Simular peticiones HTTP
    @Autowired
    private PasswordEncoder passwordEncoder;
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
        accountHolder1 = new AccountHolder("Lia", passwordEncoder.encode("1234"), accountHolderRole,
                Date.valueOf("2002-12-15"),address1);
        address2 = new Address("Salvador", 10,"La Habana",
                25,"Cuba");
        accountHolder2 = new AccountHolder("Fran", passwordEncoder.encode("1234"), accountHolderRole,
                Date.valueOf("1985-06-12"), address2,address2);
        checking = new Checking(new Money(BigDecimal.valueOf(500)),accountHolder2,
                "1234", LocalDate.of(2022,6,1));
        studentChecking = new StudentChecking(new Money(BigDecimal.valueOf(500)),accountHolder1,accountHolder2,
                passwordEncoder.encode("1234"),LocalDate.of(2022,7,1));
        creditCard = new CreditCard(new Money(BigDecimal.valueOf(1000)),accountHolder1,
                passwordEncoder.encode("1234"),new Money(BigDecimal.valueOf(200)), BigDecimal.valueOf(0.2),
                LocalDate.of(2022,7,1));
        savings = new Savings(new Money(BigDecimal.valueOf(1500)),accountHolder1,accountHolder2,
                passwordEncoder.encode("1234"), new Money(BigDecimal.valueOf(150)),
                BigDecimal.valueOf(0.0025),LocalDate.of(2021,6,1));
        thirdParty = new ThirdParty("1234","Zara");

        roleRepository.saveAll(List.of(adminRole,accountHolderRole));
        userRepository.saveAll(List.of(admin,accountHolder1,accountHolder2));
        accountRepository.saveAll(List.of(checking,studentChecking,creditCard,savings));
        thirdPartyRepository.save(thirdParty);
    }

    @AfterEach
    void tearDown() {
        thirdPartyRepository.deleteAll();
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void transferFoundings() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        TransferDTO transferDTO = new TransferDTO(new Money(BigDecimal.valueOf(10)),"Fran",checking.getId());
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
        assertEquals(new Money(BigDecimal.valueOf(510)),
                accountRepository.findById(checking.getId()).get().getBalance());
    }

//    @Test
//    void thirdPartyRefund() throws Exception {
////        HttpHeaders httpHeaders = new HttpHeaders();
////        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234
//
//        ThirdPartyTransactionDTO thirdPartyTransactionDTO =
//                new ThirdPartyTransactionDTO(new Money(BigDecimal.valueOf(10)),checking.getId(), checking.getSecretKey());
//        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);
//
//        // Hago la llamada HTTP
//        MvcResult mvcResult = mockMvc.perform(
//                        post("/third-party/"+thirdParty.getHashedKey()+"/refund")//.headers(httpHeaders)
//                                .content(body)
//                                .contentType(MediaType.APPLICATION_JSON)
//                ).andExpect(status().isAccepted())
//                .andReturn();
//        // Compruebo el formato de la respuesta
//        assertTrue(mvcResult.getResponse().getContentAsString().contains("Refund done"));
//        // Compruebo que se haya guardado en la base de datos
//        assertEquals(new Money(BigDecimal.valueOf(510)),
//                accountRepository.findById(checking.getId()).get().getBalance());
//    }

//    @Test
//    void thirdPartyDischarge() throws Exception {
////        HttpHeaders httpHeaders = new HttpHeaders();
////        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234
//
//        ThirdPartyTransactionDTO thirdPartyTransactionDTO =
//                new ThirdPartyTransactionDTO(new Money(BigDecimal.valueOf(10)),checking.getId(),checking.getSecretKey());
//        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);
//
//        // Hago la llamada HTTP
//        MvcResult mvcResult = mockMvc.perform(
//                        post("/third-party/"+thirdParty.getHashedKey()+"/discharge")//.headers(httpHeaders)
//                                .content(body)
//                                .contentType(MediaType.APPLICATION_JSON)
//                ).andExpect(status().isAccepted())
//                .andReturn();
//        // Compruebo el formato de la respuesta
//        assertTrue(mvcResult.getResponse().getContentAsString().contains("Discharge done"));
//        // Compruebo que se haya guardado en la base de datos
//        assertEquals(new Money(BigDecimal.valueOf(490)),
//                accountRepository.findById(checking.getId()).get().getBalance());
//    }
}