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

	public void displayCars(String table) {
		dbC.displayCars(table);
	}

	public void createCar() {
		//uff
	}
	public Boolean searchCar(String columnName) {
		return dbC.searchCar(columnName);
	}
	public void deleteCar() {
		dbC.deleteRow("cars");
	}
}