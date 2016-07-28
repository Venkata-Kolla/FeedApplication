package com.confluent.feed.controller;

import com.confluent.feed.Util.ValidatorUtil;
import com.confluent.feed.model.ArticleFeedModel;
import com.confluent.feed.model.ArticleModel;
import com.confluent.feed.model.FeedModel;
import com.confluent.feed.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping(value = "feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<FeedModel>> getAllFeeds(){
        return new ResponseEntity<>(feedService.getAllFeeds(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{feedGuid}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<FeedModel> getFeed(@PathVariable("feedGuid") @NotNull @Valid String feedGuid){
        return new ResponseEntity<>(feedService.getFeedByGuid(feedGuid), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> saveFeed(@Valid @RequestBody FeedModel feedModel) {
        if(ValidatorUtil.validateFeed(feedModel)){
            return new ResponseEntity<>(feedService.save(feedModel), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Request", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{feedGuid}/article/add", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> addArticle(@PathVariable("feedGuid") @NotNull @Valid String feedGuid,
                                        @Valid @RequestBody ArticleModel articleModel) {

        if(ValidatorUtil.validateArticle(articleModel)){
            return new ResponseEntity<>(feedService.addArticleToFeed(feedGuid, articleModel), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Request", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/article/add", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> addArticleByGuid(@Valid @RequestBody ArticleFeedModel articleFeedModel) {
        return new ResponseEntity<>(feedService.addArticleToFeedByGuid(articleFeedModel), HttpStatus.OK);
    }
}
