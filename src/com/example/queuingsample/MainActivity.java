package com.example.queuingsample;

import com.example.queuingsample.taskexecutor.Task;
import com.example.queuingsample.taskexecutor.TaskExecutor;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	TaskExecutor taskExecutor = new TaskExecutor();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(this);
		button.setTag(Boolean.valueOf(false));
		
		Button button2 = (Button)findViewById(R.id.button2);
		button2.setOnClickListener(this);
		button2.setTag(Boolean.valueOf(true));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	int count = 0;
	
	@Override
	public void onClick(View v) {
		v.getTag();
		Task<String> task = new Task<String>("hoge" + (count++)) {

			@Override
			public void run(String userData) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.e("Exe", userData);
			}

			@Override
			public void cancel() {
			}
		};
		
		if(((Boolean)v.getTag()).booleanValue()) {
			taskExecutor.addTaskToFirst(task);
		} else {
			taskExecutor.addTask(task);
		}
		
	}
	
	
	
	
	
	
}
