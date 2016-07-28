package com.confluent.feed.service;

import com.confluent.feed.model.ArticleModel;

import java.util.List;

public interface ArticleService {

    ArticleModel save(ArticleModel article);
    ArticleModel getArticleByGuid(String guid);
    List<ArticleModel> getAllArticles();
}
