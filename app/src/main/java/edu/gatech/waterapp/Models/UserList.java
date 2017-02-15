package edu.gatech.waterapp.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Derian on 2/15/2017.
 */

public final class UserList {

    private static HashMap<String, User> userList = new HashMap<>();

    public static boolean addUser(User u) {
        if (userList.keySet().contains(u.getName())) {
            return false;
        }
        userList.put(u.getName(), u);
        return true;
    }

    public static User getUser(String username) {
        return userList.get(username);
    }

    public static List<User> getUserList() {
        return new ArrayList<>(userList.values());
    }

    private UserList() {}
}
