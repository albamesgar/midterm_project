package com.ironhack.midterm_project.controller.impl;

import com.ironhack.midterm_project.controller.interfaces.UserController;
import com.ironhack.midterm_project.model.accounts.Account;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.Admin;
import com.ironhack.midterm_project.model.users.ThirdParty;
import com.ironhack.midterm_project.model.users.User;
import com.ironhack.midterm_project.repository.AccountHolderRepository;
import com.ironhack.midterm_project.repository.AdminRepository;
import com.ironhack.midterm_project.repository.ThirdPartyRepository;
import com.ironhack.midterm_project.repository.UserRepository;
import com.ironhack.midterm_project.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    //Show all users -> admins and account-holders (admin)
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findUsers(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userRepository.findAll();
    }

    //Show all admins (admin)
    @GetMapping("/users/admins")
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> findAdmins(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Admin> admins = adminRepository.findAll();
        return admins;
    }

    //Show all account-holders (admin)
    @GetMapping("/users/account-holders")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> findAccountHolders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return accountHolderRepository.findAll();
    }

    //Show all third-parties (admin)
    @GetMapping("/users/third-parties")
    public List<ThirdParty> findThirdParties(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return thirdPartyRepository.findAll();
    }

    //Show user by id (admin)
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findUserById(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user;
    }

    //Show third party by id (admin)
    @GetMapping("/users/third-parties/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ThirdParty findThirdPartyById(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long id) {
        ThirdParty thirdParty = thirdPartyRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Third Party not found"));
        return thirdParty;
    }

    //Show my user data (account-holder and admin)
    @GetMapping("/my-user")
    @ResponseStatus(HttpStatus.OK)
    public User findMyUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user;
    }

    //Create admin ???
    @PostMapping("/new/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin createAdmin(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestBody @Valid Admin admin) {
        for (Admin admin1 : adminRepository.findAll()){
            if (admin.equals(admin1)){
                throw new ResponseStatusException(HttpStatus.IM_USED, "Admin already exists");
            }
        }
        return adminRepository.save(admin);
    }

    //Create account holder
    @PostMapping("/new/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccountHolder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestBody @Valid AccountHolder accountHolder) {
        for (AccountHolder accountHolder1 : accountHolderRepository.findAll()){
            if (accountHolder.equals(accountHolder1)){
                throw new ResponseStatusException(HttpStatus.IM_USED, "Account Holder already exists");
            }
        }
        return accountHolderRepository.save(accountHolder);
    }

    //Create third-party
    @PostMapping("/new/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody ThirdParty thirdParty){
        for (ThirdParty thirdParty1 : thirdPartyRepository.findAll()){
            if (thirdParty.equals(thirdParty1)){
                throw new ResponseStatusException(HttpStatus.IM_USED, "Third Party already exists");
            }
        }
        return thirdPartyRepository.save(thirdParty);
    }

    // Delete user by id (admin)
    @DeleteMapping("/delete/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable Long id) { //Va en service
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));;
        userRepository.delete(user);
    }
}
