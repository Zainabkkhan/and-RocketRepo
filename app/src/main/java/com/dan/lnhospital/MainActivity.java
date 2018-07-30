package com.dan.lnhospital;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dan.lnhospital.AppVar.AppSettings;
import com.dan.lnhospital.AppVar.AppVariable;
import com.dan.lnhospital.bean.RoomDetial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener
{
    CustomAdapter adapter;
    List<String> roomnum;
    EditText username;
    EditText password;
    Spinner s1;
    SharedPreferences data;    //used for username and password
    SharedPreferences getIP;  //used for ip address
    Editor edit;
    HashMap<String, Integer> rid;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_irs_h001);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //username password in shared pref
        data = getSharedPreferences("saveData", Context.MODE_PRIVATE);
        edit = data.edit();

        getIP = getSharedPreferences("saveIP", Context.MODE_PRIVATE);

        username = findViewById(R.id.editText1);
        password = findViewById(R.id.editText2);

        String Ip = getIP.getString("IP", null);
        Log.e("Ip.......",""+Ip);
        if (Ip != null) {
            AppVariable.setIP(Ip);
        }

        if (data.getString("data", null) != null)
        {
            String input = data.getString("data", null);
            String screenData[] = input.split("/");
            if (screenData.length != 0)
            {
                username.setText(screenData[0]);
                password.setText(screenData[1]);
            }
            Log.i("LNJP", input);
        }

        Button login = findViewById(R.id.button1);//login button
        Button signout = findViewById(R.id.button2);// signout button
        Button Ipconfig = findViewById(R.id.ipconfig);//ipconfig

        login.setOnClickListener(this);
        signout.setOnClickListener(this);
        Ipconfig.setOnClickListener(this);

        startService(new Intent(getApplicationContext(), WiFiCheckService.class));
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub

        if (v.getId() == R.id.button1)
        {
            //login
            AppSettings.buttonSound(getBaseContext());
            String user = username.getText().toString();
            String pass = password.getText().toString();
            AppVariable.setUsername(user);
            AppVariable.setPassword(pass);

            String str = appendString(user, pass);

            edit.putString("data", str);
            edit.commit();

            String url = "http://" + AppVariable.getIP() + ":8080/dqms/LoginApiAndroid?u=" + user.trim() + "&p=" + pass.trim();
            url.replaceAll("//s+", "%20");

            if (AppSettings.isNetworkAvailable(this))
            {

                if (user != null && pass != null)
                {
                    //login in asyncTask
                 new LoginAsync(url, AppVariable.LOGINFLAG, this).execute();
                }
                else
                {
                 Toast.makeText(this, "Please add required details", Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                //network Unavailable dialog box
                CustomDialog dialog = new CustomDialog(this);
                dialog.show();
                AppVariable.setDialog(dialog);
            }

        }
        if (v.getId() == R.id.button2)
        {
            //sign out all account
            String user = username.getText().toString();
            String pass = password.getText().toString();
            String url = "http://" + AppVariable.getIP() + ":8080/dqms/LoginApiAndroid?u=" + user.trim() + "&p=" + pass.trim() + "&l=1";
            Log.e("url....",""+url);
            new LoginAsync(url, AppVariable.TREATFLAG, this).execute();
            AppSettings.buttonSound(getBaseContext());

        }
        if (v.getId() == R.id.ipconfig)
        {
            AppSettings.buttonSound(getBaseContext());
            IpConfigDialog Ip = new IpConfigDialog(this, "Password");
            Ip.show();
            AppVariable.setDialog1(Ip);

        }
    }

    private String appendString(String user, String pass)
    {
        StringBuilder str = new StringBuilder(user);
        str.append("/").append(pass);
        return str.toString();
    }

    private ArrayList<String> roomNumber(ArrayList<RoomDetial> ar)
    {
        ArrayList<String> as = new ArrayList<String>();

        for (int i = 0; i <= ar.size(); i++)
        {
            if (i == 0)
                as.add("Select Room");
            else {
                RoomDetial room = ar.get(i - 1);
                as.add(room.getRoom_num());
            }
        }

        return as;

    }

    private HashMap<String, Integer> roomID(ArrayList<RoomDetial> ar)
    {
        HashMap<String, Integer> as = new HashMap<String, Integer>();

        for (int i = 0; i < ar.size(); i++)
        {
            as.put(ar.get(i).getRoom_num(), ar.get(i).getRoom_id());
        }

        return as;

    }

    @Override
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        //super.onBackPressed();
    }
}
