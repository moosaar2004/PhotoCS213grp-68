package photo68.model;

/**
 * Tag class
 * 
 * 
 * @author Oyku Pul, op116
 */
public class Tag {
    private String tagName;
    private String tagValue;

    public Tag(String tagName, String tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    // Getters and setters
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }
}
