package com.rs.fer.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.rs.fer.bean.Address;
import com.rs.fer.bean.Expense;
import com.rs.fer.bean.User;
import com.rs.fer.util.DBUtil;
import com.rs.fer.util.HBUtil;

public class FERServiceImpl implements FERService {


	public boolean registration(User user) {
		boolean isRegister = false;
		try {
			Integer numberOfRecordInserted = (Integer) HBUtil.getSession().save(user);
			HBUtil.getTranctionCommit();
			System.out.println("no of records inserted:" + numberOfRecordInserted);
			isRegister = numberOfRecordInserted > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HBUtil.closeSession();
		}
		return isRegister;

	}

	public int login(String username, String password) {
		Connection connection = null;
		PreparedStatement preparestatement = null;

		try {

			connection = DBUtil.getConnection();
			preparestatement = connection.prepareStatement("select * from user where username=? and password=?;");

			preparestatement.setString(1, username);
			preparestatement.setString(2, password);

			ResultSet login = preparestatement.executeQuery();
			while (login.next()) {
				return login.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(connection);
		}
		return 0;
	}

	public boolean addExpense(Expense expense) {
		boolean isAddExpense = false;
		Connection connection = null;
		PreparedStatement preparedstatement = null;

		try {

			connection = DBUtil.getConnection();

			preparedstatement = connection.prepareStatement(
					"insert into expense(ExpenseType,Date,Price,NoofItems,Total,ByWhom)values(?,?,?,?,?,?);");

			preparedstatement.setString(1, expense.getExpensetype());
			preparedstatement.setString(2, expense.getDate());
			preparedstatement.setString(3, expense.getPrice());
			preparedstatement.setString(4, expense.getNoofitems());
			preparedstatement.setString(5, expense.getTotal());
			preparedstatement.setString(6, expense.getBywhom());

			preparedstatement.addBatch();

			int noOfRecInserted = preparedstatement.executeUpdate();
			System.out.println("no of Rec Inserted:" + noOfRecInserted);
			isAddExpense = (noOfRecInserted > 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(connection);
		}
		return isAddExpense;

	}

	public boolean editExpense(Expense expense) {
		boolean isEditExpense = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = DBUtil.getConnection();
			String sql = ("UPDATE expense SET ExpenseType=?, Date=?, Price=?, NoofItems=?, Total=?, ByWhom=? WHERE user_id=?");

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, expense.getExpensetype());
			preparedStatement.setString(2, expense.getDate());
			preparedStatement.setString(3, expense.getPrice());
			preparedStatement.setString(4, expense.getNoofitems());
			preparedStatement.setString(5, expense.getTotal());
			preparedStatement.setString(6, expense.getBywhom());
			preparedStatement.setInt(7, expense.getUserid());

			int noOfEditExpenses = preparedStatement.executeUpdate();
			System.out.println("noOfEditExpenses:" + noOfEditExpenses);
			isEditExpense = (noOfEditExpenses > 0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(connection);
		}
		return isEditExpense;
	}

	public boolean deleteExpense(int expenseId) {
		boolean isDeleted = false;
		Connection connection = null;
		PreparedStatement preparedstatement = null;

		try {

			connection = DBUtil.getConnection();
			preparedstatement = connection.prepareStatement("Delete from expense where id=?;");

			preparedstatement.setInt(1, expenseId);

			int noOfDeleteExpenses = preparedstatement.executeUpdate();
			System.out.println("noOfDeleteExpenses:" + noOfDeleteExpenses);

			isDeleted = (noOfDeleteExpenses > 0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(connection);
		}
		return isDeleted;
	}

	public boolean resetPassword(int userId, String currentPassword, String newPassword) {
		boolean resetPassword = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = DBUtil.getConnection();
			preparedStatement = connection.prepareStatement("UPDATE USER SET password=? WHERE id=? and password=?;");

			preparedStatement.setString(1, newPassword);

			preparedStatement.setInt(2, userId);

			preparedStatement.setString(3, currentPassword);

			// Execute Statement
			int noOfRecAffected = preparedStatement.executeUpdate();
			System.out.println("noOfRecAffected:" + noOfRecAffected);
			resetPassword = (noOfRecAffected > 0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(connection);
		}

		return resetPassword;
	}

	public Expense getExpense(int expenseId) {
		Session session = HBUtil.getSession();
		Expense expense = null;
		try {
			expense = (Expense) session.get(Expense.class, expenseId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HBUtil.closeSession();
		}

		return expense;
	}

	public List<Expense> getExpenses(int userId) {

		Session session = HBUtil.getSession();
		Transaction transaction = null;
		List<Expense> expenses = new ArrayList<Expense>();
		try {
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Expense where userId=?");
			query.setParameter(0, userId);
			transaction.commit();
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return expenses;
	}
	
	public List<Expense> expenseReport(int userId, String expenseType, String fromDate, String toDate) {
		List<Expense> expense = new ArrayList<Expense>();
		Connection connection = null;
		PreparedStatement preparedstatement = null;

		try {

			connection = DBUtil.getConnection();

			preparedstatement = connection
					.prepareStatement("select * from expense where user_id=? and Date between ? and ?;");
			preparedstatement.setString(1, expenseType);
			preparedstatement.setString(2, fromDate);
			preparedstatement.setString(3, toDate);

			ResultSet resultSet = preparedstatement.executeQuery();
			while (resultSet.next()) {
				// Expense expense1=new Expense();
				System.out.println(resultSet.getString("id") + "," + resultSet.getString("ExpenseType") + ","
						+ resultSet.getString("Date") + "," + resultSet.getString("Price") + ","
						+ resultSet.getString("NoofItems") + "," + resultSet.getString("Total") + ","
						+ resultSet.getString("ByWhom"));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(connection);
		}

		return expense;
	}

	public User getUser(int userId) {
	
         User user=null;
		Connection connection = null;
		PreparedStatement preparedstatement = null;

		try {

			connection = DBUtil.getConnection();

			preparedstatement = connection.prepareStatement(
					"select u.*,a.* from user u left join address a on u.id=a.user_id where a.user_id=?;");
			preparedstatement.setInt(1,userId);

			ResultSet resultSet = preparedstatement.executeQuery();
			while (resultSet.next()) {

			    user=new User();
				
				user.setFirstname(resultSet.getString("FirstName"));
				user.setMiddlename(resultSet.getString("MiddleName"));
				user.setLastname(resultSet.getString("LastName"));
				user.setUsername(resultSet.getString("UserName"));
				user.setPassword(resultSet.getString("password"));
				user.setEmailid(resultSet.getString("EmailId"));
				user.setMobileno(resultSet.getString("MobileNo"));
				
				Address address=new Address();
				address.setId(resultSet.getInt("Id"));
				address.setAddress1(resultSet.getString("Address1"));
				address.setAddress2(resultSet.getString("Address2"));
				address.setStreet(resultSet.getString("Street"));
				address.setCity(resultSet.getString("City"));
				address.setState(resultSet.getString("State"));
				address.setPincode(resultSet.getString("PinCode"));
				address.setUserid(userId);
				
             
			user.setAddress(address);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(connection);
		}

		return user;
	}


	public boolean UpdateUserMain(User user) {
		boolean updateUser=false;
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			

			connection =DBUtil.getConnection();

			statement = connection.prepareStatement(
					"update user set firstname=?,middlename=?,lastname=?,username=?,password=?,emailid=?,mobileno=? where id=?");

			statement.setString(1,user.getFirstname());
			statement.setString(2,user.getMiddlename());
			statement.setString(3,user.getLastname());
			statement.setString(4,user.getUsername());
			statement.setString(5,user.getPassword());
			statement.setString(6,user.getEmailid());
			statement.setString(7,user.getMobileno());
			statement.setInt(8,user.getId());
			
			

			int noOfRecUpdated = statement.executeUpdate();
			System.out.println("no Of Records Updated:" + noOfRecUpdated);

			updateUser=noOfRecUpdated>0;
			
			System.out.println("updateUser:"+updateUser);
			Address address=new Address();
			if(address.getId()==0) {
			statement = connection.prepareStatement(
					"insert into address(Address1,Address2,Street,City,State,PinCode,user_id)values(?,?,?,?,?,?,?)");

			statement.setString(1, address.getAddress1());
			statement.setString(2, address.getAddress2());
			statement.setString(3, address.getStreet());
			statement.setString(4, address.getCity());
			statement.setString(5, address.getState());
			statement.setString(6, address.getPincode());
			statement.setInt(7, address.getUserid());
			}else {
				statement = connection.prepareStatement(
						"update address set Address1=?,Address2=?,Street=?,City=?,State=?,PinCode=? user_id=?;");

			    statement.setString(1, address.getAddress1());
				statement.setString(2, address.getAddress2());
				statement.setString(3, address.getStreet());
				statement.setString(4, address.getCity());
				statement.setString(5, address.getState());
				statement.setString(6, address.getPincode());
				statement.setInt(7, address.getUserid());
			}
             System.out.println("statementAddress:"+statement);
             int updateAddress=statement.executeUpdate();
             System.out.println("updateAddress:"+updateAddress);
             updateUser=updateAddress>0;
             
             System.out.println("updateUser:"+updateUser);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeConnection(connection);
		}
		return updateUser;
	}
	

}
