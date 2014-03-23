package com.lb.todosqlite;

import java.util.ArrayList;
import java.util.List;

import com.lb.todosqlite.dialogs.DatePickerFragment;
import com.lb.todosqlite.dialogs.TimePickerFragment;
import com.lb.todosqlite.helper.DatabaseHelper;
import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.model.Todo;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditTodo extends FragmentActivity 
{
	final int requestCode_EditToDo = 215;
	final String themeColorCode_LableBG = "#FF9966"; // final and hardcoded for the meantime 
	final String colorCodeForTableBG = "#000000";
	final String colorCodeForText = "#CCCCCC";
	int m_todoID = 0;
	boolean isCancelPressed = true;
	OnDateSetListener onDate;
	OnTimeSetListener onTime;	
	final String dateFormat = "YYYY-MM-DD";
	final String timeFormat = "HH-MM-SS";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_todo);
		
		SharedPreferences sp =  getApplication().getSharedPreferences("editTodo", 0);
		int todo_id = sp.getInt("todoID", -1);
		int reqCode = sp.getInt("userReqCode", -1);
		m_todoID = todo_id;
				
		//updateThemeColors_OLD();
		fillViewesOnCreate(todo_id);
		setViewsHandlers();
		
	}
	
	public void fillViewesOnCreate(int todoID) 
	{
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		Todo todo = db.getTodo(todoID);
		List<Tag> tags = db.getTagsByToDo(todoID);
		db.closeDB();		
		
		Spinner sp_Category = (Spinner) findViewById(R.id.spinner_tag);
		List<String> spinnerCategories = new  ArrayList<String>();		
		for (Tag tag : tags)
		{
			spinnerCategories.add(tag.getTagName());
		}		
		ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerCategories);
    	spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	sp_Category.setAdapter(spAdapter);		
		
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status_nt);
		status.setChecked(todo.getStatus() == 1);
		
		//TODO: hardcoded positions - change once refactoring time date formats
		String todoDueDate = todo.getDueDate();
		int startIndex = 0;
		int endIndex = (todoDueDate.indexOf(":") -2);
		String date = todoDueDate.substring(startIndex, endIndex);
		
		startIndex = (todoDueDate.indexOf(":") -2) ;
		endIndex = todoDueDate.length();		
		String time =todoDueDate.substring(startIndex, endIndex);
		
		TextView tv_DDDate = (TextView) findViewById(R.id.tv_DDDate_ant);
		tv_DDDate.setText(date);				
			
		TextView tv_DDTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		tv_DDTime.setText(time);		
		
		EditText todoNote = (EditText) findViewById(R.id.eText_title);
		todoNote.setText(todo.getNote());	
	}
	
	
	public void fillViewesOnCreate_OLD(int todoID) 
	{
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		Todo todo = db.getTodo(todoID);
		List<Tag> tags = db.getTagsByToDo(todoID);
		db.closeDB();		
		
		EditText categories =  (EditText) findViewById(R.id.et_Category_ed);
		categories.setEnabled(false); // TODO: replace with a global variable if planning to reuse for EditTodo	(for all view elements?)	
		for (Tag tag : tags)
		{
			String catTxt = categories.getText().toString();			
			categories.setText(catTxt + "[" + tag.getTagName() + "] ");
		}
		
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status_ed);
		//status.setEnabled(false);
		status.setChecked(todo.getStatus() == 1);
		
		EditText duedateDate = (EditText) findViewById(R.id.et_DueDate_Date_ed);
		duedateDate.setEnabled(false);
		duedateDate.setText(todo.getDueDate());
		
		
		EditText todoNote = (EditText) findViewById(R.id.et_TodoNote_ed);
		//todoNote.setEnabled(false);
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
				// TODO: check if can change to specific relevant previous intent returned by getIntent() instead of a "new Intent()"
				Intent intent = getIntent();				
				// TODO: remove since handling update locallyy?
//				isCancelPressed = true;
//				intent.putExtra("com.lb.todosqlite.edittodo.isCancelPressed", isCancelPressed);
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
//				// TODO: remove since handling update locallyy?
//				isCancelPressed = false;
//				resultIntent.putExtra("com.lb.todosqlite.addnewtag.isCancelPressed", isCancelPressed);
//				
//				EditText et_TodoTitle = (EditText) findViewById(R.id.eText_title);
//				String reqTodoTitle =et_TodoTitle.getText().toString();  
//				resultIntent.putExtra("com.lb.todosqlite.addnewtodo.todoTitle", reqTodoTitle);
//				
//				String reqTodoCategory = tagNameLastSelected;
//				resultIntent.putExtra("com.lb.todosqlite.addnewtodo.categorySelected", reqTodoCategory);
//				
//			    TextView tv_DueDateDate = (TextView) findViewById(R.id.tv_DDDate_ant);
//			    String reqDueDateDate = tv_DueDateDate.getText().toString();
//			    TextView tv_DueDateTime = (TextView) findViewById(R.id.tv_DDTime_ant);
//			    String reqDueDateTime = tv_DueDateTime.getText().toString();
//			    String reqDueDate = reqDueDateDate.toString() + " " + reqDueDateTime.toString();
//			    
//				resultIntent.putExtra("com.lb.todosqlite.addnewtodo.dueDate", reqDueDate);
				
				collectAndUpdateData();

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
	}
	
	public void launchTimePickerDialog()
	{
		TimePickerFragment tf = new TimePickerFragment();		
		
		TextView tv_OldTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		String oldTimeString = tv_OldTime.getText().toString();				
		int oldHour = Integer.parseInt(oldTimeString.substring(0, 2)); // TODO: ?move handling into a method?
		int oldMinuets = Integer.parseInt(oldTimeString.substring(3, oldTimeString.length()));		
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
		
		// TODO: update and handle category
		
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
	}
	
	public void onDateSetHandler(DatePicker view, int year, int monthOfYear, int dayOfMonth)
	{		
		TextView tv = (TextView) findViewById(R.id.tv_DDDate_ant);
		tv.setText(getFormattedDateOfYMD(year, monthOfYear, dayOfMonth));
	}
	
	public void onTimeSetHandler(TimePicker view, int hourOfDay, int minute) 
	{
		TextView tv = (TextView) findViewById(R.id.tv_DDTime_ant);
		tv.setText(getFormattedTimeOfHM(hourOfDay, minute));
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
	
	private String getFormattedDateOfYMD(int year, int monthOfYear, int dayOfMonth)
	{
		String formattedDate;
		
		String yearString = String.valueOf(year);
		
		String month="01";
		int month_Int = monthOfYear+1;
		if (month_Int>9)
			month = String.valueOf(month_Int);
		else month = "0" + String.valueOf(month_Int);
		
		String day = "01";
		if (dayOfMonth>9)
			day = String.valueOf(dayOfMonth);
		else day = "0" + String.valueOf(dayOfMonth);
		
		formattedDate = yearString + "-" + month + "-" + day;
		return formattedDate;
	}
	
	private String getFormattedTimeOfHM(int hourOfDay, int minute)
	{
		String formattedTime;
		
		String hour="01";		
		if (hourOfDay>9)
			hour = String.valueOf(hourOfDay);
		else hour = "0" + String.valueOf(hourOfDay);
		
		String min = "01";
		if (minute>9)
			min = String.valueOf(minute);
		else min = "0" + String.valueOf(minute);
		
		formattedTime = hour + ":" + min;
		return formattedTime;		
	}
	
}
