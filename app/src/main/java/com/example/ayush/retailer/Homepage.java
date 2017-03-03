package com.example.ayush.retailer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Fragment_New_Order frag = new Fragment_New_Order();
        android.app.FragmentTransaction ft = Homepage.this.getFragmentManager().beginTransaction();
        ft.replace(R.id.frame, frag);
        ft.commit();
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ss = sp.getString("sp_status", "No Data");
        SharedPreferences.Editor ET = sp.edit();
        ET.putString("sp_status", ss);
        ET.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_order) {
            getSupportActionBar().setTitle(item.getTitle());
            Fragment_New_Order nf = new Fragment_New_Order();
            android.app.FragmentTransaction ft = Homepage.this.getFragmentManager().beginTransaction();
            ft.replace(R.id.frame, nf);
            ft.commit();
            Toast.makeText(Homepage.this,item.getTitle(),Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_pro_orders) {
            getSupportActionBar().setTitle(item.getTitle());
            Fragment_Pro_Order pf = new Fragment_Pro_Order();
            android.app.FragmentTransaction ft2 = Homepage.this.getFragmentManager().beginTransaction();
            ft2.replace(R.id.frame, pf);
            ft2.commit();
            Toast.makeText(Homepage.this, item.getTitle(), Toast.LENGTH_SHORT).show();
        } else if(id == R.id.nav_old_orders){
            getSupportActionBar().setTitle(item.getTitle());
            Fragment_Old_Order of = new Fragment_Old_Order();
            android.app.FragmentTransaction ft2 = Homepage.this.getFragmentManager().beginTransaction();
            ft2.replace(R.id.frame, of);
            ft2.commit();
            Toast.makeText(Homepage.this, item.getTitle(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_update) {
            getSupportActionBar().setTitle(item.getTitle());
            Fragment_Update uf = new Fragment_Update();
            android.app.FragmentTransaction ft3 = Homepage.this.getFragmentManager().beginTransaction();
            ft3.replace(R.id.frame, uf);
            ft3.commit();
            Toast.makeText(Homepage.this, item.getTitle(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor ET = sp.edit();
            ET.putString("sp_status", "No Data");
            ET.commit();
            Intent back = new Intent(getApplicationContext(),Login.class);
            startActivity(back);
            finish();
            Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
