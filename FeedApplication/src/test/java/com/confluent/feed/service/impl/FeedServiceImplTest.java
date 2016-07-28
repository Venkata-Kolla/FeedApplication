package com.confluent.feed.service.impl;

import com.confluent.feed.domain.Article;
import com.confluent.feed.domain.Feed;
import com.confluent.feed.model.ArticleModel;
import com.confluent.feed.model.FeedCategory;
import com.confluent.feed.model.FeedModel;
import com.confluent.feed.repository.ArticleRepository;
import com.confluent.feed.repository.FeedRepository;
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
public class FeedServiceImplTest {

    @InjectMocks
    private FeedServiceImpl feedService;
    @Mock
    private FeedRepository feedRepository;
    @Mock
    private ArticleRepository articleRepository;

    @Test
    public void testSaveFeed(){
        when(feedRepository.save(any(Feed.class))).thenReturn(getFeed());
        FeedModel feedModel = feedService.save(getFeedModel());
        Assert.assertNotNull(feedModel);
    }

    @Test
    public void testAddArticleToFeed() {
        when(feedRepository.save(any(Feed.class))).thenReturn(getFeed_Article());
        when(feedRepository.findByGuid(any(String.class))).thenReturn(getFeed());
        when(articleRepository.save(any(Article.class))).thenReturn(geArticle());
        when(articleRepository.findByGuid(any(String.class))).thenReturn(geArticle());
        FeedModel feedModel = feedService.addArticleToFeed("5a20f135-6016-40f6-b079-ba7488b50f70", getArticleModel());
        Assert.assertNotNull("Feed Model cannot be null", feedModel);
    }

    private FeedModel getFeedModel(){
        FeedModel feedModel = new FeedModel();
        feedModel.setFeedName("Test Feed");
        feedModel.setFeedCategory(FeedCategory.TECHNOLOGY);
        return feedModel;
    }

    private Feed getFeed(){
        Feed feed = new Feed();
        feed.setFeedName("Test Feed");
        feed.setFeedCategory(FeedCategory.TECHNOLOGY);
        return feed;
    }

    private Feed getFeed_Article(){
        Feed feed = new Feed();
        feed.setFeedName("Test Feed");
        feed.setFeedCategory(FeedCategory.TECHNOLOGY);
        List<Article> articles = new ArrayList<>();
        articles.add(geArticle());
        feed.setArticles(articles);
        return feed;
    }

    private ArticleModel getArticleModel(){
        ArticleModel articleModel = new ArticleModel();
        articleModel.setArticleName("Test Art1");
        articleModel.setArticleUrl("https://www.foobar.com/");
        return articleModel;
    }

    private Article geArticle(){
        Article article = new Article();
        article.setArticleName("Test Art1");
        article.setArticleUrl("https://www.foobar.com/");
        return article;
    }
}
