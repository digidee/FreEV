package com.example.freespot;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freespot.AlertDialogRadio.AlertPositiveListener;
import com.example.freespot.EditNameDialog.EditNameDialogListener;
import com.example.freespot.database.TestDatabaseActivity;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, EditNameDialogListener, AlertPositiveListener{
	
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "Navigation_item_selected";
	public final static String EXTRA_MESSAGE = "com.example.test.MESSAGE";
	
	private static final String LOG_TAG = "freespot_OverView";
	private String names = "";
	Chronometer mChronometer;
	Button selectB;
	TextView tv;
	TextView tv2;

	
	//Dialogradio - store position
	int position = 0;
		
	//Intent intent = new Intent(this, Excersice.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Setting the layout
		setContentView(R.layout.activity_main);
		
		
		//Creating tabs when the program starts
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//Adding new tabs
		actionBar.addTab(actionBar.newTab().setText("Overview").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Parking Log").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Savings Log").setTabListener(this));
		

		

		//setProduct( savedInstanceState.getString("saveNames"));
	}
	
	

	
	public void startDialogRadio(){
		
		FragmentManager manager = getFragmentManager();
		 
        /** Instantiating the DialogFragment class */
        AlertDialogRadio alert = new AlertDialogRadio();

        /** Creating a bundle object to store the selected item's index */
        Bundle b  = new Bundle();

        /** Storing the selected item's index in the bundle object */
        b.putInt("position", position);

        /** Setting the bundle object to the dialog fragment object */
        alert.setArguments(b);

        /** Creating the dialog fragment object, which will in turn open the alert dialog window */
        alert.show(manager, "alert_dialog_radio");
	}
	
	
	//Saving the tab instance
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  savedInstanceState.putString("saveNames", names);
	  // etc.
	}

	
	//To ensure the program stays on the same tab after restore
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
     // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
       // i.e. String myString = savedInstanceState.getString("MyString");   
        setProduct( savedInstanceState.getString("saveNames"));

    }
    
    
    
    /*
     * testing database. calling new activity
     * 
     * */
	public void startBase(){
		Intent i = new Intent(MainActivity.this, TestDatabaseActivity.class);
		startActivity(i);
		

	}
     
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		//Switch to the right tab
		switch(tab.getPosition()){
			case 0:  	
				OverView ov = new OverView();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, ov).commit();
				Toast.makeText(this, tab.getText().toString(), Toast.LENGTH_SHORT).show();
			break;
			case 1:  	
				ParkingLog ex = new ParkingLog();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, ex).commit();
				Toast.makeText(this, tab.getText().toString(), Toast.LENGTH_SHORT).show();
			break;
			case 2:  	
				MoneyLog re = new MoneyLog();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, re).commit();
				Toast.makeText(this, tab.getText().toString(), Toast.LENGTH_SHORT).show();
			break;
		}
			
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	

    //Called when dialog is finished
	@Override
	public void onFinishEditDialog(String inputText) {
		Log.d(LOG_TAG, "Called:	onFinishEditDialog");
		
		//Fragments don't extend context. You have to get the activity to pass as the context. (this -> OverView.this.getActivity())
		Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
		setProduct(inputText);
	}
	

	
	public void  setProduct(String n){
		names = n;
		if (!names.contentEquals("")){
			tv = (TextView) findViewById(R.id.savingitem);
			tv.setVisibility(View.VISIBLE);
			tv.setText(n);
			tv2 = (TextView) findViewById(R.id.savingfor);
			tv2.setVisibility(View.VISIBLE);
			selectB = (Button) findViewById(R.id.select);
			selectB.setVisibility(View.GONE);
		}
	}
	
	 public void startChronometer(View view) {
		 mChronometer = (Chronometer) findViewById(R.id.chronometer);
		 
		 
		 
		 mChronometer.setOnChronometerTickListener(new OnChronometerTickListener() {
			    public void onChronometerTick(Chronometer cArg) {
			        long t = SystemClock.elapsedRealtime() - cArg.getBase();
			        cArg.setText(DateFormat.format("mm:ss", t));
			    }
			});
		 
		 mChronometer.setVisibility(View.VISIBLE); 
		 mChronometer.setBase(SystemClock.elapsedRealtime());
		 mChronometer.start();
	 }

    public void stopChronometer(View view) {
		 mChronometer = (Chronometer) findViewById(R.id.chronometer);
		 mChronometer.setVisibility(View.GONE); 
		 mChronometer.setBase(SystemClock.elapsedRealtime());
		 mChronometer.stop(); 
    }
	
    public long getTime(){
    	mChronometer = (Chronometer) findViewById(R.id.chronometer);
    	long time = SystemClock.elapsedRealtime() - mChronometer.getBase();
    	return time;
    }
    
    
    /** Defining button click listener for the OK button of the alert dialog window */
    @Override
    public void onPositiveClick(int position) {
        this.position = position;
 
        /** Getting the reference of the textview from the main layout */
        TextView tv = (TextView) findViewById(R.id.savingitem);
 
        /** Setting the selected android version in the textview */
        tv.setText("You are saving for: " + ProductSelection.code[this.position]);
    }
	
	

}
