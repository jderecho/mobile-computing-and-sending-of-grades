package com.teambisu.mobilecomputingandsendingofgrades.model;

/**
 * Created by John Manuel on 03/09/2017.
 */

public class Score {
    private int id;
    private String date;
    private String test_name;
    private int score;
    private int maximum_score;
    private int student_id;
    private int grading_period;
    private int grading_category;

    public static String ID = "id";
    public static String DATE = "date";
    public static String TEST_NAME = "test_name";
    public static String SCORE = "score";
    public static String MAXIMUM_SCORE = "maximum_score";
    public static String STUDENT_ID = "student_id";
    public static String GRADING_PERIOD = "grading_period";
    public static String GRADING_CATEGORY = "grading_category";


    public static int PRELIM = 1, MIDTERM = 2, PREFINAL = 3, FINAL = 4;


    public static int PROJECTS = 1, MAJOR_EXAMS = 2, EVALUATION = 3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMaximum_score() {
        return maximum_score;
    }

    public void setMaximum_score(int maximum_score) {
        this.maximum_score = maximum_score;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getGrading_period() {
        return grading_period;
    }

    public void setGrading_period(int grading_period) {
        this.grading_period = grading_period;
    }

    public int getGrading_category() {
        return grading_category;
    }

    public void setGrading_category(int grading_category) {
        this.grading_category = grading_category;
    }

    public static String getGradingPeriodString(int period) {
        if (period == 1) {
            return "Prelim";
        } else if (period == 2) {
            return "Midterm";
        } else if (period == 3) {
            return "Prefinal";
        } else {
            return "Final";
        }
    }

    public static String getGradingCategoryString(int category) {
        if (category == 1) {
            return "Projects";
        } else if (category == 2) {
            return "Major Exams";
        } else {
            return "Evaluation";
        }
    }
    public static float getPercentageofCategory(int category){
        if (category == 1) {
            return 0.30F;
        } else if (category == 2) {
            return 0.30F;
        } else {
            return 0.40F;
        }
    }

    public static float computePercentageGrade(float score, float max, float percentage){
        return ((score / max) * 100) * percentage;
    }
}