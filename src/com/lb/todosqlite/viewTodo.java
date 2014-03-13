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
import android.widget.EditText;
import android.widget.TextView;

public class viewTodo extends Activity
{
	final int requestCode_ViewToDo = 214;
	final int requestCode_EditToDo = 215;
	final String themeColorCode_LableBG = "#FFCC66"; // final and hardcoded for the meantime 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_todo);
		
		SharedPreferences sp = getApplication().getSharedPreferences("editTodo", 0);
		int userReqCode = sp.getInt("userReqCode", -1);
		int todo_id = sp.getInt("todoID", -1);
		String dummy_todo_note = sp.getString("todo_dummy", "null");
		
		fillViewesOnCreate(todo_id);
		
		Bundle b = getIntent().getExtras();
		
		//TODO: use BackButton?
	}
	
	public void fillViewesOnCreate(int todoID) 
	{
		// consider later if to put DB services in separate method?
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		Todo todo = db.getTodo(todoID);
		List<Tag> tags = db.getTagsByToDo(todoID);
		db.closeDB();
		
		// TODO: fetch view element and fill in the data from todo and tags
		TextView catLable = (TextView) findViewById(R.id.tv_CatLable);
		catLable.setBackgroundColor(Color.parseColor(themeColorCode_LableBG));
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
		EditText duedateTime = (EditText) findViewById(R.id.et_DueDate_Time);
		duedateTime.setEnabled(false);
		// TODO: fill up date and time according to required formats
		
		EditText todoNote = (EditText) findViewById(R.id.et_TodoNote);
		todoNote.setEnabled(false);
		todoNote.setText(todo.getNote());
	}
	
	

}
