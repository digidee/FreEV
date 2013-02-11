package com.example.freespot.database;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.freespot.R;


public class TestDatabaseActivity extends ListActivity {
  private CommentsDataSource datasource;
  Button win;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.databasemain);

    datasource = new CommentsDataSource(this);
    datasource.open();

    List<Comment> values = datasource.getAllComments();

    // Use the SimpleCursorAdapter to show the
    // elements in a ListView
    ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
        R.layout.rowlayout, R.id.label, values);
    setListAdapter(adapter);
  }

  // Will be called via the onClick attribute
  // of the buttons in main.xml
  public void onClick(View view) {
    @SuppressWarnings("unchecked")
    ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
    Comment comment = null;
    
    
    switch (view.getId()) {
    case R.id.add:
      //String[] comments = new String[] { "Cool", "Very nice", "Hate it" };
      //int nextInt = new Random().nextInt(3);
      
      TextView newexc = (TextView) findViewById(R.id.newtime);
      String excer = newexc.getText().toString();
      
      if (!excer.contentEquals("")){
    	// Save the new comment to the database
    	comment = datasource.createComment(excer);
      	adapter.add(comment);  
      }
      break;
    case R.id.delete:
      if (getListAdapter().getCount() > 0) {
        comment = (Comment) getListAdapter().getItem(0);
        datasource.deleteComment(comment);
        adapter.remove(comment);
      }
      break;
    }
    adapter.notifyDataSetChanged();
    

    
  }
 
  
  
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);


	}
	


  @Override
  protected void onResume() {
    datasource.open();
    super.onResume();
  }

  @Override
  protected void onPause() {
    datasource.close();
    super.onPause();
  }

} 