package com.dan.lnhospital;

import org.json.JSONException;
import org.json.JSONObject;











import com.dan.lnhospital.AppVar.AppSettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DocSummaryActivity extends Activity {
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doc_summary);
		Intent res = getIntent();
		
		    
		    String login_time = null;
		    String total_token_group = null;
		    String total_token_doctor=null;
		    String skipped=null;
		    String treated=null;
		    String cancelled=null;
		
		String response = res.getStringExtra("response");
		String docname = res.getStringExtra("docname");
		try {
			JSONObject js = new JSONObject(response);
			
			login_time= js.getString("login_time");
			total_token_group = js.getString("total_token");
		    total_token_doctor= js.getString("total_token_doctor");
		    skipped = js.getString("total_treated_token_doctorID");
		    treated = js.getString("TotalSkippedTokenNo");
		    cancelled = js.getString("total_cancel_token");
			
			
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextView name = (TextView) findViewById(R.id.docname);
		TextView date = (TextView) findViewById(R.id.logindatevalue);
		TextView time = (TextView) findViewById(R.id.logintimevalue);
		TextView total_patient = (TextView) findViewById(R.id.totalpatientsvalue);
		TextView called_patient = (TextView) findViewById(R.id.patientcalledvalue);
		TextView treated_patient = (TextView) findViewById(R.id.patienttreatedvalue);
		TextView skipped_patient = (TextView) findViewById(R.id.patientskippedvalue);
		TextView canceled_patient = (TextView) findViewById(R.id.patientcancelledvalue);
		
		
		String[] splited =AppSettings.buildDateTime(Long.parseLong(login_time)).split("\\s+");
		 Log.i("LNJP",AppSettings.buildDateTime(Long.parseLong(login_time)));
		 name.setText(docname);
		 date.setText(splited[0]);
		 time.setText(splited[1]);
		 total_patient.setText(total_token_group);
		 called_patient.setText(total_token_doctor);
		 treated_patient.setText(skipped);
		 skipped_patient.setText(treated);
		 canceled_patient.setText(cancelled);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_doc_summary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
