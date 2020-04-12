package Model;

import UI.Validation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RentalContractManagement {

    //  Instantiate Objects
    public static Connection con;
    private static Validation validation;
    public CustomerManagement customerManagement = new CustomerManagement(con);
    public CarManagement carManagement = new CarManagement(con, validation);

    //  Console Input
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    //  Constructor
    public RentalContractManagement(Connection con, Validation validation) {
        this.con = con;
        this.validation = validation;
    }

    //  Methods
    public void display() {
        try {
            Statement statement = con.createStatement();

            String query =  "SELECT co.id, cu.first_name, cu.last_name, cu.mobile_phone, cu.address, z.zip, z.city, cu.driver_licence_number," +
                            "b.name, b.model, c.plate, c.price_per_day, c.odometer, " +
                            "co.extra_km, co.from_date, co.to_date, co.total_price " +
                    "FROM contracts co, contract_list cl, customers cu, zip z, cars c, brands b " +
                    "WHERE co.id = cl.contract_id AND cl.customer_id = cu.id AND cu.zip = z.zip AND co.car_id = c.id AND c.brand_id = b.id";

            ResultSet rs = statement.executeQuery(query);

            System.out.printf("| %-12s| %-20s| %-20s| %-10s| %-30s| %-7s| %-15s| %-18s| %-13s| %-15s| %-8s| %-11s| %-9s| %-9s| %-11s| " +
                            "%-11s| %-12s|\n", "CONTRACT ID", "FIRST NAME", "LAST NAME", "PHONE NR", "ADDRESS", "ZIP", "CITY",
                    "DRIVER LICENCE NR", "BRAND", "MODEL","PLATE", "PRICE/DAY", "ODOMETER", "EXTRA KM", "FROM", "TO", "TOTAL PRICE");
            System.out.println("************************************************************************************************************************************" +
                    "**************************************************************************************************************************************");
            while(rs.next()) {
                System.out.printf("| %-12s| %-20s| %-20s| %-10s| %-30s| %-7s| %-15s| %-18s| %-13s| %-15s| %-8s| %-11s| %-9s| %-9s| %-11s| " +
                                "%-11s| %-12s|\n", rs.getString("co.id"),rs.getString("cu.first_name"), rs.getString("cu.last_name"),
                        rs.getString("cu.mobile_phone"), rs.getString("cu.address"), rs.getString("z.zip"),
                        rs.getString("z.city"), rs.getString("cu.driver_licence_number"), rs.getString("b.name"),
                        rs.getString("b.model"), rs.getString("c.plate"), rs.getString("c.price_per_day"),
                        rs.getString("c.odometer"), rs.getString("co.extra_km"), rs.getString("co.from_date"),
                        rs.getString("co.to_date"), rs.getString("co.total_price"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static java.sql.Date convertUtilToSql(java.util.Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

    public void create() {//create the customer as well and the connection in contract_list table
        System.out.println("In order to create a new CONTRACT first type: ");
        String answer = validation.yesOrNo("\"yes/y\" or \"no/n\" if the customer already exists: ");

        int customerID, carID;
        if (answer.equals("yes")) {
            System.out.println("In this case, please type the customer's ID from the following: ");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            customerManagement.display();

            System.out.println("\n CUSTOMER ID: ");
            customerID = validation.isInsideTable("customers");

        } else {
            System.out.println("In this case, please type the following: ");

            System.out.println("Select the desired car's ID: ");
            carManagement.display();

            System.out.println("\n CAR ID: ");
            carID = validation.isInsideTable("cars");

            java.sql.Date fromDate = convertUtilToSql(validation.getValidatedDate("Please type the rental start date (yyyy-MM-dd): "));

            java.sql.Date toDate = convertUtilToSql(validation.getValidatedDate("Please type the rental end date (yyyy-MM-dd): "));

            String reply = validation.yesOrNo("You have 150 km per day. Would you like to add some extra km for the entire rent period?");

            int extraKm = 0;
            if (reply.equals("yes")) {
                System.out.println();
                extraKm = validation.getValidatedInt("Type the extra km: ");
            }
            //total price



        }



    }
}
