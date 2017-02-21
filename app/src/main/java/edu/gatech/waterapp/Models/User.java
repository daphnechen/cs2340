package edu.gatech.waterapp.Models;

/**
 * Created by Derian on 2/15/2017.
 */

/**
 * Represents a user of the water app.
 */
public class User {

    private String username;
    private String password;
    private AccountType accountType;
    private String email;

    /**
     * Creates a new user.
     * @param name the user's username
     * @param password the user's password
     * @param type the user's account type
     */
    public User(String name, String password, AccountType type) {
        this.username = name;
        this.password = password;
        accountType = type;
    }

    /**
     * @return this user's username
     */
    public String getName() {
        return username;
    }

    /**
     * @return this user's password
     */
    public String getPassword() {
        return password;
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
     * Sets this user's password
     *  @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets this user's account type
     *  @param accountType the account type to set
     */
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }


    @Override
    public String toString() {
        return accountType + ": " + username;
    }
}
