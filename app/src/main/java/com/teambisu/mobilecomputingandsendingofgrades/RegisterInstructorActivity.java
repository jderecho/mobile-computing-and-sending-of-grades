package com.teambisu.mobilecomputingandsendingofgrades;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Instructor;

public class RegisterInstructorActivity extends Activity {
    private SQLiteHelper mysqlite;
    private Session session;
    EditText et_firstname;
    EditText et_middlename;
    EditText et_lastname;
    EditText et_emailaddress;
    EditText et_username;
    EditText et_password;
    EditText et_subjects;
    Button btn_register_instructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_instructor);

        mysqlite = new SQLiteHelper(this);
        session = new Session(this);

        et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_middlename = (EditText) findViewById(R.id.et_middlename);
        et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_emailaddress = (EditText) findViewById(R.id.et_emailaddress);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_subjects = (EditText) findViewById(R.id.et_subjects);
        btn_register_instructor = (Button) findViewById(R.id.btn_register_instructor);

        btn_register_instructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Instructor instructor = new Instructor();
                instructor.setFirstname(et_firstname.getText().toString());
                instructor.setMiddlename(et_middlename.getText().toString());
                instructor.setLastname(et_lastname.getText().toString());
                instructor.setEmailaddress(et_emailaddress.getText().toString());
                instructor.setUsername(et_username.getText().toString());
                instructor.setPassword(et_password.getText().toString());

                if (mysqlite.insertInstructor(instructor)) {
                    Toast.makeText(RegisterInstructorActivity.this, "Account successfully registered..", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegisterInstructorActivity.this, "Something went wrong..", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mysqlite.close();
        } catch (Exception e) {

        }
    }

}
