package com.teambisu.mobilecomputingandsendingofgrades.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.teambisu.mobilecomputingandsendingofgrades.model.Instructor;
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
                Student.SUBJECT_ID + " INTEGER," +
                Student.SECTION_ID + " INTEGER," +
                Student.INSTRUCTOR_ID + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INSTRUCTOR);
        db.execSQL("DROP TABLE IF EXISTS " + SUBJECT);
        db.execSQL("DROP TABLE IF EXISTS " + SECTION);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT);
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
    public void logout(){
        session = new Session(context);
        session.setId(0);
        session.setusername("");
    }
    public boolean login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String FIND_USERNAME = String.format("SELECT * FROM %s WHERE %s = '%s'", INSTRUCTOR, Instructor.USERNAME, username);

        Cursor cursor = db.rawQuery(FIND_USERNAME, null);
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

    public String getInstructors() {
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s", INSTRUCTOR);

        Cursor cursor = db.rawQuery(GET_ALL, null);
        String pass = "";
        if (cursor.moveToFirst()) {
            do {
                pass += cursor.getString(cursor.getColumnIndex(Instructor.FIRSTNAME)) + "\n ";

            } while (cursor.moveToNext());
            return pass;
        } else {
            return "Empty";
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

        Log.d("test", GET_ALL);
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
        Log.d("test","result: " + result);

        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(SUBJECT, Subject.ID + " = ? ", new String[]{Integer.toString(subject.getId())});

        if (result == -1) {
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

    public ArrayList<Section> getSections(int instructors_id, int subject_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s where %s = %s", SECTION, Section.SUBJECT_ID, Integer.toString(subject_id));

//        String GET_ALL = String.format("SELECT * FROM %s ", SECTION);
        Log.d("test", GET_ALL);
        Cursor cursor = db.rawQuery(GET_ALL, null);
//        Log.d("test", );
        ArrayList<Section> arrayList = new ArrayList<Section>();
        if (cursor.moveToFirst()) {
            do {
                Section section = new Section();
                section.setId(cursor.getInt(cursor.getColumnIndex(Section.ID)));
                section.setName(cursor.getString(cursor.getColumnIndex(Section.NAME)));
                section.setInstructor_id(cursor.getInt(cursor.getColumnIndex(Section.INSTRUCTOR_ID)));
                section.setSubject_id(cursor.getInt(cursor.getColumnIndex(Section.SUBJECT_ID)));
                Log.d("test","section: " + cursor.getInt(cursor.getColumnIndex(Section.SUBJECT_ID)));
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
                student.setFirstname(cursor.getString(cursor.getColumnIndex(Student.FIRSTNAME)));
                student.setMiddlename(cursor.getString(cursor.getColumnIndex(Student.MIDDLENAME)));
                student.setLastname(cursor.getString(cursor.getColumnIndex(Student.LASTNAME)));
                arrayList.add(student);
            } while (cursor.moveToNext());
        }
        return arrayList;
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
