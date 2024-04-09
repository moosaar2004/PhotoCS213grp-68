package photo68.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.*;
import photo68.model.Album;
import photo68.model.Photo;
import photo68.model.Tag;
import photo68.model.User;
import javafx.scene.*;

public class AlbumController {
    @FXML
    private ListView<Photo> photoListView;
    private Album currentAlbum;
    private ArrayList<User> controllerList;
    private User currentUser;
    private Stage photoStage; // Declare the Stage
    private ImageView imageView; // Declare the ImageView

    @FXML
    Button addPhoto;
    @FXML
    Button removePhoto;
    @FXML
    Button captionPhoto;
    @FXML
    Button displayPhoto;
    @FXML
    Button addTag;
    @FXML
    Button removeTag;
    @FXML
    Button back;
    @FXML
    Button move;
    @FXML
    Button copy;
    @FXML
    Button quit;
    @FXML
    private Label tagLabel;
    private Label captionLabel;
    private Label dateLabel;

    /**
     * Initializes the controller with the selected album, the list of users, and the current user.
     * Sets up the cell factory for the photoListView to display thumbnails of the photos.
     * Disables buttons based on whether a photo is selected or not.
     * Adds a listener to the selection model of the photoListView to enable/disable buttons accordingly.
     *
     * @param selectedAlbum    the album selected by the user
     * @param controlLolerList the list of users
     * @param currentUser      the current user
     */
    public void initData(Album selectedAlbum, ArrayList<User> controlLolerList, User currentUser) {
        this.currentAlbum = selectedAlbum;
        this.controllerList = controlLolerList;
        this.currentUser = currentUser;
        tagLabel = new Label();
        // Set up the cell factory to display thumbnails
        photoListView.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(100); // Set the height of the thumbnail
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Photo photo, boolean empty) {
                super.updateItem(photo, empty);

                if (empty || photo == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Load the image from the file path and set it as the thumbnail
                    Image image = new Image(new File(photo.getFilePath()).toURI().toString());
                    imageView.setImage(image);
                    setGraphic(imageView);

                    // Set the caption as the text for the cell
                    setText(photo.getCaption());
                }
            }
        });

        // Populate the photoListView with photos from currentAlbum
        if (currentAlbum != null) {
            photoListView.setItems(FXCollections.observableArrayList(currentAlbum.getListOfPhotos()));
        }
        // Disable the buttons initially
        removePhoto.setDisable(true);
        captionPhoto.setDisable(true);
        displayPhoto.setDisable(true);
        addTag.setDisable(true);
        removeTag.setDisable(true);
        move.setDisable(true);
        copy.setDisable(true);

        // Add a listener to the selection model of the ListView
        photoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Enable/disable buttons based on whether a photo is selected
            boolean photoSelected = newValue != null;
            removePhoto.setDisable(!photoSelected);
            captionPhoto.setDisable(!photoSelected);
            displayPhoto.setDisable(!photoSelected);
            addTag.setDisable(!photoSelected);
            removeTag.setDisable(!photoSelected);
            move.setDisable(!photoSelected);
            copy.setDisable(!photoSelected);

        });
    }

    /**
     * Handles the event when the "Add Photo" button is clicked.
     * Displays a file chooser dialog to allow the user to select a photo.
     * Checks if the selected photo already exists in any other album and adds a reference to it.
     * If the photo is new, prompts the user to enter a caption and creates a new Photo object.
     * Adds the photo to the current album and updates the UI.
     *
     * @param event the ActionEvent triggered by clicking the "Add Photo" button
     */
    @FXML
    public void addPhoto(ActionEvent event) {
        // Create a file chooser dialog to select a photo file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            // Get the file path of the selected photo
            String filePath = selectedFile.getAbsolutePath();
            // Check if the selected photo already exists in any other album
            Photo existingPhoto = findPhotoByFilePath(filePath);
            if (existingPhoto != null) {
                // Add a reference to the existing photo in the current album
                currentAlbum.addPhoto(existingPhoto);
            } else {
                // Create a TextInputDialog to prompt the user for the caption
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Enter Caption");
                dialog.setHeaderText(null);
                dialog.setContentText("Enter caption for the photo:");

                // Show the dialog and wait for the user's input
                Optional<String> result = dialog.showAndWait();

                // If the user entered a caption, create a Photo object with the selected file
                // path and caption
                if (result.isPresent()) {
                    String caption = result.get();
                    Photo photo = new Photo(filePath, caption, LocalDateTime.now());

                    // Add the photo to the album
                    currentAlbum.addPhoto(photo);
                }
            }
            // Update UI
            initData(currentAlbum, controllerList, currentUser);
            saveUsersToFile(controllerList);
        }
    }

    /**
     * Handles the event when the "Remove Photo" button is clicked.
     * Removes the selected photo from the current album and updates the UI.
     */
    @FXML
    public void removePhoto() {
        // Handle removing photo lo// Check if a photo is selected
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto != null) {
            // Remove the selected photo from the current album
            currentAlbum.removePhoto(selectedPhoto);

            // Update UI
            initData(currentAlbum, controllerList, currentUser);
            saveUsersToFile(controllerList);

        }
        saveUsersToFile(controllerList);
        // Update button disable states
        removePhoto.setDisable(false);
        captionPhoto.setDisable(false);
        displayPhoto.setDisable(false);
        addTag.setDisable(false);
        removeTag.setDisable(false);
        move.setDisable(false);
        copy.setDisable(false);

    }

    /**
     * Handles the event when the "Caption Photo" button is clicked.
     * Creates a text input dialog to allow the user to edit the caption of the selected photo.
     * Updates the caption of the selected photo and refreshes the list view.
     */
    @FXML
    public void captionPhoto() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto != null) {
            // Create a text input dialog to edit the caption
            TextInputDialog dialog = new TextInputDialog(selectedPhoto.getCaption());
            dialog.setTitle("Edit Caption");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the new caption:");

            // Show the dialog and wait for the user's input
            Optional<String> result = dialog.showAndWait();
            // If the user entered a new caption, set it for the photo
            result.ifPresent(newCaption -> {
                selectedPhoto.setCaption(newCaption);
                // Update UI
                photoListView.refresh(); // Refresh the list view to reflect the changes
            });
        }
        saveUsersToFile(controllerList);
    }

    /**
     * Handles the event when the "Add Tag" button is clicked.
     * Creates a dialog to prompt the user for a tag name and value.
     * Adds the new tag to the selected photo and updates the UI.
     */
    @FXML
    public void addTag() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto != null) {
            // Create a dialog to prompt the user for tag information
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Add Tag");
            dialog.setHeaderText("Enter Tag Information:");

            // Set the button types
            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            // Create the tag name and tag value text fields
            TextField tagNameField = new TextField();
            tagNameField.setPromptText("Tag Name");
            TextField tagValueField = new TextField();
            tagValueField.setPromptText("Tag Value");

            // Enable/disable the Add button based on text field content
            Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
            addButton.setDisable(true);
            tagNameField.textProperty().addListener((observable, oldValue, newValue) -> {
                addButton.setDisable(newValue.trim().isEmpty() || tagValueField.getText().trim().isEmpty());
            });
            tagValueField.textProperty().addListener((observable, oldValue, newValue) -> {
                addButton.setDisable(newValue.trim().isEmpty() || tagNameField.getText().trim().isEmpty());
            });

            // Create a layout for the dialog content
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            grid.add(new Label("Tag Name:"), 0, 0);
            grid.add(tagNameField, 1, 0);
            grid.add(new Label("Tag Value:"), 0, 1);
            grid.add(tagValueField, 1, 1);

            // Add the layout to the dialog pane
            dialog.getDialogPane().setContent(grid);

            // Request focus on the tag name field by default
            Platform.runLater(tagNameField::requestFocus);

            // Convert the result to a tag when the Add button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    return new Pair<>(tagNameField.getText(), tagValueField.getText());
                }
                return null;
            });

            // Show the dialog and wait for the user's input
            Optional<Pair<String, String>> result = dialog.showAndWait();

            // If the user entered tag information, add the tag to the selected photo
            result.ifPresent(tagInfo -> {
                String tagName = tagInfo.getKey();
                String tagValue = tagInfo.getValue();
                selectedPhoto.addTag(new Tag(tagName, tagValue));

                // Update UI
                initData(currentAlbum, controllerList, currentUser);
                saveUsersToFile(controllerList);
            });
        }
        // Update button disable states
        removePhoto.setDisable(false);
        captionPhoto.setDisable(false);
        displayPhoto.setDisable(false);
        addTag.setDisable(false);
        removeTag.setDisable(false);
        move.setDisable(false);
        copy.setDisable(false);

    }

    /**
     * Handles the event when the "Remove Tag" button is clicked.
     * Creates a dialog to allow the user to select a tag to remove from the selected photo.
     * Removes the selected tag from the photo and updates the tag label.
     */
    @FXML
    public void removeTag() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto != null) {
            // Get the list of tags from the selected photo
            List<Tag> tags = selectedPhoto.getTags();

            // Create a dialog to prompt the user for the tag name to remove
            ChoiceDialog<Tag> dialog = new ChoiceDialog<>(null, tags);
            dialog.setTitle("Remove Tag");
            dialog.setHeaderText("Select a Tag to Remove:");
            dialog.setContentText("Tag:");

            // Show the dialog and wait for the user's input
            Optional<Tag> result = dialog.showAndWait();

            // If the user selected a tag, remove it from the photo's tag list
            result.ifPresent(tag -> {
                selectedPhoto.removeTag(tag);

                // Update UI
                updateTagLabel(selectedPhoto);
                saveUsersToFile(controllerList);
            });
        }
        // Update button disable states
        removePhoto.setDisable(false);
        captionPhoto.setDisable(false);
        displayPhoto.setDisable(false);
        addTag.setDisable(false);
        removeTag.setDisable(false);
        move.setDisable(false);
        copy.setDisable(false);

    }

    /**
     * Handles the event when the "Display Photo" button is clicked.
     * Creates a new stage to display the selected photo, including its tags and caption.
     * Provides navigation buttons to move to the previous or next photo.
     */
    @FXML
    public void displayPhoto() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto != null) {
            try {
                // Check if the stage is already created
                if (photoStage == null) {
                    // Create a new stage if it doesn't exist
                    photoStage = new Stage();
                    photoStage.setTitle("Photo Viewer");

                    // Create an image view to display the image
                    imageView = new ImageView();
                    imageView.setFitWidth(700); // Set width of the image view
                    imageView.setFitHeight(540); // Set height of the image view

                    // Display tags
                    tagLabel = new Label();
                    tagLabel.setStyle("-fx-font-size: 14px;");
                    // Create a label to display the caption
                    captionLabel = new Label();
                    captionLabel.setStyle("-fx-font-size: 14px;");
                    //Label for displaying date when in photo display mode
                    dateLabel = new Label();
                    dateLabel.setStyle("-fx-font-size: 14px;");

                    // Create buttons for navigation
                    Button prevButton = new Button("Previous");
                    prevButton.setOnAction(e -> navigatePhoto(-1)); // Move to previous photo
                    Button nextButton = new Button("Next");
                    nextButton.setOnAction(e -> navigatePhoto(1)); // Move to next photo

                    // Create a layout pane to hold the image view, tag label, and buttons
                    VBox root = new VBox(10);
                    root.getChildren().addAll(imageView, tagLabel, captionLabel, dateLabel, new HBox(10, prevButton, nextButton));

                    // Create a scene with the layout pane
                    Scene scene = new Scene(root, 700, 650);

                    // Set the scene to the stage
                    photoStage.setScene(scene);
                }

                // Load the image from the file path
                Image image = new Image(new File(selectedPhoto.getFilePath()).toURI().toString());
                imageView.setImage(image);

                // Show the stage
                photoStage.show();

                // Update the tag label
                displaySelectedPhoto();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Update button disable states
        removePhoto.setDisable(false);
        captionPhoto.setDisable(false);
        displayPhoto.setDisable(false);
        addTag.setDisable(false);
        removeTag.setDisable(false);
        move.setDisable(false);
        copy.setDisable(false);

    }

    /**
     * Handles the event when the "Go Back" button is clicked.
     * Saves the updated user list to the file and switches the view to the UserView.
     *
     * @param event the ActionEvent triggered by clicking the "Go Back" button
     */
    @FXML
    public void goBack(ActionEvent event) {
        saveUsersToFile(controllerList);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photo68/view/UserView.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the UserController
            UserController controller = loader.getController();
            controller.initData(currentUser, controllerList);

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the stage (window) from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event when the "Move" button is clicked.
     * Retrieves the selected photo and the source album.
     * Prompts the user to select the destination album and moves the photo to the new album.
     * Updates the UI to reflect the changes.
     */
    @FXML
    public void move() {
        // Retrieve the selected photo
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto == null) {
            // No photo selected, display an error message or handle it accordingly
            return;
        }

        // Retrieve the source album
        Album sourceAlbum = currentAlbum;

        // Open a dialog to select the destination album
        // You can implement this dialog using JavaFX dialogs or custom UI elements

        // Once the user selects the destination album, remove the photo from the source
        // album
        sourceAlbum.removePhoto(selectedPhoto);

        // Add the photo to the destination album
        // Assuming you have a method to select the destination album, for example:
        Album destinationAlbum = selectDestinationAlbum(); // Implement this method
        if (destinationAlbum != null) {
            destinationAlbum.addPhoto(selectedPhoto);

            // Update UI to reflect the changes
            initData(destinationAlbum, controllerList, currentUser);
            initData(sourceAlbum, controllerList, currentUser);
            saveUsersToFile(controllerList);
        }
        // Update button disable states
        removePhoto.setDisable(false);
        captionPhoto.setDisable(false);
        displayPhoto.setDisable(false);
        addTag.setDisable(false);
        removeTag.setDisable(false);
        move.setDisable(false);
        copy.setDisable(false);
    }

    /**
     * Handles the event when the "Copy" button is clicked.
     * Retrieves the selected photo and the destination album.
     * Adds a reference of the selected photo to the destination album.
     * Updates the UI to reflect the changes.
     */
    @FXML
    public void copy() {
        // Retrieve the selected photo
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto != null) {
            // Retrieve the destination album
            Album destinationAlbum = selectDestinationAlbum();
            if (destinationAlbum != null) {
                // Add the reference of the selected photo to the destination album
                destinationAlbum.addPhoto(selectedPhoto);

                // Update UI
                initData(currentAlbum, controllerList, currentUser);
                saveUsersToFile(controllerList);
            }
        }
        // Update button disable states
        removePhoto.setDisable(false);
        captionPhoto.setDisable(false);
        displayPhoto.setDisable(false);
        addTag.setDisable(false);
        removeTag.setDisable(false);
        move.setDisable(false);
        copy.setDisable(false);
    }

    /**
     * Selects the destination album for the "Move" or "Copy" operations.
     * Displays a ChoiceDialog to allow the user to choose the destination album from the list of available albums.
     * Returns the selected Album object or null if no album is selected.
     *
     * @return the selected destination Album, or null if no album is selected
     */
    private Album selectDestinationAlbum() {
        // Retrieve a list of album names from the controllerList or any other source
        List<String> albumNames = controllerList.stream()
                .flatMap(user -> user.getUserAlbums().stream())
                .map(Album::getNameOfAlbum)
                .filter(name -> !name.equals(currentAlbum.getNameOfAlbum())) // Exclude the current album name
                .collect(Collectors.toList());

        // Create a ChoiceDialog to allow the user to select the destination album
        ChoiceDialog<String> dialog = new ChoiceDialog<>(albumNames.get(0), albumNames);
        dialog.setTitle("Select Destination Album");
        dialog.setHeaderText(null);
        dialog.setContentText("Choose the destination album:");

        // Show the dialog and wait for the user's selection
        Optional<String> result = dialog.showAndWait();

        // If the user selected an album, find and return the corresponding Album object
        if (result.isPresent()) {
            String selectedAlbumName = result.get();
            for (User user : controllerList) {
                for (Album album : user.getUserAlbums()) {
                    if (album.getNameOfAlbum().equals(selectedAlbumName)) {
                        return album; // Return the selected destination album
                    }
                }
            }
        }

        return null; // Return null if no album was selected or found
    }

    /**
     * Navigates to the previous or next photo in the photoListView.
     * Updates the display of the selected photo, including the tag label.
     *
     * @param offset the offset to apply when navigating (e.g., -1 for previous, 1 for next)
     */
    private void navigatePhoto(int offset) {
        int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
        int newIndex = (selectedIndex + offset + photoListView.getItems().size()) % photoListView.getItems().size();
        photoListView.getSelectionModel().select(newIndex);
        displaySelectedPhoto(); // Update the tagLabel after selecting a new photo
    }

    /**
     * Updates the tag label to display the tags associated with the selected photo.
     *
     * @param selectedPhoto the selected Photo object
     */
    public void updateTagLabel(Photo selectedPhoto) {
        // Clear the existing tag labels
        tagLabel.setText("");

        // Get the list of tags from the selected photo
        List<Tag> tags = selectedPhoto.getTags();

        // Append each tag to the label text
        StringBuilder labelText = new StringBuilder("Tags: ");
        for (Tag tag : tags) {
            labelText.append(tag.getTagName()).append(": ").append(tag.getTagValue()).append(", ");
        }

        // Set the updated text to the tag label
        tagLabel.setText(labelText.toString());

    }

    /**
     * Displays the selected photo in the image view, including the tags, caption, and date taken.
     */
    private void displaySelectedPhoto() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto != null) {
            // Load the image from the file path
            Image image = new Image(new File(selectedPhoto.getFilePath()).toURI().toString());
            imageView.setImage(image);

            // Update tag label
            updateTagLabel(selectedPhoto);
            // Update the caption label
            if (captionLabel != null) {
                captionLabel.setText("Caption: " + selectedPhoto.getCaption());
            }
            dateLabel.setText("Date: " + selectedPhoto.getDateTaken());
        }
    }

    /**
     * Finds a Photo object by its file path in the list of users and their albums.
     *
     * @param filePath the file path of the photo to find
     * @return the Photo object with the given file path, or null if not found
     */
    private Photo findPhotoByFilePath(String filePath) {
        for (User user : controllerList) {
            for (Album album : user.getUserAlbums()) {
                for (Photo photo : album.getListOfPhotos()) {
                    if (photo.getFilePath().equals(filePath)) {
                        return photo;
                    }
                }
            }
        }
        return null; // Photo not found
    }

    /**
     * Saves the list of users to a file.
     *
     * @param users the list of users to save
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
