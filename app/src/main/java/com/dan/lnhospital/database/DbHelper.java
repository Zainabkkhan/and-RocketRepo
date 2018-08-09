package com.dan.lnhospital.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.dan.lnhospital.MessageActivity;
import com.dan.lnhospital.bean.MessageBean;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper
{
    String TAG="DbHelper";

    public DbHelper(Context context)
    {
        super(context,DbContract.DbDetails.DBNAME, null, DbContract.DbDetails.DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_msgDB = "Create Table " + DbContract.TABLE_MESSAGE + " ("
                + DbContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DbContract.MESSAGE.COLUMN_MESSAGE + " text)";
        db.execSQL(create_msgDB);
     //   Log.e(TAG,"database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertMsgs(List<MessageBean> msgsList)
    {
       Log.e("Message Database size",""+msgsList.size());
        ContentValues contentMsg;
        SQLiteDatabase db;
        synchronized (db = getWritableDatabase())
        {
            try
            {
                for (MessageBean msg : msgsList)
                {
                    try
                    {
                        contentMsg = new ContentValues();
                        contentMsg.put(DbContract.MESSAGE.COLUMN_MESSAGE, msg.getMessage());
 //                       Log.e("Inserted Message in db",""+msg.getMessage());
                        if (db.insert(DbContract.TABLE_MESSAGE, null, contentMsg) == -1)
                        {
                            db.update(DbContract.TABLE_MESSAGE, contentMsg, DbContract.COLUMN_ID + "=" +msg.getMessage(), null);
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
        Cursor cr = null;
        List<MessageBean> msgBeanList = new ArrayList<>();
        MessageBean msgBean;
        SQLiteDatabase db;
        synchronized (db = getReadableDatabase())
        {
            try
            {
                cr = db.rawQuery("Select * from " + DbContract.TABLE_MESSAGE, null);
                if (cr.moveToFirst())
                {
                    do
                    {
                        msgBean = new MessageBean();
                        msgBean.setMessage(cr.getString(cr.getColumnIndex(DbContract.MESSAGE.COLUMN_MESSAGE)));
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
    public boolean deletemsg(int position,String message)
    {
       // Log.e("dbdelete","Position="+position+"message="+message);
        SQLiteDatabase db;
        try
        {
           // Log.e("delete from database","message="+message);
            db=getWritableDatabase();
            int val=db.delete(DbContract.TABLE_MESSAGE, DbContract.MESSAGE.COLUMN_MESSAGE+"==?" , new String[]{message});
               Log.e("Value",""+val);
               db.close();
        }
        catch (Exception e)
        {
           Log.e("Datbase not deleted",""+e);
          //  Toast.makeText(""TAG, "Message not Deleted", Toast.LENGTH_SHORT).show();
        }
       return true;

    }


//    public void truncateFootPrintDb() {
//        SQLiteDatabase db;
//        synchronized (db = getWritableDatabase()) {
//            try {
//                db.delete(DBContract.TABLE_USER_FOOTPRINT, null, null);
//                db.delete(DBContract.TABLE_USER_FOOTPRINT_DETAILS, null, null);
//            } catch (Exception e) {
//
//            } finally {
//                db.close();
//            }
//        }
//    }
}
