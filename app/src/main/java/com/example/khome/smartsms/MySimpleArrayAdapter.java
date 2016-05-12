package com.example.khome.smartsms;

/**
 * Created by khome on 3/5/16.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
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
import java.util.Map;
import java.util.Random;

public class MySimpleArrayAdapter extends ArrayAdapter {
    private final Context context;

    List<Map.Entry<String, ArrayList<SMSData>>> li;
    public String color[]={"#009688","#FF9800","#8BC34A","#9C27B0","#2196F3","#8D6E63","#FF5722","#607D8B","#E91E63"};

    private static class ViewHolder {
        TextView msg;
        TextView phonetext;
        TextView date1;
        TextView num;
        ImageView account;
    }
    public MySimpleArrayAdapter(Context context, List<Map.Entry<String, ArrayList<SMSData>>> li1) {
        super(context, R.layout.rowlayout_week, li1);
        this.context = context;
        this.li = li1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Map.Entry<String, ArrayList<SMSData>> entry = (Map.Entry<String, ArrayList<SMSData>>) this.getItem(position);

        ArrayList<SMSData> sms=entry.getValue();
        String num1=sms.size()+"";
        String phonenum=entry.getKey();
        SMSData s2=sms.get(0);
        String smsBody=s2.getBody();
        String s3;
        try{

            s3= smsBody.substring(0,32);
        }
        catch (Exception e)
        {
            s3=smsBody;

        }
        Calendar c=MillisToDate.milliToDate(s2.getDate());
        String dat=(c.get(Calendar.DAY_OF_MONTH)+":"+c.get(Calendar.MONTH)+":"+c.get(Calendar.YEAR));


        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rowlayout_week, parent, false);
            viewHolder.msg = (TextView) convertView.findViewById(R.id.msglabel);
            viewHolder.phonetext = (TextView) convertView.findViewById(R.id.phonenum);
            viewHolder.date1 = (TextView) convertView.findViewById(R.id.date);
            viewHolder.num = (TextView) convertView.findViewById(R.id.num);
            viewHolder.account=(ImageView)convertView.findViewById(R.id.account);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.msg.setText(s3+"...");
        viewHolder.num.setText(num1);
        viewHolder.date1.setText(dat);
        viewHolder.phonetext.setText(phonenum);

        Drawable tempDrawable = context.getResources().getDrawable(R.drawable.circle);
        LayerDrawable bubble = (LayerDrawable) tempDrawable;
        int c1=position%9;
        GradientDrawable solidColor = (GradientDrawable) bubble.findDrawableByLayerId(R.id.shapeitem);
        solidColor.setColor(Color.parseColor(color[c1]));
        viewHolder.account.setImageDrawable(tempDrawable);


        /*View rowView = inflater.inflate(R.layout.rowlayout_week, parent, false);
        Random ran=new Random();
        int i=ran.nextInt(10);
        TextView msg = (TextView) rowView.findViewById(R.id.msglabel);
        TextView phonetext = (TextView) rowView.findViewById(R.id.phonenum);
        TextView date1 = (TextView) rowView.findViewById(R.id.date);
        TextView num = (TextView) rowView.findViewById(R.id.num);*/

       // SMSData n=li.get(position);



        //msg.setText(s3);
        //phonetext.setText(n.getNumber());

        return convertView;
    }
}