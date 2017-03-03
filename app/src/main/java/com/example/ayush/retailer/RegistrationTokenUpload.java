package com.example.ayush.retailer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Ayush on 12/21/2016.
 */
public class RegistrationTokenUpload extends AsyncTask<String, Void, String> {

    private static String TAG = "ABC";
    StringBuilder sb;

    @Override
    protected String doInBackground(String... params) {
        String token = params[0];
        String username = params[1];
        try {
            URL url = new URL("http://minorprojectf5.esy.es/tokenUpload_R.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            StringBuilder hashString = new StringBuilder();
            hashString.append(URLEncoder.encode("token", "UTF-8"));
            hashString.append("=");
            hashString.append(URLEncoder.encode(token, "UTF-8"));
            hashString.append("&");
            hashString.append(URLEncoder.encode("username", "UTF-8"));
            hashString.append("=");
            hashString.append(URLEncoder.encode(username, "UTF-8"));
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(hashString.toString());
            writer.flush();
            writer.close();
            out.close();

            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
            sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            reader.close();
            in.close();
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        //Log.e(TAG, sb.toString());
        return null;
    }
}
