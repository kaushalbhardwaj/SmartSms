package com.example.khome.smartsms;

/**
 * Created by khome on 6/5/16.
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


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MySimpleArrayAdapterBundle extends ArrayAdapter<SMSDataSer> {
    private final Context context;

    ArrayList<SMSDataSer> li;

    public MySimpleArrayAdapterBundle(Context context, ArrayList<SMSDataSer> li1) {
        super(context, R.layout.rowlayout_bundle, li1);
        this.context = context;
        this.li = li1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout_bundle, parent, false);
        Random ran=new Random();
        int i=ran.nextInt(10);
        TextView msg = (TextView) rowView.findViewById(R.id.msgbundle);
        TextView date = (TextView) rowView.findViewById(R.id.datebundle);
        SMSDataSer n=li.get(position);
        msg.setText(n.getBody());
        Calendar c=MillisToDate.milliToDate(n.getDate());
        String dat=(c.get(Calendar.DAY_OF_MONTH)+":"+c.get(Calendar.MONTH)+":"+c.get(Calendar.YEAR));
        date.setText(dat);
        return rowView;
    }
}
