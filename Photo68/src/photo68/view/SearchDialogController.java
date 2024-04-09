package photo68.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import photo68.model.Album;
import photo68.model.Photo;
import photo68.model.Tag;
import photo68.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;

public class SearchDialogController {

    @FXML
    private TextField tagTypeField1;

    @FXML
    private TextField tagValueField1;
    @FXML
    private TextField tagTypeField2;
    @FXML
    private TextField tagValueField2;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;
    @FXML
    private ComboBox<String> selectionComboBox;

    @FXML
    Button create;
    @FXML
    Button search;

    public User currentUser;
    public ArrayList<User> controllerList;
    private Stage dialogStage;

    /**
     * Initializes the ComboBox options for the search criteria selection.
     */
    public void initialize() {
        // Create the options for the ComboBox
        ObservableList<String> options = FXCollections.observableArrayList("AND", "OR");

        // Set the options to the ComboBox
        selectionComboBox.setItems(options);
    }

     /**
     * Initializes the data for the SearchDialogController.
     *
     * @param currentUser the current user
     * @param controllerList the list of users
     */
    public void initData(User currentUser, ArrayList<User> controllerList) {
        this.currentUser = currentUser;
        this.controllerList = controllerList;
    }

    /**
     * Sets the dialog stage for the SearchDialogController.
     *
     * @param dialogStage the dialog stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Handles the search action.
     */
    @FXML
    public void search() {
        String tagName1 = getTagName1();
        String tagValue1 = getTagValue1();
        String tagName2 = getTagName2();
        String tagValue2 = getTagValue2();
        LocalDate fromDate = getFromDate();
        LocalDate toDate = getToDate();
        String selection = getSelection();

        // Perform the search based on the criteria & set equal to a list
        ArrayList<Photo> results = performSearch(tagName1, tagValue1, tagName2, tagValue2, fromDate, toDate, selection);
        try {
            // Load the PhotoDisplayView.fxml
            FXMLLoader photoDisplayLoader = new FXMLLoader(
                    getClass().getResource("/photo68/view/PhotoDisplayView.fxml"));
            Parent photoDisplayRoot = photoDisplayLoader.load();

            // Get the controller associated with the loaded FXML
            PhotoDisplayController photoDisplayController = photoDisplayLoader.getController();

            // Pass the search results to the controller
            photoDisplayController.initData(results);

            // Create a new scene with the loaded FXML file
            Scene photoDisplayScene = new Scene(photoDisplayRoot);

            // Create a new stage for the photo display
            Stage photoDisplayStage = new Stage();
            photoDisplayStage.setTitle("Search Results");
            photoDisplayStage.setScene(photoDisplayScene);
            photoDisplayStage.show();
            dialogStage.close();
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

     /**
     * Handles the create action.
     *
     * @param e the action event
     */
    @FXML
    public void create(ActionEvent e) {
        String tagName1 = getTagName1();
        String tagValue1 = getTagValue1();
        String tagName2 = getTagName2();
        String tagValue2 = getTagValue2();
        LocalDate fromDate = getFromDate();
        LocalDate toDate = getToDate();
        String selection = getSelection();

        // Perform the search based on the criteria & set equal to a list
        ArrayList<Photo> results = performSearch(tagName1, tagValue1, tagName2, tagValue2, fromDate, toDate, selection);
        createResultAlbum(results, e);
        dialogStage.close();
    }

    /**
     * Gets the tag name from the first tag type field.
     *
     * @return the tag name from the first tag type field
     */
    public String getTagName1() {
        return tagTypeField1.getText().trim();
    }

    /**
     * Gets the tag value from the first tag value field.
     *
     * @return the tag value from the first tag value field
     */
    public String getTagValue1() {
        return tagValueField1.getText().trim();
    }

    /**
     * Gets the tag name from the second tag type field.
     *
     * @return the tag name from the second tag type field
     */
    public String getTagName2() {
        return tagTypeField2.getText().trim();
    }

    /**
     * Gets the tag value from the second tag value field.
     *
     * @return the tag value from the second tag value field
     */    
    public String getTagValue2() {
        return tagValueField2.getText().trim();
    }

    /**
     * Gets the from date from the date picker.
     *
     * @return the from date from the date picker
     */
    public LocalDate getFromDate() {
        return fromDatePicker.getValue();
    }

    /**
     * Gets the to date from the date picker.
     *
     * @return the to date from the date picker
     */
    public LocalDate getToDate() {
        return toDatePicker.getValue();
    }

    /**
     * Gets the selection from the ComboBox.
     *
     * @return the selection from the ComboBox
     */
    public String getSelection() {
        return selectionComboBox.getValue(); // Assuming selectionComboBox is the ComboBox in your FXML file
    }

    /**
     * Performs the search based on the given criteria.
     *
     * @param tagName1 the tag name for the first tag-value pair
     * @param tagValue1 the tag value for the first tag-value pair
     * @param tagName2 the tag name for the second tag-value pair
     * @param tagValue2 the tag value for the second tag-value pair
     * @param fromDate the from date for the search
     * @param toDate the to date for the search
     * @param selection the selection criteria ("AND" or "OR")
     * @return the list of photos that match the search criteria
     */
    private ArrayList<Photo> performSearch(String tagName1, String tagValue1, String tagName2, String tagValue2,
            LocalDate fromDate, LocalDate toDate, String selection) {
        ArrayList<Photo> searchResults = new ArrayList<>();

        // Iterate through the albums of the current user
        for (Album album : currentUser.userAlbums) {
            // Iterate through the photos in the album
            for (Photo photo : album.getListOfPhotos()) {
                // Check if the photo matches the search criteria for the first tag-value pair
                boolean matchesCriteria1 = true;

                if (tagName1 != null && tagValue1 != null) {
                    boolean tagFound1 = false;
                    for (Tag tag : photo.getTags()) {
                        if (tag.getTagName().equalsIgnoreCase(tagName1)
                                && tag.getTagValue().equalsIgnoreCase(tagValue1)) {
                            tagFound1 = true;
                            break;
                        }
                    }
                    if (!tagFound1) {
                        matchesCriteria1 = false;
                    }
                }

                // Check if the photo matches the search criteria for the second tag-value pair
                boolean matchesCriteria2 = true;

                if (tagName2 != null && tagValue2 != null) {
                    boolean tagFound2 = false;
                    for (Tag tag : photo.getTags()) {
                        if (tag.getTagName().equalsIgnoreCase(tagName2)
                                && tag.getTagValue().equalsIgnoreCase(tagValue2)) {
                            tagFound2 = true;
                            break;
                        }
                    }
                    if (!tagFound2) {
                        matchesCriteria2 = false;
                    }
                }

                // Check if the photo was taken within the specified date range
                boolean withinDateRange = true;
                if (fromDate != null && toDate != null) {
                    LocalDate photoDate = photo.getDateTaken().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (photoDate.isBefore(fromDate) || photoDate.isAfter(toDate)) {
                        withinDateRange = false;
                    }
                }

                // Apply the selection criteria
                if (selection.equals("AND")) {
                    if (matchesCriteria1 && matchesCriteria2 && withinDateRange) {
                        searchResults.add(photo);
                    }
                } else if (selection.equals("OR")) {
                    if ((matchesCriteria1 || matchesCriteria2) && withinDateRange) {
                        searchResults.add(photo);
                    }
                }
            }
        }
        return searchResults;
    }

     /**
     * Creates a new album with the search results.
     *
     * @param results the list of photos to be added to the new album
     * @param e the action event
     */
    private void createResultAlbum(ArrayList<Photo> results, ActionEvent e) {
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
                    Album tmp = new Album(albumName);
                    tmp.setListOfPhotos(results);
                    tmp.setNumOfPhotos(results.size());
                    currentUser.addUserAlbum(tmp);
                    initData(currentUser, controllerList);
                    saveUsers();
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("UserView.fxml")); // Set the location before loading
                        Parent root = loader.load();
                        // Pass the authenticated user to the UserController
                        UserController controller = loader.getController();
                        controller.initData(currentUser, controllerList);
                        // Create a new scene with the loaded FXML file
                        Scene scene = new Scene(root);

                        // Get the stage (window) from the event source
                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

                        // Set the new scene
                        stage.setScene(scene);
                        stage.setResizable(true);
                        stage.show();
                    } catch (IOException x) {
                        x.printStackTrace();
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Album name already exists.");
                }
            } else {
                // Display error message if the album name is empty
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid album name.");
            }
        });

    }

    /**
     * Displays an alert with the given type, title, and content.
     *
     * @param alertType the type of the alert
     * @param title the title of the alert
     * @param content the content of the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Saves the list of users to the users.dat file.
     */
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/users.dat"))) {
            oos.writeObject(controllerList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
