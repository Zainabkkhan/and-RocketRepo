package com.dan.lnhospital.AppVar;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;

public class AppSettings {
	
	
	
	public static boolean isNetworkAvailable(Context c) //checks wifi availablity
	{
	    ConnectivityManager cm = (ConnectivityManager) 
	    c.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected())
	    {
	        return true;
	    }
	    return false;
	    
	}
	
	public static String buildDateTime(long unixSeconds)
	{
		//long unixSeconds = Long.parseLong(dateString);
		Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm"); // the format of your date
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30")); // give a timezone reference for formating (see comment at the bottom
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate);

		return formattedDate;
    }
	public static String buildDate(long unixSeconds)
	{
		//long unixSeconds = Long.parseLong(dateString);
		Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm"); // the format of your date
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30")); // give a timezone reference for formating (see comment at the bottom
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate);

        return formattedDate;
    }
	
	
	 public static void  buttonSound(Context c) //generate button sound
     {
     Vibrator vib = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
     vib.vibrate(200);
        Uri path = Uri.parse("android.resource://com.dan.lnhospital/raw/click");
        MediaPlayer mp = MediaPlayer.create(c,path);
        if(mp.isPlaying())
        {
            mp.stop();
        }
        mp.setLooping(false);
        mp.start();

     }
	 
}
