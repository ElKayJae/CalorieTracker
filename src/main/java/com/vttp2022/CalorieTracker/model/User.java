package com.vttp2022.CalorieTracker.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component ("user")
public class User {
    String username;
    String password;
    List<DayObj> listOfDays;
    
}
