package com.teambisu.mobilecomputingandsendingofgrades.model;

import android.util.Log;

public class Grades {
    private int id;
    private float prelim_grade;
    private float midterm_grade;
    private float prefinal_grade;
    private float final_grade;
    private int sent;
    private int student_id;
    private String pdfFileName;

    public static String ID = "id";
    public static String PRELIM_GRADE = "prelim_grade";
    public static String MIDTERM_GRADE = "midterm_grade";
    public static String PREFINAL_GRADE = "prefinal_grade";
    public static String FINAL_GRADE = "final_grade";
    public static String STATUS = "sent";
    public static String STUDENT_ID = "student_id";
    public static String PDFFILENAME = "pdfFileName";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSent() {
        return sent == 1;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public String getStatus() {
        if (sent == 1) {
            return "Grades already sent.";
        } else {
            return "Grades not yet sent.";
        }
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }


    public float getPrelim_grade() {
        return prelim_grade;
    }

    public void setPrelim_grade(float prelim_grade) {
        this.prelim_grade = prelim_grade;
    }

    public float getMidterm_grade() {
        return midterm_grade;
    }

    public void setMidterm_grade(float midterm_grade) {
        this.midterm_grade = midterm_grade;
    }

    public float getPrefinal_grade() {
        return prefinal_grade;
    }

    public void setPrefinal_grade(float prefinal_grade) {
        this.prefinal_grade = prefinal_grade;
    }

    public float getFinal_grade() {
        return final_grade;
    }

    public void setFinal_grade(float final_grade) {
        this.final_grade = final_grade;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public float GWA() {
        return (prelim_grade + midterm_grade + prefinal_grade + final_grade) / 4;
    }

    public float GWA_MIDTERM() {
        return (prelim_grade + midterm_grade) / 2;
    }

    public float GWA_FINAL() {
        return (prefinal_grade + final_grade) / 2;
    }

    public static float GWA(int prelim_grade, int midterm_grade, int prefinal_grade, int final_grade) {
        return (prelim_grade + midterm_grade + prefinal_grade + final_grade) / 4;
    }

    public static float GWA(int prelim_grade, int midterm_grade) {
        return (prelim_grade + midterm_grade) / 2;
    }

    public static float getPointEquivalent(int percentage) {
        if (percentage == 99 || percentage == 100) {
            return 1.0F;
        } else if (percentage == 97 || percentage == 98) {
            return 1.1F;
        } else if (percentage == 95 || percentage == 96) {
            return 1.2F;
        } else if (percentage == 93 || percentage == 94) {
            return 1.3F;
        } else if (percentage == 91 || percentage == 92) {
            return 1.4F;
        } else if (percentage == 90) {
            return 1.5F;
        } else if (percentage == 89) {
            return 1.6F;
        } else if (percentage == 88) {
            return 1.7F;
        } else if (percentage == 87) {
            return 1.8F;
        } else if (percentage == 86) {
            return 1.9F;
        } else if (percentage == 85) {
            return 2.0F;
        } else if (percentage == 84) {
            return 2.1F;
        } else if (percentage == 83) {
            return 2.2F;
        } else if (percentage == 82) {
            return 2.3F;
        } else if (percentage == 81) {
            return 2.4F;
        } else if (percentage == 80) {
            return 2.5F;
        } else if (percentage == 79) {
            return 2.6F;
        } else if (percentage == 78) {
            return 2.7F;
        } else if (percentage == 77) {
            return 2.8F;
        } else if (percentage == 76) {
            return 2.9F;
        } else if (percentage == 75) {
            return 3.0F;
        } else if (percentage <= 74) {
            return 3.0F + ((75 - percentage) * 0.1F) > 5 ? 5 : 3.0F + ((75 - percentage) * 0.1F);
        } else {
            return 5.0F;
        }
    }

    public static String getStringGrade(float percentage) {
        if (percentage <= 100 || percentage >= 95) {
            return "Excellent";
        } else if (percentage <= 94 || percentage >= 90) {
            return "Very Good";
        } else if (percentage <= 89 || percentage >= 80) {
            return "Good";
        } else if (percentage <= 85 || percentage >= 79) {
            return "Fair";
        } else {
            return "";
        }
    }

    public boolean isInputted() {
        Log.d("tesst", prelim_grade + "" + midterm_grade + "" + prefinal_grade + "" + final_grade + "" + id);
        if (prelim_grade == 0 || midterm_grade == 0 || prefinal_grade == 0 || final_grade == 0 || id == 0) {
            return false;
        }
        return true;
    }

    //      1.0 - 99-100 - excellent
//      1.1 - 97 - 98 Excellent
//      1.2 - 95-96  - Excellent
//
//    Very Good
//        1.3 - 93-94
//        1.4  - 91-92
//        1.5 - 90
//    Good
//        1.6 - 89
//        1.7 - 88
//        1.8 - 87
//        1.9 - 86
//        2.0 - 85
//        2.1 - 84
//        2.2 - 83
//        2.3 - 82
//        2.4 - 81
//        2.5 - 80
//    Fair
//        2.6 - 79
//        2.7 - 78
//        2.8 - 77
//        2.9 - 76
//        3.0 - 75

}
