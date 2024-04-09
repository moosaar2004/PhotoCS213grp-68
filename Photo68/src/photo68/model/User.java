package photo68.model;

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
public class User implements Serializable {
    public String userName;
    public ArrayList<Album> userAlbums;
    public Album userCurrentAlbum;

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
        return this.userName;
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

    public Album findAlbumByName(String albumName) {
        for (Album album : userAlbums) {
            if (album.getNameOfAlbum().equals(albumName)) {
                return album;
            }
        }
        return null; // Album not found
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
    public boolean doesAlbumExist(String x) {
        // Iterate over each Album object in the userAlbums ArrayList
        for (Album album : userAlbums) {
            // Check if the name of the current Album matches x
            if (album.getNameOfAlbum().equals(x)) {
                return true; // Album with name x found
            }
        }
        // Album with name x not found
        return false;
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

}