package Model;

public class Database {

	//Instantiate Objects
	private DBConnection dbC;

	//Constructors
	public Database() {
		dbC = new DBConnection();
	}

	//Methods
	public void displayCars(String table) {
		dbC.displayCars(table);
	}
}