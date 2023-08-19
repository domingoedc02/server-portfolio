package com.screen.screen001.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_members", schema = "screen")
public class User implements UserDetails {
  
  @Id
  @Column(nullable = false, length = 5, unique = true)
  private String memberId;

  @Column(nullable = false, length = 320)
  private String password;

  @Column(nullable = false, length = 256)
  private String email;

  @Column(nullable = false, length = 40)
  private String memberName;

  @Column(nullable = false, length = 1)
  private String deleteFlag;

  @Column(nullable = false, length = 5)
  private String insertMember;

  private Timestamp insertDate;

  @Column(nullable = true, length = 5)
  private String updateMember;
  
  private Timestamp updateDate;
  
  

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 5)
  private Role authority;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authority.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return memberId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  
}
