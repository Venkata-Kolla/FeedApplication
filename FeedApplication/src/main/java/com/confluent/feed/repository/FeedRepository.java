package com.confluent.feed.repository;

import com.confluent.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long>{

    Feed findByGuid(String guid);
    Feed findByFeedName(String feedName);
}
