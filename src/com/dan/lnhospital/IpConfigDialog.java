package com.dan.lnhospital;





import com.dan.lnhospital.AppVar.AppSettings;
import com.dan.lnhospital.AppVar.AppVariable;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class IpConfigDialog extends Dialog implements OnClickListener
{
	 EditText edit1;
     String Id;
    SharedPreferences data;
    Editor edit ;
    
	Context a;
    public  IpConfigDialog(Context context ,String Id)
    {
        super(context,R.style.dialogStyle);
        
        setContentView(R.layout.ip_config);
        a = context;
        this.Id=Id;
        data = a.getSharedPreferences("saveIP", Context.MODE_PRIVATE);
        edit = data.edit();
        Button Ok = (Button) findViewById(R.id.btnOk);
        Button cancel = (Button) findViewById(R.id.btnCancel);
         edit1 = (EditText) findViewById(R.id.inputpassword);
        if(Id.equals("IPAdr"))
        {
        	edit1.setHint("Enter IP Address");
        	edit1.setText(AppVariable.getIP());
        }
       if(Id.equals("Password"))
       {
    	   edit1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
       }
        Ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
       
    }
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch(id)
		
		{
			case R.id.btnOk:
				if(Id.equals("Password"))
				{
					String text =edit1.getText().toString();
					Log.i("LNJP",AppVariable.getPasswordipconfig());
				if(text.equals(AppVariable.getPasswordipconfig()));
				{
					
					AppVariable.getDialog1().dismiss();
					IpConfigDialog ip = new IpConfigDialog(a, "IPAdr");
					AppVariable.setDialog1(ip);
					ip.show();
					
				}
				 if(!edit1.getText().toString().equals(AppVariable.getPasswordipconfig()))
				{   AppVariable.getDialog1().dismiss();
					Toast.makeText(a, "Incorrect Password", Toast.LENGTH_LONG).show();
					
				}
				
				
				
				}
				else if(Id.equals("IPAdr"))
				{
					Log.i("LNJP",edit1.getText().toString());
					AppVariable.setIP(edit1.getText().toString());
					edit.putString("IP", edit1.getText().toString());
					edit.commit();
					AppVariable.getDialog1().dismiss();
				}
		
			break;
			case R.id.btnCancel:
				
				AppVariable.getDialog1().dismiss();
				break;


				
				
				
				
			
		}
		// TODO Auto-generated method stub
		
	}
    
    
   
   
}
