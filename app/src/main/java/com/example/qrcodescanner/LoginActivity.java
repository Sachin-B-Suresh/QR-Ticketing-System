package com.example.qrcodescanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    Button btnLogin;
    EditText edtUsername;
    EditText edtPassword;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin=(Button) findViewById(R.id.ButtonLogin);
        edtUsername=(EditText) findViewById(R.id.EditTextUsername);
        edtPassword=(EditText) findViewById(R.id.EditTextPassword);
        databaseHelper = new DatabaseHelper(LoginActivity.this);
        databaseHelper.insertRecord();
        databaseHelper.getAll();
        progressDialog= new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Logging In");
                progressDialog.show();

                boolean isExist = databaseHelper.checkUserExist(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim());

                if(isExist){
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);     //Admin activity to be coded
                    startActivity(intent);
                    progressDialog.dismiss();
                } else {
                    edtPassword.setText(null);
                    edtUsername.setText(null);
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login failed. Invalid username or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
