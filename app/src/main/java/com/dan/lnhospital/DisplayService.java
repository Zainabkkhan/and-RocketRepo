package com.dan.lnhospital;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager.OnDismissListener;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.dan.lnhospital.AppVar.AppVariable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayService extends Service {

    private Intent alarmIntent;
    String department = "ENT", response;
    private final IBinder myBinder = new MyLocalBinder();
    final static String MY_ACTION = "LNJP";


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        new DownloadNewsService().execute();
        super.onCreate();


    }

    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub

        return myBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.i("onStartCommand", "called");

        new DownloadNewsService().execute();


        return START_REDELIVER_INTENT;
    }

    public class MyLocalBinder extends Binder {
        DisplayService getService() {
            return DisplayService.this;
        }
    }


    public void newsdownload() {
        Log.i("ImageUpload", "Setting up auto image capture alarm");
        long triggerTime = SystemClock.elapsedRealtime() +
                60 * 1000;

        alarmIntent = new Intent(getApplicationContext(), LoginReciever.class);

        CancelAlarm();
        Log.i("ImageUpload", "New alarm intent");
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, sender);

        Log.i("ServiceLOgin", "Alarm has been set");
    }

    public void CancelAlarm() {
        Log.i("login", "LoginService.CancelAlarm");

        if (alarmIntent != null) {
            Log.i("LNJP1", "LoginService.CancelAlarm");
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Log.i("ImageUpload", "Pending alarm intent was null? " + String.valueOf(sender == null));
            am.cancel(sender);
        }

    }


    public class DownloadNewsService extends AsyncTask<Void, Void, Void> implements OnDismissListener {

        Context mcontext;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            response = login();
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            Intent intent = new Intent();
            intent.setAction(MY_ACTION);
            intent.putExtra("response", response);

            Log.i("ehos", response);

            sendBroadcast(intent);
            stopService(new Intent(getApplicationContext(), DisplayService.class));

            //Log.i("Service","Stopped");
            newsdownload();


        }

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub

        }


    }

    public String login() {
        Log.i("i am", "in login");
        String s = "";
        URL url;
        HttpURLConnection connection = null;
        InputStream is = null;
        int status = AppVariable.getType();
        Log.i("status.loginservice", String.valueOf(status));
        try {
            url = new URL("http://" + AppVariable.getIP() + ":8080/dqms/ServiceDataAndroid?d=" + AppVariable.getDocid() + "&t=" + AppVariable.getType());
            Log.i("LNJP", "http://" + AppVariable.getIP() + ":8080/dqms/ServiceDataAndroid?d=" + AppVariable.getDocid() + "&t=" + AppVariable.getType());

            // TODO Auto-generated catch block

            StringBuilder content = new StringBuilder();

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(7000);
            connection.setRequestMethod("GET");
            connection.connect();
            is = connection.getInputStream();
            BufferedReader theReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String reply1 = "";
            Log.i("reply", "code");

            while ((reply1 = theReader.readLine()) != null) {
                content.append(reply1);
            }
            theReader.close();


            Log.i("reply", content.toString());

            s = content.toString();
            Toast.makeText(getApplicationContext(), s,
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            System.out.print(e);
            e.getStackTrace();
        }
        return s;
    }


}

