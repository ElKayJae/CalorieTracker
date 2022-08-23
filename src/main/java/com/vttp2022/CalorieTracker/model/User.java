package com.vttp2022.CalorieTracker.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component ("user")
public class User {
    String username;
    String password;
    Map<String,DayObj> dayMap = new HashMap<>();
    
    public Map<String, DayObj> getDayMap() {
        return dayMap;
    }
    public void setDayMap(Map<String, DayObj> dayMap) {
        this.dayMap = dayMap;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void addDay(DayObj dayObj){
        dayMap.put(dayObj.day, dayObj);
    }
    
}
