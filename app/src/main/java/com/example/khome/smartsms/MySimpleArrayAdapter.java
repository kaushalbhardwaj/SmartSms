package com.example.khome.smartsms;

/**
 * Created by khome on 3/5/16.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MySimpleArrayAdapter extends ArrayAdapter<SMSData> {
    private final Context context;

    List<SMSData> li;

    public MySimpleArrayAdapter(Context context, List<SMSData> li1) {
        super(context, R.layout.rowlayout_week, li1);
        this.context = context;
        this.li = li1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout_week, parent, false);
        Random ran=new Random();
        int i=ran.nextInt(10);
        TextView msg = (TextView) rowView.findViewById(R.id.msglabel);
        TextView phonetext = (TextView) rowView.findViewById(R.id.phonenum);
        TextView date1 = (TextView) rowView.findViewById(R.id.date);

        SMSData n=li.get(position);
        String m=n.getBody();
        try {
            m = m.substring(0, 50);
        }
        catch (Exception e)
        {
            m=n.getBody();

        }
            msg.setText(m);
        phonetext.setText(n.getNumber());
        Calendar c=MillisToDate.milliToDate(n.getDate());
        date1.setText(c.get(Calendar.DAY_OF_MONTH)+":"+c.get(Calendar.MONTH)+":"+c.get(Calendar.YEAR));
        return rowView;
    }
}