package com.dan.lnhospital.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dan.lnhospital.PanelActivity;
import com.dan.lnhospital.R;
import com.dan.lnhospital.bean.MessageBean;
import com.dan.lnhospital.bean.TokenBean;
import com.dan.lnhospital.bean.TokenDataReceiveBean;

import java.util.List;

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenView> {
    private LayoutInflater inflater;
    public List<TokenDataReceiveBean> tokenBeanList;
    Context context;
    PanelActivity.CallBackInterface callBackInterface;

    public TokenAdapter(PanelActivity panelActivity, List<TokenDataReceiveBean> tokenlist, PanelActivity.CallBackInterface callBackInterface)
    {
        this.context = panelActivity;
        this.tokenBeanList = tokenlist;
        this.callBackInterface = callBackInterface;
    }

    @Override
    public TokenView onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null)
        {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new TokenView(inflater.inflate(R.layout.token_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(TokenView holder, int position)
    {
        Log.e("Position...", "" + position);
        holder.tokenNum.setText(tokenBeanList.get(position).getTokenno());
    }

    @Override
    public int getItemCount()
    {
        return tokenBeanList.size();
    }

    public class TokenView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tokenNum;

        public TokenView(View itemView)
        {
            super(itemView);
            tokenNum = itemView.findViewById(R.id.token_num);
        }


        @Override
        public void onClick(View v)
        {
            Toast.makeText(context, "Click on List position : "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
            callBackInterface.getCalledPosition(this.getAdapterPosition());
        }
    }
}
