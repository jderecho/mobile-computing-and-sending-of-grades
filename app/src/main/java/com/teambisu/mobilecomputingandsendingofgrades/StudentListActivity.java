package com.teambisu.mobilecomputingandsendingofgrades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentListActivity extends Activity {
    ListView lv_students;
    TextView tv_student_title;
    FloatingActionButton btn_add;
    FloatingActionButton btn_edit;
    FloatingActionButton btn_delete;
    List<String> students;
    ArrayList<Student> myStudents;
    ArrayList<Student> sortedStudents;
    Session session;
    SQLiteHelper mysqlite;
    TextView warning;

    Section currentSection;
    Subject currentSubject;
    Student currentStudent;

    ArrayAdapter<Student> arrayAdapter;
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
        sortedStudents = new ArrayList<>();
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
//        myStudents = mysqlite.getStudents(session.getId(), currentSection.getId());
//        for (Student student : myStudents) {
//            students.add(student.getFirstname() + " " + student.getMiddlename() + " " + student.getLastname());
//        }
        // adapter
        arrayAdapter = new StudentAdapter(this, 0, sortedStudents);
//        new ArrayAdapter<String>(
//                this,
//                R.layout.item_text,
//                students);
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
                intent.putExtra(Student.GENDER, currentStudent.getGender());
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
                                    refreshList();
//                                    students.clear();
//                                    myStudents = mysqlite.getStudents(session.getId(), currentSection.getId());
//                                    for (Student student : myStudents) {
//                                        students.add(student.getFirstname() + " " + student.getMiddlename() + " " + student.getLastname());
//                                    }
//                                    arrayAdapter.notifyDataSetChanged();
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

    public void refreshList() {
        ArrayList<Student> boy = new ArrayList<Student>();
        ArrayList<Student> girl = new ArrayList<Student>();

        sortedStudents.clear();
//        students.clear();
        myStudents = mysqlite.getStudents(session.getId(), currentSection.getId());
//
        for (Student student : myStudents) {
//            students.add(student.getFirstname() + " " + student.getMiddlename() + " " + student.getLastname());
            if (student.getGender() == 1) {
                boy.add(student);
            } else {
                girl.add(student);
            }
        }

        Student boys_header = new Student();
        boys_header.setId(0);
        boys_header.setGender(1);

        Student girls_header = new Student();
        girls_header.setId(-1);
        girls_header.setGender(2);

        sortedStudents.add(boys_header);
        Collections.sort(boy, new FullNameComparator());
        sortedStudents.addAll(boy);
        sortedStudents.add(girls_header);
        Collections.sort(girl, new FullNameComparator());
        sortedStudents.addAll(girl);

//
//
//        if (sortedStudents.isEmpty()) {
//            warning.setVisibility(View.VISIBLE);
//        } else {
//            warning.setVisibility(View.GONE);
//        }

        arrayAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_edit.setVisibility(View.GONE);
        btn_delete.setVisibility(View.GONE);

        refreshList();

        lv_students.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (sortedStudents.get(position).getId() != 0 && sortedStudents.get(position).getId() != -1) {
                    Intent intent = new Intent(StudentListActivity.this, GradesActivity.class);
                    intent.putExtra(Grades.STUDENT_ID, sortedStudents.get(position).getId());
                    intent.putExtra(Student.FULLNAME, sortedStudents.get(position).getFullName());
                    intent.putExtra("subject_name", currentSubject.getName());
                    startActivity(intent);
                }
            }
        });
        lv_students.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (sortedStudents.get(position).getId() != 0 && sortedStudents.get(position).getId() != -1) {
                    currentStudent = sortedStudents.get(position);
                    view.setSelected(true);
                    view.setPressed(true);
                    isListSelected = true;
                    btn_edit.setVisibility(View.VISIBLE);
                    btn_delete.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
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

    private class StudentAdapter extends ArrayAdapter<Student> {
        Context context = null;
        ArrayList<Student> students;

        public StudentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Student> objects) {
            super(context, resource, objects);
            this.context = context;
            this.students = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (students.get(position).getId() == 0) {
                // if section header
                convertView = inflater.inflate(R.layout.item_header, parent, false);
                TextView tvSectionTitle = (TextView) convertView.findViewById(R.id.tv_text);
                tvSectionTitle.setText("Male");
            } else if (students.get(position).getId() == -1) {
                convertView = inflater.inflate(R.layout.item_header, parent, false);
                TextView tvSectionTitle = (TextView) convertView.findViewById(R.id.tv_text);
                tvSectionTitle.setText("Female");
            } else {
                // if item
                convertView = inflater.inflate(R.layout.item_text, parent, false);
                TextView tvItemTitle = (TextView) convertView.findViewById(R.id.tv_text);
                tvItemTitle.setText(students.get(position).getFName());
            }

            return convertView;
        }
    }

    public class FullNameComparator implements Comparator<Student> {
        @Override
        public int compare(Student o1, Student o2) {
            return o1.getFName().compareTo(o2.getFName());
        }
    }
}
