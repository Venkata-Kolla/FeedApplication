package com.confluent.feed.controller;


import com.confluent.feed.Util.ValidatorUtil;
import com.confluent.feed.model.SubscriptionModel;
import com.confluent.feed.model.UserModel;
import com.confluent.feed.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<UserModel>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{userGuid}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserModel> getUser(@PathVariable("userGuid") @NotNull @Valid String userGuid){
        return new ResponseEntity<>(userService.getUser(userGuid), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserModel userModel) {
        if(ValidatorUtil.validateUser(userModel)){
            return new ResponseEntity<>(userService.save(userModel), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Request", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> subscribeToFeed(@Valid @RequestBody SubscriptionModel subscriptionModel){
        String message;
        String user;
        if(userService.subscribeFeed(subscriptionModel)){
            user = subscriptionModel.getUserName() == null?subscriptionModel.getUserGuid() : subscriptionModel.getUserName();
            message = "User : "+user+ " successfully subscribed to feed";
        } else {
            user = subscriptionModel.getUserName() == null?subscriptionModel.getUserGuid() : subscriptionModel.getUserName();
            message = "User : "+user+ " failed to subscribed to feed";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @RequestMapping(value = "/unSubscribe", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> unSubscribeToFeed(@Valid @RequestBody SubscriptionModel subscriptionModel){
        String message;
        String user;
        if(userService.unSubscribeFeed(subscriptionModel)){
            user = subscriptionModel.getUserName() == null?subscriptionModel.getUserGuid() : subscriptionModel.getUserName();
            message = "User : "+user+ " successfully unSubscribed to feed";
        } else {
            user = subscriptionModel.getUserName() == null?subscriptionModel.getUserGuid() : subscriptionModel.getUserName();
            message = "User : "+user+ " failed to unSubscribed to feed";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @RequestMapping(value = "/{userGuid}/subscriptions", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getSubscriptions(@PathVariable("userGuid") @NotNull @Valid String userGuid){
        return new ResponseEntity<>(userService.getAllFeeds(userGuid), HttpStatus.OK);
    }

    @RequestMapping(value = "/{userGuid}/articles", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getArticless(@PathVariable("userGuid") @NotNull @Valid String userGuid){
        return new ResponseEntity<>(userService.getAllArticles(userGuid), HttpStatus.OK);
    }

}
