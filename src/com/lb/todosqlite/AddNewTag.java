package com.lb.todosqlite;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class AddNewTag extends Activity 
{
	// vars for color theme use   
	int colorCodeForTableBG = -16777216;
	int colorCodeForTableText = -1;
	int colorCodeForHintText = -1644826;
	int colorCodeForButtonsTxt = -1; 
	// class vars
	public String tagNameToCreate = "dummyname";
	boolean isCancelPressed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_tag_pane);
		
		isCancelPressed = true;
		
		Button btCancel = (Button) findViewById(R.id.bt_CancelTagCreation);
		btCancel.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				onCancelPressed();
			}
		});
		
		Button btCreate = (Button) findViewById(R.id.bt_ApplyTagCreation);
		btCreate.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				onCreatePressed();
			}
		});
		
		getThemeColorsFromPreferences();
		updateElementsWithThemeColors();
	}
	
	private void getThemeColorsFromPreferences()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());		
		
		colorCodeForTableBG = sp.getInt("colorCodeForTableBG", colorCodeForTableBG); 
		colorCodeForTableText = sp.getInt("colorCodeForTableText", colorCodeForTableText);
		colorCodeForHintText = sp.getInt("colorCodeForHintText", colorCodeForHintText);
		colorCodeForButtonsTxt = sp.getInt("colorCodeForButtonsTxt", colorCodeForButtonsTxt); 
	}
	
	private void updateElementsWithThemeColors()
	{
		// Background
		RelativeLayout viewLayout = (RelativeLayout) findViewById(R.id.rl_ant);
		viewLayout.setBackgroundColor(colorCodeForTableBG);		
		
		// New tag name EditText
		EditText et_NewTagName = (EditText) findViewById(R.id.et_NewTagName);
		et_NewTagName.setTextColor(colorCodeForTableText);	
		et_NewTagName.setHintTextColor(colorCodeForHintText);
		
		// Buttons in Create and Edit modes
		Button btCancel = (Button) findViewById(R.id.bt_CancelTagCreation);
		btCancel.setTextColor(colorCodeForButtonsTxt);		
		Button btCreate = (Button) findViewById(R.id.bt_ApplyTagCreation);
		btCreate.setTextColor(colorCodeForButtonsTxt);	
	}

	private void onCreatePressed()
	{
		Intent intent = getIntent();
		
		isCancelPressed = false;
		intent.putExtra("com.lb.todosqlite.addnewtag.isCancelPressed", isCancelPressed);
		
		EditText et_newTag = (EditText) findViewById(R.id.et_NewTagName);
		String reqTagName =et_newTag.getText().toString();  		
		intent.putExtra("com.lb.todosqlite.addnewtag.tagNameToCreate", reqTagName);
		
        setResult(RESULT_OK, intent);
        finish();
	}
	
	private void onCancelPressed()
	{
		Intent intent = getIntent();
		
		isCancelPressed = true;
		intent.putExtra("com.lb.todosqlite.addnewtag.isCancelPressed", isCancelPressed);
		
        setResult(RESULT_OK, intent);
        finish();		
	}
}
