package com.teambisu.mobilecomputingandsendingofgrades.section;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Section;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;

public class EditSectionActivity extends Activity {
    TextView tv_section_title;
    EditText et_section;
    Button btn_save;
    Session session;
    SQLiteHelper mysqlite;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_section);
        intent = getIntent();

        tv_section_title = (TextView) findViewById(R.id.tv_section_title);
        et_section = (EditText) findViewById(R.id.et_section);
        btn_save = (Button) findViewById(R.id.btn_save);


        session = new Session(this);
        mysqlite = new SQLiteHelper(this);

        et_section.setText(intent.getStringExtra(Section.NAME));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_section.getText().toString().isEmpty()) {
                    Toast.makeText(EditSectionActivity.this, "Section name is required.", Toast.LENGTH_LONG).show();
                    return;
                }
                Section section = new Section();
                section.setInstructor_id(session.getId());
                section.setId(intent.getIntExtra(Section.ID, 0));
                section.setSubject_id(intent.getIntExtra(Section.SUBJECT_ID, 0));
                section.setName(et_section.getText().toString());
                Log.d("test", "section id" + intent.getIntExtra(Section.ID, 0));
                Log.d("test", "" + intent.getIntExtra(Section.ID, 0));
                if (mysqlite.updateSection(section)) {
                    Toast.makeText(EditSectionActivity.this, "Section updated.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(EditSectionActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
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
