package com.rs.fer.main;

import com.rs.fer.service.FERService;
import com.rs.fer.service.FERServiceImpl;

public class FER_Login {

	public static void main(String[] args) {
		FERService ferservice = new FERServiceImpl();

		String userName = "anilkumar";
		String Password = "67891";

		int login = ferservice.login(userName, Password);

		if (login > 0) {
			System.out.println("Records Fetched Successfully");
		} else {
			System.out.println("No matched Records");
		}

	}

}