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
import android.widget.Toast;

import com.example.freespot.database.Comment;
import com.example.freespot.database.CommentsDataSource;

public class ParkingLog extends ListFragment {
	
	private String[] values;
	
	private CommentsDataSource datasource;
	

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
/*
    
	values = new String[] { "Test", "Test" };
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        R.layout.rowlayout, R.id.label, values);    
    
    setListAdapter(adapter);
    */
    
    
	   datasource = new CommentsDataSource(getActivity());
	   datasource.open();

	    List<Comment> values = datasource.getAllComments();

	    // Use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(getActivity(),
	        R.layout.rowlayout, R.id.label, values);
	    setListAdapter(adapter);



  }


  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		View v = inflater.inflate(R.layout.fragment_parkinglog, container, false);
	
	       //finding button
	       Button bselect = (Button) v.findViewById(R.id.delete);
	       
	       //setting onclicklistened
	       bselect.setOnClickListener(new OnClickListener() {
	           public void onClick(View v) {
	        	   
	        	ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();

	       	    Comment comment = null;
	       	    
	       	    
	       	 /*comment = datasource.createComment("esfsefes");
	          	adapter.add(comment);  */
	        	
	        	
	        	if (getListAdapter().getCount() > 0) {
	                comment = (Comment) getListAdapter().getItem(0);
	                datasource.deleteComment(comment);
	                adapter.remove(comment);
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
		Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

	    
	}

} 