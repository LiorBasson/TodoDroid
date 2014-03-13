package com.lb.todosqlite;

import java.util.List;

import com.lb.todosqlite.helper.DatabaseHelper;
import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.model.Todo;

import android.R.color;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class viewTodo extends Activity
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
		setContentView(R.layout.view_todo);
		
		SharedPreferences sp = getApplication().getSharedPreferences("editTodo", 0);
		int todo_id = sp.getInt("todoID", -1);
				
		updateThemeColors();
		fillViewesOnCreate(todo_id);
	}
	
	public void fillViewesOnCreate(int todoID) 
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
		duedateDate.setText(todo.getCreationDate());
		
		
		EditText todoNote = (EditText) findViewById(R.id.et_TodoNote);
		todoNote.setEnabled(false);
		todoNote.setText(todo.getNote());		
	}
	
	public void updateThemeColors()
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
