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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewStudentActivity extends Activity {
	SQLiteHelper sql = new SQLiteHelper(this);
    int getStudent;
    EditText activity,project,midterm,finals,misc;
    TextView fullName,grade,equivalent,displaySchoolYearSection;
    Button editSave;
    Boolean edit;
    AlertDialog dialog;
    Records rc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_student);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0CB1A")));
		fullName=(TextView)findViewById(R.id.lblFullName);
        displaySchoolYearSection=(TextView)findViewById(R.id.lblDisplaySchoolYearSection);
        activity=(EditText) findViewById(R.id.txtActivity);
        project=(EditText) findViewById(R.id.txtProject);
        midterm=(EditText) findViewById(R.id.txtMidterm);
        finals=(EditText) findViewById(R.id.txtFinals);
        misc=(EditText) findViewById(R.id.txtMisc);
        grade=(TextView) findViewById(R.id.lblGrade);
        equivalent=(TextView) findViewById(R.id.lblEquivalent);
        editSave=(Button)findViewById(R.id.btnEditSave);

        getStudent = Integer.parseInt(getIntent().getStringExtra("saveStudent"));

        rc = sql.getSingleData(getStudent);
        String syc = rc.getSchoolYear()+"\n"+rc.getSection();
        displaySchoolYearSection.setText(syc);
        fullName.setText(rc.getStudentFname().toUpperCase()+", "+rc.getStudentLname().toUpperCase()+" "+rc.getStudentMname().toUpperCase()+".");
        String scores = rc.getStudentGrade();
        String[] sc = scores.split(" ");

        if (scores.equals("")){
            activity.setText("");
            project.setText("");
            midterm.setText("");
            finals.setText("");
            misc.setText("");
        }else{
            activity.setText(sc[0]);
            project.setText(sc[1]);
            midterm.setText(sc[2]);
            finals.setText(sc[3]);
            misc.setText(sc[4]);
            compute();
        }

        edit = true;
	}

	public void editScores(View v){
        if (edit){
            activity.setEnabled(true);
            project.setEnabled(true);
            midterm.setEnabled(true);
            finals.setEnabled(true);
            misc.setEnabled(true);
            editSave.setText("COMPUTE AND SAVE");
            edit=false;
        }else{
            boolean check=checkNumber();
            if(check){
                compute();
                activity.setEnabled(false);
                project.setEnabled(false);
                midterm.setEnabled(false);
                finals.setEnabled(false);
                misc.setEnabled(false);
                editSave.setText("EDIT SCORES");
                edit=true;
            }else{
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            
        }
    }

    public boolean checkNumber(){
        String act = activity.getText().toString();
        String proj = project.getText().toString();
        String mid = midterm.getText().toString();
        String fin = finals.getText().toString();
        String msc = misc.getText().toString();
        boolean valid = true;
        if(act.length()<=0){
            activity.setError("Enter activity score");
            valid = false;
        }
        if(proj.length()<=0){
            project.setError("Enter project score");
            valid = false;
        }
        if(mid.length()<=0){
            midterm.setError("Enter midterm score");
            valid = false;
        }
        if(fin.length()<=0){
            finals.setError("Enter finals score");
            valid = false;
        }
        if(msc.length()<=0){
            misc.setError("Enter misc. score");
            valid = false;
        }
        return valid;
    }

    public void compute(){
        String act = activity.getText().toString();
        String proj = project.getText().toString();
        String mid = midterm.getText().toString();
        String fin = finals.getText().toString();
        String msc = misc.getText().toString();

        int a = Integer.parseInt(act);//convert string to int
        int b = Integer.parseInt(proj);
        int c = Integer.parseInt(mid);
        int d = Integer.parseInt(fin);
        int e = Integer.parseInt(msc);

        double final_grade= (a*0.30 + b*0.30 + c*0.15 + d*0.15 + e*0.10)/100*50+50 ;
        int x = (int) final_grade;//convert double to int
        double eqv;
        if (x>=97 && x<=100){
            eqv=1.00;
        }else if(x>=94 && x<=96){
            eqv=1.25;
        }else if(x>=91 && x<=93){
            eqv=1.50;
        }else if(x>=88 && x<=90){
            eqv=1.75;
        }else if(x>=85 && x<=87){
            eqv=2.00;
        }else if(x>=82 && x<=84){
            eqv=2.25;
        }else if(x>=79 && x<=81){
            eqv=2.50;
        }else if(x>=76 && x<=78){
            eqv=2.75;
        }else if(x==75){
            eqv=3.00;
        }else{
            eqv=5.00;
        }
        grade.setText(Integer.toString(x)+"%");
        equivalent.setText(Double.toString(eqv));

        sql.updateStudentGrade(getStudent,act+" "+proj+" "+mid+" "+fin+" "+msc);

    }

    public void updateStudent(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewStudentActivity.this);
        builder.setTitle("Update student name");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_student_layout,null);
        builder.setView(dialogView);

        final EditText updateFname=(EditText) dialogView.findViewById(R.id.txtUpFname);
        final EditText updateMname=(EditText) dialogView.findViewById(R.id.txtUpMname);
        final EditText updateLname=(EditText) dialogView.findViewById(R.id.txtUpLname);

        rc = sql.getSingleData(getStudent);
        updateFname.setText(rc.getStudentFname());
        updateMname.setText(rc.getStudentMname());
        updateLname.setText(rc.getStudentLname());

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
                String mname = updateMname.getText().toString();
                String lname = updateLname.getText().toString();

                boolean checkName = sql.checkStudent(fname,lname);

                if(fname.startsWith(" ") || fname.length()<=0 || mname.startsWith(" ") || mname.length()<=0 || lname.startsWith(" ") || lname.length()<=0 ){
                    Toast.makeText(ViewStudentActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(checkName){
                    Toast.makeText(ViewStudentActivity.this, "Name already exists", Toast.LENGTH_SHORT).show();
                }else{
                    sql.updateStudent(getStudent,fname,mname,lname);
                    dialog.dismiss();
                    rc = sql.getSingleData(getStudent);
                    fullName.setText(rc.getStudentFname().toUpperCase()+", "+rc.getStudentLname().toUpperCase()+" "+rc.getStudentMname().toUpperCase()+".");
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

    public void deleteStudent(View v){
        rc = sql.getSingleData(getStudent);
        String school_year = rc.getSchoolYear();
        String section = rc.getSection();
        sql.deleteSrudent(getStudent);
        //Toast.makeText(this, school_year+section, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ViewStudentActivity.this,StudentActivity.class);
        i.putExtra("saveSchoolYear", school_year);
        i.putExtra("saveSection", section);
        startActivity(i);
        finish();
    }

    //back button
    @Override
    public void onBackPressed() {
        rc = sql.getSingleData(getStudent);
        String school_year = rc.getSchoolYear();
        String section = rc.getSection();
        Intent i = new Intent(ViewStudentActivity.this,StudentActivity.class);
        i.putExtra("saveSchoolYear", school_year);
        i.putExtra("saveSection", section);
        startActivity(i);
        finish();
    }
	
}
