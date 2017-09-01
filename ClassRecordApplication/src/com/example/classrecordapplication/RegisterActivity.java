package com.example.classrecordapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class RegisterActivity extends Activity {
	EditText firstname,lastname,email,number,username,password;
    SQLiteRegisterUser sqlAdmin = new SQLiteRegisterUser(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0CB1A")));
		firstname=(EditText)findViewById(R.id.txtFname);
        lastname=(EditText)findViewById(R.id.txtLname);
        email=(EditText)findViewById(R.id.txtEmail);
        number=(EditText)findViewById(R.id.txtContact);
        username=(EditText)findViewById(R.id.txtRegUsername);
        password=(EditText)findViewById(R.id.txtRegPassword);
        
        number.addTextChangedListener(new TextWatcher() {
            int len=0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = number.getText().toString();
                len = str.length();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //0935-836-5846
                String str = number.getText().toString();
                if(str.length()==4 && len <str.length() || str.length()==8 && len <str.length()){//len check for backspace
                    number.append("-");
                }

            }
        });

	}
	
	public void register (View v){
        String _fname=firstname.getText().toString();
        String _lname=lastname.getText().toString();
        String _email=email.getText().toString();
        String _number=number.getText().toString();
        String _username=username.getText().toString();
        String _password=password.getText().toString();

        Boolean userRegister=validateUserInput();
        Boolean userCheck=sqlAdmin.checkUsername(_username);
        if(userRegister){//if true
            if(userCheck){
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
            else{
                sqlAdmin.insertAdminRecord(new Admin(_fname,_lname,_email,_number,_username,_password,"active"));
                Toast.makeText(this, "Successfuly registered", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this,AdminActivity.class);
                startActivity(i);
                finish();
            }

        }else{
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }

    }

    public Boolean validateUserInput()
    {
        Boolean valid=true;

        //empty firstname
        if (firstname.getText().toString().startsWith(" ") || firstname.getText().toString().length()<=0){
            firstname.setError("Enter your first name");
            valid=false;
        }
        //firstname numbers and symbols
        else if (!firstname.getText().toString().matches("[a-zA-Z ]+")){//don't accept number and symbol
            firstname.setError("First name must not contain numbers or symbols");
            valid=false;
        }
        //empty lastname
        if (lastname.getText().toString().startsWith(" ") || lastname.getText().toString().length()<=0){
            lastname.setError("Enter your last name");
            valid=false;
        }
        //lastname numbers and symbols
        else if (!lastname.getText().toString().matches("[a-zA-Z ]+")){
            lastname.setError("Last name must not contain numbers or symbols");
            valid=false;
        }
        //empty email
        if (email.getText().toString().startsWith(" ") || email.getText().toString().length()<=0){
            email.setError("Enter your email address");
            valid=false;
        }
        //valid email format
        String email_expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence email_inputStr = email.getText().toString();

        Pattern email_pattern = Pattern.compile(email_expression, Pattern.CASE_INSENSITIVE);
        Matcher email_matcher = email_pattern.matcher(email_inputStr);

        if (!email_matcher.matches()){
            email.setError("Enter a valid email address");
            valid=false;
        }

        //number should be 11 digits
        if (number.getText().toString().length()!=13){
            number.setError("Mobile number should be 11 digits");
            valid=false;
        }

        //empty username
        if (username.getText().toString().startsWith(" ") || username.getText().toString().length()<=0){
            username.setError("Enter your username");
            valid=false;
        }
        //username alphanumeric
        else if (!username.getText().toString().matches("[a-zA-Z0-9_-]*")){//accept numbers letters hyphen and underscore
            username.setError("Username must not contain special characters");
            valid=false;
        }
        //username must 3-15 characters
        else if (username.getText().toString().length()<5 || username.getText().toString().length()>15 ){
            username.setError("Username must 5-15 characters");
            valid=false;
        }
        //empty password
        if (password.getText().toString().startsWith(" ") || password.getText().toString().length()<=0){
            password.setError("Enter your password");
            valid=false;
        }
        
        return valid;
    }

    public void clear (View v){
        firstname.setText("");
        lastname.setText("");
        email.setText("");
        number.setText("");
        username.setText("");
        password.setText("");
    }

	
}
