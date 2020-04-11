package Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerManagement {

    //  Instantiate Objects
    public static Connection con;

    //  Console Input
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    //  Constructor
    public CustomerManagement (Connection con) {
        this.con = con;
    }

    //  Methods
    public void display() {
        try {
            Statement statement = con.createStatement();

            /*String query = "SELECT c.id, c.first_name, c.last_name, c.mobile_phone, c.address, z.zip, z.city, c.driver_licence_number," +
                    "c.driver_since_date, c.email " +
                    "FROM customers c, zip z " +
                    "WHERE c.zip = z.zip";*/

            String query = "SELECT * " +
                    "FROM customers c, zip z " +
                    "WHERE c.zip = z.zip";

            ResultSet rs = statement.executeQuery(query);

            System.out.printf("| %-7s| %-20s| %-20s| %-10s| %-30s| %-7s| %-15s| %-18s| %-13s| %-25s|\n", "ID",
                    "FIRST NAME", "LAST NAME", "PHONE NR", "ADDRESS", "ZIP", "CITY", "DRIVER LICENCE NR", "DRIVER SINCE", "EMAIL");
            System.out.println("*****************************************************************************************************************" +
                    "*************************************************************************");
            while(rs.next()) {
                System.out.printf("| %-7s| %-20s| %-20s| %-10s| %-30s| %-7s| %-15s| %-18s| %-13s| %-25s|\n",
                        rs.getString("c.id"),rs.getString("c.first_name"), rs.getString("c.last_name"),
                        rs.getString("c.mobile_phone"), rs.getString("c.address"), rs.getString("z.zip"),
                        rs.getString("z.city"), rs.getString("c.driver_licence_number"), rs.getString("c.driver_since_date"),
                        rs.getString("c.email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
