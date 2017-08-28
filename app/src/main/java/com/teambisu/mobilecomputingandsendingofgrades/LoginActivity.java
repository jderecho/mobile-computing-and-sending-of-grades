package com.teambisu.mobilecomputingandsendingofgrades;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mysqlite.login(et_username.getText().toString(), et_password.getText().toString())) {
                    Intent intent = new Intent(LoginActivity.this, ListSubjectActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this,"Login failed", Toast.LENGTH_LONG).show();
                }
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
    }
}
