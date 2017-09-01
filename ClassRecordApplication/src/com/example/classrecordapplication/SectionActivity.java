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
import java.util.ArrayList;
import java.util.List;

public class SectionActivity extends Activity {
	SQLiteHelper sqlHelper = new SQLiteHelper(this);
    ArrayList<Records> recordList = new ArrayList<Records>();
    TextView noSection,displaySchoolYear;
    ListView lvSection;
    DataAdapter da;
    AlertDialog dialog;
    String getSchoolYear,getCYSG;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_section);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0CB1A")));
		getSchoolYear=getIntent().getStringExtra("saveSchoolYear");
        noSection=(TextView)findViewById(R.id.lblNoSection);
        displaySchoolYear=(TextView)findViewById(R.id.lblDisplaySchoolYear);

        displaySchoolYear.setText(getSchoolYear);
        //check database content
        checkSectionContent();
        try{

            lvSection=(ListView)findViewById(R.id.lvSection);
            showRecords();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "not data", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());
        }
	}
	 //check database content
    public void checkSectionContent(){
        String check = sqlHelper.checkSectionData(getSchoolYear);
        if(check=="no_data"){
            noSection.setVisibility(View.VISIBLE);
        }else{
            noSection.setVisibility(View.GONE);
        }
    }

    //add new school year
    public void addSection (View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Section");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_section_layout,null);
        builder.setView(dialogView);

        final Spinner insertCourse=(Spinner) dialogView.findViewById(R.id.spnCourse);
        final EditText insertSubject=(EditText) dialogView.findViewById(R.id.txtSubject);
        final Spinner insertSection=(Spinner) dialogView.findViewById(R.id.spnSection);
        final Spinner insertYear=(Spinner) dialogView.findViewById(R.id.spnYear);
        final Spinner insertGroup=(Spinner) dialogView.findViewById(R.id.spnGroup);

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
                //Toast.makeText(SectionActivity.this, getSchoolYear, Toast.LENGTH_SHORT).show();
                String course=insertCourse.getSelectedItem().toString();
                String subject=insertSubject.getText().toString();
                String year=insertYear.getSelectedItem().toString();
                String section=insertSection.getSelectedItem().toString();
                String group=insertGroup.getSelectedItem().toString();
                boolean check=sqlHelper.checkSection(course+" "+subject+" "+year+section+"-"+group);

                if(check){
                    Toast.makeText(SectionActivity.this, "Section already exists", Toast.LENGTH_SHORT).show();
                }

                else if(course.equals("Course") || subject.startsWith(" ") || subject.length()<=0 || year.equals("Year") || section.equals("Section") || group.equals("Group")){
                    Toast.makeText(SectionActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SectionActivity.this, "Successfuly added!", Toast.LENGTH_SHORT).show();
                    sqlHelper.insertSectionRecord(new Records(getSchoolYear,course+" "+subject+" "+year+section+"-"+group,"","","",""));
                    dialog.dismiss();
                    showRecords();
                    checkSectionContent();
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
        List<Records> dataArray = sqlHelper.showSectionRecords(getSchoolYear);

        for(int i=0;i<dataArray.size();i++){
            int arrayId=dataArray.get(i).getId();
            String arraySchoolYear=dataArray.get(i).getSchoolYear();
            String arraySection=dataArray.get(i).getSection();
            Records r = new Records();
            r.setId(arrayId);
            r.setSchoolYear(arraySchoolYear);
            r.setSection(arraySection);
            recordList.add(r);
        }
        sqlHelper.close();
        da = new DataAdapter(this,R.layout.section_list,recordList);
        lvSection.setAdapter(da);
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
                holder.showSection=(TextView)row.findViewById(R.id.lblSectionList);
                holder.btnEdit=(Button)row.findViewById(R.id.btnSedit);
                holder.btnDelete=(Button)row.findViewById(R.id.btnSdelete);
                holder.view=(LinearLayout)row.findViewById(R.id.sectionLayout);
                row.setTag(holder);
            }else{
                holder = (ViewHolder)row.getTag();
            }
            r = records.get(position);
            //holder.view.setTag(r.getId());
            holder.view.setTag(r.getSection());
            holder.btnEdit.setTag(r.getSection());
            holder.btnDelete.setTag(r.getSection());
            holder.showSection.setText(r.getSection().toUpperCase());

            holder.view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(),v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(activity,StudentActivity.class);
                    i.putExtra("saveSchoolYear", getSchoolYear);
                    i.putExtra("saveSection", v.getTag().toString());
                    activity.startActivity(i);

                }

            });

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(SectionActivity.this);
                    builder.setTitle("Update Section");
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.update_section_layout,null);
                    builder.setView(dialogView);

                    final Spinner updateCourse=(Spinner) dialogView.findViewById(R.id.spnUpCourse);
                    final EditText updateSubject=(EditText) dialogView.findViewById(R.id.txtUpSubject);
                    final Spinner updateSection=(Spinner) dialogView.findViewById(R.id.spnUpSection);
                    final Spinner updateYear=(Spinner) dialogView.findViewById(R.id.spnUpYear);
                    final Spinner updateGroup=(Spinner) dialogView.findViewById(R.id.spnUpGroup);

                    //final String getId = view.getTag().toString();

                    //int sectionId = Integer.parseInt(getId);
                    //Records r = sqlHelper.getSection(sectionId);
                    String sec = view.getTag().toString();
                    getCYSG = sec;
                    //BSIT mobile application 4F-G1
                    int x = sec.indexOf(" ");//return number
                    int z = sec.lastIndexOf(" ");//15
                    String y = sec.substring(x,z);

                    String getCourse = sec.substring(0,x);//BSIT
                    String getSubject = y.substring(1);
                    String getYear = sec.substring(z+1,z+2);//4
                    String getSection = sec.substring(z+2,z+3);//F
                    String getGroup = sec.substring(z+4);//G1

                    updateCourse.setSelection(((ArrayAdapter<String>)updateCourse.getAdapter()).getPosition(getCourse));
                    updateSubject.setText(getSubject);
                    updateYear.setSelection(((ArrayAdapter<String>)updateYear.getAdapter()).getPosition(getYear));
                    updateSection.setSelection(((ArrayAdapter<String>)updateSection.getAdapter()).getPosition(getSection));
                    updateGroup.setSelection(((ArrayAdapter<String>)updateGroup.getAdapter()).getPosition(getGroup));

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

                            //String getId = view.getTag().toString();
                            //int sectionId = Integer.parseInt(getId);
                            //Records r = sqlHelper.getSection(sectionId);
                            //String sec = r.getSection();

                            String course=updateCourse.getSelectedItem().toString();
                            String subject=updateSubject.getText().toString();
                            String year=updateYear.getSelectedItem().toString();
                            String section=updateSection.getSelectedItem().toString();
                            String group=updateGroup.getSelectedItem().toString();

                            String x = course+" "+subject+" "+year+section+"-"+group;
                            //Toast.makeText(getApplicationContext(),getCYSG, Toast.LENGTH_LONG).show();
                            //sqlHelper.updateSectionRecord(new Records(sectionId,x,x,x,x,x));

                            boolean checkSection = sqlHelper.checkSection(x);

                            if(checkSection==true){
                                Toast.makeText(SectionActivity.this, "Section already exists", Toast.LENGTH_SHORT).show();
                            }else{
                                sqlHelper.updateSectionRecord(getCYSG,x);
                                showRecords();
                                checkSectionContent();
                                dialog.dismiss();
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

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sqlHelper.deleteSection(view.getTag().toString());
                    showRecords();
                    checkSectionContent();
                }
            });

            return row;

        }

        class ViewHolder{
            TextView showSection;
            LinearLayout view;
            Button btnEdit,btnDelete;
        }

    }

	
}
