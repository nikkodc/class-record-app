package com.example.classrecordapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SQLiteRegisterUser extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="dbRegUsers";
    private static final String DATABASE_TABLE="RegUsers";
    //columns
    private static final String COLUMN_ID="id";
    private static final String COLUMN_FIRSTNAME="firstname";
    private static final String COLUMN_LASTNAME="lastname";
    private static final String COLUMN_EMAIL="address";
    private static final String COLUMN_NUMBER="number";
    private static final String COLUMN_USERNAME="username";
    private static final String COLUMN_PASSWORD="password";
    private static final String COLUMN_STATUS="status";
    //database query
    private static  final String CREATE_TABLE="CREATE TABLE "+DATABASE_TABLE+" ( "
                    +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    +COLUMN_FIRSTNAME+" VARCHAR(50), "
                    +COLUMN_LASTNAME+" VARCHAR(50), "
                    +COLUMN_EMAIL+" VARCHAR(50), "
                    +COLUMN_NUMBER+" VARCHAR(50), "
                    +COLUMN_USERNAME+" VARCHAR(50), "
                    +COLUMN_PASSWORD+" VARCHAR(50), "
                    +COLUMN_STATUS+" VARCHAR(50))";
    //drop table if exists
    private static final String DROP_TABLE=" DROP TABLE IF EXISTS "+DATABASE_TABLE;

    private final ArrayList<Admin> recordList = new ArrayList<Admin>();

    public SQLiteRegisterUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    //check if no admin inserted
    public String checkData(){
        String flag;
        SQLiteDatabase db = this.getReadableDatabase();
        String query_count=" SELECT count(*) FROM "+DATABASE_TABLE;
        Cursor cr = db.rawQuery(query_count,null);
        cr.moveToFirst();
        int i = cr.getInt(0);
        if(i==0){
            flag = "no_data";
        }else{
            flag = "with_data";
        }
        return flag;
    }

    //check Admin Status
    public boolean checStatus(String username){
        String status="blocked";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + COLUMN_USERNAME + " = " + "'"+username+"' AND "+COLUMN_STATUS+" = "+"'"+status+"'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            //cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //username and password compare
    public boolean compareUserPass(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + COLUMN_USERNAME + " = " + "'"+username+"' AND "+COLUMN_PASSWORD+" = "+"'"+password+"'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){//if not found
            //cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //username and password compare
    public String checkPassword(String username){
        String pass;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+COLUMN_PASSWORD+" FROM " + DATABASE_TABLE + " WHERE " + COLUMN_USERNAME + " = " + "'"+username+"'";
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()){
            pass=c.getString(c.getColumnIndex(COLUMN_PASSWORD));
        }else{
            pass="wrong";
        }
        return pass;
    }

    //activate account
    public int activateAdmin(int id)
    {
        String status="activated";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATUS, status);
        String [] whereArgs = {String.valueOf(id)};
        int count = db.update(DATABASE_TABLE, cv, COLUMN_ID + " =? ", whereArgs);
        return count;
    }

    //deactivate account
    public int blockAdmin(String username)
    {
        String status="blocked";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATUS, status);
        String [] whereArgs = {username};
        int count = db.update(DATABASE_TABLE, cv, COLUMN_USERNAME + " =? ", whereArgs);
        return count;
    }

    //add data in the database
    public void insertAdminRecord(Admin ad){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FIRSTNAME,ad.getFirstname());
        cv.put(COLUMN_LASTNAME,ad.getLastname());
        cv.put(COLUMN_EMAIL,ad.getEmail());
        cv.put(COLUMN_NUMBER,ad.getNumber());
        cv.put(COLUMN_USERNAME,ad.getUsername());
        cv.put(COLUMN_PASSWORD,ad.getPassword());
        cv.put(COLUMN_STATUS,ad.getStatus());
        //insert
        db.insert(DATABASE_TABLE,null,cv);
        db.close();
    }

    //show all data in the database
    public ArrayList<Admin> showRecords(){
        try {
            recordList.clear();
            String query = "SELECT * FROM "+DATABASE_TABLE+" ORDER BY "+COLUMN_ID+" ASC";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(query,null);

            if(c.moveToFirst()){
                do {
                    Admin a = new Admin();
                    a.setId(Integer.parseInt(c.getString(0)));
                    a.setFirstname(c.getString(1));
                    a.setLastname(c.getString(2));
                    a.setEmail(c.getString(3));
                    a.setNumber(c.getString(4));
                    a.setUsername(c.getString(5));
                    a.setPassword(c.getString(6));
                    a.setStatus(c.getString(7));
                    recordList.add(a);
                }while (c.moveToNext());
            }
            c.close();
            db.close();
            return recordList;
        }catch (Exception e){
            Log.e("records",""+e);
        }
        return recordList;
    }

    //get single data
    Admin getSingleData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(DATABASE_TABLE, new String[]{COLUMN_ID, COLUMN_FIRSTNAME,
                COLUMN_LASTNAME,
                COLUMN_EMAIL,
                COLUMN_NUMBER,
                COLUMN_USERNAME,
                COLUMN_PASSWORD,
                COLUMN_STATUS}, COLUMN_ID+"=?", new String[]{String.valueOf(id)},null,null,null,null);
        if(c!=null)
            c.moveToFirst();
        Admin dt = new Admin(Integer.parseInt(c.getString(0)),c.getString(1),
                c.getString(2),
                c.getString(3),
                c.getString(4),
                c.getString(5),
                c.getString(6),
                c.getString(7));
        c.close();
        db.close();
        return dt;
    }

    //check exists username
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + DATABASE_TABLE + " where " + COLUMN_USERNAME + " = " + "'"+username+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //update data
    public int updateAdmin(int id, String fname, String lname, String email, String number)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FIRSTNAME, fname);
        cv.put(COLUMN_LASTNAME, lname);
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_NUMBER, number);
        String [] whereArgs = {String.valueOf(id)};
        int count = db.update(DATABASE_TABLE, cv, COLUMN_ID + " =? ", whereArgs);
        return count;

    }

    //delete data
    public void deleteRecord(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE,COLUMN_ID+" =? ",new String[]{String.valueOf(id)});
        db.close();
    }


}