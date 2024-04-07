package photo68.app;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import photo68.model.User;
import photo68.view.LoginController;

public class Main extends Application {

	public void start(Stage primaryStage) throws Exception {
		// Just for intialization for stock

		ArrayList<User> ba = new ArrayList<>();
		User stock = new User("stock");
		ba.add(stock);
		FXMLLoader loader1 = new FXMLLoader(getClass().getResource("LoginView.fxml"));
		LoginController controller = new LoginController();
		loader1.setController(controller);
		controller.saveUsersToFile(ba);

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

	private static class ExitHandler implements EventHandler<WindowEvent> {
		// Call the saveUsersToFile method to save user data before closing the
		// application
		@Override
		public void handle(WindowEvent event) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
			LoginController controller = new LoginController();
			loader.setController(controller);
			controller.saveUsersToFile(controller.usersList);

		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}
