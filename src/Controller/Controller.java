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

	public void updateCar(int toUpdate, String columnName) {
		dbC.updateCar(toUpdate, columnName);
	}

	public void deleteCar() {
		dbC.deleteRow("cars");
	}



	//		---CUSTOMERS---		\\
	public void displayCustomers() {
		dbC.displayCustomers();
	}

	public boolean searchCustomer() {
        return dbC.searchCustomer();
	}

	public void updateCustomer() {

	}

	public void deleteCustomer() {
		dbC.deleteRow("customers");
	}


	//		---RENTAL CONTRACTS---		\\
	public void displayRentalContracts() {
		dbC.displayRentalContracts();
	}

	public void createRentalContract(boolean alreadyExists, int customerId) {
		dbC.createRentalContract(alreadyExists, customerId);
	}

	public boolean searchRentalContract(String columnName) {
		return dbC.searchRentalContract(columnName);
	}

	public void updateRentalContract() {

	}

	public void deleteRentalContract() {

	}
}