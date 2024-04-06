package photo68.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Ali Rehman
 */
public class AdminUser implements Serializable
{
    public static final String storeDirectory = "data";
    public static final String fileName = "user.ser";

    public ArrayList<User> allUsers;
    public User currentUser;
    public boolean userLoggedIn;

    public AdminUser()
    {
        allUsers = new ArrayList<>();
        allUsers.add(new User("admin"));
        this.currentUser = null;
        this.userLoggedIn = false;
    }

    /**
     * set the list of users
     * @param users the list of users
     */
    public void setUsers(ArrayList<User> users)
    {
        this.allUsers = users;
    }

    /**
     * getter for current user
     * @return current user
     */
    public User getCurrentUser()
    {
        return currentUser;
    }

    /**
     * setter for current user
     * @param user to be set as current
     */
    public void setCurrentUser(User user)
    {
        this.currentUser = user;
    }

    /**
     * Getter for list of all users
     * @return list of all users
     */
    public ArrayList<User> getAllUsers()
    {
        return allUsers;
    }

    /**
     * Add user to list
     * @param username of the new user to add
     */
    public void addUserToList(String username)
    {
        allUsers.add(new User(username));
    }

    /**
     * delete user from list
     * @param username of the new user to delete
     */
    public void deleteUserByNameFromList(String username)
    {
        allUsers.remove(username);
    }

    /**
     * delete user by index in list
     * @param index of the user to delete in the list
     */
    public void removeUserByIndexFromList(int i)
    {
        allUsers.remove(i);
    }

    public User getUserByUsername(String username)
    {
        for(User u: allUsers)
        {
            if(u.getUserName().equals(username))
                return u;
        }
        return null;
    }

    /**
     * check to see if the user already exists
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    public boolean checkIfUserExists(String username)
    {
        for(User u: allUsers)
        {
            if(u.getUserName().equals(username))
                return true;
        }
        return false;
    }

    /**
     * check if user exists and set as currentUser
     * @param uName the name of the user to check
     * @return true if the user exists, false otherwise
     */
    public boolean checkAndSetUser(String uName)
    {
        if(checkIfUserExists(uName))
        {
            int spot = 0;
            for(int i = 0; i < allUsers.size(); i++)
            {
                if(allUsers.get(i).getUserName().equals(uName))
                {
                    spot = i;
                    break;
                }
            }
            this.setCurrentUser(allUsers.get(spot));
            this.userLoggedIn = true;
            return true;
        }
        return false;
    }

    /**
     * saves the state of Admin instance to file
     * @param pdA admin instance to save
     * @throws IOException if an error occurs
     */
    public static void saveToFile(AdminUser pdA) throws IOException
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDirectory + File.separator + fileName)))
        {
            oos.writeObject(pdA);
            oos.close();
        }
    }

    /**
     * Loads Admin data from file
     * @return loaded admin instance
     * @throws IOException if I/O error occurs
     * @throws ClassNotFoundException if class not found
     */
    public static AdminUser loadFromFile() throws IOException, ClassNotFoundException
    {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDirectory + File.separator + fileName)))
        {
            return (AdminUser) ois.readObject();
        }
    }

}