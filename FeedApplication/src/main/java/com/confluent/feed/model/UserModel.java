package com.confluent.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(value = {"id"}, ignoreUnknown = true)
public class UserModel implements Serializable{

    private Long id;

    @Size(max = 38)
    @JsonProperty(value = "guid")
    private String guid;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    @JsonProperty(value = "user_name")
    @NotBlank(message = "User Name Cannot be blank")
    private String userName;
    @JsonProperty(value = "email")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please provide valid email")
    private String email;
    @JsonProperty(value = "feeds")
    private List<FeedModel> feeds = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<FeedModel> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<FeedModel> feeds) {
        this.feeds = feeds;
    }

    @Override
    public String toString() {
        return "UserModel : {" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", feeds=" + feeds +
                '}';
    }
}
