package UI;

import Main.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CarMenu {

	//  Console Inputs
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	//  Instantiate Validation Object
	private static Validation validation = new Validation();

	//  Constructors
	public CarMenu() {}

	//	Menu Options
	public static void displayOptions(){
		System.out.println("\t\t\t -MANAGE CARS MENU- \t\t\t");
		System.out.println("Choose an option:");
		System.out.println("[1] Display Cars");
		System.out.println("[2] Add Car");
		System.out.println("[3] Search for a Car");
		System.out.println("[4] Back to Main Menu");
	}

	public static void displaySearchOptions(){
		System.out.println("Do you wish to perform further operations such as: ");
		System.out.println("[1] Update Car information");
		System.out.println("[2] Remove Car");
		System.out.println("[3] Back to Car Menu");
	}

	public static void displaySearchFilters() {
		System.out.println("Choose a filter:");
		System.out.println("[1] Car Brand");
		System.out.println("[2] HP Range");
		System.out.println("[3] Number of seats");
		System.out.println("[4] Price per day Range");
		System.out.println("[5] Back to Car Menu");
	}

	public static void displayUpdateOptions() {
		System.out.println("Choose what to modify: ");
		System.out.println("[1] Cruise control ");
		System.out.println("[2] Plate");
		System.out.println("[3] Price per Day");
		System.out.println("[4] Seats material");
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
					App.getController().displayCars();
					validation.doesStop();
					MainMenu.printEmptyLines();
					break;

				case "2":
					MainMenu.printEmptyLines();
					App.getController().createCar();
					validation.doesStop();
					MainMenu.printEmptyLines();
					break;

				case "3":
					MainMenu.printEmptyLines();
					//searchCar();
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

	public static void searchCar(){
		String choice = "-1";
		boolean updated = false;
		boolean doesContinue = false;

		do {
			System.out.println("SEARCH CAR MENU");
			System.out.println("****************************");

			String column = chooseFilter();	//this is the chosen filter
			if(!column.equals("BACK")) {
				App.getController().searchCar(column);
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
					updateCarMenu();
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "2":
					App.getController().deleteCar();
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

	public static void updateCarMenu(){
		boolean updated = false;
		String choice = "-1";
		int toUpdate = validation.isInsideTable("cars");
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
					App.getController().updateCar(toUpdate, "cruise_control");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "2":

					MainMenu.printEmptyLines();
					App.getController().updateCar(toUpdate, "plate");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "3":

					MainMenu.printEmptyLines();
					App.getController().updateCar(toUpdate, "price_per_day");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "4":
					MainMenu.printEmptyLines();
					App.getController().updateCar(toUpdate, "seats_material");
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "5":
					MainMenu.printEmptyLines();
					App.getController().updateCar(toUpdate, "everything");
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

	public static String chooseFilter() {
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
}