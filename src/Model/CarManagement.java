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
  
    public void display() {
        try {
            Statement statement = con.createStatement();

            String query = "SELECT * " +
                    "FROM cars c, brands b " +
                    "WHERE b.id = c.brand_id";

            ResultSet rs = statement.executeQuery(query);

            System.out.printf("| %-7s| %-15s| %-15s| %-23s| %-10s| %-15s| %-15s| %-15s| %-15s| %-9s| %-15s| %-12s| %-13s| %-11s|\n", "Car_ID",
                    "BRAND", "MODEL", "ENGINE CAPACITY", "HP", "FUEL TYPE", "ODOMETER", "AUTOMATIC", "PRICE/DAY", "NR_SEATS",
                    "SEATS MATERIAL", "CRUISE CTRL", "PLATE", "REG DATE");
            System.out.println("**************************************************************************************************************" +
                    "*************************************************************************************************************");
            while(rs.next()) {
                System.out.printf("| %-7s| %-15s| %-15s| %-23s| %-10s| %-15s| %-15s| %-15s| %-15s| %-9s| %-15s| %-12s| %-13s| %-11s|\n",
                        rs.getString("c.id"),rs.getString("b.name"), rs.getString("b.model"), rs.getString("c.litre_engine"),
                        rs.getString("c.hp"), rs.getString("c.fuel_type"), rs.getString("c.odometer"),
                        rs.getString("c.automatic_gear"), rs.getString("c.price_per_day"), rs.getString("c.number_seats"),
                        rs.getString("c.seats_material"), rs.getString("c.cruise_control"), rs.getString("c.plate"),
                        rs.getString("c.registration_date"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
