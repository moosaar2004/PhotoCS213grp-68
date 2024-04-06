package photo68.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

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
import photo68.model.User;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    Button login;

    @FXML
    public void login(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == login) {
            String username = usernameField.getText();
            User currentUser = null;
            ArrayList<User> loadedUsers = loadUsersFromFile();

            for (User user : loadedUsers) {
                if (user.userName.equals(username))
                    currentUser = user;
            }
            if (username.equals("admin")) {
                openAdminView(e);
            } else if (currentUser == null) {
                showAlert("Invalid username", "Please enter a valid username.");
                return;
            } else {

                openUserView(currentUser, e);
            }
        }
    }

    private void openAdminView(ActionEvent e) {
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
    }

    private void openUserView(User user, ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserView.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the UserController
            UserController controller = loader.getController();
            controller.initData(user);
            ArrayList<User> loadedUsers = loadUsersFromFile();
            controller.updateList(loadedUsers);

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @SuppressWarnings("unchecked")
    private ArrayList<User> loadUsersFromFile() {
        // Deserialize users from a file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/users.dat"))) {
            return (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}