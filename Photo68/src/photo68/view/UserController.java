package photo68.view;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import photo68.model.Album;
import photo68.model.User;

/**
 * The `UserController` class is responsible for managing the user's albums, including creating, deleting, renaming, and opening albums.
 * It also handles the logout functionality and saving the updated user data to a file.
 */
public class UserController {
    @FXML
    private ListView<String> albumListView;
    @FXML
    Button createButton;
    @FXML
    Button deleteButton;
    @FXML
    Button openButton;
    @FXML
    Button renameButton;
    @FXML
    Button logout;
    private ObservableList<String> albumListObservable;
    private User currentUser;
    private ArrayList<User> controllerList;

    /**
     * Initializes the controller with the current user and the list of all users.
     * Populates the album ListView with the current user's albums and sets up the button states.
     *
     * @param user           the current user
     * @param controlLolerList the list of all users
     */
    public void initData(User user, ArrayList<User> controlLolerList) {
        this.currentUser = user;
        this.controllerList = controlLolerList;
        // Use currentUser to initialize the view with user-specific data
        albumListObservable = FXCollections.observableArrayList();
        for (Album a : currentUser.userAlbums) {
            String albumCompleteInfo = a.getNameOfAlbum() + "\nDate Range: " + a.firstDate() + " To " + a.lastDate()
                    + "\n Number of Photos: " + a.getNumOfPhotos();
            albumListObservable.add(albumCompleteInfo);
        }
        albumListView.setItems(albumListObservable);

        // Disable the delete button initially
        openButton.setDisable(true);
        deleteButton.setDisable(true);
        renameButton.setDisable(true);

        // Add a listener to the selection model of the ListView
        albumListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Extract the album name from the selected item

            // Enable the delete button only if a user is selected
            openButton.setDisable(newValue == null);
            deleteButton.setDisable(newValue == null);
            renameButton.setDisable(newValue == null);
        });
    }

    /**
     * Handles the "Create Album" button click.
     * Opens a dialog to prompt the user for the new album name, creates the album, and updates the UI.
     */
    @FXML
    public void createAlbum() {
        // Open a dialog to prompt the user for the album name
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the name of the new album:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(albumName -> {
            // Check if the album name is not empty
            if (!albumName.trim().isEmpty()) {
                // Create the album
                if (!currentUser.doesAlbumExist(albumName)) {
                    currentUser.addUserAlbum(new Album(albumName));
                    initData(currentUser, controllerList);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Album name already exists.");
                }
            } else {
                // Display error message if the album name is empty
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid album name.");
            }
        });
        saveUsers();
    }

    /**
     * Handles the "Delete Album" button click.
     * Removes the selected album from the current user's album list and updates the UI.
     */
    @FXML
    public void deleteAlbum() {
        String selectedAlbumName = albumListView.getSelectionModel().getSelectedItem().split("\n")[0];
        Album selectedAlbum = currentUser.findAlbumByName(selectedAlbumName);
        // Check if an album is selected
        if (selectedAlbum != null) {
            // Remove the selected album from the data model
            currentUser.removeUserAlbum(selectedAlbum);
            initData(currentUser, controllerList);

        }
        saveUsers();

    }

    /**
     * Handles the "Open Album" button click.
     * Loads the AlbumView FXML file, creates a new scene, and passes the selected album to the AlbumController.
     *
     * @param e the ActionEvent triggered by clicking the "Open Album" button
     */
    @FXML
    public void openAlbum(ActionEvent e) {
        String selectedAlbumName = albumListView.getSelectionModel().getSelectedItem().split("\n")[0];
        Album selectedAlbum = currentUser.findAlbumByName(selectedAlbumName);
        if (selectedAlbum != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/photo68/view/AlbumView.fxml"));
                Parent root = loader.load();

                // Get the controller associated with the loaded FXML
                AlbumController controller = loader.getController();

                // Pass the selected album to the controller
                controller.initData(selectedAlbum, controllerList, currentUser);
                // Create a new scene with the loaded FXML file
                Scene scene = new Scene(root);
                // Get the stage (window) from the event source
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

                stage.setScene(scene);
                stage.show();
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    /**
     * Handles the "Rename Album" button click.
     * Creates a TextInputDialog to allow the user to enter a new name for the selected album and updates the album name.
     */
    @FXML
    public void renameAlbum() {
        String selectedAlbumName = albumListView.getSelectionModel().getSelectedItem().split("\n")[0];
        Album selectedAlbum = currentUser.findAlbumByName(selectedAlbumName);
        if (selectedAlbum != null) {
            // Create a TextInputDialog
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Rename Album");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the new name for the album:");

            // Show the dialog and wait for the user's input
            Optional<String> result = dialog.showAndWait();
            // If the user entered a new name, rename the album
            result.ifPresent(newName -> {
                selectedAlbum.reNameAlbum(newName);
                // Update UI
                initData(currentUser, controllerList);

            });

        }
        saveUsers();
    }

    /**
     * Handles the "Logout" button click.
     * Saves the updated user data and switches the view to the LoginView.
     *
     * @param e the ActionEvent triggered by clicking the "Logout" button
     */
    @FXML
    public void logout(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == logout) {
            try {
                saveUsers();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/photo68/view/LoginView.fxml"));

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
    }

    /**
     * Displays an alert dialog with the specified parameters.
     *
     * @param alertType the type of alert (e.g., ERROR, WARNING)
     * @param title     the title of the alert
     * @param content   the content to be displayed in the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Saves the updated list of users to a file.
     */
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/users.dat"))) {
            oos.writeObject(controllerList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
