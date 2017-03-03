package com.example.ayush.retailer;

import android.content.Context;
import android.os.AsyncTask;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Ayush on 12/22/2016.
 */
public class AcceptOrder extends AsyncTask<String, Void, String> {

    Context C;
    String fresult;

    public AcceptOrder(Context C) {
        this.C = C;
    }

    @Override
    protected String doInBackground(String... params) {
        String k = params[0];
        try {
            URL url = new URL("http://minorprojectf5.esy.es/acceptOrder.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            StringBuilder hashString = new StringBuilder();
            hashString.append(URLEncoder.encode("k", "UTF-8"));
            hashString.append("=");
            hashString.append(URLEncoder.encode(k, "UTF-8"));
            OutputStream outstream = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream,"UTF-8"));
            writer.write(hashString.toString());
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
            fresult = result.toString();
            reader.close();
            JSONObject root = new JSONObject(fresult);
            JSONArray response = root.getJSONArray("result");
            JSONObject finaljsonobject = response.getJSONObject(0);
            fresult = finaljsonobject.getString("output");
            reader.close();
            instream.close();
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fresult;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("YES")){
            Toast.makeText(C, "Order Accepted.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(C, "Try Again.", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(s);
    }
}
