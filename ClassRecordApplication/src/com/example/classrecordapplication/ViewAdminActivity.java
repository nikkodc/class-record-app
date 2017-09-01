package com.example.classrecordapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class ViewAdminActivity extends Activity {
	SQLiteRegisterUser sqlAdmin = new SQLiteRegisterUser(this);
    TextView name,email,number;
    int getAdmin;
    AlertDialog dialog;
    Button active_deactive;
    SQLiteRegisterUser sql = new SQLiteRegisterUser(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_admin);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0CB1A")));
		name=(TextView)findViewById(R.id.lblViewName);
        email=(TextView)findViewById(R.id.lblViewEmail);
        number=(TextView)findViewById(R.id.lblViewNumber);
        active_deactive=(Button)findViewById(R.id.btnActIveDeactive);
        
        getAdmin= Integer.parseInt(getIntent().getStringExtra("saveAdmin"));

        //Toast.makeText(this, getAdmin, Toast.LENGTH_SHORT).show();
        loadAdmin();
        checkStatus();

	}
	public void checkStatus(){
        Admin ad = sqlAdmin.getSingleData(getAdmin);
        String status = ad.getStatus();

        if(status.equals("blocked")){
            active_deactive.setText("Account blocked, click to activate");
        }else{
            active_deactive.setText("Activated");
            active_deactive.setEnabled(false);
        }
    }
	
	public void loadAdmin(){
        Admin ad = sqlAdmin.getSingleData(getAdmin);
        name.setText(ad.getFirstname()+" "+ad.getLastname());
        email.setText(ad.getEmail());
        number.setText(ad.getNumber());
    }

    public void accountStatus(View v){
        Admin ad = sqlAdmin.getSingleData(getAdmin);
        sqlAdmin.activateAdmin(ad.getId());
        checkStatus();
    }

    public void update(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewAdminActivity.this);
        builder.setTitle("Update admin");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_admin_layout,null);
        builder.setView(dialogView);

        final EditText updateFname=(EditText) dialogView.findViewById(R.id.txtUpdateAdminFname);
        final EditText updateLname=(EditText) dialogView.findViewById(R.id.txtUpdateAdminLname);
        final EditText updateEmail=(EditText) dialogView.findViewById(R.id.txtUpdateAdminEmail);
        final EditText updateNumber=(EditText) dialogView.findViewById(R.id.txtUpdateAdminNumber);

        Admin ad = sql.getSingleData(getAdmin);
        updateFname.setText(ad.getFirstname());
        updateLname.setText(ad.getLastname());
        updateEmail.setText(ad.getEmail());
        updateNumber.setText(ad.getNumber());

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog=builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String fname = updateFname.getText().toString();
                String lname = updateLname.getText().toString();
                String email = updateEmail.getText().toString();
                String number = updateNumber.getText().toString();

                if(fname.startsWith(" ") || fname.length()<=0 || lname.startsWith(" ") || lname.length()<=0 || email.startsWith(" ") || number.length()<=0 ){
                    Toast.makeText(ViewAdminActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    sql.updateAdmin(getAdmin,fname,lname,email,number);
                    dialog.dismiss();
                    loadAdmin();
                }
            }
        });
        //cancel button
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });
    }

    public void delete(View v){

        sqlAdmin.deleteRecord(getAdmin);
        Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,AdminActivity.class);
        startActivity(i);
        finish();

    }

    //back button
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent i = new Intent(this,AdminActivity.class);
        startActivity(i);
        finish();
    }

	
}
