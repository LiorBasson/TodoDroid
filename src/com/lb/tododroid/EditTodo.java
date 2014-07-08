package com.lb.tododroid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lb.tododroid.dialogs.DatePickerFragment;
import com.lb.tododroid.dialogs.MyArrayAdapter;
import com.lb.tododroid.dialogs.TimePickerFragment;
import com.lb.tododroid.model.Tag;
import com.lb.tododroid.model.Todo;
import com.lb.tododroid.serviceapp.NotifyService;
import com.lb.tododroid.serviceapp.ScheduleClient;
import com.lb.tododroid.services.DatabaseHelper;
import com.lb.tododroid.services.DateTimeServices;
import com.lb.tododroid.R;

public class EditTodo extends FragmentActivity 
{
	final int requestCode_EditToDo = 215;
	// vars for color theme use   
	int colorCodeForTableBG = -16777216;
	int colorCodeForTableText = -1;
	int colorCodeForHintText = -1644826;
	int colorCodeForButtonsTxt = -1;  
	int colorCodeForTableHeaderBG = -6645094;
	int colorCodeForTableHeaderTxt = -1644826;
	
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
	
	private ScheduleClient scheduleClient;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_todo);
		//  TODO: NewImp for intent invoking support
		//SharedPreferences sp =  getApplication().getSharedPreferences("editTodo", 0);
		//int todo_id_x = sp.getInt("todoID", -1);
//		int reqCode = sp.getInt("userReqCode", -1);
		int todo_id = getIntent().getIntExtra("com.lb.tododroid.edittodo.todoID", -1);
		String sender = getIntent().getStringExtra("com.lb.tododroid.edittodo.sender");  //  ("com.lb.tododroid.edittodo.sender", "ContextMenu");

		m_todoID = todo_id;
		
		if (sender.equals("NotifyService")) //== "NotifyService") 
			clearTodoReminder();
		fillViewesOnCreate(todo_id);
		setViewsHandlers();
		getThemeColorsFromPreferences();
		updateElementsWithThemeColors();		
	}
	
	// TODO:  NotifNewImp to copy
	@Override
	protected void onResume() 
	{
		super.onResume();

		scheduleClient = new ScheduleClient(getApplicationContext());
        scheduleClient.doBindService();
	};

	@Override
	protected void onPause() {
		super.onPause();
		
		if(scheduleClient != null)
            scheduleClient.doUnbindService();
	}
	
	
	private void fillViewesOnCreate(int todoID) 
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
		// TODO: NotifNewImp to copy
		CheckBox reminder = (CheckBox) findViewById(R.id.cb_notif_nt);
		reminder.setChecked(todo.getNotification().equals(Todo.NOTIFICATION_STATUS_ENABLED));
		
		EditText todoNote = (EditText) findViewById(R.id.eText_title);
		todoNote.setText(todo.getNote());	
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
		
		// Status Checkbox - ignore for now - image issues
		CheckBox status = (CheckBox) findViewById(R.id.cb_Status_nt);
		status.setTextColor(colorCodeForTableHeaderTxt);
		status.setBackgroundColor(colorCodeForTableHeaderBG);
		
		// Spinner? 
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
		// TODO: NotifNewImp to copy
		// Reminder Checkbox - ignore for now - image issues
		CheckBox reminder = (CheckBox) findViewById(R.id.cb_notif_nt);
		reminder.setTextColor(colorCodeForTableHeaderTxt);
		reminder.setBackgroundColor(colorCodeForTableHeaderBG);
		
		//Buttons in Create and Edit modes
		Button btCancel = (Button) findViewById(R.id.bt_CancellCreateTodo);
		btCancel.setTextColor(colorCodeForButtonsTxt);		
		Button btCreate = (Button) findViewById(R.id.bt_ApplyCreateTodo);
		btCreate.setTextColor(colorCodeForButtonsTxt);	
	}
	
	private void setViewsHandlers()
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
	
	private void clearTodoReminder()
	{
		// clears the reminder flag for todo in its DB record 
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		Todo todo = db.getTodo(m_todoID);
		todo.setNotification(Todo.NOTIFICATION_STATUS_DISABLED);
		db.updateToDo(todo);
		db.closeDB();	
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
	}
	
	private void launchTimePickerDialog()
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
	
	private void collectAndUpdateData()
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
	    // TODO:  NotifNewImp to copy
	    CheckBox reminder = (CheckBox) findViewById(R.id.cb_notif_nt);
		if (reminder.isChecked())
			updatedTodo.setNotification(Todo.NOTIFICATION_STATUS_ENABLED);
		else updatedTodo.setNotification(Todo.NOTIFICATION_STATUS_DISABLED);	
		
		EditText todoNote = (EditText) findViewById(R.id.eText_title);
		updatedTodo.setNote(todoNote.getText().toString());		
		
		db.updateToDo(updatedTodo);
				
		db.closeDB();
		
		// TODO: try to update SchedulerClient here
		// TODO:  NotifNewImp to copy
		if (true)// reminder.isDirty()) // commented for the meantime due to API Level issues
			{
				if (reminder.isChecked())
				{
					int year = DateTimeServices.getYearOfDateFormat(reqDueDateDate)
							, month = (DateTimeServices.getMonthOfDateFormat(reqDueDateDate))-1 // 0 based 
							, day = DateTimeServices.getDayOfDateFormat(reqDueDateDate) 
							, hourOfDay = DateTimeServices.getHourOfTimeFormat(reqDueDateTime)
							, minute = DateTimeServices.getMinuteOfTimeFormat(reqDueDateTime);
						Calendar c = Calendar.getInstance();
					    c.set(year, month, day, hourOfDay, minute);
						scheduleClient.setAlarmForNotification(c, (int) updatedTodo.getId());  //setReminder(TodoID, date);
				}
				else toastDebugInfo("reminder.isDirty()=true and reminder.isChecked()=false", false); //cancelReminder(TodoID);
			}	
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
	
	private void spinnerHandling()
    {
	    	Spinner sp_Category = (Spinner) findViewById(R.id.spinner_tag);
	    	
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
	    	  	
	    	// ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerCategories);
	    	//spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	
	    	MyArrayAdapter spAdapter = new MyArrayAdapter(this, R.layout.spinner_adapter_text_view_item, spinnerCategories);
	    	spAdapter.setDropDownViewResource(R.layout.spinner_adapter_text_view_item);
	    	sp_Category.setAdapter(spAdapter);
	    	
	    	// #StartCustomSpinnerHandling
	    	
//	    	ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.my_spinner_array, R.layout.custom_xml_spinner_layout); //change the last argument here to your xml above.
//	    	typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		
	    	// #EndCustomSpinnerHandling
	    	sp_Category.setOnItemSelectedListener(new OnItemSelectedListener() 
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
