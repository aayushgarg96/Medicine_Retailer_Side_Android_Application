package com.example.ayush.retailer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {

    Button btn1;
    EditText et1;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        boolean network = isNetworkAvailable();
        if(network==false){
            Toast.makeText(ForgotPassword.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Forgot Password?");
        btn1 = (Button) findViewById(R.id.button3);
        et1 = (EditText) findViewById(R.id.editText7);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = et1.getText().toString();
                if(mail.isEmpty())
                    Toast.makeText(ForgotPassword.this, "Please enter Email or Mobile", Toast.LENGTH_SHORT).show();
                else{
                    ForgotPasswordThread forgotPasswordThread = new ForgotPasswordThread(ForgotPassword.this);
                    forgotPasswordThread.execute(mail);
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }
}
