package UI;

import Main.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;

public class Validation {

    //  Instantiate Objects
    private static Connection con;

    //  Console Input
    Scanner scanner = new Scanner(System.in);
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    //  Constructors
    public Validation(Connection con) {
        this.con = con;
    }

    public Validation() {
        this(con);
    }

    private boolean validateInt(String number) {
        Pattern pattern =  Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    private boolean validateDouble(String number) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    private boolean validateName(String name){
        Pattern pattern = Pattern.compile("[A-Za-z]+");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private boolean validateCpr(String cpr){
        Pattern pattern = Pattern.compile("\\d{6}-\\d{4}");
        Matcher matcher = pattern.matcher(cpr);
        return matcher.matches();
    }

    private boolean validateAge(String age){
        Pattern pattern = Pattern.compile("[1-5]");
        Matcher matcher = pattern.matcher(age);
        return matcher.matches();
    }

    private boolean validateEmail(String email){
        Pattern pattern = Pattern.compile("(.+)@(.+)");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validatePhoneNumber(String phoneNumber){
        Pattern pattern = Pattern.compile("\\d{8}");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    public int getValidatedInt(String message) {
        System.out.println(message);
        String number = scanner.nextLine();
        if(this.validateInt(number)) {
            return Integer.parseInt(number);
        }
        System.out.println();
        return getValidatedInt("Invalid input. Cannot contain any characters other than digits: ");
    }

    public double getValidatedDouble(String message) {
        System.out.println(message);
        String number = scanner.nextLine();
        if (this.validateDouble(number)) {
            return Double.parseDouble(number);
        }
        return getValidatedDouble("Invalid input. It cannot contain any characters other than decimal numbers. (eg: 2.5)");
    }

    public ArrayList<Integer> getValidatedIds (String message) {
        System.out.println(message);
        ArrayList<Integer> ids = new ArrayList<Integer>();

        String input = scanner.nextLine();
        String[] split = input.split("\\s+");   //any spaces
        for(int i = 0; i < split.length; i++) {
            if(!validateInt(split[i])) {
                return getValidatedIds("Invalid Input. IDs can only be figures.");
            }
            ids.add(Integer.parseInt(split[i]));
        }
        return ids;
    }

    public String getValidatedName(String message){
        System.out.println(message);
        String name = "";
        try {
            name = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(this.validateName(name)){
            return name;
        }
        System.out.println("Invalid name. Name cannot contain special characters or numbers");
        return getValidatedName(message);
    }

    public String getValidatedCpr(String message){
        System.out.println(message);
        String cpr = scanner.nextLine();
        if(this.validateCpr(cpr)){
            return cpr;
        }
        System.out.println("Invalid cpr number. Correct format: XXXXXX-XXXX");
        return getValidatedCpr(message);
    }

    public int getValidatedAge(String message){
        System.out.println(message);
        String age = scanner.nextLine();
        if(this.validateAge(age)){
            return Integer.parseInt(age);
        }
        System.out.println("Invalid age. Age cannot contain any characters. Child cannot be older than 6 years old.");
        return getValidatedAge(message);
    }

    public String getValidatedEmail(String message) {
        System.out.println(message);
        String email = scanner.nextLine();
        if(this.validateEmail(email)){
            return email;
        }
        System.out.println("Invalid email address. Try again");
        return getValidatedEmail(message);
    }

    public String getValidatedPhone(String message) {
        System.out.println(message);
        String phoneNumber = scanner.nextLine();
        if(this.validatePhoneNumber(phoneNumber)){
            return phoneNumber;
        }
        System.out.println("Invalid phone number. Please try again! (eg: 12345678)");
        return getValidatedPhone(message);
    }

    public String getValidatedYear(String message) {
        System.out.println(message);
        String year = scanner.nextLine();
        if(Integer.parseInt(year) > 2020 && year.length() == 4){
            return year;
        }
        System.out.println("Invalid year. Year has 4 digits and cannot be the past year");
        return getValidatedYear(message);
    }

    public String getValidatedAnswer(String message) {
        System.out.println(message);
        String answer = scanner.nextLine();
        if(!isNotYesOrNO(answer)) {
            return answer;
        }
        return getValidatedAnswer("Wrong input. Type \"Y/YES\" or \"N/NO\"");
    }

    public String yesOrNo(String message) {
        System.out.println(message);
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
            return "yes";
        } else if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) {
            return "no";
        } else {
            System.out.println();
            return yesOrNo("Invalid answer. Try \"yes/y\" or \"no/n\"!");
        }
    }

    public boolean isNotYesOrNO(String input) {
        return !(input.equalsIgnoreCase("N") || input.equalsIgnoreCase("NO") ||
                input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("YES"));
    }

    public void doesStop() {
        System.out.println();   //Readability
        System.out.println("Do you want to do continue? (Type \"Y/YES\" or \"N/NO\")");

        String input = scanner.next();
        while(isNotYesOrNO(input)) {     //Input Validation
            System.out.println("Wrong input. Type \"Y/YES\" or \"N/NO\"");
            input = scanner.next();
        }

        if(input.equalsIgnoreCase("N") || input.equalsIgnoreCase("NO"))   {
            System.exit(0);
        }
    }

    public Date getValidatedDate (String message) {
        System.out.println(message);
        String datePattern = "([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})";
        String answer = scanner.nextLine();
        boolean match = Pattern.matches(datePattern, answer);

        if (match) {
            java.util.Date date;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(answer);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
                return getValidatedDate("Wrong input. Please try again! (yyyy-MM-dd)");// why?
            }
        } else {
            return getValidatedDate("Wrong input. Please try again! (yyyy-MM-dd)");
        }

    }

    public String getValidatedPlate(String message) {
        System.out.println(message);
        Pattern pattern = Pattern.compile("[A-Z]{2}[0-9]{5}"); //SD12345
        String answer = scanner.nextLine();
        Matcher matcher = pattern.matcher(answer);
        if (matcher.matches()) {
            return answer;
        }
        return getValidatedPlate("Wrong input. Please try again! (eg: AB12345)");
    }

    public String getValidatedZip(String message) {
        Pattern pattern = Pattern.compile("[0-9]{4}"); //2300
        System.out.println(message);
        String zip = null;
        try {
            zip = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matcher matcher = pattern.matcher(zip);

        if (matcher.matches()) {
            return zip;
        } else {
            return getValidatedZip("Wrong ZIP format. Please try again! (eg: 2200)\n ZIP Code: ");
        }
    }

    public int isInsideTable(String tableName) {
        int input = -1;

        input = getValidatedInt("Choose an option: (Type the ID)");

        try {
            Statement statement = con.createStatement();

            String query = "SELECT * FROM " + tableName + " WHERE id = " + input;
            ResultSet rs = statement.executeQuery(query);

            if(rs.next()) { //checks if rs is empty, if it is, it means it found nothing in the table matching the input
                return input;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Invalid input. You can only choose between the listed IDs.");
        return isInsideTable(tableName);
    }

    public boolean doesLicenceExist(String licenceNr) {
        try {
            String query = "SELECT driver_licence_number " +
                    "FROM customers " +
                    "WHERE driver_licence_number = \"" + licenceNr + "\"";

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                System.out.println("The customer already exists!\n");
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean doesZipExist(String zip) {
        Statement statement = null;
        try {
            String query = "SELECT zip " +
                    "FROM zip z " +
                    "WHERE z.zip = \"" + zip + "\"";
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
         return false;
    }
}