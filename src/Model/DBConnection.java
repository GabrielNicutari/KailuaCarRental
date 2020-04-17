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
		rentalContractManagement = new RentalContractManagement(con, validation, carManagement, customerManagement);
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

	public void createRentalContract(boolean alreadyExists, int customerId) {
		rentalContractManagement.create(alreadyExists, customerId);
	}


    //		---SEARCH---		\\
    public void searchCar(String columnName) {
        carManagement.search(columnName);
    }

    public boolean searchCustomer() {
		return customerManagement.search();
	}

	public boolean searchRentalContract(String columnName) {
		return rentalContractManagement.search(columnName);
	}

    //		---UPDATE---		\\
    public void updateCar(int toUpdate, String columnName) {
	    carManagement.update(toUpdate, columnName);
    }

	public void updateCustomer(int toUpdate, String columnName) {
		customerManagement.update(toUpdate, columnName);
	}

	public void updateRentalContract(int toUpdate, String columnName) {
		rentalContractManagement.update(toUpdate, columnName);
	}

    //		---DELETE---		\\
	public void deleteRow(String tableName) {
		int userInput = -1;
		userInput = validation.isInsideTable(tableName);
		//userInput = isInsideList(tableName);
		try {
			PreparedStatement st = con.prepareStatement("DELETE FROM " + tableName + " WHERE id = " + userInput);

			System.out.println();
			System.out.println("Are you sure you want to delete the row with ID = " + userInput + " from this table? " +
					"(Type \"Y/YES\" or \"N/NO\")");

			String answer = validation.getValidatedAnswer("");
			if(answer.equalsIgnoreCase("YES") || answer.equalsIgnoreCase("Y"))   {
				st.executeUpdate();
				System.out.println("The row has been successfully deleted.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}		//only cars and rental contracts
}