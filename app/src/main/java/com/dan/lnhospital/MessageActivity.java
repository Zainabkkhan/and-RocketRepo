package com.dan.lnhospital;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dan.lnhospital.bean.MessageBean;
import com.dan.lnhospital.retroface.RetroWraper;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends Activity {
    EditText edMsg;
    Button btnSend;
    MessageBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        edMsg= (EditText) findViewById(R.id.message_edittext);
        btnSend=(Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String message=edMsg.getText().toString().trim();
                Log.e("message....",message);
                bean=new MessageBean();
                bean.setMess(message);
              //  Log.e("SendBean",new Gson().toJson(bean));
               // bean.setmFlag(0);
                RetroWraper client=new RetroWraper();
                Call<String> dataSend=client.getRetroService().setMesageData(message);
                dataSend.enqueue(new Callback<String>()
                {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response)
                    {
                       Log.e("Url...", "" + call.request().url());
                       Log.e("response1...", "" + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                      Log.e("failure...",t.toString());
                    }
                });

             }
        });
    }

}
