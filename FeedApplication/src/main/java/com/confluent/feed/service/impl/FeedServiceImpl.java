package com.confluent.feed.service.impl;

import com.confluent.feed.Util.ValidatorUtil;
import com.confluent.feed.config.MapperConfig;
import com.confluent.feed.domain.Article;
import com.confluent.feed.domain.Feed;
import com.confluent.feed.domain.User;
import com.confluent.feed.exception.ConfluentBusinessRuntimeException;
import com.confluent.feed.exception.FeedValidationError;
import com.confluent.feed.model.ArticleFeedModel;
import com.confluent.feed.model.ArticleModel;
import com.confluent.feed.model.FeedModel;
import com.confluent.feed.model.UserModel;
import com.confluent.feed.repository.ArticleRepository;
import com.confluent.feed.repository.FeedRepository;
import com.confluent.feed.repository.UserRepository;
import com.confluent.feed.service.FeedService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FeedServiceImpl implements FeedService{

    private  final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<FeedModel> getAllFeeds() {
        List<Feed> feeds = feedRepository.findAll();
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(MapperConfig.getFeedPorperties());
        List<FeedModel> feedList = new ArrayList<>();
        feeds.stream().forEach(feed -> {
            feedList.add(mapper.map(feed, FeedModel.class));
        });
        return feedList;
    }

    @Override
    public FeedModel getFeedByGuid(String feedGuid) {
        FeedModel feedModel = null;
        if(StringUtils.isNotBlank(feedGuid)) {
            Feed feed = getFeed(feedGuid);
            feedModel = getFeedModel(feed);
        }
        return feedModel;
    }

    @Override
    @Transactional
    public FeedModel save(@Valid FeedModel feedModel) {
        if(ValidatorUtil.validateFeed(feedModel)){
            ModelMapper mapper = new ModelMapper();
            Feed feed = mapper.map(feedModel, Feed.class);
            List<UserModel> users = feedModel.getUsers();
            List<ArticleModel> articles = feedModel.getArticles();
            List<User> userList = new ArrayList<>();
            List<Article> articleList = new ArrayList<>();
            if(users != null && users.size() > 0){
                saveUsers(mapper, users, userList);
                feed.setUsers(userList);
            }
            if(articles != null && articles.size() > 0){
                saveArticles(mapper, articles, articleList);
                feed.setArticles(articleList);
            }
            feed.setGuid(UUID.randomUUID().toString());
            feed = feedRepository.save(feed);
            feedModel = mapper.map(feed, FeedModel.class);
        }
        return feedModel;
    }

    @Override
    @Transactional
    public FeedModel addArticleToFeed(String feedGuid, @Valid ArticleModel articleModel) {
        FeedModel feedModel = null;
        if(StringUtils.isNotBlank(feedGuid) && articleModel != null){
            Feed feed = getFeed(feedGuid);
            if(ValidatorUtil.validateArticle(articleModel)) {
                ModelMapper mapper = new ModelMapper();
                Article article = mapper.map(articleModel, Article.class);
                article.setGuid(UUID.randomUUID().toString());
                article = articleRepository.save(article);
                List<Article> articles = feed.getArticles();
                if(articles == null)
                    articles = new ArrayList<>();
                if(!checkForExistingArticle(articles, article)) {
                    articles.add(article);
                } else {
                    String message = "Article "+article.getArticleName()+" is already added to feed "+feed.getFeedName();
                    FeedValidationError error = new FeedValidationError("400", message, "UnRecoverable");
                    logger.error(message);
                    throw new ConfluentBusinessRuntimeException(error);
                }
                feed.setArticles(articles);
                feed = feedRepository.save(feed);
                feedModel = getFeedModel(feed);
            }
        }
        return feedModel;
    }

    @Override
    @Transactional
    public FeedModel addArticleToFeedByGuid(@Valid ArticleFeedModel articleFeedModel) {
        String feedGuid = articleFeedModel.getFeedGuid();
        String articleGuid = articleFeedModel.getArticleGuId();
        FeedModel feedModel = null;
        if(StringUtils.isNotBlank(feedGuid) && StringUtils.isNotBlank(articleGuid)){
            Feed feed = getFeed(feedGuid);
            Article article = getArticle(articleGuid);
            List<Article> articles = feed.getArticles();
            if(articles == null)
                articles = new ArrayList<>();
            if(!checkForExistingArticle(articles, article)) {
                articles.add(article);
            } else {
                String message = "Article "+article.getArticleName()+" is already added to feed "+feed.getFeedName();
                FeedValidationError error = new FeedValidationError("400", message, "UnRecoverable");
                logger.error(message);
                throw new ConfluentBusinessRuntimeException(error);
            }
            feed.setArticles(articles);
            feed = feedRepository.save(feed);
            feedModel = getFeedModel(feed);
        }
        return feedModel;
    }

    private FeedModel getFeedModel(Feed feed) {
        ModelMapper feedMapper  = new ModelMapper();
        feedMapper.addMappings(MapperConfig.getFeedPorperties());
        List<ArticleModel> articleModels = new ArrayList<>();
        ModelMapper articleMapper  = new ModelMapper();
        articleMapper.addMappings(MapperConfig.getArticlePorperties());
        if(feed.getArticles() != null) {
            feed.getArticles().parallelStream().forEach(article ->
                articleModels.add(articleMapper.map(article, ArticleModel.class))
            );
        }
        FeedModel feedModel = feedMapper.map(feed, FeedModel.class);
        feedModel.setArticles(articleModels);
        return feedModel;
    }

    private void saveArticles(ModelMapper mapper, List<ArticleModel> articles, List<Article> articleList) {
        if(articles != null && articles.size() > 0){
            articles.stream().forEach(articleModel ->{
                if(ValidatorUtil.validateArticle(articleModel)) {
                    Article article = mapper.map(articleModel, Article.class);
                    article.setGuid(UUID.randomUUID().toString());
                    article = articleRepository.save(article);
                    articleList.add(article);
                }
            });
        }
    }

    protected void saveUsers(ModelMapper mapper, List<UserModel> users, List<User> userList) {
        if(users != null && users.size() >0) {
            users.stream().forEach(userModel -> {
                if(ValidatorUtil.validateUser(userModel)) {
                    User user = mapper.map(userModel, User.class);
                    user.setGuid(UUID.randomUUID().toString());
                    user = userRepository.save(user);
                    userList.add(user);
                }
            });
        }
    }

    protected Feed getFeed(String feedGuid) {
        Feed feed = feedRepository.findByGuid(feedGuid);
        if(feed == null){
            String message = "Unable to find the feed for guid : "+feedGuid;
            FeedValidationError error = new FeedValidationError("400", message, "UnRecoverable");
            logger.error(message);
            throw new ConfluentBusinessRuntimeException(error);
        }
        return feed;
    }

    protected Article getArticle(String articleGuid) {
        Article article = articleRepository.findByGuid(articleGuid);
        if(article == null){
            String message = "Unable to find the article for guid : "+articleGuid;
            FeedValidationError error = new FeedValidationError("400", message, "UnRecoverable");
            logger.error(message);
            throw new ConfluentBusinessRuntimeException(error);
        }
        return article;
    }

    protected boolean checkForExistingArticle(List<Article> articles, Article article){
        if(articles.size() == 0)
            return false;
        long count = articles.parallelStream().filter(a -> StringUtils.equals(article.getGuid(), a.getGuid())).count();
        return count >=1 ? true : false;
    }

}
