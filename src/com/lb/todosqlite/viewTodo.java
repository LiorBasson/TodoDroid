package com.lb.todosqlite;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class viewTodo extends Activity
{
	final int requestCode_ViewToDo = 214;
	final int requestCode_EditToDo = 215;	
	
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
	}
	
	public void fillViewesOnCreate(int todoID) 
	{
		
		
	}
	
	

}
