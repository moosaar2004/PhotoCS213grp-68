package photo68.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import photo68.view.LoginController;

public class Main extends Application {

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
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public void stop() throws Exception {
		// Call the saveUsersToFile method to save user data before closing the
		// application
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
		LoginController controller = new LoginController();
		loader.setController(controller);
		controller.saveUsersToFile(controller.usersList);

	}

	public static void main(String[] args) {
		launch(args);
	}

}
