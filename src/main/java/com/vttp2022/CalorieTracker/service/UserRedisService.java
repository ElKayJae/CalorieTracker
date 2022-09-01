package com.vttp2022.CalorieTracker.service;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.vttp2022.CalorieTracker.model.User;

@Service
public class UserRedisService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRedisService.class);

    @Autowired
    @Qualifier("userRedisConfig")
    RedisTemplate<String, User> redisTemplate;


    public void save(final User user) {
        logger.info("saving...");
        redisTemplate.opsForValue().set(user.getUsername(),user);
        logger.info("saved");
    }

    public Optional<User> getByUsername(final String username) {
        logger.info("find user by username> " + username);
        try{User result = (User) redisTemplate.opsForValue().get(username);
            return Optional.of(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("Redis Service >>>>> no user found");
        return Optional.empty();
    }

    public String getList(){
        Set<String> userList = redisTemplate.keys("*");
        StringBuilder sb= new StringBuilder();
        for (String username:userList){
            sb.append(username +"\n");
        }
        String list = sb.substring(0, sb.length()-1).toString();
        return list;
    }
}
