package com.teambisu.mobilecomputingandsendingofgrades.grade;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.IntegerRes;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Score;

import java.util.Date;

public class AddScoreActivity extends Activity {

    Intent intent;
    SQLiteHelper mysqlite;
    Session session;
    EditText et_date, et_score_name, et_score, et_total;
    Button btn_save;

    int student_id = 0, grading_period = 0, grading_category = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);

        intent = getIntent();
        session = new Session(this);
        mysqlite = new SQLiteHelper(this);

        et_date = (EditText) findViewById(R.id.et_date);
        Date d = new Date();
        CharSequence s = DateFormat.format("yyyy-MM-dd", d.getTime());
        et_date.setText(s.toString());
        et_score_name = (EditText) findViewById(R.id.et_score_name);
        et_score_name.requestFocus();
        et_score = (EditText) findViewById(R.id.et_score);
        et_total = (EditText) findViewById(R.id.et_total);
        btn_save = (Button) findViewById(R.id.btn_save);

        student_id = intent.getIntExtra(Score.STUDENT_ID, 0);
        grading_period = intent.getIntExtra(Score.GRADING_PERIOD, 0);
        grading_category = intent.getIntExtra(Score.GRADING_CATEGORY, 0);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Score score = new Score();
                score.setDate(et_date.getText().toString());
                score.setTest_name(et_score_name.getText().toString());
                score.setScore(Integer.parseInt(et_score.getText().toString()));
                score.setMaximum_score(Integer.parseInt(et_total.getText().toString()));
                score.setDate(et_date.getText().toString());
                score.setGrading_category(grading_category);
                score.setGrading_period(grading_period);
                score.setStudent_id(student_id);
                Log.d("test", "score " + score.getScore());
                if (mysqlite.insertScore(score)) {
                    Toast.makeText(AddScoreActivity.this, "Successfully saved.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AddScoreActivity.this, "Error", Toast.LENGTH_LONG).show();
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
