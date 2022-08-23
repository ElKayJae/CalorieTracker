package com.vttp2022.CalorieTracker.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Component("foodListObj")
public class FoodListObj {
    private static final Logger logger = LoggerFactory.getLogger(FoodListObj.class);

    private List<FoodData> foodList = new ArrayList<>();
    

    public List<FoodData> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<FoodData> foodList) {
        this.foodList = foodList;
    }

    public void addfood(FoodData foodData){
        foodList.add(foodData);
    }

    public void listName(){
        logger.info("Items in List");
        for (FoodData foodData : foodList){
            logger.info(foodData.getName());
        }
    }


    public static FoodListObj createJson(String json) throws IOException {
        logger.info("createJson foodList");
        FoodListObj foodList = new FoodListObj();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject jsonItem = r.readObject();
            JsonArray arr =jsonItem.getJsonArray("items");
            for (int i = 0; i < arr.size(); i++) {
                JsonObject o = arr.getJsonObject(i);
                FoodData foodData = FoodData.createJson(o);
                foodList.addfood(foodData);
                logger.info(foodData.getName());
            }
            
            return foodList;
        }
    }
}
