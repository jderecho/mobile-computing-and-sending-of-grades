package com.teambisu.mobilecomputingandsendingofgrades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Section;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;
import com.teambisu.mobilecomputingandsendingofgrades.section.AddSectionActivity;

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

    Subject currentSubject;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_list);
        tv_section_title = (TextView) findViewById(R.id.tv_section_title);
        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_edit = (FloatingActionButton) findViewById(R.id.btn_edit);
        btn_delete = (FloatingActionButton) findViewById(R.id.btn_delete);

        // init
        Intent intent = getIntent();
        mySections = new ArrayList<>();
        sections = new ArrayList<>();
        currentSubject = new Subject();

        // value from intent
        currentSubject.setName(intent.getStringExtra("subject"));
        currentSubject.setId(intent.getIntExtra("subject_id", 0));

        //set page title
        tv_section_title.setText("SUBJECT: " + currentSubject.getName());

        lv_sections = (ListView) findViewById(R.id.lv_sections);

        // helpers
        session = new Session(this);
        mysqlite = new SQLiteHelper(this);

        // list value
        mySections =  mysqlite.getSections(session.getId(), currentSubject.getId());
        for( Section section: mySections){
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
                intent.putExtra(Subject.ID, currentSubject.getId());
                intent.putExtra(Subject.NAME, currentSubject.getName());
                startActivity(intent);
//                final MailService mailService = new MailService("takuashaminua@gmail.com","manuel123123");
//
//                String[] toArr = {"jmanuel.derecho@gmail.com","jan2.str8@gmail.com"};
//                mailService.setTo(toArr);
//                mailService.setFrom("jmanuel.derecho@gmail.com");
//                mailService.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
//                mailService.setBody("Email body.");
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            if(mailService.send()) {
//                                Log.d("test", "email was sent");
//                            } else {
//                                Log.e("test", "email was not sent");
//                            }
//                        } catch (Exception e) {
//                            Log.e("test", "Could not send email", e);
//                        }
//                    }
//                }).start();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "pdf");
//                PDFHelper pdfHelper = new PDFHelper(SectionListActivity.this);
//                pdfHelper.createPDF();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        sections.clear();
        mySections =  mysqlite.getSections(session.getId(), currentSubject.getId());
        Log.d("test","length: " + mySections.size());
        for( Section section: mySections){
            sections.add(section.getName());
        }
        arrayAdapter.notifyDataSetChanged();
        lv_sections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SectionListActivity.this, StudentListActivity.class);
                intent.putExtra("section_id", mySections.get(position).getId());
                intent.putExtra("subject_id", mySections.get(position).getSubject_id());
                intent.putExtra("section", sections.get(position));
                startActivity(intent);
            }
        });
    }
}
