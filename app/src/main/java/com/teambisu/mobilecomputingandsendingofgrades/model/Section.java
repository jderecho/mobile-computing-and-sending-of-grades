package com.teambisu.mobilecomputingandsendingofgrades.model;

/**
 * Created by John Manuel on 21/08/2017.
 */

public class Section {
    private int id;
    private String name;
    private String course;
    private String year;
    private String section;

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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
