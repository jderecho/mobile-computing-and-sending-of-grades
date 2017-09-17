package com.teambisu.mobilecomputingandsendingofgrades.grade;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.speech.RecognizerIntent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.helper.CustomEditText;
import com.teambisu.mobilecomputingandsendingofgrades.helper.DrawableClickListener;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.helper.StringHelper;
import com.teambisu.mobilecomputingandsendingofgrades.model.Score;
import com.teambisu.mobilecomputingandsendingofgrades.model.Student;

import java.util.ArrayList;
import java.util.Date;

public class AddScoreActivity extends Activity {

    Intent intent;
    SQLiteHelper mysqlite;
    Session session;
    EditText et_date, et_score_name;
    CustomEditText et_score, et_total;
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
        et_score = (CustomEditText) findViewById(R.id.et_score);
        et_total = (CustomEditText) findViewById(R.id.et_total);
        btn_save = (Button) findViewById(R.id.btn_save);

        et_score.requestFocus();
        student_id = intent.getIntExtra(Score.STUDENT_ID, 0);
        grading_period = intent.getIntExtra(Score.GRADING_PERIOD, 0);
        grading_category = intent.getIntExtra(Score.GRADING_CATEGORY, 0);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_score.getText().toString().isEmpty()) {
                    Toast.makeText(AddScoreActivity.this, "Please input required fields", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(AddScoreActivity.this, "Please input required fields", Toast.LENGTH_LONG).show();

                    et_score.setError("Please input valid number");
                    return;
                }
                try {
                    Integer.parseInt(et_total.getText().toString());

                } catch (Exception e) {
                    Toast.makeText(AddScoreActivity.this, "Please input required fields", Toast.LENGTH_LONG).show();

                    et_total.setError("Please input valid number");
                    return;
                }
                if(Integer.parseInt(et_score.getText().toString()) > Integer.parseInt(et_total.getText().toString())){
                    Toast.makeText(AddScoreActivity.this, "Score is more than the maximum score", Toast.LENGTH_LONG).show();

                    et_score.setError("Score is more than the maximum score");
                    return;
                }
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
                    Student student = new Student();
                    student.setId(student_id);
                    student.setIsGradeReady(2);
                    if(mysqlite.updateStudentGrade(student)){
                        Log.d("test","update student grade success");
                    }else{
                        Log.d("test","update student grade success");
                    }
                    Toast.makeText(AddScoreActivity.this, "Successfully saved.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AddScoreActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
        et_score.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target){
                    case RIGHT:
                        Log.d("test","RIGHT");
                        startVoiceRecognitionActivity(SCORE_REQUEST);
                        break;
                }
            }
        });
        et_total.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target){
                    case RIGHT:
                        Log.d("test","RIGHT");
                        startVoiceRecognitionActivity(TOTALSCORE_REQUEST);
                        break;
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
    private static final int SCORE_REQUEST = 1;
    private static final int TOTALSCORE_REQUEST = 2;

    private void startVoiceRecognitionActivity(int REQUEST) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // identifying your application to the Google service
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        // hint in the dialog
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        // hint to the recognizer about what the user is going to say
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // number of results
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        // recognition language
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        startActivityForResult(intent, REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCORE_REQUEST && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            // do whatever you want with the results
            for (String match : matches){
                Log.d("test", "test " + match);
                et_score.setText(StringHelper.inNumber(match));
                break;
            }

        }else if(requestCode == TOTALSCORE_REQUEST && resultCode == RESULT_OK){
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            // do whatever you want with the results
            for (String match : matches){
                Log.d("test", "test " + match);
                et_total.setText(StringHelper.inNumber(match));
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
