package com.example.khome.smartsms;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {

    private Toolbar mToolbar;
    FloatingActionButton newmsg;
    CoordinatorLayout cl;
    ListView lv1;
    List<SMSData> smsList;

    int totalSMS=-1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        cl=(CoordinatorLayout)findViewById(R.id.mainpagecl);

        newmsg=(FloatingActionButton)findViewById(R.id.newmsg);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_mainpage);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Smart Sms");


        newmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), NewMsg.class);
                startActivityForResult(i, 1);

            }
        });
        lv1=(ListView)findViewById(R.id.listView1);

        if (ContextCompat.checkSelfPermission(MainPage.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {


            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(MainPage.this,
                    new String[]{Manifest.permission.READ_SMS},
                    2);



        }
        else
        {
            smsList=getAllSms();
            if(smsList!=null)
            {
                MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getApplicationContext(), smsList);
                lv1.setAdapter(adapter);

            }
        }




        //getAllSms();
/*
        if(smsList!=null)
        {
            MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getApplicationContext(), smsList);
            lv1.setAdapter(adapter);

        }*/



        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   smsList= getAllSms();
                    if(smsList!=null)
                    {
                        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getApplicationContext(), smsList);
                        lv1.setAdapter(adapter);

                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(MainPage.this, "No Permission Available", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public List<SMSData> getAllSms() {
        List<SMSData> lstSms = new ArrayList<SMSData>();
        SMSData objSms = new SMSData();
        Uri message = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        startManagingCursor(c);
         totalSMS = c.getCount();
        System.out.println(totalSMS + " -> total msg");


        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new SMSData();
                objSms.setNumber(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setBody(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setDate(c.getString(c.getColumnIndexOrThrow("date")));

                lstSms.add(objSms);
                c.moveToNext();
            }
        }

       // c.close();

        return lstSms;
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitem, menu);
        if(totalSMS!=-1)
        {

            MenuItem item = (MenuItem)  menu.findItem(R.id.num);
            item.setVisible(true);
            item.setTitle("" + totalSMS);
        }

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.sent:

                Intent i2=new Intent(getApplicationContext(),SentMsg.class);
                startActivityForResult(i2, 2);

                return true;
            case R.id.draft:

                Intent i3=new Intent(getApplicationContext(),DraftMsg.class);
                startActivityForResult(i3, 2);

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