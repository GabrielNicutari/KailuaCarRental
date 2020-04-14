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
		System.out.println("[1] Display Customers");
		System.out.println("[2] Add Customer");
		System.out.println("[3] Search for Customers");
		System.out.println("[4] Back to Main Menu");
	}

	public static void displaySearchOptions(){
		System.out.println("Do you wish to perform further operations such as: ");
		System.out.println("[1] Update Customer information");
		System.out.println("[2] Remove Customer");
		System.out.println("[3] Back to Customer Menu");
	}

	public static void displaySearchFilters() {			//we'll see
		System.out.println("Choose a filter:");

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

				case "2":
					MainMenu.printEmptyLines();
					//App.getController().createCustomer();
					validation.doesStop();
					MainMenu.printEmptyLines();
					break;

				case "3":
					MainMenu.printEmptyLines();
					searchCustomer();
					MainMenu.printEmptyLines();
					break;

				case "4":
					break;

				default:
					MainMenu.printEmptyLines();
					System.out.println("Choice must be a value between \"1\" and \"4\".");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					MainMenu.printEmptyLines();
			}
		} while (!choice.equals("4"));
	}

	public static void searchCustomer(){
		String choice = "-1";
		boolean updated = false;
		boolean doesContinue = false;

		do {
			System.out.println("SEARCH CUSTOMER MENU");
			System.out.println("****************************");

			String column = chooseFilter();	//this is the chosen filter
			if(!column.equals("BACK")) {
				//App.getController().searchCustomer(column);
				doesContinue = true;
			}

			if(!doesContinue) {
				MainMenu.printEmptyLines();
				break;
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

				case "2":
					App.getController().deleteCustomer();
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "3":
					break;

				default:
					MainMenu.printEmptyLines();
					System.out.println("Choice must be a value between \"1\" and \"3\".");
					MainMenu.printEmptyLines();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		} while (!choice.equals("3") && !updated);
	}

	public static String chooseFilter() {	//Dunno if we'll use it or not at all
		String choice = "-1";

		displaySearchFilters();
		try {
			choice = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (choice) {
			case "1":
				return "brand.name";

			case "2":
				return "cars.hp";

			case "3":
				return "cars.number_seats";

			case "4":
				return "cars.price_per_day";

			case "5":
				return "BACK";

			default:
				MainMenu.printEmptyLines();
				System.out.println("Choice must be a value between \"1\" and \"5\".");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				MainMenu.printEmptyLines();
				return chooseFilter();
		}
	}

	public static void updateCustomerMenu(){

		int toUpdate = validation.getValidatedInt("Type the <ID> of the Customer you want to modify");

		String choice = "-1";
		do {
			System.out.println();
			System.out.println("Choose what to modify: ");		//holy shit, a lot of things
			System.out.println("[1] Employee ID ");
			System.out.println("[2] Shift IDs");
			System.out.println("[3] Everything");
			System.out.println("[4] Go back");

			try {
				choice = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			switch(choice) {
				case "1":

					MainMenu.printEmptyLines();
					//App.getController().updateCustomer(toUpdate,"employeeId");
					MainMenu.printEmptyLines();
					break;

				case "2":

					MainMenu.printEmptyLines();
					//App.getController().updateCustomer(toUpdate,"shiftIds");
					MainMenu.printEmptyLines();
					break;

				case "3":

					MainMenu.printEmptyLines();
					//App.getController().updateCustomer(toUpdate,"everything");
					MainMenu.printEmptyLines();
					break;

				case "4":
					break;

				default:
					MainMenu.printEmptyLines();
					System.out.println("Choice must be a value between \"1\" and \"4\".");
					MainMenu.printEmptyLines();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		}  while(!choice.equals("4"));
	}
}