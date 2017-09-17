package com.teambisu.mobilecomputingandsendingofgrades;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Instructor;

public class UpdateInstructorActivity extends Activity {
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
    EditText et_password2;

    Instructor currentInstructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_instructor);

        mysqlite = new SQLiteHelper(this);
        session = new Session(this);

        et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_middlename = (EditText) findViewById(R.id.et_middlename);
        et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_emailaddress = (EditText) findViewById(R.id.et_emailaddress);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_subjects = (EditText) findViewById(R.id.et_subjects);
        et_password2 = (EditText) findViewById(R.id.et_password2);
        btn_register_instructor = (Button) findViewById(R.id.btn_register_instructor);

        currentInstructor = mysqlite.getInstructor(session.getId());

        et_firstname.setText(currentInstructor.getFirstname());
        et_middlename.setText(currentInstructor.getMiddlename());
        et_lastname.setText(currentInstructor.getLastname());
        et_emailaddress.setText(currentInstructor.getEmailaddress());
        et_username.setText(currentInstructor.getUsername());
        et_password.setText(currentInstructor.getPassword());
        et_password2.setText(currentInstructor.getPassword());


        btn_register_instructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_firstname.getText().toString().isEmpty()) {
                    et_firstname.setError("Input required field");
                    et_firstname.requestFocus();
                    return;
                }
                if (et_lastname.getText().toString().isEmpty()) {
                    et_lastname.setError("Input required field");
                    et_lastname.requestFocus();
                    return;
                }
                if (et_emailaddress.getText().toString().isEmpty()) {
                    et_emailaddress.setError("Input required field");
                    et_emailaddress.requestFocus();
                    return;
                }

                if (!Session.isValidEmailAddress(et_emailaddress.getText().toString())) {
                    et_emailaddress.setError("Please input a valid email");
                    et_emailaddress.requestFocus();
                    return;
                }
                if (et_username.getText().toString().isEmpty()) {
                    et_username.setError("Input required field");
                    et_username.requestFocus();
                    return;
                }
                if (et_password.getText().toString().isEmpty()) {
                    et_password.setError("Input required field");
                    et_password.requestFocus();
                    return;
                }
                if (!et_password.getText().toString().equals(et_password2.getText().toString())) {
                    et_password2.requestFocus();
                    Toast.makeText(UpdateInstructorActivity.this, "Password don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                Instructor instructor = new Instructor();
                instructor.setId(currentInstructor.getId());
                instructor.setFirstname(et_firstname.getText().toString());
                instructor.setMiddlename(et_middlename.getText().toString());
                instructor.setLastname(et_lastname.getText().toString());
                instructor.setEmailaddress(et_emailaddress.getText().toString());
                instructor.setUsername(et_username.getText().toString());
                instructor.setPassword(et_password.getText().toString());

                if (mysqlite.updateInstructor(instructor)) {
                    Toast.makeText(UpdateInstructorActivity.this, "Instructor info successfully updated..", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(UpdateInstructorActivity.this, "Something went wrong..", Toast.LENGTH_LONG).show();
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
