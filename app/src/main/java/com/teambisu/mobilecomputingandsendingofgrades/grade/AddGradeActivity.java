package com.teambisu.mobilecomputingandsendingofgrades.grade;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Grades;

public class AddGradeActivity extends Activity {
    EditText et_prelim_grade;
    EditText et_midterm_grade;
    EditText et_prefinal_grade;
    EditText et_final_grade;
    Button btn_save;

    Intent intent;
    SQLiteHelper mysqlite;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);

        intent = getIntent();
        mysqlite = new SQLiteHelper(this);
        session = new Session(this);

        et_prelim_grade = (EditText) findViewById(R.id.et_prelim_grade);
        et_midterm_grade = (EditText) findViewById(R.id.et_midterm_grade);
        et_prefinal_grade = (EditText) findViewById(R.id.et_prefinal_grade);
        et_final_grade = (EditText) findViewById(R.id.et_final_grade);
        btn_save = (Button) findViewById(R.id.btn_save);

        et_prelim_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddGradeActivity.this, GradingPeriodActivity.class);
                startActivity(intent);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Grades grade = new Grades();
                grade.setPrelim_grade(Float.parseFloat(et_prelim_grade.getText().toString()));
                grade.setMidterm_grade(Float.parseFloat(et_midterm_grade.getText().toString()));
                grade.setPrefinal_grade(Float.parseFloat(et_prefinal_grade.getText().toString()));
                grade.setFinal_grade(Float.parseFloat(et_final_grade.getText().toString()));
                grade.setStudent_id(intent.getIntExtra(Grades.STUDENT_ID, 0));
                grade.setSent(0);

                if (mysqlite.insertGrade(grade)) {
                    Toast.makeText(AddGradeActivity.this, "Successfully saved.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AddGradeActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
    *  "Name: "
    *  "Subject: "
    *  "Instructor: "
    *  "Date: "
    *
    *  Dear Student,
    *
    *
    *  Grades:
    *  Prelim: 1.2 Midterm 1.2
    *
    * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mysqlite.close();
        } catch (Exception e) {

        }
    }
}
