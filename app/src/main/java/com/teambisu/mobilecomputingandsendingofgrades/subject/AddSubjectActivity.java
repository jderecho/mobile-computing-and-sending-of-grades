package com.teambisu.mobilecomputingandsendingofgrades.subject;

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

public class AddSubjectActivity extends Activity {
    EditText et_subject;
    Button btn_save;

    private Session session;
    private SQLiteHelper mysqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        et_subject = (EditText) findViewById(R.id.et_subject);
        btn_save = (Button) findViewById(R.id.btn_save);

        mysqlite = new SQLiteHelper(this);
        session = new Session(this);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_subject.getText().toString().isEmpty()) {
                    et_subject.setError("Input required field");
                    et_subject.requestFocus();
                    return;
                }
                Subject subject = new Subject();
                subject.setName(et_subject.getText().toString());
                subject.setInstructor_id(session.getId());
                if (mysqlite.insertSubject(subject)) {
                    Toast.makeText(AddSubjectActivity.this, " saved!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddSubjectActivity.this, " error!", Toast.LENGTH_SHORT).show();
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
