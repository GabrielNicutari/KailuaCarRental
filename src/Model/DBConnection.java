package Model;

import java.sql.*;

public class DBConnection {

	//Fields
	private static final String URL = "jdbc:mysql://den1.mysql2.gear.host:3306/kailuacarrental?autorecconect=true&useSSL=false";
	private static final String USER = "kailuacarrental";
	private static final String PASSWORD = "Orangeplant!";

	//Instantiate Objects
	private static Connection con;

	static {
		try {
			con = DriverManager.getConnection(URL,USER,PASSWORD);
			Statement statement = con.createStatement();
			String query = "SELECT * FROM customers";
			ResultSet rs = statement.executeQuery(query);

			while(rs.next()) {
				System.out.println(rs.getString("first_name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}