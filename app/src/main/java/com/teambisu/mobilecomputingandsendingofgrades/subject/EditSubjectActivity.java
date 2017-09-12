package com.teambisu.mobilecomputingandsendingofgrades.subject;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;

public class EditSubjectActivity extends Activity {
    EditText et_subject;
    Button btn_save;

    private Session session;
    private SQLiteHelper mysqlite;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);

        intent = getIntent();

        et_subject = (EditText) findViewById(R.id.et_subject);
        btn_save = (Button) findViewById(R.id.btn_save);

        et_subject.setText(intent.getStringExtra(Subject.NAME));

        mysqlite = new SQLiteHelper(this);
        session = new Session(this);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Subject subject = new Subject();
                subject.setId(intent.getIntExtra(Subject.ID, 0));
                subject.setName(et_subject.getText().toString());
                subject.setInstructor_id(session.getId());

                if (et_subject.getText().toString().isEmpty()) {
                    Toast.makeText(EditSubjectActivity.this, "Subject name is required.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mysqlite.updateSubject(subject)) {
                    Toast.makeText(EditSubjectActivity.this, " saved!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditSubjectActivity.this, " error!", Toast.LENGTH_SHORT).show();
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
