package com.confluent.feed.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class SubscriptionModel implements Serializable {

    @JsonProperty(value = "feed_attribute", required = true)
    @NotBlank(message = "please provide valid attribute value")
    private String feedAttribute;
    @JsonProperty(value = "feed_attr_value", required = true)
    @NotBlank(message = "Please provide valid attribute value")
    private String feedAttrValue;
    @JsonProperty(value = "user_name")
    private String userName;
    @JsonProperty(value = "user_guid")
    private String userGuid;

    public String getFeedAttribute() {
        return feedAttribute;
    }

    public void setFeedAttribute(String feedAttribute) {
        this.feedAttribute = feedAttribute;
    }

    public String getFeedAttrValue() {
        return feedAttrValue;
    }

    public void setFeedAttrValue(String feedAttrValue) {
        this.feedAttrValue = feedAttrValue;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    @Override
    public String toString() {
        return "SubscriptionModel : {" +
                "feedAttribute='" + feedAttribute + '\'' +
                ", feedAttrValue='" + feedAttrValue + '\'' +
                ", userName='" + userName + '\'' +
                ", userGuid='" + userGuid + '\'' +
                '}';
    }
}
