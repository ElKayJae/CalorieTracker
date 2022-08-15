package com.vttp2022.CalorieTracker.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.vttp2022.CalorieTracker.model.FoodList;
import com.vttp2022.CalorieTracker.model.Query;

@Service
public class CalorieTrackerService {
    private static final Logger logger = LoggerFactory.getLogger(CalorieTrackerService.class);

    private static final String URL = "https://api.calorieninjas.com/v1/nutrition";

    public Optional<FoodList> getFoodList(Query q){
        String apiKey = System.getenv("CALORIE_NINJA_API_KEY");

        String requestUrl = UriComponentsBuilder.fromUriString(URL)
                            .queryParam("query", q.getQueryString())
                            .toUriString()
                            .replace("%20", " ");
                            
        // String requestUrl = "https://api.calorieninjas.com/v1/nutrition?query=apple orange";

        logger.info(requestUrl);
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Api-Key", apiKey);
            HttpEntity request = new HttpEntity<>(headers);
            resp = template.exchange(requestUrl, 
                                    HttpMethod.GET, 
                                    request, 
                                    String.class,
                                    1);
            FoodList foodList = FoodList.createJson(resp.getBody());
            logger.info("response body >>>>>>>>> " + resp.getBody());
            return Optional.of(foodList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("response empty");
        return Optional.empty();

    }

}
