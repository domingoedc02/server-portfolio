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
    private String user_id;

    @Column(nullable = false, columnDefinition = "varchar(120)", length = 120)
    private String password;

    @Column(nullable = false, columnDefinition = "varchar(200)", length = 200)
    private String email;

    @Column(nullable = false, columnDefinition = "char(1)", length = 1)
    private String working_status;

    @Column(nullable = false, columnDefinition = "varchar(40)", length = 40)
    private String last_name;

    @Column(nullable = false, columnDefinition = "varchar(40)", length = 40)
    private String first_name;

    @Column(nullable = true, length = 40, columnDefinition = "varchar(40)")
    private String last_name_kana;

    @Column(nullable = true, length = 40, columnDefinition = "varchar(40)")
    private String first_name_kana;

    @Column(nullable = false, columnDefinition = "char(1)", length = 1)
    private String delete_flag;

    @Column(nullable = false, columnDefinition = "char(5)", length = 5)
    private String insert_user;

    @Column(nullable = false)
    private Timestamp insert_date;

    @Column(nullable = false, columnDefinition = "char(5)", length = 5)
    private String update_user;

    @Column(nullable = false)
    private Timestamp update_date;

    // @Enumerated(EnumType.STRING)
    // private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // return List.of(new SimpleGrantedAuthority(role.name()));
        return List.of(new SimpleGrantedAuthority(working_status));
    }

    @Override
    public String getUsername() {
        return user_id;
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
