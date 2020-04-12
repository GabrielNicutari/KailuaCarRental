package Controller;

import Model.DBConnection;
import UI.Validation;
import File_Management.FileManagement;

public class Controller {

	//	Instantiate Objects
	private DBConnection dbC;
	private static FileManagement fm;

	//	Constructors
	public Controller() {
		dbC = new DBConnection();
	}


	//		---CARS---		\\
	public void displayCars() {
		dbC.displayCars();
	}

	public void createCar() {
		dbC.createCar();
	}

	public void searchCar(String columnName) {
		dbC.searchCar(columnName);
	}

	public void updateCar() {
		dbC.updateCar();
	}

	public void deleteCar() {
		dbC.deleteRow("cars");
	}



	//		---CUSTOMERS---		\\
	public void displayCustomers() {
		dbC.displayCustomers();
	}


	public void searchCustomer() {

	}

	public void updateCustomer() {

	}

	public void deleteCustomer() {

	}


	//		---RENTAL CONTRACTS---		\\
	public void displayRentalContracts() {
		dbC.displayRentalContracts();
	}

	public void createRentalContract() {
		dbC.createRentalContract();
	}

	public void searchRentalContract() {

	}

	public void updateRentalContract() {

	}

	public void deleteRentalContract() {

	}
}