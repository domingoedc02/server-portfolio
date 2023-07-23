package com.screen.screen001.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.screen.screen001.entity.MemberProfile;

public interface MemberProfileControllerRepository extends JpaRepository<MemberProfile, Integer>{
    Optional<MemberProfile> findByMemberId(String memberId);
}
