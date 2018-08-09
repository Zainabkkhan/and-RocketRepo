package com.dan.lnhospital;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    MessageBean bean;
    DbHelper dbHelper;
    List<MessageBean> arrayList;
    RecyclerView msgRecycler;
    Context context;
    MessageAdapter msgAdapter;
    ImageButton typemsgBtn;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        context=this;
        dbHelper=new DbHelper(this);
        msgRecycler=findViewById(R.id.msg_recycler);
        msgRecycler.setLayoutManager(new LinearLayoutManager(context));
        typemsgBtn= (ImageButton) findViewById(R.id.open_msg_act);
        setToolbar();

        //showing database
        if(dbHelper.getMsgList().size()>0)
        {
            setAdapter(dbHelper.getMsgList());

        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setAdapter(dbHelper.getMsgList());
    }

    private void setToolbar()
    {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Message");
        toolbar.setTitleTextColor(Color.WHITE);
        typemsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(context,MessageTypingActivity.class));
            }
        });


    }

    private void setAdapter(List<MessageBean> msgList)
    {
     msgAdapter=new MessageAdapter(this, msgList);
     msgRecycler.setAdapter(msgAdapter);
    }

}
