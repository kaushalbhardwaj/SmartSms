package com.example.khome.smartsms;


import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainPage extends AppCompatActivity {

    private Toolbar mToolbar;
    CoordinatorLayout cl;
    ListView lv1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        cl=(CoordinatorLayout)findViewById(R.id.mainpagecl);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_mainpage);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Smart Sms");


        lv1=(ListView)findViewById(R.id.listView1);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitem, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.newmsg:

                Intent i=new Intent(getApplicationContext(),NewMsg.class);
                startActivityForResult(i, 1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Snackbar snackbar = Snackbar
                    .make(cl, "SMS Sent", Snackbar.LENGTH_LONG);

            snackbar.show();

        }
        if(requestCode==1 && resultCode==RESULT_CANCELED)
        {

            Snackbar snackbar = Snackbar
                    .make(cl, "Sending SMS Failed", Snackbar.LENGTH_LONG);

            snackbar.show();

        }
    }
}