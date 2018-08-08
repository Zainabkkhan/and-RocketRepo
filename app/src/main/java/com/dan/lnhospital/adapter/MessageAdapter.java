package com.dan.lnhospital.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dan.lnhospital.AppVar.AppVariable;
import com.dan.lnhospital.MessageActivity;
import com.dan.lnhospital.R;
import com.dan.lnhospital.bean.MessageBean;
import com.dan.lnhospital.database.DbHelper;
import com.dan.lnhospital.retroface.RetroWraper;
import com.google.gson.Gson;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageView>
{
    private static LayoutInflater inflater;
    public List<MessageBean> msgBeanList;
    Context context;
    DbHelper dbHelper;
    RetroWraper wraper;
    MessageBean msgbean;

    public MessageAdapter(MessageActivity messageActivity, List<MessageBean> msgList)
    {
        this.context=messageActivity;
        this.msgBeanList=msgList;
        dbHelper=new DbHelper(context);
    }

    @Override
    public MessageView onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (inflater == null)
        {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new MessageView(inflater.inflate(R.layout.msg_showing_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final MessageView holder, final int position)
    {
        holder.displayCheckBox.setText(msgBeanList.get(position).getMessage());
            holder.displayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (holder.displayCheckBox.isChecked()) {
                        String checkvalue = holder.displayCheckBox.getText().toString().trim();
                        setDisDelData(checkvalue, 1, position);
                    } else {

                        String uncheckedvalue = holder.displayCheckBox.getText().toString().trim();
                        setDisDelData(uncheckedvalue, 2, position);
                        Toast.makeText(context, "UnChecked Value..." + uncheckedvalue, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        holder.imgDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String checkvalue=holder.displayCheckBox.getText().toString().trim();
                setDisDelData(checkvalue,3, position);
            }
        });

    }

    private void setDisDelData(final String checkvalue, int i, final int position)
    {
          Log.e("Position","i="+i+"CheckBoxValue="+checkvalue);
          msgbean=new MessageBean();
          if(i==1)
          {
            msgbean.setmFlag("0");//display message
          }
          if(i==2)
          {
              msgbean.setmFlag("2");//hide message
          }
          if(i==3)
          {
            msgbean.setmFlag("1");//delete message
          }
          msgbean.setMessage(checkvalue);
          msgbean.setDocId(""+AppVariable.getDocid());

          Log.e("SendBean",new Gson().toJson(msgbean));
         wraper=new RetroWraper();
        Call<String> dataDisDel=wraper.getRetroService().setDisplayDelete(new Gson().toJson(msgbean));
        dataDisDel.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                Log.e("DisDel",""+call.request().url());
                Log.e("response",""+response.body());
                try
                {
                    if (response.body().equals("DeletionSucess"))
                    {
                        dbHelper.deletemsg(position, checkvalue);
                        Log.e("return from database", "" + dbHelper.deletemsg(position, checkvalue));
                        if (dbHelper.deletemsg(position, checkvalue) ==true)
                        {
                            delete(position);
                        }
                        else{
                            Toast.makeText(context, "Message not Deleted", Toast.LENGTH_SHORT).show();
                        }

                    }
                    if(response.body().equals("DisplaySuccess"))
                    {
                        Toast.makeText(context, "Message Displayon Display Board", Toast.LENGTH_SHORT).show();
                    }
                    if(response.body().equals("MessageHide"))
                    {
                        Toast.makeText(context, "Message hide from Display Board", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                Toast.makeText(context, "Data not send on server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return msgBeanList.size();
    }

    public class MessageView extends RecyclerView.ViewHolder
    {
        CheckBox displayCheckBox;
        ImageView imgDelete;
        public MessageView(View itemView)
        {
            super(itemView);
            displayCheckBox=itemView.findViewById(R.id.msg_check_box);
            imgDelete=itemView.findViewById(R.id.image_delete_message);

        }

    }
    public void delete(int position)
    {
        //removes the row
        msgBeanList.remove(position);
        notifyItemRemoved(position);
        //notifyDataSetChanged();
    }

}
