package com.lb.tododroid.dialogs;

import com.lb.tododroid.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class StylishSpinner extends Spinner 
{
	int dbgCounter = 0;
	int colorCodeForTableBG = -16777216;
	int colorCodeForTableText = -15550446; // green?

//	public StylishSpinner(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		// TODO Auto-generated constructor stub
//		this.setBackgroundColor(Color.GREEN);
//		
//	}

	public StylishSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//this.setBackgroundColor(Color.GREEN);
		//this.getAdapter().getItem(0).

	}

//	public StylishSpinner(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//		this.setBackgroundColor(Color.GREEN);
//	}
	
	
	@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			
			Canvas myCanvas = canvas;
			TextView tv_spinnerContent = (TextView) findViewById(R.id.customized_spinner_format);
//			tv_spinnerContent.setTextColor(colorCodeForTableText);
//			tv_spinnerContent.setBackgroundColor(colorCodeForTableBG);
			super.onDraw(myCanvas);
		}
	
	
	
	

}
