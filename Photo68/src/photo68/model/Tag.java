/*NOTES FOR OUTSELVES - TO BE REMOVED LATER
 * its the code that you wrote, just moved it this package
 * another copy still there in app package, still to see where it would be suited best
 */

 package photo68.model;

 import java.io.Serializable;
 
 /**
  * This class represents a basic example class.
  * It demonstrates how to implement Javadoc comments.
  * 
  * @author Oyku Pul
  */
 public class Tag implements Serializable
 {
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
 
     /**
      * Method to compare tag values
      * @param object to compare with
      * @return true if tag values are equal, false otherwise
      */
     @Override
     public boolean equals(Object obj)
     {
         if(!(obj instanceof Tag) || obj == null)
             return false;
         Tag t = (Tag) obj;
         boolean result1 = t.tagName.equals(this.tagName);
         boolean result2 = t.tagValue.equals(this.tagValue);
 
         return result1 && result2;
     }
 }
 