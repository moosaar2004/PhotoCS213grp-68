package photo68.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Base Structure class for Photo
 * 
 * @author Oyku Pul
 */
public class Photo implements Serializable {
    private String filePath;
    private String caption;
    private Calendar calanderDate;
    private Date dateTaken;
    private ArrayList<Tag> tags;

    /**
     * constructor for Photo
     *
     * @param filePath
     * @param caption
     * @param dateTaken
     */
    public Photo(String filePath, String caption, LocalDateTime dateTaken) {
        this.filePath = filePath;
        this.caption = caption;
        this.tags = new ArrayList<>();

        calanderDate = new GregorianCalendar();
        calanderDate.set(Calendar.MILLISECOND, 0);
        this.dateTaken = calanderDate.getTime();
    }

    /**
     * getter for file path
     *
     * @return filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * setter for file path
     *
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * getter for caption on a photo
     * @return caption of a photo
     */
    public String getCaption() {
        return caption;
    }

    /**
     * setter for caption on a photo
     *
     * @param caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * getter for date of a Photo
     *
     * @return dateTaken of a photo
     */
    public Date getDateTaken() {
        return dateTaken;
    }

    /**
     * setter for date of a photo
     *
     * @param dateTaken
     */
    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

     /**
     * getter for tags on photo
     *
     * @return tags list
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * setter for tags on photo
     *
     * @param tags
     */
    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    /**
     * method to add a tag on a photo
     *
     * @param tag
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }
    
    /**
     * method to remove a tag on a photo
     *
     * @param tag
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }
}
