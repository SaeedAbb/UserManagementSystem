package com.xAdmin.userMangment.bean;

/**
 * this class represents an user
 *
 * @author Saeed
 * @since 04.04.2021
 */

public class User {
    private int id;
    private String name;
    private String email;
    private String country;

    public User(int id, String name, String email, String country) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public User(String name, String email, String country) {
        this.name = name;
        this.email = email;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }


}
