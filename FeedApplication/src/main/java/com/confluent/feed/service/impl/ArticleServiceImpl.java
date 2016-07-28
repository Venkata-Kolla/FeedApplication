package com.confluent.feed.service.impl;

import com.confluent.feed.config.MapperConfig;
import com.confluent.feed.domain.Article;
import com.confluent.feed.exception.ConfluentBusinessRuntimeException;
import com.confluent.feed.exception.FeedValidationError;
import com.confluent.feed.model.ArticleModel;
import com.confluent.feed.repository.ArticleRepository;
import com.confluent.feed.service.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ArticleServiceImpl implements ArticleService{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public ArticleModel save(ArticleModel articleModel) {
        if(articleModel == null) {
            FeedValidationError validationError = new FeedValidationError("400", "Invalid ArticleModel Passed", "Recoverable");
            logger.error(validationError.toString());
            throw new ConfluentBusinessRuntimeException(validationError);
        }
        ModelMapper mapper = new ModelMapper();
        Article article = mapper.map(articleModel, Article.class);
        article.setGuid(UUID.randomUUID().toString());
        article = articleRepository.save(article);
        articleModel = mapper.map(article, ArticleModel.class);
        return articleModel;
    }

    @Override
    public ArticleModel getArticleByGuid(String guid) {
        ArticleModel articleModel = null;
        if(StringUtils.isNotBlank(guid)) {
            ModelMapper mapper = new ModelMapper();
            mapper.addMappings(MapperConfig.getArticlePorperties());
            Article article = articleRepository.findByGuid(guid);
            if(article != null)
                articleModel = mapper.map(article, ArticleModel.class);
        }
        return articleModel;
    }

    @Override
    public List<ArticleModel> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleModel> articleModels = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(MapperConfig.getArticlePorperties());
        articles.parallelStream().forEach(article -> {
            ArticleModel articleModel = mapper.map(article, ArticleModel.class);
            articleModels.add(articleModel);
        });
        return articleModels;
    }
}
