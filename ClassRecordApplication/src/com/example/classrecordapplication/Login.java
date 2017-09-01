package com.example.classrecordapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	String systemAdminUsername="SystemAdmin", systemAdminPassword="SystemAdmin";
    EditText username,password;
    SQLiteRegisterUser sqlAdmin = new SQLiteRegisterUser(this);
    boolean blocked=false;
    int count=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0CB1A")));
		username=(EditText)findViewById(R.id.txtUsername);
        password=(EditText)findViewById(R.id.txtPassword);
	}
	public void login(View v){
        String _username=username.getText().toString();
        String _password=password.getText().toString();

        String checkPass = sqlAdmin.checkPassword(_username);
        boolean checkUsername = sqlAdmin.checkUsername(_username);
        boolean status = sqlAdmin.checStatus(_username);
        boolean checkUserPass = sqlAdmin.compareUserPass(_username,_password);

        if(_username.startsWith(" ") || _password.startsWith(" ")){
            Toast.makeText(this, "Enter your username and password", Toast.LENGTH_SHORT).show();
        }

        else if(_username.equals(systemAdminUsername) && _password.equals(systemAdminPassword)){
            Intent i = new Intent(this,SystemAdmin.class);
            startActivity(i);
            finish();
        }

        else if(checkPass.equals(_password) && status==false ){
            Intent i = new Intent(this,SchoolYearActivity.class);
            startActivity(i);
            finish();
        }
        
        else if(status){
            Toast.makeText(this, "This account is blocked", Toast.LENGTH_SHORT).show();
        }

        else if(checkUsername && !checkPass.equals(_password)){
            if(blocked==false){
                count++;
                Toast.makeText(this, "Incorrect login attempts: "+String.valueOf(count), Toast.LENGTH_SHORT).show();
                if(count==3){
                    Toast.makeText(this, "You have exceeded the number of allowed login attempts(3)", Toast.LENGTH_SHORT).show();
                    //you account has been blocked
                    sqlAdmin.blockAdmin(_username);
                    blocked=true;
                    count=0;
                }
            }else{
                sqlAdmin.blockAdmin(_username);
                //Toast.makeText(this, "This account has been blocked", Toast.LENGTH_SHORT).show();
            }

        }
        /*
        else if(check==true){
            if(status){
                Toast.makeText(this, "This account is blocked", Toast.LENGTH_SHORT).show();
            }else{
                Intent i = new Intent(this,SchoolYearActivity.class);
                startActivity(i);
            }
        }*/
        else{
            Toast.makeText(this, "Wrong username and password", Toast.LENGTH_SHORT).show();
        }

    }

}
