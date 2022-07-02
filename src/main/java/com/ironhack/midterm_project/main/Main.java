package com.ironhack.midterm_project.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm_project.classes.Money;
import com.ironhack.midterm_project.controller.dto.CheckingDTO;

import java.math.BigDecimal;
import java.sql.Date;

public class Main {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws JsonProcessingException {
        CheckingDTO checkingDTO = new CheckingDTO(new Money(BigDecimal.valueOf(1500)),3L,"1234");
        String body = objectMapper.writeValueAsString(checkingDTO);
        System.out.println(body);
    }
}
