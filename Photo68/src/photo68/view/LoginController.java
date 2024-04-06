package photo68.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    Button login;

    public void login(ActionEvent e) {
        String username = usernameField.getText();
        // Handle login logic
        Button b = (Button) e.getSource();
        if (b == login) {
            if (!isValidUsername(username)) {
                showAlert("Invalid username", "Please enter a valid username.");
                return; // Exit the method without proceeding to login
            } else if (username.equals("admin")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminView.fxml"));
                    Parent root = loader.load();
                    // Create a new scene with the loaded FXML file
                    Scene scene = new Scene(root);

                    // Get the stage (window) from the event source
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

                    // Set the new scene
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException event) {
                    event.printStackTrace();
                }
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("UserView.fxml"));
                    Parent root = loader.load();
                    // Create a new scene with the loaded FXML file
                    Scene scene = new Scene(root);

                    // Get the stage (window) from the event source
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

                    // Set the new scene
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        }
    }

    private boolean isValidUsername(String username) {
        // You can implement your validation logic here
        // For example, check if the username is 'admin'
        return "admin".equals(username);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
