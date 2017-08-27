package com.teambisu.mobilecomputingandsendingofgrades;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Instructor;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;
import com.teambisu.mobilecomputingandsendingofgrades.subject.AddSubjectActivity;

import java.util.ArrayList;
import java.util.List;

public class ListSubjectActivity extends Activity {
    ListView lv_subjects;
    FloatingActionButton btn_add;
    FloatingActionButton btn_edit;
    FloatingActionButton btn_delete;
    RelativeLayout container_rl;
    ArrayAdapter<String> arrayAdapter;
    List<String> subjects;

    Session session;
    SQLiteHelper mysqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subject);
        lv_subjects = (ListView) findViewById(R.id.lv_subjects);
        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_edit = (FloatingActionButton) findViewById(R.id.btn_edit);
        btn_delete = (FloatingActionButton) findViewById(R.id.btn_delete);
        container_rl = (RelativeLayout) findViewById(R.id.container_rl);

        session = new Session(this);
        mysqlite = new SQLiteHelper(this);
        subjects = new ArrayList<String>();

        for(Subject sub : mysqlite.getSubjects(session.getId())){
            subjects.add(sub.getName());
        }

        arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.item_text,
                subjects );
        lv_subjects.setAdapter(arrayAdapter);
        lv_subjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListSubjectActivity.this, SectionListActivity.class);
                intent.putExtra("subject",subjects.get(position));
                intent.putExtra("subject_id", subjects.get(position));
                startActivity(intent);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListSubjectActivity.this, AddSubjectActivity.class);
                startActivity(intent);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(container_rl, "Edit", Snackbar.LENGTH_LONG).show();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(container_rl, "Delete" + lv_subjects.getSelectedItemPosition(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        for(Subject sub : mysqlite.getSubjects(session.getId())){
            subjects.add(sub.getName());
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
