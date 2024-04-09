package photo68.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import photo68.model.Photo;
import photo68.model.Tag;


public class PhotoDisplayController {
    @FXML
    private ListView<Photo> photoListView;
    private Stage photoStage; // Declare the Stage
    private ImageView imageView; // Declare the ImageView
    @FXML
    Button displayPhoto;
    @FXML
    private Label tagLabel;
    private Label captionLabel;

     /**
     * Initializes the data for the photo display.
     *
     * @param results the list of photos to be displayed in the photo list view
     */
    public void initData(ArrayList<Photo> results) {
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
        if (results != null) {
            photoListView.setItems(FXCollections.observableArrayList(results));
        }
        // Disable the buttons initially
        displayPhoto.setDisable(true);

        // Add a listener to the selection model of the ListView
        photoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Enable/disable buttons based on whether a photo is selected
            boolean photoSelected = newValue != null;
            displayPhoto.setDisable(!photoSelected);

        });
    }

    /**
     * Displays the selected photo in a new stage.
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
                    imageView.setFitHeight(550); // Set height of the image view

                    // Display tags
                    tagLabel = new Label();
                    tagLabel.setStyle("-fx-font-size: 14px;");
                    // Create a label to display the caption
                    captionLabel = new Label();
                    captionLabel.setStyle("-fx-font-size: 14px;");

                    // Create buttons for navigation
                    Button prevButton = new Button("Previous");
                    prevButton.setOnAction(e -> navigatePhoto(-1)); // Move to previous photo
                    Button nextButton = new Button("Next");
                    nextButton.setOnAction(e -> navigatePhoto(1)); // Move to next photo

                    // Create a layout pane to hold the image view, tag label, and buttons
                    VBox root = new VBox(10);
                    root.getChildren().addAll(imageView, tagLabel, captionLabel, new HBox(10, prevButton, nextButton));

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
        displayPhoto.setDisable(false);

    }

    /**
     * Navigates to the previous or next photo in the list view.
     *
     * @param offset the offset to be applied to the current selection index (-1 for previous, 1 for next)
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
     * @param selectedPhoto the selected photo
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
     * Displays the selected photo in the image view and updates the tag and caption labels.
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
        }
    }
}
