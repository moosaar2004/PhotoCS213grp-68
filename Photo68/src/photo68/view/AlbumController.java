package photo68.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import photo68.model.Album;
import photo68.model.Photo;
import photo68.model.Tag;
import photo68.model.User;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

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

    public void initData(Album selectedAlbum, ArrayList<User> controlLolerList, User currentUser) {
        this.currentAlbum = selectedAlbum;
        this.controllerList = controlLolerList;
        this.currentUser = currentUser;

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

        // Add a listener to the selection model of the ListView
        photoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Enable/disable buttons based on whether a photo is selected
            boolean photoSelected = newValue != null;
            removePhoto.setDisable(!photoSelected);
            captionPhoto.setDisable(!photoSelected);
            displayPhoto.setDisable(!photoSelected);
            addTag.setDisable(!photoSelected);
            removeTag.setDisable(!photoSelected);
        });
    }

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
            // Update UI
            initData(currentAlbum, controllerList, currentUser);
            saveUsersToFile(controllerList);
        }
    }

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
    }

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
    }

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
    }

    @FXML
    public void removeTag() {

    }

    @FXML
    public void displayPhoto() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        // Create Label to display tags
        Label tagLabel = new Label();
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
                    imageView.setFitHeight(550); // Set height of the image view

                    // Display tags
                    tagLabel.setStyle("-fx-font-size: 14px;");

                    // Create buttons for navigation
                    Button prevButton = new Button("Previous");
                    prevButton.setOnAction(e -> navigatePhoto(-1)); // Move to previous photo
                    Button nextButton = new Button("Next");
                    nextButton.setOnAction(e -> navigatePhoto(1)); // Move to next photo

                    // Create a layout pane to hold the image view, tag label, and buttons
                    VBox root = new VBox(10);
                    root.getChildren().addAll(imageView, tagLabel, new HBox(10, prevButton, nextButton));

                    // Create a scene with the layout pane
                    Scene scene = new Scene(root, 700, 650);

                    // Set the scene to the stage
                    photoStage.setScene(scene);
                }

                // Load the image from the file path
                Image image = new Image(new File(selectedPhoto.getFilePath()).toURI().toString());
                imageView.setImage(image);

                // Display tags horizontally
                StringBuilder tagsText = new StringBuilder("Tags: ");
                List<Tag> tags = selectedPhoto.getTags();
                for (int i = 0; i < tags.size(); i++) {
                    Tag tag = tags.get(i);
                    tagsText.append(tag.getTagName()).append(": ").append(tag.getTagValue());
                    if (i < tags.size() - 1) {
                        tagsText.append(", ");
                    }
                }
                tagLabel.setText(tagsText.toString());
                // Update button disable states
                removePhoto.setDisable(false);
                captionPhoto.setDisable(false);
                displayPhoto.setDisable(false);
                addTag.setDisable(false);
                removeTag.setDisable(false);

                // Show the stage
                photoStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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

    private void navigatePhoto(int offset) {
        int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
        int newIndex = (selectedIndex + offset + photoListView.getItems().size()) % photoListView.getItems().size();
        photoListView.getSelectionModel().select(newIndex);
        displayPhoto();
    }

    public void saveUsersToFile(ArrayList<User> users) {
        // Serialize users to a file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
