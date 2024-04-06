/*EXPLANATIONS FOR OUTSELVES - TO BE REMOVED LATER
 * 
 * Plan is to have this user abstract class and then create two subclasses that extend this
 * one is for reguler users and the other for admin users
 * 
 * This class also implements the Serializable interface
 * save method serializes the User object and saves it to a file specified by storeDirectory and fileName.
 * load method deserializes the User object from the file specified by storeDirectory and fileName.
 * Allowing application to save and load user data to and from a file. 
 * 
 * inRangePhotos method to still be worked on/looked at
 * ^that is one way to search photos need to implement the other ways to search as per directions posted
 */

package photo68.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract User class that represents a User in the App
 * 
 * @author Ali Rehman
 */
public abstract class User implements Serializable {
    private String userName;
    private ArrayList<Album> userAlbums;
    public Album userCurrentAlbum;

    public static final String storeDirectory = "data";
    public static final String fileName = "user.ser";

    /**
     * Constructor for the User class
     * 
     * @param userName, the name of the user
     */
    public User(String userName) {
        this.userName = userName;
        this.userAlbums = new ArrayList<Album>();
    }

    /**
     * Get the the userName of a user
     * 
     * @return userName, the name of the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set for the userName of a user
     * 
     * @param userName, the name of the user
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the the full albusmlist belonging to a user
     * 
     * @return userAlbums, list of albums
     */
    public ArrayList<Album> getUserAlbums() {
        return userAlbums;
    }

    /**
     * Set the list of albumss belonging to a user
     * 
     * @param userAlbums, list of albums
     */
    public void setUserAlbums(ArrayList<Album> userAlbums) {
        this.userAlbums = userAlbums;
    }

    /**
     * Get the the current album of a user
     * 
     * @return userCurrentAlbum, the current album
     */
    public Album getUserCurrentAlbum() {
        return userCurrentAlbum;
    }

    /**
     * Set the current album of a user
     * 
     * @param userCurrentAlbum, the current album
     */
    public void setUserCurrentAlbum(Album userCurrentAlbum) {
        this.userCurrentAlbum = userCurrentAlbum;
    }

    /**
     * Add an album to a user's album list
     * 
     * @param album, the album to be added
     */
    public void addUserAlbum(Album album) {
        userAlbums.add(album);
    }

    /**
     * Remove an album from a user's album list
     * 
     * @param album, the album to be removed
     */
    public void removeUserAlbum(Album album) {
        userAlbums.remove(album);
    }

    /**
     * Check if an album exists in a user's album list
     * 
     * @param album, the album to be checked
     * @return true if the album exists, false otherwise
     */
    public boolean doesAlbumExist(Album album) {
        return userAlbums.contains(album);
    }

    /**
     * Get a specific album from a user's album list
     * 
     * @param i, the index of the album to be returned
     * @return album, the album at the specified index
     */
    public Album getSpecificAlbum(int i) {
        return userAlbums.get(i);
    }

    // Method needs to be worked on

    // /**
    // * Get photos within the specified time(date) range
    // *
    // * @param fromDate the start date of the photos
    // * @param toDate the end date of the photos
    // *
    // * @return inRangePhotos, the list of photos within the specified range
    // * */
    // public List<Photo> getPhotosInRange(LocalDate fromDate, LocalDate toDate)
    // {
    // List<Photo> inRangePhotos = new ArrayList<>();
    // for (Album album : userAlbums)
    // {
    // for (Photo photo : album.getPhotos())
    // {
    // LocalDate photoDate =
    // photo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    // if (photoDate.isAfter(fromDate) && photoDate.isBefore(toDate) ||
    // photoDate.equals(fromDate) || photoDate.equals(toDate))
    // inRangePhotos.add(photo);
    // }
    // }
    // return inRangePhotos;
    // }

    /**
     * Save the user object to a file.
     *
     * @param pdApp the user object to save
     * @throws IOException if an I/O error occurs
     */
    public static void save(User pdApp) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(storeDirectory + File.separator + fileName))) {
            oos.writeObject(pdApp);
        }
    }

    /**
     * Load a user object from a file.
     *
     * @return the loaded user object
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if the class of the serialized object cannot
     *                                be found
     */
    public static User load() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(storeDirectory + File.separator + fileName))) {
            return (User) ois.readObject();
        }
    }

    // abstract methods that will be implemented in subclasses

    /**
     * Create a newAlbum.
     *
     * @param name the name of the album
     */
    public abstract void createUserAlbum(String name);

    /**
     * Rename an album.
     *
     * @param album   the album to rename
     * @param newName the new name of the album
     */
    public abstract void renameUserAlbum(Album album, String newName);

    /**
     * Open an album.
     *
     * @param album the album to open
     */
    public abstract void openUserAlbum(Album album);
}