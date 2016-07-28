package com.confluent.feed.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ARTICLE")
public class Article extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "GUID", unique = true, nullable = false, length = 38)
    private String guid;

    @Column(name = "ARTICLE_NAME", nullable = false, unique = true)
    private String articleName;

    @Column(name = "ARTICLE_URL")
    private String articleUrl;

    @ManyToMany(mappedBy = "articles", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feed> feeds;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
