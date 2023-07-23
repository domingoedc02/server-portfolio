package com.screen.screen001.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.screen.screen001.entity.MemberProfile;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Integer>{
    
   Iterable<MemberProfile> findByMemberId(String memberId);
   
}
