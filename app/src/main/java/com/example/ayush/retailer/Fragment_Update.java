package com.example.ayush.retailer;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Ayush on 12/20/2016.
 */
public class Fragment_Update extends Fragment {
    EditText et1, et2, et3, et4;
    Button btn1;
    public static String username;
    String email, mobile, price, password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        et1 = (EditText) view.findViewById(R.id.editText3);
        et2 = (EditText) view.findViewById(R.id.editText4);
        et3 = (EditText) view.findViewById(R.id.editText5);
        et4 = (EditText) view.findViewById(R.id.editText6);
        btn1 = (Button) view.findViewById(R.id.button2);
        username = Homepage.ss;

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et1.getText().toString();
                mobile = et2.getText().toString();
                price = et3.getText().toString();
                password = et4.getText().toString();

                if(email.isEmpty())
                    Toast.makeText(getActivity(), "Please enter your Shop Name", Toast.LENGTH_SHORT).show();
                else if(mobile.isEmpty())
                    Toast.makeText(getActivity(), "Please enter your Email", Toast.LENGTH_SHORT).show();
                else if(price.isEmpty())
                    Toast.makeText(getActivity(), "Please enter your Mobile", Toast.LENGTH_SHORT).show();
                else if(password.isEmpty())
                    Toast.makeText(getActivity(), "Please enter your Password", Toast.LENGTH_SHORT).show();
                else {
                    InsertIntoRetailer insert = new InsertIntoRetailer();
                    insert.execute(username, email, mobile, price, password);
                }
            }
        });
        return view;
    }

    public class InsertIntoRetailer extends AsyncTask<String, Void, String>{

        String fresult, username, email, mobile, price, password;
        HashMap<String, String> hm = new HashMap<>();
        ProgressDialog pg;

        @Override
        protected String doInBackground(String... params) {

            username = params[0];
            email = params[1];
            mobile = params[2];
            price = params[3];
            password = params[4];
            hm.put("username", username);
            hm.put("email", email);
            hm.put("mobile", mobile);
            hm.put("price", price);
            hm.put("password", password);

            try {
                URL url = new URL("http://minorprojectf5.esy.es/updateProfile_R.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                //Exception thrown if no data available for reading in the set time.
                conn.setConnectTimeout(15000);
                //Exception thrown if no connection is made by the web server.
                conn.setRequestMethod("POST");
                //sets the request method for the url request.
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream outstream = conn.getOutputStream();
                //Opening a stream with an intention of writing data to server.
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, "UTF-8"));
                //BufferedWriter provides a memory buffer and improves the speed.
                //OutputStreamWriter encodes the character written to it to bytes. Encoded data takes less space.
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
                fresult = result.toString();
                JSONObject root = new JSONObject(fresult);
                JSONArray response = root.getJSONArray("result");
                JSONObject finaljsonobject = response.getJSONObject(0);
                fresult = finaljsonobject.getString("output");
                reader.close();
                instream.close();
                conn.disconnect();
            }catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return fresult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg = new ProgressDialog(getActivity());
            pg.setTitle("Logging In");
            pg.setMessage("Please Wait...");
            pg.setCancelable(false);
            pg.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pg.dismiss();
            if(s.equals("YES")){
                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
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
