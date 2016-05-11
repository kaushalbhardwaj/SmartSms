package com.example.khome.smartsms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BundleMsg extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    private Toolbar mToolbar;
    ListView lv1;
    EditText msg;
    FloatingActionButton send;
    private SimpleGestureFilter detector;
    String address=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle_msg);
        detector = new SimpleGestureFilter(this,this);
        ArrayList<SMSDataSer> sms=(ArrayList<SMSDataSer>) getIntent().getSerializableExtra("mylist");
         address=getIntent().getExtras().getString("key");

        mToolbar = (Toolbar) findViewById(R.id.toolbar_bundle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("" + address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        msg=(EditText)findViewById(R.id.msgbundle);
        send=(FloatingActionButton)findViewById(R.id.sendbundle);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(BundleMsg.this,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {


                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(BundleMsg.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            2);


                } else {
                    sendSMS();
                }
            }
        });
        lv1=(ListView)findViewById(R.id.listView_bundle);
        if(sms!=null)
        {
            MySimpleArrayAdapterBundle adapter = new MySimpleArrayAdapterBundle(getApplicationContext(), sms);
            lv1.setAdapter(adapter);

        }

    }
    protected void sendSMS() {


        String toPhoneNumber = address;
        String smsMessage = msg.getText().toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(toPhoneNumber, null, smsMessage, null, null);


            setResult(RESULT_OK);
            Toast.makeText(BundleMsg.this, "Message Sent", Toast.LENGTH_SHORT).show();

            finish();
        } catch (Exception e) {

            System.out.println(e);
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    sendSMS();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(BundleMsg.this, "No Permission Available", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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

