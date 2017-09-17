package com.teambisu.mobilecomputingandsendingofgrades;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;

public class Main2Activity extends Activity {
    Button btn_add_student;
    Button btn_listview;
    Button btn_update_profile;
    Button btn_logout;
    Session session;
    SQLiteHelper mysqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn_add_student = (Button) findViewById(R.id.btn_add_student);
        btn_listview = (Button) findViewById(R.id.btn_listview);
        btn_update_profile = (Button) findViewById(R.id.btn_update_profile);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        session = new Session(this);
        if (session.getId() == 0) {
            finish();
        }
        mysqlite = new SQLiteHelper(this);

        btn_add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "not yet..", Toast.LENGTH_SHORT).show();
            }
        });
        btn_listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, SubjectListActivity.class);
                startActivity(intent);
            }
        });
        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, UpdateInstructorActivity.class);
                startActivity(intent);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysqlite.logout();
                finish();
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
