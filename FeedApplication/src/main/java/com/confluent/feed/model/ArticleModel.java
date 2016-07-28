package com.confluent.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(value = {"id"}, ignoreUnknown = true)
public class ArticleModel {

    private Long id;
    @JsonProperty(value = "guid")
    private String guid;
    @JsonProperty(value = "article_name")
    @NotNull(message = "Please specify valid article name")
    private String articleName;
    @JsonProperty(value = "article_url")
    private String articleUrl;
    @JsonProperty(value = "feeds")
    private List<FeedModel> feeds;

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

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public List<FeedModel> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<FeedModel> feeds) {
        this.feeds = feeds;
    }

    @Override
    public String toString() {
        return "ArticleModel : {" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", articleName='" + articleName + '\'' +
                ", articleUrl='" + articleUrl + '\'' +
                ", feeds=" + feeds +
                '}';
    }
}
