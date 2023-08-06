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

	

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		application.sources(Screen001Application.class);
		application.profiles("dev");
		

		return application.sources(Screen001Application.class);
	}

}
