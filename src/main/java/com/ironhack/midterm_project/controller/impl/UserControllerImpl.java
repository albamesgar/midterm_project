package com.ironhack.midterm_project.controller.impl;

import com.ironhack.midterm_project.classes.Address;
import com.ironhack.midterm_project.controller.dto.users.AccountHolderDTO;
import com.ironhack.midterm_project.controller.dto.users.AdminDTO;
import com.ironhack.midterm_project.controller.dto.users.ThirdPartyDTO;
import com.ironhack.midterm_project.controller.interfaces.UserController;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.Admin;
import com.ironhack.midterm_project.model.users.ThirdParty;
import com.ironhack.midterm_project.model.users.User;
import com.ironhack.midterm_project.repository.AccountHolderRepository;
import com.ironhack.midterm_project.repository.AdminRepository;
import com.ironhack.midterm_project.repository.ThirdPartyRepository;
import com.ironhack.midterm_project.repository.UserRepository;
import com.ironhack.midterm_project.security.CustomUserDetails;
import com.ironhack.midterm_project.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;

@RestController
public class UserControllerImpl implements UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private UserService userService;

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
        return adminRepository.findAll();
    }

    //Show all account-holders (admin)
    @GetMapping("/users/account-holders")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> findAccountHolders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return accountHolderRepository.findAll();
    }

    //Show all third-parties (admin)
    @GetMapping("/users/third-parties")
    @ResponseStatus(HttpStatus.OK)
    public List<ThirdParty> findThirdParties(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return thirdPartyRepository.findAll();
    }

    //Show user by id (admin)
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findUserById(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long id) {
        return userService.findUserById(id);
    }

    //Show third party by id (admin)
    @GetMapping("/users/third-parties/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ThirdParty findThirdPartyById(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long id) {
        return userService.findThirdPartyById(id);
    }

    //Show my user data (account-holder and admin)
    @GetMapping("/my-user")
    @ResponseStatus(HttpStatus.OK)
    public User findMyUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = userDetails.getUser().getId();

        return userService.findMyUser(userId);
    }

    //Create admin (admin)
    @PostMapping("/new/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin createAdmin(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestBody @Valid AdminDTO adminDTO) {
        String username = adminDTO.getUsername();
        String password = adminDTO.getPassword();
        String roleName = adminDTO.getRole();
        return userService.createAdmin(username,password,roleName);
    }

    //Create account holder (admin)
    @PostMapping("/new/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccountHolder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestBody @Valid AccountHolderDTO accountHolderDTO) {
        String username = accountHolderDTO.getUsername();
        String password = accountHolderDTO.getPassword();
        String roleName = accountHolderDTO.getRole();
        Date dateOfBirth= accountHolderDTO.getDateOfBirth();
        Address primaryAddress = accountHolderDTO.getPrimaryAddress();
        Address mailingAddress = accountHolderDTO.getMailingAddress();
        return userService.createAccountHolder(username,password,roleName,dateOfBirth,primaryAddress,mailingAddress);
    }

    //Create third-party (admin)
    @PostMapping("/new/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody @Valid ThirdPartyDTO thirdPartyDTO){
        String hashedKey = thirdPartyDTO.getHashedKey();
        String name = thirdPartyDTO.getName();
        return userService.createThirdParty(hashedKey,name);
    }

    //Modify admin data (admin)
    @PutMapping("/users/admins/{id}/modify-data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyAdmin(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @PathVariable Long id,@RequestBody @Valid AdminDTO adminDTO) {
        String username = adminDTO.getUsername();
        String password = adminDTO.getPassword();
        String roleName = adminDTO.getRole();
        userService.modifyAdmin(id,username,password,roleName);
    }

    //Modify account holder data (admin)
    @PutMapping("/users/account-holders/{id}/modify-data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyAccountHolder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Long id,
                                    @RequestBody @Valid AccountHolderDTO accountHolderDTO) {
        String username = accountHolderDTO.getUsername();
        String password = accountHolderDTO.getPassword();
        String roleName = accountHolderDTO.getRole();
        Date dateOfBirth= accountHolderDTO.getDateOfBirth();
        Address primaryAddress = accountHolderDTO.getPrimaryAddress();
        Address mailingAddress = accountHolderDTO.getMailingAddress();
        userService.modifyAccountHolder(id,username,password,roleName,dateOfBirth,primaryAddress,mailingAddress);
    }

    //Modify third-party data (admin)
    @PutMapping("/users/third-parties/{id}/modify-data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyThirdParty(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @PathVariable Long id,@RequestBody @Valid ThirdPartyDTO thirdPartyDTO){
        String hashedKey = thirdPartyDTO.getHashedKey();
        String name = thirdPartyDTO.getName();
        userService.modifyThirdParty(id, hashedKey,name);
    }

    // Delete user by id (admin)
    @DeleteMapping("/delete/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable Long id) {
        userService.deleteUser(id);
    }

    // Delete third-party by id (admin)
    @DeleteMapping("/delete/third-party/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteThirdParty(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @PathVariable Long id) {
        userService.deleteThirdParty(id);
    }
}
