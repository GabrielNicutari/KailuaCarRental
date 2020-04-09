package Controller;

import Repository.CarManagement;
import Repository.CustomerManagement;
import Repository.RentalContractManagement;
import UI.Validation;
import Model.Database;
import File_Management.FileManagement;

public class Controller {

	//Instantiate Objects
	private static Database database;
	private static FileManagement fm;
	private static Validation validation;
	private static CustomerManagement customerManagement;
	private static CarManagement carManagement;
	private static RentalContractManagement rentalContractManagement;

	//Constructors
	public Controller() {
		database = new Database();
	}

	public CustomerManagement getCustomerManagement() {
		return customerManagement;
	}

	public CarManagement getCarManagement() {
		return carManagement;
	}

	public RentalContractManagement getRentalContractManagement() {
		return rentalContractManagement;
	}
}