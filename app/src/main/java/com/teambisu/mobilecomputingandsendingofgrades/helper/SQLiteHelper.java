package com.teambisu.mobilecomputingandsendingofgrades.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.teambisu.mobilecomputingandsendingofgrades.model.Instructor;
import com.teambisu.mobilecomputingandsendingofgrades.model.Section;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "bisu_dotaboys.db";
    public static final String STUDENT = "student";
    public static final String GRADES = "grades";
    public static final String INSTRUCTOR = "instructor";
    public static final String SUBJECT = "subject";
    public static final String SECTION = "section";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INSTRUCTOR);
        db.execSQL("DROP TABLE IF EXISTS " + SUBJECT);
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
        contentValues.put(Instructor.FIRSTNAME , instructor.getFirstname());
        contentValues.put(Instructor.MIDDLENAME , instructor.getMiddlename());
        contentValues.put(Instructor.LASTNAME , instructor.getLastname());
        contentValues.put(Instructor.EMAIL_ADDRESS , instructor.getEmailaddress());
        contentValues.put(Instructor.USERNAME , instructor.getUsername());
        contentValues.put(Instructor.PASSWORD , instructor.getPassword());
        contentValues.put(Instructor.SUBJECTS , instructor.getSubjects().toString());

        long result = db.insert(INSTRUCTOR, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public boolean login(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String FIND_USERNAME = String.format("SELECT * FROM %s WHERE %s = '%s'", INSTRUCTOR, Instructor.USERNAME, username);

        Cursor cursor = db.rawQuery(FIND_USERNAME, null);

        if(cursor.moveToFirst()){
            do {
                String pass = cursor.getString(cursor.getColumnIndex(Instructor.PASSWORD));
                return pass == password;
            }while (cursor.moveToNext());
        }else{
            return false;
        }
    }
    public String getInstructors(){
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s", INSTRUCTOR);

        Cursor cursor = db.rawQuery(GET_ALL, null);
        String pass = "";
        if(cursor.moveToFirst()){
            do {
                 pass += cursor.getString(cursor.getColumnIndex(Instructor.FIRSTNAME )) + "\n ";

            }while (cursor.moveToNext());
            return pass;
        }else{
            return "Empty";
        }
    }

    /*
    *
    *  SUBJECT TABLE :: This table is used for saving subject the INSTRUCTORS handle.
    *  a subject can have many classes/section
    *
    */

    public boolean insertSubject(Instructor instructor, Subject subject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Subject.NAME , subject.getName());
        contentValues.put(Subject.INSTRUCTOR_ID, instructor.getId());

        long result = db.insert(SUBJECT, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public String getSubjects(){
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL = String.format("SELECT * FROM %s", SUBJECT);

        Cursor cursor = db.rawQuery(GET_ALL, null);
        String pass = "";
        if(cursor.moveToFirst()){
            do {
                pass += cursor.getString(cursor.getColumnIndex(Instructor.FIRSTNAME )) + "\n ";

            }while (cursor.moveToNext());
            return pass;
        }else{
            return "Empty";
        }
    }
    public boolean editSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Subject.NAME, subject.getName());
        int result = db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(subject.getId()) } );

        if(result == -1){
            return true;
        }else {
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
