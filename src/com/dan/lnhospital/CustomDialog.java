package com.dan.lnhospital;



import com.dan.lnhospital.AppVar.AppVariable;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class CustomDialog extends Dialog implements OnClickListener
{

    
   
    
	Context a;
    public  CustomDialog(Context context)
    {
        super(context,R.style.dialogStyle);
        
        setContentView(R.layout.dialog_alarm);
        a = context;
        
        Button Ok = (Button) findViewById(R.id.btnOk1);
        Button wifiSetting = (Button) findViewById(R.id.btnsetting);
        System.out.println("in edit");
        Ok.setOnClickListener(this);
        wifiSetting.setOnClickListener(this);
       
    }
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch(id)
		
		{
			case R.id.btnOk1:
				Log.i("LNJP","inside Customdialog");
				AppVariable.getDialog().dismiss();
				break;
				
			case R.id.btnsetting:
				AppVariable.getDialog().dismiss();
				final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                intent.setComponent(cn);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                a.startActivity(intent);



				
				break;
				
				
			
		}
		// TODO Auto-generated method stub
		
	}
    
    
   
   
}