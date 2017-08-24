package com.teambisu.mobilecomputingandsendingofgrades.model;

/**
 * Created by John Manuel on 21/08/2017.
 */

public class Subject {
    private int id;
    private String name;

    private int instructor_id;

    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String INSTRUCTOR_ID = "instructor_id";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInstructor_id() {
        return instructor_id;
    }
    public void setInstructor_id(int instructor_id) {
        this.instructor_id = instructor_id;
    }


}
