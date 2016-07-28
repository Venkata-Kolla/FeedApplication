package com.confluent.feed.config;

import com.confluent.feed.service.ArticleService;
import com.confluent.feed.service.FeedService;
import com.confluent.feed.service.UserService;
import com.confluent.feed.service.impl.ArticleServiceImpl;
import com.confluent.feed.service.impl.FeedServiceImpl;
import com.confluent.feed.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeedApplicationConfig {

    @Bean
    public UserService userService(){
        return new UserServiceImpl();
    }

    @Bean
    public FeedService feedService(){
        return new FeedServiceImpl();
    }

    @Bean
    public ArticleService articleService(){
        return new ArticleServiceImpl();
    }
}
