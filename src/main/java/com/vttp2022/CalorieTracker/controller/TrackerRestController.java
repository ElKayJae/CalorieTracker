package com.vttp2022.CalorieTracker.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.vttp2022.CalorieTracker.model.User;
import com.vttp2022.CalorieTracker.service.CalorieTrackerService;
import com.vttp2022.CalorieTracker.service.UserRedisService;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
public class TrackerRestController {
    private static final Logger logger = LoggerFactory.getLogger(TrackerRestController.class);

    @Autowired
    CalorieTrackerService service;

    @Autowired
    UserRedisService redisService;

    @Autowired
    @Qualifier ("userRedisConfig")
    RedisTemplate<String, User> redisTemplate;

    @GetMapping (path="/user/{username}", consumes =MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE) 
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
     
        logger.info("get" + username);
            Optional<User> optUser = redisService.getByUsername(username);

            if(optUser.isEmpty()){
            JsonObject errJson = Json.createObjectBuilder()
            .add("error","User " + username + " not found")
            .build();
            return ResponseEntity.status(404).body(errJson.toString());
            
        }
        
        return ResponseEntity.ok(optUser.get());
    }

    @GetMapping (path="/userList", consumes =MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getList(){
        String userList = redisService.getList();
       
        return ResponseEntity.ok(userList);
    }
}
