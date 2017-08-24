package com.teambisu.mobilecomputingandsendingofgrades;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teambisu.mobilecomputingandsendingofgrades.model.Student;

import java.util.ArrayList;
import java.util.List;

public class SectionListActivity extends Activity {
    ListView lv_sections;
    TextView tv_section_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_list);
        tv_section_title = (TextView) findViewById(R.id.tv_section_title);

        Intent intent = getIntent();

        tv_section_title.setText(intent.getStringExtra("subject").toString());

        lv_sections = (ListView) findViewById(R.id.lv_sections);
        final List<String> sections = new ArrayList<String>();
        sections.add("MWF 3:00-4:00");
        sections.add("Thur-Fri 1:00-2:00");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.item_text,
                sections );
        lv_sections.setAdapter(arrayAdapter);
        lv_sections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SectionListActivity.this, StudentListActivity.class);
                intent.putExtra("section",sections.get(position));
                startActivity(intent);
            }
        });

        arrayAdapter.notifyDataSetChanged();
    }

}
