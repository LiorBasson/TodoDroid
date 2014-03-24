package com.lb.todosqlite;

import java.util.ArrayList;
import java.util.List;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lb.todosqlite.dialogs.DatePickerFragment;
import com.lb.todosqlite.dialogs.TimePickerFragment;
import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.model.Todo;
import com.lb.todosqlite.services.DatabaseHelper;
import com.lb.todosqlite.services.DateTimeServices;

public class EditTodo extends FragmentActivity 
{
	final int requestCode_EditToDo = 215;
	final String themeColorCode_LableBG = "#FF9966"; // final and hardcoded for the meantime 
	final String colorCodeForTableBG = "#000000";
	final String colorCodeForText = "#CCCCCC";
	int m_todoID = 0;
	boolean isCancelPressed = true;
	final String defaultInternalTagName = "None";
	String tagNameLastSelected = "";
	String TagNameNewSelected = "";
	OnDateSetListener onDate;
	OnTimeSetListener onTime;	
	final String dateFormat = "YYYY-MM-DD";
	final String timeFormat = "HH-MM-SS";
	boolean isDebugMode = false; // dummy commit


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_todo);
		
		SharedPreferences sp =  getApplication().getSharedPreferences("editTodo", 0);
		int todo_id = sp.getInt("todoID", -1);
//		int reqCode = sp.getInt("userReqCode", -1);
		m_todoID = todo_id;
				
		//updateThemeColors_OLD();
		fillViewesOnCreate(todo_id);
		setViewsHandlers();
		
	}
	
	public void fillViewesOnCreate(int todoID) 
	{
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		Todo todo = db.getTodo(todoID);
		db.closeDB();	
		
		spinnerHandling();		
		
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status_nt);
		status.setChecked(todo.getStatus() == 1);
		
		String todoDueDate = todo.getDueDate();		
		String date = DateTimeServices.getDateOfDateTimeFormat(todoDueDate);
		TextView tv_DDDate = (TextView) findViewById(R.id.tv_DDDate_ant);
		tv_DDDate.setText(date);				
		
		String time = DateTimeServices.getTimeOfDateTimeFormat(todoDueDate);	
		TextView tv_DDTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		tv_DDTime.setText(time);		
		
		EditText todoNote = (EditText) findViewById(R.id.eText_title);
		todoNote.setText(todo.getNote());	
	}
		
	public void updateThemeColors_OLD()
	{
		RelativeLayout viewLayout = (RelativeLayout) findViewById(R.id.rl_EditTodo_RootLayout);
		viewLayout.setBackgroundColor(Color.parseColor(colorCodeForTableBG));
		TextView catLable = (TextView) findViewById(R.id.tv_CatLable_ed);
		catLable.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
		TextView statusLable = (TextView) findViewById(R.id.tv_StatusLable_ed);
		statusLable.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
		TextView dueDateLable = (TextView) findViewById(R.id.tv_DueDateLable_ed);
		dueDateLable.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
		TextView todoNoteLable = (TextView) findViewById(R.id.tv_TodoNoteLable_ed);
		todoNoteLable.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
		
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status_ed);
		status.setTextColor(Color.parseColor(colorCodeForText));
		status.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
		
		//findViewById(R.id.et_Category_ed)
		
		//EditText et_DueDate_Date_ed = (EditText) findViewById(R.id.et_DueDate_Date_ed);
		//et_DueDate_Date_ed.setTextColor(Color.parseColor(colorCodeForText));
		EditText et_TodoNote_ed = (EditText) findViewById(R.id.et_TodoNote_ed);
		et_TodoNote_ed.setTextColor(Color.parseColor(colorCodeForText));		
	}
	
	public void setViewsHandlers()
	{
		TextView tv_DDDate = (TextView) findViewById(R.id.tv_DDDate_ant);
		tv_DDDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchDatePickerDialog();				
			}
		});
			
		TextView tv_DDTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		tv_DDTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchTimePickerDialog();
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
		
		Button btCancel = (Button) findViewById(R.id.bt_CancellCreateTodo);
		btCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();	
                setResult(RESULT_OK, intent);
                finish();
			}
		});
		
		Button btCreate = (Button) findViewById(R.id.bt_ApplyCreateTodo);
		btCreate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				collectAndUpdateData();

				Intent resultIntent = getIntent();
                setResult(RESULT_OK, resultIntent);	
                finish();
}
		});
	}
	
	public void launchDatePickerDialog()
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
	}
	
	public void launchTimePickerDialog()
	{
		TimePickerFragment tf = new TimePickerFragment();		
		
		TextView tv_OldTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		String oldTimeString = tv_OldTime.getText().toString();				
		int oldHour =  DateTimeServices.getHourOfTimeFormat(oldTimeString);
		int oldMinuets = DateTimeServices.getMinuteOfTimeFormat(oldTimeString);		
		Bundle bd = new Bundle();
		bd.putInt("oldHour", oldHour);
		bd.putInt("oldMinuets", oldMinuets);
		tf.setArguments(bd);			
		
		tf.setCallBack(onTime);
		
		tf.show(getSupportFragmentManager(), "setDueDateTime");		
	}
	
	public void collectAndUpdateData()
	{
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		Todo updatedTodo = db.getTodo(m_todoID);
		// refers to a single tag for current implementation and support
		List<Tag> allTags = db.getAllTags();
		for (Tag tag : allTags)
		{
			// TODO: enhance and validate the condition - this implementation is not safe enough
			if (tagNameLastSelected.equals(tag.getTagName()))
			{
				db.updateTagForSpecificTodo(m_todoID, tag.getId()); 
			}
		}		
		
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status_nt);
		if (status.isChecked())
			updatedTodo.setStatus(1);
		else updatedTodo.setStatus(0);		
		
		TextView tv_DueDateDate = (TextView) findViewById(R.id.tv_DDDate_ant);
	    String reqDueDateDate = tv_DueDateDate.getText().toString();
	    TextView tv_DueDateTime = (TextView) findViewById(R.id.tv_DDTime_ant);
	    String reqDueDateTime = tv_DueDateTime.getText().toString();
	    String reqDueDate = reqDueDateDate.toString() + " " + reqDueDateTime.toString();		
	    updatedTodo.setDueDate(reqDueDate); 
		
		EditText todoNote = (EditText) findViewById(R.id.eText_title);
		updatedTodo.setNote(todoNote.getText().toString());		
		
		db.updateToDo(updatedTodo);
				
		db.closeDB();
	}
	
	public void onDateSetHandler(DatePicker view, int year, int monthOfYear, int dayOfMonth)
	{		
		TextView tv = (TextView) findViewById(R.id.tv_DDDate_ant);
		tv.setText(DateTimeServices.getFormattedDateOfYMD(year, monthOfYear, dayOfMonth));
	}
	
	public void onTimeSetHandler(TimePicker view, int hourOfDay, int minute) 
	{
		TextView tv = (TextView) findViewById(R.id.tv_DDTime_ant);
		tv.setText(DateTimeServices.getFormattedTimeOfHM(hourOfDay, minute));
	}
	
	public void spinnerHandling()
    {
	    	Spinner sp = (Spinner) findViewById(R.id.spinner_tag);
	    	
	    	List<String> spinnerCategories = new  ArrayList<String>();
	    	
//	    	spinnerCategories.add(spinnerDefaultValue);  // default value which also instruct the user to choose a category
//	    	spinnerCategories.add(requestToCreateNewTag);
	    	
	    	DatabaseHelper db = new DatabaseHelper(getApplicationContext());
	    	List<Tag> tags = db.getAllTags();
	    	List<Tag> tagsForTodo = db.getTagsByToDo(m_todoID);
	    	for (Tag currentCategory : tagsForTodo)
	    	{ 
	    		spinnerCategories.add(currentCategory.getTagName());
	    		tagNameLastSelected = currentCategory.getTagName();
	    	}
	    			
	    	db.closeDB();
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
					tagNameLastSelected = selectedItemValue;
					TagNameNewSelected = selectedItemValue;
								
					toastDebugInfo("OnItemSelected via parent: " + selectedItemValue ,false);								
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					toastDebugInfo("onNothingSelected() invoked" ,false);
				}
		});    	
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
