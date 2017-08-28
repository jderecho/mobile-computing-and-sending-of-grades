package com.teambisu.mobilecomputingandsendingofgrades.student;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.ListSubjectActivity;
import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.RegisterInstructorActivity;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Instructor;
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
                Student student = new Student();
                student.setFirstname(et_firstname.getText().toString());
                student.setMiddlename(et_middlename.getText().toString());
                student.setLastname(et_lastname.getText().toString());
                student.setEmailaddress(et_emailaddress.getText().toString());
                student.setInstructor_id(session.getId());
                student.setSection_id(intent.getIntExtra("section_id", 0));
                student.setSubject_id(intent.getIntExtra("subject_id", 0));

                if(mysqlite.insertStudent(student)){
                   Toast.makeText(AddStudentActivity.this,"ADDED..", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(AddStudentActivity.this,"Something went wrong..", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
