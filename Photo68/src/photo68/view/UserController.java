package photo68.view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import photo68.model.User;

public class UserController {
    @FXML
    private ListView<String> albumListView;// ??
    @FXML
    Button create;
    @FXML
    Button delete;
    @FXML
    Button open;
    @FXML
    Button logout;
    private User currentUser;
    public ArrayList<User> listOfUsers;

    // do this when you logout out of the userview
    public void saveUsersToFile(ArrayList<User> users) {
        // Serialize users to a file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/data/users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateList(ArrayList<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public void initData(User user) {
        this.currentUser = user;
        // Use currentUser to initialize the view with user-specific data
    }

    @FXML
    public void createAlbum() {
        // Handle album creation logic
    }

    @FXML
    public void deleteAlbum() {
        // Handle album deletion logic
    }

    @FXML
    public void openAlbum() {
        // currentUser dependent
    }

    @FXML
    public void logout(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == logout) {
            saveUsersToFile(listOfUsers);
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/photo68/view/LoginView.fxml"));

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
