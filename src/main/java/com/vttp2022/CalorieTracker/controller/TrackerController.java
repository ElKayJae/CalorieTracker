package com.vttp2022.CalorieTracker.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vttp2022.CalorieTracker.model.DayObj;
import com.vttp2022.CalorieTracker.model.FoodData;
import com.vttp2022.CalorieTracker.model.FoodListObj;
import com.vttp2022.CalorieTracker.model.User;
import com.vttp2022.CalorieTracker.service.CalorieTrackerService;
import com.vttp2022.CalorieTracker.service.UserRedisService;

@Controller
public class TrackerController {
    private static final Logger logger = LoggerFactory.getLogger(TrackerController.class);
    
    @Autowired
    private CalorieTrackerService trackerService;

    @Autowired
    private User currUser;

    @Autowired
    private DayObj dayObj;

    @Autowired
    UserRedisService redisService;

    @Autowired
    FoodListObj currDayListObj;

    String currDay;


    @RequestMapping("/")
    public String showIndexPage(Model model){
        currUser = new User();
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam(required=true) String username, Model model){
        
        Optional<User> optUser = redisService.getByUsername(username);
        if (optUser.isEmpty()){
            currUser = new User();
            currUser.setUsername(username);
        } else {
            currUser = optUser.get();
        }
        if (currUser.getDayMap().containsKey(dayObj.day)){
            currDayListObj= currUser.getDayMap().get(dayObj.day).getDailyFood();
            model.addAttribute("foodListObj", currDayListObj);
        } else {
            model.addAttribute("foodListObj", "emptyList");
        }
        dayObj.setDailyFood(currDayListObj);
        currDay= dayObj.day;
        logger.info(dayObj.day);
        model.addAttribute("currUser", currUser);
        model.addAttribute("dayObj", dayObj);

        return "main";
    }
 
    
    @PostMapping(value="/add", params = "addSelected")
    public String addFood(@ModelAttribute FoodListObj foodListObj, @ModelAttribute DayObj dayObj, Model model){
        for(FoodData foodData : foodListObj.getFoodList()){
            if (foodData.isChecked()){
                foodData.setChecked(false);
                currDayListObj.getFoodList().add(foodData);
                logger.info(foodData.getName());
            }
        }
        dayObj.setDailyFood(currDayListObj);
        currUser.addDay(dayObj);
        redisService.save(currUser);
        
        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }

    @PostMapping(value="/add", params = "addAll")
    public String addAll(@ModelAttribute FoodListObj foodListObj, @ModelAttribute DayObj dayObj, Model model){
        for(FoodData foodData : foodListObj.getFoodList()){
                foodData.setChecked(false);
                currDayListObj.getFoodList().add(foodData);
            }

        dayObj.setDailyFood(currDayListObj);
        currUser.addDay(dayObj);
        redisService.save(currUser);
        
        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }

    @PostMapping("/del")
    public String delete(@ModelAttribute FoodListObj foodListObj, @ModelAttribute DayObj dayObj, Model model){

        int counter=0;
        for(int i=0; i < foodListObj.getFoodList().size(); i++){
            if (foodListObj.getFoodList().get(i).isChecked()){
                foodListObj.getFoodList().get(i).setChecked(false);
                currDayListObj.getFoodList().remove(i+counter);
                counter--;
            }
        }

        dayObj.setDay(currDay);
        
        if(currDayListObj.getFoodList().size()==0){
            currUser.delDay(dayObj);
            dayObj.newDay();
            currDayListObj=currUser.getDayMap().get(dayObj.day).getDailyFood();
            currDay = dayObj.getDay();
            logger.info("list size" + currDayListObj.getFoodList().size());
        } else {
            dayObj.setDailyFood(currDayListObj);
            currUser.addDay(dayObj);
        }
        redisService.save(currUser);
        logger.info("del day >>>>>>>>>" + dayObj.day);
        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }
    
    @PostMapping("/edit")
    public String edit(@ModelAttribute DayObj dayObj, Model model){
        logger.info("back day >>>>>>>>>" + dayObj.day);
        dayObj.setDay(currDay);
        logger.info("back day >>>>>>>>>" + dayObj.day);
        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "edit";
    }

    @GetMapping("/selectDay/{date}")
    public String selectDay( @PathVariable(name="date", required=true) String date, 
    @ModelAttribute DayObj dayObj, Model model){
        
        currDayListObj=currUser.getDayMap().get(date).getDailyFood();
        dayObj.setDay(date);
        currDay= dayObj.day;
        logger.info(dayObj.day);
        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }
    
    @GetMapping("/back")
    public String backto(@ModelAttribute FoodListObj foodListObj, @ModelAttribute DayObj dayObj, Model model){
        logger.info("back day >>>>>>>>>" + dayObj.day);
        dayObj.setDay(currDay);
        logger.info("back day >>>>>>>>>" + dayObj.day);
        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }
    

    @PostMapping("/showFood")
    public String showFood(@RequestParam(required=true) String queryString, @RequestParam(required=true) String day, Model model){
        logger.info(day);
        logger.info(dayObj.day);
    
        if (!dayObj.getDay().equals(day)){
            logger.info("new day");
            if(currUser.getDayMap().containsKey(day)){
                currDayListObj = currUser.getDayMap().get(day).dailyFood;
            } else {
                currDayListObj = new FoodListObj();
            }
            dayObj.setDay(day);
            }
    
        logger.info(dayObj.day);
        currDay= dayObj.day;
        Optional<FoodListObj> optFoodListObj = trackerService.getFoodList(queryString);
        
    
        model.addAttribute("dayObj", dayObj);
        model.addAttribute("currUser", currUser);
        if(optFoodListObj.isEmpty()){
            model.addAttribute("foodListObj", new FoodListObj());
            return "showFood";
        }
        model.addAttribute("foodListObj", optFoodListObj.get());
        logger.info("add day >>>>>>>>>" + dayObj.day);
            return "showFood";
    }
}
