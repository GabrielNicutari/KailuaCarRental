package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class CarManagement {
    public static Connection con;
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public CarManagement (Connection con) {
        this.con = con;
    }

    public static Boolean searchCar (String columnName) {

       if (columnName.equals("brand.name")) {
           displayBrand();
           choosenBrand();
       }
        if (columnName.equals("cars.hp")) {
        }
        if (columnName.equals("cars.number_seats")) {
        }
        if (columnName.equals("cars.price_per_day")) {
        }
        return true;
    }

        public static void displayBrand() {
            try {
                Statement statement = con.createStatement();
                String query = "SELECT id, name " +
                        "FROM brands";
                ResultSet rs = statement.executeQuery(query);

                while(rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    System.out.printf("%-20s%-20s%n", id, name);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public static void choosenBrand() {
            try {
                Statement statement = con.createStatement();
                String userInput = null;
                try {
                    userInput = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String query = "SELECT * " +
                        "FROM brands, cars " +
                        "WHERE brands.id = " + userInput  + " AND cars.brand_id = " + userInput;
                ResultSet rs = statement.executeQuery(query);

                while(rs.next()) {
                    String id = rs.getString("cars.id");
                    String name = rs.getString("name");
                    String model = rs.getString("model");
                    String gear = rs.getString("automatic_gear");
                    String fuelType = rs.getString("fuel_type");
                    System.out.printf("%-20s%-20s%-20s%-20s%-20s%n", id, name, model, gear, fuelType);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public static void deleteRow(String tableName) {

            try {
                String userInput = null;
                try {
                    userInput = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PreparedStatement st = con.prepareStatement("DELETE FROM " + tableName + " WHERE id = " + userInput);
                st.executeUpdate();

                System.out.println("Its been succesfully deleted.! Congratz conrad!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
}
