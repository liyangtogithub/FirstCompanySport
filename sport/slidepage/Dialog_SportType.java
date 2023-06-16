package com.desay.sport.slidepage;
import java.util.ArrayList;

import com.desay.sport.data.SportData;
import com.desay.sport.main.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Dialog_SportType extends DialogFragment { 
    private static final String TAG = "Dialog_SportType";
    private Dialog ad = null;
    private ListView list;
    private TextView tv_sporttype;
    @Override 
    public Dialog onCreateDialog(Bundle savedInstanceState) { 
   	AlertDialog.Builder builder ;
	builder = new AlertDialog.Builder(getActivity());
    if (ad == null)
    {
        ad = builder.create();
    }	
    if (!ad.isShowing())
    {
        ad.show();
        LayoutInflater inflater=  LayoutInflater.from(getActivity());
		final String [] sporttype = getResources().getStringArray(R.array.sporttype);
		View layout = inflater.inflate(R.layout.dialog, null);
	    ad.setContentView(layout);
	    ad.getWindow().setLayout((int)getResources().getDimension(R.dimen.edge_560), LayoutParams.WRAP_CONTENT);
		list  = (ListView)layout.findViewById(R.id.list);
		dateAdapter_dialog adapter = new dateAdapter_dialog(getActivity(),sporttype,SportData.ICON_SPORTTYPE);
		adapter.setSplitLine(SportData.POSITION_SPLIT_PLAN, SportData.POSITION_SPLIT_FREE);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				tv_sporttype.setText(sporttype[arg2]);
					SportData.UpdateSportType(getActivity().getApplicationContext(),arg2);
				ad.dismiss();
			}});
    }
		return ad;
    }
    
    public void setSportType(TextView tv_show) {
    	tv_sporttype = tv_show;
	}
}
