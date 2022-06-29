package com.ironhack.midterm_project.controller.impl;

import com.ironhack.midterm_project.controller.interfaces.UserController;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.Admin;
import com.ironhack.midterm_project.model.users.ThirdParty;
import com.ironhack.midterm_project.model.users.User;
import com.ironhack.midterm_project.repository.AccountHolderRepository;
import com.ironhack.midterm_project.repository.AdminRepository;
import com.ironhack.midterm_project.repository.ThirdPartyRepository;
import com.ironhack.midterm_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserControllerImpl implements UserController {
    // View users (admin?) -> GET
    // Create users (admin) -> POST

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    //Show all users -> admins and account-holders
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    //Show all admins
    @GetMapping("/users/admins")
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> findAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return admins;
    }

    //Show all account-holders
    @GetMapping("/users/account-holders")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> findAccountHolders() {
        return accountHolderRepository.findAll();
    }

    //Show all third-parties
    @GetMapping("/users/third-parties")
    public List<ThirdParty> findThirdParties() {
        return thirdPartyRepository.findAll();
    }

    //Create admin ???
    @PostMapping("/admins")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin createAdmin(@RequestBody @Valid Admin admin) {
        return adminRepository.save(admin);
    }

    //Create account holder
    @PostMapping("/account-holders")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccountHolder(@RequestBody @Valid AccountHolder accountHolder) {
        return accountHolderRepository.save(accountHolder);
    }

    //Create third-party
    @PostMapping("/third-parties")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@RequestBody ThirdParty thirdParty){
        return thirdPartyRepository.save(thirdParty);
    }
}
