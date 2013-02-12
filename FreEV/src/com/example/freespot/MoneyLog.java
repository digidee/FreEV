package com.example.freespot;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.freespot.database.Logging;
import com.example.freespot.database.LoggingDataSource;

public class MoneyLog extends ListFragment {
	
	private String[] values;
	
	private LoggingDataSource datasource;
	
	private ProgressBar pb;
	
	private int totalCosts = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
/*
    
	values = new String[] { "Test", "Test" };
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        R.layout.rowlayout, R.id.label, values);    
    
    setListAdapter(adapter);
    */
    
    
	   datasource = new LoggingDataSource(getActivity());
	   datasource.open();

	    List<Logging> values = datasource.getAllLogs();

	    // Use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    ArrayAdapter<Logging> adapter = new ArrayAdapter<Logging>(getActivity(),
	        R.layout.rowlayout, R.id.label, values);
	    setListAdapter(adapter);

		    
	       for (Logging logs : values) {
	            totalCosts += logs.getTotalCosts();
	       }
	    
	    
	    
  }


  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		View v = inflater.inflate(R.layout.fragment_moneylog, container, false);
	
	       //finding button
	       Button bselect = (Button) v.findViewById(R.id.delete);
	       
	       //setting onclicklistened
	       bselect.setOnClickListener(new OnClickListener() {
	           public void onClick(View v) {
	        	   
	        	ArrayAdapter<Logging> adapter = (ArrayAdapter<Logging>) getListAdapter();

	       	    Logging log = null;
	       	    
	       	    
	       	 /*comment = datasource.createComment("esfsefes");
	          	adapter.add(comment);  */
	        	
	        	
	        	if (getListAdapter().getCount() > 0) {
	                log = (Logging) getListAdapter().getItem(0);
	                datasource.deleteLog(log);
	                adapter.remove(log);
	              }
	        	
	           }
	       }); 
	       

	        //finding progressbar
			 pb = (ProgressBar) v.findViewById(R.id.pgbAwardProgress);
			 
			 //setting progressbar options
			 pb.setVisibility(View.VISIBLE);
	         pb.setMax(4000);
	         pb.setProgress(totalCosts);
	         pb.setIndeterminate(false);
	         
	         //finding button
	         Button bselect2 = (Button) v.findViewById(R.id.select);
	         
	         //setting onclicklistened
	         bselect2.setOnClickListener(new OnClickListener() {
	             public void onClick(View v) {
	         
	            	 //call shoeEcitDialog
	            	 //showEditDialog();
	            	 
	            	 //car dialog radio from main activity
	            	 ((MainActivity) getActivity()).startDialogRadio();
	             }
	         }); 

		
		return v;
		
	}



	
	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		/**
		 * Toast message will be shown when you click any list element
		 */
		Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

	    
	}

} 