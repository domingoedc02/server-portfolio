package com.screen.screen001;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.screen.screen001.entity.MemberProfile;
import com.screen.screen001.entity.Role;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.MemberProfileRepository;
import com.screen.screen001.repository.UserRepository;



@SpringBootApplication
public class Screen001Application {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MemberProfileRepository memberProfileRepository;
	
	
	public void addUserAdmin(){
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//Register Admin User
		User user = User
		.builder()
		.memberId("SC001")
		.memberName("株式会社SCREEN")
		.email("screen2019.inc@gmail.com")
		.password(passwordEncoder.encode("password"))
		.authority(Role.ADMIN)
		.deleteFlag("0")
		.insertMember("SC001")
		.insertDate(timestamp)
		.updateMember("SC001")
		.updateDate(timestamp)
		.build();

		MemberProfile profile = MemberProfile
		.builder()
		.memberId("SC001")
		.memberName("株式会社SCREEN")
		.build();

		memberProfileRepository.save(profile);
		userRepository.save(user);
	}
	
	public static void main(String[] args) {
		
		SpringApplication.run(Screen001Application.class, args);
	}



	

}
