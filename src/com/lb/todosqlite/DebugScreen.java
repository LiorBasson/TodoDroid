package com.lb.todosqlite;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.model.Todo;
import com.lb.todosqlite.services.DatabaseHelper;

public class DebugScreen extends Activity
{
	//final int requestCode_TableViewer = 213;
	//final int requestCode_AddNewTag = 212;
	boolean isDebugMode = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("MainActivity", "onCreate() started");
		setContentView(R.layout.debug_view);			
				
		Button printDBBT = (Button) findViewById(R.id.printAll);
		printDBBT.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					printDB();
				}
			});	
		
		Button clearDBBT = (Button) findViewById(R.id.clrdb);
		clearDBBT.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					clearDB();
				}
			});
			
		
		Button btTodoTableV = (Button) findViewById(R.id.bt_TodoTableView);
		btTodoTableV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{								
				Intent resultIntent = getIntent();
				setResult(RESULT_OK, resultIntent);
				
				finish();	
			}
		});
		
		Log.d("DebugScreen", "onCreate() ended");
	}
		
    public void printDB()
    {
    	// prints DB (prints to LogCat)
    	Log.d("DebugScreen", "printDB() started");
    	DatabaseHelper db = new DatabaseHelper(getApplicationContext());
    	
    	List<Todo> allTodosInDB = db.getAllToDos();
    	Log.d("DebugScreen", "db.getAllToDos() sais there are " + allTodosInDB.size() + "Todos in the DB");
    	
    	
    	List<Tag> allTags = db.getAllTags();
    	Log.d("DebugScreen", "getAllTags() returned " + allTags.size() + " tags.");
    	int index = 0;
    	for (Tag tag : allTags)
    	{
    		List<Todo> allTodosPerTag = db.getAllToDosByTag(tag.getTagName());
    		Log.d("DebugScreen", "getAllToDosByTag() returned " + allTodosPerTag.size() + " Todos.");
    		for (Todo tagTodo : allTodosPerTag)
    		{
    			index++;
    			String todoInfo = "*" + index + "* Tag = {" + tag.getId() + "} " + tag.getTagName()   
    					+ " ToDo = {" + tagTodo.getId() + "} " + tagTodo.getNote() + " ";
    			
    			Log.d("DebugScreen", todoInfo);
    			// later can write info to file if needed
    		}
    	}
    	
    	// TODO: print TodoTags table? verify no Todo which wasn't "Registered" to a Tag exists?
    	
    	db.closeDB();    	
    	toastDebugInfo("printDB() was called", false);
    	Log.d("DebugScreen", "printDB() Ended");
    }
    
    public void clearDB()
    {
    	Log.d("DebugScreen", "clearDB() started");
    	
    	DatabaseHelper db = new DatabaseHelper(getApplicationContext());
    	boolean should_delete_all_tag_todos = true;
    	List<Tag> tags = db.getAllTags();
    	for (Tag tag : tags)
    	{
    		db.deleteTag(tag, should_delete_all_tag_todos);
    	}
    	
    	List<Todo> allTodosInDB = db.getAllToDos();
    	if (allTodosInDB.size() > 0)
    		Log.d("DebugScreen", "db.getAllToDos() sais there are still " + allTodosInDB.size() + "Todos in the DB");
    	    	
    	// TODO: Basically should check if also TodoTags table should be cleared
    	
    	db.clearDB();
    	
    	db.closeDB();
    	toastDebugInfo("clearDB() was called (End)", false);
    	Log.d("DebugScreen", "clearDB() Ended");
    }
        
    public void toastDebugInfo(String message, boolean IsLongDuration)
    {
    	if (isDebugMode) {
	    	int duration;
	    	if (IsLongDuration)
	    		duration = Toast.LENGTH_LONG;
	    	else duration = Toast.LENGTH_SHORT;    	
	    	
	    	Toast t = Toast.makeText(this, message, duration);
	    	t.show();
    	}
    }
	
}
