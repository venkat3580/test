package com.rs.fer.main;

import java.util.List;

import com.rs.fer.bean.Expense;
import com.rs.fer.service.FERService;
import com.rs.fer.service.FERServiceImpl;

public class ExpenseReportMain {

	public static void main(String[] args) {
		FERService ferservice = new FERServiceImpl();
		int userId=2;
		String Expensetype="product1";
		String FromDate="2019-12-22";
		String ToDate="2019-12-20";

		List<Expense> expense= ferservice.expenseReport(userId, Expensetype, FromDate, ToDate);

		if (expense == null) {
			System.out.println("no record found");
		} else {
			for(Expense expense1:expense) {
			System.out.println("expensereport:" + expense1.getExpensetype() + "," + expense1.getDate() + ","
					+ expense1.getPrice() + "," + expense1.getNoofitems() + "," + expense1.getTotal() + ","
					+ expense1.getBywhom() + "," + expense1.getUserid());
		}
		
	}

	}
}
