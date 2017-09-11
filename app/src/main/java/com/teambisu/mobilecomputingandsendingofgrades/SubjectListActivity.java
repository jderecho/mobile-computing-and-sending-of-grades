package com.teambisu.mobilecomputingandsendingofgrades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;
import com.teambisu.mobilecomputingandsendingofgrades.subject.AddSubjectActivity;
import com.teambisu.mobilecomputingandsendingofgrades.subject.EditSubjectActivity;

import java.util.ArrayList;
import java.util.List;

public class SubjectListActivity extends Activity {
    ListView lv_subjects;
    FloatingActionButton btn_add;
    FloatingActionButton btn_edit;
    FloatingActionButton btn_delete;
    RelativeLayout container_rl;
    ArrayAdapter<String> arrayAdapter;
    List<String> subjects;
    ArrayList<Subject> mySubjects;
    Session session;
    SQLiteHelper mysqlite;
    TextView warning;

    Subject currentSubject;
    boolean isListSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subject);
        lv_subjects = (ListView) findViewById(R.id.lv_subjects);
        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_edit = (FloatingActionButton) findViewById(R.id.btn_edit);
        btn_delete = (FloatingActionButton) findViewById(R.id.btn_delete);
        container_rl = (RelativeLayout) findViewById(R.id.container_rl);
        warning = (TextView) findViewById(R.id.tv_warning);
        warning.setText("There is no Subject yet. Tap the plus button to add.");
        warning.setVisibility(View.GONE);

        session = new Session(this);
        mysqlite = new SQLiteHelper(this);
        subjects = new ArrayList<String>();
        mySubjects = new ArrayList<>();
        currentSubject = new Subject();

        mySubjects = mysqlite.getSubjects(session.getId());

        for (Subject sub : mySubjects) {
            subjects.add(sub.getName());
        }

        arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.item_text,
                subjects);
        lv_subjects.setAdapter(arrayAdapter);
        lv_subjects.setItemsCanFocus(true);
        lv_subjects.setSelected(true);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubjectListActivity.this, AddSubjectActivity.class);
                startActivity(intent);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "" + currentSubject.getId());
                Intent intent = new Intent(SubjectListActivity.this, EditSubjectActivity.class);
                intent.putExtra(Subject.ID, currentSubject.getId());
                intent.putExtra(Subject.NAME, currentSubject.getName());
                startActivity(intent);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "" + currentSubject.getId());
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SubjectListActivity.this);
                builder1.setMessage("Are you sure you want to delete " + currentSubject.getName() + " ?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (mysqlite.deleteSubject(currentSubject)) {
                                    subjects.clear();

                                    mySubjects = mysqlite.getSubjects(session.getId());
                                    for (Subject sub : mySubjects) {
                                        subjects.add(sub.getName());
                                    }
                                    arrayAdapter.notifyDataSetChanged();
                                    Toast.makeText(SubjectListActivity.this, "Subject deleted and all of it's sections and students.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SubjectListActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
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

        subjects.clear();

        mySubjects = mysqlite.getSubjects(session.getId());
        for (Subject sub : mySubjects) {
            subjects.add(sub.getName());
        }
        if (subjects.isEmpty()) {
            warning.setVisibility(View.VISIBLE);
        } else {
            warning.setVisibility(View.GONE);
        }

        lv_subjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubjectListActivity.this, SectionListActivity.class);
                intent.putExtra(Subject.NAME, subjects.get(position));
                intent.putExtra(Subject.ID, mySubjects.get(position).getId());
                Log.d("test", "position " + position);
                Log.d("test", "id " + id);
                startActivity(intent);
            }
        });
        lv_subjects.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentSubject = mySubjects.get(position);
                view.setSelected(true);
                view.setPressed(true);
                isListSelected = true;
                btn_edit.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                return true;
            }
        });

        arrayAdapter.notifyDataSetChanged();
        if (!mySubjects.isEmpty()) {
            Snackbar.make(container_rl, " Long press on a subject to edit or delete.", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (isListSelected) {
            isListSelected = false;
            lv_subjects.clearChoices();
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
