package com.confluent.feed.service;

import com.confluent.feed.model.ArticleFeedModel;
import com.confluent.feed.model.ArticleModel;
import com.confluent.feed.model.FeedModel;

import java.util.List;

public interface FeedService {

    List<FeedModel> getAllFeeds();
    FeedModel save(FeedModel feedModel);
    FeedModel addArticleToFeed(String feedGuid, ArticleModel articleModel);
    FeedModel addArticleToFeedByGuid(ArticleFeedModel articleFeedModel);
    FeedModel getFeedByGuid(String feedGuid);
}
