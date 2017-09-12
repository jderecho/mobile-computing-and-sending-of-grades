package com.teambisu.mobilecomputingandsendingofgrades.grade;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.SubjectListActivity;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.Grades;
import com.teambisu.mobilecomputingandsendingofgrades.model.Score;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;

import java.util.ArrayList;
import java.util.Collections;

public class GradingPeriodActivity extends Activity {

    private TextView tab_title, tv_grading_title, tv_score_status;
    private ListView lv_score;
    FloatingActionButton btn_add;
    FloatingActionButton btn_edit;
    FloatingActionButton btn_delete;

    Intent intent;
    Session session;
    SQLiteHelper mysqlite;

    int student_id = 0;
    int grading_period = 0;
    int grading_category = 1;
    int total_score = 0;
    int total_maxscore = 0;
    Score currentScore;

    ArrayList<Score> scores;
    ScoreAdapter scoreAdapter;
    boolean isListSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading_period);
        lv_score = (ListView) findViewById(R.id.lv_score);
        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_edit = (FloatingActionButton) findViewById(R.id.btn_edit);
        btn_delete = (FloatingActionButton) findViewById(R.id.btn_delete);
        tab_title = (TextView) findViewById(R.id.tv_tab_title);
        tv_grading_title = (TextView) findViewById(R.id.tv_grading_title);
        tv_score_status = (TextView) findViewById(R.id.tv_score_status);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        currentScore = new Score();
        intent = getIntent();
        mysqlite = new SQLiteHelper(this);
        scores = new ArrayList<>();

        student_id = intent.getIntExtra(Score.STUDENT_ID, 0);
        grading_period = intent.getIntExtra(Score.GRADING_PERIOD, 0);
        tv_grading_title.setText(Score.getGradingPeriodString(grading_period));

        scores = mysqlite.getScore(student_id, grading_period, grading_category);


        scoreAdapter = new ScoreAdapter(this, R.layout.item_score, scores);
        lv_score.setAdapter(scoreAdapter);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GradingPeriodActivity.this, AddScoreActivity.class);
                intent.putExtra(Score.STUDENT_ID, student_id);
                intent.putExtra(Score.GRADING_PERIOD, grading_period);
                intent.putExtra(Score.GRADING_CATEGORY, grading_category);
                startActivity(intent);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GradingPeriodActivity.this, EditScoreActivity.class);
                intent.putExtra(Score.ID, currentScore.getId());
                intent.putExtra(Score.DATE, currentScore.getDate());
                intent.putExtra(Score.TEST_NAME, currentScore.getTest_name());
                intent.putExtra(Score.SCORE, currentScore.getScore());
                intent.putExtra(Score.MAXIMUM_SCORE, currentScore.getMaximum_score());
                intent.putExtra(Score.STUDENT_ID, student_id);
                intent.putExtra(Score.GRADING_PERIOD, grading_period);
                intent.putExtra(Score.GRADING_CATEGORY, grading_category);
                startActivity(intent);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(GradingPeriodActivity.this);
                builder1.setMessage("Are you sure you want to delete the score?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (mysqlite.deleteScore(currentScore)) {
                                    scores.clear();
                                    total_score = 0;
                                    total_maxscore = 0;
                                    for (Score s : mysqlite.getScore(student_id, grading_period, grading_category)) {
                                        scores.add(s);
                                        total_score += s.getScore();
                                        total_maxscore += s.getMaximum_score();
                                    }
                                    Collections.reverse(scores);
                                    scoreAdapter.notifyDataSetChanged();
                                    tv_score_status.setText(total_score + "/" + total_maxscore);

                                    Toast.makeText(GradingPeriodActivity.this, "Score deleted.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(GradingPeriodActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                                }
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        lv_score.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentScore = scores.get(position);
                view.setSelected(true);
                view.setPressed(true);
                isListSelected = true;
                btn_edit.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_projects:
                    tab_title.setText(R.string.title_projects);
                    grading_category = Score.PROJECTS;

                    scores.clear();
                    total_score = 0;
                    total_maxscore = 0;
                    for (Score s : mysqlite.getScore(student_id, grading_period, grading_category)) {
                        scores.add(s);
                        total_score += s.getScore();
                        total_maxscore += s.getMaximum_score();
                    }
                    Collections.reverse(scores);
                    scoreAdapter.notifyDataSetChanged();
                    tv_score_status.setText(total_score + "/" + total_maxscore);
                    return true;
                case R.id.navigation_major_exams:
                    tab_title.setText(R.string.title_major_exams);
                    grading_category = Score.MAJOR_EXAMS;

                    scores.clear();
                    total_score = 0;
                    total_maxscore = 0;
                    for (Score s : mysqlite.getScore(student_id, grading_period, grading_category)) {
                        scores.add(s);
                        total_score += s.getScore();
                        total_maxscore += s.getMaximum_score();
                    }
                    Collections.reverse(scores);
                    scoreAdapter.notifyDataSetChanged();
                    tv_score_status.setText(total_score + "/" + total_maxscore);
                    return true;
                case R.id.navigation_evaluation:
                    tab_title.setText(R.string.title_evaluation);
                    grading_category = Score.EVALUATION;
                    scores.clear();
                    total_score = 0;
                    total_maxscore = 0;
                    for (Score s : mysqlite.getScore(student_id, grading_period, grading_category)) {
                        scores.add(s);
                        total_score += s.getScore();
                        total_maxscore += s.getMaximum_score();
                    }
                    Collections.reverse(scores);
                    scoreAdapter.notifyDataSetChanged();
                    tv_score_status.setText(total_score + "/" + total_maxscore);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onResume() {
        super.onResume();

        scores.clear();
        total_score = 0;
        total_maxscore = 0;
        for (Score s : mysqlite.getScore(student_id, grading_period, grading_category)) {
            scores.add(s);
            total_score += s.getScore();
            total_maxscore += s.getMaximum_score();
        }
        Collections.reverse(scores);
        scoreAdapter.notifyDataSetChanged();
        tv_score_status.setText(total_score + "/" + total_maxscore);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mysqlite.close();
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        if (isListSelected) {
            isListSelected = false;
            lv_score.clearChoices();
            scoreAdapter.notifyDataSetChanged();

            btn_edit.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
