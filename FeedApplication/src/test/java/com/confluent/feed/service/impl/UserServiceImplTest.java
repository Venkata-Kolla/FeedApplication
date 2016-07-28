package com.confluent.feed.service.impl;

import com.confluent.feed.domain.Feed;
import com.confluent.feed.domain.User;
import com.confluent.feed.exception.ConfluentBusinessRuntimeException;
import com.confluent.feed.model.FeedCategory;
import com.confluent.feed.model.SubscriptionModel;
import com.confluent.feed.model.UserModel;
import com.confluent.feed.repository.FeedRepository;
import com.confluent.feed.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FeedRepository feedRepository;

    @Test
    public void testSave(){
        User user = getUser();
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserModel userModel = userServiceImpl.save(getUserModel());
        Assert.assertNotNull("User Model Cannot be null", userModel);
        Assert.assertEquals("First Name should be same", user.getFirstName(), userModel.getFirstName());
        Assert.assertEquals("Email should be same", user.getEmail(), userModel.getEmail());
    }

    @Test(expected = ConfluentBusinessRuntimeException.class)
    public void testSaveUser_Invalid_Email() {
        UserModel userModel = new UserModel();
        userServiceImpl.save(userModel);
        Assert.fail("User Model should fail validation");
    }

    @Test
    public void testSaveUser_Null() {
        UserModel userModel = userServiceImpl.save(null);
        Assert.assertNull("User Model should be null", userModel);
    }

    @Test
    public void testSubscribeFeed() {
        User user = getUser();
        Feed feed = getFeed();
        when(userRepository.findByUserName(any())).thenReturn(user);
        when(feedRepository.findByGuid(any())).thenReturn(feed);
        when(userRepository.save(any(User.class))).thenReturn(user);
        Boolean subscribeFeed = userServiceImpl.subscribeFeed(subscriptionModel());
        Assert.assertTrue("User should be subscribed successfully", subscribeFeed);
    }

    @Test(expected = ConfluentBusinessRuntimeException.class)
    public void testSubscribeFeed_Invalid_User() {
        when(userRepository.findByUserName(any())).thenReturn(null);
        userServiceImpl.subscribeFeed(subscriptionModel());
        Assert.fail("User should be Invalid");
    }

    @Test(expected = ConfluentBusinessRuntimeException.class)
    public void testSubscribeFeed_Invalid_Feed() {
        User user = getUser();
        when(userRepository.findByUserName(any())).thenReturn(user);
        when(feedRepository.findByGuid(any())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);
        userServiceImpl.subscribeFeed(subscriptionModel());
        Assert.fail("Feed should be Invalid");
    }

    @Test(expected = ConfluentBusinessRuntimeException.class)
    public void testSubscribeFeed_Invalid_Existing_Feed() {
        User user = getUser_Feed();
        Feed feed = getFeed();
        when(userRepository.findByUserName(any())).thenReturn(user);
        when(feedRepository.findByGuid(any())).thenReturn(feed);
        when(userRepository.save(any(User.class))).thenReturn(user);
        userServiceImpl.subscribeFeed(subscriptionModel());
        Assert.fail("Feed Subscription should be Invalid");
    }

    @Test
    public void testUnSubscribeFeed(){
        User user = getUser_Feed();
        Feed feed = getFeed();
        when(userRepository.findByUserName(any())).thenReturn(user);
        when(feedRepository.findByGuid(any())).thenReturn(feed);
        when(userRepository.save(any(User.class))).thenReturn(user);
        Boolean subscribeFeed = userServiceImpl.unSubscribeFeed(subscriptionModel());
        Assert.assertTrue("User should be unSubscribed Sucessfully", subscribeFeed);
    }

    @Test(expected = ConfluentBusinessRuntimeException.class)
    public void testUnSubscribeFeed_InValidFeed(){
        User user = getUser();
        Feed feed = getFeed();
        when(userRepository.findByUserName(any())).thenReturn(user);
        when(feedRepository.findByGuid(any())).thenReturn(feed);
        when(userRepository.save(any(User.class))).thenReturn(user);
        userServiceImpl.unSubscribeFeed(subscriptionModel());
        Assert.fail("User is not subscribed to feed");
    }

    private SubscriptionModel subscriptionModel(){
        SubscriptionModel s = new SubscriptionModel();
        s.setFeedAttribute("guid");
        s.setFeedAttrValue("5a20f135-6016-40f6-b079-ba7488b50f70");
        s.setUserName("foo-bar");
        s.setUserGuid("379bd4df-b616-46f3-a614-60ebb6a17540");
        return s;
    }

    private UserModel getUserModel() {
        UserModel userModel = new UserModel();
        userModel.setFirstName("foo");
        userModel.setLastName("bar");
        userModel.setUserName("foo-bar");
        userModel.setEmail("foobar@test.com");
        return userModel;
    }

    private User getUser(){
        User user = new User();
        user.setFirstName("foo");
        user.setLastName("bar");
        user.setUserName("foo-bar");
        user.setEmail("foobar@test.com");
        return user;
    }

    private User getUser_Feed(){
        User user = new User();
        user.setFirstName("foo");
        user.setLastName("bar");
        user.setUserName("foo-bar");
        user.setEmail("foobar@test.com");
        List<Feed> feeds = new ArrayList<>();
        feeds.add(getFeed());
        user.setFeeds(feeds);
        return user;
    }

    private Feed getFeed(){
        Feed feed = new Feed();
        feed.setFeedName("Test Feed");
        feed.setFeedCategory(FeedCategory.TECHNOLOGY);
        feed.setGuid("1234-5678");
        return feed;
    }
}
