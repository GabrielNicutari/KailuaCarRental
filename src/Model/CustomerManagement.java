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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void create() {
        System.out.println();
        String firstName = validation.getValidatedName("First Name: ");

        String lastName = validation.getValidatedName("Last Name");

        String address = "";
        try {
            System.out.println("Address: ");
            address = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String zip = validation.getValidatedZip("ZIP Code: ");

        String city = validation.getValidatedName("City: ");

        String phoneNr = validation.getValidatedPhone("Phone Number: ");

        String email = validation.getValidatedEmail("Email Address: ");

        String licenceNr = "";
        try {
            System.out.println("Licence Number: ");
            licenceNr = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //it checks if the customer already exists in order not to create duplicates
        try {
            String query = "SELECT driver_licence_number " +
                    "FROM customers ";

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            ArrayList<String> licenceNrList = new ArrayList<>();
            while (rs.next()) {
                    licenceNrList.add(rs.getString("customers.driver_licence_number"));
            }

            if (licenceNrList.contains(licenceNr)) {
                System.out.println("You already exist in our database: ");

                try {
                    query = "SELECT * " +
                            "FROM customers c" +
                            "WHERE driver_licence_number = \"" + licenceNr + "\"";

                    rs = statement.executeQuery(query);

                    System.out.printf("| %-7s| %-20s| %-20s| %-10s| %-30s| %-7s| %-18s| %-13s| %-25s|\n", "ID",
                            "FIRST NAME", "LAST NAME", "PHONE NR", "ADDRESS", "ZIP", "DRIVER LICENCE NR", "DRIVER SINCE", "EMAIL");
                    System.out.println("*****************************************************************************************************************" +
                            "**********************************************************");
                    while (rs.next()) {
                        System.out.printf("| %-7s| %-20s| %-20s| %-10s| %-30s| %-7s| %-18s| %-13s| %-25s|\n",
                                rs.getString("c.id"), rs.getString("c.first_name"), rs.getString("c.last_name"),
                                rs.getString("c.mobile_phone"), rs.getString("c.address"), rs.getString("c.zip"),
                                rs.getString("c.driver_licence_number"), rs.getString("c.driver_since_date"),
                                rs.getString("c.email"));
                    }

                    App.getController().createRentalContract();//it sends the customer back to rental contract menu

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Date driverSince = validation.getValidatedDate("Driver Since Date: ");


        //  --VALIDATE CUSTOMER--
        String asnwer = validation.yesOrNo("\nIs the information correct? Type \"yes/y\" or \"no/n\": ");
        if (asnwer.equals("yes")) {
            String query1 = "INSERT INTO customers " +
                    "VALUES (MAX(id) + 1, \"" + firstName + "\", \"" + lastName + "\"" + address + "\", \"" + zip + "\", \"" + phoneNr
                    + "\", \"" + email + "\", \"" + licenceNr + "\", \"" + driverSince + "\")";

            String query2 = "INSERT INTO zip " +
                    "VALUES (\"" + zip + "\", \"" + city + "\")";

            try {
                Statement statement = con.createStatement();
                statement.executeUpdate(query1);
                statement.executeUpdate(query2);
                System.out.println("The customer information has been saved!");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
