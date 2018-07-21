package com.dan.lnhospital;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WifiReciever extends  BroadcastReceiver {



	  @Override
	  public void onReceive(Context context, Intent intent) {
		 Intent service = new Intent(context, WiFiCheckService.class);
	    Log.i("LNJP","OnRecievecalled");
	  
	    context.startService(service);
	  }
	
}
