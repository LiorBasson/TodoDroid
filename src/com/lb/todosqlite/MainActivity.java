package com.lb.todosqlite;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.model.Todo;
import com.lb.todosqlite.preferences.SettingsActivity;
import com.lb.todosqlite.services.DatabaseHelper;


public class MainActivity extends Activity 
{
	// vars for additional screens handling 
	final int requestCode_AddNewToDo = 211; // the next ReqCode is in use in AddNewToDo.java: final int requestCode_AddNewTag = 212;
	final int requestCode_DeubgScr = 213;
	final int requestCode_ViewToDo = 214;
	final int requestCode_EditToDo = 215;		
	// vars for later color theme use  // TODO: check if it is a graphics thumb rule that for Hex color code (#XxYyZz) where Yy is below 66 then considered dark and above 66 (>99) then considered light.  
	String colorCodeForTableBG = "#000000";
	String colorCodeForTableText = "#FFFFFF";
	String colorCodeForTRInFocus = "#003366";
	String colorCodeForHintText = "#CCCCCC";
	String colorCodeForButtonsBG = "#80003399"; // Temporarily not in use due to the fact that button's 3D effect is lost
	String colorCodeForButtonsTxt = "#FFFFFF";
	String colorCodeForTableHeaderBG = "#666666";
	String colorCodeForTableHeaderTxt = "#CCCCCC";
	// general vars
	final String spinnerDefaultValue = "Select Todo category";
	final String defaultInternalTagName = "None";
	final String sharedPrefUserPrefFileName = "com.lb.todosqlite_preferences";
	// vars for debug
	boolean isDebugMode = false; // TODO: Change back to false when finished debugging
	int searchCount = 0;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todos_table);		
		
		fillUpTableFromDB();		
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.todos_table_screen_linlay);
		ll.setBackgroundColor(Color.parseColor(colorCodeForTableBG));
		
		Button bt_search = (Button) findViewById(R.id.bt_Search);
		//bt_search.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		bt_search.setTextColor(Color.parseColor(colorCodeForButtonsTxt));
		bt_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				updateThemeColors();
			}
		});
		
		Button bt_addToDo = (Button) findViewById(R.id.bt_AddTodo);
		//bt_addToDo.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		bt_addToDo.setTextColor(Color.parseColor(colorCodeForButtonsTxt));
		bt_addToDo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent todoTableViewer = new Intent(getApplicationContext(), AddNewToDo.class);
				startActivityForResult(todoTableViewer, requestCode_AddNewToDo);	
				
			}
		});
		
		
		Button bt_DebugScreen = (Button) findViewById(R.id.bt_ToDebugScr);
		//bt_backToMain.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		bt_DebugScreen.setTextColor(Color.parseColor(colorCodeForButtonsTxt));
		bt_DebugScreen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent debugScreen = new Intent(getApplicationContext(), DebugScreen.class);
				Intent getIntent = getIntent();
				debugScreen.putExtra("req_code_dbg_scr", requestCode_DeubgScr); 
				//debugScreen.putExtra("com.lb.todosqlite.debugscreen.requestCode_DeubgScr", requestCode_DeubgScr); 
				getIntent.putExtra("com.lb.todosqlite.MainActivity.requestCode_DeubgScr", requestCode_DeubgScr);
				int debugTodoID = 1;
				debugScreen.putExtra("dbg_todo_id", debugTodoID); 
				//debugScreen.putExtra("com.lb.todosqlite.debugscreen.debugTodoID", debugTodoID); 
				getIntent.putExtra("com.lb.todosqlite.MainActivity.debugTodoID", debugTodoID);
				startActivity(debugScreen);
				//startActivityForResult(debugScreen, requestCode_DeubgScr);		
			}
		});
		
		EditText et_search = (EditText) findViewById(R.id.et_Search);
		et_search.setTextColor(Color.parseColor(colorCodeForTableText));
		et_search.setHintTextColor(Color.parseColor(colorCodeForHintText));
		
		// TODO: check for duplication in manifest?
		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		// Preferences handling
		//PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		
		SharedPreferences sp = getSharedPreferences(sharedPrefUserPrefFileName, 0);		
		
		isDebugMode = sp.getBoolean("pref_key_debug_mode", false);
		
		if (!(isDebugMode))
			bt_DebugScreen.setVisibility(View.INVISIBLE);
		
		// TODO: theme impl		
		// updateThemeColors();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// TODO: if relevant to this class, register to preference changes, otherwise remove this overriden method
		//getPreferenceScreen()

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// TODO Auto-generated method stub
		// TODO: if relevant to this class, register to preference changes, otherwise remove this overriden method


	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    
	    MenuItem mi_about = menu.findItem(R.id.action_about);
		mi_about.setOnMenuItemClickListener(new OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem item) 
					{																
						launchAboutDialog();
						return false;
					}
		});
		
		MenuItem mi_preferences = menu.findItem(R.id.action_settings);
		mi_preferences.setOnMenuItemClickListener(new OnMenuItemClickListener() 
		{
			@Override
			public boolean onMenuItemClick(MenuItem item) 
			{
				launchPreferences();
				return false;
			}
		});
	    
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (resultCode == RESULT_OK)
			switch (requestCode)
			{
				case requestCode_AddNewToDo:
				{
					Bundle bd = data.getExtras();
					if ((bd.containsKey("com.lb.todosqlite.addnewtag.isCancelPressed")) && !(bd.getBoolean("com.lb.todosqlite.addnewtag.isCancelPressed")))
					{
						String todoTitle = bd.getString("com.lb.todosqlite.addnewtodo.todoTitle");	
						String categorySelected = bd.getString("com.lb.todosqlite.addnewtodo.categorySelected");  
						String dueDate = bd.getString("com.lb.todosqlite.addnewtodo.dueDate");  
						
						if (categorySelected.equals(spinnerDefaultValue))
							categorySelected = defaultInternalTagName;
						
						createToDo(todoTitle, categorySelected, dueDate);
						clearTableData();
						fillUpTableFromDB();
					}
					toastDebugInfo("returned from AddNewToDo Screen", false);
					break;							
				}
				case requestCode_DeubgScr:
				{
					toastDebugInfo("returned from Debug Screen", false);
					break;
				}
				case requestCode_EditToDo:
				{
					clearTableData();
					fillUpTableFromDB();
					break;
				}
				case 555:
				{
					toastDebugInfo("returned from preferences Screen. resultCode = " + resultCode, false);
				}
				
			}
		else toastDebugInfo("returned with !(Result_OK)", false);
	}
	
	public void updateThemeColors()
	{
		// get updated values from prefsett
		colorCodeForTableBG = "#000000";
		colorCodeForTableText = "#FFFFFF";
		colorCodeForTRInFocus = "#003366";
		colorCodeForHintText = "#CCCCCC";
		colorCodeForButtonsBG = "#80003399"; // Temporarily not in use due to the fact that button's 3D effect is lost
		colorCodeForButtonsTxt = "#FFFFFF";
		colorCodeForTableHeaderBG = "#666666";
		colorCodeForTableHeaderTxt = "#CCCCCC";
		// update screen elements with current colors / theme
		
	}

	public void fillUpTableFromDB()
	{
		try {
			TableLayout todosTable = (TableLayout) findViewById(R.id.table_ToDos);
			
			int i = 0;
			TableRow tr = (TableRow) todosTable.getChildAt(i);
			int columns = tr.getChildCount();
			
			DatabaseHelper db = new DatabaseHelper(getApplicationContext());
			List<Todo> todos = db.getAllToDos();
			
			if (todos.size()==0)
				{
					TableRow welcome = new TableRow(getApplicationContext());
					
					ImageView iv = new ImageView(getApplicationContext());
					iv.setBackgroundResource(R.drawable.hooray2);
										
					welcome.addView(iv);					
					todosTable.addView(welcome, 1);					
				}
			
			
			for (Todo todo : todos)
			{
			
				TableRow ntr = new TableRow(getApplicationContext());
				ntr.setId((int) todo.getId());
				
					for (int index = 0; index < columns; index++) 
					{
						TextView ntv = new TextView(getApplicationContext());
						ntv.setTextColor(Color.parseColor(colorCodeForTableText));
						ntv.setText("Text" + index);
						
						switch (index)
						{
							case 0:
							{
								ntv.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.3f));
								ntv.setText(todo.getNote());	
								break;
							}
							case 1:
							{
								List<Tag> categories = db.getTagsByToDo(todo.getId());
								String category = "";
								for (Tag ctg : categories)
									{category = category + "[" + ctg.getTagName() + "] ";}
									
								ntv.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.15f));
								ntv.setText(category);								
								break;
							}
							case 2:
							{
								ntv.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.4f));
								ntv.setText(todo.getDueDate());		
								break;
							}
							case 3:
							{
								ntv.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.15f));
								if (todo.getStatus()== 1)
									ntv.setText("[X]");
								else ntv.setText("[   ]");								
								break;
							}
						}			
						ntr.addView(ntv);						
					}
				ntr.setFocusableInTouchMode(true);
				ntr.setBackgroundColor(Color.parseColor(colorCodeForTableBG));
				
				ntr.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) 
					{						
						v.requestFocus(); 																		
						toastDebugInfo("TR onLongClick() for todo: " + v.getId(), true);						
						return false;
					}
				});
				
				ntr.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus)
							v.setBackgroundColor(Color.parseColor(colorCodeForTRInFocus));
						else v.setBackgroundColor(Color.parseColor(colorCodeForTableBG));						
					}
				});
				
				
				ntr.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						
						MenuItem viewTodo =  null;
						MenuItem editTodo =  null;
						MenuItem checkTodo =  null;
						MenuItem deleteTodo =  null;
						int rowTodoID = -1;					
						
						if (!(menu == null) && !(v == null))
						{
							rowTodoID = v.getId();
							Todo selectedTodo = getTodoByID(rowTodoID);
							String contMenuTitle = selectedTodo.getNote();
							int endSubString = 0;
							if (contMenuTitle.length() < 20)
								endSubString = contMenuTitle.length();
							else endSubString = 19;
							contMenuTitle = contMenuTitle.substring(0, endSubString) + "...";
							
							
							menu.setHeaderTitle(contMenuTitle);
							viewTodo = menu.add(rowTodoID, 1, Menu.NONE, "View");
							editTodo =  menu.add(rowTodoID, 2, Menu.NONE, "Edit");
							checkTodo =  menu.add(rowTodoID, 3, Menu.NONE, "Un/Check As Completed");
							deleteTodo =  menu.add(rowTodoID, 4, Menu.NONE, "Delete");
							
														
							// View selected row
							viewTodo.setOnMenuItemClickListener(new OnMenuItemClickListener() {
								
								@Override
								public boolean onMenuItemClick(MenuItem item) {
									
									int todoID = item.getGroupId();
									int id = item.getItemId();
									
									launchViewTodo(todoID);
									
									toastDebugInfo("TR onMenuItemClick() was invoked for menuItemID= " + id + " GroupID= " + todoID, false);
									return false;
								}
							});
							
							// Edit selected row
							editTodo.setOnMenuItemClickListener(new OnMenuItemClickListener() {
								
								@Override
								public boolean onMenuItemClick(MenuItem item) {
									int id = item.getItemId();									
									int idg = item.getGroupId();									
									launchEditTodo(idg);
									
									toastDebugInfo("TR onMenuItemClick() was invoked for menuItemID= " + id + " GroupID= " + idg, false);
									return false;
								}
							});

							// Check as completed selected row
							checkTodo.setOnMenuItemClickListener(new OnMenuItemClickListener() {
								
								@Override
								public boolean onMenuItemClick(MenuItem item) {
									int id = item.getItemId();
									int idg = item.getGroupId(); 									
									toggleTodoStatus(idg);
									
									toastDebugInfo("TR onMenuItemClick() was invoked for menuItemID= " + id + " GroupID= " + idg, false);
									return false;
								}
							});
							
							// Delete selected row
							deleteTodo.setOnMenuItemClickListener(new OnMenuItemClickListener() {
								
								@Override
								public boolean onMenuItemClick(MenuItem item) {
									int id = item.getItemId();
									int idg = item.getGroupId();
									deleteTodo(idg);
									
									toastDebugInfo("TR onMenuItemClick() was invoked for menuItemID= " + id + " GroupID= " + idg, false);
									return false;
								}
							});
						}
						
						toastDebugInfo("TR onCreateContextMenu() was invoked for vID= " + rowTodoID, false);
					}
				});
				
				todosTable.addView(ntr,1);
			}
			db.closeDB();
			
		} catch (Exception e) {
			Log.d("MainActivity", "fillUpTableOnCreation() caught an exception: ", e);
			toastDebugInfo("fillUpTableOnCreation() caught an exception", false);
		}
	}
	
	public void clearTableData()
	{
		TableLayout todosTable = (TableLayout) findViewById(R.id.table_ToDos);
		todosTable.removeViews(1, todosTable.getChildCount()-1);
	}
	
	public void createToDo(String todoTitle, String categorySelected, String dueDate)
	{
		int defaultTodoStatus = 0;
		// updates DB with new Todo
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
				
		long tagID = 0;		
		// TODO: ?exclude cases of default Spinner items {spinnerDefaultValue and requestToCreateNewTag} handle categoryOnspinnerDefaultValue? - apparently handling correctly now 
		if (!(db.getAllTags().contains(categorySelected)))
		{
			Tag tag = new Tag(categorySelected);
			tagID = db.createTag(tag);
		}
		else 
		{
			List<Tag> tags = db.getAllTags();
			for (Tag tag : tags)
			{
				if ( categorySelected.equals(tag.getTagName()) )
					tagID  = tag.getId();					
			}				 
		}		
		Todo todo = new Todo(todoTitle, defaultTodoStatus);
		todo.setDueDate(dueDate);  // TODO: consider adding a date format validator
		db.createToDo(todo, new long[] {tagID});
				
		db.closeDB();
	}
				
	// Get Todo  (int todoID)
	public Todo getTodoByID(int todoID)
	{
		Todo todo = null;
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		todo = db.getTodo(todoID);		
		db.closeDB();		
		return todo;		
	}
	
	public List<Tag> getTagByTodoID(int todoID)
	{
		List<Tag> tag = null;
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		tag = db.getTagsByToDo(todoID);
		db.closeDB();		
		return tag;
	}
	
	// TODO: Update Todo and its tags (Todo updatedTodo, ?Tag updatedTag?)
	public void updateTodoAndTags (Todo updatedTodo, List<Tag> updatedTags)
	{
		//launch edit todo view
		
		//update table row (apparently with clearTableRows() and fillTableOCreation()
		
		toastDebugInfo("Will updateTodoAndTags", false);
	}
	
	// Un/Check Todo as completed (int todoID)
	public void toggleTodoStatus(int todoID)
	{
		// '1' represents Completed while '0' represents NotCompleted
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		Todo todo = db.getTodo(todoID);
		if (todo.getStatus() == 1)
			todo.setStatus(0);
		else todo.setStatus(1);
		
		db.updateToDo(todo);
		db.closeDB();
		
		clearTableData();
		fillUpTableFromDB();		
	}
	
	// Delete todo (int todoID)  include its tag if not in use elsewhere
	public void deleteTodo(int todoID)
	{
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		db.deleteToDo(todoID, true); 
		db.closeDB();
		
		try
		{	
			TableLayout todosTable = (TableLayout) findViewById(R.id.table_ToDos);
			
			int childCount = todosTable.getChildCount();
			for (int index=1; index < childCount; index++)
			{
				TableRow tr = (TableRow) todosTable.getChildAt(index);
				if (tr.getId() == todoID)
				{
					todosTable.removeViewAt(index);
					break;
				}
			}		
		 }
		catch
		(Exception e)
		{
			Log.d("MainActivity","deleteTodo() caught an exception", e);
		}
		toastDebugInfo("Will delete the todo", false);		
	}
	
	public void launchViewTodo(int todoID)
	{
		SharedPreferences sp =  getApplication().getSharedPreferences("viewTodo", 0);
	    Editor ed =  sp.edit();
	    ed.putInt("userReqCode", requestCode_ViewToDo);
        ed.putInt("todoID", todoID);
	    ed.putString("todo_dummy", "Dummy text: it might be better to use this instead of the default public void onClick(View v). should be checked later");
	    ed.commit();
	   
	    Intent viewTodoIntent = new Intent(getApplicationContext(), ViewTodo.class);	        
	    viewTodoIntent.putExtra("todoID", todoID);
	    startActivityForResult(viewTodoIntent, requestCode_ViewToDo);
		
		toastDebugInfo("launchViewTodo() - Will inflate a view which will only display the todo", false);
	}
	
	public void launchEditTodo(int todoID)
	{
		SharedPreferences sp =  getApplication().getSharedPreferences("editTodo", 0);
	    Editor ed =  sp.edit();
	    ed.putInt("userReqCode", requestCode_EditToDo);
        ed.putInt("todoID", todoID);
	    ed.commit();
	   
	    Intent viewTodoIntent = new Intent(getApplicationContext(), EditTodo.class);	        
	    viewTodoIntent.putExtra("todoID", todoID);
	    startActivityForResult(viewTodoIntent, requestCode_EditToDo);
		toastDebugInfo("launchEditTodo() - Will inflate a view which alows editing the todo", false);
	}
	
	public void launchAboutDialog()
	{
		// texts, links and version number are all hardcoded into "dialogContent" in strings.xml 
		try
		{		
			final TextView textView = new TextView(this);
			textView.setText(R.string.dialogContent);
			textView.setMovementMethod(LinkMovementMethod.getInstance()); 
			   
			final AlertDialog.Builder aDBuilder = new AlertDialog.Builder(this);
			aDBuilder.setTitle("About!");
			aDBuilder.setIcon(R.drawable.ic_logo);
			aDBuilder.setView(textView);
			aDBuilder.setPositiveButton("OK", null);
			aDBuilder.create().show();							
		}
		catch (Exception e)
		{
			Log.d("MainActivity", "launchAboutDialog() throws the next exception: ", e);
		}		
	}
	
	public void launchPreferences()
	{
		try 
		{
			Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivityForResult(intent, 555);			
		} 
		catch (Exception e) 
		{
			Log.d("MainActivity", "launchPreferences() throws the next exception: ", e);
		}
	}
		
	@Override
	public void onBackPressed() 
	{
		userVerificationToExit();
	}
		
	private void userVerificationToExit()
	{
		final AlertDialog.Builder aDBuilder = new AlertDialog.Builder(this);
		try
		{	
			aDBuilder.setMessage("Are you sure you want to exit?");
			aDBuilder.setTitle("Exit!");
			
			aDBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					// temporary solution or legit? (to call finish() from this handler)
					finish();					
				}
			});	
			aDBuilder.setNeutralButton("Cancel", null);
			
			aDBuilder.create().show();							
		}
		catch (Exception e)
		{
			Log.d("DialogBox_ExitApp", "DialogBox throws the next exception: ", e);
		}		
	}
		
	public void toastDebugInfo(String message, boolean IsLongDuration)
    {    	
    	if (isDebugMode) {
			int duration;
			if (IsLongDuration)
				duration = Toast.LENGTH_LONG;
			else
				duration = Toast.LENGTH_SHORT;
			Toast t = Toast.makeText(this, message, duration);
			t.show();
		}
    }
	
}
