package Controller;

import UI.Validation;
import Model.Database;
import File_Management.FileManagement;

public class Controller {

	//Instantiate Objects
	private static Database database;
	private static FileManagement fm;
	private static Validation validation;

	//Constructors
	public Controller() {
		database = new Database();
	}


	public void displayCars(String table) {
		database.displayCars(table);
	}

	public void createCar() {
		//uff
	}
}