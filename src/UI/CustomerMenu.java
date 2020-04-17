package UI;

import Main.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CustomerMenu {
	//  Console Inputs
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	//  Instantiate Validation Object
	private static Validation validation = new Validation();

	//  Constructors
	public CustomerMenu() {}

	//	Menu Options
	public static void displayOptions(){
		System.out.println("\t\t\t -MANAGE CUSTOMERS MENU- \t\t\t");
		System.out.println("Choose an option:");
		System.out.println("[1] Display Customers");    //We don't add customers here, only when we create a contract
		System.out.println("[2] Search for Customers");		//we never delete customers tho
		System.out.println("[3] Back to Main Menu");
	}

	public static void displaySearchOptions(){
		System.out.println("Do you wish to perform further operations such as: ");
		System.out.println("[1] Update Customer information");
		System.out.println("[2] Back to Customer Menu");
	}

	public static void displayUpdateOptions() {
		System.out.println("Choose what to modify: ");
		System.out.println("[1] First Name ");
		System.out.println("[2] Last Name");
		System.out.println("[3] Address");
		System.out.println("[4] Mobile Phone");
		System.out.println("[5] Email");
		System.out.println("[6] Driver Licence Number");
		//System.out.println("[7] Zip & City");
		System.out.println("[7] Everything");
		System.out.println("[8] Go back");
	}

	//  UI Methods
	public static void display() {
		String choice = "-1";

		do {
			displayOptions();
			try {
				choice = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			switch (choice) {
				case "1":
					MainMenu.printEmptyLines();
					App.getController().displayCustomers();
					validation.doesStop();
					MainMenu.printEmptyLines();
					break;

//				case "2":
//					MainMenu.printEmptyLines();
//					//App.getController().createCustomer();
//					validation.doesStop();
//					MainMenu.printEmptyLines();
//					break;

				case "2":
					MainMenu.printEmptyLines();
					searchCustomer();
					MainMenu.printEmptyLines();
					break;

				case "3":
					break;

				default:
					MainMenu.printEmptyLines();
					System.out.println("Choice must be a value between \"1\" and \"3\".");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					MainMenu.printEmptyLines();
			}
		} while (!choice.equals("3"));
	}

	public static void searchCustomer(){
		String choice = "-1";
		boolean updated = false;
		boolean doesContinue = false;
		boolean repeat = true;
		boolean found = true;

		do {
			System.out.println("SEARCH CUSTOMER MENU");
			System.out.println("****************************");

			System.out.println("Type the <First Name> or <Last Name> or <Phone Number> of the customer.");
			found = App.getController().searchCustomer();

			if(!found) {
				MainMenu.printEmptyLines();
				continue;
			}

			displaySearchOptions();

			try {
				choice = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			switch (choice) {
				case "1":
					updated = true;
					updateCustomerMenu();
					MainMenu.printEmptyLines();
					break;

//				case "2":
//					App.getController().deleteCustomer();
//					updated = true;
//					MainMenu.printEmptyLines();
//					break;

				case "2":
					break;

				default:
					MainMenu.printEmptyLines();
					System.out.println("Choice must be a value between \"1\" and \"2\".");
					MainMenu.printEmptyLines();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		} while (!choice.equals("2") && !updated);
	}

	public static void updateCustomerMenu(){

		boolean updated = false;
		String choice = "-1";
		int toUpdate = validation.isInsideTable("customers");
		do {
			System.out.println();
			displayUpdateOptions();

			try {
				choice = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			switch(choice) {
				case "1":

					MainMenu.printEmptyLines();
					App.getController().updateCustomer(toUpdate, "first_name");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "2":

					MainMenu.printEmptyLines();
					App.getController().updateCustomer(toUpdate, "last_name");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "3":

					MainMenu.printEmptyLines();
					App.getController().updateCustomer(toUpdate, "address");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "4":
					MainMenu.printEmptyLines();
					App.getController().updateCustomer(toUpdate, "mobile_phone");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "5":
					MainMenu.printEmptyLines();
					App.getController().updateCustomer(toUpdate, "email");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "6":
					MainMenu.printEmptyLines();
					App.getController().updateCustomer(toUpdate, "driver_licence_number");
					updated = true;
					MainMenu.printEmptyLines();
					break;

//				case "7":
//					MainMenu.printEmptyLines();
//					App.getController().updateCustomer(toUpdate, "zip");
//					updated = true;
//					MainMenu.printEmptyLines();
//					break;

				case "7":
					MainMenu.printEmptyLines();
					App.getController().updateCustomer(toUpdate, "everything");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "8":
					break;

				default:
					MainMenu.printEmptyLines();
					System.out.println("Choice must be a value between \"1\" and \"8\".");
					MainMenu.printEmptyLines();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		}  while(!choice.equals("8") && !updated);
	}
}