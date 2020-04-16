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
					//searchRentalContract();
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

		do {
			System.out.println("SEARCH RENTAL CONTRACT MENU");
			System.out.println("****************************");

			String column = chooseFilter();	//this is the chosen filter
			if(!column.equals("BACK")) {
				//App.getController().searchRentalContract(column);
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
				return "brand.name";

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

		int toUpdate = validation.getValidatedInt("Type the <ID> of the RentalContract you want to modify");

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
					//App.getController().updateRentalContract(toUpdate,"employeeId");
					MainMenu.printEmptyLines();
					break;

				case "2":

					MainMenu.printEmptyLines();
					//App.getController().updateRentalContract(toUpdate,"shiftIds");
					MainMenu.printEmptyLines();
					break;

				case "3":

					MainMenu.printEmptyLines();
					//App.getController().updateRentalContract(toUpdate,"everything");
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