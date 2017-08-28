package com.teambisu.mobilecomputingandsendingofgrades;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Section;
import com.teambisu.mobilecomputingandsendingofgrades.model.Student;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;
import com.teambisu.mobilecomputingandsendingofgrades.student.AddStudentActivity;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends Activity {
    ListView lv_students;
    TextView tv_student_title;
    FloatingActionButton btn_add;
    FloatingActionButton btn_edit;
    FloatingActionButton btn_delete;
    List<String> students;
    ArrayList<Student> myStudents;
    Session session;
    SQLiteHelper mysqlite;

    Section currentSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        tv_student_title = (TextView) findViewById(R.id.tv_student_title);
        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_edit = (FloatingActionButton) findViewById(R.id.btn_edit);
        btn_delete = (FloatingActionButton) findViewById(R.id.btn_delete);
        lv_students = (ListView) findViewById(R.id.lv_students);

        // init
        Intent intent = getIntent();
        myStudents = new ArrayList<>();
        students = new ArrayList<>();
        currentSection = new Section();
        // value from intent
        currentSection.setName(intent.getStringExtra("section"));
        currentSection.setId(intent.getIntExtra("section_id", 0));
        currentSection.setSubject_id(intent.getIntExtra("subject_id", 0));

        //set page title
        tv_student_title.setText("SECTION: " + currentSection.getName());

        // helpers
        session = new Session(this);
        mysqlite = new SQLiteHelper(this);

        // list value
        myStudents =  mysqlite.getStudents(session.getId(), currentSection.getId());
        for( Student student: myStudents){
            students.add(student.getFirstname() + " " + student.getMiddlename() + " " + student.getLastname());
        }
        // adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.item_text,
                students);
        lv_students.setAdapter(arrayAdapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, AddStudentActivity.class);
                intent.putExtra("section_id",currentSection.getId());
                intent.putExtra("subject_id",currentSection.getSubject_id());
                startActivity(intent);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "click");
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "click");
            }
        });
    }

}
