package com.screen.screen001.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.screen.screen001.entity.User;

public interface UserControllerRepository extends JpaRepository<User, Integer> {

    Iterable<User> findByMemberId(String memberId);
  
    
  }
