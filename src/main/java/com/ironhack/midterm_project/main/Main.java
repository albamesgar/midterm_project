package com.ironhack.midterm_project.main;

import com.ironhack.midterm_project.classes.Money;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Money test = new Money(new BigDecimal(-1.222));
        System.out.println(test);
    }
}
