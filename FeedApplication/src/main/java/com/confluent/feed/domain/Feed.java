package com.confluent.feed.domain;

import com.confluent.feed.model.FeedCategory;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FEED")
public class Feed extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "GUID", unique = true, nullable = false, length = 38)
    private String guid;

    @Column(name = "FEED_NAME", nullable = false, unique = true)
    private String feedName;

    @Column(name = "FEED_CATEGORY", nullable = false)
    @Enumerated(EnumType.STRING)
    private FeedCategory feedCategory;

    @ManyToMany(mappedBy = "feeds", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "article_feed", joinColumns = @JoinColumn(name = "feed_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"))
    private List<Article> articles;

    public FeedCategory getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(FeedCategory feedCategory) {
        this.feedCategory = feedCategory;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feed feed = (Feed) o;

        if (!id.equals(feed.id)) return false;
        if (!guid.equals(feed.guid)) return false;
        return feedName.equals(feed.feedName);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + guid.hashCode();
        result = 31 * result + feedName.hashCode();
        return result;
    }
}
