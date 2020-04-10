package Controller;

import Model.DBConnection;
import UI.Validation;
import File_Management.FileManagement;

public class Controller {

	//Instantiate Objects
	private DBConnection dbC;
	private static FileManagement fm;
	private static Validation validation;

	//Constructors
	public Controller() {
		dbC = new DBConnection();
	}

	public void displayCars() {
		dbC.displayCars();
	}

	public void displayCustomers() {
		dbC.displayCustomers();
	}

	public void displayContracts() {
		dbC.displayContracts();
	}

	public void createCar() {
		//uff
	}
}