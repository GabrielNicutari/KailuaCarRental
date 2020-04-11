package UI;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    //  Instantiate Objects
    private static Connection con;

    //  Console Input
    private static Scanner scanner = new Scanner(System.in);

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
        System.out.println("Invalid input. Cannot contain any characters other than figures");
        return getValidatedInt(message);
    }

    public double getValidatedDouble(String message) {
        System.out.println(message);
        String number = scanner.nextLine();
        if (this.validateDouble(number)) {
            return Double.parseDouble(number);
        }
        return getValidatedDouble("Invalid input. Salary cannot contains any characters other than figures.");
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
        String name = scanner.nextLine();
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
        System.out.println("Invalid phone number. Should have 8 digits. Try again");
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
        return getValidatedAnswer("Wrong input. Type \\\"Y/YES\\\" or \\\"N/NO\\\"\"");
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
            System.out.println("Wrong input. Type Type \"Y/YES\" or \"N/NO\"");
            input = scanner.next();
        }

        if(input.equalsIgnoreCase("N") || input.equalsIgnoreCase("NO"))   {
            System.exit(0);
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
}