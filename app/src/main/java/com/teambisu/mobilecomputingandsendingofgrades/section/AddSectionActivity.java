package com.teambisu.mobilecomputingandsendingofgrades.section;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;

public class AddSectionActivity extends Activity {
    TextView tv_section_title;
    EditText et_section;
    Button btn_save;
    Session session;
    SQLiteHelper mysqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_section);

        tv_section_title = (TextView) findViewById(R.id.tv_section_title);
        et_section = (EditText) findViewById(R.id.et_section);
        btn_save = (Button) findViewById(R.id.btn_save);

        session = new Session(this);
        mysqlite = new SQLiteHelper(this);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

}
