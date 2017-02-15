package edu.gatech.waterapp.Models;

/**
 * Created by Derian on 2/15/2017.
 */

public class User {

    private String username;
    private String password;
    private AccountType accountType;

    private String email;
    private String address;

    public User(String name, String password, AccountType type) {
        this.username = name;
        this.password = password;
        accountType = type;
    }

    public String getName() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.username = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
