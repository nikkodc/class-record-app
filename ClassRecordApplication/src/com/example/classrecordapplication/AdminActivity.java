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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminActivity extends Activity {
	
	SQLiteRegisterUser sqlHelper = new SQLiteRegisterUser(this);
    AlertDialog dialog;
    TextView noData,test;
    ListView list;
    ArrayList<Admin> recordList = new ArrayList<Admin>();
    DataAdapter da;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0CB1A")));
		noData=(TextView)findViewById(R.id.lblNoAdmin);
        //check database content
        checkContent();
        try{

            list=(ListView)findViewById(R.id.lvlAdmin);
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

    public void addAdmin(View v){
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
        finish();
    }

    //show all list
    public void showRecords(){
        recordList.clear();
        ArrayList<Admin> dataArray = sqlHelper.showRecords();

        for(int i=0;i<dataArray.size();i++){
            int arrayId=dataArray.get(i).getId();
            String firstname=dataArray.get(i).getFirstname();
            String lastname=dataArray.get(i).getLastname();

            Admin a = new Admin();
            a.setId(arrayId);
            a.setFirstname(firstname);
            a.setLastname(lastname);
            recordList.add(a);
        }
        sqlHelper.close();
        da = new DataAdapter(this,R.layout.admin_list,recordList);
        list.setAdapter(da);
        da.notifyDataSetChanged();
    }

    //put data in array
    public class DataAdapter extends ArrayAdapter<Admin> {
        Activity activity;
        int layout;
        Admin r;
        ArrayList<Admin> records = new ArrayList<Admin>();

        public DataAdapter(Activity activity, int layout, ArrayList<Admin>records) {
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
                holder.showAdmin=(TextView)row.findViewById(R.id.lblAdminList);
                holder.view=(LinearLayout)row.findViewById(R.id.adminLayout);
                row.setTag(holder);
            }else{
                holder = (ViewHolder)row.getTag();
            }
            r = records.get(position);
            holder.view.setTag(r.getId());
            //holder.view.setTag(r.getUsername());
            holder.showAdmin.setText(r.getFirstname().toUpperCase()+" "+r.getLastname().toUpperCase());

            holder.view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent(activity,ViewAdminActivity.class);
                    i.putExtra("saveAdmin", v.getTag().toString());
                    activity.startActivity(i);
                    finish();
                }

            });
            return row;
        }
        class ViewHolder{
            TextView showAdmin;
            LinearLayout view;
        }
    }

}
