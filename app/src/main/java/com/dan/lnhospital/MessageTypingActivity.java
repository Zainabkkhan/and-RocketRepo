package com.dan.lnhospital;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dan.lnhospital.AppVar.AppVariable;

import com.dan.lnhospital.bean.MessageBean;
import com.dan.lnhospital.database.DbHelper;
import com.dan.lnhospital.retroface.RetroWraper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageTypingActivity extends Activity
{
    EditText edMsg;
    Button btnSend;
    MessageBean bean;
    DbHelper dbHelper;
    List<MessageBean> arrayList;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_typing);
        context=this;
        dbHelper=new DbHelper(this);
        edMsg= (EditText) findViewById(R.id.message_edittext);
        btnSend= (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String message=edMsg.getText().toString().trim();
                bean=new MessageBean();
                bean.setMessage(message);
                bean.setDocId(""+AppVariable.getDocid());
                bean.setFlag("2");
                RetroWraper client=new RetroWraper();
                Call<String> dataSend=client.getRetroService().setMessageData(new Gson().toJson(bean));
                dataSend.enqueue(new Callback<String>()
                {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response)
                    {
                        edMsg.setText("");
                        Log.e("Message server response",""+response.body());
                        Log.e("response........",""+call.request().url());
                        if(response.body().equals("Success"))
                        {
                            arrayList=new ArrayList<>();
                            arrayList.add(bean);
                            dbHelper.insertMsgs(arrayList);
                            Toast.makeText(context, "Messsage send Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, "Message not send on Server", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t)
                    {
                        Log.e("failure...",t.toString());
                    }
                });

            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
