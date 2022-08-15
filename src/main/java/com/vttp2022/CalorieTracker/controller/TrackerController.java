package com.vttp2022.CalorieTracker.controller;

import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vttp2022.CalorieTracker.model.FoodData;
import com.vttp2022.CalorieTracker.model.FoodList;
import com.vttp2022.CalorieTracker.model.Query;
import com.vttp2022.CalorieTracker.service.CalorieTrackerService;

@Controller
public class TrackerController {
    private static final Logger logger = LoggerFactory.getLogger(TrackerController.class);
    
    @Autowired
    private CalorieTrackerService trackerService;


    @GetMapping("/")
    public String showIndexPage( Model model){
        FoodData foodData = new FoodData();
        Query query = new Query();
        model.addAttribute("foodData", foodData);
        model.addAttribute("query", query);
        return "index";
    }
    
    @PostMapping("/showFood")
    public String showFood(@RequestParam(required=true) String queryString, Model model){
        Query q = new Query();
        logger.info(queryString);
        q.setQueryString(queryString);
        Optional<FoodList> optFoodList = trackerService.getFoodList(q);
        optFoodList.get().listName();
        if(optFoodList.isEmpty()){
            model.addAttribute("foodList", new FoodList().getFoodList());
            return "showFood";
        }
        model.addAttribute("foodList", optFoodList.get().getFoodList());
            return "showFood";
    }

}
