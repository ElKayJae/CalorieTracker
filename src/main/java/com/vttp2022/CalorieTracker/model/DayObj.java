package com.vttp2022.CalorieTracker.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component("dayObj")
public class DayObj {
    public String day;
    public FoodListObj dailyFood ;
    
    public DayObj(){
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.day = localDate.format(formatter);
    }

    public FoodListObj getDailyFood() {
        return dailyFood;
    }

    public void setDailyFood(FoodListObj dailyFood) {
        this.dailyFood = dailyFood;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
    
    public void newDay(){
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.day = localDate.format(formatter);
    }

    
}
