package com.teambisu.mobilecomputingandsendingofgrades;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListSubjectActivity extends Activity {
    ListView lv_subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subject);
        lv_subjects = (ListView) findViewById(R.id.lv_subjects);

        final List<String> subjects = new ArrayList<String>();
        subjects.add("English 101");
        subjects.add("English 201");
        subjects.add("Physical Education 1");
        subjects.add("Physical Education 2");
        subjects.add("Physical Education 3");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.item_text,
                subjects );
        lv_subjects.setAdapter(arrayAdapter);
        lv_subjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListSubjectActivity.this, SectionListActivity.class);
                intent.putExtra("subject",subjects.get(position));
                startActivity(intent);
            }
        });
        arrayAdapter.notifyDataSetChanged();
    }

}
