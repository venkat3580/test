package com.rs.fer.main;

import com.rs.fer.bean.Expense;
import com.rs.fer.service.FERService;
import com.rs.fer.service.FERServiceImpl;

public class FER_EditExpense {

	public static void main(String[] args) {

		FERService ferservice = new FERServiceImpl();
		Expense expense = new Expense();

		expense.setExpensetype("Product7");
		expense.setDate("2019-12-15");
		expense.setPrice("500");
		expense.setNoofitems("6");
		expense.setTotal("11");
		expense.setBywhom("pavan");
		expense.setUserid(2);

		boolean isEditExpense = ferservice.editExpense(expense);
		if (isEditExpense) {
			System.out.println("Expenses edited Successfully");
		} else {
			System.out.println("No expenses edited");
		}

	}

}
