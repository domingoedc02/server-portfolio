package com.screen.screen001.auth;

import java.sql.Timestamp;

import com.screen.screen001.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

private String memberId;
private String password;
private String email;
private String memberName;
private Role adminFlag;
private String deleteFlag;
private String insertMember;
private Timestamp insertDate;
private String updateMember;
private Timestamp updateDate;
}
