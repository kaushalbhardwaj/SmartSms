package com.example.khome.smartsms;

import java.io.Serializable;

/**
 * Created by khome on 3/5/16.
 */
public class SMSDataSer implements Serializable{
    public String number;
    public String body;
    public String date;
    public SMSDataSer(SMSData sms)
    {
        number=sms.getNumber();
        body=sms.getBody();
        date=sms.getDate();

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String toString()
    {


        return ""+getNumber()+" "+getBody()+" "+getDate();
    }
}
