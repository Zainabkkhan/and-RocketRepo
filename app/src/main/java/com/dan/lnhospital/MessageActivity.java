package com.dan.lnhospital;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dan.lnhospital.AppVar.AppVariable;
import com.dan.lnhospital.adapter.MessageAdapter;
import com.dan.lnhospital.bean.MessageBean;
import com.dan.lnhospital.database.DbHelper;
import com.dan.lnhospital.retroface.RetroWraper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends Activity
{
    EditText edMsg;
    Button btnSend;
    MessageBean bean;
    DbHelper dbHelper;
    List<MessageBean> arrayList;
    RecyclerView msgRecycler;
    Context context;
    MessageAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        context=this;
        dbHelper=new DbHelper(this);

        edMsg= findViewById(R.id.message_edittext);
        btnSend= findViewById(R.id.btn_send);
        msgRecycler=findViewById(R.id.msg_recycler);
        msgRecycler.setLayoutManager(new LinearLayoutManager(context));
        //showing database
        if(dbHelper.getMsgList().size() >0)
        {
            setAdapter(dbHelper.getMsgList());
        }
        //on click on Button send
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
                        Log.e("response",""+response.body());
                        Log.e("response........",""+call.request().url());
                           if(response.body().equals("Sucess"))
                           {
                               arrayList=new ArrayList<>();
                               arrayList.add(bean);
                               dbHelper.insertMsgs(arrayList);
                               setAdapter(dbHelper.getMsgList());
                           }
                           else
                           {
                               Toast.makeText(context, "message not send", Toast.LENGTH_SHORT).show();
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

    private void setAdapter(List<MessageBean> msgList)
    {
     msgAdapter=new MessageAdapter(this, msgList);
     msgRecycler.setAdapter(msgAdapter);
    }

}
