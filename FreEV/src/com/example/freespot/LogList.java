package com.example.freespot;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.freespot.database.Logging;
import com.example.freespot.database.LoggingDataSource;

public class LogList extends ListFragment {

	private int idd;

	private LoggingDataSource datasource;

	private static final String LOG_TAG = "FreEV_LogList";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * 
		 * values = new String[] { "Test", "Test" }; ArrayAdapter<String>
		 * adapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout,
		 * R.id.label, values);
		 * 
		 * setListAdapter(adapter);
		 */

		datasource = new LoggingDataSource(getActivity());
		datasource.open();

		List<Logging> values = datasource.getAllLogs();

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<Logging> adapter = new ArrayAdapter<Logging>(
				getActivity(), R.layout.rowlayout, R.id.label, values);
		setListAdapter(adapter);

		for (Logging logs : values) {
			

			
			String log = "Id: " + logs.getId() + ", Name: " + logs.getType()
					+ ", Total Costs: " + logs.getTotalCosts();
			// Writing to log
			Log.d(LOG_TAG, "Log: " + log);
			

		}
		
		idd = (int) values.get(0).getId();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_parkinglog, container,
				false);

		// finding button
		Button bselect = (Button) v.findViewById(R.id.delete);

		// setting onclicklistened
		bselect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				ArrayAdapter<Logging> adapter = (ArrayAdapter<Logging>) getListAdapter();

				Logging log = null;

				if (getListAdapter().getCount() > 0) {
					log = (Logging) getListAdapter().getItem(0);
					datasource.deleteLog(log);
					adapter.remove(log);
				}

			}
		});

		return v;

	}

	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		/**
		 * Toast message will be shown when you click any list element
		 */
		Toast.makeText(getActivity(),
				getListView().getItemAtPosition(position).toString(),
				Toast.LENGTH_SHORT).show();

		Log.d(LOG_TAG, "position: " + position + "ID:" +idd);
		Intent i = new Intent(getActivity(), LogActivity.class);
		i.putExtra("pos", position+idd);
		startActivity(i);

	}

}