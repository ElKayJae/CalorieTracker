package com.vttp2022.CalorieTracker.controller;


import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vttp2022.CalorieTracker.model.DayObj;
import com.vttp2022.CalorieTracker.model.FoodListObj;
import com.vttp2022.CalorieTracker.model.Query;
import com.vttp2022.CalorieTracker.model.User;
import com.vttp2022.CalorieTracker.service.CalorieTrackerService;

@Controller
public class TrackerController {
    private static final Logger logger = LoggerFactory.getLogger(TrackerController.class);
    
    @Autowired
    private CalorieTrackerService trackerService;

    @Autowired
    private User user;

    @Autowired
    private DayObj dayObj;


    @GetMapping("/")
    public String showIndexPage( Model model){
        Query query = new Query();
       
        model.addAttribute("query", query);
        model.addAttribute("dayObj", dayObj);
        return "index";
    }
 
    @PostMapping("/showFood")
    public String showFood(@RequestParam(required=true) String queryString, @RequestParam(required=true) String day, Model model){
        logger.info(day);
        Query q = new Query();
        logger.info(queryString);
        q.setQueryString(queryString);
        dayObj.setDay(day);
        Optional<FoodListObj> optFoodListObj = trackerService.getFoodList(q);
        optFoodListObj.get().listName();
        model.addAttribute("dayObj", dayObj);
        if(optFoodListObj.isEmpty()){
            model.addAttribute("foodListObj", new FoodListObj());
            return "showFood";
        }
        model.addAttribute("foodListObj", optFoodListObj.get());
            return "showFood";
    }

    @PostMapping("/save")
    public String saveFood(@ModelAttribute FoodListObj foodListObj, @ModelAttribute DayObj dayObj, Model model){
        model.addAttribute("foodListObj", foodListObj);
        model.addAttribute("dayObj", dayObj);
    
        return "save";
    }

}
