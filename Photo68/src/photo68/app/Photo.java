package photo68.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a basic example class.
 * It demonstrates how to implement Javadoc comments.
 * 
 * @author Oyku Pul
 */
public class Photo {
    private String filePath;
    private String caption;
    private LocalDateTime dateTaken;
    private List<Tag> tags;

    public Photo(String filePath, String caption, LocalDateTime dateTaken) {
        this.filePath = filePath;
        this.caption = caption;
        this.dateTaken = dateTaken;
        this.tags = new ArrayList<>();
    }

    // Getters and setters
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public LocalDateTime getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(LocalDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    // Method to add a tag
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    // Method to remove a tag
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }
}
