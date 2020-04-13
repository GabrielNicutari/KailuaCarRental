package Model;

import UI.Validation;

import java.sql.*;

public class DBConnection {

	//  Fields
	private static final String URL = "jdbc:mysql://den1.mysql2.gear.host:3306/kailuacarrental?autorecconect=true&useSSL=false";
	private static final String USER = "kailuacarrental";
	private static final String PASSWORD = "Orangeplant!";

	//  Instantiate Objects
    private CustomerManagement customerManagement;
    private CarManagement carManagement;
    private RentalContractManagement rentalContractManagement;
	private static Connection con;
	private static Validation validation;

	//  Constructors
	public DBConnection() {
		try {
			con = DriverManager.getConnection(URL,USER,PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		validation = new Validation(con);
		customerManagement = new CustomerManagement(con, validation);
		carManagement = new CarManagement(con, validation);
		rentalContractManagement = new RentalContractManagement(con, validation);
	}


	//  Methods
    //		---DISPLAY---		\\
	public void displayCars() {
		carManagement.display();
	}

	public void displayCustomers() {
		customerManagement.display();
	}

    public void displayRentalContracts() {
        rentalContractManagement.display();
    }


	// 		---CREATE---		\\
    public void createCar() {
		carManagement.create();
	}

	public void createRentalContract() {
		rentalContractManagement.create();
	}


    //		---SEARCH---		\\
    public void searchCar(String columnName) {
        carManagement.searchCar(columnName);
    }


    //		---UPDATE---		\\
    public void updateCar() {
	    //not yet
    }


    //		---DELETE---		\\
    public void deleteRow(String tableName) {
        CarManagement.deleteRow(tableName);
    }


}