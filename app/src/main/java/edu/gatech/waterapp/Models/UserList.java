package edu.gatech.waterapp.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Derian on 2/15/2017.
 */

/**
 * A static map holding the users in this application.
 */
public final class UserList {

    private static HashMap<String, User> userList = new HashMap<>();

    public static User currentUser = null;

    /**
     * Adds a new user to the userList
     * @param u the user to add
     * @return whether the user was successfully added
     */
    public static boolean addUser(User u) {
        if (userList.keySet().contains(u.getName())) {
            return false;
        }
        userList.put(u.getName(), u);
        return true;
    }

    /**
     * Gets a user from the userList based on their username
     * @param username the username of the user to get
     * @return the user associated with this username
     */
    public static User getUser(String username) {
        return userList.get(username);
    }

    /**
     * Gets the users from userList as an ArrayList<User>
     * @return the userList as an ArrayList<User>
     */
    public static List<User> getUserList() {
        return new ArrayList<>(userList.values());
    }

    private UserList() {}
}
