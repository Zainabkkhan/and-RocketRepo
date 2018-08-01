package com.dan.lnhospital.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dan.lnhospital.MessageActivity;
import com.dan.lnhospital.R;
import com.dan.lnhospital.bean.MessageBean;
import com.dan.lnhospital.database.DbHelper;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageView>
{
    private static LayoutInflater inflater;
    public List<MessageBean> msgBeanList;
    Context context;
    DbHelper dbHelper;

    public MessageAdapter(MessageActivity messageActivity, List<MessageBean> msgList)
    {
       // Log.e("arraylist in adapter",""+msgList.get(0).getMessage());
        this.context=messageActivity;
        this.msgBeanList=msgList;
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
    public void onBindViewHolder(MessageView holder, int position)
    {
        holder.displayCheckBox.setText(msgBeanList.get(position).getMessage());

    }

    @Override
    public int getItemCount()
    {
        return msgBeanList.size();
    }

    public class MessageView extends RecyclerView.ViewHolder {
        TextView msgView;
        CheckBox displayCheckBox;
        public MessageView(View itemView)
        {
            super(itemView);
           // msgView=itemView.findViewById(R.id.msg_tv);
            displayCheckBox=itemView.findViewById(R.id.msg_check_box);

        }
    }
}
