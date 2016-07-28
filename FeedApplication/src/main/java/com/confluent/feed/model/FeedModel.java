package com.confluent.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(value = {"id"}, ignoreUnknown = true)
public class FeedModel {

    private Long id;

    @Size(max = 38)
    @JsonProperty(value = "guid")
    private String guid;
    @JsonProperty(value = "feed_name")
    @NotNull(message = "Please specify valid feed name")
    private String feedName;
    @JsonProperty(value = "category")
    @NotNull(message = "Please specify valid feed category")
    private FeedCategory feedCategory;
    @JsonProperty(value = "users")
    private List<UserModel> users;
    @JsonProperty(value = "articles")
    private List<ArticleModel> articles;

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

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public FeedCategory getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(FeedCategory feedCategory) {
        this.feedCategory = feedCategory;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }

    public List<ArticleModel> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleModel> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "FeedModel : {" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", feedName='" + feedName + '\'' +
                ", feedCategory=" + feedCategory +
                ", users=" + users +
                ", articles=" + articles +
                '}';
    }
}
