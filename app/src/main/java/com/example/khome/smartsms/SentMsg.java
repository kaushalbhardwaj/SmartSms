package com.example.khome.smartsms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SentMsg extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener{

    private Toolbar mToolbar;
    private SimpleGestureFilter detector;
    List<SMSData> smsList;
    int totalSMS=-1;
    LinkedHashMap<String,ArrayList<SMSData>> smsMap;


    ListView lv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_msg);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_sent);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sent");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        smsMap=new LinkedHashMap<String,ArrayList<SMSData>>();

        detector = new SimpleGestureFilter(this,this);


        lv1=(ListView)findViewById(R.id.listView_sent);
        if (ContextCompat.checkSelfPermission(SentMsg.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {


            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(SentMsg.this,
                    new String[]{Manifest.permission.READ_SMS},
                    2);



        }
        else
        {
            smsList=getAllSms();
            if(smsList!=null)
            {
                List<Map.Entry<String, ArrayList<SMSData>>> list = new ArrayList(smsMap.entrySet());
                MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getApplicationContext(), list);
                lv1.setAdapter(adapter);

            }
        }







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
                Intent i=new Intent(SentMsg.this,BundleMsg.class);
                i.putExtra("mylist", myList);
                i.putExtra("key",entry.getKey());
                startActivityForResult(i,4);



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
                        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getApplicationContext(), list);
                        lv1.setAdapter(adapter);

                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(SentMsg.this, "No Permission Available", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sent, menu);
        if(totalSMS!=-1)
        {

            MenuItem item = (MenuItem)  menu.findItem(R.id.numsent);
            item.setVisible(true);
            item.setTitle("" + totalSMS);
        }
        return true;
    }

    public List<SMSData> getAllSms() {
        List<SMSData> lstSms = new ArrayList<SMSData>();
        SMSData objSms = new SMSData();
        Uri message = Uri.parse("content://sms/sent");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        startManagingCursor(c);
        totalSMS = c.getCount();

        System.out.println(totalSMS+" -> total msg");

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
                smsMap.put(objSms.getNumber(), i1);
                c.moveToNext();
            }
        }

         //c.close();
        //c=null;

        return lstSms;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    public void onDoubleTap() {
        //Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }
}
