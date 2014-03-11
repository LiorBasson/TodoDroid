package com.lb.todosqlite;

import java.util.List;
import java.util.concurrent.Exchanger;

import com.lb.todosqlite.helper.DatabaseHelper;
import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.model.Todo;

import android.os.Build;
import android.os.Bundle;
import android.R.color;
import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewPropertyAnimator;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;


public class MainActivity extends Activity 
{
	// vars for additional screens handling 
	final int requestCode_AddNewToDo = 211; // the next ReqCode is in use in AddNewToDo.java: final int requestCode_AddNewTag = 212;
	final int requestCode_DeubgScr = 213;
	final int requestCode_ViewToDo = 214;
	final int requestCode_EditToDo = 215;		
	// vars for later color theme use
	final String colorCodeForTableBG = "#000000";
	final String colorCodeForTableText = "#FFFFFF";
	final String colorCodeForTRInFocus = "#003366";
	final String colorCodeForHintText = "#CCCCCC";
	final String colorCodeForButtonsBG = "#80003399"; // Temporarily not in use due to the fact that button's 3D effect is lost
	final String colorCodeForButtonsTxt = "#FFFFFF";
	final String colorCodeForTableHeaderBG = "#666666";
	final String colorCodeForTableHeaderTxt = "#CCCCCC";
	// general vars
	final String spinnerDefaultValue = "Select Todo category";
	final String defaultInternalTagName = "None";
	// vars for debug
	int searchCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.todos_table);
		
		// TODO: handle the screen rotation recreating activity screen - currently workaround in manifest
		
		fillUpTableOnCreation();	
		
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.todos_table_screen_linlay);
		ll.setBackgroundColor(Color.parseColor(colorCodeForTableBG));
		
		Button bt_search = (Button) findViewById(R.id.bt_Search);
		//bt_search.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		bt_search.setTextColor(Color.parseColor(colorCodeForButtonsTxt));
		bt_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
												
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
		
		
		Button bt_backToMain = (Button) findViewById(R.id.bt_ToDebugScr);
		//bt_backToMain.setBackgroundColor(Color.parseColor(colorCodeForButtonsBG));
		bt_backToMain.setTextColor(Color.parseColor(colorCodeForButtonsTxt));
		bt_backToMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent debugScreen = new Intent(getApplicationContext(), DebugScreen.class);
				Intent getIntent = getIntent();
				debugScreen.putExtra("com.lb.todosqlite.debugscreen.requestCode_DeubgScr", requestCode_DeubgScr); 
				getIntent.putExtra("com.lb.todosqlite.MainActivity.requestCode_DeubgScr", requestCode_DeubgScr);
				int debugTodoID = 1;
				debugScreen.putExtra("com.lb.todosqlite.debugscreen.debugTodoID", debugTodoID); 
				getIntent.putExtra("com.lb.todosqlite.MainActivity.debugTodoID", debugTodoID);
				startActivity(debugScreen);
				//startActivityForResult(debugScreen, requestCode_DeubgScr);		
			}
		});
		
		EditText et_search = (EditText) findViewById(R.id.et_Search);
		et_search.setTextColor(Color.parseColor(colorCodeForTableText));
		et_search.setHintTextColor(Color.parseColor(colorCodeForHintText));
		
		
		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		int m_resultCode = 0;
		if (resultCode == RESULT_OK)
			switch (requestCode)
			{
				case requestCode_AddNewToDo:
				{
					Bundle bd = data.getExtras();
					if ((bd.containsKey("com.lb.todosqlite.addnewtag.isCancelPressed")) && !(bd.getBoolean("com.lb.todosqlite.addnewtag.isCancelPressed")))
					{
						String todoTitle = bd.getString("com.lb.todosqlite.addnewtodo.todoTitle");	// "com.lb.todosqlite.addnewtodo.todoTitle"
						String categorySelected = bd.getString("com.lb.todosqlite.addnewtodo.categorySelected");  // "com.lb.todosqlite.addnewtodo.categorySelected"
						String creationDate = bd.getString("com.lb.todosqlite.addnewtodo.creationDate");  // "com.lb.todosqlite.addnewtodo.creationDate"
						
						if (categorySelected.equals(spinnerDefaultValue))
							categorySelected = defaultInternalTagName;
						
						createToDo(todoTitle, categorySelected, creationDate);
						clearTableData();
						fillUpTableOnCreation();
					}
					toastt("returned from AddNewToDo Screen", false);
					break;
							
				}
				case requestCode_DeubgScr:
				{
					toastt("returned from Debug Screen", false);
					break;
				}
				
			}
		else toastt("returned with !(Result_OK)", false);
		
		String Dummy = "";
	}
	
	public void fillUpTableOnCreation()
	{
		try {
			TableLayout todosTable = (TableLayout) findViewById(R.id.table_ToDos);
			int c1 = todosTable.getChildCount();
			
			int i = 0;
			TableRow tr = (TableRow) todosTable.getChildAt(i);
			int c2 = tr.getChildCount();
			
			int j = 0;
			TextView tv = (TextView) tr.getChildAt(j);

			//tv.setText("ToDo item");
			
			int k = 1;
			
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
				
					for (int index = 0; index < c2; index++) 
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
								ntv.setText(todo.getCreationDate());		
								break;
							}
							case 3:
							{
								ntv.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.15f));
								if (todo.getStatus()== 1)
									ntv.setText("[X]");
								else ntv.setText("[ ]");								
								break;
							}
						}			
						ntr.addView(ntv);						
					}
				ntr.setFocusableInTouchMode(true);
				ntr.setBackgroundColor(Color.parseColor(colorCodeForTableBG));
				
				ntr.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						
						
						boolean isfocustaken = v.requestFocus();
						int tagIdByRowId = v.getId();
																		
						toastt("TR onLongClick() for todo: " + tagIdByRowId, true);
						
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
						String vieweToString = null;
						int rowTodoID = -1;					
						
						if (!(menu == null) && !(v == null))
						{
							vieweToString = v.toString();
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
									
									toastt("TR onMenuItemClick() was invoked for menuItemID= " + id + " GroupID= " + todoID, false);
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
									
									toastt("TR onMenuItemClick() was invoked for menuItemID= " + id + " GroupID= " + idg, false);
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
									
									toastt("TR onMenuItemClick() was invoked for menuItemID= " + id + " GroupID= " + idg, false);
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
									
									toastt("TR onMenuItemClick() was invoked for menuItemID= " + id + " GroupID= " + idg, false);
									return false;
								}
							});
						}
						
						toastt("TR onCreateContextMenu() was invoked for vID= " + rowTodoID, false);
					}
				});
				
				todosTable.addView(ntr,1);
			}
			db.closeDB();
			
		} catch (Exception e) {
			Log.d("MainActivity", "fillUpTableOnCreation() caught an exception: ", e);
			toastt("fillUpTableOnCreation() caught an exception", false);
		}
	}
	
	
	
	public void helpOnTableCreation()
	{
		searchCount++;
		
		try {
			TableLayout todosTable = (TableLayout) findViewById(R.id.table_ToDos);
			int c1 = todosTable.getChildCount();
			
			int i = 0;
			TableRow tr = (TableRow) todosTable.getChildAt(i);
			int c2 = tr.getChildCount();
			
			int j = 0;
			TextView tv = (TextView) tr.getChildAt(j);

			tv.setText("ToDoDoDo" + searchCount);
			
			int k = 1;
						
			TableRow ntr = new TableRow(getApplicationContext());
			
				for (int index = 0; index < c2; index++) 
				{
					TextView ntv = new TextView(getApplicationContext());
					ntv.setText("Text" + index);
					if (index == 0)
						ntv.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.4f));
					
					ntr.addView(ntv);
					
				}
			todosTable.addView(ntr,1);
			
		} catch (Exception e) {
			e.printStackTrace();
			toastt("helpOnTableCreation() caught an exception", false);
		}
	}
	
	
	public void createToDo(String todoTitle, String categorySelected, String creationDate)
	{
		int getStatusFromUserWasntImplementedYet = 0;
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
		
		Todo todo = new Todo(todoTitle, getStatusFromUserWasntImplementedYet);
		long todoID = db.createToDo(todo, new long[] {tagID});
		//TODO: to remove if no drama occurred after commented
//		Todo TodoFromDB = db.getTodo(todoID);
//		
//		AddTableDataRow(TodoFromDB);
		
		db.closeDB();
	}
	
	
	//TODO: to remove if no drama occurred after commented. deprecated due to use of ClearTableRows() and FillTableOnCreation()
	/*public void  AddTableDataRowggggggg(Todo newTodo)
	{
		try 
		{
			TableLayout todosTable = (TableLayout) findViewById(R.id.table_ToDos);
						
			int titlesRowIndex = 0;
			TableRow tr = (TableRow) todosTable.getChildAt(titlesRowIndex);
			int numberOfColumns = tr.getChildCount();
		
					
					
				TableRow ntr = new TableRow(getApplicationContext());
				
					for (int index = 0; index < numberOfColumns; index++) 
					{
						TextView ntv = new TextView(getApplicationContext());
						ntv.setText("Text" + index);
						
						switch (index)
						{
							case 0:
							{
								ntv.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.4f));
								ntv.setText(newTodo.getNote());	
								break;
							}
							case 1:
							{
								DatabaseHelper db = new DatabaseHelper(getApplicationContext());
								List<Tag> categories = db.getTagsByToDo(newTodo.getId());
								String category = "";
								for (Tag ctg : categories)
									{category = category + "[" + ctg.getTagName() + "] ";}
								
								ntv.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.15f));
								ntv.setText(category);
								db.closeDB();
								break;
							}
							case 2:
							{
								ntv.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.4f));
								ntv.setText(newTodo.getCreationDate());		
								break;
							}
						}			
						ntr.addView(ntv);						
					}
				todosTable.addView(ntr,1);
			
			
		} catch (Exception e) {
			Log.d("MainActivity", "AddTableDataRow() caught an exception: ", e);
			toastt("AddTableDataRow() caught an exception", false);
		}
	}
*/
	
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
		
		toastt("Will updateTodoAndTags", false);
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
		
		int dbRowsAffected = db.updateToDo(todo);
		
		//updateTableDataRow(todoID, ); // if todoID != dbUpdatedTodoID it should be updated also at tableRowID
		Todo todoAfterUpdate = db.getTodo(todoID);
		
		db.closeDB();
		
		clearTableData();
		fillUpTableOnCreation();
		
		//toastt("toggleTodoStatus() invoked", false);		
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
		toastt("Will delete the todo", false);		
	}
	
	public void launchViewTodo(int todoID)
	{
		toastt("Will inflate a view which will only display the todo", false);
	}
	
	public void launchEditTodo(int todoID)
	{
		toastt("Will inflate a view which alows editing the todo", false);
	}
	
	public void clearTableData()
	{
		TableLayout todosTable = (TableLayout) findViewById(R.id.table_ToDos);
		todosTable.removeViews(1, todosTable.getChildCount()-1);
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
