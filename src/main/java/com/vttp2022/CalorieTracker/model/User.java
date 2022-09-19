package com.vttp2022.CalorieTracker.model;

import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component ("user")
public class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class);
    String username;
    TreeMap<String,DayObj> dayMap = new TreeMap<>();


    public TreeMap<String, DayObj> getDayMap() {
        return dayMap;
    }
    public void setDayMap(TreeMap<String, DayObj> dayMap) {
        this.dayMap = dayMap;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void addDay(DayObj dayObj){
        dayMap.put(dayObj.day, dayObj);
    }

    public void delDay(String day){
        dayMap.remove(day);
        logger.info(day + " deleted");
    }
    
    public void addFood(String day, FoodData foodData){ 
        if (dayMap.containsKey(day)){
            dayMap.get(day).getDailyFood().addFood(foodData);
        } else {
            DayObj dayObj = new DayObj();
            FoodListObj foodlistObj = new FoodListObj();
            foodlistObj.addFood(foodData);
            dayObj.setDailyFood(foodlistObj);
            dayMap.put(day, dayObj);
        }
    }

    public void addFoodFromListObj(String day, FoodListObj foodListObj){

        if (foodListObj.getFoodList().size()==1){
            addFood(day, foodListObj.getFoodList().get(0));
            
        } else {
            
            for(FoodData foodData : foodListObj.getFoodList()){
                if (foodData.isChecked()){
                    foodData.setChecked(false);
                    addFood(day, foodData);
                }
            }
        }
    }

    public void addAllFromListObj(String day, FoodListObj foodListObj){

        for(FoodData foodData : foodListObj.getFoodList()){
            foodData.setChecked(false);
            addFood(day, foodData);
        }
    }

    public FoodListObj getFoodListObj(String day){
        if (!dayMap.containsKey(day)){
            DayObj dayObj = new DayObj();
            FoodListObj foodlistObj = new FoodListObj();
            dayObj.setDailyFood(foodlistObj);
            dayMap.put(day, dayObj);
        }
        return dayMap.get(day).getDailyFood();
    }
    
    public void delFood(String day, int i){
        dayMap.get(day).getDailyFood().delFood(i);
    }

    public void delFoodFromListObj(String day, FoodListObj foodListObj){
        int counter=0;
        for(int i=0; i < foodListObj.getFoodList().size(); i++){
            if (foodListObj.getFoodList().get(i).isChecked()){
                foodListObj.getFoodList().get(i).setChecked(false);
                delFood(day, i+counter);
                counter--;
                logger.info("size is " + getFoodList(day).size());
            }
        }
        if (getDayMap().containsKey(day)){
            if(getFoodList(day).size()==0){
                delDay(day);
               
            }
        }
    }

    public List<FoodData> getFoodList(String day){
        return dayMap.get(day).getDailyFood().getFoodList();
    }

}