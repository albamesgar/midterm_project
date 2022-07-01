package com.ironhack.midterm_project.service.impl;

import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.Admin;
import com.ironhack.midterm_project.model.users.ThirdParty;
import com.ironhack.midterm_project.model.users.User;
import com.ironhack.midterm_project.repository.AccountHolderRepository;
import com.ironhack.midterm_project.repository.AdminRepository;
import com.ironhack.midterm_project.repository.ThirdPartyRepository;
import com.ironhack.midterm_project.repository.UserRepository;
import com.ironhack.midterm_project.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Admin createAdmin(Admin admin) {
        for (Admin admin1 : adminRepository.findAll()){
            if (admin.equals(admin1)){
                throw new ResponseStatusException(HttpStatus.IM_USED, "Admin already exists");
            }
        }
        return adminRepository.save(admin);
    }

    public AccountHolder createAccountHolder(AccountHolder accountHolder) {
        for (AccountHolder accountHolder1 : accountHolderRepository.findAll()){
            if (accountHolder.equals(accountHolder1)){
                throw new ResponseStatusException(HttpStatus.IM_USED, "Account Holder already exists");
            }
        }
        return accountHolderRepository.save(accountHolder);
    }

    public ThirdParty createThirdParty(ThirdParty thirdParty){
        for (ThirdParty thirdParty1 : thirdPartyRepository.findAll()){
            if (thirdParty.equals(thirdParty1)){
                throw new ResponseStatusException(HttpStatus.IM_USED, "Third Party already exists");
            }
        }
        return thirdPartyRepository.save(thirdParty);
    }

    public void modifyAdmin(Long id, Admin admin) {
        User user = findUserById(id);
        admin.setId(user.getId());
        adminRepository.save(admin);
    }

    public void modifyAccountHolder(Long id,AccountHolder accountHolder) {
        User user = findUserById(id);
        accountHolder.setId(user.getId());
        accountHolderRepository.save(accountHolder);
    }

    public void modifyThirdParty(Long id,ThirdParty thirdParty){
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
