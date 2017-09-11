package com.teambisu.mobilecomputingandsendingofgrades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.teambisu.mobilecomputingandsendingofgrades.grade.GradingPeriodActivity;
import com.teambisu.mobilecomputingandsendingofgrades.helper.MailService;
import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.helper.Session;
import com.teambisu.mobilecomputingandsendingofgrades.model.GradeCategory;
import com.teambisu.mobilecomputingandsendingofgrades.model.Grades;
import com.teambisu.mobilecomputingandsendingofgrades.model.Instructor;
import com.teambisu.mobilecomputingandsendingofgrades.model.Score;
import com.teambisu.mobilecomputingandsendingofgrades.model.Student;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class GradesActivity extends Activity {

    Button btn_compute;
    Button btn_send_email;
    TextView tv_student_name;
    TextView tv_subject;
    TextView tv_status;
    TextView tv_prelim_grade;
    TextView tv_midterm_grade;
    TextView tv_prefinal_grade;
    TextView tv_final_grade;
    TextView tv_midterm_gwa;
    TextView tv_final_gwa;
    TextView tv_gwa;
    TextView tv_warning;
    LinearLayout ll_gradecontainer;
    LinearLayout ll_computed_grades_container;
    Intent intent;

    Button btn_prelim_grade;
    Button btn_midterm_score;
    Button btn_prefinal_score;
    Button btn_final_score;

    Grades currentGrade;
    Student currentStudent;
    Instructor currentInstructor;

    int currentStudent_ID = 0;
    SQLiteHelper mysqlite;
    Session session;

    File pdfFile;

    ProgressBar pb_load_grade;

    boolean is_ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        tv_student_name = (TextView) findViewById(R.id.tv_student_name);
        tv_subject = (TextView) findViewById(R.id.tv_subject);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_prelim_grade = (TextView) findViewById(R.id.tv_prelim_grade);
        tv_midterm_grade = (TextView) findViewById(R.id.tv_midterm_grade);
        tv_prefinal_grade = (TextView) findViewById(R.id.tv_prefinal_grade);
        tv_final_grade = (TextView) findViewById(R.id.tv_final_grade);
        tv_midterm_gwa = (TextView) findViewById(R.id.tv_midterm_gwa);
        tv_final_gwa = (TextView) findViewById(R.id.tv_final_gwa);
        tv_midterm_gwa = (TextView) findViewById(R.id.tv_midterm_gwa);
        tv_gwa = (TextView) findViewById(R.id.tv_gwa);
        tv_warning = (TextView) findViewById(R.id.tv_warning);
        ll_gradecontainer = (LinearLayout) findViewById(R.id.ll_gradecontainer);
        btn_send_email = (Button) findViewById(R.id.btn_send_email);
        btn_prelim_grade = (Button) findViewById(R.id.btn_prelim_grade);
        btn_midterm_score = (Button) findViewById(R.id.btn_midterm_score);
        btn_prefinal_score = (Button) findViewById(R.id.btn_prefinal_score);
        btn_final_score = (Button) findViewById(R.id.btn_final_score);
        btn_compute = (Button) findViewById(R.id.btn_compute);
        pb_load_grade = (ProgressBar) findViewById(R.id.pb_load_grade);
        ll_computed_grades_container = (LinearLayout) findViewById(R.id.ll_computed_grades_container);

        btn_send_email.setVisibility(View.GONE);

        intent = getIntent();
        currentGrade = new Grades();

        mysqlite = new SQLiteHelper(this);
        session = new Session(this);
        currentStudent_ID = intent.getIntExtra(Grades.STUDENT_ID, 0);
        currentInstructor = mysqlite.getInstructor(session.getId());

        btn_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "student " + currentStudent_ID);
                Log.d("test", "student " + currentStudent.getEmailaddress());
                final ProgressDialog pd = new ProgressDialog(GradesActivity.this);
                pd.setMessage("Sending....");
                pd.show();

                final MailService mailService = new MailService("bisu.mcsgs@gmail.com", "bisu123123");
                Log.d("test", "" + currentStudent.getEmailaddress());
                String[] toArr = {currentStudent.getEmailaddress()};
                mailService.setTo(toArr);
                mailService.setFrom("jmanuel.derecho@gmail.com");
                mailService.setSubject("Grades Computation");
                mailService.setBody("See attached file for your grades. Thanks");
                try {
                    mailService.addAttachment(pdfFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mailService.send()) {
                                Log.d("test", "email was sent");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GradesActivity.this, "Grade was successfully sent through email!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Log.e("test", "email was not sent");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GradesActivity.this, "Sending failed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            Log.e("test", "Could not send email");
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GradesActivity.this, "Could not send email, Check your internet connection.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.hide();
                            }
                        });
                    }
                }).start();
            }
        });

        btn_prelim_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GradesActivity.this, GradingPeriodActivity.class);
                intent.putExtra(Score.GRADING_PERIOD, Score.PRELIM);
                intent.putExtra(Score.STUDENT_ID, currentStudent_ID);
                startActivity(intent);
            }
        });
        btn_midterm_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GradesActivity.this, GradingPeriodActivity.class);
                intent.putExtra(Score.GRADING_PERIOD, Score.MIDTERM);
                intent.putExtra(Score.STUDENT_ID, currentStudent_ID);
                startActivity(intent);
            }
        });
        btn_prefinal_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GradesActivity.this, GradingPeriodActivity.class);
                intent.putExtra(Score.GRADING_PERIOD, Score.PREFINAL);
                intent.putExtra(Score.STUDENT_ID, currentStudent_ID);
                startActivity(intent);
            }
        });
        btn_final_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GradesActivity.this, GradingPeriodActivity.class);
                intent.putExtra(Score.GRADING_PERIOD, Score.FINAL);
                intent.putExtra(Score.STUDENT_ID, currentStudent_ID);
                startActivity(intent);
            }
        });

        btn_compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pb_load_grade.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);

                ll_computed_grades_container.setVisibility(View.VISIBLE);


                // PRELIM GRADES
                tv_prelim_grade.setText(displayGrades(currentStudent_ID, Score.PRELIM));

                // MIDTERM GRADES
                tv_midterm_grade.setText(displayGrades(currentStudent_ID, Score.MIDTERM));

                // PREFINAL GRADES
                tv_prefinal_grade.setText(displayGrades(currentStudent_ID, Score.PREFINAL));

                // FINAL GRADES
                tv_final_grade.setText(displayGrades(currentStudent_ID, Score.FINAL));


                GradeCategory prelim_project = returnGradeCategroy(currentStudent_ID, Score.PRELIM, Score.PROJECTS);
                GradeCategory prelim_major = returnGradeCategroy(currentStudent_ID, Score.PRELIM, Score.MAJOR_EXAMS);
                GradeCategory prelim_evaluation = returnGradeCategroy(currentStudent_ID, Score.PRELIM, Score.EVALUATION);

                GradeCategory midterm_project = returnGradeCategroy(currentStudent_ID, Score.MIDTERM, Score.PROJECTS);
                GradeCategory midterm_major = returnGradeCategroy(currentStudent_ID, Score.MIDTERM, Score.MAJOR_EXAMS);
                GradeCategory midterm_evaluation = returnGradeCategroy(currentStudent_ID, Score.MIDTERM, Score.EVALUATION);

                GradeCategory prefinal_project = returnGradeCategroy(currentStudent_ID, Score.PREFINAL, Score.PROJECTS);
                GradeCategory prefinal_major = returnGradeCategroy(currentStudent_ID, Score.PREFINAL, Score.MAJOR_EXAMS);
                GradeCategory prefinal_evaluation = returnGradeCategroy(currentStudent_ID, Score.PREFINAL, Score.EVALUATION);

                GradeCategory final_project = returnGradeCategroy(currentStudent_ID, Score.FINAL, Score.PROJECTS);
                GradeCategory final_major = returnGradeCategroy(currentStudent_ID, Score.FINAL, Score.MAJOR_EXAMS);
                GradeCategory final_evaluation = returnGradeCategroy(currentStudent_ID, Score.FINAL, Score.EVALUATION);

                int prelim_grade = prelim_project.percentage + prelim_major.percentage + prelim_evaluation.percentage;
                int midterm_grade = midterm_project.percentage + midterm_major.percentage + midterm_evaluation.percentage;

                int prefinal_grade = prefinal_project.percentage + prefinal_major.percentage + prefinal_evaluation.percentage;
                int final_grade = final_project.percentage + final_major.percentage + final_evaluation.percentage;

                if ((displayGrades(currentStudent_ID, Score.PRELIM).contains("Please add grades") == false)
                        && (displayGrades(currentStudent_ID, Score.MIDTERM).contains("Please add grades") == false)) {
                    tv_midterm_gwa.setText("" + Grades.getPointEquivalent((int) Grades.GWA(prelim_grade, midterm_grade)));
                    is_ready = true;
                } else {
                    tv_midterm_gwa.setText("N/A");
                }
                if ((displayGrades(currentStudent_ID, Score.PREFINAL).contains("Please add grades") == false)
                        && (displayGrades(currentStudent_ID, Score.FINAL).contains("Please add grades") == false)) {
                    tv_final_gwa.setText("" + Grades.getPointEquivalent((int) Grades.GWA(prefinal_grade, final_grade)));
                    is_ready = true;
                } else {
                    tv_final_gwa.setText("N/A");
                }
                if ((displayGrades(currentStudent_ID, Score.PRELIM).contains("Please add grades") == false)
                        && (displayGrades(currentStudent_ID, Score.MIDTERM).contains("Please add grades") == false)
                        && (displayGrades(currentStudent_ID, Score.PREFINAL).contains("Please add grades") == false)
                        && (displayGrades(currentStudent_ID, Score.FINAL).contains("Please add grades") == false)) {
                    tv_gwa.setText(Grades.getPointEquivalent((int) Grades.GWA(prelim_grade, midterm_grade, prefinal_grade, final_grade)) + "");
                    is_ready = true;
                } else {
                    tv_gwa.setText("N/A");
                }


                if (displayGrades(currentStudent_ID, Score.PRELIM).contains("Please add grades")
                        || displayGrades(currentStudent_ID, Score.MIDTERM).contains("Please add grades")
                        || displayGrades(currentStudent_ID, Score.PREFINAL).contains("Please add grades")
                        || displayGrades(currentStudent_ID, Score.FINAL).contains("Please add grades")
                        ) {
                    tv_warning.setVisibility(View.GONE);
                    pb_load_grade.setVisibility(View.GONE);
                    v.setVisibility(View.VISIBLE);
                    Toast.makeText(GradesActivity.this, "Please Complete adding scores", Toast.LENGTH_SHORT).show();
                }

                final Student temp = new Student();
                temp.setId(currentStudent_ID);
                tv_warning.setText("Creating PDF file.");
                tv_warning.setVisibility(View.VISIBLE);
                pb_load_grade.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            createPdf(temp);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_warning.setVisibility(View.GONE);
                                    pb_load_grade.setVisibility(View.GONE);
                                    Toast.makeText(GradesActivity.this, "Successfully created the pdf file. Grade is ready to send!", Toast.LENGTH_LONG).show();

                                    if (is_ready) {
                                        btn_send_email.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO on resume
        currentGrade = mysqlite.getMyGrade(currentStudent_ID);
        currentStudent = mysqlite.getStudent(currentStudent_ID);

        Log.d("test", "student " + currentStudent_ID);
        Log.d("test", "student " + currentStudent_ID);
        Log.d("test", "student " + currentStudent.getEmailaddress());

        currentStudent = mysqlite.getStudent(currentStudent_ID);

//        currentGrade.setStudent_id(currentStudent_ID);
        tv_student_name.setText(intent.getStringExtra(Student.FULLNAME));
        tv_subject.setText(intent.getStringExtra("subject_name"));
        tv_status.setText(currentGrade.getStatus());

    }

    private void createPdf(Student student) throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents/bisu/" + student.getId() + "/");
        if (!docsFolder.exists()) {
            docsFolder.mkdirs();
            Log.i("test", "Created a new directory for PDF");
        }
        pdfFile = new File(docsFolder.getAbsolutePath(), "grades.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, output);
        document.open();
        Paragraph title = new Paragraph("Grade Report");
        Font font = new Font();
        font.setColor(0, 0, 0);
        font.setSize(20);
        font.setStyle(Font.BOLD);
        title.setFont(font);
        try {
            Drawable d = getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapData = stream.toByteArray();
            Image image = Image.getInstance(bitmapData);
            image.setAlignment(Image.MIDDLE);
            document.add(image);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Paragraph apptitle = new Paragraph("Mobile Computing and Sending of Grades");
        apptitle.setAlignment(Element.ALIGN_CENTER);
        document.add(apptitle);
        Paragraph bisu = new Paragraph("Bohol Island State University");
        bisu.setAlignment(Element.ALIGN_CENTER);
        document.add(bisu);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Student: " + intent.getStringExtra(Student.FULLNAME)));
        document.add(new Paragraph("Subject: " + intent.getStringExtra("subject_name")));
        document.add(new Paragraph("Instructor:" + mysqlite.getInstructorName(session.getId())));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setTotalWidth(PageSize.A4.getWidth());

        table.setHeaderRows(1);

        table.addCell("");
        table.addCell("Prelim");
        table.addCell("Midterm");
        table.addCell("Prefinal");
        table.addCell("Final");


        GradeCategory prelim_project = returnGradeCategroy(student.getId(), Score.PRELIM, Score.PROJECTS);
        GradeCategory prelim_major = returnGradeCategroy(student.getId(), Score.PRELIM, Score.MAJOR_EXAMS);
        GradeCategory prelim_evaluation = returnGradeCategroy(student.getId(), Score.PRELIM, Score.EVALUATION);

        GradeCategory midterm_project = returnGradeCategroy(student.getId(), Score.MIDTERM, Score.PROJECTS);
        GradeCategory midterm_major = returnGradeCategroy(student.getId(), Score.MIDTERM, Score.MAJOR_EXAMS);
        GradeCategory midterm_evaluation = returnGradeCategroy(student.getId(), Score.MIDTERM, Score.EVALUATION);

        GradeCategory prefinal_project = returnGradeCategroy(student.getId(), Score.PREFINAL, Score.PROJECTS);
        GradeCategory prefinal_major = returnGradeCategroy(student.getId(), Score.PREFINAL, Score.MAJOR_EXAMS);
        GradeCategory prefinal_evaluation = returnGradeCategroy(student.getId(), Score.PREFINAL, Score.EVALUATION);

        GradeCategory final_project = returnGradeCategroy(student.getId(), Score.FINAL, Score.PROJECTS);
        GradeCategory final_major = returnGradeCategroy(student.getId(), Score.FINAL, Score.MAJOR_EXAMS);
        GradeCategory final_evaluation = returnGradeCategroy(student.getId(), Score.FINAL, Score.EVALUATION);


        table.addCell("Projects 30%");
        table.addCell("" + prelim_project.score + "/" + prelim_project.max);
        table.addCell("" + midterm_project.score + "/" + midterm_project.max);
        table.addCell("" + prefinal_project.score + "/" + prefinal_project.max);
        table.addCell("" + final_project.score + "/" + final_project.max);

        table.addCell("Major Exam 30%");
        table.addCell("" + prelim_major.score + "/" + prelim_major.max);
        table.addCell("" + midterm_major.score + "/" + midterm_major.max);
        table.addCell("" + prefinal_major.score + "/" + prefinal_major.max);
        table.addCell("" + final_major.score + "/" + final_major.max);

        table.addCell("Evaluation 40%");
        table.addCell("" + prelim_evaluation.score + "/" + prelim_evaluation.max);
        table.addCell("" + midterm_evaluation.score + "/" + midterm_evaluation.max);
        table.addCell("" + prefinal_evaluation.score + "/" + prefinal_evaluation.max);
        table.addCell("" + final_evaluation.score + "/" + final_evaluation.max);

        int prelim_grade = prelim_project.percentage + prelim_major.percentage + prelim_evaluation.percentage;
        int midterm_grade = midterm_project.percentage + midterm_major.percentage + midterm_evaluation.percentage;
        int prefinal_grade = prefinal_project.percentage + prefinal_major.percentage + prefinal_evaluation.percentage;
        int final_grade = final_project.percentage + final_major.percentage + final_evaluation.percentage;

        table.addCell("Grade");
        table.addCell(prelim_grade == 0 ? "N/A" : "" + prelim_grade);
        table.addCell(midterm_grade == 0 ? "N/A" : "" + midterm_grade);
        table.addCell(prefinal_grade == 0 ? "N/A" : "" + prefinal_grade);
        table.addCell(final_grade == 0 ? "N/A" : "" + final_grade);


        table.addCell("Equivalent");
        table.addCell(prelim_grade == 0 ? "N/A" : "" + Grades.getPointEquivalent(prelim_grade));
        table.addCell(midterm_grade == 0 ? "N/A" : "" + Grades.getPointEquivalent(midterm_grade));
        table.addCell(prefinal_grade == 0 ? "N/A" : "" + Grades.getPointEquivalent(prefinal_grade));
        table.addCell(final_grade == 0 ? "N/A" : "" + Grades.getPointEquivalent(final_grade));

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new DottedLineSeparator());
        if (prelim_grade != 0 && midterm_grade != 0) {
            document.add(new Paragraph("Midterm GWA: " + Grades.getPointEquivalent((int) Grades.GWA(prelim_grade, midterm_grade))));
        } else {
            document.add(new Paragraph("Midterm GWA: N/A"));
        }
        if (prefinal_grade != 0 && final_grade != 0) {
            document.add(new Paragraph("Final GWA: " + Grades.getPointEquivalent((int) Grades.GWA(prefinal_grade, final_grade))));
        } else {
            document.add(new Paragraph("Final GWA: N/A"));
        }
        if (prelim_grade != 0 && midterm_grade != 0 && prefinal_grade != 0 && final_grade != 0) {
            document.add(new Paragraph("Overall GWA: " + Grades.getPointEquivalent((int) Grades.GWA(prelim_grade, midterm_grade, prefinal_grade, final_grade))));
        } else {
            document.add(new Paragraph("Overall GWA: N/A"));
        }

        document.add(new Paragraph(" "));
        document.add(new DottedLineSeparator());

        document.close();
    }

    private void previewPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        } else {
            Toast.makeText(this, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }
    }

    public String getGWA(int student_id, int[] grading_period) {
        float total_score = 0;
        float max_score = 0;
        float total_percent_grade = 0;
        float total_point_grade = 0;


        for (int grading : grading_period) {

            for (Score s : mysqlite.getScore(student_id, grading, Score.PROJECTS)) {
                total_score += s.getScore();
                max_score += s.getMaximum_score();
            }
            total_percent_grade += Score.computePercentageGrade(total_score, max_score, 0.30F);

            /**
             * MAJOR EXAMS
             * ***************/
            total_score = 0;
            max_score = 0;
            for (Score s : mysqlite.getScore(currentStudent_ID, grading, Score.MAJOR_EXAMS)) {

                total_score += s.getScore();
                max_score += s.getMaximum_score();
            }
            total_percent_grade += Score.computePercentageGrade(total_score, max_score, 0.30F);

            /**
             * EVALUATIONS
             * ***********************/
            total_score = 0;
            max_score = 0;
            for (Score s : mysqlite.getScore(currentStudent_ID, grading, Score.EVALUATION)) {
                total_score += s.getScore();
                max_score += s.getMaximum_score();
            }
            total_percent_grade += Score.computePercentageGrade(total_score, max_score, 0.40F);
            Log.d("test", total_percent_grade + "");
        }
        total_point_grade += Grades.getPointEquivalent((int) total_percent_grade);

        return (int) (total_point_grade / grading_period.length) + "";
    }


    public GradeCategory returnGradeCategroy(int student_id, int period, int category) {

        GradeCategory gradeCategory = new GradeCategory();
        int total_score = 0;
        int max_score = 0;

        for (Score s : mysqlite.getScore(student_id, period, category)) {
            total_score += s.getScore();
            max_score += s.getMaximum_score();
        }

        gradeCategory.percentage = (int) Score.computePercentageGrade(total_score, max_score, Score.getPercentageofCategory(category));
        gradeCategory.score = total_score;
        gradeCategory.max = max_score;
        gradeCategory.message = Score.getGradingCategoryString(category) + ": " + gradeCategory.score + "/" + gradeCategory.max + " = " + gradeCategory.percentage;
        if (max_score == 0) {
            gradeCategory.message = "Please add grades for " + Score.getGradingCategoryString(category);
            gradeCategory.isCompleted = true;
        }
        gradeCategory.point = (int) Grades.getPointEquivalent(gradeCategory.percentage);
        return gradeCategory;
    }

    public String displayGrades(int student_id, int period) {

        GradeCategory projectCategory = new GradeCategory();
        GradeCategory majorCategory = new GradeCategory();
        GradeCategory evaluationCategory = new GradeCategory();
        /**
         * PROJECT
         */
        projectCategory = returnGradeCategroy(student_id, period, Score.PROJECTS);

        String projects = projectCategory.message;

        /**
         * MAJOR EXAMS
         * ***************/
        majorCategory = returnGradeCategroy(student_id, period, Score.MAJOR_EXAMS);

        String major_exams = majorCategory.message;

        /**
         * EVALUATIONS
         * ***********************/
        evaluationCategory = returnGradeCategroy(student_id, period, Score.EVALUATION);

        String evaluations = evaluationCategory.message;


        int overallscore = projectCategory.percentage + majorCategory.percentage + evaluationCategory.percentage;

        return "Equivalent: " + Grades.getPointEquivalent(overallscore) + " \n" + projects + "\n" + major_exams + "\n" + evaluations + "\n" + "Percentage: " + overallscore;
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
