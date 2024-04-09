package photo68.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Base Strcuture Class for Album
 * 
 * @author Ali Rehman, amr567
 */
public class Album implements Serializable {
    private String nameOfAlbum;
    private ArrayList<Photo> listOfPhotos;
    private int numOfPhotos;

    public Album(String aName) {
        this.nameOfAlbum = aName;
        this.listOfPhotos = new ArrayList<>();
        this.numOfPhotos = 0;
    }

    /**
     * method to get the name of the album
     * 
     * @return name of the album
     */
    public String getNameOfAlbum() {
        return nameOfAlbum;
    }

    /**
     * method to get the current album name
     * 
     * @return current album name
     */
    public String getCurrentAlbumName() {
        return this.nameOfAlbum;
    }

    /**
     * method to set the name of the album
     * 
     * @param aName name of the album to be set
     */
    public void setAlbumName(String aName) {
        this.nameOfAlbum = aName;
    }

    /**
     * method to rename the album
     * 
     * @param aName new name of the album
     */
    public void reNameAlbum(String aName) {
        this.nameOfAlbum = aName;
    }

    /**
     * method to get the number of photos in the album
     * 
     * @return num of photos in the album
     */
    public int getNumOfPhotos() {
        return numOfPhotos;
    }

    /**
     * method to get the list of photos in the album
     * 
     * @return the list of photos in the album
     */
    public ArrayList<Photo> getListOfPhotos() {
        return listOfPhotos;
    }

    /*
     * method to set the list of photos in the album
     * 
     */
    public void setListOfPhotos(ArrayList<Photo> results) {
        this.listOfPhotos = results;
    }

    /**
     * Method to check if a photo in the given filepath exists in same album
     * 
     * @param filePath file path of the photo to check
     * @return true if the photo exists, false otherwise
     */
    public boolean checkIfAlbumExists(String filePath) {
        return listOfPhotos.stream().anyMatch(photo -> photo.getFilePath().equals(filePath));
    }

    /**
     * method to get the date of the earliest photo in the album
     * 
     * @return optional containing the date of the earliest photo in the album, or
     *         empty optional if album empty
     */
    public String firstDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = "N/A";
        if (!listOfPhotos.isEmpty()) {
            Date d = this.getListOfPhotos().get(0).getDateTaken();
            for (Photo p : listOfPhotos) {
                if (p.getDateTaken().before(d)) {

                    d = p.getDateTaken();
                }
            }
            date = sdf.format(d);
        }
        return date;
    }

    /**
     * method to get the date of the latest photo in the album
     * 
     * @return an optional containing the date of the latest photo in the album, or
     *         empty optional
     */
    public String lastDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = "N/A";
        if (!listOfPhotos.isEmpty()) {
            Date d = this.getListOfPhotos().get(0).getDateTaken();
            for (Photo p : listOfPhotos) {
                if (p.getDateTaken().after(d)) {
                    d = p.getDateTaken();
                }
                date = sdf.format(d);
            }

        }
        return date;
    }

    /**
     * method to add a new photo to the album
     * 
     * @param photo the new photo to add
     */
    public void addPhoto(Photo photo) {
        listOfPhotos.add(photo);
        numOfPhotos++;
    }

    /**
     * method to remove a photo
     * 
     * @param i the idex of photo to be removed
     */
    public void removePhoto(Photo i) {
        listOfPhotos.remove(i);
        numOfPhotos--;
    }

    /**
     * method to set the number of photos
     * 
     * @param i the number of photos to be set
     */
    public void setNumOfPhotos(int i) {
        this.numOfPhotos = i;
    }

    @Override
    public String toString() {
        return getCurrentAlbumName();
    }

}