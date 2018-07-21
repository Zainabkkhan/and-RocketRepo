package com.dan.lnhospital;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import com.dan.lnhospital.bean.PateintDetailHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class PateintDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pateint_detail);
		
		Intent i = getIntent();
		String response = i.getStringExtra("response");
		Log.i("exception",response);
		ListView pdetail = (ListView) findViewById(R.id.listpateint);
		PateintDetailAdapter pda = new PateintDetailAdapter(this, jsonParser(response));
		pdetail.setAdapter(pda);
		
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pateint_detail, menu);
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
	
	
	private ArrayList<PateintDetailHelper>jsonParser(String response)
	{
		ArrayList<PateintDetailHelper> ar = new ArrayList<PateintDetailHelper>();
		try {
			JSONObject js = new JSONObject(response);
			JSONArray ja = js.getJSONArray("patients");
			for(int i =0;i<ja.length();i++)
			{
				JSONObject js1 =(JSONObject) ja.get(i);
				ar.add(new PateintDetailHelper(js1.getString("patientname"), js1.getInt("status"), js1.getInt("tokenno")));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ar;
	}
}
