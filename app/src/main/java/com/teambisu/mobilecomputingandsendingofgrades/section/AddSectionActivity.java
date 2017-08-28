package com.teambisu.mobilecomputingandsendingofgrades.section;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Section;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;

public class AddSectionActivity extends Activity {
    TextView tv_section_title;
    EditText et_section;
    Button btn_save;
    Session session;
    SQLiteHelper mysqlite;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_section);
        intent = getIntent();

        tv_section_title = (TextView) findViewById(R.id.tv_section_title);
        et_section = (EditText) findViewById(R.id.et_section);
        btn_save = (Button) findViewById(R.id.btn_save);

        session = new Session(this);
        mysqlite = new SQLiteHelper(this);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Section section = new Section();
                section.setInstructor_id(session.getId());
                section.setSubject_id(intent.getIntExtra(Subject.ID,0));
                section.setName(et_section.getText().toString());
                if(mysqlite.insertSection(section)){
                    Log.d("test","Save section!");
                    finish();
                }else{
                    Log.e("test","error!");
                }
            }
        });
    }

}
