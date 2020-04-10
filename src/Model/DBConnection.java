package Model;

import UI.RentalContractsMenu;

import java.sql.*;

public class DBConnection {

	//Fields
	private static final String URL = "jdbc:mysql://den1.mysql2.gear.host:3306/kailuacarrental?autorecconect=true&useSSL=false";
	private static final String USER = "kailuacarrental";
	private static final String PASSWORD = "Orangeplant!";
	private CustomerManagement customerManagement = new CustomerManagement();
	private CarManagement carManagement = new CarManagement();
	private RentalContractsManagement rentalContractsManagement = new RentalContractsManagement();

	//Instantiate Objects
	private static Connection con;

	//Constructors
	public DBConnection() {
		try {
			con = DriverManager.getConnection(URL,USER,PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Methods
	public void displayCars() {
		carManagement.display(con);
	}

	public void displayCustomers() {
		customerManagement.display(con);
	}

	public void displayContracts() {
		rentalContractsManagement.display(con);
	}

}