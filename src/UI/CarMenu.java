package UI;

import Main.App;
import Repository.CarManagement;

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

	//  UI Methods
	public static void displayOptions(){
		System.out.println("\t\t\t -MANAGE CAR MENU- \t\t\t");
		System.out.println("[1] Display cars");
		System.out.println("[2] Add car");
		System.out.println("[3] Search for a car");
		System.out.println("[4] Back to main menu");
	}

	public static void displaySearchOptions(){
		System.out.println("Do you wish to perform further operations such as: ");
		System.out.println("[1] Update car information");
		System.out.println("[2] Remove car");
		System.out.println("[3] Back to Car Menu");
	}

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
					//App.getController().displayWorkSchedule();
					validation.doesStop();
					MainMenu.printEmptyLines();
					break;

				case "2":
					MainMenu.printEmptyLines();
					//App.getController().createWorkSchedule();
					validation.doesStop();
					MainMenu.printEmptyLines();
					break;

				case "3":
					MainMenu.printEmptyLines();
					searchCar();
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
		boolean repeat = true;
		boolean updated = false;
		boolean found = true;

		do {
			System.out.println("SEARCH CAR MENU");
			System.out.println("****************************");


			if(repeat)  {
				System.out.println("Type the <Car Brand> or <HP Range> or <Number of Seats> or <Price per day Range>" +
						" of the Car.");
				//found = App.getController().getCarManagement().searchCar();
			}

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
					updateCarMenu();
					repeat = false;
					updated = true;
					MainMenu.printEmptyLines();
					break;

				case "2":
					//repeat = App.getController().deleteWorkSchedule();
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

		int toUpdate = validation.getValidatedInt("Type the <ID> of the Work Schedule you want to modify");

		String choice = "-1";
		do {
			System.out.println();
			System.out.println("Choose what to modify: ");
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
					//App.getController().updateWorkSchedule(toUpdate,"employeeId");
					MainMenu.printEmptyLines();
					break;

				case "2":

					MainMenu.printEmptyLines();
					//App.getController().updateWorkSchedule(toUpdate,"shiftIds");
					MainMenu.printEmptyLines();
					break;

				case "3":

					MainMenu.printEmptyLines();
					//App.getController().updateWorkSchedule(toUpdate,"everything");
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