package com.screen.screen001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;



@SpringBootApplication

public class Screen001Application {

	
	
	public static void main(String[] args) {
		
		
		SpringApplication.run(Screen001Application.class, args);
		
	}


	

	

}
