package com.teambisu.mobilecomputingandsendingofgrades.model;

/**
 * Created by John Manuel on 21/08/2017.
 */

public class Instructor{
    private String id;
    private String firstname;
    private String middlename;
    private String lastname;
    private String emailaddress;
    private String username;
    private String password;
    private Subject subjects;

    public static String ID = "id";
    public static String FIRSTNAME = "firstname";
    public static String MIDDLENAME = "middlename";
    public static String LASTNAME = "lastname";
    public static String EMAIL_ADDRESS = "emailaddress";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String SUBJECTS = "subjects";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Subject getSubjects() {
        return subjects;
    }

    public void setSubjects(Subject subjects) {
        this.subjects = subjects;
    }
}
