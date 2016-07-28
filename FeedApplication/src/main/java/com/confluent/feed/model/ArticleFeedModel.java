package com.confluent.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleFeedModel {

    @JsonProperty(value = "article_guid")
    @NotBlank(message = "Please provide valid Article Guid")
    private String articleGuId;
    @JsonProperty(value = "feed_guid")
    @NotBlank(message = "Please provide valid Feed Guid")
    private String feedGuid;

    public String getArticleGuId() {
        return articleGuId;
    }

    public void setArticleGuId(String articleGuId) {
        this.articleGuId = articleGuId;
    }

    public String getFeedGuid() {
        return feedGuid;
    }

    public void setFeedGuid(String feedGuid) {
        this.feedGuid = feedGuid;
    }
}
