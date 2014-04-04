package com.lb.todosqlite;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils.TruncateAt;
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
	final int requestCode_SettingsActivity = 216;
	// vars for color theme use   
	int colorCodeForTableBG = -16777216;
	int colorCodeForTableText = -1;
	int colorCodeForTRInFocus = -13085737;
	int colorCodeForHintText = -1644826;
	//int colorCodeForButtonsBG = -11119018; // Temporarily not in use due to the fact that button's 3D effect is lost
	int  colorCodeForButtonsTxt = -1;  
	int colorCodeForTableHeaderBG = -6645094;
	int colorCodeForTableHeaderTxt = -1644826;
	// general vars
	final String spinnerDefaultValue = "Select Todo category";
	final String defaultInternalTagName = "None";
	final String sharedPrefUserPrefFileName = "com.lb.todosqlite_preferences";
	// vars for debug
	boolean isDebugMode = false;
	int searchCount = 0;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todos_table);		
		
		fillUpTableFromDB();		
		
		
		Button bt_search = (Button) findViewById(R.id.bt_Search);
		//bt_search.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		//bt_search.setTextColor(colorCodeForButtonsTxt);
		bt_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				updateElementsWithThemeColors(); // here for debugging
			}
		});
		
		Button bt_addToDo = (Button) findViewById(R.id.bt_AddTodo);
		//bt_addToDo.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		//bt_addToDo.setTextColor(colorCodeForButtonsTxt);
		bt_addToDo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent todoTableViewer = new Intent(getApplicationContext(), AddNewToDo.class);
				startActivityForResult(todoTableViewer, requestCode_AddNewToDo);	
				
			}
		});
		
		
		Button bt_DebugScreen = (Button) findViewById(R.id.bt_ToDebugScr);
		//bt_backToMain.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		//bt_DebugScreen.setTextColor(colorCodeForButtonsTxt);
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
				
		// Preferences handling 
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);			
				
		getThemeColorsFromPreferences(); 
		updateElementsWithThemeColors();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// TODO: if relevant to this class, register to OnPreferenceChanged event
		
		// TODO: add a mechanism combined with OnPreferenceChanged event to update condition boolean (relevant to isDebugMode too)
		if (true) 
		{
			getThemeColorsFromPreferences(); 
			updateElementsWithThemeColors();
		}
		
		if (true)
		{
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());	
			isDebugMode = sp.getBoolean("pref_key_debug_mode", false);
			Button bt_DebugScreen = (Button) findViewById(R.id.bt_ToDebugScr);
			if (!(isDebugMode))
				bt_DebugScreen.setVisibility(View.INVISIBLE);
			else bt_DebugScreen.setVisibility(View.VISIBLE);
		}			
		
		toastDebugInfo("MainActivity.onResume() invoked", true);		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
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
	
	private void fillUpTableFromDB()
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
						ntv.setSingleLine(true);
						ntv.setEllipsize(TruncateAt.END);
						ntv.setPadding(2, 10, 2, 10);
						
						switch (index)
						{
							case 0:
							{
								ntv.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 0.40f));
								ntv.setText(todo.getNote());	
								break;
							}
							case 1:
							{
								List<Tag> categories = db.getTagsByToDo(todo.getId());
								String category = "";
								for (Tag ctg : categories)
									{
										category = ctg.getTagName(); // category + "[" + ctg.getTagName() + "] ";
									}
									
								ntv.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 0.15f));
								ntv.setText(category);								
								break;
							}
							case 2:
							{
								ntv.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 0.35f));
								ntv.setText(todo.getDueDate());		
								break;
							}
							case 3:
							{
								ntv.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 0.10f));
								if (todo.getStatus()== 1)
									ntv.setText("[X]");
								else ntv.setText("[   ]");								
								break;
							}
							default:
							{
								ntv.setText("Text" + index);
							}
						}			
						ntr.addView(ntv);						
					}
				ntr.setFocusableInTouchMode(true);
				
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
							v.setBackgroundColor(colorCodeForTRInFocus);
						else v.setBackgroundColor(colorCodeForTableBG);						
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
		updateElementsWithThemeColors_Table();
	}
	
	private void clearTableData()
	{
		TableLayout todosTable = (TableLayout) findViewById(R.id.table_ToDos);
		todosTable.removeViews(1, todosTable.getChildCount()-1);
	}
	
	private void createToDo(String todoTitle, String categorySelected, String dueDate)
	{
		int defaultTodoStatus = 0;
		// updates DB with new Todo
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
				
		long tagID = 0;		
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
		todo.setDueDate(dueDate); 
		db.createToDo(todo, new long[] {tagID});
				
		db.closeDB();
	}
				
	// Get Todo  (int todoID)
	private Todo getTodoByID(int todoID)
	{
		Todo todo = null;
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		todo = db.getTodo(todoID);		
		db.closeDB();		
		return todo;		
	}
	
	// Un/Check Todo as completed (int todoID)
	private void toggleTodoStatus(int todoID)
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
	private void deleteTodo(int todoID)
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
	
	private void launchViewTodo(int todoID)
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
	
	private void launchEditTodo(int todoID)
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
	
	private void launchAboutDialog()
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
	
	private void launchPreferences()
	{
		try 
		{
			Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivityForResult(intent, requestCode_SettingsActivity);			
		} 
		catch (Exception e) 
		{
			Log.d("MainActivity", "launchPreferences() throws the next exception: ", e);
		}
	}
	
	private void getThemeColorsFromPreferences()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());		
		
		colorCodeForTableBG = sp.getInt("colorCodeForTableBG", colorCodeForTableBG); 
		colorCodeForTableText = sp.getInt("colorCodeForTableText", colorCodeForTableText);
		colorCodeForTRInFocus = sp.getInt("colorCodeForTRInFocus", colorCodeForTRInFocus); 
		colorCodeForHintText = sp.getInt("colorCodeForHintText", colorCodeForHintText);
//		//colorCodeForButtonsBG = "#80003399"; // Temporarily not in use due to the fact that button's 3D effect is lost
		colorCodeForButtonsTxt = sp.getInt("colorCodeForButtonsTxt", colorCodeForButtonsTxt); 
		colorCodeForTableHeaderBG = sp.getInt("colorCodeForTableHeaderBG", colorCodeForTableHeaderBG);
		colorCodeForTableHeaderTxt = sp.getInt("colorCodeForTableHeaderTxt", colorCodeForTableHeaderTxt);
	}
	
	private void updateElementsWithThemeColors()
	{
		// Buttons
		Button bt_search = (Button) findViewById(R.id.bt_Search);
		//bt_search.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		bt_search.setTextColor(colorCodeForButtonsTxt);
				
		Button bt_addToDo = (Button) findViewById(R.id.bt_AddTodo);
		//bt_addToDo.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		bt_addToDo.setTextColor(colorCodeForButtonsTxt);		
		
		Button bt_DebugScreen = (Button) findViewById(R.id.bt_ToDebugScr);
		//bt_backToMain.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		bt_DebugScreen.setTextColor(colorCodeForButtonsTxt); 
		
		// Table - in a separate method since need to be called by fillUpTableFromDB()
		updateElementsWithThemeColors_Table();
		
		// backgrounds
		LinearLayout screenLayout = (LinearLayout) findViewById(R.id.todos_table_screen_linlay);
		screenLayout.setBackgroundColor(colorCodeForTableBG);
		
		// others
		EditText et_search = (EditText) findViewById(R.id.et_Search);
		et_search.setTextColor(colorCodeForTableText);
		et_search.setHintTextColor(colorCodeForHintText);		
	}
	
	private void updateElementsWithThemeColors_Table() 
	{
		TableLayout todosTable = (TableLayout) findViewById(R.id.table_ToDos);
		todosTable.setBackgroundColor(colorCodeForTableBG); // basically table takes layout BG and also children handled below,
															// but should I invoke just in case?

		for (int row = 0; row < todosTable.getChildCount(); row++) 
		{
			TableRow tr = (TableRow) todosTable.getChildAt(row);
			if (!(tr.getChildCount()==1))	// 	tr.getChildCount()==1 when there are no tasks and displaying an welcome image		
				for (int column = 0; column < tr.getChildCount(); column++) 
				{
					TextView tv = (TextView) tr.getChildAt(column);
					if (!(row == 0)) 
					{
						tv.setTextColor(colorCodeForTableText);
						tr.setBackgroundColor(colorCodeForTableBG);
					} else {
						tv.setBackgroundColor(colorCodeForTableHeaderBG);
						tv.setTextColor(colorCodeForTableHeaderTxt);
					}
				}
		}
	}

	@Override
	public void onBackPressed() {
		userVerificationToExit();
	}

	private void userVerificationToExit() {
		final AlertDialog.Builder aDBuilder = new AlertDialog.Builder(this);
		try {
			aDBuilder.setMessage("Are you sure you want to exit?");
			aDBuilder.setTitle("Exit!");

			aDBuilder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// temporary solution or legit? (to call finish()
							// from this handler)
							finish();
						}
					});
			aDBuilder.setNeutralButton("Cancel", null);

			aDBuilder.create().show();
		} catch (Exception e) {
			Log.d("DialogBox_ExitApp", "DialogBox throws the next exception: ",
					e);
		}
	}

	private void toastDebugInfo(String message, boolean IsLongDuration) {
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
