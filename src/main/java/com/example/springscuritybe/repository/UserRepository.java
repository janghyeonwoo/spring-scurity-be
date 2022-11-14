package com.example.springscuritybe.repository;

import com.example.springscuritybe.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findById(@Param("id") String id);
}
