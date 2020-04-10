package Model;

import java.sql.*;

public class DBConnection {

	//Fields
	private static final String URL = "jdbc:mysql://den1.mysql2.gear.host:3306/kailuacarrental?autorecconect=true&useSSL=false";
	private static final String USER = "kailuacarrental";
	private static final String PASSWORD = "Orangeplant!";

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
	public void displayCars(String table) {
		try {
			Statement statement = con.createStatement();
			String query = "SELECT * FROM " + table;
			ResultSet rs = statement.executeQuery(query);

			while(rs.next()) {
				System.out.print(rs.getString("plate")+ " ");
				System.out.println(rs.getString("hp"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}