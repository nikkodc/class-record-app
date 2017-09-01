package com.example.classrecordapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SystemAdmin extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_admin);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0CB1A")));
	}
	
	public void viewAdmin(View v){
        Intent i = new Intent(this,AdminActivity.class);
        startActivity(i);

    }

    public void viewSchoolYear(View v){
        Intent i = new Intent(this,SchoolYearActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Do you want to logout?");
        alert.setCancelable(false);

        alert.setNegativeButton("Yes",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                Intent i = new Intent(SystemAdmin.this,Login.class);
                startActivity(i);
                finish();
            }
        });

        alert.setPositiveButton("No",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        alert.create().show();

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.system_admin, menu);
		return true;
	}

	
}
