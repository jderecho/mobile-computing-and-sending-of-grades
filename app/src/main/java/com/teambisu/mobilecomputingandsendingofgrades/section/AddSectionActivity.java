package com.teambisu.mobilecomputingandsendingofgrades.section;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Section;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;
import com.teambisu.mobilecomputingandsendingofgrades.student.AddStudentActivity;

public class AddSectionActivity extends Activity {
    TextView tv_section_title;
    EditText et_section;
    Button btn_save;
    Session session;
    SQLiteHelper mysqlite;
    Intent intent;

    Spinner sp_course;
    Spinner sp_year;
    Spinner sp_section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_section);
        intent = getIntent();

        tv_section_title = (TextView) findViewById(R.id.tv_section_title);
        et_section = (EditText) findViewById(R.id.et_section);
        btn_save = (Button) findViewById(R.id.btn_save);
        sp_course = (Spinner) findViewById(R.id.spn_course);
        sp_year = (Spinner) findViewById(R.id.spn_year);
        sp_section = (Spinner) findViewById(R.id.spn_section);

        ArrayAdapter<CharSequence> courseAdapter = ArrayAdapter.createFromResource(this, R.array.courses, R.layout.item_text);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.year, R.layout.item_text);
        ArrayAdapter<CharSequence> sectionAdapter = ArrayAdapter.createFromResource(this, R.array.section, R.layout.item_text);

        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_course.setAdapter(courseAdapter);
        sp_year.setAdapter(yearAdapter);
        sp_section.setAdapter(sectionAdapter);

        session = new Session(this);
        mysqlite = new SQLiteHelper(this);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Section section = new Section();
                section.setInstructor_id(session.getId());
                section.setSubject_id(intent.getIntExtra(Section.SUBJECT_ID, 0));
                section.setName(sp_course.getSelectedItem().toString() + " " + sp_year.getSelectedItem().toString() + " " + sp_section.getSelectedItem().toString());
                if (mysqlite.insertSection(section)) {
                    Toast.makeText(AddSectionActivity.this, "Class successfully added.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AddSectionActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
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
