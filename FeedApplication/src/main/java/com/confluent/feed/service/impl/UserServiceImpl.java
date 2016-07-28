package com.confluent.feed.service.impl;

import com.confluent.feed.Util.ValidatorUtil;
import com.confluent.feed.config.MapperConfig;
import com.confluent.feed.domain.Article;
import com.confluent.feed.domain.Feed;
import com.confluent.feed.domain.User;
import com.confluent.feed.exception.ConfluentBusinessRuntimeException;
import com.confluent.feed.exception.FeedValidationError;
import com.confluent.feed.model.ArticleModel;
import com.confluent.feed.model.FeedModel;
import com.confluent.feed.model.SubscriptionModel;
import com.confluent.feed.model.UserModel;
import com.confluent.feed.repository.FeedRepository;
import com.confluent.feed.repository.UserRepository;
import com.confluent.feed.service.UserService;
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
import java.util.ListIterator;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private  final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Override
    @Transactional
    public UserModel save(@Valid UserModel userModel) {
        if(userModel != null && ValidatorUtil.validateUser(userModel)) {
            ModelMapper mapper = new ModelMapper();
            User user = mapper.map(userModel, User.class);
            user.setGuid(UUID.randomUUID().toString());
            List<Feed> feeds = new ArrayList<>();
            List<FeedModel> feedModels = userModel.getFeeds();
            if(feedModels != null && feedModels.size() >0){
                feedModels.stream().forEach(feedModel -> {
                    Feed feed = mapper.map(feedModel, Feed.class);
                    feed = feedRepository.save(feed);
                    feeds.add(feed);
                });
                user.setFeeds(feeds);
            }

            user = userRepository.save(user);
            userModel = mapper.map(user, UserModel.class);
        }
        return userModel;
    }

    @Override
    public List<UserModel> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserModel> userList = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(MapperConfig.getUserProperties());
        users.parallelStream().forEach(user -> {
            UserModel userModel = mapper.map(user, UserModel.class);
            userList.add(userModel);
        });
        return userList;
    }

    @Override
    @Transactional
    public Boolean subscribeFeed(SubscriptionModel subscriptionModel) {
        User existingUser = null;
        if(subscriptionModel != null && ValidatorUtil.validateSubscriptionModel(subscriptionModel)) {
            String userName = subscriptionModel.getUserName();
            String guid = subscriptionModel.getUserGuid();
            User user = getUser(userName, guid);
            Feed feed = getFeed(subscriptionModel);
            List<Feed> feeds = user.getFeeds();
            if(feeds == null)
                feeds = new ArrayList<>();
            if(!checkForExistingFeed(feeds, feed)) {
                feeds.add(feed);
            } else {
                String message = "Feed "+feed.getFeedName()+" is already subscribed by user "+user.getUserName();
                FeedValidationError error = new FeedValidationError("400", message, "UnRecoverable");
                logger.error(message);
                throw new ConfluentBusinessRuntimeException(error);
            }
            user.setFeeds(feeds);
            existingUser = userRepository.save(user);
        }
        return existingUser != null ? true : false;
    }

    @Override
    @Transactional
    public Boolean unSubscribeFeed(SubscriptionModel subscriptionModel) {
        User existingUser = null;
        if(subscriptionModel != null && ValidatorUtil.validateSubscriptionModel(subscriptionModel)) {
            String userName = subscriptionModel.getUserName();
            String guid = subscriptionModel.getUserGuid();
            User user = getUser(userName, guid);
            Feed inputFeed = getFeed(subscriptionModel);
            List<Feed> feeds = user.getFeeds();
            if(feeds != null) {
                ListIterator<Feed> feedListIterator = feeds.listIterator();
                boolean subscribe = false;
                while (feedListIterator.hasNext()){
                    Feed feed = feedListIterator.next();
                    if(StringUtils.equalsIgnoreCase(feed.getGuid(), inputFeed.getGuid())){
                        subscribe = true;
                        feedListIterator.remove();
                    }
                }
                if(!subscribe){
                    String attr = userName == null?guid:userName;
                    String message = "User : "+attr+" did not subscribe to "+subscriptionModel.getFeedAttrValue()+". Please" +
                            " check subscription status ";
                    FeedValidationError error = new FeedValidationError("400", message, "UnRecoverable");
                    logger.error(message);
                    throw new ConfluentBusinessRuntimeException(error);
                }
                user.setFeeds(feeds);
                existingUser = userRepository.save(user);
            } else {
                String attr = userName == null?guid:userName;
                String message = "User : "+attr+" does not have any subscriptions";
                FeedValidationError error = new FeedValidationError("400", message, "UnRecoverable");
                logger.error(message);
                throw new ConfluentBusinessRuntimeException(error);
            }
        }
        return existingUser != null ? true : false;
    }

    @Override
    public List<FeedModel> getAllFeeds(String guid) {
        List<FeedModel> feedModelList = new ArrayList<>();
        if(StringUtils.isNotBlank(guid)){
            List<Feed> feeds = getFeeds(guid);
            ModelMapper mapper = new ModelMapper();
            mapper.addMappings(MapperConfig.getFeedPorperties());
            feeds.parallelStream().forEach(feed -> feedModelList.add(mapper.map(feed, FeedModel.class)));
        }
        return feedModelList;
    }

    protected List<Feed> getFeeds(String guid) {
        User user = userRepository.findByGuid(guid);
        if(user == null){
            String message = "Unable to find the user with the guid : "+guid;
            FeedValidationError error = new FeedValidationError("404", message, "UnRecoverable");
            logger.error(message);
            throw new ConfluentBusinessRuntimeException(error);
        }
        return user.getFeeds();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleModel> getAllArticles(String guid) {
        List<ArticleModel> articleModels = new ArrayList<>();
        if(StringUtils.isNotBlank(guid)){
            List<Feed> feeds = getFeeds(guid);
            ModelMapper mapper = new ModelMapper();
            mapper.addMappings(MapperConfig.getArticlePorperties());
            if(feeds != null && feeds.size() >0 ){
                feeds.parallelStream().forEach(feed -> {
                    List<Article> articles = feed.getArticles();
                    if(articles != null && articles.size() >0 ){
                        articles.parallelStream().forEach(article -> {
                            ArticleModel articleModel = mapper.map(article, ArticleModel.class);
                            articleModels.add(articleModel);
                        });
                    }
                });
            }
        }
        return articleModels;
    }

    @Override
    public UserModel getUser(String guid) {
        UserModel userModel = null;
        if(StringUtils.isNotBlank(guid)){
            User user = getUser(null, guid);
            ModelMapper mapper = new ModelMapper();
            mapper.addMappings(MapperConfig.getUserProperties());
            userModel = mapper.map(user, UserModel.class);
        }
        return userModel;
    }

    protected User getUser(String userName, String guid) {
        User user = null;
        if(StringUtils.isNotBlank(userName)){
            user = userRepository.findByUserName(userName);
        } else if(StringUtils.isNotBlank(guid)){
            user = userRepository.findByGuid(guid);
        }
        if(user == null){
            String message = "Unable to find the user with user name : "+userName+" or guid : "+guid;
            FeedValidationError error = new FeedValidationError("400", message, "UnRecoverable");
            logger.error(message);
            throw new ConfluentBusinessRuntimeException(error);
        }
        return user;
    }

    protected Feed getFeed(SubscriptionModel subscriptionModel) {
        Feed feed = null;
        if(StringUtils.isNotBlank(subscriptionModel.getFeedAttribute())){
            if(StringUtils.equalsIgnoreCase(subscriptionModel.getFeedAttribute(), "guid")){
                feed = feedRepository.findByGuid(subscriptionModel.getFeedAttrValue());
            } else {
                feed = feedRepository.findByFeedName(subscriptionModel.getFeedAttrValue());
            }
            if(feed == null){
                String message = "Unable to find feed with "+subscriptionModel.getFeedAttribute()+
                        " and value "+subscriptionModel.getFeedAttrValue();
                FeedValidationError error = new FeedValidationError("400", message, "UnRecoverable");
                logger.error(message);
                throw new ConfluentBusinessRuntimeException(error);
            }
        }
        return feed;
    }

    protected boolean checkForExistingFeed(List<Feed> feeds, Feed feed){
        if(feeds.size() == 0)
            return false;
        long count = feeds.parallelStream().filter(f -> StringUtils.equals(feed.getGuid(), f.getGuid())).count();
        return count >=1 ? true : false;
    }
}
