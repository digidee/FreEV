package com.example.freespot;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.freespot.database.Comment;
import com.example.freespot.database.CommentsDataSource;



/*
 * 	Lifecycle of fragments
 *
 * 	Though a Fragment's lifecycle is tied to its owning activity, it has its own wrinkle on the standard activity lifecycle.
 * 	It includes basic activity lifecycle methods such as onResume(), but also important are methods related to interactions with 
 * 	the activity and UI generation.
 *		
 *	The core series of lifecycle methods that are called to bring a fragment up to resumed state (interacting with the user) are:
 *	
 *	1. onAttach(Activity) called once the fragment is associated with its activity.
 *	2. onCreate(Bundle) called to do initial creation of the fragment.
 *	3. onCreateView(LayoutInflater, ViewGroup, Bundle) creates and returns the view hierarchy associated with the fragment.
 *	4. onActivityCreated(Bundle) tells the fragment that its activity has completed its own Activity.onCreate().
 * 	5. onViewStateRestored(Bundle) tells the fragment that all of the saved state of its view hierarchy has been restored.
 * 	6. onStart() makes the fragment visible to the user (based on its containing activity being started).
 *	7. onResume() makes the fragment interacting with the user (based on its containing activity being resumed).
 *	
 *	As a fragment is no longer being used, it goes through a reverse series of callbacks:
 *	
 *	1. onPause() fragment is no longer interacting with the user either because its activity is being paused or a fragment operation is modifying it in the activity.
 *	2. onStop() fragment is no longer visible to the user either because its activity is being stopped or a fragment operation is modifying it in the activity.
 *	3. onDestroyView() allows the fragment to clean up resources associated with its View.
 *	4. onDestroy() called to do final cleanup of the fragment's state.
 *	5. onDetach() called immediately prior to the fragment no longer being associated with its activity. 
 */



public class OverView extends ListFragment {
	

	private ProgressBar pb;
	private static final String LOG_TAG = "freespot_OverView";

	
	private CommentsDataSource datasource;
	
	Chronometer mChronometer;
	
	/* 1 - 	Constructor (General programming: Java/C)
	 * 		Purpose and function:
	 *		Constructors have one purpose in life: to create an instance of a class. 
	 *		This can also be called creating an object, as in:
	 *		Platypus p1 = new Platypus();
	 */
	public OverView() {		
		//Logging to LogCat
		Log.d(LOG_TAG, "Called: Constructor OverView");
	}
	
	
	
	/*
	 * 2 - 	onCreate(Bundle) called to do initial creation of the fragment.
	 * 		This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
	 * 		This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.  
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "Called: onCreate");
		/**
		 * ListAdapter will get info from dataArray and put it to the list
		 */
		//ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dataArray);
		//setListAdapter(listAdapter);
		
		   datasource = new CommentsDataSource(getActivity());
		   datasource.open();

		    List<Comment> values = datasource.getAllComments();

		    ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(getActivity(),
			        R.layout.rowlayout, R.id.label, values);
			    setListAdapter(adapter);
  
	}

	
	
	// 3 - 	onCreateView(LayoutInflater, ViewGroup, Bundle) creates and returns the view hierarchy associated with the fragment. 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 Log.d(LOG_TAG, "Called: onCreateView");
		
		View v = inflater.inflate(R.layout.fragment_overview, container, false);
		
		
		
		
	    
        
		   final Button startSaving = (Button) v.findViewById(R.id.saving);
	         startSaving.setOnClickListener(new OnClickListener() {
	             public void onClick(View v) {
	         
	            	 String saveText = startSaving.getText().toString();
	            	 
	            	 if (saveText.equals("Start Parking")){
	            		 saveText = "End Parking";
	            		 startSaving.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_button_end));
	            		 
	            		 //Need to call chronometer from mainactivity
	            		 //Start chronometer from current view
	            		 ((MainActivity) getActivity()).startChronometer(v);
	            	 }
	            		 
	            	 else{
	            		 saveText = "Start Parking";

	            		 startSaving.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_button));
	            		 

	            		 long millichronTime = ((MainActivity) getActivity()).getTime();
	            		 int seconds = (int)millichronTime / 1000;


	            		 //formatting date
	            		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	            		 //get current date time with Calendar()
	            		 Calendar cal = Calendar.getInstance();	            		 
	            		 
	            		 
	            		 String formatTime = String.format("%02d:%02d:%02d", seconds/3600, (seconds%3600)/60, (seconds%60));
	            		 
	            		 String time = "Parking time: "+formatTime;
	            		 String date = dateFormat.format(cal.getTime());
	            		 
	            		 Log.d(LOG_TAG, "Called: Chronometer time: " +  formatTime);
	            		 
	            		 String parkTime = "Parking date: "+date+"\n"+time;
		            		 
	            		 //Add time to database	            		 
	     	        	 ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
	    	       	     Comment comment = null;
	    	       	     comment = datasource.createComment(parkTime);
	    	          	 adapter.add(comment);  
	            		 
	    	          	 String toastTime = "End of parking time!"+"\n"+parkTime;
	    	          	 Toast.makeText(OverView.this.getActivity(), toastTime, Toast.LENGTH_LONG).show();
	    	          	
	    	          	((MainActivity) getActivity()).stopChronometer(v);
  		 
	            	 }
	            	 
	            	 
	            	 startSaving.setText(saveText);
	             }
	         });   

		
		

        return v;
	}
	
	
	
	/* 4 - 	onViewCreated(View view, Bundle savedInstanceState)
	 *		Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
	 */
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		Log.d(LOG_TAG, "Called: onViewCreated");
	        
        //finding progressbar
		 pb = (ProgressBar) v.findViewById(R.id.pgbAwardProgress);
		 
		 //setting progressbar options
		 pb.setVisibility(View.VISIBLE);
         pb.setMax(100);
         pb.setProgress(60);
         pb.setIndeterminate(false);
         
         //finding button
         Button bselect = (Button) v.findViewById(R.id.select);
         
         //setting onclicklistened
         bselect.setOnClickListener(new OnClickListener() {
             public void onClick(View v) {
         
            	 //call shoeEcitDialog
            	 //showEditDialog();
            	 
            	 //car dialog radio from main activity
            	 ((MainActivity) getActivity()).startDialogRadio();
             }
         }); 
         
        /* 
         //finding button
         Button reg = (Button) v.findViewById(R.id.register);
         
         //setting onclicklistened
         reg.setOnClickListener(new OnClickListener() {
             public void onClick(View v) {
         
            	 //call shoeEcitDialog
            	 //showEditDialog();
            	 
            	 //car dialog radio from main activity
            	 ((MainActivity) getActivity()).startBase();
             }
         }); 
         
         */
         
     
	}

	  
	//Shows the dialog
    private void showEditDialog() {
    	Log.d(LOG_TAG, "Called:	showEditDialog");
    	
        FragmentManager fm = getFragmentManager();
        EditNameDialog editNameDialog = new EditNameDialog();
        editNameDialog.show(fm, "fragment_dialog");
        
    }
    

}
