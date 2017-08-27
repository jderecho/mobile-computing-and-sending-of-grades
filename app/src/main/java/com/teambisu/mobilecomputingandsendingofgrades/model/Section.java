package com.teambisu.mobilecomputingandsendingofgrades.model;

/**
 * Created by John Manuel on 21/08/2017.
 */

public class Section {
    private int id;
    private String name;
    private int subject_id;
    private int instructor_id;
    public static String ID = "id";
    public static String NAME = "name";
    public static String SUBJECT_ID = "subject_id";
    public static String INSTRUCTOR_ID = "instructor_id";

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

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getInstructor_id() {
        return instructor_id;
    }

    public void setInstructor_id(int instructor_id) {
        this.instructor_id = instructor_id;
    }
}
