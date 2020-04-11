package Model;

import UI.MainMenu;
import UI.Validation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class CarManagement {

    //  Instantiate Objects
    private static Connection con;
    private static Validation validation;

    //  Console Input
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    //  Constructor
    public CarManagement (Connection con, Validation validation) {
        this.con = con;
        this.validation = validation;
    }

    //  Methods
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

    public static void searchCar (String columnName) {

       if (columnName.equals("brand.name")) {
           displayBrand();
           chooseBrand();
       }
        if (columnName.equals("cars.hp")) {
        }
        if (columnName.equals("cars.number_seats")) {
        }
        if (columnName.equals("cars.price_per_day")) {
        }
    }

    public static void displayBrand() {
        MainMenu.printEmptyLines();
        System.out.printf("| %-10s| %-15s|\n", "BRAND_ID", "NAME");
        System.out.println("**************************************");

        try {
            Statement statement = con.createStatement();
            String query = "SELECT id, name " +
                    "FROM brands";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
                System.out.printf("| %-10s| %-15s|\n", rs.getString("id"),rs.getString("name"));
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void chooseBrand() {
        int userInput = -1;
        userInput = validation.isInsideTable("brands"); //check if input is found in the table - User Restriction
        try {
            Statement statement = con.createStatement();

            String query = "SELECT * " +
                    "FROM brands, cars " +
                    "WHERE brands.id = " + userInput  + " AND cars.brand_id = " + userInput;
            ResultSet rs = statement.executeQuery(query);

            System.out.println();
            while(rs.next()) {
                String id = rs.getString("cars.id");
                String name = rs.getString("name");
                String model = rs.getString("model");
                String gear = rs.getString("automatic_gear");
                String fuelType = rs.getString("fuel_type");
                System.out.printf("%-20s%-20s%-20s%-20s%-20s%n", id, name, model, gear, fuelType);
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRow(String tableName) {
        int userInput = -1;
        userInput = validation.isInsideTable(tableName);
        //userInput = isInsideList();   //we actually need another column or another table to properly do this
        //because we're gonna have problems when we filter by ranges

        try {
            PreparedStatement st = con.prepareStatement("DELETE FROM " + tableName + " WHERE id = " + userInput);

            System.out.println();
            System.out.println("Are you sure you want to delete this row? " +
                    "(Type \"Y/YES\" or \"N/NO\")");

            String answer = validation.getValidatedAnswer("");
            if(answer.equalsIgnoreCase("YES") || answer.equalsIgnoreCase("Y"))   {
                st.executeUpdate();
                System.out.println("The row has been successfully deleted.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int isInsideList() {
        return 0;
    }
}
