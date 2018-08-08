//package com.dan.lnhospital.adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.example.acer3.aamadmidoctoropd.DoctorsActivity;
//import com.example.acer3.aamadmidoctoropd.R;
//
//import java.util.ArrayList;
//import java.util.TreeSet;
//
//import DBHelper.DatabaseHandler;
//
///**
// * Created by ats on 9/12/2016.
// */
//public class TokenListAdapter extends BaseAdapter {
//    ArrayList<String> myList;
//    LayoutInflater inflater;
//    Context context;
//    DatabaseHandler db;
//    int flag;
//    TreeSet<String> allTokens;
//
//
//    public TokenListAdapter(Context context, ArrayList myList,int flag) {
//        this.myList = myList;
//        this.context = context;
//        this.flag=flag;
//        inflater = LayoutInflater.from(this.context);
//        db = new DatabaseHandler(context);
//        allTokens = db.getAllTokens();
//    }
//
//    @Override
//    public int getCount() {
//        return myList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        MyViewHolder mViewHolder;
//
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.token_list, parent, false);
//            mViewHolder = new MyViewHolder(convertView);
//            convertView.setTag(mViewHolder);
//            switch (flag)
//            {
//                case 0: {
//                    if (((DoctorsActivity) context).allToken == position) {
//                        mViewHolder.tvToken.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
//                    }
//                    break;
//                }
//                case 1: {
//                    if (((DoctorsActivity) context).newPatientFl == position) {
//                        mViewHolder.tvToken.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
//                    }
//                    break;
//                }
//                case 2: {
//                    if (((DoctorsActivity) context).oldPatientF1 == position) {
//                        mViewHolder.tvToken.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
//                    }
//                    break;
//                }
//                case 3: {
//                    if (((DoctorsActivity) context).refPatientFl == position) {
//                        mViewHolder.tvToken.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
//                    }
//                    break;
//                }
//                case 4: {
//                    if (((DoctorsActivity) context).otherPatientFl == position) {
//                        mViewHolder.tvToken.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
//                    }
//                    break;
//                }
//                case 5: {
//                    if (((DoctorsActivity) context).skipPatientFl == position) {
//                        mViewHolder.tvToken.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
//                    }
//                    break;
//                }
//            }
//        } else {
//            mViewHolder = (MyViewHolder) convertView.getTag();
//
//        }
//
//        if (allTokens != null) {
//            if (allTokens.contains(myList.get(position))) {
//                mViewHolder.tvToken.setTextColor(Color.parseColor("#4CAF50"));
//            } else {
//                mViewHolder.tvToken.setTextColor(Color.parseColor("#000000"));
//            }
//        }
//        mViewHolder.tvToken.setText(myList.get(position));
//
//
//        return convertView;
//    }
//
//    private class MyViewHolder {
//        TextView tvToken;
//
//
//        public MyViewHolder(View item) {
//
//            tvToken = (TextView) item.findViewById(R.id.tv_token_item);
//
//        }
//    }
//}
