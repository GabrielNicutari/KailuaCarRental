package Model;

import UI.RentalContractsMenu;

import java.sql.*;

public class DBConnection {

	//Fields
	private static final String URL = "jdbc:mysql://den1.mysql2.gear.host:3306/kailuacarrental?autorecconect=true&useSSL=false";
	private static final String USER = "kailuacarrental";
	private static final String PASSWORD = "Orangeplant!";
	private CustomerManagement customerManagement;
	private CarManagement carManagement;
	private RentalContractsManagement rentalContractsManagement;

	//Instantiate Objects
	private static Connection con;

	//Constructors
	public DBConnection() {
		try {
			con = DriverManager.getConnection(URL,USER,PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		customerManagement = new CustomerManagement();
		carManagement = new CarManagement();
		rentalContractsManagement = new RentalContractsManagement();
	}

	//Methods
	public void displayCars(String table) {
		try {
			Statement statement = con.createStatement();
			String query = "SELECT brands.id, cars.id " +
					"FROM cars, brands " +
					"WHERE brands.id = cars.brand_id";
			ResultSet rs = statement.executeQuery(query);

			while(rs.next()) {
				System.out.print(rs.getString("cars.id") + " ");
				System.out.println(rs.getString("brands.id"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static Boolean searchCar(String columnName) {
		return CarManagement.searchCar(columnName, con);
	}
}