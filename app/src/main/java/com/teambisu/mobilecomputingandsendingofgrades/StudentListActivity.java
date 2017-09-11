package com.teambisu.mobilecomputingandsendingofgrades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Grades;
import com.teambisu.mobilecomputingandsendingofgrades.model.Section;
import com.teambisu.mobilecomputingandsendingofgrades.model.Student;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;
import com.teambisu.mobilecomputingandsendingofgrades.student.AddStudentActivity;
import com.teambisu.mobilecomputingandsendingofgrades.student.EditStudentActivity;

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
    TextView warning;

    Section currentSection;
    Subject currentSubject;
    Student currentStudent;

    ArrayAdapter<String> arrayAdapter;
    boolean isListSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        tv_student_title = (TextView) findViewById(R.id.tv_student_title);
        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_edit = (FloatingActionButton) findViewById(R.id.btn_edit);
        btn_delete = (FloatingActionButton) findViewById(R.id.btn_delete);
        lv_students = (ListView) findViewById(R.id.lv_students);
        warning = (TextView) findViewById(R.id.tv_warning);
        warning.setText("There is no Students yet. Tap the plus button to add.");
        warning.setVisibility(View.GONE);
        // init
        Intent intent = getIntent();
        myStudents = new ArrayList<>();
        students = new ArrayList<>();
        currentSection = new Section();
        currentSubject = new Subject();

        // value from intent
        currentSection.setName(intent.getStringExtra(Section.NAME));
        currentSection.setId(intent.getIntExtra(Section.ID, 0));
        currentSection.setSubject_id(intent.getIntExtra(Section.SUBJECT_ID, 0));

        currentSubject.setId(intent.getIntExtra("subject-" + Subject.ID, 0));
        currentSubject.setName(intent.getStringExtra("subject-" + Subject.NAME));

        //set page title
        tv_student_title.setText("Students of " + currentSubject.getName() + " (" + currentSection.getName() + ")");

        // helpers
        session = new Session(this);
        mysqlite = new SQLiteHelper(this);

        // list value
        myStudents = mysqlite.getStudents(session.getId(), currentSection.getId());
        for (Student student : myStudents) {
            students.add(student.getFirstname() + " " + student.getMiddlename() + " " + student.getLastname());
        }
        // adapter
        arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.item_text,
                students);
        lv_students.setAdapter(arrayAdapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, AddStudentActivity.class);
                intent.putExtra(Student.SECTION_ID, currentSection.getId());
                intent.putExtra(Student.SUBJECT_ID, currentSection.getSubject_id());
                startActivity(intent);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "click");
                Intent intent = new Intent(StudentListActivity.this, EditStudentActivity.class);
                intent.putExtra(Student.SECTION_ID, currentStudent.getId());
                intent.putExtra(Student.SUBJECT_ID, currentStudent.getSubject_id());
                intent.putExtra(Student.ID, currentStudent.getId());
                intent.putExtra(Student.FIRSTNAME, currentStudent.getFirstname());
                intent.putExtra(Student.MIDDLENAME, currentStudent.getMiddlename());
                intent.putExtra(Student.LASTNAME, currentStudent.getLastname());
                intent.putExtra(Student.EMAILADDRESS, currentStudent.getEmailaddress());
                Log.d("test", "click" + currentStudent.getEmailaddress());
                startActivity(intent);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "click");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(StudentListActivity.this);
                builder1.setMessage("Are you sure you want to delete " + currentStudent.getFullName() + " ?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (mysqlite.deleteStudent(currentStudent)) {
                                    students.clear();
                                    myStudents = mysqlite.getStudents(session.getId(), currentSection.getId());
                                    for (Student student : myStudents) {
                                        students.add(student.getFirstname() + " " + student.getMiddlename() + " " + student.getLastname());
                                    }
                                    arrayAdapter.notifyDataSetChanged();
                                    Toast.makeText(StudentListActivity.this, "Student deleted", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(StudentListActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                                }
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_edit.setVisibility(View.GONE);
        btn_delete.setVisibility(View.GONE);

        students.clear();
        myStudents = mysqlite.getStudents(session.getId(), currentSection.getId());
        for (Student student : myStudents) {
            students.add(student.getFirstname() + " " + student.getMiddlename() + " " + student.getLastname());
        }
        if (students.isEmpty()) {
            warning.setVisibility(View.VISIBLE);
        } else {
            warning.setVisibility(View.GONE);
        }

        arrayAdapter.notifyDataSetChanged();

        lv_students.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StudentListActivity.this, GradesActivity.class);
                intent.putExtra(Grades.STUDENT_ID, myStudents.get(position).getId());
                intent.putExtra(Student.FULLNAME, myStudents.get(position).getFullName());
                intent.putExtra("subject_name", currentSubject.getName());
                startActivity(intent);
            }
        });
        lv_students.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentStudent = myStudents.get(position);
                view.setSelected(true);
                view.setPressed(true);
                isListSelected = true;
                btn_edit.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isListSelected) {
            isListSelected = false;
            lv_students.clearChoices();
            arrayAdapter.notifyDataSetChanged();

            btn_edit.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
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
