package com.example.khome.smartsms;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainPage extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar mToolbar;
    FloatingActionButton newmsg;
    CoordinatorLayout cl;
    ListView lv1;
    List<SMSData> smsList;
    MySimpleArrayAdapter adapter;

    int totalSMS=-1;
    LinkedHashMap<String,ArrayList<SMSData>> smsMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        cl=(CoordinatorLayout)findViewById(R.id.mainpagecl);
        smsMap=new LinkedHashMap<String,ArrayList<SMSData>>();


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
                List<Map.Entry<String, ArrayList<SMSData>>> list = new ArrayList(smsMap.entrySet());
                adapter = new MySimpleArrayAdapter(getApplicationContext(), list);
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

                List<Map.Entry<String, ArrayList<SMSData>>> list = new ArrayList(smsMap.entrySet());


                Map.Entry<String, ArrayList<SMSData>> entry = (Map.Entry<String, ArrayList<SMSData>>) list.get(position);
                ArrayList<SMSData> sms=entry.getValue();
                ArrayList<SMSDataSer> myList = new ArrayList<SMSDataSer>();

                for(int j=0;j<sms.size();j++)
                {
                    SMSData sms1=sms.get(j);
                    SMSDataSer s=new SMSDataSer(sms1);
                    myList.add(s);
                }
                Intent i=new Intent(MainPage.this,BundleMsg.class);
                i.putExtra("mylist", myList);
                i.putExtra("key",entry.getKey());
                startActivityForResult(i,4);


                /*String num1=sms.size()+"";
                String phonenum=entry.getKey();
                SMSData s2=sms.get(0);
                String smsBody=s2.getBody();*/
                //System.out.println(num1+"  "+phonenum+" "+smsBody);

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
                        List<Map.Entry<String, ArrayList<SMSData>>> list = new ArrayList(smsMap.entrySet());
                        adapter = new MySimpleArrayAdapter(getApplicationContext(), list);
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


    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        MainPage.this.adapter.getFilter().filter(newText);

        // User changed the text
        return false;
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
                ArrayList<SMSData> i1=smsMap.get(objSms.getNumber());
                if(i1==null)
                {
                    i1=new ArrayList<SMSData>();
                    i1.add(objSms);

                }
                else
                {
                    i1.add(objSms);

                }
                smsMap.put(objSms.getNumber(),i1);
                c.moveToNext();
            }
        }

       // c.close();
        /*Set set = smsMap.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }*/

        return lstSms;
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitem, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
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
            case R.id.backup:
                ArrayList<SMSDataSer>  smsBackup=new ArrayList<SMSDataSer>();
                for(int k=0;k<smsList.size();k++)
                {
                    SMSDataSer s=new SMSDataSer(smsList.get(k));
                    smsBackup.add(s);

                }

                Intent i5=new Intent(getApplicationContext(),BackupDrive.class);
                i5.putExtra("backupdata", smsBackup);
                startActivityForResult(i5, 2);
                //startService();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void startService( ) {
        startService(new Intent(getBaseContext(), BackupService.class));
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