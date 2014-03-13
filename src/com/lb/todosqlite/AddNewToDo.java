package com.lb.todosqlite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.entity.StringEntity;

import com.lb.todosqlite.helper.DatabaseHelper;
import com.lb.todosqlite.model.Tag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddNewToDo extends Activity
{
	
	final int requestCode_AddNewTag = 212;
	final String requestToCreateNewTag = "Create New Category...";
	final String spinnerDefaultValue = "Select Todo category";
	final String defaultInternalTagName = "None";  // categoryOnspinnerDefaultValue
	String tagNameLastSelected = "";
	String TagNameNewSelected = "";
	int btnNewTodoCount = 0;
	boolean isCancelPressed;
	boolean isDebugMode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_todo);
		
		tagNameLastSelected = spinnerDefaultValue;
		
		try 
		{
			spinnerHandling();
		}
		catch (Exception e)
		{
			toastDebugInfo("Failed to handle Spinner creation", false);
			Log.d("MainActivity", "Failed to handle Spinner. see exception: ", e);
		}
		
		isCancelPressed = true;
		
		Button btCancel = (Button) findViewById(R.id.bt_CancellCreateTodo);
		btCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO: check if can change to specific relevant previous intent returned by getIntent() instead of a "new Intent()"
				Intent intent = getIntent();
				
				isCancelPressed = true;
				intent.putExtra("com.lb.todosqlite.addnewtag.isCancelPressed", isCancelPressed);
				
                setResult(RESULT_OK, intent);
                finish();
				
			}
		});
		
		Button btCreate = (Button) findViewById(R.id.bt_ApplyCreateTodo);
		btCreate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO: check if can change to specific relevant previous intent returned by getIntent() instead of a "new Intent()"
				Intent resultIntent = getIntent();
				
				isCancelPressed = false;
				resultIntent.putExtra("com.lb.todosqlite.addnewtag.isCancelPressed", isCancelPressed);
				
				// TODO: check which View relevant in this screen
				EditText et_TodoTitle = (EditText) findViewById(R.id.eText_title);
				String reqTodoTitle =et_TodoTitle.getText().toString();  
				resultIntent.putExtra("com.lb.todosqlite.addnewtodo.todoTitle", reqTodoTitle);
				
				String reqTodoCategory = tagNameLastSelected;
				resultIntent.putExtra("com.lb.todosqlite.addnewtodo.categorySelected", reqTodoCategory);
				
				// TODO: later change to DueDate. for now implementing "Creation Date"
				SimpleDateFormat dateFormat = new SimpleDateFormat(
			    		   "dd-MM-yyyy HH:mm:ss", Locale.getDefault()); 
			       Date date = new Date();
			    String reqCreationDate = dateFormat.format(date);			    
				resultIntent.putExtra("com.lb.todosqlite.addnewtodo.creationDate", reqCreationDate);
				
                setResult(RESULT_OK, resultIntent);
                finish();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == requestCode_AddNewTag)
		{
			if (resultCode == RESULT_OK)
			{
				try 
				{
					Bundle bdl = data.getExtras();
					
					if (bdl.containsKey("com.lb.todosqlite.addnewtag.isCancelPressed"))
						if (!bdl.getBoolean("com.lb.todosqlite.addnewtag.isCancelPressed")) 
						{							
								if (bdl.containsKey("com.lb.todosqlite.addnewtag.tagNameToCreate"))
								{
									String tagName = bdl.getString("com.lb.todosqlite.addnewtag.tagNameToCreate");
									
									// TODO: Set and Update spinner with new tag name
									updateTagNameOnCreation(tagName); //TagNameNewSelected = tagName;
								}
								else toastDebugInfo("Couldn't find tagNameToCreate Extra", false);								
						}
						else restoreTagNameOnCreateCancelled();									
				}
				catch (Exception e)
				{
					Log.d("OnActivityResult", "OnActivityResult gathering data thrown an xception: ", e);
					toastDebugInfo("OnActivityResult gathering data thrown an xception",true);
				}
			}
			else restoreTagNameOnCreateCancelled();
		}					
	}

	public void spinnerHandling()
    {
	    	Spinner sp = (Spinner) findViewById(R.id.spinner_tag);
	    	
	    	List<String> spinnerCategories = new  ArrayList<String>();
	    	spinnerCategories.add(spinnerDefaultValue);  // default value which also instruct the user to choose a category
	    	spinnerCategories.add(requestToCreateNewTag);
	    	
	    	DatabaseHelper db = new DatabaseHelper(getApplicationContext());
	    	List<Tag> tags = db.getAllTags();
	    	for (Tag tag : tags)
	    	{
	    		boolean isTagDoesntExistInSpinner = !(spinnerCategories.contains(tag.getTagName()));
	    		boolean isTagIsntDefaultInternalTagName = !(defaultInternalTagName.equals(tag.getTagName()));
	    		if (isTagDoesntExistInSpinner && isTagIsntDefaultInternalTagName)
	    			spinnerCategories.add(tag.getTagName());
	    	}
	    	  	
	    	ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerCategories);
	    	spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	sp.setAdapter(spAdapter);
	    		    	
	    	sp.setOnItemSelectedListener(new OnItemSelectedListener() 
	    	{	
				@Override
				public void onItemSelected(AdapterView<?> parent, View itemSelected,
						int position, long id) 
				{	
					String selectedItemValue = parent.getItemAtPosition(position).toString();
					
					if ( selectedItemValue.equals(requestToCreateNewTag) )
						{
							// calls AddNewTag screen and class
							Intent addNewTagIntent = new Intent(itemSelected.getContext(), AddNewTag.class);							
							startActivityForResult(addNewTagIntent, requestCode_AddNewTag);						
						}
					else 
					{
						tagNameLastSelected = selectedItemValue;
						TagNameNewSelected = selectedItemValue;
					}										
					toastDebugInfo("OnItemSelected via parent: " + selectedItemValue ,false);								
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					toastDebugInfo("onNothingSelected() invoked" ,false);
				}
		});    	
	}
	
	public void updateTagNameOnCreation(String tagName)
	{
		String tagN = tagName;
		
		if (tagN.equals(""))
			tagN = spinnerDefaultValue;		
		TagNameNewSelected = tagN;		
		Spinner sp = (Spinner) findViewById(R.id.spinner_tag);
		ArrayAdapter<String> spAdapter = (ArrayAdapter<String>) sp.getAdapter();
		
		if (!(TagNameNewSelected.equals(spinnerDefaultValue)))
		{
			//spAdapter.remove(requestToCreateNewTag);
			spAdapter.add(TagNameNewSelected);
			//spAdapter.add(requestToCreateNewTag);
			
			int position = (spAdapter.getCount()-1);
			sp.setSelection(position);			
		}
		else sp.setSelection(0);
		
		tagNameLastSelected = tagN;
		toastDebugInfo("the new category = " + tagN, false);
	}
	
	public void restoreTagNameOnCreateCancelled()
	{
		// sets position to occurrence of tagNameLastSelected instead of selected requestToCreateNewTag value
		Spinner sp = (Spinner) findViewById(R.id.spinner_tag);
		ArrayAdapter<String> spAdapter = (ArrayAdapter<String>) sp.getAdapter();
		int position;
		if ( tagNameLastSelected.equals("") || tagNameLastSelected.equals(requestToCreateNewTag) || tagNameLastSelected.equals(spinnerDefaultValue) )
			position = 0;
		else position = spAdapter.getPosition(tagNameLastSelected);
		
		sp.setSelection(position);		
		toastDebugInfo("restoring to " + tagNameLastSelected, false);
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
