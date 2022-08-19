package com.vttp2022.CalorieTracker.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component("dayObj")
public class DayObj {
    public String day;
    public List<FoodData> dailyFood = new ArrayList<>();
    

    public String getDay() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.day = localDate.format(formatter);
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    
}
