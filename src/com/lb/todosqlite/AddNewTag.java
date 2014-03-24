package com.lb.todosqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddNewTag extends Activity 
{
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
	}
	
	public void onCreatePressed()
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
	
	public void onCancelPressed()
	{
		Intent intent = getIntent();
		
		isCancelPressed = true;
		intent.putExtra("com.lb.todosqlite.addnewtag.isCancelPressed", isCancelPressed);
		
        setResult(RESULT_OK, intent);
        finish();		
	}
}
