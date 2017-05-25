package com.glenwell.bima;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView; 

public class AboutUs extends AppCompatActivity {
 

	TextView tvStn;
    
 
      @SuppressLint("NewApi") @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
 
            setContentView(R.layout.about_us);
            overridePendingTransition(0,0);

		  getSupportActionBar().setDisplayShowHomeEnabled(true);
		  getSupportActionBar().setIcon(R.mipmap.ic_launcher);
		  getSupportActionBar().setHomeButtonEnabled(true);
            

      
          }
      
      
      
    
  	public void onBackPressed(){
		Intent intent = new Intent(AboutUs.this, MainActivity.class);
		startActivity(intent);
  	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				//NavUtils.navigateUpFromSameTask(this);


					Intent intent = new Intent(AboutUs.this, MainActivity.class);
					startActivity(intent);

				return true;
		}
		return super.onOptionsItemSelected(item);
	}
      
      
  }