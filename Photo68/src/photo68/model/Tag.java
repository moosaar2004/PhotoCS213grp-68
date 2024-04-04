/*NOTES FOR OUTSELVES - TO BE REMOVED LATER
 * its the code that you wrote, just moved it this package
 * another copy still there in app package, still to see where it would be suited best
 */

package photo68.model;

/**
 * This class represents a basic example class.
 * It demonstrates how to implement Javadoc comments.
 * 
 * @author Oyku Pul
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
