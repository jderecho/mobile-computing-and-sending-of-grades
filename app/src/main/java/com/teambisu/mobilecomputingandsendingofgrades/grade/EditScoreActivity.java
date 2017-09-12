package com.teambisu.mobilecomputingandsendingofgrades.grade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Score;

import java.util.Date;

public class EditScoreActivity extends Activity {
    Intent intent;
    SQLiteHelper mysqlite;
    Session session;
    EditText et_date, et_score_name, et_score, et_total;
    Button btn_save;

    Score currentScore = new Score();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_score);

        intent = getIntent();
        session = new Session(this);
        mysqlite = new SQLiteHelper(this);

        et_date = (EditText) findViewById(R.id.et_date);
        Date d = new Date();
        CharSequence s = DateFormat.format("yyyy-MM-dd", d.getTime());
        et_date.setText(s.toString());
        et_score_name = (EditText) findViewById(R.id.et_score_name);
        et_score = (EditText) findViewById(R.id.et_score);
        et_total = (EditText) findViewById(R.id.et_total);
        btn_save = (Button) findViewById(R.id.btn_save);

        et_score.requestFocus();

        currentScore.setId(intent.getIntExtra(Score.ID, 0));
        currentScore.setDate(intent.getStringExtra(Score.DATE));
        currentScore.setTest_name(intent.getStringExtra(Score.TEST_NAME));
        currentScore.setScore(intent.getIntExtra(Score.SCORE, 0));
        currentScore.setMaximum_score(intent.getIntExtra(Score.MAXIMUM_SCORE, 0));

        et_date.setText(currentScore.getDate());
        et_score.setText(currentScore.getScore() + "");
        et_total.setText(currentScore.getMaximum_score() + "");

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_score.getText().toString().isEmpty()) {
                    Toast.makeText(EditScoreActivity.this, "Please input required fields", Toast.LENGTH_LONG).show();
                    et_score.setError("Please input required field");
                    return;
                }
                if (et_total.getText().toString().isEmpty()) {
                    et_total.setError("Please input required field");
                    return;
                }
                try {
                    Integer.parseInt(et_score.getText().toString());

                } catch (Exception e) {
                    Toast.makeText(EditScoreActivity.this, "Please input required fields", Toast.LENGTH_LONG).show();

                    et_score.setError("Please input valid number");
                    return;
                }
                try {
                    Integer.parseInt(et_total.getText().toString());

                } catch (Exception e) {
                    Toast.makeText(EditScoreActivity.this, "Please input required fields", Toast.LENGTH_LONG).show();

                    et_total.setError("Please input valid number");
                    return;
                }

                Score score = new Score();
                score.setId(currentScore.getId());
                score.setDate(et_date.getText().toString());
                score.setTest_name(et_score_name.getText().toString());
                score.setScore(Integer.parseInt(et_score.getText().toString()));
                score.setMaximum_score(Integer.parseInt(et_total.getText().toString()));
                score.setDate(et_date.getText().toString());

                if (mysqlite.updateScore(score)) {
                    Toast.makeText(EditScoreActivity.this, "Successfully saved.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(EditScoreActivity.this, "Error", Toast.LENGTH_LONG).show();
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
