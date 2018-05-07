package com.btd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;

import com.btd.model.User;
import com.btd.repository.UserRepository;
import com.btd.rest.ApiConstant;

@SpringBootApplication
@EntityScan("com.btd.model")
public class BTDRestApiApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(BTDRestApiApplication.class, args);

		UserRepository userRepository = context.getBean(UserRepository.class);

		if (userRepository.findAll().size() == 0) {

			User admin = new User();

			admin.setEmail(ApiConstant.ADMIN_EMAIL);
			admin.setPassword(ApiConstant.ADMIN_PWD);
			admin.setFirstname(ApiConstant.ADMIN_FIRSTNAME);
			admin.setLastname(ApiConstant.ADMIN_LASTNAME);
			admin.setAdmin(true);
			admin.setCreator(null);

			userRepository.save(admin);

		}
	}
}
