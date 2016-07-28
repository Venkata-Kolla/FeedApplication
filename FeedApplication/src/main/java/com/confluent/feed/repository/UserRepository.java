package com.confluent.feed.repository;

import com.confluent.feed.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByUserName(String userName);
    User findByGuid(String guid);
}
