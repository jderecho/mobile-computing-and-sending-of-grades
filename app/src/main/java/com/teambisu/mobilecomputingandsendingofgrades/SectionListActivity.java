package com.teambisu.mobilecomputingandsendingofgrades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Section;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;
import com.teambisu.mobilecomputingandsendingofgrades.section.AddSectionActivity;
import com.teambisu.mobilecomputingandsendingofgrades.section.EditSectionActivity;
import com.teambisu.mobilecomputingandsendingofgrades.subject.EditSubjectActivity;

import java.util.ArrayList;
import java.util.List;

public class SectionListActivity extends Activity {
    ListView lv_sections;
    TextView tv_section_title;
    FloatingActionButton btn_add;
    FloatingActionButton btn_edit;
    FloatingActionButton btn_delete;
    List<String> sections;
    ArrayList<Section> mySections;
    Session session;
    SQLiteHelper mysqlite;
    TextView warning;

    Subject currentSubject;
    Section currentSection;
    ArrayAdapter<String> arrayAdapter;
    boolean isListSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_list);
        tv_section_title = (TextView) findViewById(R.id.tv_section_title);
        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_edit = (FloatingActionButton) findViewById(R.id.btn_edit);
        btn_delete = (FloatingActionButton) findViewById(R.id.btn_delete);

        warning = (TextView) findViewById(R.id.tv_warning);
        warning.setText("There is no Section yet. Tap the plus button to add.");
        warning.setVisibility(View.GONE);
        // init
        Intent intent = getIntent();
        mySections = new ArrayList<>();
        sections = new ArrayList<>();
        currentSubject = new Subject();
        currentSection = new Section();

        // value from intent
        currentSubject.setName(intent.getStringExtra(Subject.NAME));
        currentSubject.setId(intent.getIntExtra(Subject.ID, 0));

        //set page title
        tv_section_title.setText("Sections of " + currentSubject.getName());

        lv_sections = (ListView) findViewById(R.id.lv_sections);

        // helpers
        session = new Session(this);
        mysqlite = new SQLiteHelper(this);

        // list value
        mySections = mysqlite.getSections(session.getId(), currentSubject.getId());
        for (Section section : mySections) {
            sections.add(section.getName());
        }

        // adapter
        arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.item_text,
                sections);
        lv_sections.setAdapter(arrayAdapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SectionListActivity.this, AddSectionActivity.class);
                intent.putExtra(Section.SUBJECT_ID, currentSubject.getId());
                intent.putExtra(Section.NAME, currentSubject.getName());
                startActivity(intent);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SectionListActivity.this, EditSectionActivity.class);
                intent.putExtra(Section.SUBJECT_ID, currentSection.getSubject_id());
                intent.putExtra(Section.ID, currentSection.getId());
                intent.putExtra(Section.NAME, currentSection.getName());
                startActivity(intent);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SectionListActivity.this);
                builder1.setMessage("Are you sure you want to delete " + currentSection.getName() + " ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (mysqlite.deleteSection(currentSection)) {
                                    sections.clear();
                                    mySections = mysqlite.getSections(session.getId(), currentSubject.getId());
                                    for (Section section : mySections) {
                                        sections.add(section.getName());
                                    }
                                    arrayAdapter.notifyDataSetChanged();
                                    Toast.makeText(SectionListActivity.this, "Section deleted and all of it's students.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SectionListActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
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

        sections.clear();
        mySections = mysqlite.getSections(session.getId(), currentSubject.getId());
        Log.d("test", "length: " + mySections.size());
        for (Section section : mySections) {
            sections.add(section.getName());
        }
        if (sections.isEmpty()) {
            warning.setVisibility(View.VISIBLE);
        } else {
            warning.setVisibility(View.GONE);
        }

        arrayAdapter.notifyDataSetChanged();
        lv_sections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SectionListActivity.this, StudentListActivity.class);
                intent.putExtra(Section.ID, mySections.get(position).getId());
                intent.putExtra(Section.SUBJECT_ID, mySections.get(position).getSubject_id());
                intent.putExtra(Section.NAME, sections.get(position));
                intent.putExtra("subject-" + Subject.ID, currentSubject.getId());
                intent.putExtra("subject-" + Subject.NAME, currentSubject.getName());
                startActivity(intent);
            }
        });
        lv_sections.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentSection = mySections.get(position);
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
            lv_sections.clearChoices();
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
