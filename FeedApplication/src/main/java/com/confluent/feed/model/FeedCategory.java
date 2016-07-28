package com.confluent.feed.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

public enum FeedCategory {
    TECHNOLOGY("Technology"),
    BUSINESS("Business"),
    Marketing("Marketing"),
    FOOD("Food"),
    NEWS("News"),
    FASHION("Fashion"),
    OTHER("Other")
    ;

    private String category;

    FeedCategory(String category) {
        this.category = category;
    }

    @JsonValue
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @JsonCreator
    public static FeedCategory fromValue(String value){
        FeedCategory[] feeds = FeedCategory.values();
        for(FeedCategory feed : feeds){
            if(StringUtils.equalsIgnoreCase(feed.getCategory(), value))
                return feed;
        }
        return null;
    }
}
