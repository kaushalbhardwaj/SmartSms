package com.example.khome.smartsms;


/**
 * Created by khome on 6/5/16.
 */
import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    String smsBody=null;
    Context con;
    String address=null;
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        System.out.println("in onreceive");

        con=context;
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                 smsBody = smsMessage.getMessageBody().toString();
                 address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }
            //Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

            if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                    Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                System.out.println("permi1");


                // No explanation needed, we can request the permission.
                Activity activity = (Activity) context;

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        2);



            }
            else
            {
                notifyMsg();
            }


            }



        }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    notifyMsg();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    //Toast.makeText(NewMsg.this, "No Permission Available", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void notifyMsg()
    {
        Intent i = new Intent(con.getApplicationContext(), MainPage.class);
        PendingIntent pIntent = PendingIntent.getActivity(con.getApplicationContext(), (int) System.currentTimeMillis(), i, 0);


        Notification noti = new Notification.Builder(con.getApplicationContext())
                .setContentTitle("SmartSms ")
                .setContentText("From: "+address).setSmallIcon(R.drawable.ic_local_post_office_white_24dp)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
        System.out.println("after notify");


    }
    }




