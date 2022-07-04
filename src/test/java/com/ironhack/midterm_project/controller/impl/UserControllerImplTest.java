package com.ironhack.midterm_project.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm_project.classes.Address;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.controller.dto.users.AccountHolderDTO;
import com.ironhack.midterm_project.controller.dto.users.AdminDTO;
import com.ironhack.midterm_project.controller.dto.users.ThirdPartyDTO;
import com.ironhack.midterm_project.enums.TransactionType;
import com.ironhack.midterm_project.model.Transaction;
import com.ironhack.midterm_project.model.accounts.Savings;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerImplTest {
    @Autowired
    private MockMvc mockMvc; //Simular peticiones HTTP
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private AccountRepository accountRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    Role adminRole, accountHolderRole;
    Admin admin;
    AccountHolder accountHolder1, accountHolder2;
    Address address1, address2;
    ThirdParty thirdParty;
    Savings savings;

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
        savings = new Savings(new Money(BigDecimal.valueOf(1500)),accountHolder1,accountHolder2,
                UUID.nameUUIDFromBytes(("1234").getBytes()).toString(), new Money(BigDecimal.valueOf(150)),
                BigDecimal.valueOf(0.0025), LocalDate.of(2021,6,1));
        thirdParty = new ThirdParty(UUID.nameUUIDFromBytes(("1234").getBytes()).toString(),"Zara");
        Transaction transaction =
                new Transaction(LocalDateTime.of(2020,3,3,11,12,1),
                        new Money(BigDecimal.valueOf(30)),savings,thirdParty, TransactionType.REFUND);
        savings.getTransactionsReceived().add(transaction);
        thirdParty.getTransactionsDone().add(transaction);

        roleRepository.saveAll(List.of(adminRole,accountHolderRole));
        userRepository.saveAll(List.of(admin,accountHolder1,accountHolder2));
        accountRepository.save(savings);
        thirdPartyRepository.save(thirdParty);
    }

    @AfterEach
    void tearDown() {
        thirdPartyRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void findUsers() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/users").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Alba"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
    }

    @Test
    void findAdmins() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/users/admins").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Alba"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Fran"));
    }

    @Test
    void findAccountHolders() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/users/account-holders").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Alba"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Fran"));
    }

    @Test
    void findThirdParties() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/users/third-parties").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Alba"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Zara"));
    }

    @Test
    void findUserById() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/users/"+accountHolder1.getId()).headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Alba"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Fran"));
    }

    @Test
    void findThirdPartyById() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/users/third-parties/"
                        +thirdParty.getId()).headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Zara"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Alba"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Fran"));
    }

    @Test
    void findMyUser_Admin() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-user").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Zara"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Alba"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Fran"));
    }

    @Test
    void findMyUser_AccountHolder() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic TGlhOjEyMzQ="); //username: Lia, password: 1234

        MvcResult mvcResult = mockMvc.perform(get("/my-user").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Zara"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Lia"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Fran"));
    }

    @Test
    void createAdmin() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        AdminDTO adminDTO = new AdminDTO("Pep","1234",adminRole.getName());
        String body = objectMapper.writeValueAsString(adminDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/admin").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pep"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(userRepository.count()==4);
    }

    @Test
    void createAdmin_WrongRole() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        AdminDTO adminDTO = new AdminDTO("Pep","1234","AMIN");
        String body = objectMapper.writeValueAsString(adminDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/admin").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void createAdmin_AdminAlreadyExists() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        AdminDTO adminDTO = new AdminDTO("Alba","1234","ADMIN");
        String body = objectMapper.writeValueAsString(adminDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/admin").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isImUsed())
                .andReturn();
    }

    @Test
    void createAccountHolder() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("Pep",
                passwordEncoder.encode("1234"), accountHolderRole.getName(),
                Date.valueOf("2003-04-22"),address1);
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        System.out.println(body);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/account-holder").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pep"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("2003-04-22"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(userRepository.count()==4);
    }

    @Test
    void createAccountHolder_WithMailingAddress() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("Pep",
                passwordEncoder.encode("1234"), accountHolderRole.getName(),
                Date.valueOf("2003-04-22"),address1,address1);
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        System.out.println(body);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/account-holder").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pep"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("2003-04-22"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(userRepository.count()==4);
    }

    @Test
    void createAccountHolder_WrongRole() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("Pep",
                passwordEncoder.encode("1234"), "ACOUNT_HOLDER",
                Date.valueOf("2003-04-22"),address1);
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        System.out.println(body);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/account-holder").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void createThirdParty() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("1234","Dia");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/third-party").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Compruebo el formato de la respuesta
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Dia"));
        // Compruebo que se haya guardado en la base de datos
        assertTrue(thirdPartyRepository.count()==2);
    }

    @Test
    void createThirdParty_AlreadyExists() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("1234","Zara");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        post("/new/third-party").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isImUsed())
                .andReturn();
    }

    @Test
    void modifyAdmin() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        AdminDTO adminDTO = new AdminDTO("Pep","1234",adminRole.getName());
        String body = objectMapper.writeValueAsString(adminDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        put("/users/admins/"+admin.getId()+"/modify-data").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent())
                .andReturn();
        // Compruebo el formato de la respuesta
        // Compruebo que se haya guardado en la base de datos
        assertTrue(userRepository.count()==3);
        assertEquals("Pep",userRepository.findById(admin.getId()).get().getUsername());
    }

    @Test
    void modifyAccountHolder() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("Pep",
                passwordEncoder.encode("1234"),accountHolderRole.getName(),
                Date.valueOf("2003-04-22"),address1,address1);
        String body = objectMapper.writeValueAsString(accountHolderDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        put("/users/account-holders/"+accountHolder1.getId()+"/modify-data")
                                .headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent())
                .andReturn();
        // Compruebo el formato de la respuesta
        // Compruebo que se haya guardado en la base de datos
        assertTrue(userRepository.count()==3);
        assertEquals("Pep",userRepository.findById(accountHolder1.getId()).get().getUsername());
    }

    @Test
    void modifyThirdParty() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("1234","Dia");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);

        // Hago la llamada HTTP
        MvcResult mvcResult = mockMvc.perform(
                        put("/users/third-parties/"+thirdParty.getId()+"/modify-data").headers(httpHeaders)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent())
                .andReturn();
        // Compruebo el formato de la respuesta
        // Compruebo que se haya guardado en la base de datos
        assertTrue(thirdPartyRepository.count()==1);
        assertEquals("Dia",thirdPartyRepository.findById(thirdParty.getId()).get().getName());
    }

    @Test
    void deleteUser() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(delete("/delete/user/"+accountHolder1.getId())
                        .headers(httpHeaders))
                .andExpect(status().isNoContent())
                .andReturn();
        assertFalse(userRepository.existsById(accountHolder1.getId()));
    }

    @Test
    void deleteThirdParty() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Basic QWxiYToxMjM0"); //username: Alba, password: 1234

        MvcResult mvcResult = mockMvc.perform(delete("/delete/third-party/"+thirdParty.getId())
                        .headers(httpHeaders))
                .andExpect(status().isNoContent())
                .andReturn();
        assertFalse(thirdPartyRepository.existsById(thirdParty.getId()));
    }
}