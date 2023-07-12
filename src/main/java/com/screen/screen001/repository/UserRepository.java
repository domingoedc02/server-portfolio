package com.screen.screen001.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.screen.screen001.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByMemberId(String memberId);

  
}
