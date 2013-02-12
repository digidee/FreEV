package com.example.freespot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freespot.database.Logging;
import com.example.freespot.database.LoggingDataSource;

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

public class OverView extends ListFragment implements OnSeekBarChangeListener {

	private static final String LOG_TAG = "freEV_OverView";

	private SeekBar bar; // declare seekbar object variable
	private SeekBar bar2;

	private int totalCosts = 0, newcost = 0, totalCosts2 = 0;

	private int timebar = 0, costbar = 0, totalbar = 0;

	// declare text label objects
	private TextView textPrice, textTime, textTotal, moneySaved;

	private LoggingDataSource datasource;

	LinearLayout regInterface;

	Chronometer mChronometer;

	/*
	 * 1 - Constructor (General programming: Java/C) Purpose and function:
	 * Constructors have one purpose in life: to create an instance of a class.
	 * This can also be called creating an object, as in: Platypus p1 = new
	 * Platypus();
	 */
	public OverView() {
		// Logging to LogCat
		Log.d(LOG_TAG, "Called: Constructor OverView");
	}

	/*
	 * 2 - onCreate(Bundle) called to do initial creation of the fragment. This
	 * is where you should do all of your normal static set up: create views,
	 * bind data to lists, etc. This method also provides you with a Bundle
	 * containing the activity's previously frozen state, if there was one.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "Called: onCreate");
		/**
		 * ListAdapter will get info from dataArray and put it to the list
		 */
		// ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1, dataArray);
		// setListAdapter(listAdapter);

		datasource = new LoggingDataSource(getActivity());
		datasource.open();

		List<Logging> values = datasource.getAllLogs();

		ArrayAdapter<Logging> adapter = new ArrayAdapter<Logging>(
				getActivity(), R.layout.rowlayout, R.id.label, values);
		setListAdapter(adapter);

		for (Logging logs : values) {
			totalCosts += logs.getTotalCosts();
		}

		Log.d(LOG_TAG, "totalCosts: " + totalCosts);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		switch (seekBar.getId()) {
		case R.id.seekBar1:
			// change progress text label with current seekbar value
			textPrice.setText("Rate = " + progress + " NOK");
			// change action text label to changing
			textTotal.setText("Rate x Time = " + bar2.getProgress()
					* bar.getProgress() + " NOK");
			if ((bar2.getProgress() != 0) && (bar.getProgress() != 0)) {
				moneySaved.setText(totalCosts2 + " + " + bar2.getProgress()
						* bar.getProgress() + " NOK");
			} else {
				moneySaved.setText(totalCosts2 + " NOK");
			}
			break;
		case R.id.seekBar2:
			// change progress text label with current seekbar value
			textTime.setText("Time = " + progress + " hours");
			// change action text label to changing
			textTotal.setText("Rate x Time = " + bar2.getProgress()
					* bar.getProgress() + " NOK");
			if ((bar2.getProgress() != 0) && (bar.getProgress() != 0)) {
				moneySaved.setText(totalCosts2 + " + " + bar2.getProgress()
						* bar.getProgress() + " NOK");
			} else {
				moneySaved.setText(totalCosts2 + " NOK");
			}
			break;
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// textAction.setText("starting to track touch");

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		seekBar.setSecondaryProgress(seekBar.getProgress());
		// textAction.setText("Total parking cost: "+bar.getProgress()*bar2.getProgress());
		timebar = bar2.getProgress();
		costbar = bar.getProgress();
		totalbar = timebar * costbar;
	}

	// 3 - onCreateView(LayoutInflater, ViewGroup, Bundle) creates and returns
	// the view hierarchy associated with the fragment.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG_TAG, "Called: onCreateView");

		View v = inflater.inflate(R.layout.fragment_overview, container, false);

		// SLIDER START
		bar = (SeekBar) v.findViewById(R.id.seekBar1); // make seekbar object
		bar.setOnSeekBarChangeListener(this); // set seekbar listener.

		bar2 = (SeekBar) v.findViewById(R.id.seekBar2); // make seekbar object
		bar2.setOnSeekBarChangeListener(this); // set seekbar listener.

		// since we are using this class as the listener the class is "this"

		// make text label for progress value
		textPrice = (TextView) v.findViewById(R.id.price);
		// make text label for action
		textTime = (TextView) v.findViewById(R.id.time);

		textTotal = (TextView) v.findViewById(R.id.totalprice);
		// SLIDER END

		String totalf = totalCosts + " NOK";
		moneySaved = (TextView) v.findViewById(R.id.nok);
		moneySaved.setText(totalf);

		return v;
	}

	/*
	 * 4 - onViewCreated(View view, Bundle savedInstanceState) Called
	 * immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has
	 * returned, but before any saved state has been restored in to the view.
	 */
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		Log.d(LOG_TAG, "Called: onViewCreated");

		/*
		 * //finding button Button reg = (Button) v.findViewById(R.id.register);
		 * 
		 * //setting onclicklistened reg.setOnClickListener(new
		 * OnClickListener() { public void onClick(View v) {
		 * 
		 * //call shoeEcitDialog //showEditDialog();
		 * 
		 * //car dialog radio from main activity ((MainActivity)
		 * getActivity()).startBase(); } });
		 */

		regInterface = (LinearLayout) v.findViewById(R.id.llRegInt);
		final Button startSaving = (Button) v.findViewById(R.id.saving);
		startSaving.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// String saveText = startSaving.getText().toString();

				// if (saveText.equals("Start Parking")){
				// saveText = "End Parking";
				// startSaving.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_button_end));

				// Need to call chronometer from mainactivity
				// Start chronometer from current view
				// ((MainActivity) getActivity()).startChronometer(v);
				// }

				// else{
				// saveText = "Start Parking";

				// startSaving.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_button));

				if (regInterface.isShown()) {
					if (totalbar != 0) {
						long millichronTime = ((MainActivity) getActivity())
								.getTime();
						int seconds = (int) millichronTime / 1000;

						// formatting date
						DateFormat dateFormat = new SimpleDateFormat(
								"yyyy/MM/dd HH:mm:ss");
						// get current date time with Calendar()
						Calendar cal = Calendar.getInstance();

						String formatTime = String.format("%02d:%02d:%02d",
								seconds / 3600, (seconds % 3600) / 60,
								(seconds % 60));

						String time = "Parking time: " + formatTime;
						String date = dateFormat.format(cal.getTime());

						Log.d(LOG_TAG, "Called: Chronometer time: "
								+ formatTime);

						String fixedInfo = "Parking price: " + costbar
								+ " Parking time: " + timebar
								+ " Total costs: " + totalbar;

						String parkTime = "Parking date: " + date + "\n"
								+ fixedInfo;

						// Add time to database
						ArrayAdapter<Logging> adapter = (ArrayAdapter<Logging>) getListAdapter();
						Logging log = null;
						log = datasource.createLog(parkTime, totalbar);
						adapter.add(log);

						String toastTime = "Parking registered!" + "\n"
								+ parkTime;
						Toast.makeText(OverView.this.getActivity(), toastTime,
								Toast.LENGTH_LONG).show();

						Log.d(LOG_TAG, "totalcosts: " + totalCosts
								+ " - totalcosts+totalbar " + newcost
								+ " - totalbar; " + totalbar);

						startSaving.setText(R.string.new_savings);
					}
					((MainActivity) getActivity()).refreshOVerView();
				} else {
					totalCosts2 = totalCosts;
					regInterface.setVisibility(0);
					startSaving.setText(R.string.register_parking);
					startSaving.setBackgroundColor(getResources().getColor(
							R.color.red1));
				}
				// ((MainActivity) getActivity()).stopChronometer(v);

				// }

				// startSaving.setText(saveText);
			}
		});

	}

	// Shows the dialog
	private void showEditDialog() {
		Log.d(LOG_TAG, "Called:	showEditDialog");

		FragmentManager fm = getFragmentManager();
		EditNameDialog editNameDialog = new EditNameDialog();
		editNameDialog.show(fm, "fragment_dialog");

	}

}
