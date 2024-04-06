package photo68.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import photo68.view.UserController;

public class Main extends Application {

	public void start(Stage primaryStage) throws Exception {
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("UserView.fxml"));
		UserController controller = loader.getController();
		controller.saveUsersToFile(controller.listOfUsers);

	}

	public static void main(String[] args) {
		launch(args);
	}

}
