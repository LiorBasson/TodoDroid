package com.lb.todosqlite;

import java.util.ArrayList;
import java.util.List;

import com.lb.todosqlite.R.id;
import com.lb.todosqlite.helper.DatabaseHelper;
import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.model.Todo;

import android.R.color;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

public class ViewTodo extends Activity
{
	final int requestCode_ViewToDo = 214;
	final int requestCode_EditToDo = 215;
	final String themeColorCode_LableBG = "#FF9966"; // final and hardcoded for the meantime 
	final String colorCodeForTableBG = "#000000";
	final String colorCodeForText = "#CCCCCC";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.new_todo);
		
		SharedPreferences sp = getApplication().getSharedPreferences("viewTodo", 0);
		int todo_id = sp.getInt("todoID", -1);
				
		//updateThemeColors();
		fillViewesOnCreate(todo_id);
	}
	
	public void fillViewesOnCreate(int todoID) 
	{
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		Todo todo = db.getTodo(todoID);
		List<Tag> tags = db.getTagsByToDo(todoID);
		db.closeDB();		
		
		Spinner sp_Category = (Spinner) findViewById(R.id.spinner_tag);
		sp_Category.setEnabled(false);		
		List<String> spinnerCategories = new  ArrayList<String>();		
		for (Tag tag : tags)
		{
			spinnerCategories.add(tag.getTagName());
		}		
		ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerCategories);
    	spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	sp_Category.setAdapter(spAdapter);		
		
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status_nt);
		status.setEnabled(false);
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
		todoNote.setEnabled(false);
		todoNote.setText(todo.getNote());	
		
		Button bt_Cancel = (Button) findViewById(R.id.bt_CancellCreateTodo);
		bt_Cancel.setVisibility(View.INVISIBLE);
		Button bt_Save = (Button) findViewById(R.id.bt_ApplyCreateTodo);
		bt_Save.setVisibility(View.INVISIBLE);
	}
	
	
	public void fillViewesOnCreate_OLD(int todoID) 
	{
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		Todo todo = db.getTodo(todoID);
		List<Tag> tags = db.getTagsByToDo(todoID);
		db.closeDB();		
		
		EditText categories =  (EditText) findViewById(R.id.et_Category);
		categories.setEnabled(false); // TODO: replace with a global variable if planning to reuse for EditTodo	(for all view elements?)	
		for (Tag tag : tags)
		{
			String catTxt = categories.getText().toString();			
			categories.setText(catTxt + "[" + tag.getTagName() + "] ");
		}
		
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status);
		status.setEnabled(false);
		status.setChecked(todo.getStatus() == 1);
		
		EditText duedateDate = (EditText) findViewById(R.id.et_DueDate_Date);
		duedateDate.setEnabled(false);
		duedateDate.setText(todo.getDueDate());
		
		
		EditText todoNote = (EditText) findViewById(R.id.et_TodoNote);
		todoNote.setEnabled(false);
		todoNote.setText(todo.getNote());		
	}
	
	public void updateThemeColors_OLD()
	{
		RelativeLayout viewLayout = (RelativeLayout) findViewById(R.id.rl_ViewTodo_RootLayout);
		viewLayout.setBackgroundColor(Color.parseColor(colorCodeForTableBG));
		TextView catLable = (TextView) findViewById(R.id.tv_CatLable);
		catLable.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
		TextView statusLable = (TextView) findViewById(R.id.tv_StatusLable);
		statusLable.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
		TextView dueDateLable = (TextView) findViewById(R.id.tv_DueDateLable);
		dueDateLable.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
		TextView todoNoteLable = (TextView) findViewById(R.id.tv_TodoNoteLable);
		todoNoteLable.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
		
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status);
		status.setTextColor(Color.parseColor(colorCodeForText));
		status.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
	}
}
