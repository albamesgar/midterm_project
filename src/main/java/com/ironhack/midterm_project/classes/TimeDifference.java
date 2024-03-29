package com.ironhack.midterm_project.classes;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class TimeDifference {
    public static int monthDifference(LocalDate localDate1){
        LocalDate today = LocalDate.now();
        Period timePassed = Period.between(localDate1,today);
        int months = timePassed.getMonths();
        return months;
    }

    public static int yearDifference(LocalDate localDate1){
        LocalDate today = LocalDate.now();
        Period timePassed = Period.between(localDate1,today);
        int years = timePassed.getYears();
        return years;
    }
}
