package com.lb.todosqlite;

import java.util.ArrayList;
import java.util.List;
import com.lb.todosqlite.helper.DatabaseHelper;
import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.model.Todo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class DebugScreen extends Activity
{
	
	
	Tag tag1 = null;
	Tag tag3 = null;
	long todo8_id = -1; // value signifies wasn't created yet
	int btnNewTodoCount = 0;
	final String requestToCreateNewTag = "Create New Category...";
	String tagNameLastSelected = "";
	String TagNameNewSelected = "";	
	//final int requestCode_TableViewer = 213;
	final int requestCode_AddNewTag = 212;

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug_view);
		
		Log.d("MainActivity", "onCreate() started");
		
		Button fullSAI1BT = (Button) findViewById(R.id.fullseq);
		fullSAI1BT.setOnClickListener(new OnClickListener() 
			{			
				@Override
				public void onClick(View v) 
				{
					fullSeqAllInOne();					
				}
			});
		
		Button fullSeqMethodicalBT = (Button) findViewById(R.id.fullseqmeth);
		fullSeqMethodicalBT.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					fullSeqMethods();
				}
			});		
		
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
	
		try 
		{
			spinnerHandling();
		}
		catch (Exception e)
		{
			toastt("Failed to handle Spinner creation", false);
			Log.d("MainActivity", "Failed to handle Spinner. see exception: ", e);
		}
		
		Button btNTodo = (Button) findViewById(R.id.bt_newTodo);
		btNTodo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				
				toastt("handling is in development", false);
				btnPressed_NewTodo();				
			}
		});
		
		
		
		Button btTodoTableV = (Button) findViewById(R.id.bt_TodoTableView);
		btTodoTableV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{								
//				Intent todoTableViewer = new Intent(getApplicationContext(), DebugScreen.class);
//				startActivityForResult(todoTableViewer, requestCode_TableViewer);			
				Intent resultIntent = getIntent();
				setResult(RESULT_OK, resultIntent);
				
				finish();	
			}
		});
		
		Log.d("MainActivity", "onCreate() ended");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		Log.d("MainActivity", "onCreateOptionsMenu() was called. no handling currently");
		return true;
	}
	
	public void createDBInf()
	{
		
			// Database Helper
		    DatabaseHelper db;
		    db = new DatabaseHelper(getApplicationContext());
		 
	        // Creating tags
	        tag1 = new Tag("Shopping");
	        Tag tag2 = new Tag("Important");
	        tag3 = new Tag("Watchlist");
	        Tag tag4 = new Tag("Androidhive");
	 
	        // Inserting tags in db
	        long tag1_id = db.createTag(tag1);
	        long tag2_id = db.createTag(tag2);
	        long tag3_id = db.createTag(tag3);
	        long tag4_id = db.createTag(tag4);
	 
	        Log.d("Tag Count", "Tag Count: " + db.getAllTags().size());
	 
	        // Creating ToDos
	        Todo todo1 = new Todo("iPhone 5S", 0);
	        Todo todo2 = new Todo("Galaxy Note II", 0);
	        Todo todo3 = new Todo("Whiteboard", 0);
	 
	        Todo todo4 = new Todo("Riddick", 0);
	        Todo todo5 = new Todo("Prisoners", 0);
	        Todo todo6 = new Todo("The Croods", 0);
	        Todo todo7 = new Todo("Insidious: Chapter 2", 0);
	 
	        Todo todo8 = new Todo("Don't forget to call MOM", 0);
	        Todo todo9 = new Todo("Collect money from John", 0);
	 
	        Todo todo10 = new Todo("Post new Article", 0);
	        Todo todo11 = new Todo("Take database backup", 0);
	 
	        // Inserting todos in db
	        // Inserting todos under "Shopping" Tag
	        long todo1_id = db.createToDo(todo1, new long[] { tag1_id });
	        long todo2_id = db.createToDo(todo2, new long[] { tag1_id });
	        long todo3_id = db.createToDo(todo3, new long[] { tag1_id });
	 
	        // Inserting todos under "Watchlist" Tag
	        long todo4_id = db.createToDo(todo4, new long[] { tag3_id });
	        long todo5_id = db.createToDo(todo5, new long[] { tag3_id });
	        long todo6_id = db.createToDo(todo6, new long[] { tag3_id });
	        long todo7_id = db.createToDo(todo7, new long[] { tag3_id });
	 
	        // Inserting todos under "Important" Tag
	        todo8_id = db.createToDo(todo8, new long[] { tag2_id });
	        long todo9_id = db.createToDo(todo9, new long[] { tag2_id });
	 
	        // Inserting todos under "Androidhive" Tag
	        long todo10_id = db.createToDo(todo10, new long[] { tag4_id });
	        long todo11_id = db.createToDo(todo11, new long[] { tag4_id });
	 
	        Log.e("Todo Count", "Todo count: " + db.getToDoCount());
	 
	        // "Post new Article" - assigning this under "Important" Tag
	        // Now this will have - "Androidhive" and "Important" Tags
	        db.createTodoTag(todo10_id, tag2_id);	
	        
	     // Don't forget to close database connection
	        db.closeDB();	
	}
	
	public void printAllTags()
	{
			// Database Helper
		    DatabaseHelper db;
	 
	        db = new DatabaseHelper(getApplicationContext());
		
	        // Getting all tag names
	        Log.d("Get Tags", "Getting All Tags");
	 
	        List<Tag> allTags = db.getAllTags();
	        for (Tag tag : allTags) {
	            Log.d("Tag Name", tag.getTagName());
	        }
	        
	        // Don't forget to close database connection
	        db.closeDB();	
	}

	public void printAllToDos()
	{
			// Database Helper
		    DatabaseHelper db;
	        db = new DatabaseHelper(getApplicationContext());
			 
	        // Getting all Todos
	        Log.d("Get Todos", "Getting All ToDos");
	 
	        List<Todo> allToDos = db.getAllToDos();
	        for (Todo todo : allToDos) {
	            Log.d("ToDo", todo.getNote());
	        }
	        
	        // Don't forget to close database connection
	        db.closeDB();	
	}
	
	public void printWatchlistToDos()
	{
		// Database Helper
    	DatabaseHelper db;
        db = new DatabaseHelper(getApplicationContext());
	
	
        // Getting todos under "Watchlist" tag name
        Log.d("ToDo", "Get todos under single Tag name");
 
        List<Todo> tagsWatchList = db.getAllToDosByTag(tag3.getTagName());
        for (Todo todo : tagsWatchList) {
            Log.d("ToDo Watchlist", todo.getNote());
        }
        
        // Don't forget to close database connection
        db.closeDB();	
	}
	
	public void deleteSpecificToDo()
	{
		// Database Helper
    	DatabaseHelper db;
        db = new DatabaseHelper(getApplicationContext());
        
        // Deleting a ToDo
        Log.d("Delete ToDo", "Deleting a Todo");
        Log.d("Tag Count", "Tag Count Before Deleting: " + db.getToDoCount());
 
        db.deleteToDo(todo8_id);
 
        Log.d("Tag Count", "Tag Count After Deleting: " + db.getToDoCount());
        
        // Don't forget to close database connection
        db.closeDB();	
	}
	
	public void deleteAllTodosInSpecifiedTag()
	{
		// Database Helper
    	DatabaseHelper db;
        db = new DatabaseHelper(getApplicationContext());
       
        // Deleting all Todos under "Shopping" tag
        Log.d("Tag Count",
                "Tag Count Before Deleting 'Shopping' Todos: "
                        + db.getToDoCount());
 
        db.deleteTag(tag1, true);
 
        Log.d("Tag Count",
                "Tag Count After Deleting 'Shopping' Todos: "
                        + db.getToDoCount());
        
        // Don't forget to close database connection
        db.closeDB();	
	}	
	
	public void updateSpecifiedTagName()
	{
		// Database Helper
    	DatabaseHelper db;
        db = new DatabaseHelper(getApplicationContext());
	     	 
        
        // Updating tag name
        tag3.setTagName("Movies to watch");
        db.updateTag(tag3);
        
        // Don't forget to close database connection
        db.closeDB();	
 
	}
	
	
	public void fullSeqMethods()
	{
		Log.d("MainActivity", "fullSeqMethods() Started");
		
		createDBInf();
		printAllTags();		
		printAllToDos();
		printWatchlistToDos();
		deleteSpecificToDo();
		deleteAllTodosInSpecifiedTag();
		updateSpecifiedTagName();
		
		toastt("fullSeqMethods() was called", false);
		Log.d("MainActivity", "fullSeqMethods() Ended"); 
	}
	
    public void fullSeqAllInOne()
	{
    	 Log.d("MainActivity", "fullSeqAllInOne() Started"); 
    	 
    	// Database Helper
    	DatabaseHelper db;
    	
    	db = new DatabaseHelper(getApplicationContext());
    	 
        // Creating tags
        Tag tag1 = new Tag("Shopping");
        Tag tag2 = new Tag("Important");
        Tag tag3 = new Tag("Watchlist");
        Tag tag4 = new Tag("Androidhive");
 
        // Inserting tags in db
        long tag1_id = db.createTag(tag1);
        long tag2_id = db.createTag(tag2);
        long tag3_id = db.createTag(tag3);
        long tag4_id = db.createTag(tag4);
 
        Log.d("Tag Count", "Tag Count: " + db.getAllTags().size());
 
        // Creating ToDos
        Todo todo1 = new Todo("iPhone 5S", 0);
        Todo todo2 = new Todo("Galaxy Note II", 0);
        Todo todo3 = new Todo("Whiteboard", 0);
 
        Todo todo4 = new Todo("Riddick", 0);
        Todo todo5 = new Todo("Prisoners", 0);
        Todo todo6 = new Todo("The Croods", 0);
        Todo todo7 = new Todo("Insidious: Chapter 2", 0);
 
        Todo todo8 = new Todo("Don't forget to call MOM", 0);
        Todo todo9 = new Todo("Collect money from John", 0);
 
        Todo todo10 = new Todo("Post new Article", 0);
        Todo todo11 = new Todo("Take database backup", 0);
 
        // Inserting todos in db
        // Inserting todos under "Shopping" Tag
        long todo1_id = db.createToDo(todo1, new long[] { tag1_id });
        long todo2_id = db.createToDo(todo2, new long[] { tag1_id });
        long todo3_id = db.createToDo(todo3, new long[] { tag1_id });
 
        // Inserting todos under "Watchlist" Tag
        long todo4_id = db.createToDo(todo4, new long[] { tag3_id });
        long todo5_id = db.createToDo(todo5, new long[] { tag3_id });
        long todo6_id = db.createToDo(todo6, new long[] { tag3_id });
        long todo7_id = db.createToDo(todo7, new long[] { tag3_id });
 
        // Inserting todos under "Important" Tag
        long todo8_id = db.createToDo(todo8, new long[] { tag2_id });
        long todo9_id = db.createToDo(todo9, new long[] { tag2_id });
 
        // Inserting todos under "Androidhive" Tag
        long todo10_id = db.createToDo(todo10, new long[] { tag4_id });
        long todo11_id = db.createToDo(todo11, new long[] { tag4_id });
 
        Log.e("Todo Count", "Todo count: " + db.getToDoCount());
 
        // "Post new Article" - assigning this under "Important" Tag
        // Now this will have - "Androidhive" and "Important" Tags
        db.createTodoTag(todo10_id, tag2_id);
 
        // Getting all tag names
        Log.d("Get Tags", "Getting All Tags");
 
        List<Tag> allTags = db.getAllTags();
        for (Tag tag : allTags) {
            Log.d("Tag Name", tag.getTagName());
        }
 
        // Getting all Todos
        Log.d("Get Todos", "Getting All ToDos");
 
        List<Todo> allToDos = db.getAllToDos();
        for (Todo todo : allToDos) {
            Log.d("ToDo", todo.getNote());
        }
 
        // Getting todos under "Watchlist" tag name
        Log.d("ToDo", "Get todos under single Tag name");
 
        List<Todo> tagsWatchList = db.getAllToDosByTag(tag3.getTagName());
        for (Todo todo : tagsWatchList) {
            Log.d("ToDo Watchlist", todo.getNote());
        }
 
        
        
        // Deleting a ToDo
        Log.d("Delete ToDo", "Deleting a Todo");
        Log.d("Todo Count", "Todo Count Before Deleting: " + db.getToDoCount());
        db.deleteToDo(todo8_id);
        Log.d("Todo Count", "Todo Count After Deleting: " + db.getToDoCount());
 
        // Deleting all Todos under "Shopping" tag
        Log.d("Todo Count",
                "Todo Count, db.getTagCount(), Before Deleting 'Shopping''s tag Todos: "
                        + db.getToDoCount());
 
        db.deleteTag(tag1, true);
 
        Log.d("Todo Count",
                "Todo Count, db.getToDoCount(), After Deleting 'Shopping''s tag Todos: "
                        + db.getToDoCount());
 
        // Updating tag name
        tag3.setTagName("Movies to watch");
        db.updateTag(tag3);
 
        // Don't forget to close database connection
        db.closeDB();
        
        toastt("fullSeqAllInOne() was called", false);
        Log.d("MainActivity", "fullSeqAllInOne() Ended"); 
	}
    
    public void printDB()
    {
    	// prints DB (prints to LogCat)
    	Log.d("MainActivity", "printDB() started");
    	DatabaseHelper db = new DatabaseHelper(getApplicationContext());
    	
    	List<Todo> allTodosInDB = db.getAllToDos();
    	Log.d("MainActivity", "db.getAllToDos() sais there are " + allTodosInDB.size() + "Todos in the DB");
    	
    	
    	List<Tag> allTags = db.getAllTags();
    	Log.d("MainActivity", "getAllTags() returned " + allTags.size() + " tags.");
    	int index = 0;
    	for (Tag tag : allTags)
    	{
    		List<Todo> allTodosPerTag = db.getAllToDosByTag(tag.getTagName());
    		Log.d("MainActivity", "getAllToDosByTag() returned " + allTodosPerTag.size() + " Todos.");
    		for (Todo tagTodo : allTodosPerTag)
    		{
    			index++;
    			String todoInfo = "*" + index + "* Tag = {" + tag.getId() + "} " + tag.getTagName()   
    					+ " ToDo = {" + tagTodo.getId() + "} " + tagTodo.getNote() + " ";
    			
    			Log.d("MainActivity", todoInfo);
    			// later can write info to file if needed (line by line?)
    		}
    	}
    	
    	// TODO: print TodoTags table? verify no Todo which wasn't "Registered" to a Tag exists?
    	
    	db.closeDB();    	
    	toastt("printDB() was called", false);
    	Log.d("MainActivity", "printDB() Ended");
    }
    
    public void clearDB()
    {
    	Log.d("MainActivity", "clearDB() started");
    	
    	DatabaseHelper db = new DatabaseHelper(getApplicationContext());
    	boolean should_delete_all_tag_todos = true;
    	List<Tag> tags = db.getAllTags();
    	for (Tag tag : tags)
    	{
    		db.deleteTag(tag, should_delete_all_tag_todos);
    	}
    	
    	List<Todo> allTodosInDB = db.getAllToDos();
    	if (allTodosInDB.size() > 0)
    		Log.d("MainActivity", "db.getAllToDos() sais there are still " + allTodosInDB.size() + "Todos in the DB");
    	    	
    	// TODO: Basically should check if also TodoTags table should be cleared
    	
    	db.clearDB();
    	
    	db.closeDB();
    	toastt("clearDB() was called (End)", false);
    	Log.d("MainActivity", "clearDB() Ended");
    }
    
    
    // Spinner   
	public void spinnerHandling()
    {
    	Spinner sp = (Spinner) findViewById(R.id.spinner_tag2);
    	
    	List<String> spinnerCategories = new  ArrayList<String>();
    	spinnerCategories.add("Select Todo category");  // default value which also instruct the user to choose a category
    	spinnerCategories.add("Work");
    	spinnerCategories.add("Home");
    	spinnerCategories.add("Car");
    	spinnerCategories.add(requestToCreateNewTag);

    	
    	    	
    	ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerCategories);
    	spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	sp.setAdapter(spAdapter);
    	
    	//spAdapter.remove("Select Todo category");
    	sp.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				toastt("Spinner onTouch invoked", false);
				removeSpinnerPrompt();
				return false;
			}
		});
    	
    	sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View itemSelected,
					int position, long id) {
								
				//TODO: consider later if to execute it not on selection but on item touched if event exists
				if (parent.getItemAtPosition(position).toString() == requestToCreateNewTag)
					{
						//call AddNewTag screen and class
						Intent addNewTagIntent = new Intent(itemSelected.getContext(), AddNewTag.class);
						
						startActivityForResult(addNewTagIntent, requestCode_AddNewTag);
						
						String dummyForDebbugging = "";						
					}					
				
				toastt("OnItemSelected via parent: " + parent.getItemAtPosition(position).toString() ,false);								
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				toastt("onNothingSelected() invoked" ,false);
			}
		});
    	
    	int childCount = sp.getChildCount();    	
    }
	
	public void removeSpinnerPrompt()
	{
		Spinner sp = (Spinner) findViewById(R.id.spinner_tag2);
	    ArrayAdapter<String> spAdapter = (ArrayAdapter<String>) sp.getAdapter();
	    		
		spAdapter.remove("Select Todo category");
		
	}
	
	public void addSpinnerPrompt()
	{
		Spinner sp = (Spinner) findViewById(R.id.spinner_tag2);
	    ArrayAdapter<String> spAdapter = (ArrayAdapter<String>) sp.getAdapter();
	    		
		spAdapter.add("Select Todo category");
		spAdapter.add("xSelect Todo category" + btnNewTodoCount);
	}
	
	
	
	public void updateTagNameOnCreation(String tagName)
	{
		TagNameNewSelected = tagName;
		
		Spinner sp = (Spinner) findViewById(R.id.spinner_tag2);
		ArrayAdapter<String> spAdapter = (ArrayAdapter<String>) sp.getAdapter();
		spAdapter.remove(requestToCreateNewTag);
		spAdapter.add(TagNameNewSelected);
		spAdapter.add(requestToCreateNewTag);
		
		int position = (spAdapter.getCount()-2);
		sp.setSelection(position);
		
		tagNameLastSelected = tagName;
		toastt("the new category = " + tagName, false);
	}
	
	public void restoreTagNameOnCreateCancelled()
	{
		// sets position to occurrence of tagNameLastSelected instead of selected requestToCreateNewTag value
		Spinner sp = (Spinner) findViewById(R.id.spinner_tag2);
		ArrayAdapter<String> spAdapter = (ArrayAdapter<String>) sp.getAdapter();
		int position;
		if ( tagNameLastSelected == "")
			position = 0;
		else position = spAdapter.getPosition(tagNameLastSelected);
		
		sp.setSelection(position);		
		toastt("restoring to " + tagNameLastSelected, false);
	}

	public void btnPressed_NewTodo()
	{
		btnNewTodoCount++;
		addSpinnerPrompt();
	}
    
    public void toastt(String message, boolean IsLongDuration)
    {
    	
    	int duration;
    	if (IsLongDuration)
    		duration = Toast.LENGTH_LONG;
    	else duration = Toast.LENGTH_SHORT;    	
    	
    	Toast t = Toast.makeText(this, message, duration);
    	t.show();
    }
	
	

}
