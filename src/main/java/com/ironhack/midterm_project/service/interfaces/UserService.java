package com.ironhack.midterm_project.service.interfaces;

import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.Admin;
import com.ironhack.midterm_project.model.users.ThirdParty;
import com.ironhack.midterm_project.model.users.User;

public interface UserService {
    User findUserById(Long id);
    ThirdParty findThirdPartyById(Long id);
    User findMyUser(Long userId);
    Admin createAdmin(Admin admin);
    AccountHolder createAccountHolder(AccountHolder accountHolder);
    ThirdParty createThirdParty(ThirdParty thirdParty);
    void modifyAdmin(Long id, Admin admin);
    void modifyAccountHolder(Long id,AccountHolder accountHolder);
    void modifyThirdParty(Long id,ThirdParty thirdParty);
    void deleteUser(Long id);
    void deleteThirdParty(Long id);
}
