package com.lb.todosqlite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lb.todosqlite.dialogs.DatePickerFragment;
import com.lb.todosqlite.dialogs.TimePickerFragment;
import com.lb.todosqlite.helper.DatabaseHelper;
import com.lb.todosqlite.model.Tag;

public class AddNewToDo extends FragmentActivity
{
	
	final int requestCode_AddNewTag = 212;
	final String requestToCreateNewTag = "Create New Category...";
	final String spinnerDefaultValue = "Select Todo category";
	final String defaultInternalTagName = "None";  // categoryOnspinnerDefaultValue
	final String dateFormat = "YYYY-MM-DD";
	final String timeFormat = "HH-MM-SS";
	String tagNameLastSelected = "";
	String TagNameNewSelected = "";
	int btnNewTodoCount = 0;
	boolean isCancelPressed;
	boolean isDebugMode = true; // TODO: enabled for debug. set to false again
	OnDateSetListener onDate;
	OnTimeSetListener onTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_todo);
		
		tagNameLastSelected = spinnerDefaultValue;
		
				
		TextView tv_DDDate = (TextView) findViewById(R.id.tv_DDDate_ant);
		tv_DDDate.setText(getDate());		
		tv_DDDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchDatePickerDialog();				
			}
		});
			
		TextView tv_DDTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		tv_DDTime.setText(getTime());
		tv_DDTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchTimePickerDialog();
			}
		});
		
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
				
				EditText et_TodoTitle = (EditText) findViewById(R.id.eText_title);
				String reqTodoTitle =et_TodoTitle.getText().toString();  
				resultIntent.putExtra("com.lb.todosqlite.addnewtodo.todoTitle", reqTodoTitle);
				
				String reqTodoCategory = tagNameLastSelected;
				resultIntent.putExtra("com.lb.todosqlite.addnewtodo.categorySelected", reqTodoCategory);
				
			    TextView tv_DueDateDate = (TextView) findViewById(R.id.tv_DDDate_ant);
			    String reqDueDateDate = tv_DueDateDate.getText().toString();
			    TextView tv_DueDateTime = (TextView) findViewById(R.id.tv_DDTime_ant);
			    String reqDueDateTime = tv_DueDateTime.getText().toString();
			    String reqDueDate = reqDueDateDate.toString() + " " + reqDueDateTime.toString();
			    
				resultIntent.putExtra("com.lb.todosqlite.addnewtodo.dueDate", reqDueDate);
				
                setResult(RESULT_OK, resultIntent);
                finish();
			}
		});
		
		onDate = new OnDateSetListener() 
		{
			  @Override
			  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
			  {
				  onDateSetHandler(view, year, monthOfYear, dayOfMonth);
			  }
		};
		
		onTime = new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
			{
				onTimeSetHandler(view, hourOfDay, minute); 
			}
		};

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

	public void launchDatePickerDialog()
	{
		DatePickerFragment df = new DatePickerFragment();		
		
		TextView tv_OldDate = (TextView) findViewById(R.id.tv_DDDate_ant);
		String oldDateString = tv_OldDate.getText().toString();		
		
		int oldYear = getYearOfDateFormat(oldDateString);
		int oldMonth = getMonthOfDateFormat(oldDateString);
		int oldDay = getDayOfDateFormat(oldDateString);	
		
		// TODO: change key name to follow Android's conventions (with namespace)
		Bundle bd = new Bundle();
		bd.putInt("oldYear", oldYear);
		bd.putInt("oldMonth", oldMonth);
		bd.putInt("oldDay", oldDay);		
		df.setArguments(bd);
			
		
		df.setCallBack(onDate);
		
		df.show(getSupportFragmentManager(), "setDueDate");
		
		toastDebugInfo("launchDatePickerDialog() exit method after show invoked", true);
	}
	
	public void launchTimePickerDialog()
	{
		TimePickerFragment tf = new TimePickerFragment();		
		
		TextView tv_OldTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		String oldTimeString = tv_OldTime.getText().toString();				
		int oldHour = Integer.parseInt(oldTimeString.substring(0, 2));
		int oldMinuets = Integer.parseInt(oldTimeString.substring(3, 5));		
		Bundle bd = new Bundle();
		bd.putInt("oldHour", oldHour);
		bd.putInt("oldMinuets", oldMinuets);
		tf.setArguments(bd);			
		
		tf.setCallBack(onTime);
		
		tf.show(getSupportFragmentManager(), "setDueDateTime");
		
		toastDebugInfo("launchTimePickerDialog() exit method after show invoked", true);
	}

	private String getDateTime() 
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	    		   "yyyy-MM-dd HH:mm:ss", Locale.getDefault()); 
	    Date date = new Date();
	    return dateFormat.format(date);
	}
	
	private String getDate() 
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	    		   "yyyy-MM-dd", Locale.getDefault()); 
	    Date date = new Date();
	    return dateFormat.format(date);
	}
	
	private String getTime() 
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	    		   "HH:mm", Locale.getDefault()); 
	    Date date = new Date();
	    return dateFormat.format(date);
	}
	
	private int getYearOfDateFormat(String todoDueDate)
	{
		int year = 1900;
		if (dateFormat.equals("YYYY-MM-DD"))
		{	
			int startIndex = 0;
			int endIndex = 4;				
			try 
			{	year = Integer.parseInt(todoDueDate.substring(startIndex, endIndex));			} 
			catch (NumberFormatException e) 			{
				Log.e("AddNewTodo", "getYearOfDateFormat() caught an exception when attempted to parse date from string to int"); 
			}
			catch (IndexOutOfBoundsException e)			{
				Log.e("AddNewTodo", "getYearOfDateFormat() caught an exception when attempted to trim a substring"); 
			}			
		}
		
		return year;
	}
	
	private int getMonthOfDateFormat(String todoDueDate)
	{
		int month = 0;
		if (dateFormat.equals("YYYY-MM-DD"))
		{			
			int startIndex = 5;
			int endIndex = 7;				
			try 
			{	month = Integer.parseInt(todoDueDate.substring(startIndex, endIndex));			} 
			catch (NumberFormatException e) 			{
				Log.e("AddNewTodo", "getYearOfDateFormat() caught an exception when attempted to parse date from string to int"); 
			}
			catch (IndexOutOfBoundsException e)			{
				Log.e("AddNewTodo", "getYearOfDateFormat() caught an exception when attempted to trim a substring"); 
			}			
		}
		
		return month;
	}
	
	private int getDayOfDateFormat(String todoDueDate)
	{
		int day = 1;
		if (dateFormat.equals("YYYY-MM-DD"))
		{	
			int startIndex = 8;
			int endIndex = 10;				
			try 
			{	day = Integer.parseInt(todoDueDate.substring(startIndex, endIndex));			} 
			catch (NumberFormatException e) 			{
				Log.e("AddNewTodo", "getYearOfDateFormat() caught an exception when attempted to parse date from string to int"); 
			}
			catch (IndexOutOfBoundsException e)			{
				Log.e("AddNewTodo", "getYearOfDateFormat() caught an exception when attempted to trim a substring"); 
			}			
		}
		
		return day;
	}
	
	public void onDateSetHandler(DatePicker view, int year, int monthOfYear, int dayOfMonth)
	{
		
		TextView tv = (TextView) findViewById(R.id.tv_DDDate_ant);
		tv.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
	}
	
	public void onTimeSetHandler(TimePicker view, int hourOfDay, int minute) 
	{
		TextView tv = (TextView) findViewById(R.id.tv_DDTime_ant);
		tv.setText(hourOfDay + ":" + minute);
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
