package photo68.app;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import photo68.model.User;
import photo68.view.LoginController;

public class Photos extends Application {

	/**
	 * Starts the JavaFX application.
	 * Loads the LoginView FXML file, creates a new scene, and sets it on the primary stage.
	 *
	 * @param primaryStage the primary stage of the JavaFX application
	 * @throws Exception if an error occurs while loading the FXML file
	 */
	public void start(Stage primaryStage) throws Exception {
		// Just for intialization for stock
		/*
		 * ArrayList<User> ba = new ArrayList<>();
		 * User stock = new User("stock");
		 * ba.add(stock);
		 * FXMLLoader loader1 = new
		 * FXMLLoader(getClass().getResource("LoginView.fxml"));
		 * LoginController controller = new LoginController();
		 * loader1.setController(controller);
		 * controller.saveUsersToFile(ba);
		 */
		// create FXML loader
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/photo68/view/LoginView.fxml"));

		// load fmxl, root layout manager in fxml file is GridPane
		GridPane root = (GridPane) loader.load();

		// set scene to root
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.show();
	}

	/**
	 * The main method that launches the JavaFX application.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args)
	{
		launch(args);
	}

}
