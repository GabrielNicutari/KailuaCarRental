package Model;

import Main.App;
import UI.Validation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class CustomerManagement {

    //  Instantiate Objects
    public static Connection con;
    public static Validation validation;



    //  Console Input
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    Scanner scanner = new Scanner(System.in);

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static java.sql.Date convertUtilToSql(java.util.Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
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
                            + "\", \"" + email + "\", \"" + licenceNr + "\", \"" + driverSince + "\")";

                    statement3.executeUpdate(query3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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

