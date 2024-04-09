package photo68.view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

/**
 * The `LoginController` class is responsible for handling the login functionality of the application.
 * It loads the list of users from a file, validates the user's input, and opens the appropriate view (Admin or User) based on the user's credentials.
 */
public class LoginController {
    public ArrayList<User> usersList;
    @FXML
    private TextField usernameField;

    @FXML
    Button login;

    /**
     * Constructor that loads the list of users from the file.
     */
    public LoginController() {
        this.usersList = loadUsersFromFile();
    }

    /**
     * Handles the login button click event.
     * Retrieves the username from the text field, checks if the user is an admin or a regular user, and opens the appropriate view.
     *
     * @param e the ActionEvent triggered by the login button click
     */    
    @FXML
    public void login(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == login) {
            String username = usernameField.getText();
            User currentUser = null;
            usersList = loadUsersFromFile();

            for (User user : usersList) {
                if (user.userName.equals(username))
                    currentUser = user;
            }
            if (username.equals("admin")) {
                openAdminView(e);
            } else if (currentUser == null) {
                showAlert("Invalid username", "Please enter a valid username.");
                return;
            } else {

                openUserView(currentUser, e, usersList);
            }
        }
    }

    /**
     * Opens the AdminView by loading the FXML file and creating a new scene.
     *
     * @param e the ActionEvent that triggered the method
     */    
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
            stage.setResizable(true);
            stage.show();
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    /**
     * Opens the UserView by loading the FXML file, creating a new scene, and passing the authenticated user and user list to the UserController.
     *
     * @param user       the authenticated user
     * @param e          the ActionEvent that triggered the method
     * @param usersList  the list of users
     */    
    private void openUserView(User user, ActionEvent e, ArrayList<User> usersList) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("UserView.fxml")); // Set the location before loading
            Parent root = loader.load();
            // Pass the authenticated user to the UserController
            UserController controller = loader.getController();
            controller.initData(user, usersList);
            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the stage (window) from the event source
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    /**
     * Displays an error alert with the given title and message.
     *
     * @param title   the title of the alert
     * @param message the message to be displayed in the alert
     */    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Loads the list of users from a file.
     *
     * @return the list of users loaded from the file, or null if an exception occurs
     */
    @SuppressWarnings("unchecked")
    public ArrayList<User> loadUsersFromFile() {
        // Deserialize users from a file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/users.dat"))) {
            return (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves the list of users to a file.
     *
     * @param users the list of users to be saved
     */
    public void saveUsersToFile(ArrayList<User> users) {
        // Serialize users to a file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}