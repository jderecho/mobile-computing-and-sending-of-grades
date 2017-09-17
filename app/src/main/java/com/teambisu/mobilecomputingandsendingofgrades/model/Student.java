package com.teambisu.mobilecomputingandsendingofgrades.model;

public class Student {
    private int id;
    private String firstname;
    private String middlename;
    private String lastname;
    private String emailaddress;
    private int subject_id;
    private int section_id;
    private int instructor_id;
    private int isGradeReady;
    private String gradePDFlocation;

    public static String ID = "id";
    public static String FIRSTNAME = "firstname";
    public static String MIDDLENAME = "middlename";
    public static String LASTNAME = "lastname";
    public static String EMAILADDRESS = "emailaddress";
    public static String SUBJECT_ID = "subject_id";
    public static String SECTION_ID = "section_id";
    public static String INSTRUCTOR_ID = "instructor_id";
    public static String ISGRADEREADY = "is_grade_ready";
    public static String GRADEPDFLOCATION = "grade_pdf_location";
    public static String FULLNAME = "fullname";

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public int getInstructor_id() {
        return instructor_id;
    }

    public void setInstructor_id(int instructor_id) {
        this.instructor_id = instructor_id;
    }

    public int getIsGradeReady() {
        return isGradeReady;
    }

    public void setIsGradeReady(int isGradeReady) {
        this.isGradeReady = isGradeReady;
    }

    public String getGradePDFlocation() {
        return gradePDFlocation;
    }

    public void setGradePDFlocation(String gradePDFlocation) {
        this.gradePDFlocation = gradePDFlocation;
    }

    public String getFullName() {
        return getFirstname() + " " + getMiddlename() + " " + getLastname();
    }
}
