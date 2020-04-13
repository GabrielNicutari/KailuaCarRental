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
                    "FROM cars c, brands b " +
                    "WHERE b.id = " + userInput  + " AND c.brand_id = " + userInput;
            ResultSet rs = statement.executeQuery(query);

            System.out.println();
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
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(int toUpdate, String columnName) {
        String value;
        switch(columnName) {
            case "cruise_control":
                value = validation.getValidatedAnswer("Do you want the car to have Cruise Control? (" +
                        "Type \"Y/YES\" or \"N/NO\")");
                updateQueryForStringField(columnName, value, toUpdate);
                break;

            case "plate":
                value = validation.getValidatedPlate("Please type the Registration Number (eg: AB12345):");
                updateQueryForStringField(columnName, value, toUpdate);
                break;

            case "seats_material":
                value = validation.getValidatedName("Please type the Seats' Material:");
                updateQueryForStringField(columnName, value, toUpdate);
                break;

            case "price_per_day":           //also gotta update total price in rental contracts
                double price = validation.getValidatedDouble("Please type the Price Per Day of the car: ");
                try {
                    //Update Car Info
                    String query = "UPDATE cars SET " + columnName + " = ? WHERE id = ?";

                    PreparedStatement preparedStmt = con.prepareStatement(query);   //for car

                    preparedStmt.setDouble(1, price);
                    preparedStmt.setInt(2, toUpdate);

                    preparedStmt.executeUpdate();

                    //Update Contract Info
//                    String selectContract = "SELECT from_date, to_date, extra_km " +
//                            "FROM contracts WHERE id = " + toUpdate; //I need to know how much extra_km factor in the price
//                    String updateContract = "UPDATE cars SET total_price = ? WHERE id = ?";
//
//                    Statement statement = con.createStatement();
//                    PreparedStatement contractStmt = con.prepareStatement(updateContract);
//
//                    ResultSet rs = statement.executeQuery(selectContract);
//
//                    double total_price = 0; //gotta calculate it
//                    contractStmt.setDouble(1, total_price);
//
//                    contractStmt.executeUpdate();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
                break;

            case "everything":
                String cruise_control = validation.getValidatedAnswer("Do you want the car to have Cruise Control? (" +
                        "Type \"Y/YES\" or \"N/NO\")");
                String plate = validation.getValidatedPlate("Please type the Registration Number (eg: AB12345):");
                String seats_material = validation.getValidatedName("Please type the Seats' Material:");
                double price_per_day = validation.getValidatedDouble("Please type the Price Per Day of the car: ");
                try {
                    String query = "UPDATE cars SET cruise_control = ?, plate = ?, seats_material = ?, price_per_day = ? " +
                            " WHERE id = ?";
                    PreparedStatement preparedStmt = con.prepareStatement(query);
                    preparedStmt.setString(1, cruise_control);
                    preparedStmt.setString(2, plate);
                    preparedStmt.setString(3, seats_material);
                    preparedStmt.setDouble(4, price_per_day);

                    preparedStmt.setInt(5, toUpdate);   //id
                    preparedStmt.executeUpdate();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
                break;

            default:
                System.out.println("Wrong field");  //never happens tho, just for debugging purposes
        }
        System.out.println("The row has been successfully updated.");
    }

    public static void updateQueryForStringField(String columnName, String value, int toUpdate) {
        try {
            String query = "UPDATE cars SET " + columnName + " = ? WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, value);
            preparedStmt.setInt(2, toUpdate);

            preparedStmt.executeUpdate();
        } catch(SQLException e) {
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
