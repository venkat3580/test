package com.rs.fer.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.rs.fer.bean.User;
import com.rs.fer.service.FERService;
import com.rs.fer.service.FERServiceImpl;
import com.rs.fer.util.HBUtil;

public class RegistrationMain {

	public static void main(String[] args) {

		FERService ferService = new FERServiceImpl();

		User user = new User();

		user.setFirstname("venkat");
		user.setMiddlename("eswarao");
		user.setLastname("konidena");
		user.setUsername("admin");
		user.setPassword("admin");
		user.setEmailid("konide@rs.com");
		user.setMobileno("987654321");

		boolean isRegister = ferService.registration(user);

		if (isRegister) {
			System.out.println("Registration is done successfully...");
		} else {
			System.out.println("Registration is failed...");
		}

	}

}
