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

    public void initData(User user, ArrayList<User> controlLolerList) {
        this.currentUser = user;
        this.controllerList = controlLolerList;
        // Use currentUser to initialize the view with user-specific data
        albumListObservable = FXCollections.observableArrayList();
        for (Album a : currentUser.userAlbums) {
            albumListObservable.add(a.getNameOfAlbum());
        }
        albumListView.setItems(albumListObservable);

        // Disable the delete button initially
        openButton.setDisable(true);
        deleteButton.setDisable(true);
        renameButton.setDisable(true);

        // Add a listener to the selection model of the ListView
        albumListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Enable the delete button only if a user is selected
            openButton.setDisable(newValue == null);
            deleteButton.setDisable(newValue == null);
            renameButton.setDisable(newValue == null);
        });
    }

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
    }

    @FXML
    public void deleteAlbum() {
        Album selectedAlbum = currentUser.findAlbumByName(albumListView.getSelectionModel().getSelectedItem());
        // Check if an album is selected
        if (selectedAlbum != null) {
            // Remove the selected album from the data model
            currentUser.removeUserAlbum(selectedAlbum);
            initData(currentUser, controllerList);

        }

    }

    @FXML
    public void openAlbum(ActionEvent e) {
        Album selectedAlbum = currentUser.findAlbumByName(albumListView.getSelectionModel().getSelectedItem());
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

    @FXML
    public void renameAlbum() {
        Album selectedAlbum = currentUser.findAlbumByName(albumListView.getSelectionModel().getSelectedItem());
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
    }

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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/users.dat"))) {
            oos.writeObject(controllerList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
