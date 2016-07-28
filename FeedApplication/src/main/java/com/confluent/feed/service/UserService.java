package com.confluent.feed.service;

import com.confluent.feed.model.ArticleModel;
import com.confluent.feed.model.FeedModel;
import com.confluent.feed.model.SubscriptionModel;
import com.confluent.feed.model.UserModel;

import java.util.List;

public interface UserService {

    UserModel save(UserModel userModel);
    List<UserModel> getAllUsers();
    Boolean subscribeFeed(SubscriptionModel subscriptionModel);
    Boolean unSubscribeFeed(SubscriptionModel subscriptionModel);
    List<FeedModel> getAllFeeds(String guid);
    List<ArticleModel> getAllArticles(String guid);
    UserModel getUser(String guid);
}
