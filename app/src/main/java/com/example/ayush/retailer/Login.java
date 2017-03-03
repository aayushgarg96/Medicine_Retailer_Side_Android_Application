package com.example.ayush.retailer;

/**
 * Created by sankalp on 02-12-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button btn1;
    EditText et1, et2;
    TextView tv1;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Log In");
        boolean network = isNetworkAvailable();
        if(network==false){
            Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        et1 = (EditText) findViewById(R.id.editText1);
        et2 = (EditText) findViewById(R.id.editText2);
        tv1 = (TextView) findViewById(R.id.textView1);
        btn1 = (Button) findViewById(R.id.button1);

        btn1.setOnClickListener(this);
        tv1.setOnClickListener(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String Data = sp.getString("sp_status","No Data");
        if(!Data.equals("No Data")){
            Intent i = new Intent(Login.this,Homepage.class);
            startActivity(i);
            i.putExtra("name", Data);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button1: {
                username = et1.getText().toString();
                password = et2.getText().toString();
                if (username.isEmpty())
                    Toast.makeText(Login.this, "Please enter your Email or Mobile", Toast.LENGTH_SHORT).show();
                else if (password.isEmpty())
                    Toast.makeText(Login.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                else {
                    SelectFromTable select = new SelectFromTable(this);
                    select.execute(username, password);
                }
                break;

            }

            case R.id.textView1: {
                Intent i = new Intent(Login.this, ForgotPassword.class);
                startActivity(i);
                break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    public class SelectFromTable extends AsyncTask<String,Void,String>{

        Context C;

        public SelectFromTable(Context C){
            this.C = C;
        }

        @Override
        protected String doInBackground(String... params) {
            String row_count = null;
            HashMap<String,String> hm = new HashMap<String, String>();
            hm.put("username", params[0]);
            hm.put("password", params[1]);

            try {
                URL url = new URL("http://minorprojectf5.esy.es/Login_R.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream outstream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream,"UTF-8"));
                writer.write(getPostDataString(hm));
                writer.flush();
                writer.close();
                outstream.close();

                InputStream instream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "iso-8859-1"));
                StringBuilder result = new StringBuilder();
                String line = null;

                while((line = reader.readLine())!=null){
                    result.append(line+"\n");
                }
                String fresult = result.toString();
                JSONObject root = new JSONObject(fresult);
                JSONArray response = root.getJSONArray("result");
                JSONObject finaljsonobject = response.getJSONObject(0);
                row_count = finaljsonobject.getString("rowcount");
                reader.close();
                instream.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return row_count;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("1")){
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(C);
                SharedPreferences.Editor ET = sp.edit();
                ET.putString("sp_status", username);
                ET.commit();

                SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String tokenId = sp2.getString("sp_token", "NO DATA");
                //Toast.makeText(Login.this, ""+username+"\n"+tokenId, Toast.LENGTH_SHORT).show();
                RegistrationTokenUpload upload = new RegistrationTokenUpload();
                upload.execute(tokenId, username);

                Intent I_Go = new Intent(C, Homepage.class);
                C.startActivity(I_Go);
                finish();
            }
            else{
                Toast.makeText(C, "Username or Password do not match", Toast.LENGTH_SHORT).show();
            }
        }

        public String getPostDataString(HashMap<String, String> hm) throws UnsupportedEncodingException {
            StringBuilder hashString = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> point : hm.entrySet()){
                if(first)
                    first = false;
                else
                    hashString.append("&");
                hashString.append(URLEncoder.encode(point.getKey(), "UTF-8"));
                hashString.append("=");
                hashString.append(URLEncoder.encode(point.getValue(), "UTF-8"));
            }
            return hashString.toString();
        }
    }
}

