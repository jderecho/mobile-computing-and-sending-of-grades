package com.teambisu.mobilecomputingandsendingofgrades;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.teambisu.mobilecomputingandsendingofgrades.helper.SQLiteHelper;
import com.teambisu.mobilecomputingandsendingofgrades.model.Instructor;
import com.teambisu.mobilecomputingandsendingofgrades.model.Subject;

public class LoginActivity extends Activity {
    private SQLiteHelper mysqlite;
    EditText et_username;
    EditText et_password;
    Button btn_login;
    TextView tv_redirect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mysqlite = new SQLiteHelper(this);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_redirect = (TextView) findViewById(R.id.tv_redirect);

        Instructor gran = new Instructor();
        gran.setFirstname("Gran");
        gran.setMiddlename("B");
        gran.setLastname("Sabandal");
        gran.setEmailaddress("jmanuel.derecho@gmail.com");
        gran.setUsername("granix01");
        gran.setPassword("123123");
        Subject programming101 = new Subject();
        programming101.setName("Programming 101");

        gran.setSubjects(programming101);

        if(mysqlite.insertInstructor(gran)){
            Log.d("test","register succeed");
        }else{
            Log.d("test","register failed");
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ListSubjectActivity.class);
                startActivity(intent);
            }
        });
        tv_redirect.setClickable(true);
        tv_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterInstructorActivity.class);
                startActivity(intent);
            }
        });

        Log.d("test",mysqlite.getInstructors());
        if(mysqlite.login("granix01","123123")){
            Log.d("test","Login Successfully");
        }else{
            Log.d("test","Login Failed");
        }
    }

}
