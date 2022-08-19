package com.vttp2022.CalorieTracker.model;

import org.springframework.stereotype.Component;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@Component("query")
public class Query {
    // private static final Logger logger = LoggerFactory.getLogger(Query.class);

    public String queryString;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }


}
