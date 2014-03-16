package com.lb.todosqlite;

import java.util.List;

import com.lb.todosqlite.helper.DatabaseHelper;
import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.model.Todo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditTodo extends Activity 
{
	final int requestCode_ViewToDo = 214;
	final int requestCode_EditToDo = 215;
	final String themeColorCode_LableBG = "#FF9966"; // final and hardcoded for the meantime 
	final String colorCodeForTableBG = "#000000";
	final String colorCodeForText = "#CCCCCC";
	int m_todoID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_todo);
		
		SharedPreferences sp =  getApplication().getSharedPreferences("editTodo", 0);
		int todo_id = sp.getInt("todoID", -1);
		int reqCode = sp.getInt("userReqCode", -1);
		m_todoID = todo_id;
		
		updateThemeColors();
		fillViewesOnCreate(todo_id);		
	}
	
	public void updateThemeColors()
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
	
	public void fillViewesOnCreate(int todoID) 
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
	
	public void collectUpdatedData()
	{
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		Todo updatedTodo = db.getTodo(m_todoID);
		
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status_ed);
		if (status.isChecked())
			updatedTodo.setStatus(1);
		else updatedTodo.setStatus(0);
		
		EditText duedateDate = (EditText) findViewById(R.id.et_DueDate_Date_ed);
		updatedTodo.setDueDate(duedateDate.getText().toString()); // TODO: implement normally with stricted input possibly with widget	
		
		EditText todoNote = (EditText) findViewById(R.id.et_TodoNote_ed);
		updatedTodo.setNote(todoNote.getText().toString());		
		
		db.updateToDo(updatedTodo);
	}
	
	@Override
	public void finish() {
		collectUpdatedData();
		setResult(RESULT_OK);
		
		super.finish();
	}

}
