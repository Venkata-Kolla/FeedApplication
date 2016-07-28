package com.confluent.feed.config;


import com.confluent.feed.domain.Article;
import com.confluent.feed.domain.Feed;
import com.confluent.feed.domain.User;
import com.confluent.feed.model.ArticleModel;
import com.confluent.feed.model.FeedModel;
import com.confluent.feed.model.UserModel;
import org.modelmapper.PropertyMap;

public class MapperConfig {

    public static PropertyMap<User, UserModel> getUserProperties(){
        PropertyMap<User, UserModel> map = new PropertyMap<User, UserModel>() {
            protected void configure() {
                skip(source.getFeeds(), destination.getFeeds());
            }
        };
        return map;
    }

    public static PropertyMap<Feed, FeedModel> getFeedPorperties(){
        PropertyMap<Feed, FeedModel> map = new PropertyMap<Feed, FeedModel>() {
            protected void configure() {
                skip(source.getArticles(), destination.getArticles());
                skip(source.getUsers(), destination.getUsers());
            }
        };
        return map;
    }

    public static PropertyMap<Article, ArticleModel> getArticlePorperties(){
        PropertyMap<Article, ArticleModel> map = new PropertyMap<Article, ArticleModel>() {
            protected void configure() {
                skip(source.getFeeds(), destination.getFeeds());
            }
        };
        return map;
    }
}
