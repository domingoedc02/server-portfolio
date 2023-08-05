package com.screen.screen001;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.screen.screen001.entity.MemberProfile;
import com.screen.screen001.entity.Role;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.MemberProfileRepository;
import com.screen.screen001.repository.UserRepository;


public class ServletInitializer extends SpringBootServletInitializer{

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MemberProfileRepository memberProfileRepository;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		application.sources(Screen001Application.class);
		application.profiles("dev");
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

		return application.sources(Screen001Application.class);
	}

}
