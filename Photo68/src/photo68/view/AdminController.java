package photo68.view;

import java.io.*;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class AdminController {
    @FXML
    Button create;
    @FXML
    Button delete;
    @FXML
    Button logout;

    @FXML
    private ListView<String> userListView;

    @FXML
    public void createUser() {
        // Handle user creation logic
    }

    @FXML
    public void deleteUser() {
        // Handle user deletion logic
    }

    @FXML
    public void logout(ActionEvent e) {
        Button b = (Button) e.getSource();
        if (b == logout) {

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
