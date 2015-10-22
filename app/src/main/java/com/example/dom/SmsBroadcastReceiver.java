package com.example.dom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;
    private String SEND, GMAIL, PASS;

    private void setting(Context context)
    {

        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSettings.contains("SEND_TO"))
        {
            SEND = mSettings.getString("SEND_TO", "");
        }

        if (mSettings.contains("GMAIL"))
        {
            GMAIL = mSettings.getString("GMAIL", "");
        }

        if (mSettings.contains("PASSWORD"))
        {
            PASS = mSettings.getString("PASSWORD", "");
        }

    }


    @Override
    public void onReceive(final Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");

        if (pdus.length == 0) {
            return;
        }
        final Sms sms = SmsFromPdus(pdus, context);

        if(sms.Text.contains("@"+mSettings.getInt("DOMPASS", 0)))
        {
            mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            sms.Text = sms.Text.substring(6,sms.Text.length());
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("SEND_TO", sms.Text);
            editor.apply();
            sms.Text = "Settings changed "+sms.Text;
        }

        Log.i("My", sms.RecieveDate + " " + sms.SenderId + "\n" + sms.Text);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                setting(context);
                GMailSender sender = new GMailSender(GMAIL, PASS);
                try {
                    sender.sendMail(sms.SenderId,
                            sms.Text+"\n"+sms.RecieveDate.toString(),
                            sms.SenderId,
                            SEND);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }

            }
        });

        thread.start();

    }

    private Sms SmsFromPdus(Object[] pdus, Context context) {

        Sms sms = new Sms();

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            sms.Text += smsMessage.getMessageBody();
        }

        SmsMessage first = SmsMessage.createFromPdu((byte[]) pdus[0]);
        sms.SenderId = first.getOriginatingAddress();
        Date receiveDate = new Date(first.getTimestampMillis());
        sms.RecieveDate = receiveDate;

        return sms;
    }


    public class Sms {
        public String SenderId;
        public String Text = "";
        public Date RecieveDate;
    }
}

