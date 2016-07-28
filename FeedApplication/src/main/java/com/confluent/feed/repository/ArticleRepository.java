package com.confluent.feed.repository;

import com.confluent.feed.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>{

    Article findByGuid(String guid);
}
