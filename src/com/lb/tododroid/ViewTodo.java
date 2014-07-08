package com.lb.tododroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lb.tododroid.dialogs.MyArrayAdapter;
import com.lb.tododroid.model.Tag;
import com.lb.tododroid.model.Todo;
import com.lb.tododroid.services.DatabaseHelper;
import com.lb.tododroid.services.DateTimeServices;
import com.lb.tododroid.R;

public class ViewTodo extends Activity
{
	final int requestCode_ViewToDo = 214;
	// vars for color theme use   
	int colorCodeForTableBG = -16777216;
	int colorCodeForTableText = -1;
	int colorCodeForHintText = -1644826;
	int colorCodeForButtonsTxt = -1;  
	int colorCodeForTableHeaderBG = -6645094;
	int colorCodeForTableHeaderTxt = -1644826;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.new_todo);
		
		SharedPreferences sp = getApplication().getSharedPreferences("viewTodo", 0);
		int todo_id = sp.getInt("todoID", -1);
				
		fillViewesOnCreate(todo_id);
		getThemeColorsFromPreferences();
		updateElementsWithThemeColors();
	}
	
	private void fillViewesOnCreate(int todoID) 
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
		MyArrayAdapter spAdapter = new MyArrayAdapter(this, R.layout.spinner_adapter_text_view_item, spinnerCategories);
    	spAdapter.setDropDownViewResource(R.layout.spinner_adapter_text_view_item);
    	sp_Category.setAdapter(spAdapter);	
		
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status_nt);
		status.setChecked(todo.getStatus() == 1);
		status.setEnabled(false);
		
		String todoDueDate = todo.getDueDate();		
		String date = DateTimeServices.getDateOfDateTimeFormat(todoDueDate);
		TextView tv_DDDate = (TextView) findViewById(R.id.tv_DDDate_ant);
		tv_DDDate.setText(date);
		tv_DDDate.setEnabled(false);
		
		String time = DateTimeServices.getTimeOfDateTimeFormat(todoDueDate);	
		TextView tv_DDTime = (TextView) findViewById(R.id.tv_DDTime_ant);
		tv_DDTime.setText(time);
		tv_DDTime.setEnabled(false);
		CheckBox reminder = (CheckBox) findViewById(R.id.cb_notif_nt);
		reminder.setChecked(todo.getNotification().equals(Todo.NOTIFICATION_STATUS_ENABLED));
		reminder.setEnabled(false);
		
		EditText todoNote = (EditText) findViewById(R.id.eText_title);
		todoNote.setEnabled(false);
		todoNote.setText(todo.getNote());	
		
		Button bt_Cancel = (Button) findViewById(R.id.bt_CancellCreateTodo);
		bt_Cancel.setVisibility(View.INVISIBLE);
		Button bt_Save = (Button) findViewById(R.id.bt_ApplyCreateTodo);
		bt_Save.setVisibility(View.INVISIBLE);
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
		
		// Spinner
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
		// Reminder Checkbox 
		CheckBox reminder = (CheckBox) findViewById(R.id.cb_notif_nt);
		reminder.setTextColor(colorCodeForTableHeaderTxt);
		reminder.setBackgroundColor(colorCodeForTableHeaderBG);
	}
		
}
