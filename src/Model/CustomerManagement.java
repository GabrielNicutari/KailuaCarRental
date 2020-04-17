package Model;

import Main.App;
import UI.Validation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class CustomerManagement {

    //  Instantiate Objects
    public static Connection con;
    public static Validation validation;

    //  Console Input
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    //  Constructor
    public CustomerManagement(Connection con, Validation validation) {
        this.con = con;
        this.validation = validation;
    }

    //  Methods
    public void display() {
        try {
            Statement statement = con.createStatement();

            String query = "SELECT * " +
                    "FROM customers c, zip z " +
                    "WHERE c.zip = z.zip";

            ResultSet rs = statement.executeQuery(query);

            System.out.printf("| %-7s| %-20s| %-20s| %-10s| %-30s| %-7s| %-15s| %-18s| %-13s| %-25s|\n", "ID",
                    "FIRST NAME", "LAST NAME", "PHONE NR", "ADDRESS", "ZIP", "CITY", "DRIVER LICENCE NR", "DRIVER SINCE", "EMAIL");
            System.out.println("*****************************************************************************************************************" +
                    "*************************************************************************");
            while (rs.next()) {
                System.out.printf("| %-7s| %-20s| %-20s| %-10s| %-30s| %-7s| %-15s| %-18s| %-13s| %-25s|\n",
                        rs.getString("c.id"), rs.getString("c.first_name"), rs.getString("c.last_name"),
                        rs.getString("c.mobile_phone"), rs.getString("c.address"), rs.getString("z.zip"),
                        rs.getString("z.city"), rs.getString("c.driver_licence_number"), rs.getString("c.driver_since_date"),
                        rs.getString("c.email"));
            }
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void create() {
        System.out.println();
        String firstName = validation.getValidatedName("First Name: ");

        String lastName = validation.getValidatedName("Last Name");

        String address = null;
        System.out.println("Address: ");
        try {
            address = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String zip = validation.getValidatedZip("ZIP Code: ");
        boolean zipExists = validation.doesZipExist(zip);

        String city= null;
        if (!zipExists) {
            city = validation.getValidatedName("City: ");
        }

        String phoneNr = validation.getValidatedPhone("Phone Number: ");

        String email = validation.getValidatedEmail("Email Address: ");

        //it checks if the customer already exists in order not to create duplicates
        String licenceNr = null;
        try {
            System.out.println("Licence Number: ");
            licenceNr = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean licenceExists = validation.doesLicenceExist(licenceNr);
        if (licenceExists) {//this means the customer already exists

            int customerId = 0;
            try {
                String query = "SELECT id " +
                        "FROM customers " +
                        "WHERE driver_licence_number = \"" + licenceNr + "\"";

                Statement statement = con.createStatement();

                ResultSet rs = statement.executeQuery(query);


                while(rs.next()) {
                    customerId = rs.getInt("id");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            driverLicenceAlreadyExists(licenceNr);

            App.getController().createRentalContract(true, customerId);
        } else {

            java.sql.Date driverSince = convertUtilToSql(validation.getValidatedDate("Driver Since Date: "));

            int customerID = validateCustomer();

            if (customerID > 0) {
                //save zip and city in zip table
                if (!zipExists) {
                    try {
                        Statement statement2 = con.createStatement();
                        String query2 = "INSERT INTO zip " +
                                "VALUES (\"" + zip + "\", \"" + city + "\")";

                        statement2.executeUpdate(query2);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                //Save the customer information in the db
                try {
                    Statement statement3 = con.createStatement();
                    String query3 = "INSERT INTO customers " +
                            "VALUES (" + customerID + ", \"" + firstName + "\", \"" + lastName + "\", \"" + address + "\", \"" + zip + "\", \"" + phoneNr
                            + "\", \"" + email + "\", \"" + licenceNr + "\", \"" + driverSince + "\", 0)";

                    statement3.executeUpdate(query3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static boolean search() {

        boolean found = false;
        String userInput = null;

        try {
            userInput = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = con.createStatement();
            Statement st = con.createStatement();

            String query = "SELECT * " +
                    "FROM customers c , zip z " +
                    "WHERE (c.zip = z.zip) AND (c.first_name = " + "\"" + userInput + "\"" +
                    " OR c.last_name = " + "\"" + userInput + "\"" +
                    " OR c.mobile_phone = " + "\"" + userInput + "\")";

            ResultSet rs = statement.executeQuery(query);
            ResultSet rs2 = st.executeQuery(query);

            if(rs2.next()) {
                found = true;

                System.out.printf("| %-7s| %-20s| %-20s| %-10s| %-30s| %-7s| %-15s| %-18s| %-13s| %-25s|\n", "ID",
                        "FIRST NAME", "LAST NAME", "PHONE NR", "ADDRESS", "ZIP", "CITY", "DRIVER LICENCE NR", "DRIVER SINCE", "EMAIL");
                System.out.println("*****************************************************************************************************************" +
                        "*************************************************************************");

                while (rs.next()) {
                    System.out.printf("| %-7s| %-20s| %-20s| %-10s| %-30s| %-7s| %-15s| %-18s| %-13s| %-25s|\n",
                            rs.getString("c.id"), rs.getString("c.first_name"), rs.getString("c.last_name"),
                            rs.getString("c.mobile_phone"), rs.getString("c.address"), rs.getString("z.zip"),
                            rs.getString("z.city"), rs.getString("c.driver_licence_number"), rs.getString("c.driver_since_date"),
                            rs.getString("c.email"));
                }
                System.out.println();
            }

            if(!found) {
                System.out.print("No customer with this information has been found.");
                System.out.println();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static java.sql.Date convertUtilToSql(java.util.Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

    public void update(int toUpdate, String columnName) {
        String value = null;
        switch(columnName) {
            case "first_name":
                value = validation.getValidatedName("Please type the new <First Name>");
                updateQueryForStringField(columnName, value, toUpdate);
                break;

            case "last_name":
                value = validation.getValidatedName("Please type the new <Last Name>");
                updateQueryForStringField(columnName, value, toUpdate);
                break;

            case "address":
                System.out.println("Please type the new <Address>");
                try {
                    value = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateQueryForStringField(columnName, value, toUpdate);
                break;

            case "driver_licence_number":
                boolean ok = false;
                try {
                    while(!ok) {
                        System.out.println("Please type the new <Driver Licence Number>");
                        value = br.readLine();
                        if(!validation.doesLicenceExist(value)) {
                            ok = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateQueryForStringField(columnName, value, toUpdate);
                break;

            case "email":
                value = validation.getValidatedEmail("Please type the new <Email>");
                updateQueryForStringField(columnName, value, toUpdate);
                break;

            case "mobile_phone":
                value = validation.getValidatedPhone("Please type the new <Mobile Phone Number>");
                updateQueryForStringField(columnName, value, toUpdate);
                break;

            case "zip":
                value = validation.getValidatedZip("Please type the new <Zip>");
                updateQueryForStringField(columnName, value, toUpdate);

                value = validation.getValidatedName("Please type the new <City>");
                updateQueryForStringField(columnName, value, toUpdate);
                break;

            case "everything":
                String first_name = validation.getValidatedName("Please type the new <First Name>");
                String last_name = validation.getValidatedName("Please type the new <Last Name>");

                String address = null;
                try {
                    System.out.println("Please type the new <Address>");
                    address = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ok = false;
                String driver_licence_number = null;
                try {
                    while(!ok) {
                        System.out.println("Please type the new <Driver Licence Number>");
                        driver_licence_number = br.readLine();
                        if(!validation.doesLicenceExist(driver_licence_number)) {
                            ok = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String email = validation.getValidatedEmail("Please type the new <Email>");
                String mobile_phone = validation.getValidatedPhone("Please type the new <Mobile Phone Number>");
//                String zip = validation.getValidatedZip("Please type the new <Zip>");
//                String city = validation.getValidatedName("Please type the new <City>");
                try {
                    String query = "UPDATE customers SET first_name = ?, last_name = ?, address = ?, " +
                            "driver_licence_number = ?, email = ?, mobile_phone = ?  WHERE id = ?";
                    PreparedStatement preparedStmt = con.prepareStatement(query);
                    preparedStmt.setString(1, first_name);
                    preparedStmt.setString(2, last_name);
                    preparedStmt.setString(3, address);
                    preparedStmt.setString(4, driver_licence_number);
                    preparedStmt.setString(5, email);
                    preparedStmt.setString(6, mobile_phone);
                    preparedStmt.setInt(7, toUpdate);

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
            String query = "UPDATE customers SET " + columnName + " = ? WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, value);
            preparedStmt.setInt(2, toUpdate);

            preparedStmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void driverLicenceAlreadyExists(String licenceNr) {

        try {
            String query = "SELECT * " +
                    "FROM customers c " +
                    "WHERE driver_licence_number = \"" + licenceNr + "\"";
            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery(query);

            System.out.printf("| %-7s| %-20s| %-20s| %-10s| %-30s| %-7s| %-18s| %-13s| %-25s|\n", "ID",
                    "FIRST NAME", "LAST NAME", "PHONE NR", "ADDRESS", "ZIP", "DRIVER LICENCE NR", "DRIVER SINCE", "EMAIL");
            System.out.println("***************************************************************************************************************" +
                    "**********************************************************");
            while (rs.next()) {
                System.out.printf("| %-7s| %-20s| %-20s| %-10s| %-30s| %-7s| %-18s| %-13s| %-25s|\n",
                        rs.getString("c.id"), rs.getString("c.first_name"), rs.getString("c.last_name"),
                        rs.getString("c.mobile_phone"), rs.getString("c.address"), rs.getString("c.zip"),
                        rs.getString("c.driver_licence_number"), rs.getString("c.driver_since_date"),
                        rs.getString("c.email"));
            }
            System.out.println();
            //it sends the customer back to rental contract menu

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int validateCustomer() {
        String answer = validation.yesOrNo("\nIs the information correct? Type \"yes/y\" or \"no/n\": ");
        if (answer.equals("yes")) {

            //get customer id
            int customerID = 0;
            try {
                Statement statement = con.createStatement();
                String query = "SELECT MAX(id) " +
                        "FROM customers";

                ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    customerID = Integer.parseInt(rs.getString(1));
                }
                customerID++;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("The customer information has been saved!");
            return customerID;

        } else {
            System.out.println("The customer information has NOT been saved!");
            return -1;
        }
    }
}

