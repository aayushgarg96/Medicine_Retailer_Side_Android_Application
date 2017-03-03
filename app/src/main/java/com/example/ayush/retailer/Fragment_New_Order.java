package com.example.ayush.retailer;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ayush on 12/20/2016.
 */
public class Fragment_New_Order extends Fragment {
    private ListView lv;
    ArrayList<HashMap<String, String>> orderList;
    public static ProgressDialog pDialog;
    View view;
    public static HashMap<String, String> orderSelected = new HashMap<>();
    AlertDialog.Builder dialogBuilder;
    String k;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_order, container, false);
        lv = (ListView)view.findViewById(R.id.list1);
        dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage("Do you wish to accept this order");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AcceptOrder acceptOrder = new AcceptOrder(getActivity());
                acceptOrder.execute(k);
            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RejectOrder rejectOrder = new RejectOrder(getActivity());
                rejectOrder.execute(k);
            }
        });
        new GetOrder(getActivity(), lv).execute();
        return view;
    }

    private class GetOrder extends AsyncTask<Void,Void,Void> {

        Activity mcontext;
        ListView lv1;

        public GetOrder(Activity a, ListView lv) {
            this.mcontext = a;
            this.lv1 = lv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading");
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            orderList = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(2);
            if(jsonStr!=null){
                try {
                    JSONArray orders = new JSONArray(jsonStr);
                    for (int i = 0; i < orders.length(); i++) {
                        JSONObject c = orders.getJSONObject(i);
                        String id = c.getString("id");
                        String imagePath = c.getString("imagePath");
                        String description = c.getString("description");
                        String custName = c.getString("custName");
                        String custMobile = c.getString("custMobile");
                        String address = c.getString("address");
                        String date = c.getString("date");
                        String time = c.getString("time");

                        HashMap<String, String> order = new HashMap<>();
                        order.put("id", id);
                        order.put("imagePath", imagePath);
                        order.put("description", description);
                        order.put("custName", custName);
                        order.put("custMobile", custMobile);
                        order.put("address", address);
                        order.put("date", date);
                        order.put("time", time);
                        orderList.add(order);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), orderList,
                    R.layout.list_view, new String[]{"id", "custName", "custMobile", "description",
                    "date","time","address"}, new int[]{R.id.id_order, R.id.Customer_name_order, R.id.Customer_mobile_order, R.id.description_order,  R.id.address_order, R.id.date_order, R.id.time_order});

            lv1.setAdapter(adapter);
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    orderSelected = (HashMap<String, String>) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), ShowOrder.class);
                    intent.putExtra("order", "new");
                    startActivity(intent);
                }
            });

            lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    orderSelected = (HashMap<String, String>) parent.getItemAtPosition(position);
                    k = orderSelected.get("id");
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                    return true;
                }
            });
        }
    }
}
