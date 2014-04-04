package com.lb.todosqlite;

import java.util.ArrayList;
import java.util.List;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lb.todosqlite.dialogs.DatePickerFragment;
import com.lb.todosqlite.dialogs.MyArrayAdapter;
import com.lb.todosqlite.dialogs.TimePickerFragment;
import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.services.DatabaseHelper;
import com.lb.todosqlite.services.DateTimeServices;

public class AddNewToDo extends FragmentActivity
{
	
	final int requestCode_AddNewTag = 212;
	final int requestCode_ViewToDo = 214;
	final int requestCode_EditToDo = 215;	
	// vars for color theme use   
	int colorCodeForTableBG = -16777216;
	int colorCodeForTableText = -1;
	int colorCodeForHintText = -1644826;
	int colorCodeForButtonsTxt = -1;  
	int colorCodeForTableHeaderBG = -6645094;
	int colorCodeForTableHeaderTxt = -1644826;
	final String requestToCreateNewTag = "Create New Category...";
	final String spinnerDefaultValue = "Select Todo category";
	final String defaultInternalTagName = "None";  // categoryOnspinnerDefaultValue
	final String dateFormat = "YYYY-MM-DD";
	final String timeFormat = "HH-MM-SS";
	String tagNameLastSelected = "";
	String TagNameNewSelected = "";
	int btnNewTodoCount = 0;
	boolean isCancelPressed;
	boolean isDebugMode = false; 
	OnDateSetListener onDate;
	OnTimeSetListener onTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_todo);
				
		prepareLayoutForNewTodo();
				
		tagNameLastSelected = spinnerDefaultValue;
						
		TextView tv_DDDate = (TextView) findViewById(R.id.tv_DDDate_ant);
		tv_DDDate.setText(DateTimeServices.getDate());		
		tv_DDDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchDatePickerDialog();				
			}
		});
			
		TextView tv_DDTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		tv_DDTime.setText(DateTimeServices.getTime());
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
		
		getThemeColorsFromPreferences();
		updateElementsWithThemeColors();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO: refactoring, reformat this "if hell"
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
									updateTagNameOnCreation(tagName); 
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

	
	public void prepareLayoutForNewTodo()
	{
		CheckBox cb_Status = (CheckBox) findViewById(R.id.cb_Status_nt);
		cb_Status.setVisibility(View.INVISIBLE);
		TextView tv_StatusLabel = (TextView) findViewById(R.id.tv_StatusLable_ntt);
		tv_StatusLabel.setVisibility(View.INVISIBLE);
	}
	
	private void getThemeColorsFromPreferences()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());		
		
		colorCodeForTableBG = sp.getInt("colorCodeForTableBG", colorCodeForTableBG); 
		colorCodeForTableText = sp.getInt("colorCodeForTableText", colorCodeForTableText);
		//colorCodeForTRInFocus = sp.getInt("colorCodeForTRInFocus", colorCodeForTRInFocus); 
		colorCodeForHintText = sp.getInt("colorCodeForHintText", colorCodeForHintText);
//		//colorCodeForButtonsBG = "#80003399"; // Temporarily not in use due to the fact that button's 3D effect is lost
		colorCodeForButtonsTxt = sp.getInt("colorCodeForButtonsTxt", colorCodeForButtonsTxt); 
		colorCodeForTableHeaderBG = sp.getInt("colorCodeForTableHeaderBG", colorCodeForTableHeaderBG);
		colorCodeForTableHeaderTxt = sp.getInt("colorCodeForTableHeaderTxt", colorCodeForTableHeaderTxt);
	}
	
	private void updateElementsWithThemeColors()
	{
		// background
//		RelativeLayout viewLayout = (RelativeLayout) findViewById(R.id.rl_newTodo_RootLayout);
//		viewLayout.setBackgroundColor(colorCodeForTableBG);
		ScrollView viewLayout = (ScrollView) findViewById(R.id.rl_newTodo_RootLayout);
		viewLayout.setBackgroundColor(colorCodeForTableBG);
		
		// labels
		TextView catLable = (TextView) findViewById(R.id.tView_tag);
		catLable.setBackgroundColor(colorCodeForTableHeaderBG);
		catLable.setTextColor(colorCodeForTableHeaderTxt);
		TextView statusLable = (TextView) findViewById(R.id.tv_StatusLable_ntt);
		statusLable.setBackgroundColor(colorCodeForTableHeaderBG);
		statusLable.setTextColor(colorCodeForTableHeaderTxt);
		TextView dueDateLable = (TextView) findViewById(R.id.tv_DueDateLabel_ant);
		dueDateLable.setBackgroundColor(colorCodeForTableHeaderBG);
		dueDateLable.setTextColor(colorCodeForTableHeaderTxt);		
		TextView todoNoteLable = (TextView) findViewById(R.id.tView_title);
		todoNoteLable.setBackgroundColor(colorCodeForTableHeaderBG);
		todoNoteLable.setTextColor(colorCodeForTableHeaderTxt);
		
		// Checkbox - ignore for now - image issues
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status_nt);
		status.setTextColor(colorCodeForTableHeaderTxt);
		status.setBackgroundColor(colorCodeForTableHeaderBG);
		
		// Spinner? TODO:Spinner theme color update
		Spinner sp_category = (Spinner) findViewById(R.id.spinner_tag);
		MyArrayAdapter sp_adapter = (MyArrayAdapter) sp_category.getAdapter();
		sp_adapter.setTextColor_mine(colorCodeForTableText);
		sp_adapter.setBackgroundColor_mine(colorCodeForTableBG);		
		
		// DueDate and TodoNote texts
		TextView dueDateDate = (TextView) findViewById(R.id.tv_DDDate_ant);
		dueDateDate.setTextColor(colorCodeForTableText);	
		TextView dueDateTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		dueDateTime.setTextColor(colorCodeForTableText);
		EditText et_TodoNote_ed = (EditText) findViewById(R.id.eText_title);
		et_TodoNote_ed.setTextColor(colorCodeForTableText);	
		et_TodoNote_ed.setHintTextColor(colorCodeForHintText);
		
		//Buttons in Create and Edit modes
		Button btCancel = (Button) findViewById(R.id.bt_CancellCreateTodo);
		btCancel.setTextColor(colorCodeForButtonsTxt);		
		Button btCreate = (Button) findViewById(R.id.bt_ApplyCreateTodo);
		btCreate.setTextColor(colorCodeForButtonsTxt);	
	}
	
	private void spinnerHandling()
    {
	    	Spinner sp_Category = (Spinner) findViewById(R.id.spinner_tag);
	    	
	    	List<String> spinnerCategories = new  ArrayList<String>();
	    	spinnerCategories.add(spinnerDefaultValue);  // default value which also instruct the user to choose a category
	    	spinnerCategories.add(requestToCreateNewTag);
	    	
	    	DatabaseHelper db = new DatabaseHelper(getApplicationContext());
	    	List<Tag> tags = db.getAllTags();
	    	db.closeDB();
	    	for (Tag tag : tags)
	    	{
	    		boolean isTagDoesntExistInSpinner = !(spinnerCategories.contains(tag.getTagName()));
	    		boolean isTagIsntDefaultInternalTagName = !(defaultInternalTagName.equals(tag.getTagName()));
	    		if (isTagDoesntExistInSpinner && isTagIsntDefaultInternalTagName)
	    			spinnerCategories.add(tag.getTagName());
	    	}
	    	  	
	    	MyArrayAdapter spAdapter = new MyArrayAdapter(this, R.layout.spinner_adapter_text_view_item, spinnerCategories);
	    	spAdapter.setDropDownViewResource(R.layout.spinner_adapter_text_view_item);
	    	sp_Category.setAdapter(spAdapter);
	    		    	
	    	sp_Category.setOnItemSelectedListener(new OnItemSelectedListener() 
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
	
	private void updateTagNameOnCreation(String tagName)
	{
		String tagN = tagName;
		
		if (tagN.equals(""))
			tagN = spinnerDefaultValue;		
		TagNameNewSelected = tagN;		
		Spinner sp = (Spinner) findViewById(R.id.spinner_tag);
		@SuppressWarnings("unchecked")
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
	
	private void restoreTagNameOnCreateCancelled()
	{
		// sets position to occurrence of tagNameLastSelected instead of selected requestToCreateNewTag value
		Spinner sp = (Spinner) findViewById(R.id.spinner_tag);
		@SuppressWarnings("unchecked")
		ArrayAdapter<String> spAdapter = (ArrayAdapter<String>) sp.getAdapter();
		int position;
		if ( tagNameLastSelected.equals("") || tagNameLastSelected.equals(requestToCreateNewTag) || tagNameLastSelected.equals(spinnerDefaultValue) )
			position = 0;
		else position = spAdapter.getPosition(tagNameLastSelected);
		
		sp.setSelection(position);		
		toastDebugInfo("restoring to " + tagNameLastSelected, false);
	}

	private void launchDatePickerDialog()
	{
		DatePickerFragment df = new DatePickerFragment();		
		
		TextView tv_OldDate = (TextView) findViewById(R.id.tv_DDDate_ant);
		String oldDateString = tv_OldDate.getText().toString();		
		
		int oldYear = DateTimeServices.getYearOfDateFormat(oldDateString);
		int oldMonth = DateTimeServices.getMonthOfDateFormat(oldDateString);
		int oldDay = DateTimeServices.getDayOfDateFormat(oldDateString);	
		
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
	
	private void launchTimePickerDialog()
	{
		TimePickerFragment tf = new TimePickerFragment();		
		
		TextView tv_OldTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		String oldTimeString = tv_OldTime.getText().toString();				
		int oldHour = DateTimeServices.getHourOfTimeFormat(oldTimeString);
		int oldMinuets = DateTimeServices.getMinuteOfTimeFormat(oldTimeString);	
		Bundle bd = new Bundle();
		bd.putInt("oldHour", oldHour);
		bd.putInt("oldMinuets", oldMinuets);
		tf.setArguments(bd);			
		
		tf.setCallBack(onTime);
		
		tf.show(getSupportFragmentManager(), "setDueDateTime");
		
		toastDebugInfo("launchTimePickerDialog() exit method after show invoked", true);
	}

	private void onDateSetHandler(DatePicker view, int year, int monthOfYear, int dayOfMonth)
	{
		
		TextView tv = (TextView) findViewById(R.id.tv_DDDate_ant);
		tv.setText(DateTimeServices.getFormattedDateOfYMD(year, monthOfYear, dayOfMonth));
	}
	
	private void onTimeSetHandler(TimePicker view, int hourOfDay, int minute) 
	{
		TextView tv = (TextView) findViewById(R.id.tv_DDTime_ant);
		tv.setText(DateTimeServices.getFormattedTimeOfHM(hourOfDay, minute));
	}
			
	private void toastDebugInfo(String message, boolean IsLongDuration)
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
