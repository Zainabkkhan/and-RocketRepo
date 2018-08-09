package com.dan.lnhospital;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dan.lnhospital.AppVar.AppVariable;
import com.dan.lnhospital.bean.RoomDetial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;


class LoginAsync extends AsyncTask<String, Void, String> {

    String url1;
    String stat;
    ProgressDialog mProgressDialog;
    Context main;
    String Username, Roomnumber;
    String response;
    int flag;

    public LoginAsync(String url, String stat, Context c) {

        this.url1 = url;
        this.stat = stat;
        main = c;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        if (stat.equals(AppVariable.LOGINFLAG)) {
            mProgressDialog = new ProgressDialog((MainActivity) main);
            // Set progressdialog title
            mProgressDialog.setTitle("LOGIN");
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
    protected String doInBackground(String... urls)
    {
        Log.i("LNJP", "inbackground");
        response = login(url1);//http connection
        Log.i("result", response);
        return response;

    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        Log.i("result", result);
        if (stat.equals(AppVariable.LOGINFLAG))
        {
            try {
                String error = jsonValue(result);
                if (flag == 1)
                {
                    //Server disconnected
                    Toast.makeText(main, "Server disconnected", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    flag = 0;
                }
                else {
                    if (error != null)
                    {
                        //prints error
                        Toast.makeText(main, error, Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }
                    else
                    {  //login
                        Log.i("LNJP", "loginsuccess");
                        Intent service = new Intent(main, CameraService.class);
                        service.putExtra("user", Username);
                        main.startService(service);

                        mProgressDialog.dismiss();
                        Intent i = new Intent(main, PanelActivity.class);
                        Bundle data = new Bundle();
                        data.putString("response", result);

                        i.putExtras(data);
                        main.startActivity(i);
                        ((MainActivity) main).finish();

                    }
                }
            }
            catch (final IllegalArgumentException e)
            {
                // Handle or log or ignore
            }
            catch (final Exception e)
            {
                // Handle or log or ignore
                e.printStackTrace();
            }
            finally
            {
                mProgressDialog = null;
            }
        }

        if (stat.equals(AppVariable.TREATFLAG))//sign out all account
        {
            String error = jsonValue(result);
            if (flag == 1)
            {
                Toast.makeText(main, "Server disconnected", Toast.LENGTH_LONG).show();
                flag = 0;
            } else if (error.equals("0"))
            {
                Toast.makeText(main, "Please login now", Toast.LENGTH_LONG).show();

            }
        }


    }


    public String login(String path)
    {
        String s = "";
        URL url;
        HttpURLConnection connection = null;
        InputStream is = null;
        try
        {
            url = new URL(path);
            Log.i("LNJP", path);
            // TODO Auto-generated catch block

            StringBuilder content = new StringBuilder();

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);

            connection.setRequestMethod("GET");
            connection.connect();
            is = connection.getInputStream();
            BufferedReader theReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String reply1 = "";
            Log.i("reply", "code1");

            while ((reply1 = theReader.readLine()) != null) {
                content.append(reply1);
            }
            theReader.close();


            Log.i("reply", content.toString());

            s = content.toString();
        } catch (ConnectException e) {
            // TODO: handle exception
            flag = 1;
        } catch (SocketException e) {
            e.printStackTrace();
            Log.i("LNJP", "EXCEPTION");
            flag = 1;
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            // TODO: handle exception
            flag = 1;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("result", s);
        return s;
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


    private ArrayList<RoomDetial> jsonRoomDetail(String response) {

        ArrayList<RoomDetial> ar = new ArrayList<RoomDetial>();
        try {
            JSONObject js = new JSONObject(response);
            JSONArray ja = js.getJSONArray("rooms");

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                int roomId = jo.getInt("room_id");
                String room_num = jo.getString("room_no");
                String dept_name = jo.getString("depart_name");
                ar.add(new RoomDetial(roomId, room_num, dept_name));
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ar;

    }
}
