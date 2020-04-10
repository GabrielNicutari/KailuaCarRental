package Main;

import Controller.Controller;
import UI.MainMenu;

public class App {

	private static Controller controller;

	public static void main(String[] args) {

		controller = new Controller();
		controller.displayContracts();
		new MainMenu();
	}

	public static Controller getController() {
		return controller;
	}

}