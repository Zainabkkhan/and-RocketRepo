package com.dan.lnhospital;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dan.lnhospital.AppVar.AppSettings;
import com.dan.lnhospital.AppVar.AppVariable;
import com.dan.lnhospital.DisplayService.MyLocalBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;


public class PanelActivity extends Activity implements android.view.View.OnClickListener {

    private Context mContext = this;

    static int Listtype = 0;

    private String username;
    private String department;
    private String password;
    private String Roomnum;

    private String Exceptionflag = "false";//flag for server state check

    static String docname;
    static String Total_token = "na"; //total token number
    static String current_token = "na"; // current token number

    String total_skip_token; //total skip token in skip list

    Context mcontext = this;
    ProgressDialog mProgressDialog;


    Drawable img;
    Button call;
    Button skip;
    Button patientsdetail;
    Button logout;
    Button Skiplist;
    Button Summary;
    TextView doctor;
    TextView totaltoken;
    TextView currenttoken;
    TextView list;
    TextView roomnum, depart;
    TextView skipped;

    Vibrator vib;


    BroadcastReceiver broadcastReceiver;
    DisplayService mService;
    boolean mBound = false;

    //call back for bindservice
    public ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MyLocalBinder binder = (MyLocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        //bind Service
        Intent intent = new Intent(this, DisplayService.class);
        this.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        //register broadcast Reciever
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DisplayService.MY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_panel);


        Intent i = getIntent();
        Bundle data = i.getExtras();
        String value = data.getString("response");
        username = data.getString("username");
        vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds

        AppVariable.setType(0); //set listtype flag 0 Fresh

        AppVariable.setListtype("Fresh List"); //set List type Fresh List

        intialiseXml();// intialize layout


        if (savedInstanceState == null) {
            try {

                loginJson(value); // get data from login Json

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        //broadcast Reciever for getting data from DisplayService
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {

                String value = intent.getStringExtra("response");

                try {
                    if (AppVariable.getListtype().equals("Fresh List"))
                        serviceJsonFresh(value);
                    else if (AppVariable.getListtype().equals("Skip List"))
                        serviceJsonSkip(value);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        Log.i("LNJP", "onDistroy");
        mService.CancelAlarm();
        stopService(new Intent(getApplicationContext(), DisplayService.class));

        unbindService(mConnection);
        Log.i("ehos", "distroy called");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int Id = v.getId();
        AppSettings.buttonSound(getBaseContext());
        switch (Id) {
            case R.id.Call:
                // button onclick


                if (AppSettings.isNetworkAvailable(this)) {
                    callAction();
                } else {
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.show();
                    AppVariable.setDialog(dialog);
                }


                break;

            case R.id.Skip:


                if (AppSettings.isNetworkAvailable(this)) {
                    skipAction();
                } else {
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.show();
                    AppVariable.setDialog(dialog);
                }

                break;
            case R.id.PatientsDetail:


                if (AppSettings.isNetworkAvailable(this)) {
                    patientDetailAction();
                } else {
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.show();
                    AppVariable.setDialog(dialog);
                }

                break;


            case R.id.Logout:


                if (AppSettings.isNetworkAvailable(this)) {
                    if (AppVariable.getCallButtonStat().equals("Call"))
                        new Data("http://" + AppVariable.getIP() + ":8080/dqms/DCUKeyFunctionAndroid?did=" + AppVariable.getDocid() + "&f=4", AppVariable.LOGINFLAG).execute();
                    else
                        Toast.makeText(this, "Please treat patient first", Toast.LENGTH_SHORT).show();
                }

                //Log.i("LNJP","http://"+AppVariable.IP+"/dqms/api/android_json.php?username="+username+"&logout=1");
                else {
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.show();
                    AppVariable.setDialog(dialog);
                }

                break;

            case R.id.summary:


                if (AppSettings.isNetworkAvailable(this)) {
                    new Data("http://" + AppVariable.getIP() + ":8080/dqms/DCUKeyFunctionAndroid?did=" + AppVariable.getDocid() + "&f=2", AppVariable.SUMMARYFLAG).execute();
                } else {
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.show();
                    AppVariable.setDialog(dialog);
                }
                break;

            case R.id.Skiplist:


                if (AppSettings.isNetworkAvailable(this)) {
                    if (AppVariable.getCallButtonStat().equals("Call"))
                        skiplistAction();
                    else
                        Toast.makeText(this, "Please treat patient first", Toast.LENGTH_SHORT).show();
                } else {
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.show();
                    AppVariable.setDialog(dialog);
                }

                break;

        }
    }


    private void callAction()

    {
        if (AppVariable.getCallButtonStat().equals("Call") && !AppVariable.isCallStat()) {
            //call
            if (AppVariable.getListtype().equals("Fresh List")) {
                //send always 0
                new Data(apiDisplay(AppVariable.getDocid(), 1, 1, 1, AppVariable.getType(), 0), AppVariable.CALLFLAG).execute();
                Log.i("LNJP", apiDisplay(AppVariable.getDocid(), 1, 1, 1, AppVariable.getType(), 0));
            } else if (AppVariable.getListtype().equals("Skip List")) {
                //send first time 0 then previous token
                new Data(apiDisplay(AppVariable.getDocid(), 1, 1, 1, AppVariable.getType(), AppVariable.getCalltoken()), AppVariable.CALLFLAG).execute();
                Log.i("LNJP", apiDisplay(AppVariable.getDocid(), 1, 1, 1, AppVariable.getType(), AppVariable.getCalltoken()));
            }


            Log.i("LNJP", "inCAll");
            AppVariable.setCallStat(true);


        } else if (AppVariable.getCallButtonStat().equals("Treat")) { // treat

            Log.i("LNJP", "inTReat");
            new Data(apiDisplay(AppVariable.getDocid(), 1, 1, 3, AppVariable.getType(), Integer.parseInt(current_token)), AppVariable.TREATFLAG).execute();

        }
    }

    private void skipAction() {
        Log.i("LNJP", "inSkip");
        if (AppVariable.getCallButtonStat().equals("Treat"))
            new Data(apiDisplay(AppVariable.getDocid(), 1, 2, 2, AppVariable.getType(), Integer.parseInt(current_token)), AppVariable.SKIPFLAG).execute();
        else
            Toast.makeText(this, "Please Call patient first", Toast.LENGTH_SHORT).show();


    }

    private void skiplistAction() {
        if (AppVariable.getCallButtonStat().equals("Call")) {
            if (AppVariable.getListtype().equals("Fresh List"))//Moving to Skip List
            {

                AppVariable.setType(1);
                AppVariable.setListtype("Skip List");//Text View Stat list type
                AppVariable.setSkipListButtonStat("Fresh List"); //Button Stat list Type
                AppVariable.setCalltoken(0); //set call token value 0 for next Skip list first Call


                currenttoken.setText("Current token:   ");
                patientsdetail.setText("Cancel"); // pateintdetail button stat cancel

                skipped = (TextView) findViewById(R.id.totalSkippedToken);
                skipped.setVisibility(View.VISIBLE); // Skipped Text view visible

                img = mContext.getResources().getDrawable(R.drawable.cancel); //Image change to Cancel Button
                img.setBounds(0, 0, 60, 60);
                patientsdetail.setCompoundDrawables(img, null, null, null);


            } else { //move to fresh list
                AppVariable.setType(0);
                AppVariable.setListtype("Fresh List");
                AppVariable.setSkipListButtonStat("Skip List");
                currenttoken.setText("Last token:   ");
                patientsdetail.setText("Patients Detail");

                skipped = (TextView) findViewById(R.id.totalSkippedToken);
                skipped.setVisibility(View.INVISIBLE);

                img = mContext.getResources().getDrawable(R.drawable.patientdetail);
                img.setBounds(0, 0, 60, 60);
                patientsdetail.setCompoundDrawables(img, null, null, null);


            }
            Skiplist.setText(AppVariable.getSkipListButtonStat());
            list.setText(AppVariable.getListtype());
            new Data("http://" + AppVariable.getIP() + ":8080/dqms/ServiceDataAndroid?d=" + AppVariable.getDocid() + "&t=" + AppVariable.getType(), AppVariable.SKIPLISTFLAG).execute();
        } else {
            Toast.makeText(this, "Please Treat patient first", Toast.LENGTH_SHORT).show();
        }
    }


    private void patientDetailAction() {

        if (AppVariable.getListtype().equals("Fresh List")) {
            //PatientDetail

            new Data("http://" + AppVariable.getIP() + ":8080/dqms/DCUKeyFunctionAndroid?did=" + AppVariable.getDocid() + "&f=3", AppVariable.PATIENTDETAIL).execute();

        } else if (AppVariable.getListtype().equals("Skip List")) {
            //cancel

            if (AppVariable.getCallButtonStat().equals("Treat"))
                new Data(apiDisplay(AppVariable.getDocid(), 1, 2, 4, AppVariable.getType(), Integer.parseInt(current_token)), AppVariable.CANCELFLAG).execute();
            else
                Toast.makeText(this, "Please Call patient first", Toast.LENGTH_SHORT).show();
        }


    }

    private void intialiseXml()

    {
        call = findViewById(R.id.Call);
        skip = findViewById(R.id.Skip);
        patientsdetail = findViewById(R.id.PatientsDetail);
        logout = findViewById(R.id.Logout);
        Skiplist = findViewById(R.id.Skiplist);
        Summary = findViewById(R.id.summary);
        doctor = findViewById(R.id.nameString);
        totaltoken = findViewById(R.id.totalToken);
        currenttoken = findViewById(R.id.currentToken);
        list = findViewById(R.id.listType);

        logout.setOnClickListener(this);
        call.setOnClickListener(this);
        skip.setOnClickListener(this);
        Skiplist.setOnClickListener(this);
        Summary.setOnClickListener(this);
        patientsdetail.setOnClickListener(this);
    }


    private String login(String urlString) {
        String s = "";
        URL url;
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            url = new URL(urlString);

            // TODO Auto-generated catch block

            StringBuilder content = new StringBuilder();

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
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


            Log.i("LNJP", content.toString());

            s = content.toString();
            Toast.makeText(getApplicationContext(), s,
                    Toast.LENGTH_LONG).show();


        } catch (ConnectException e) {
            System.out.print(e);
            e.getStackTrace();
            Exceptionflag = "true";
        } catch (SocketTimeoutException e) {
            // TODO: handle exception
            e.getStackTrace();
            Exceptionflag = "true";
        } catch (Exception e) {
            System.out.print(e);
            e.getStackTrace();

        }
        return s;

    }

    private void postEx(String result, String id) throws JSONException {
        if (Exceptionflag.equals("false")) {
            if (jsonValue(result) != null && !jsonValue(result).equals("0")) {
                AppVariable.setCallStat(false);
                errorMessage(jsonValue(result));

            } else {
                if (id.equals(AppVariable.CALLFLAG)) {
                    CallJson(result);
                    panelState();

                }

                if (id.equals(AppVariable.TREATFLAG)) {
                    treatJson(result);
                    panelState();
                }

                if (id.equals(AppVariable.SKIPFLAG)) {
                    treatJson(result);
                    panelState();
                }

                if (id.equals(AppVariable.SUMMARYFLAG)) {
                    Intent i = new Intent(PanelActivity.this, DocSummaryActivity.class);
                    i.putExtra("response", result);
                    i.putExtra("docname", docname);
                    startActivity(i);
                    mProgressDialog.dismiss();
                }

                if (id.equals(AppVariable.SKIPLISTFLAG)) {
                    serviceJsonFresh(result);
                }
                if (id.equals(AppVariable.PATIENTDETAIL)) {
                    Intent i = new Intent(PanelActivity.this, PateintDetailActivity.class);
                    i.putExtra("response", result);
                    startActivity(i);
                    mProgressDialog.dismiss();
                }
                if (id.equals(AppVariable.CANCELFLAG)) {
                    treatJson(result);
                    panelState();
                }
                if (id.equals(AppVariable.LOGINFLAG)) {
                    Intent i = new Intent(PanelActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }

            }
        } else if (Exceptionflag.equals("true")) {
            Toast.makeText(this, "Server disconnected",
                    Toast.LENGTH_LONG).show();
            Exceptionflag = "false";
            AppVariable.setCallStat(false);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }

    }

    private String jsonValue(String response) {
        JSONObject js;
        String error = null;
        try {
            js = new JSONObject(response);
            error = js.getString("Error");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return error;
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;


    }

    private void loginJson(String value) throws JSONException {
        JSONObject login = new JSONObject(value);
        docname = login.getString("doctor_name");
        Total_token = login.getString("total_token");
        current_token = login.getString("current_token");
        AppVariable.setDocid(login.getInt("doctor_id"));
        doctor.setText("Welcome " + docname);

    }

    private void CallJson(String value) throws JSONException {
        JSONObject login = new JSONObject(value);
        if (AppVariable.getListtype().equals("Fresh List")) {
            Log.i("LNJP", "list.fresh");
            Total_token = login.getString("total_token");
            current_token = login.getString("token_issue");

        } else if (AppVariable.getListtype().equals("Skip List")) {
            Total_token = login.getString("total_token");

            current_token = login.getString("CurrentSkippedTokenNo");
            total_skip_token = login.getString("TotalSkippedTokenNo");
            AppVariable.setCalltoken(Integer.parseInt(current_token));
        }


        AppVariable.setCallButtonStat("Treat");
        AppVariable.setCallStat(false);
        call.setText(AppVariable.getCallButtonStat());
        Drawable img = this.getResources().getDrawable(R.drawable.treat);
        img.setBounds(0, 0, 60, 60);
        call.setCompoundDrawables(img, null, null, null);
    }

    private void treatJson(String value) throws JSONException {
        JSONObject login = new JSONObject(value);
        if (AppVariable.getListtype().equals("Fresh List")) {
            Log.i("LNJP", "list.fresh");
            Total_token = login.getString("total_token");
            current_token = login.getString("current_token");

        } else if (AppVariable.getListtype().equals("Skip List")) {
            Total_token = login.getString("total_token");
            current_token = login.getString("CurrentSkippedTokenNo");
            total_skip_token = login.getString("TotalSkippedTokenNo");

        }


        AppVariable.setCallButtonStat("Call");

        call.setText(AppVariable.getCallButtonStat());
        Drawable img = this.getResources().getDrawable(R.drawable.call);
        img.setBounds(0, 0, 60, 60);
        call.setCompoundDrawables(img, null, null, null);
    }


    private void panelState()
    {
        if (AppVariable.getCallButtonStat().equals("Treat")) {
            currenttoken.setText("Called Token: " + current_token);

        } else if (AppVariable.getListtype().equals("Fresh List")) {

            currenttoken.setText("Last Called Token: " + current_token);
        } else if (AppVariable.getListtype().equals("Skip List")) {
            currenttoken.setText("Current Token: " + current_token);
            skipped.setText("Skipped Patients: " + total_skip_token);
        }
        totaltoken.setText("Last Alloted Token: " + Total_token);
    }

    private void serviceJsonFresh(String value) throws JSONException
    {
        JSONObject js = new JSONObject(value);
        if (AppVariable.getListtype().equals("Fresh List")) {
            Total_token = js.getString("total_token");
            current_token = js.getString("current_token");

            panelState();
        }
        if (AppVariable.getListtype().equals("Skip List")) {
            Total_token = js.getString("total_token");
            current_token = js.getString("CurrentSkippedTokenNo");
            total_skip_token = js.getString("TotalSkippedTokenNo");

            panelState();
        }

    }

    private void serviceJsonSkip(String value) throws JSONException
    {
        JSONObject js = new JSONObject(value);
        if (AppVariable.getListtype().equals("Fresh List")) {
            Total_token = js.getString("total_token");
            current_token = js.getString("current_token");

            panelState();
        }
        if (AppVariable.getListtype().equals("Skip List")) {
            Total_token = js.getString("total_token");

            total_skip_token = js.getString("TotalSkippedTokenNo");

            panelState();
        }

    }


    private String apiDisplay(int did, int funtion_type, int roomid, int type, int listtype, int tokennum) {
        String url = "http://" + AppVariable.getIP() + ":8080/dqms/DCUKeyFunctionAndroid?did=" + did + "&f=" + funtion_type + "&t=" + type + "&listtype=" + listtype + "&tokenno=" + tokennum;


        return url;

    }

    private class Data extends AsyncTask<String, Void, String>
    {


        String urlString;
        String id;

        public Data(String string, String string2) {
            urlString = string;
            id = string2;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (id.equals(AppVariable.LOGINFLAG) || id.equals(AppVariable.SUMMARYFLAG) || id.equals(AppVariable.PATIENTDETAIL)) {
                mProgressDialog = new ProgressDialog(PanelActivity.this);
                // Set progressdialog title
                mProgressDialog.setTitle("Please wait");
                // Set progressdialog message
                mProgressDialog.setMessage("Loading...");
                //mProgressDialog.setProgressDrawable(getWallpaper().)
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setCanceledOnTouchOutside(false);
                // Show progressdialog
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... urls) {

            return login(urlString);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("LNJP", result);

            try {
                postEx(result, id);


            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                mProgressDialog = null;
            }


        }

    }

    private void errorMessage(String result)

    {

        switch (Integer.parseInt(result)) {
            case 1:
                Toast.makeText(this, "Room no or Type is blank", Toast.LENGTH_LONG).show();
                break;

            case 2:
                Toast.makeText(this, "This room does not have any room group", Toast.LENGTH_LONG).show();
                break;
            case 4:
                Toast.makeText(this, "All patient checked. No more patient", Toast.LENGTH_LONG).show();

                break;
            case 5:
                Toast.makeText(this, "No token for this room number", Toast.LENGTH_LONG).show();
                break;
            case 6:
                Toast.makeText(this, "All patient checked. No more patient", Toast.LENGTH_LONG).show();
                break;
            case 8:
                Toast.makeText(this, "/Patient treated does not exists", Toast.LENGTH_LONG).show();
                break;
            case 9:
                Toast.makeText(this, "Patient treated does not exists", Toast.LENGTH_LONG).show();
                break;
            case 10:
                Toast.makeText(this, "No data available for this group ID", Toast.LENGTH_LONG).show();
                break;
            case 11:
                Toast.makeText(this, "Logout failed", Toast.LENGTH_LONG).show();
                break;
            case 12:

                Toast.makeText(this, "No more token for Fresh List", Toast.LENGTH_LONG).show();

                break;

            case 7:
                Toast.makeText(this, "All skipped token passed", Toast.LENGTH_LONG).show();

                break;

            case 3:
                Toast.makeText(this, "No more token for Skip List", Toast.LENGTH_LONG).show();

                break;
        }

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

}



