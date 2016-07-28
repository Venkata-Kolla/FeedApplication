package com.confluent.feed.Util;

import com.confluent.feed.exception.ConfluentBusinessRuntimeException;
import com.confluent.feed.exception.FeedValidationError;
import com.confluent.feed.model.ArticleModel;
import com.confluent.feed.model.FeedModel;
import com.confluent.feed.model.SubscriptionModel;
import com.confluent.feed.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final Logger logger = LoggerFactory.getLogger(ValidatorUtil.class);

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validateArticle(ArticleModel article){
        UrlValidator validator = new UrlValidator();
        if(!validator.isValid(article.getArticleUrl())){
            FeedValidationError error = new FeedValidationError("400", "Please enter valid URL",
                    "Not Recoverable");
            logger.error(error.toString());
            throw new ConfluentBusinessRuntimeException(error);
        }
        return true;
    }

    public static boolean validateUser(UserModel userModel){
        if(!validateEmail(userModel.getEmail())) {
            FeedValidationError error = new FeedValidationError("400", "Please enter valid Email",
                    "Not Recoverable");
            logger.error(error.toString());
            throw new ConfluentBusinessRuntimeException(error);
        }
        return true;
    }

    public static boolean validateFeed(FeedModel feedModel){
        if(feedModel == null)
            return false;
        return true;
    }

    public static boolean validateEmail(String email){
        if(StringUtils.isNotBlank(email)) {
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;
    }

    public static boolean validateSubscriptionModel(SubscriptionModel subscriptionModel){
        if(StringUtils.isBlank(subscriptionModel.getUserGuid()) && StringUtils.isBlank(subscriptionModel.getUserName())){
            FeedValidationError error = new FeedValidationError("400", "Please provide user name or user guid",
                    "Not Recoverable");
            logger.error(error.toString());
            throw new ConfluentBusinessRuntimeException(error);
        }
        List<String> validFeedAttributes = getValidFeedAttributes();
        for(String s : validFeedAttributes) {
            if(StringUtils.equalsIgnoreCase(s, subscriptionModel.getFeedAttribute())){
                return true;
            }
        }
        return false;
    }

    private static List<String> getValidFeedAttributes(){
        return Arrays.asList(new String [] {"guid", "feed_name"});
    }
}
