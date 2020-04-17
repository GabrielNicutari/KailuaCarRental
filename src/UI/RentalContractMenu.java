package UI;

import Main.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RentalContractMenu {
	//  Console Inputs
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	//  Instantiate Validation Object
	private static Validation validation = new Validation();

	//  Constructors
	public RentalContractMenu() {}

	//	Menu Options
	public static void displayOptions(){
		System.out.println("\t\t\t -MANAGE RENTAL CONTRACTS MENU- \t\t\t");
		System.out.println("Choose an option:");
		System.out.println("[1] Display Rental Contracts");
		System.out.println("[2] Create Rental Contract");
		System.out.println("[3] Search for Rental Contracts");
		System.out.println("[4] Back to Main Menu");
	}

	public static void displaySearchOptions(){
		System.out.println("Do you wish to perform further operations such as: ");
		System.out.println("[1] Update Rental Contract information");
		System.out.println("[2] Remove Rental Contract");
		System.out.println("[3] Back to Rental Contract Menu");
	}

	public static void displaySearchFilters() {			//we'll see
		System.out.println("Choose a filter:");
		System.out.println("[1] Brands");
		System.out.println("[2] Customer Info");
		System.out.println("[3] Back to Rental Contracts Menu");
	}

	public static void displayUpdateOptions() {
		System.out.println("Choose what to modify: ");
		System.out.println("[1] Car ");
		System.out.println("[2] Extra Km on rent");
		System.out.println("[3] Rental Date");
		System.out.println("[4] Return Date");
		System.out.println("[5] Everything");
		System.out.println("[6] Go back");
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
					App.getController().displayRentalContracts();
					validation.doesStop();
					MainMenu.printEmptyLines();
					break;

				case "2":
					MainMenu.printEmptyLines();
					App.getController().createRentalContract(false, 0);
					validation.doesStop();
					MainMenu.printEmptyLines();
					break;

				case "3":
					MainMenu.printEmptyLines();
					searchRentalContract();
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

	public static void searchRentalContract(){
		String choice = "-1";
		boolean updated = false;
		boolean doesContinue = false;
		boolean found = true;

		do {
			System.out.println("SEARCH RENTAL CONTRACT MENU");
			System.out.println("****************************");

			String column = chooseFilter();	//this is the chosen filter
			if(!column.equals("BACK")) {
				if(column.equals("customerInfo")) {
					System.out.println("Type the <First Name> or <Last Name> or <Phone Number> of the customer " +
							"assigned to the rental contract.");
					found = App.getController().searchRentalContract("customerInfo");
				} else {
					App.getController().searchRentalContract(column);
				}
				doesContinue = true;
			}

			if(!doesContinue || !found) {
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
					updateRentalContractMenu();
					MainMenu.printEmptyLines();
					break;

				case "2":
					App.getController().deleteRentalContract();
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
				return "brand_name";

			case "2":
				return "customerInfo";

			case "3":
				return "BACK";

			default:
				MainMenu.printEmptyLines();
				System.out.println("Choice must be a value between \"1\" and \"3\".");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				MainMenu.printEmptyLines();
				return chooseFilter();
		}
	}

	public static void updateRentalContractMenu(){

		boolean updated = false;
		String choice = "-1";
		int toUpdate = validation.isInsideTable("contracts");
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
					App.getController().updateRentalContract(toUpdate, "car_id");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "2":

					MainMenu.printEmptyLines();
					App.getController().updateRentalContract(toUpdate, "extra_km");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "3":

					MainMenu.printEmptyLines();
					App.getController().updateRentalContract(toUpdate, "from_date");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "4":
					MainMenu.printEmptyLines();
					App.getController().updateRentalContract(toUpdate, "to_date");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "5":
					MainMenu.printEmptyLines();
					App.getController().updateRentalContract(toUpdate, "everything");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "6":
					break;

				default:
					MainMenu.printEmptyLines();
					System.out.println("Choice must be a value between \"1\" and \"6\".");
					MainMenu.printEmptyLines();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		}  while(!choice.equals("6") && !updated);
	}
}