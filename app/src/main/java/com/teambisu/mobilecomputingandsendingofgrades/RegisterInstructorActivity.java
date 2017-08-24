package com.teambisu.mobilecomputingandsendingofgrades;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterInstructorActivity extends Activity {
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
                String firstname = et_firstname.getText().toString();
                String middlename = et_middlename.getText().toString();
                String lastname = et_lastname.getText().toString();
                String emailaddress = et_emailaddress.getText().toString();
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String subjects = et_subjects.getText().toString();


                Log.d("test",subjects);
            }
        });

    }


}
