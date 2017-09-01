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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
public class SchoolYearActivity extends Activity {
	SQLiteHelper sqlHelper = new SQLiteHelper(this);
    AlertDialog dialog;
    TextView noData,displaySchoolYear;
    ListView list;
    String SchoolYear;
    ArrayList<Records> recordList = new ArrayList<Records>();
    DataAdapter da;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_year);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0CB1A")));
		noData=(TextView)findViewById(R.id.lblNoSchoolYear);

        //check database content
        checkContent();
        try{

            list=(ListView)findViewById(R.id.lvSchoolYear);
            list.setItemsCanFocus(false);
            showRecords();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "not data", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());
        }
	}

	//check database content
    public void checkContent(){
        String check = sqlHelper.checkData();
        if(check=="no_data"){
            noData.setVisibility(View.VISIBLE);
        }else{
            noData.setVisibility(View.GONE);
        }
    }

    //add new school year
    public void addSchoolYear (View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add School Year");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_school_year_layout,null);
        builder.setView(dialogView);

        final Spinner insertSemester=(Spinner) dialogView.findViewById(R.id.spnSemester);
        final Spinner insertSchoolYear=(Spinner) dialogView.findViewById(R.id.spnSchoolYear);

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
                String semester=insertSemester.getSelectedItem().toString();
                String schoolYear=insertSchoolYear.getSelectedItem().toString();
                Boolean check=sqlHelper.checkSchoolYear(semester+" "+schoolYear);

                if(check==true){
                    Toast.makeText(SchoolYearActivity.this, "School year already exists", Toast.LENGTH_SHORT).show();
                }else{
                    sqlHelper.insertRecord(new Records(semester+" "+schoolYear,"","","","",""));
                    dialog.dismiss();
                    showRecords();
                    checkContent();
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
        ArrayList<Records> dataArray = sqlHelper.showRecords();

        for(int i=0;i<dataArray.size();i++){
            int arrayId=dataArray.get(i).getId();
            String arraySchoolYear=dataArray.get(i).getSchoolYear();
            Records r = new Records();
            r.setId(arrayId);
            r.setSchoolYear(arraySchoolYear);
            recordList.add(r);
        }
        sqlHelper.close();
        da = new DataAdapter(this,R.layout.school_year_list,recordList);
        list.setAdapter(da);
        da.notifyDataSetChanged();
    }

    //put data in array
    public class DataAdapter extends ArrayAdapter<Records>{
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
                holder.showSchoolYear=(TextView)row.findViewById(R.id.lblSchoolYearList);
                holder.btnEdit=(Button)row.findViewById(R.id.btnSCedit);
                holder.btnDelete=(Button)row.findViewById(R.id.btnSCdelete);
                holder.view=(LinearLayout)row.findViewById(R.id.schoolYearLayout);
                row.setTag(holder);
            }else{
                holder = (ViewHolder)row.getTag();
            }
            r = records.get(position);
            holder.view.setTag(r.getSchoolYear());
            holder.showSchoolYear.setTag(r.getSchoolYear());
            holder.btnEdit.setTag(r.getSchoolYear());
            holder.btnDelete.setTag(r.getSchoolYear());
            holder.showSchoolYear.setText(r.getSchoolYear());

            holder.view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Toast.makeText(getApplicationContext(),v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(activity,SectionActivity.class);
                    i.putExtra("saveSchoolYear", v.getTag().toString());
                    activity.startActivity(i);
                }

            });
            //btn edit
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    final AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(SchoolYearActivity.this);
                    builder.setTitle("Update School Year");
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.update_school_year_layout,null);
                    builder.setView(dialogView);

                    final Spinner updateSemester=(Spinner) dialogView.findViewById(R.id.spnUpSem);
                    final Spinner updateSchoolYear=(Spinner) dialogView.findViewById(R.id.spnUpSc);
                    //First Semester 2013-2016
                    String getYear = view.getTag().toString();
                    int index = getYear.lastIndexOf(" ");//15
                    final String semester = getYear.substring(0,index);//0-15
                    final String year = getYear.substring(getYear.lastIndexOf(" ")+1);//16
                    SchoolYear = semester+" "+year;

                    updateSemester.setSelection(((ArrayAdapter<String>)updateSemester.getAdapter()).getPosition(semester));
                    updateSchoolYear.setSelection(((ArrayAdapter<String>)updateSchoolYear.getAdapter()).getPosition(year));

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
                            String semester=updateSemester.getSelectedItem().toString();
                            String schoolYear=updateSchoolYear.getSelectedItem().toString();
                            Boolean check=sqlHelper.checkSchoolYear(semester+" "+schoolYear);

                            if(check==true){
                                Toast.makeText(SchoolYearActivity.this, "School year already exists", Toast.LENGTH_SHORT).show();
                            }else{
                                sqlHelper.updateRecord(SchoolYear,semester+" "+schoolYear);
                                dialog.dismiss();
                                showRecords();
                                checkContent();
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
            });
            //btn delete
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sqlHelper.deleteSchoolYear(view.getTag().toString());
                    showRecords();
                    checkContent();
                }
            });
            return row;
        }

        class ViewHolder{
            TextView showSchoolYear;
            LinearLayout view;
            Button btnEdit,btnDelete;
        }

    }
	
}
