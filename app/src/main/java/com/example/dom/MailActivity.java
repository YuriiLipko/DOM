package com.example.dom;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MailActivity extends Activity {

    private EditText etmail, etgmail, password, dompass;
    private NumberPicker np, np1, np2, np3;
    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;
    private int p1 = 0;
    private int p2 = 0;
    private int p3 = 0;
    private int p4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        etmail = (EditText) findViewById(R.id.editText);
        etgmail = (EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText3);
        dompass = (EditText) findViewById(R.id.editText4);
        np = (NumberPicker) findViewById(R.id.numberPicker);
        np1 = (NumberPicker) findViewById(R.id.numberPicker2);
        np2 = (NumberPicker) findViewById(R.id.numberPicker3);
        np3 = (NumberPicker) findViewById(R.id.numberPicker4);

        np.setMaxValue(9);
        np.setMinValue(0);

        np1.setMaxValue(9);
        np1.setMinValue(0);

        np2.setMaxValue(9);
        np2.setMinValue(0);

        np3.setMaxValue(9);
        np3.setMinValue(0);
    }

    private void save()
    {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("SEND_TO", etmail.getText().toString());
        editor.putString("GMAIL", etgmail.getText().toString());
        editor.putString("PASSWORD", password.getText().toString());
        if(dompass.getText().toString() != "")
        {
            if(dompass.getText().length() == 4)
            {
                editor.putInt("DOMPASS", Integer.parseInt(dompass.getText().toString()));
            }
            else {
                Toast.makeText(getApplicationContext(), "Password is not correct!", Toast.LENGTH_SHORT).show();
            }
        }
        editor.apply();
        Toast.makeText(getApplicationContext(), "Settings have been saved!", Toast.LENGTH_SHORT).show();

    }


    private void error ()
    {
        Toast.makeText(getApplicationContext(), "Password Error!", Toast.LENGTH_SHORT).show();
    }

    public void save(View v)
    {
        p1 = mSettings.getInt("DOMPASS", 0)/1000;
        p2 = (mSettings.getInt("DOMPASS", 0)-p1*1000)/100;
        p3 = (mSettings.getInt("DOMPASS", 0)-(p1*1000)-(p2*100))/10;
        p4 = (mSettings.getInt("DOMPASS", 0)-(p1*1000)-(p2*100)-(p3*10))/1;

        if(np.getValue() == p1)
        {
            if(np1.getValue() == p2)
            {
                if(np2.getValue() == p3)
                {
                    if(np3.getValue() == p4)
                    {
                        save();
                    }
                    else
                    {
                       error();
                    }
                }
                else
                {
                    error();
                }
            }
            else
            {
                error();
            }
        }
        else
        {
            error();
        }
    }

}
