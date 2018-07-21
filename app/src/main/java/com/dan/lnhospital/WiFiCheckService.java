package com.dan.lnhospital;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;





import com.dan.lnhospital.AppVar.AppSettings;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class WiFiCheckService extends Service{
	 public static final String PSK = "PSK";
	  public static final String WPA = "WPA";
	  private Intent alarmIntent;
	    public static final String WEP = "WEP";
	    public static final String OPEN = "Open";

	    private static final int REQUEST_ENABLE_WIFI = 10;

	    private final ScheduledExecutorService worker =
	            Executors.newSingleThreadScheduledExecutor();
	    private ScheduledFuture taskHandler;

	    private ProgressDialog progressDialog;
	    private ScanReceiver scanReceiver;
	  
	    private ConnectionReceiver connectionReceiver;
	    
	  
	    /**
	     * Get the timeout in seconds for connecting to the wifi network
	     *
	     * @return wifi network connection timeout
	     */
	    protected int getSecondsTimeout() {
	        return 10;
	    }

	    protected String getWifiSSID() {
	        return "DQMS";
	    }

	    protected String getWifiPass() {
	        return "dqms12345678";
	    }
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//unregisterReceiver(scanReceiver);
		// unregisterReceiver(connectionReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
			connectToSpecificNetwork();
			
			Log.i("LNJP","wifiServiceStarted");
			
		
		
		
		return START_REDELIVER_INTENT;
	}
	private void connectToSpecificNetwork() {
        final WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        if (networkInfo.isConnected() && wifiInfo.getSSID().replace("\"", "").equals(getWifiSSID())) {
        	 stopSelf();
 		    newsdownload();
            return;
        }
       // progressDialog = ProgressDialog.show(this, getString(R.string.connecting), String.format(getString(R.string.connecting_to_wifi), getWifiSSID()));
        taskHandler = worker.schedule(new TimeoutTask(), getSecondsTimeout(), TimeUnit.SECONDS);
        scanReceiver = new ScanReceiver();
        registerReceiver(scanReceiver
                , new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifi.startScan();
    }
	 private class ConnectionReceiver extends BroadcastReceiver {

	        WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);


	        @Override
	        public void onReceive(Context context, Intent intent) {
	            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	            NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	            WifiInfo wifiInfo = wifi.getConnectionInfo();
	            if (networkInfo.isConnected()) {
	                if (wifiInfo.getSSID().replace("\"", "").equals(getWifiSSID())) {
	                    unregisterReceiver(this);
	                    if (taskHandler != null) {
	                        taskHandler.cancel(true);
	                    }
	                    /*if (progressDialog != null) {
	                        progressDialog.dismiss();
	                    }*/
	                }
	            }
	        }
	    }

	  private class ScanReceiver extends BroadcastReceiver {


	        @Override
	        public void onReceive(Context context, Intent intent) {
	            WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
	            List<ScanResult> scanResultList = wifi.getScanResults();
	            boolean found = false;
	            String security = null;
	            for (ScanResult scanResult : scanResultList) {
	                if (scanResult.SSID.equals(getWifiSSID())) {
	                    security = getScanResultSecurity(scanResult);
	                    found = true;
	                }
	            }
	            if (!found) {
	                // if no wifi network with the specified ssid is not found exit
	                /*if (progressDialog != null) {
	                    progressDialog.dismiss();
	                }*/
	               /* new AlertDialog.Builder(MainActivity.this)
	                        .setCancelable(false)
	                        .setMessage(String.format(getString(R.string.wifi_not_found), getWifiSSID()))
	                        .setPositiveButton(getString(R.string.exit_app), new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int which) {
	                                unregisterReceiver(ScanReceiver.this);
	                                finish();
	                            }
	                        })
	                        .show();*/
	            } else {
	            	Log.i("LNJP","WifiConnectinggggg");
	                // configure based on security
	                final WifiConfiguration conf = new WifiConfiguration();
	                conf.SSID = "\"" + getWifiSSID() + "\"";
	                switch (security) {
	                    case WEP:
	                        conf.wepKeys[0] = "\"" + getWifiPass() + "\"";
	                        conf.wepTxKeyIndex = 0;
	                        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	                        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
	                        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
	                        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
	                        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
	                        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
	                        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
	                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
	                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
	                        break;
	                    case WPA:
	                    	conf.preSharedKey = "\"" + getWifiPass() + "\"";
	                    	conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
	                    	conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
	                    	conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
	                    	conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
	                    	conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
	                    	conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
	                    	conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
	                    	conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	                    	conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	                        break;
	                    
	                    case PSK:
	                        conf.preSharedKey = "\"" + getWifiPass() + "\"";
	                        break;
	                    case OPEN:
	                    	conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	                    	conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
	                    	conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
	                    	conf.allowedAuthAlgorithms.clear();
	                    	conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
	                    	conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
	                    	conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
	                    	conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
	                    	conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	                    	conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);	                        break;
	                }
	                connectionReceiver = new ConnectionReceiver();
	                IntentFilter intentFilter = new IntentFilter();
	                intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
	                intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
	                intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
	                registerReceiver(connectionReceiver, intentFilter);
	                int netId = wifi.addNetwork(conf);
	                wifi.disconnect();
	                wifi.enableNetwork(netId, true);
	                wifi.reconnect();
	                unregisterReceiver(this);
	               
	                stopSelf();
	    		    newsdownload();
	            }
	        }
	    }

	    /**
	     * Timeout task. Called when timeout is reached
	     */
	    private class TimeoutTask implements Runnable {
	        @Override
	        public void run() {
	            WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
	            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	            NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	            WifiInfo wifiInfo = wifi.getConnectionInfo();
	            Log.i("LNJP","TimeoutTask"
	            		+ "");
	            if (networkInfo.isConnected() && wifiInfo.getSSID().replace("\"", "").equals(getWifiSSID())) {
	                try {
	                    unregisterReceiver(connectionReceiver);
	                } catch (Exception ex) {
	                    // ignore if receiver already unregistered
	                }
	               /* MainActivity.this.runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                        if (progressDialog != null) {
	                            progressDialog.dismiss();
	                        }
	                    }
	                });*/
	            } else {
	                try {
	                    unregisterReceiver(connectionReceiver);
	                } catch (Exception ex) {
	                    // ignore if receiver already unregistered
	                }
	              /*  MainActivity.this.runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                        if (progressDialog != null) {
	                            progressDialog.dismiss();
	                        }
	                        new AlertDialog.Builder(MainActivity.this)
	                                .setCancelable(false)
	                                .setMessage(String.format(getString(R.string.wifi_not_connected), getWifiSSID()))
	                                .setPositiveButton(getString(R.string.exit_app), new DialogInterface.OnClickListener() {
	                                    public void onClick(DialogInterface dialog, int which) {
	                                        finish();
	                                    }
	                                })
	                                .show();
	                    }

	                });*/
	            }
	        }
	    }

	
	
	
	
	    private String getScanResultSecurity(ScanResult scanResult) {
	        final String cap = scanResult.capabilities;
	        final String[] securityModes = {WEP, PSK,WPA,};
	        for (int i = securityModes.length - 1; i >= 0; i--) {
	            if (cap.contains(securityModes[i])) {
	                return securityModes[i];
	            }
	        }

	        return OPEN;
	    }
	
	    public void newsdownload()
		  {
			  Log.i("ImageUpload", "Setting up auto image capture alarm");
			  long triggerTime = SystemClock.elapsedRealtime() +
				       60*1000;
		               
		      alarmIntent = new Intent(getApplicationContext(), WifiReciever.class);
		   
		      CancelAlarm();
		      Log.i("ImageUpload", "New alarm intent");
		      PendingIntent sender = PendingIntent.getBroadcast(this, 0, alarmIntent,
		              PendingIntent.FLAG_UPDATE_CURRENT);
		     AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		     am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, sender);
		    
		      Log.i("ServiceLOgin","Alarm has been set");
		  }
		  
		  
		  public void CancelAlarm()
		  {
		      Log.i("login", "LoginService.CancelAlarm");

		      if (alarmIntent != null)
		      {
		          Log.i("LNJP1","LoginService.CancelAlarm");
		          AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		          PendingIntent sender = PendingIntent.getBroadcast(this, 0, alarmIntent,
		                  PendingIntent.FLAG_UPDATE_CURRENT);
		          Log.i("ImageUpload", "Pending alarm intent was null? " + String.valueOf(sender == null));
		          am.cancel(sender);
		      }

		  }
	
	
	
}
