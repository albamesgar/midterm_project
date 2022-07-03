package com.ironhack.midterm_project.service.impl;

import com.ironhack.midterm_project.classes.Address;
import com.ironhack.midterm_project.model.users.*;
import com.ironhack.midterm_project.repository.*;
import com.ironhack.midterm_project.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user;
    }

    public ThirdParty findThirdPartyById(Long id) {
        ThirdParty thirdParty = thirdPartyRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Third Party not found"));
        return thirdParty;
    }

    public User findMyUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user;
    }

    public Admin createAdmin(String username, String password, String roleName) {
        password = passwordEncoder.encode(password);
        Role role = roleRepository.findByName(roleName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        Admin admin = new Admin(username,password,role);
        for (Admin admin1 : adminRepository.findAll()){
            if (admin.equals(admin1)){
                throw new ResponseStatusException(HttpStatus.IM_USED, "Admin already exists");
            }
        }
        return adminRepository.save(admin);
    }

    public AccountHolder createAccountHolder(String username, String password, String roleName, Date dateOfBirth,
                                             Address primaryAddress,Address mailingAddress) {
        password = passwordEncoder.encode(password);
        Role role = roleRepository.findByName(roleName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        AccountHolder accountHolder = new AccountHolder(username,password,role,dateOfBirth,primaryAddress);
        if (mailingAddress!=null){
            accountHolder = new AccountHolder(username,password,role,dateOfBirth,primaryAddress,
                    mailingAddress);
        }
        for (AccountHolder accountHolder1 : accountHolderRepository.findAll()){
            if (accountHolder.equals(accountHolder1)){
                throw new ResponseStatusException(HttpStatus.IM_USED, "Account Holder already exists");
            }
        }
        return accountHolderRepository.save(accountHolder);
    }

    public ThirdParty createThirdParty(String hashedKey, String name){
        hashedKey = UUID.nameUUIDFromBytes(hashedKey.getBytes()).toString();
        ThirdParty thirdParty = new ThirdParty(hashedKey,name);
        for (ThirdParty thirdParty1 : thirdPartyRepository.findAll()){
            if (thirdParty.equals(thirdParty1)){
                throw new ResponseStatusException(HttpStatus.IM_USED, "Third Party already exists");
            }
        }
        return thirdPartyRepository.save(thirdParty);
    }

    public void modifyAdmin(Long id, String username, String password, String roleName) {
        password = passwordEncoder.encode(password);
        Role role = roleRepository.findByName(roleName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        Admin admin = new Admin(username,password,role);
        User user = findUserById(id);
        admin.setId(user.getId());
        adminRepository.save(admin);
    }

    public void modifyAccountHolder(Long id,String username, String password, String roleName, Date dateOfBirth,
                                    Address primaryAddress,Address mailingAddress) {
        password = passwordEncoder.encode(password);
        Role role = roleRepository.findByName(roleName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        AccountHolder accountHolder = new AccountHolder(username,password,role,dateOfBirth,primaryAddress);
        if (mailingAddress!=null){
            accountHolder = new AccountHolder(username,password,role,dateOfBirth,primaryAddress,
                    mailingAddress);
        }
        User user = findUserById(id);
        accountHolder.setId(user.getId());
        accountHolderRepository.save(accountHolder);
    }

    public void modifyThirdParty(Long id,String hashedKey, String name){
        hashedKey = UUID.nameUUIDFromBytes(hashedKey.getBytes()).toString();
        ThirdParty thirdParty = new ThirdParty(hashedKey,name);
        ThirdParty thirdParty1 = findThirdPartyById(id);
        thirdParty.setId(thirdParty1.getId());
        thirdPartyRepository.save(thirdParty);
    }

    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    public void deleteThirdParty(Long id) {
        ThirdParty thirdParty = findThirdPartyById(id);
        thirdPartyRepository.delete(thirdParty);
    }
}
