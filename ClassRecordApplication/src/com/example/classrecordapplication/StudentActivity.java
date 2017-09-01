package com.example.classrecordapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
public class StudentActivity extends Activity {
	SQLiteHelper sqlHelper = new SQLiteHelper(this);
    ArrayList<Records> recordList = new ArrayList<Records>();
    ListView list;
    String getSchoolYear,getSection;
    TextView noStudent,displaySection;
    DataAdapter da;
    AlertDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0CB1A")));
		getSchoolYear=getIntent().getStringExtra("saveSchoolYear");
        getSection=getIntent().getStringExtra("saveSection");
        noStudent=(TextView)findViewById(R.id.lblNoStudent);
        displaySection=(TextView)findViewById(R.id.lblDisplaySection);

        displaySection.setText(getSchoolYear+"\n"+getSection);
        checkStudentContent();
        try{

            list=(ListView)findViewById(R.id.lvlStudent);
            showRecords();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "not data", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());
        }
	}
	//check database content
    public void checkStudentContent(){
        String check = sqlHelper.checkStudentData(getSection);
        if(check=="no_data"){
            noStudent.setVisibility(View.VISIBLE);
        }else{
            noStudent.setVisibility(View.GONE);
        }
    }

    //add new school year
    public void addStudent (View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Student");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_student_layout,null);
        builder.setView(dialogView);

        final EditText insertFname=(EditText) dialogView.findViewById(R.id.txtAddFname);
        final EditText insertMname=(EditText) dialogView.findViewById(R.id.txtAddMname);
        final EditText insertLname=(EditText) dialogView.findViewById(R.id.txtAddLname);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
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
                String fname=insertFname.getText().toString();
                String mname=insertMname.getText().toString();
                String lname=insertLname.getText().toString();
                boolean checkName = sqlHelper.checkStudent(fname,lname);

                if(fname.startsWith(" ") || fname.length()<=0 || mname.startsWith(" ") || mname.length()<=0 || lname.startsWith(" ") || lname.length()<=0 ){
                    Toast.makeText(StudentActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(checkName){
                    Toast.makeText(StudentActivity.this, "Name already exists", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(StudentActivity.this, "Successfuly added!", Toast.LENGTH_SHORT).show();
                    sqlHelper.insertStudentRecord(new Records(getSchoolYear,getSection,fname,mname,lname,""));
                    dialog.dismiss();
                    showRecords();
                    checkStudentContent();
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

    //show all list
    public void showRecords(){
        recordList.clear();
        ArrayList<Records> dataArray = sqlHelper.showStudentRecords(getSection);

        for(int i=0;i<dataArray.size();i++){
            int arrayId=dataArray.get(i).getId();
            String fname=dataArray.get(i).getStudentFname();
            String mname=dataArray.get(i).getStudentMname();
            String lname=dataArray.get(i).getStudentLname();
            Records r = new Records();
            r.setId(arrayId);
            r.setStudentFname(fname);
            r.setStudentMname(mname);
            r.setStudentLname(lname);
            recordList.add(r);
        }
        sqlHelper.close();
        da = new DataAdapter(this,R.layout.student_list,recordList);
        list.setAdapter(da);
        da.notifyDataSetChanged();
    }

    //put data in array
    public class DataAdapter extends ArrayAdapter<Records> {
        Activity activity;
        int layout;
        Records r;
        ArrayList<Records> records = new ArrayList<Records>();

        public DataAdapter(Activity activity, int layout, ArrayList<Records>records) {
            super(activity, layout, records);
            this.layout=layout;
            this.activity=activity;
            this.records=records;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View row =convertView;
            ViewHolder holder = null;

            if(row==null){
                //Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layout, parent,false);
                holder = new ViewHolder();
                holder.showStudent=(TextView)row.findViewById(R.id.lblStudentList);
                holder.view=(LinearLayout)row.findViewById(R.id.studentLayout);
                row.setTag(holder);
            }else{
                holder = (ViewHolder)row.getTag();
            }
            r = records.get(position);
            holder.view.setTag(r.getId());
            //holder.view.setTag(r.getSchoolYear());
            holder.showStudent.setText(r.getStudentFname().toUpperCase()+" "+r.getStudentLname().toUpperCase());

            holder.view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(getApplicationContext(),v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    //student id
                    Intent i = new Intent(activity,ViewStudentActivity.class);
                    i.putExtra("saveStudent", v.getTag().toString());
                    activity.startActivity(i);
                    finish();
                }

            });

            return row;

        }

        class ViewHolder{
            TextView showStudent;
            LinearLayout view;
        }

    }
	
}
