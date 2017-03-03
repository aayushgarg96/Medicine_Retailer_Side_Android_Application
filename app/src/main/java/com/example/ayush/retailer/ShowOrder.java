package com.example.ayush.retailer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;

import java.util.HashMap;

public class ShowOrder extends AppCompatActivity {

    ImageView iv;
    HashMap<String, String> orderSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        iv = (ImageView) findViewById(R.id.imageView);
        Bundle b = getIntent().getExtras();
        String key = b.getString("order");
        if(key.equals("new")){
            orderSelected = Fragment_New_Order.orderSelected;
        }
        else if(key.equals("pro")){
            orderSelected = Fragment_Pro_Order.orderSelected;
        }
        else if(key.equals("old")){
            orderSelected = Fragment_Old_Order.orderSelected;
        }
        String imagePath = orderSelected.get("imagePath");
        byte[] byteArray = Base64.decode(imagePath, Base64.DEFAULT);
        Bitmap photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        iv.setImageBitmap(photo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
