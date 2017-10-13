package com.teambisu.mobilecomputingandsendingofgrades.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.teambisu.mobilecomputingandsendingofgrades.LoginActivity;
import com.teambisu.mobilecomputingandsendingofgrades.model.Grades;
import com.teambisu.mobilecomputingandsendingofgrades.model.Instructor;
import com.teambisu.mobilecomputingandsendingofgrades.model.Score;
import com.teambisu.mobilecomputingandsendingofgrades.model.Section;
import com.teambisu.mobilecomputingandsendingofgrades.model.Student;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "bisu_dotaboys.db";
    public static final String STUDENT = "student";
    public static final String GRADES = "grades";
    public static final String INSTRUCTOR = "instructor";
    public static final String SUBJECT = "subject";
    public static final String SECTION = "section";
    public static final String SCORE = "score";

    Session session;
    Context context;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        session = new Session(context);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + INSTRUCTOR + "(" +
                Instructor.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Instructor.FIRSTNAME + " TEXT," +
                Instructor.MIDDLENAME + " TEXT," +
                Instructor.LASTNAME + " TEXT," +
                Instructor.EMAIL_ADDRESS + " TEXT," +
                Instructor.USERNAME + " TEXT," +
                Instructor.PASSWORD + " TEXT," +
                Instructor.SUBJECTS + " TEXT )");

        db.execSQL("create table " + SUBJECT + "(" +
                Subject.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Subject.NAME + " TEXT," +
                Subject.INSTRUCTOR_ID + " INTEGER)");

        db.execSQL("create table " + SECTION + "(" +
                Section.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Section.NAME + " TEXT," +
                Section.SUBJECT_ID + " INTEGER," +
                Section.INSTRUCTOR_ID + " INTEGER)");

        db.execSQL("create table " + STUDENT + "(" +
                Student.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Student.FIRSTNAME + " TEXT," +
                Student.MIDDLENAME + " TEXT," +
                Student.LASTNAME + " TEXT," +
                Student.EMAILADDRESS + " TEXT," +
                Student.GENDER + " INTEGER," +
                Student.SUBJECT_ID + " INTEGER," +
                Student.SECTION_ID + " INTEGER," +
                Student.INSTRUCTOR_ID + " INTEGER," +
                Student.ISGRADEREADY + " INTEGER," +
                Student.GRADEPDFLOCATION + " TEXT)");

        db.execSQL("create table " + GRADES + "(" +
                Grades.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Grades.PRELIM_GRADE + " REAL," +
                Grades.MIDTERM_GRADE + " REAL," +
                Grades.PREFINAL_GRADE + " REAL," +
                Grades.FINAL_GRADE + " REAL," +
                Grades.STUDENT_ID + " INTEGER," +
                Grades.PDFFILENAME + " TEXT," +
                Grades.STATUS + " INTEGER)");

        db.execSQL("create table " + SCORE + "(" +
                Score.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Score.DATE + " TEXT," +
                Score.TEST_NAME + " TEXT," +
                Score.SCORE + " INTEGER," +
                Score.MAXIMUM_SCORE + " INTEGER," +
                Score.STUDENT_ID + " INTEGER," +
                Score.GRADING_PERIOD + " INTEGER," +
                Score.GRADING_CATEGORY + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INSTRUCTOR);
        db.execSQL("DROP TABLE IF EXISTS " + SUBJECT);
        db.execSQL("DROP TABLE IF EXISTS " + SECTION);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + GRADES);
        db.execSQL("DROP TABLE IF EXISTS " + SCORE);
        onCreate(db);
    }

    /*
    *
    *  INSTRUCTOR TABLE :: This model is use for registering instructors and
    *  allowing instructors or teachers to use the app.
    *
     */
    public boolean insertInstructor(Instructor instructor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Instructor.FIRSTNAME, instructor.getFirstname());
        contentValues.put(Instructor.MIDDLENAME, instructor.getMiddlename());
        contentValues.put(Instructor.LASTNAME, instructor.getLastname());
        contentValues.put(Instructor.EMAIL_ADDRESS, instructor.getEmailaddress());
        contentValues.put(Instructor.USERNAME, instructor.getUsername());
        contentValues.put(Instructor.PASSWORD, instructor.getPassword());
//        contentValues.put(Instructor.SUBJECTS , instructor.getSubjects().toString());

        long result = db.insert(INSTRUCTOR, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean updateInstructor(Instructor instructor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Instructor.FIRSTNAME, instructor.getFirstname());
        contentValues.put(Instructor.MIDDLENAME, instructor.getMiddlename());
        contentValues.put(Instructor.LASTNAME, instructor.getLastname());
        contentValues.put(Instructor.EMAIL_ADDRESS, instructor.getEmailaddress());
        contentValues.put(Instructor.USERNAME, instructor.getUsername());
        contentValues.put(Instructor.PASSWORD, instructor.getPassword());

        Log.d("test", " " + instructor.getId());
        int result = db.update(INSTRUCTOR, contentValues, Instructor.ID + " = ?", new String[]{instructor.getId()});

        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        session = new Session(context);
        session.setId(0);
        session.setusername("");
    }

    public boolean login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String FIND_USERNAME = String.format("SELECT * FROM %s WHERE %s = '%s'", INSTRUCTOR, Instructor.USERNAME, username);

        Cursor cursor = db.rawQuery(FIND_USERNAME, null);
        if (cursor == null) {
            return false;
        }

        if (cursor.moveToFirst()) {
            do {
                String pass = cursor.getString(cursor.getColumnIndex(Instructor.PASSWORD));
                if (pass.equals(password)) {
                    session.setId(cursor.getInt(cursor.getColumnIndex(Instructor.ID)));
                    return true;
                }
            } while (cursor.moveToNext());
            return false;
        } else {
            return false;
        }
    }

    public Instructor getInstructor(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s WHERE %s = '%s'", INSTRUCTOR, Instructor.ID, id + "");

        Cursor cursor = db.rawQuery(GET_ALL, null);
        Instructor instructor = new Instructor();
        if (cursor.moveToFirst()) {
            do {
                instructor.setId(cursor.getInt(cursor.getColumnIndex(Instructor.ID)) + "");
                instructor.setUsername(cursor.getString(cursor.getColumnIndex(Instructor.USERNAME)));
                instructor.setPassword(cursor.getString(cursor.getColumnIndex(Instructor.PASSWORD)));
                instructor.setEmailaddress(cursor.getString(cursor.getColumnIndex(Instructor.EMAIL_ADDRESS)));
                instructor.setFirstname(cursor.getString(cursor.getColumnIndex(Instructor.FIRSTNAME)));
                instructor.setMiddlename(cursor.getString(cursor.getColumnIndex(Instructor.MIDDLENAME)));
                instructor.setLastname(cursor.getString(cursor.getColumnIndex(Instructor.LASTNAME)));
                break;
            } while (cursor.moveToNext());
        }
        return instructor;
    }

    public String getInstructorName(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String FIND_USERNAME = String.format("SELECT * FROM %s WHERE %s = '%s'", INSTRUCTOR, Instructor.ID, id + "");

        Cursor cursor = db.rawQuery(FIND_USERNAME, null);
        if (cursor.moveToFirst()) {
            do {
                String firstname = cursor.getString(cursor.getColumnIndex(Instructor.FIRSTNAME));
                String middlename = cursor.getString(cursor.getColumnIndex(Instructor.MIDDLENAME));
                String lastname = cursor.getString(cursor.getColumnIndex(Instructor.LASTNAME));

                return firstname + " " + middlename + " " + lastname;

            } while (cursor.moveToNext());
        } else {
            return "";
        }
    }

    /*
    *
    *  SUBJECT TABLE :: This table is used for saving subjects the INSTRUCTORS handle.
    *  a subject can have many classes/section
    *
    */

    public boolean insertSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Subject.NAME, subject.getName());
        contentValues.put(Subject.INSTRUCTOR_ID, subject.getInstructor_id());

        long result = db.insert(SUBJECT, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<Subject> getSubjects(int instructors_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s where instructor_id = %s", SUBJECT, Integer.toString(instructors_id));

        Cursor cursor = db.rawQuery(GET_ALL, null);
        ArrayList<Subject> arrayList = new ArrayList<Subject>();

        if (cursor.moveToFirst()) {
            do {
                Subject subject = new Subject();
                subject.setId(cursor.getInt(cursor.getColumnIndex(Subject.ID)));
                subject.setName(cursor.getString(cursor.getColumnIndex(Subject.NAME)));
                subject.setInstructor_id(cursor.getInt(cursor.getColumnIndex(Subject.INSTRUCTOR_ID)));
                arrayList.add(subject);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public boolean updateSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Subject.NAME, subject.getName());

        int result = db.update(SUBJECT, contentValues, Subject.ID + " = ?", new String[]{Integer.toString(subject.getId())});

        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(SUBJECT, Subject.ID + " = ? ", new String[]{Integer.toString(subject.getId())});

        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }
    /*
     *
     * Section/Class Model
     *
     *
     */

    public boolean insertSection(Section section) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Section.NAME, section.getName());
        contentValues.put(Section.SUBJECT_ID, section.getSubject_id());
        contentValues.put(Section.INSTRUCTOR_ID, session.getId());

        long result = db.insert(SECTION, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateSection(Section section) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Section.NAME, section.getName());

        int result = db.update(SECTION, contentValues, Subject.ID + " = ?", new String[]{Integer.toString(section.getId())});

        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteSection(Section section) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(SECTION, Section.ID + " = ? ", new String[]{Integer.toString(section.getId())});
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Section> getSections(int instructors_id, int subject_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s where %s = %s", SECTION, Section.SUBJECT_ID, Integer.toString(subject_id));
        Cursor cursor = db.rawQuery(GET_ALL, null);
        ArrayList<Section> arrayList = new ArrayList<Section>();
        if (cursor.moveToFirst()) {
            do {
                Section section = new Section();
                section.setId(cursor.getInt(cursor.getColumnIndex(Section.ID)));
                section.setName(cursor.getString(cursor.getColumnIndex(Section.NAME)));
                section.setInstructor_id(cursor.getInt(cursor.getColumnIndex(Section.INSTRUCTOR_ID)));
                section.setSubject_id(cursor.getInt(cursor.getColumnIndex(Section.SUBJECT_ID)));
                arrayList.add(section);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    /*
    *
    * Students Model
    *
     */

    public boolean insertStudent(Student student) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Student.FIRSTNAME, student.getFirstname());
        contentValues.put(Student.MIDDLENAME, student.getMiddlename());
        contentValues.put(Student.LASTNAME, student.getLastname());
        contentValues.put(Student.EMAILADDRESS, student.getEmailaddress());
        contentValues.put(Student.GENDER, student.getGender());
        contentValues.put(Student.SUBJECT_ID, student.getSubject_id());
        contentValues.put(Student.SECTION_ID, student.getSection_id());
        contentValues.put(Student.INSTRUCTOR_ID, session.getId());

        long result = db.insert(STUDENT, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateStudentGrade(Student student) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Student.ISGRADEREADY, student.getIsGradeReady());
        if (student.getGradePDFlocation() != null) {
            contentValues.put(Student.GRADEPDFLOCATION, student.getGradePDFlocation());
        }
        long result = db.update(STUDENT, contentValues, Student.ID + " = ?", new String[]{Integer.toString(student.getId())});

        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateStudent(Student student) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Student.FIRSTNAME, student.getFirstname());
        contentValues.put(Student.MIDDLENAME, student.getMiddlename());
        contentValues.put(Student.LASTNAME, student.getLastname());
        contentValues.put(Student.EMAILADDRESS, student.getEmailaddress());
        contentValues.put(Student.GENDER, student.getGender());

        long result = db.update(STUDENT, contentValues, Student.ID + " = ?", new String[]{Integer.toString(student.getId())});

        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(STUDENT, Student.ID + " = ? ", new String[]{Integer.toString(student.getId())});
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public Student getStudent(int student_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s where %s = %s ", STUDENT,
                Student.ID, Integer.toString(student_id));
        Student student = new Student();


        Cursor cursor = db.rawQuery(GET_ALL, null);
        ArrayList<Student> arrayList = new ArrayList<Student>();
        if (cursor.moveToFirst()) {
            do {
                student.setId(cursor.getInt(cursor.getColumnIndex(Student.ID)));
                student.setSection_id(cursor.getInt(cursor.getColumnIndex(Student.SECTION_ID)));
                student.setSubject_id(cursor.getInt(cursor.getColumnIndex(Student.SUBJECT_ID)));
                student.setFirstname(cursor.getString(cursor.getColumnIndex(Student.FIRSTNAME)));
                student.setMiddlename(cursor.getString(cursor.getColumnIndex(Student.MIDDLENAME)));
                student.setLastname(cursor.getString(cursor.getColumnIndex(Student.LASTNAME)));
                student.setEmailaddress(cursor.getString(cursor.getColumnIndex(Student.EMAILADDRESS)));
                Log.d("test", "email address " + cursor.getString(cursor.getColumnIndex(Student.EMAILADDRESS)));
                student.setGender(cursor.getInt(cursor.getColumnIndex(Student.GENDER)));
                student.setInstructor_id(cursor.getInt(cursor.getColumnIndex(Student.INSTRUCTOR_ID)));
                student.setIsGradeReady(cursor.getInt(cursor.getColumnIndex(Student.ISGRADEREADY)));
                student.setGradePDFlocation(cursor.getString(cursor.getColumnIndex(Student.GRADEPDFLOCATION)));

            } while (cursor.moveToNext());
        }
        return student;
    }

    public ArrayList<Student> getStudents(int instructors_id, int section_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s where %s = %s AND %s = %s", STUDENT,
                Student.INSTRUCTOR_ID, Integer.toString(instructors_id),
                Student.SECTION_ID, Integer.toString((section_id)));

        Log.d("test", GET_ALL);

        Cursor cursor = db.rawQuery(GET_ALL, null);
        ArrayList<Student> arrayList = new ArrayList<Student>();
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(cursor.getColumnIndex(Student.ID)));
                student.setSection_id(cursor.getInt(cursor.getColumnIndex(Student.SECTION_ID)));
                Log.d("test", "student ID: " + cursor.getInt(cursor.getColumnIndex(Student.ID)) + "");
                student.setSubject_id(cursor.getInt(cursor.getColumnIndex(Student.SUBJECT_ID)));
                student.setFirstname(cursor.getString(cursor.getColumnIndex(Student.FIRSTNAME)));
                student.setMiddlename(cursor.getString(cursor.getColumnIndex(Student.MIDDLENAME)));
                student.setLastname(cursor.getString(cursor.getColumnIndex(Student.LASTNAME)));
                student.setEmailaddress(cursor.getString(cursor.getColumnIndex(Student.EMAILADDRESS)));
                student.setGender(cursor.getInt(cursor.getColumnIndex(Student.GENDER)));
                student.setInstructor_id(cursor.getInt(cursor.getColumnIndex(Student.INSTRUCTOR_ID)));
                student.setIsGradeReady(cursor.getInt(cursor.getColumnIndex(Student.ISGRADEREADY)));
                student.setGradePDFlocation(cursor.getString(cursor.getColumnIndex(Student.GRADEPDFLOCATION)));
                arrayList.add(student);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    /*
    *
    * Grades
    *
    * */
    public boolean insertGrade(Grades grade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Grades.PRELIM_GRADE, grade.getPrelim_grade());
        contentValues.put(Grades.MIDTERM_GRADE, grade.getMidterm_grade());
        contentValues.put(Grades.PREFINAL_GRADE, grade.getPrefinal_grade());
        contentValues.put(Grades.FINAL_GRADE, grade.getFinal_grade());
        contentValues.put(Grades.STATUS, grade.getSent());
        contentValues.put(Grades.STUDENT_ID, grade.getStudent_id());
        long result = db.insert(GRADES, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Grades getMyGrade(int student_id) {
        Grades grade = new Grades();

        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s where %s = %s", GRADES,
                Grades.STUDENT_ID, Integer.toString((student_id)));

        Log.d("test", GET_ALL);
        Cursor cursor = db.rawQuery(GET_ALL, null);
        Log.d("test", "cursor " + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                grade.setId(cursor.getInt(cursor.getColumnIndex(Grades.ID)));
                grade.setPrelim_grade(cursor.getFloat(cursor.getColumnIndex(Grades.PRELIM_GRADE)));
                grade.setMidterm_grade(cursor.getFloat(cursor.getColumnIndex(Grades.MIDTERM_GRADE)));
                grade.setPrefinal_grade(cursor.getFloat(cursor.getColumnIndex(Grades.PREFINAL_GRADE)));
                grade.setFinal_grade(cursor.getFloat(cursor.getColumnIndex(Grades.FINAL_GRADE)));
                grade.setStudent_id(cursor.getInt(cursor.getColumnIndex(Grades.STUDENT_ID)));
                Log.d("test", "grades student id: " + cursor.getInt(cursor.getColumnIndex(Grades.STUDENT_ID)));
                grade.setSent(cursor.getInt(cursor.getColumnIndex(Grades.STATUS)));
            } while (cursor.moveToNext());
        }
        return grade;
    }

    /*
    *
    * Scores
    *
    * */
    public boolean insertScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Score.DATE, score.getDate());
        contentValues.put(Score.TEST_NAME, score.getTest_name());
        contentValues.put(Score.SCORE, score.getScore());
        contentValues.put(Score.MAXIMUM_SCORE, score.getMaximum_score());
        contentValues.put(Score.STUDENT_ID, score.getStudent_id());
        contentValues.put(Score.GRADING_PERIOD, score.getGrading_period());
        contentValues.put(Score.GRADING_CATEGORY, score.getGrading_category());

        long result = db.insert(SCORE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<Score> getScore(int student_id, int grading_period, int grading_category) {
        ArrayList<Score> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s where %s = %s AND %s = %s AND %s = %s", SCORE,
                Score.STUDENT_ID, Integer.toString(student_id), Score.GRADING_PERIOD, grading_period, Score.GRADING_CATEGORY, grading_category);
//        String GET_ALL = String.format("SELECT * FROM %s", SCORE);
        Cursor cursor = db.rawQuery(GET_ALL, null);
        if (cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setId(cursor.getInt(cursor.getColumnIndex(Score.ID)));
                score.setDate(cursor.getString(cursor.getColumnIndex(Score.DATE)));
                score.setTest_name(cursor.getString(cursor.getColumnIndex(Score.TEST_NAME)));
                score.setScore(cursor.getInt(cursor.getColumnIndex(Score.SCORE)));
                score.setMaximum_score(cursor.getInt(cursor.getColumnIndex(Score.MAXIMUM_SCORE)));
                score.setMaximum_score(cursor.getInt(cursor.getColumnIndex(Score.MAXIMUM_SCORE)));
                score.setStudent_id(cursor.getInt(cursor.getColumnIndex(Score.STUDENT_ID)));
                score.setGrading_period(cursor.getInt(cursor.getColumnIndex(Score.GRADING_PERIOD)));
                score.setGrading_category(cursor.getInt(cursor.getColumnIndex(Score.GRADING_CATEGORY)));
                scores.add(score);

            } while (cursor.moveToNext());
        }
        return scores;
    }

    public ArrayList<Score> getGradingPeriodScores(int student_id, int grading_period) {
        ArrayList<Score> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s where %s = %s AND %s = %s", SCORE,
                Score.STUDENT_ID, Integer.toString(student_id), Score.GRADING_PERIOD, grading_period);
//        String GET_ALL = String.format("SELECT * FROM %s", SCORE);
        Cursor cursor = db.rawQuery(GET_ALL, null);
        if (cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setId(cursor.getInt(cursor.getColumnIndex(Score.ID)));
                score.setDate(cursor.getString(cursor.getColumnIndex(Score.DATE)));
                score.setTest_name(cursor.getString(cursor.getColumnIndex(Score.TEST_NAME)));
                score.setScore(cursor.getInt(cursor.getColumnIndex(Score.SCORE)));
                score.setMaximum_score(cursor.getInt(cursor.getColumnIndex(Score.MAXIMUM_SCORE)));
                score.setMaximum_score(cursor.getInt(cursor.getColumnIndex(Score.MAXIMUM_SCORE)));
                score.setStudent_id(cursor.getInt(cursor.getColumnIndex(Score.STUDENT_ID)));
                score.setGrading_period(cursor.getInt(cursor.getColumnIndex(Score.GRADING_PERIOD)));
                score.setGrading_category(cursor.getInt(cursor.getColumnIndex(Score.GRADING_CATEGORY)));
                scores.add(score);

            } while (cursor.moveToNext());
        }
        return scores;
    }

    public boolean updateScore(Score score) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Score.SCORE, score.getScore());
        contentValues.put(Score.MAXIMUM_SCORE, score.getMaximum_score());
        contentValues.put(Score.DATE, score.getDate());

        long result = db.update(SCORE, contentValues, Score.ID + " = ?", new String[]{Integer.toString(score.getId())});

        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(SCORE, Score.ID + " = ? ", new String[]{Integer.toString(score.getId())});
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

//    public boolean insertData(String lastname, String firstname){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_2, lastname);
//        contentValues.put(COL_3, firstname);
//
//        long result = db.insert(TABLE_NAME, null, contentValues);
//
//        if (result == -1 )
//            return false;
//        else
//            return true;
//    }
//    public FullName[] getAllFullNames(){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        FullName[] fullnames = new FullName[100];
//        int i = 0;
//        while(cursor.moveToNext()){
//            fullnames[i++] = new FullName(cursor.getInt(0),cursor.getString(1), cursor.getString(2));
//        }
//        return fullnames;
//
//    }
//    public String[] getAllNames(){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        String[] fullnames = new String[100];
//
//        int i = 0;
//        while(cursor.moveToNext()){
//            fullnames[i++] = cursor.getString(1) + cursor.getString(2);
//        }
//        for (int j = 0; j < 100 ; j++){
//            // fix for null index
//            if (fullnames[j] == null){
//                fullnames[j] = " ";
//            }
//        }
//        return fullnames;
//    }
}
