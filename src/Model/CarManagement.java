package Model;

import UI.MainMenu;
import UI.Validation;

import java.sql.*;

public class CarManagement {

    //  Instantiate Objects
    private static Connection con;
    private static Validation validation;


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
                    "FROM cars c, brands b , models m " +
                    "WHERE b.id = m.brand_id and m.id = c.model_id " +
                    "ORDER BY c.id";

            ResultSet rs = statement.executeQuery(query);

            System.out.printf("| %-7s| %-15s| %-15s| %-23s| %-10s| %-15s| %-15s| %-15s| %-15s| %-9s| %-15s| %-12s| %-13s| %-11s|\n", "Car_ID",
                    "BRAND", "MODEL", "ENGINE CAPACITY", "HP", "FUEL TYPE", "ODOMETER", "AUTOMATIC", "PRICE/DAY", "NR_SEATS",
                    "SEATS MATERIAL", "CRUISE CTRL", "PLATE", "REG DATE");
            System.out.println("**************************************************************************************************************" +
                    "*************************************************************************************************************");
            while(rs.next()) {
                System.out.printf("| %-7s| %-15s| %-15s| %-23s| %-10s| %-15s| %-15s| %-15s| %-15s| %-9s| %-15s| %-12s| %-13s| %-11s|\n",
                        rs.getString("c.id"),rs.getString("b.name"), rs.getString("m.name"), rs.getString("c.litre_engine"),
                        rs.getString("c.hp"), rs.getString("c.fuel_type"), rs.getString("c.odometer"),
                        rs.getString("c.automatic_gear"), rs.getString("c.price_per_day"), rs.getString("c.number_seats"),
                        rs.getString("c.seats_material"), rs.getString("c.cruise_control"), rs.getString("c.plate"),
                        rs.getString("c.registration_date"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static java.sql.Date convertUtilToSql(java.util.Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

    public void create () {
        //display all the brands from the db
        try {
            System.out.println("In order to create a new CAR please enter the following:");
            System.out.printf("|%-5s| %-15s|\n", "ID", "BRAND");
            System.out.println("*************************");

            Statement statement = con.createStatement();

            String query = "SELECT * " +
                    "FROM brands b";

            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
                System.out.printf("|%-5s| %-15s|\n", rs.getString("b.id"), rs.getString("b.name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\nPlease type the ID of the BRAND you want to choose: ");
        int brandID = validation.isInsideTable("brands");

        //Saving the brand name to display it in the top of the models table

        String brand = "";
        try {
            String query = "SELECT name " +
                    "FROM brands " +
                    "WHERE brands.id = " + brandID;

            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                brand = rs.getString("name");
            }

            System.out.println();

            int width = 25;
            String s = brand + " Models";

            int padSize = width - s.length();
            int padStart = s.length() + padSize / 2;
            s = String.format("%" + padStart + "s", s);
            s = String.format("%-" + width  + "s", s);
            System.out.println(s);
            System.out.println("*************************");

        } catch (SQLException e) {
            e.printStackTrace();
        }


        //display all the models based on the brand id chosen by the user;

        try {
            System.out.printf("|%-5s| |%-15s|\n", "ID", "MODEL");
            System.out.println("*************************");

            String query = "SELECT id, name " +
                    "FROM models m " +
                    "WHERE m.brand_id = " + brandID;

            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
                System.out.printf("|%-5s| |%-15s|\n", rs.getString("m.id"), rs.getString("m.name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\nPlease type the ID of the MODEL you want to add: ");
        int modelID = validation.isInsideTable("models");

        String engineCap = null;
        if (!brand.equals("Tesla")) {
            engineCap = Double.toString(validation.getValidatedDouble("Please type the engine capacity in litres: "));
        } else {
            engineCap = "electrical";
        }

        int horsePower = validation.getValidatedInt("Please type the output of the engine (in horsepower): ");

        String automaticGear = validation.yesOrNo("Please type \"yes/y\" or \"no/n\" for automatic gear: ");

        String fuelType = validation.getValidatedName("Please enter the fuel type: ");

        int odometer = validation.getValidatedInt("Please type existing number of kilometers (odometer): ");

        String plate = validation.getValidatedPlate("Please type the registration number (eg: AB12345):");

        java.sql.Date registrationDate = convertUtilToSql(validation.getValidatedDate("Please type the first registration date (yyyy-MM-dd): "));

        int numberSeats = validation.getValidatedInt("Please type the number of seats: ");

        String cruiseControl = validation.yesOrNo("Please type \"yes/y\" or \" no/n\" for cruise control: ");

        String seatsMaterial = validation.getValidatedName("Please type the seats material: ");

        double priceDay = validation.getValidatedDouble("Please type the price per day of the car: ");

        //  --VALIDATION AND ADDING IN DB--
        String answer = validation.yesOrNo("Are you sure you want to add the car into the database? (Type \"Y/YES\" or \"N/NO\")");
        if (answer.equals("no")) {
            System.out.println("The data has NOT been saved!");
        } else {
            try {
                String query = "INSERT INTO cars " +
                        "VALUES (DEFAULT, " + modelID +", \"" + engineCap + "\", " + horsePower + ", \"" + automaticGear + "\", \"" + fuelType +"\", " + odometer +
                        ", \"" + plate + "\", \"" + registrationDate + "\", " + numberSeats + ", \"" + cruiseControl + "\", \"" +
                        seatsMaterial + "\", " + priceDay + ")";

                Statement statement = con.createStatement();

                statement.executeUpdate(query);

                System.out.println("The data has been saved!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        System.out.println("******************************");

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
                    "FROM cars c, brands b, models m " +
                    "WHERE b.id = " + userInput + " AND m.brand_id = " + userInput + " AND c.model_id = m.id";

            ResultSet rs = statement.executeQuery(query);

            System.out.println();
            System.out.printf("| %-7s| %-15s| %-15s| %-23s| %-10s| %-15s| %-15s| %-15s| %-15s| %-9s| %-15s| %-12s| %-13s| %-11s|\n", "Car_ID",
                    "BRAND", "MODEL", "ENGINE CAPACITY", "HP", "FUEL TYPE", "ODOMETER", "AUTOMATIC", "PRICE/DAY", "NR_SEATS",
                    "SEATS MATERIAL", "CRUISE CTRL", "PLATE", "REG DATE");
            System.out.println("**************************************************************************************************************" +
                    "*************************************************************************************************************");

            while(rs.next()) {
                System.out.printf("| %-7s| %-15s| %-15s| %-23s| %-10s| %-15s| %-15s| %-15s| %-15s| %-9s| %-15s| %-12s| %-13s| %-11s|\n",
                        rs.getString("c.id"),rs.getString("b.name"), rs.getString("m.name"), rs.getString("c.litre_engine"),
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
                updatePriceEverywhere(columnName, price, toUpdate);
                break;

            case "everything":
                String cruise_control = validation.getValidatedAnswer("Do you want the car to have Cruise Control? (" +
                        "Type \"Y/YES\" or \"N/NO\")");
                String plate = validation.getValidatedPlate("Please type the Registration Number (eg: AB12345):");
                String seats_material = validation.getValidatedName("Please type the Seats' Material:");
                double price_per_day = validation.getValidatedDouble("Please type the Price Per Day of the car: ");
                try {
                    String query = "UPDATE cars SET cruise_control = ?, plate = ?, seats_material = ? WHERE id = ?";
                    PreparedStatement preparedStmt = con.prepareStatement(query);
                    preparedStmt.setString(1, cruise_control);
                    preparedStmt.setString(2, plate);
                    preparedStmt.setString(3, seats_material);
                    updatePriceEverywhere("price_per_day", price_per_day, toUpdate);
                    //we need this because the contracts table has to be updated as well, not just the price_per_day
                    preparedStmt.setInt(4, toUpdate);

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

    private static void updatePriceEverywhere(String columnName, double price, int toUpdate) {
        try {
            //Update Car price_per_day
            String query = "UPDATE cars SET " + columnName + " = ? WHERE id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);   //for car
            preparedStmt.setDouble(1, price);
            preparedStmt.setInt(2, toUpdate);
            preparedStmt.executeUpdate();


            //Get needed values from contracts in order to update the total_price..
            //..based on the new price_per_day of the car
            String selectContract = "SELECT from_date, to_date, extra_km, id " +
                    "FROM contracts WHERE car_id = " + toUpdate;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(selectContract);


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
