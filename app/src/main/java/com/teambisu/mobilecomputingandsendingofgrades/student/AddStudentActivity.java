package com.teambisu.mobilecomputingandsendingofgrades.student;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Student;

public class AddStudentActivity extends Activity {

    private SQLiteHelper mysqlite;
    private Session session;
    EditText et_firstname;
    EditText et_middlename;
    EditText et_lastname;
    EditText et_emailaddress;
    Button btn_save;
    Intent intent;
    private int gender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        mysqlite = new SQLiteHelper(this);
        session = new Session(this);
        intent = getIntent();

        et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_middlename = (EditText) findViewById(R.id.et_middlename);
        et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_emailaddress = (EditText) findViewById(R.id.et_emailaddress);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
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
                Student student = new Student();
                student.setFirstname(et_firstname.getText().toString());
                student.setMiddlename(et_middlename.getText().toString());
                student.setLastname(et_lastname.getText().toString());
                student.setGender(gender);
                student.setEmailaddress(et_emailaddress.getText().toString());
                student.setInstructor_id(session.getId());
                student.setSection_id(intent.getIntExtra(Student.SECTION_ID, 0));
                student.setSubject_id(intent.getIntExtra(Student.SUBJECT_ID, 0));

                if (mysqlite.insertStudent(student)) {
                    Toast.makeText(AddStudentActivity.this, "Student added..", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AddStudentActivity.this, "Something went wrong..", Toast.LENGTH_LONG).show();
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
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.gender_male:
                if (checked)
                    // gender male
                    gender = 1;
                    break;
            case R.id.gender_female:
                if (checked)
                    // gender female
                    gender = 2;
                    break;
        }
    }

}
