package photo68.view;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import photo68.model.User;

public class AdminController {
    @FXML
    Button createButton;
    @FXML
    Button deleteButton;
    @FXML
    Button logout;

    @FXML
    private ListView<String> userListView;
    public ArrayList<User> userList;
    private ObservableList<String> userListObservable;

    /**
     * method to set up the initial state
     */
    public void initialize() {
        // Load users from file during initialization
        userList = loadUsersFromLoginController();
        // Populate the ListView with usernames
        userListObservable = FXCollections.observableArrayList();
        for (User user : userList) {
            userListObservable.add(user.getUserName());
        }
        userListView.setItems(userListObservable);

        // Disable the delete button initially
        deleteButton.setDisable(true);

        // Add a listener to the selection model of the ListView
        userListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Enable the delete button only if a user is selected
            deleteButton.setDisable(newValue == null);
        });

    }

    /**
     * an event handler for the "Create User" button.
     * displays a TextInputDialog to the user, prompting them to enter a username.
     * If the user enters a valid username, the createUser(String username) method is called.
     *
     * @param e
     */
    @FXML
    public void createUserButton(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == createButton) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create User");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter username:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(username -> createUser(username));
        }
    }

    /**
     * an event handler for the "Delete User" button
     * calls the deleteUser() method to remove the selected user from the list.
     * @param e
     */
    @FXML
    public void deleteUserButton(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == deleteButton) {
            deleteUser();
        }
    }

    /**
     * responsible for creating a new user
     * first checks if the username is valid and not already in use,
     * then creates a new User object, adds it to the userList, and saves the updated list to the file
     * adds the new username to the userListView.
     *
     * @param username
     */
    public void createUser(String username) {
        userList = loadUsersFromLoginController();
        if (username.trim().isEmpty()) {
            showAlert("Error", "Please enter a username.");
            return;
        }
        if (userExists(username)) {
            showAlert("Error", "User already exists.");
            return;
        }
        User newUser = new User(username);
        userList.add(newUser);
        saveUsersToFileFromLoginController();

        // Add the new username to the ListView
        ObservableList<String> items = userListView.getItems();
        items.add(newUser.getUserName());

    }

    /**
     * method responsible for deleting a user
     */
    public void deleteUser() {
        String selectedUsername = userListView.getSelectionModel().getSelectedItem();
        if (selectedUsername != null) {
            // Find the user object corresponding to the selected username
            User selectedUser = null;
            for (User user : userList) {
                if (user.getUserName().equals(selectedUsername)) {
                    selectedUser = user;
                    break;
                }
            }
            // Remove the selected user from the userList
            userList.remove(selectedUser);

            // Remove the selected username from the ListView
            userListView.getItems().remove(selectedUsername);
            saveUsersToFileFromLoginController();

        }
    }

    /**
     * utility method to logout user
     *
     * @param e
     */
    @FXML
    public void logout(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == logout) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/photo68/view/LoginView.fxml"));
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

    /**
     * uitility method to show an Alert to the user
     *
     * @param title
     * @param message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * utility method to check if a user exists
     *
     * @param username
     * @return true if username exists, false otherwise
     */
    private boolean userExists(String username) {
        for (User user : userList) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * utility method to load users from file
     *
     * @return users loaded from file
     */
    private ArrayList<User> loadUsersFromLoginController() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/photo68/view/LoginView.fxml"));
        LoginController controller = new LoginController();
        loader.setController(controller);
        return controller.loadUsersFromFile();
    }

    /**
     * utility method to save users to file
     */
    private void saveUsersToFileFromLoginController() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/users.dat"))) {
            oos.writeObject(userList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
