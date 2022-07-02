package com.ironhack.midterm_project.service.interfaces;

import com.ironhack.midterm_project.classes.Address;
import com.ironhack.midterm_project.model.users.AccountHolder;
import com.ironhack.midterm_project.model.users.Admin;
import com.ironhack.midterm_project.model.users.ThirdParty;
import com.ironhack.midterm_project.model.users.User;

import java.sql.Date;

public interface UserService {
    User findUserById(Long id);
    ThirdParty findThirdPartyById(Long id);
    User findMyUser(Long userId);
    Admin createAdmin(String username, String password, String roleName);
    AccountHolder createAccountHolder(String username, String password, String roleName, Date dateOfBirth,
                                      Address primaryAddress, Address mailingAddress);
    ThirdParty createThirdParty(String hashedKey, String name);
    void modifyAdmin(Long id,String username, String password, String roleName);
    void modifyAccountHolder(Long id,String username, String password, String roleName, Date dateOfBirth,
                             Address primaryAddress,Address mailingAddress);
    void modifyThirdParty(Long id,String hashedKey, String name);
    void deleteUser(Long id);
    void deleteThirdParty(Long id);
}
