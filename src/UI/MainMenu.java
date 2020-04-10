package UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainMenu {
	
	//Console Inputs
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	//Constructors
	public MainMenu() {
		mainMenuAdmin();
	}

	//Methods
	private static void displayMainMenuAdmin(){
		System.out.println("\t\t\t -MENU- \t\t\t");
		System.out.println("Choose an option:");
		System.out.println("1. Manage customers menu");
		System.out.println("2. Manage cars menu");
		System.out.println("3. Manage contracts menu");
		System.out.println("4. Exit");
	}

	public static void mainMenuAdmin(){
		String choice = "-1";
		do {
			displayMainMenuAdmin();

			try {
				choice = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			switch (choice) {
				case "1":
					printEmptyLines();
					//CustomerMenu.display();
					printEmptyLines();
					break;

				case "2":
					printEmptyLines();
					CarMenu.display();
					printEmptyLines();
					break;

				case "3":
					printEmptyLines();
					//RentalContractsMenu.display();
					printEmptyLines();
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

	public static void printEmptyLines() {
		System.out.println();
		System.out.println();
		System.out.println();
	}
}