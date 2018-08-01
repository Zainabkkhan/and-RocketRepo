package com.dan.lnhospital.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.dan.lnhospital.bean.MessageBean;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper
{
    String TAG="DbHelper";


    public DbHelper(Context context)
    {
        super(context,DbContract.DbDetails.DBNAME, null, DbContract.DbDetails.DBVERSION);
        Log.e("context",""+context.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_msgDB = "Create Table " + DbContract.TABLE_MESSAGE + " ("

                + DbContract.COLUMN_ID + " number PRIMARY KEY,"
                + DbContract.MESSAGE.COLUMN_MESSAGE + " text ,"
                + DbContract.MESSAGE.COLUMN_DELETE_FLAG + " text ,"
                + DbContract.MESSAGE.COLUMN_DISPLAY_FLAG + " text )";

        db.execSQL(create_msgDB);
        Log.e(TAG,"database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertMsgs(List<MessageBean> msgsList)
    {
        Log.e("contentMsg",""+msgsList.get(0).getMessage());
        SQLiteDatabase db;
        ContentValues contentMsg;
        synchronized (db = getWritableDatabase())
        {
            try
            {
                for (MessageBean msg : msgsList)
                {
                    try
                    {
                          contentMsg = new ContentValues();

//                        contentMsg.put(DbContract.COLUMN_ID, );
                        contentMsg.put(DbContract.MESSAGE.COLUMN_MESSAGE, msg.getMessage());
                        Log.e("Message",""+msg.getMessage());
//                        contentMsg.put(DbContract.MESSAGE.COLUMN_DELETE_FLAG, msg.get);
//                        contentMsg.put(DbContract.MESSAGE.COLUMN_DISPLAY_FLAG, msg.getAnswer());
                        if (db.insert(DbContract.TABLE_MESSAGE, null, contentMsg) == -1)
                        {
                            db.update(DbContract.TABLE_MESSAGE, contentMsg, msg.getMessage(), null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                db.close();
            }
        }
    }

    public List<MessageBean> getMsgList()
    {
        SQLiteDatabase db;
        Cursor cr = null;
        List<MessageBean> msgBeanList = new ArrayList<>();
        MessageBean msgBean;
        synchronized (db = getReadableDatabase()) {
            try {
                cr = db.rawQuery("Select * from " + DbContract.TABLE_MESSAGE, null);
                if (cr.moveToFirst()) {
                    do
                    {
                        msgBean = new MessageBean();
                       // faqBean.setId(cr.getInt(cr.getColumnIndex(DBContract.COLUMN_ID)));
                        msgBean.setMessage(cr.getString(cr.getColumnIndex(DbContract.MESSAGE.COLUMN_MESSAGE)));
                       // faqBean.setAnswer(cr.getString(cr.getColumnIndex(DBContract.FAQ.COLUMN_ANSWER)));
                        msgBeanList.add(msgBean);
                    } while (cr.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
                if (cr != null)
                    cr.close();
                msgBean = null;
            }
            return msgBeanList;
        }
    }
}
