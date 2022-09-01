package com.vttp2022.CalorieTracker.controller;


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
    private UserRedisService redisService;
    

    @RequestMapping("/")
    public String showIndexPage(Model model){
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam(required=true) String username, Model model){
        
        Optional<User> optUser = redisService.getByUsername(username);
        User currUser = new User();

        if (optUser.isEmpty()){
            currUser.setUsername(username);
        } else {
            currUser = optUser.get();
        }
        logger.info("login username >>>>>>" + currUser.getUsername());

        DayObj dayObj = new DayObj();
        String currDay = dayObj.getDay();
        FoodListObj currDayListObj= currUser.getFoodListObj(currDay);
        redisService.save(currUser);
        
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("currUser", currUser);
        model.addAttribute("dayObj", dayObj);

        return "main";
    }
 
    @PostMapping("/search/{username}")
    public String showFood( @PathVariable(name="username", required=true) String username, 
    @RequestParam(required=true) String queryString, @RequestParam(required=true) String day, Model model){

        logger.info("Username in /search >>>> " +username+" day>>>>" + day);
        User currUser = redisService.getByUsername(username).get();
        Optional<FoodListObj> optFoodListObj = trackerService.getFoodList(queryString);
        DayObj dayObj = new DayObj(day);

        model.addAttribute("dayObj", dayObj);
        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", optFoodListObj.get());
        
        return "search";
    }
    
    @PostMapping(value="/add/{username}/{day}", params = "addSelected")
    public String addFood(@PathVariable(name="username", required=true) String username,
    @PathVariable(required=true) String day, @ModelAttribute FoodListObj foodListObj, Model model){

        logger.info("add selected >>>>>>>>>> " +username+" day>>>>" + day);
        User currUser = redisService.getByUsername(username).get();
        DayObj dayObj = new DayObj(day);
        currUser.addFoodFromListObj(day, foodListObj);
        FoodListObj currDayListObj= currUser.getFoodListObj(day);
        redisService.save(currUser);
        
        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }
    
    @PostMapping(value="/add/{username}/{day}", params = "addAll")
    public String addAll(@PathVariable(name="username", required=true) String username,
    @PathVariable(required=true) String day, @ModelAttribute FoodListObj foodListObj, Model model){

        User currUser = redisService.getByUsername(username).get();
        DayObj dayObj = new DayObj(day);
        currUser.addAllFromListObj(day, foodListObj);
        redisService.save(currUser);
        FoodListObj currDayListObj=currUser.getFoodListObj(day);

        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }

    @PostMapping("/edit/{username}/{day}")
    public String edit(@PathVariable(name="username", required=true) String username,  
    @PathVariable(required=true) String day, Model model){

        User currUser = redisService.getByUsername(username).get();
        FoodListObj currDayListObj = currUser.getFoodListObj(day);
        DayObj dayObj = new DayObj(day);
        logger.info("edit >>>>>>>>>>>>> " +username+" day>>>>" + day);

        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "edit";
    }
    
    @PostMapping(value = "/del/{username}/{day}", params = "delSelected")
    public String delete(@PathVariable(name="username", required=true) String username,  @PathVariable(required=true) String day,
    @ModelAttribute FoodListObj foodListObj, Model model){
        
        User currUser = redisService.getByUsername(username).get();
        DayObj dayObj = new DayObj(day);
        currUser.delFoodFromListObj(day, foodListObj);
        FoodListObj currDayListObj = currUser.getFoodListObj(day);
        redisService.save(currUser);
        logger.info("del selected >>>>>>>>>>>>> " +username+" day>>>>" + day);

        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }
    
    @PostMapping(value = "/del/{username}/{day}", params = "delAll")
    public String deleteAll(@PathVariable(name="username", required=true) String username,  @PathVariable(required=true) String day,
    @ModelAttribute FoodListObj foodListObj, Model model){

        User currUser = redisService.getByUsername(username).get();
        currUser.delDay(day);
        DayObj dayObj = new DayObj();
        String currDay = dayObj.getDay();
        FoodListObj currDayListObj=currUser.getFoodListObj(currDay);
        redisService.save(currUser);
        logger.info("del all >>>>>>>>>>>> " +username+" day>>>>" + day);

        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }
    

    @GetMapping("/selectDay/{username}/{day}")
    public String selectDay( @PathVariable(name="username", required=true) String username, 
    @PathVariable(required=true) String day, Model model){

        User currUser = redisService.getByUsername(username).get();
        FoodListObj currDayListObj=currUser.getFoodListObj(day);
        DayObj dayObj = new DayObj(day);
        logger.info("select >>>>>>>>>>>> " +username+" day>>>>" + day);

        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }
    
    @GetMapping("/back/{username}/{day}")
    public String backto( @PathVariable(name="username", required=true) String username, 
    @PathVariable(required=true) String day, Model model){
        User currUser = redisService.getByUsername(username).get();
        DayObj dayObj = new DayObj(day);
        FoodListObj currDayListObj = currUser.getFoodListObj(day);
        logger.info("back >>>>>>>>>>>>> " +username+" day>>>>" + day);
        model.addAttribute("currUser", currUser);
        model.addAttribute("foodListObj", currDayListObj);
        model.addAttribute("dayObj", dayObj);
        
        return "main";
    }
    

}
