package Model;

import UI.Validation;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.*;

public class RentalContractManagement {

    //  Instantiate Objects
    private static Connection con;
    private static Validation validation;
    private static CarManagement carManagement;
    private static CustomerManagement customerManagement;
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static boolean loop = false;

    //  Constructor
    public RentalContractManagement(Connection con, Validation validation, CarManagement carManagement, CustomerManagement customerManagement) {
        this.con = con;
        this.validation = validation;
        this.carManagement = carManagement;
        this.customerManagement = customerManagement;
    }

    //  Methods
    public void display() {
        try {
            Statement statement = con.createStatement();

            String query =  "SELECT co.id, cu.first_name, cu.last_name, cu.mobile_phone, cu.address, z.zip, z.city, cu.driver_licence_number," +
                            "m.name, b.name, c.plate, c.price_per_day, c.odometer, " +
                            "co.extra_km, co.from_date, co.to_date, co.total_price " +
                    "FROM contracts co, contract_list cl, customers cu, zip z, cars c, models m, brands b " +
                    "WHERE co.id = cl.contract_id AND cl.customer_id = cu.id AND cu.zip = z.zip AND co.car_id = c.id AND c.model_id = m.id AND " +
                            "m.brand_id = b.id";

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
                        rs.getString("m.name"), rs.getString("c.plate"), rs.getString("c.price_per_day"),
                        rs.getString("c.odometer"), rs.getString("co.extra_km"), rs.getString("co.from_date"),
                        rs.getString("co.to_date"), rs.getString("co.total_price"));
            }
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static java.sql.Date convertUtilToSql(java.util.Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

    public void create(boolean alreadyExists, int customerId) {//create the customer as well and the connection in contract_list table
        boolean setLoop = false;
        int contractId = 0;
        String answer = "";
        if (!alreadyExists) {//only when user types no, but the customer already exists
            System.out.println("In order to create a new CONTRACT first type: ");
            answer = validation.yesOrNo("\"yes/y\" or \"no/n\" if the customer already exists: ");

            //  --CREATE CUSTOMER--
            customerId = 0;
            //loop = false;

            if (answer.equals("yes")) {
                System.out.println("In this case, please type the customer's ID from the following: ");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                customerManagement.display();// filter

                System.out.println("\nCUSTOMER ID: ");
                customerId = validation.isInsideTable("customers");

            } else {
                System.out.println("In this case, please type the following: ");
                customerManagement.create();

                try {
                    Statement statement = con.createStatement();
                    String query = "SELECT id " +
                            "FROM customers " +
                            "ORDER BY id";

                    ResultSet rs = statement.executeQuery(query);

                    while (rs.next()) {
                        customerId = Integer.parseInt(rs.getString("id"));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            setLoop = true;
        }

        if (!loop) {

            //  --CREATE CONTRACT--
            System.out.println("\nSelect the desired car's ID: \n");
            carManagement.display();

            System.out.println("\nCAR ID: ");
            int carID = validation.isInsideTable("cars");

            java.util.Date startDate = validation.getValidatedDate("Please type the rental start date (yyyy-MM-dd): ");
            java.sql.Date fromDate = convertUtilToSql(startDate);

            java.util.Date endDate = validation.getValidatedDate("Please type the rental end date (yyyy-MM-dd): ");
            java.sql.Date toDate = convertUtilToSql(endDate);

            String reply = validation.yesOrNo("You have 150 km per day. Would you like to add some EXTRA KM for the entire rent period?" +
                    "(Type \"Y/YES\" or \"N/NO\")");

            int extraKm = 0;//The customer can add an extra amount of km
            if (reply.equals("yes")) {
                System.out.println();
                extraKm = validation.getValidatedInt("Type the extra km: ");
            }

            //Calculate total price
            BigDecimal totalPrice = new BigDecimal(0.0);
            try {
                Statement statement = con.createStatement();

                String query = "SELECT price_per_day " +
                        "FROM cars " +
                        "WHERE cars.id = " + carID;

                ResultSet rs = statement.executeQuery(query);
                double priceDay = 0;
                while(rs.next()) {
                    priceDay = rs.getDouble("price_per_day");
                }

                System.out.println("price/day: " + priceDay);
                int days = (int) (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
                System.out.println("days:" + days);
                double val = priceDay * (days + (double) (extraKm) / 150);
                totalPrice = totalPrice.valueOf(val).setScale(2, BigDecimal.ROUND_HALF_UP);
                System.out.println("TOTAL PRICE: " + totalPrice);

            } catch (SQLException e) {
                e.printStackTrace();
            }


            //  --VALIDATE CONTRACT AND SAVING IN DB--
            answer = validation.yesOrNo("\nType \"yes/y or \"no/n if you want to create the contract: ");
            if (answer.equals("yes")) {

                //Insert values in contracts table
                try {
                    String query = "INSERT INTO contracts " +
                            "VALUES (DEFAULT, " + carID + ", \"" + fromDate + "\", \"" + toDate + "\", " + extraKm + ", " + totalPrice + ", 0)";

                    Statement statement = con.createStatement();

                    statement.executeUpdate(query);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //it returns the contract ID
                try {
                    Statement statement = con.createStatement();
                    String query = "SELECT MAX(id) " +
                            "FROM contracts";

                    ResultSet rs = statement.executeQuery(query);
                    while (rs.next()) {
                        contractId = rs.getInt(1);
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //it stores the customer-contract relation in contract_list table
                try {
                    Statement statement = con.createStatement();

                    String query = "INSERT INTO contract_list " +
                            "VALUES (DEFAULT, " + customerId + ", " + contractId + ")";

                    statement.executeUpdate(query);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.println("The contract information has been saved!");
            } else {
                System.out.println("The contract information has NOT been saved!");
            }
        }

        if (setLoop) {
            loop = true;
        }


    }

    public boolean search(String columnName) {
        switch(columnName) {
            case "customerInfo":
                return searchByCustomer();

            case "brand_name":
                carManagement.displayBrand();
                searchByBrand();
                break;
        }
        return true;
    }

    public static void searchByBrand() {
        int userInput = -1;
        userInput = validation.isInsideTable("brands"); //check if input is found in the table - User Restriction
        try {
            Statement statement = con.createStatement();

            String query = "SELECT * " +
                    "FROM customers cu, cars c, brands b, models m, contracts co, contract_list cl, zip z " +
                    "WHERE b.id = " + userInput + " AND m.brand_id = " + userInput + " AND c.model_id = m.id AND " +
                    "co.car_id = c.id AND cl.contract_id = co.id AND cl.customer_id = cu.id AND z.zip = cu.zip";

            ResultSet rs = statement.executeQuery(query);

            System.out.println();
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
                        rs.getString("m.name"), rs.getString("c.plate"), rs.getString("c.price_per_day"),
                        rs.getString("c.odometer"), rs.getString("co.extra_km"), rs.getString("co.from_date"),
                        rs.getString("co.to_date"), rs.getString("co.total_price"));
            }
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean searchByCustomer() {

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
                    "FROM customers cu , contracts co , contract_list cl, zip z, brands b, models m, cars c " +
                    "WHERE (cu.first_name = " + "\"" + userInput + "\"" +
                    "OR cu.last_name = " + "\"" + userInput + "\"" +
                    "OR cu.mobile_phone = " + "\"" + userInput + "\")" +
                    "AND (cl.contract_id = co.id AND cu.id = cl.customer_id AND cu.zip = z.zip AND c.model_id = m.id " +
                    "AND m.brand_id = b.id AND co.car_id = c.id)" +
                    "ORDER BY co.id";

            ResultSet rs = statement.executeQuery(query);
            ResultSet rs2 = st.executeQuery(query);

            if(rs2.next()) {
                found = true;

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
                            rs.getString("m.name"), rs.getString("c.plate"), rs.getString("c.price_per_day"),
                            rs.getString("c.odometer"), rs.getString("co.extra_km"), rs.getString("co.from_date"),
                            rs.getString("co.to_date"), rs.getString("co.total_price"));
                }
                System.out.println();
            }
            if(!found) {
                System.out.println("No rental contract with this info has been found. ");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void update(int toUpdate, String columnName) {
        int value;
        java.util.Date date;
        switch(columnName) {
            case "car_id":
                carManagement.display();
                System.out.println();
                System.out.println("Type the new <Car ID>");
                value = validation.isInsideTable("cars");
                updateQueryForIntField(columnName, value, toUpdate);
                updatePriceEverywhere(toUpdate);
                break;

            case "extra_km":
                value = validation.getValidatedInt("Please type the new amount of <Extra Km> purchased");
                updateQueryForIntField(columnName, value, toUpdate);
                updatePriceEverywhere(toUpdate);
                break;

            case "from_date":
                date = validation.getValidatedDate("Please type the new <Rental Date>:");
                java.sql.Date from_date = convertUtilToSql(date);
                updateQueryForDateField(columnName, from_date, toUpdate);
                updatePriceEverywhere(toUpdate);
                break;

            case "to_date":
                date = validation.getValidatedDate("Please type the new <Return Date>:");
                java.sql.Date to_date = convertUtilToSql(date);
                updateQueryForDateField(columnName, to_date, toUpdate);
                updatePriceEverywhere(toUpdate);
                break;

            case "everything":
                carManagement.display();
                System.out.println();

                System.out.println("Type the new <Car ID>");
                int id = validation.isInsideTable("cars");
                int extra_km = validation.getValidatedInt("Please type the new amount of <Extra Km> purchased");

                date = validation.getValidatedDate("Please type the new <Rental Date>:");
                from_date = convertUtilToSql(date);

                date = validation.getValidatedDate("Please type the new <Return Date>:");
                to_date = convertUtilToSql(date);

                try {
                    String query = "UPDATE contracts SET car_id = ?, extra_km = ?, from_date = ?, " +
                            "to_date = ? WHERE id = ?";
                    PreparedStatement preparedStmt = con.prepareStatement(query);
                    preparedStmt.setInt(1, id);
                    preparedStmt.setInt(2, extra_km);
                    preparedStmt.setDate(3, from_date);
                    preparedStmt.setDate(4, to_date);
                    updatePriceEverywhere(toUpdate);
                    preparedStmt.setInt(5, toUpdate);

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

    public static void updateQueryForIntField(String columnName, int value, int toUpdate) {
        try {
            String query = "UPDATE contracts SET " + columnName + " = ? WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, value);
            preparedStmt.setInt(2, toUpdate);

            preparedStmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateQueryForDateField(String columnName, Date date, int toUpdate) {
        try {
            String query = "UPDATE contracts SET " + columnName + " = ? WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setDate(1, date);
            preparedStmt.setInt(2, toUpdate);

            preparedStmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updatePriceEverywhere(int toUpdate) {
        try {
            //Get needed values from contracts in order to update the total_price..
            //..based on the new price_per_day of the car
            String selectContract = "SELECT from_date, to_date, extra_km, id " +
                    "FROM contracts WHERE id = " + toUpdate;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(selectContract);

            //Get price_per_day value from the cars table, in order to update total_price in contracts
            String selectCar = "SELECT * FROM cars c, contracts co WHERE c.id = co.car_id AND co.car_id = " + toUpdate;
            Statement carSt = con.createStatement();
            ResultSet rsCar = carSt.executeQuery(selectCar);

            double price = 0;
            while(rsCar.next()) {
                price = rsCar.getDouble("c.price_per_day");
            }

            //Update every contract based on car id, also taking into account its own id
            //eg. we can have 2 contracts for the same car, but one has extra_km and one doesn't..
            //..so the total price differs from one to another
            Date from_date = null;
            Date to_date = null;
            int extra_km = 0, days, id;
            double total_price;

            while(rs.next()) {
                String updateContract = "UPDATE contracts SET total_price = ? WHERE car_id = ? AND id = ?";
                PreparedStatement contractStmt = con.prepareStatement(updateContract);

                from_date = rs.getDate(1);
                to_date = rs.getDate(2);
                extra_km = rs.getInt(3);
                id = rs.getInt(4);

                days = (int) (to_date.getTime() - from_date.getTime()) / (1000 * 60 * 60 * 24);
                total_price = price * (days + ((double) extra_km / 150));

                contractStmt.setDouble(1, total_price);
                contractStmt.setInt(2, toUpdate);
                contractStmt.setInt(3, id);
                contractStmt.executeUpdate();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
