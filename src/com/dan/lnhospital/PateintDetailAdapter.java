package com.dan.lnhospital;




import java.util.ArrayList;





import com.dan.lnhospital.bean.PateintDetailHelper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class PateintDetailAdapter extends ArrayAdapter<PateintDetailHelper> {
	
	Context c;
	
	ArrayList<PateintDetailHelper> data;
	
	
	 
    public PateintDetailAdapter(Context context,ArrayList<PateintDetailHelper> data) {
    	super(context, R.layout.list_pateintdetail, data);
    	
    	
       
       c=context;
     
      
      this.data=data;
      
    }
   
  



	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
		
		PateintDetailHelper pdh = getItem(position);
		
		if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_pateintdetail, parent, false);
	       }
		
		TextView sno = (TextView) convertView.findViewById(R.id.Sno);
		TextView patientname = (TextView) convertView.findViewById(R.id.pateintname);
		TextView status = (TextView) convertView.findViewById(R.id.Status);
		TextView tokenno = (TextView) convertView.findViewById(R.id.tokenno);
		 
		int stat = pdh.getStaus();
		switch(stat)
		{
		case 0:
			status.setText("Not Called");
			break;
		case 1:
			status.setText("Called");
			status.setTextColor(Color.parseColor("#1c1e85"));
			break;
			
		case 2:
			status.setText("Skipped");
			status.setTextColor(Color.parseColor("#fe2e2e"));
			break;
			
		case 3:
			status.setText("Treated");
			status.setTextColor(Color.parseColor("#0b5e56"));
			break;
			
		case 4:
			status.setText("Cancelled");
			status.setTextColor(Color.parseColor("#946b2d"));
			break;
			
		}
		
		sno.setText(String.valueOf(position+1));
		patientname.setText(pdh.getPateint_name());
		tokenno.setText(String.valueOf(pdh.getToken_num()));
		
		return convertView;
		
		
}

	
}
