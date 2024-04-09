package photo68.model;

import java.io.Serializable;

/**
 * Tag class
 * 
 * 
 * @author Oyku Pul, op116
 */
public class Tag implements Serializable {
    private String tagName;
    private String tagValue;

    public Tag(String tagName, String tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    /**
     * method to get tag name
     * @return tagName
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * method to set tag name
     *
     * @param tagName
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * method to get tag value
     *
     * @return tagValue
     */
    public String getTagValue() {
        return tagValue;
    }

    /**
     * methid to set tag value
     *
     * @param tagValue
     */
    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    /**
     * method to get string representation of tag name
     *
     * @return tagName
     */
    @Override
    public String toString() {
        return tagName; // Return the tag name when converting to string
    }
}
