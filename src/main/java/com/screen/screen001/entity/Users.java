package com.screen.screen001.entity;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_users")
public class Users implements UserDetails {

    @Id
    @Column(nullable = false, columnDefinition = "char(5)", length = 5)
    private String member_id;

    @Column(nullable = false, columnDefinition = "varchar(120)", length = 120)
    private String password;

    @Column(nullable = false, columnDefinition = "varchar(200)", length = 200)
    private String email;

    @Column(nullable = false, columnDefinition = "varchar(40)", length = 40)
    private String member_name;

    @Column(nullable = false, columnDefinition = "char(1)", length = 1)
    private String admin_flag;

    @Column(nullable = false, columnDefinition = "char(1)", length = 1)
    private String delete_flag;

    @Column(nullable = false, columnDefinition = "char(5)", length = 5)
    private String insert_member;

    @Column(nullable = false)
    private Timestamp insert_date;

    @Column(nullable = false, columnDefinition = "char(5)", length = 5)
    private String update_member;

    @Column(nullable = false)
    private Timestamp update_date;

    // @Enumerated(EnumType.STRING)
    // private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // return List.of(new SimpleGrantedAuthority(role.name()));
        return List.of(new SimpleGrantedAuthority(admin_flag));
    }

    @Override
    public String getUsername() {
        return member_id;
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

    @Override
    public String getPassword() {
        return password;
    }

}
