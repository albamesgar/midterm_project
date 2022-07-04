package com.ironhack.midterm_project.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm_project.classes.Address;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.controller.dto.accounts.BalanceDTO;
import com.ironhack.midterm_project.controller.dto.accounts.CheckingDTO;
import com.ironhack.midterm_project.controller.dto.accounts.CreditCardDTO;
import com.ironhack.midterm_project.controller.dto.accounts.SavingsDTO;
import com.ironhack.midterm_project.enums.TransactionType;
import com.ironhack.midterm_project.model.Transaction;
import com.ironhack.midterm_project.model.accounts.Checking;
import com.ironhack.midterm_project.model.accounts.CreditCard;
import com.ironhack.midterm_project.model.accounts.Savings;
import com.ironhack.midterm_project.model.accounts.StudentChecking;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.Admin;
import com.ironhack.midterm_project.model.users.Role;
import com.ironhack.midterm_project.repository.AccountRepository;
import com.ironhack.midterm_project.repository.RoleRepository;
import com.ironhack.midterm_project.repository.UserRepository;
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
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerImplTest {

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    Role adminRole, accountHolderRole;
    Admin admin;
    AccountHolder accountHolder1, accountHolder2;
    Checking checking;
    CreditCard creditCard;
    Savings savings;
    StudentChecking studentChecking;
    Address address1, address2;

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
        Transaction transaction =
                new Transaction(LocalDateTime.of(2020,3,3,11,12,1),
                        new Money(BigDecimal.valueOf(30)),savings,checking);
        savings.getTransactionsReceived().add(transaction);

        roleRepository.saveAll(List.of(adminRole,accountHolderRole));
        userRepository.saveAll(List.of(admin,accountHolder1,accountHolder2));
        accountRepository.saveAll(List.of(checking,studentChecking,creditCard,savings));
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void findAllAccounts() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/accounts").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CHECKING"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
    }

    @Test
    void findAllChecking() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/accounts/checkings").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CHECKING"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("STUDENT_CHECKING"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("488"));
    }

    @Test
    void findAllStudentChecking() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/accounts/student-checkings").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("STUDENT_CHECKING"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
    }

    @Test
    void findAllCreditCard() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/accounts/credit-cards").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("CHECKING"));
    }

    @Test
    void findAllSavings() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/accounts/savings").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("CHECKING"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("1503.75"));
    }

    @Test
    void findAllMyAccounts_Lia() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("STUDENT_CHECKING"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
    }

    @Test
    void findAllMyAccounts_Fran() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic RnJhbjoxMjM0"); //username: Fran, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("STUDENT_CHECKING"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CHECKING"));
    }

    @Test
    void findAllMyChecking_Fran() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic RnJhbjoxMjM0"); //username: Fran, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts/checkings").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CHECKING"));
    }

    @Test
    void findAllMyChecking_Lia() throws Exception{
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts/checkings").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("CHECKING"));
    }

    @Test
    void findAllMyStudentChecking_Fran() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic RnJhbjoxMjM0"); //username: Fran, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts/student-checkings").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("STUDENT_CHECKING"));
    }

    @Test
    void findAllMyStudentChecking_Lia() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts/student-checkings").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("STUDENT_CHECKING"));
    }

    @Test
    void findAllMyCreditCard_Fran() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic RnJhbjoxMjM0"); //username: Fran, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts/credit-cards").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
    }

    @Test
    void findAllMyCreditCard_Lia() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts/credit-cards").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
    }

    @Test
    void findAllMySavings_Fran() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic RnJhbjoxMjM0"); //username: Fran, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts/savings").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
    }

    @Test
    void findAllMySavings_Lia() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts/savings").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
    }

    @Test
    void findAccount_CorrectId_CorrectAccount() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/accounts/"+savings.getId()).headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
    }

    @Test
    void findAccount_IncorrectId_NoAccount() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/accounts/5").headers(httpHeaders))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findMyAccount_LiaCorrectId_CorrectAccount() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts/"+savings.getId()).headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
    }

    @Test
    void findMyAccount_LiaIncorrectId_NoAccount() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-accounts/"+checking.getId()).headers(httpHeaders))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void createChecking_PrimaryOwnerOlderThan24_CheckingAccount() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CheckingDTO checkingDTO = new CheckingDTO(new Money(BigDecimal.valueOf(1500)),
                accountHolder2.getId(), "1234");
        String body = objectMapper.writeValueAsString(checkingDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/checking").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("minimumBalance"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("STUDENT_CHECKING"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createChecking_PrimaryYoungerOlderThan24_StudentCheckingAccount() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CheckingDTO checkingDTO = new CheckingDTO(new Money(BigDecimal.valueOf(1500)),
                accountHolder1.getId(),"1234");
        String body = objectMapper.writeValueAsString(checkingDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/checking").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("minimumBalance"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("STUDENT_CHECKING"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createCreditCard_DefaultValues() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CreditCardDTO creditCardDTO = new CreditCardDTO(new Money(BigDecimal.valueOf(1500)),accountHolder1.getId(),
                "1234");
        String body = objectMapper.writeValueAsString(creditCardDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/credit-card").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.2")); //default interestRate
        assertTrue(mvcResult.getResponse().getContentAsString().contains("100")); //default creditLimit
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createCreditCard_DefaultValuesSecondOwner() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CreditCardDTO creditCardDTO = new CreditCardDTO(new Money(BigDecimal.valueOf(1500)),accountHolder1.getId(),
                accountHolder2.getId(),"1234");
        String body = objectMapper.writeValueAsString(creditCardDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/credit-card").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.2")); //default interestRate
        assertTrue(mvcResult.getResponse().getContentAsString().contains("100")); //default creditLimit
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createCreditCard_CreditLimit() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CreditCardDTO creditCardDTO = new CreditCardDTO(new Money(BigDecimal.valueOf(1500)), accountHolder1.getId(),
                BigDecimal.valueOf(200),"1234");
        String body = objectMapper.writeValueAsString(creditCardDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/credit-card").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.2")); //default interestRate
        assertTrue(mvcResult.getResponse().getContentAsString().contains("200"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createCreditCard_CreditLimitSecondOwner() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CreditCardDTO creditCardDTO = new CreditCardDTO(new Money(BigDecimal.valueOf(1500)), accountHolder1.getId(),
                accountHolder2.getId(),BigDecimal.valueOf(200),"1234");
        String body = objectMapper.writeValueAsString(creditCardDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/credit-card").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.2")); //default interestRate
        assertTrue(mvcResult.getResponse().getContentAsString().contains("200"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createCreditCard_InterestRate() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CreditCardDTO creditCardDTO = new CreditCardDTO(new Money(BigDecimal.valueOf(1500)), accountHolder1.getId(),
                "1234", BigDecimal.valueOf(0.15));
        String body = objectMapper.writeValueAsString(creditCardDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/credit-card").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.15"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("100")); //default
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createCreditCard_InterestRateSecondOwner() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CreditCardDTO creditCardDTO = new CreditCardDTO(new Money(BigDecimal.valueOf(1500)), accountHolder1.getId(),
                accountHolder2.getId(),"1234", BigDecimal.valueOf(0.15));
        String body = objectMapper.writeValueAsString(creditCardDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/credit-card").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.15"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("100")); //default
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createCreditCard_CreditLimitInterestRate() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CreditCardDTO creditCardDTO = new CreditCardDTO(new Money(BigDecimal.valueOf(1500)), accountHolder1.getId(),
                BigDecimal.valueOf(200),"1234",BigDecimal.valueOf(0.15));
        String body = objectMapper.writeValueAsString(creditCardDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/credit-card").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.15"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("200"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createCreditCard_CreditLimitInterestRateSecondOwner() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CreditCardDTO creditCardDTO = new CreditCardDTO(new Money(BigDecimal.valueOf(1500)), accountHolder1.getId(),
                accountHolder2.getId(),BigDecimal.valueOf(200),"1234",BigDecimal.valueOf(0.15));
        String body = objectMapper.writeValueAsString(creditCardDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/credit-card").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CREDIT_CARD"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.15"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("200"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createSavingsAccount_DefaultValues() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        SavingsDTO savingsDTO = new SavingsDTO(new Money(BigDecimal.valueOf(1500)), accountHolder1.getId(),
                "1234");
        String body = objectMapper.writeValueAsString(savingsDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/savings").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.0025")); //default interestRate
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1000")); //default minimumBalance
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createSavingsAccount_DefaultValuesSecondOwner() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        SavingsDTO savingsDTO = new SavingsDTO(new Money(BigDecimal.valueOf(1500)), accountHolder1.getId(),
                accountHolder2.getId(),"1234");
        String body = objectMapper.writeValueAsString(savingsDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/savings").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.0025")); //default interestRate
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1000")); //default minimumBalance
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createSavingsAccount_MinimumBalance() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        SavingsDTO savingsDTO = new SavingsDTO(new Money(BigDecimal.valueOf(1500)), accountHolder1.getId(),
                BigDecimal.valueOf(200),"1234");
        String body = objectMapper.writeValueAsString(savingsDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/savings").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.0025")); //default interestRate
        assertTrue(mvcResult.getResponse().getContentAsString().contains("200"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createSavingsAccount_MinimumBalanceSecondOwner() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        SavingsDTO savingsDTO = new SavingsDTO(new Money(BigDecimal.valueOf(1500)), accountHolder1.getId(),
                accountHolder2.getId(),BigDecimal.valueOf(200),"1234");
        String body = objectMapper.writeValueAsString(savingsDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/savings").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.0025")); //default interestRate
        assertTrue(mvcResult.getResponse().getContentAsString().contains("200"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createSavingsAccount_InterestRate() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        SavingsDTO savingsDTO = new SavingsDTO(new Money(BigDecimal.valueOf(1500)),accountHolder1.getId(),
                "1234", BigDecimal.valueOf(0.005));
        String body = objectMapper.writeValueAsString(savingsDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/savings").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.005"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1000")); //default minimumBalance
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createSavingsAccount_InterestRateSecondOwner() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        SavingsDTO savingsDTO = new SavingsDTO(new Money(BigDecimal.valueOf(1500)),accountHolder1.getId(),
                accountHolder2.getId(),"1234", BigDecimal.valueOf(0.005));
        String body = objectMapper.writeValueAsString(savingsDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/savings").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.005"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1000")); //default minimumBalance
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createSavingsAccount_InterestRateMinimumBalance() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        SavingsDTO savingsDTO = new SavingsDTO(new Money(BigDecimal.valueOf(1500)),accountHolder1.getId(),
                BigDecimal.valueOf(200),"1234",BigDecimal.valueOf(0.005));
        String body = objectMapper.writeValueAsString(savingsDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/savings").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.005"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("200"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void createSavingsAccount_InterestRateMinimumBalanceSecondOwner() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        SavingsDTO savingsDTO = new SavingsDTO(new Money(BigDecimal.valueOf(1500)),accountHolder1.getId(),
                accountHolder2.getId(),BigDecimal.valueOf(200),"1234",BigDecimal.valueOf(0.005));
        String body = objectMapper.writeValueAsString(savingsDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/savings").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("SAVINGS"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.005"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("200"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==5);
    }

    @Test
    void modifyCheckingAccount() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CheckingDTO checkingDTO = new CheckingDTO(new Money(BigDecimal.valueOf(1500)),accountHolder2.getId(),
                accountHolder1.getId(),"1234");
        String body = objectMapper.writeValueAsString(checkingDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        put("/accounts/checkings/"+checking.getId()+"/modify-data").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent())
                .andReturn();
        // Compruebo el formato de la respuesta
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==4);
        assertTrue(accountRepository.existsById(checking.getId()));
        assertEquals(new Money(BigDecimal.valueOf(1500)),
                accountRepository.findById(checking.getId()).get().getBalance());
    }

    @Test
    void modifyCreditCardAccount() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        CreditCardDTO creditCardDTO = new CreditCardDTO(new Money(BigDecimal.valueOf(1500)),accountHolder1.getId(),
                accountHolder2.getId(),BigDecimal.valueOf(200), Currency.getInstance("USD"),
                "1234",BigDecimal.valueOf(0.15));
        String body = objectMapper.writeValueAsString(creditCardDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        put("/accounts/credit-cards/"+creditCard.getId()+"/modify-data").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent())
                .andReturn();
        // Compruebo el formato de la respuesta
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==4);
        assertTrue(accountRepository.existsById(creditCard.getId()));
        assertEquals(new Money(BigDecimal.valueOf(1500)),
                accountRepository.findById(creditCard.getId()).get().getBalance());
    }

    @Test
    void modifySavingsAccount() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        SavingsDTO savingsDTO = new SavingsDTO(new Money(BigDecimal.valueOf(3000)),accountHolder1.getId(),
                accountHolder2.getId(),BigDecimal.valueOf(200),Currency.getInstance("USD"),
                "1234",BigDecimal.valueOf(0.005));
        String body = objectMapper.writeValueAsString(savingsDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        put("/accounts/savings/"+savings.getId()+"/modify-data").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent())
                .andReturn();
        // Compruebo el formato de la respuesta
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==4);
        assertTrue(accountRepository.existsById(savings.getId()));
        assertEquals(new Money(BigDecimal.valueOf(3000.00)),
                accountRepository.findById(savings.getId()).get().getBalance());
    }

    @Test
    void modifyBalance() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        BalanceDTO balanceDTO = new BalanceDTO(new Money(BigDecimal.valueOf(2000)));
        String body = objectMapper.writeValueAsString(balanceDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        patch("/accounts/"+savings.getId()+"/modify-balance").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent())
                .andReturn();
        // Compruebo el formato de la respuesta
        // Compruebo que se haya guardado en la base de datos
        assertTrue(accountRepository.count()==4);
        assertTrue(accountRepository.existsById(savings.getId()));
        assertEquals(new Money(BigDecimal.valueOf(2000.00)),
                accountRepository.findById(savings.getId()).get().getBalance());
    }

    @Test
    void deleteAccount_CorrectId_AccountDeleted() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(delete("/delete/account/"+savings.getId()).headers(httpHeaders))
                .andExpect(status().isNoContent())
                .andReturn();
        assertFalse(accountRepository.existsById(savings.getId()));
    }

    @Test
    void deleteAccount_InorrectId_NoAccountDeleted() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(delete("/delete/account/5").headers(httpHeaders))
                .andExpect(status().isNotFound())
                .andReturn();
        assertFalse(accountRepository.count()<4);
    }
}