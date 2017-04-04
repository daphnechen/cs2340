package edu.gatech.waterapp.Models;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Derian
 * Represents a user of the water app.
 */
public class User {

    private String username = "";
    private AccountType accountType;
    private String email;

    /**
     * Creates a new user.
     * @param email the user's email address
     * @param type the user's account type
     */
    public User(String email, AccountType type) {
        this.email = email;
        accountType = type;
    }

    public User() {}

    /**
     * @return this user's username
     */
    public String getName() {
        return username;
    }

    /**
     * @return this user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return this user's account type
     */
    public AccountType getAccountType() {
        return accountType;
    }

    /**
     * Sets this user's email
     *  @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets this user's username
     *  @param name the username to set
     */
    public void setName(String name) {
        this.username = name;
    }

    /**
     * Sets this user's account type
     *  @param accountType the account type to set
     */
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }


    /**
     * Creates a Map representation of the User information
     * @return a HashMap representation of the User information
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("username", username);
        map.put("accountType", accountType.toString());
        return map;
    }

    @Override
    public String toString() {
        return accountType + ": " + username;
    }
}
