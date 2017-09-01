package com.example.classrecordapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikko on 02/07/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=1;
    //database and table
    private static final String DATABASE_NAME="dbClassRecord";
    private static final String DATABASE_TABLE_NAME="tblClassRecord";
    //columns
    private static final String COL_ID="id";
    private static final String COL_SCHOOL_YEAR="school_year";
    private static final String COL_SECTION="section";
    private static final String COL_STUDENT_LNAME="student_lname";
    private static final String COL_STUDENT_FNAME="student_fname";
    private static final String COL_STUDENT_MNAME="student_mname";
    private static final String COL_STUDENT_GRADE="student_grade";
    //create database query
    private static final String CREATE_TABLE=" CREATE TABLE "+DATABASE_TABLE_NAME+" ( "+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                                                                                +COL_SCHOOL_YEAR+" VARCHAR(50), "
                                                                                +COL_SECTION+" VARCHAR(50), "
                                                                                +COL_STUDENT_LNAME+" VARCHAR(50), "
                                                                                +COL_STUDENT_FNAME+" VARCHAR(50), "
                                                                                +COL_STUDENT_MNAME+" VARCHAR(50), "
                                                                                +COL_STUDENT_GRADE+" VARCHAR(50))";
    //drop table
    private static final String DROP_TABLE_EXISTS= " DROP TABLE IF EXISTS "+DATABASE_TABLE_NAME;

    private final ArrayList<Records> recordList = new ArrayList<Records>();

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_EXISTS);
        onCreate(sqLiteDatabase);
    }

    //check if table has data inserted
    public String checkData(){
        String flag;
        SQLiteDatabase db = this.getReadableDatabase();
        String query_count=" SELECT count(*) FROM "+DATABASE_TABLE_NAME;
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

    //check if section table has data inserted
    public String checkSectionData(String school_year){
        String flag;
        SQLiteDatabase db = this.getReadableDatabase();
        String query_count="SELECT count(*) FROM "+DATABASE_TABLE_NAME+" where "+COL_SCHOOL_YEAR+" = '"+school_year+"' AND "+COL_SECTION+" !=''" ;
        Cursor cr = db.rawQuery(query_count,null);
        cr.moveToFirst();
        int i = cr.getInt(0);
        if(i<=0){
            flag = "no_data";
        }else{
            flag = "with_data";
        }
        return flag;
    }

    public int countSection() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME+" where "+COL_SECTION+" !=''" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //check if section table has data inserted
    public String checkStudentData(String section){
        String flag;
        SQLiteDatabase db = this.getReadableDatabase();
        String query_count=" SELECT count(*) FROM "+DATABASE_TABLE_NAME+" where "+COL_SECTION+" = '"+section+"' AND "+COL_STUDENT_FNAME+" !='' ";
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

    //add data in the database
    public void insertRecord(Records r){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SCHOOL_YEAR,r.getSchoolYear());
        cv.put(COL_SECTION,r.getSection());
        cv.put(COL_STUDENT_LNAME,r.getStudentLname());
        cv.put(COL_STUDENT_FNAME,r.getStudentFname());
        cv.put(COL_STUDENT_MNAME,r.getStudentMname());
        cv.put(COL_STUDENT_GRADE,r.getStudentGrade());
        //insert
        db.insert(DATABASE_TABLE_NAME,null,cv);
        db.close();
    }

    //add section data in the database
    public void insertSectionRecord(Records r){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SCHOOL_YEAR,r.getSchoolYear());
        cv.put(COL_SECTION,r.getSection());
        cv.put(COL_STUDENT_LNAME,r.getStudentLname());
        cv.put(COL_STUDENT_FNAME,r.getStudentFname());
        cv.put(COL_STUDENT_MNAME,r.getStudentMname());
        cv.put(COL_STUDENT_GRADE,r.getStudentGrade());
        //insert
        db.insert(DATABASE_TABLE_NAME,null,cv);
        db.close();
    }

    //add section data in the database
    public void insertStudentRecord(Records r){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SCHOOL_YEAR,r.getSchoolYear());
        cv.put(COL_SECTION,r.getSection());
        cv.put(COL_STUDENT_LNAME,r.getStudentLname());
        cv.put(COL_STUDENT_FNAME,r.getStudentFname());
        cv.put(COL_STUDENT_MNAME,r.getStudentMname());
        cv.put(COL_STUDENT_GRADE,r.getStudentGrade());
        //insert
        db.insert(DATABASE_TABLE_NAME,null,cv);
        db.close();
    }

    //check duplicate
    public boolean checkSchoolYear(String schoolYear) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + DATABASE_TABLE_NAME + " where " + COL_SCHOOL_YEAR + " = " + "'"+schoolYear+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //check duplicate
    public boolean checkSection(String section) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + DATABASE_TABLE_NAME + " where " + COL_SECTION + " = " + "'"+section+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //check duplicate
    public boolean checkStudent(String fname, String lname) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + DATABASE_TABLE_NAME + " WHERE " + COL_STUDENT_FNAME + " = " + " '"+fname+"' AND "+COL_STUDENT_LNAME+" = '"+lname+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //get single data
    Records getSingleData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(DATABASE_TABLE_NAME, new String[]{COL_ID,COL_SCHOOL_YEAR,COL_SECTION,COL_STUDENT_FNAME,
                COL_STUDENT_MNAME,
                COL_STUDENT_LNAME,
                COL_STUDENT_GRADE}, COL_ID+"=?", new String[]{String.valueOf(id)},null,null,null,null);
        if(c!=null)
            c.moveToFirst();
        Records dt = new Records(Integer.parseInt(c.getString(0)),c.getString(1),
                c.getString(2),
                c.getString(3),
                c.getString(4),
                c.getString(5),
                c.getString(6));
        c.close();
        db.close();
        return dt;
    }

    //show all data in the database
    public ArrayList<Records> showRecords(){
        try {
            recordList.clear();
            String query = "SELECT * FROM "+DATABASE_TABLE_NAME+" WHERE "+COL_SCHOOL_YEAR+" !='' GROUP BY "+COL_SCHOOL_YEAR+" ORDER BY "+COL_ID+" ASC";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(query,null);

            if(c.moveToFirst()){
                do {
                    Records r = new Records();
                    r.setId(Integer.parseInt(c.getString(0)));
                    r.setSchoolYear(c.getString(1));
                    r.setSection(c.getString(2));
                    r.setStudentLname(c.getString(3));
                    r.setStudentFname(c.getString(4));
                    r.setStudentMname(c.getString(5));
                    recordList.add(r);
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

    //show all section data in the database
    public ArrayList<Records> showSectionRecords(String schoolYear){
        try {
            recordList.clear();
            String query = "SELECT * FROM "+DATABASE_TABLE_NAME+" WHERE "+COL_SCHOOL_YEAR+" = '"+schoolYear+"' AND "+COL_SECTION+" !='' GROUP BY "+COL_SECTION+" ORDER BY "+COL_ID+" ASC";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(query,null);

            if(c.moveToFirst()){
                do {
                    Records r = new Records();
                    r.setId(Integer.parseInt(c.getString(0)));
                    r.setSchoolYear(c.getString(1));
                    r.setSection(c.getString(2));
                   // r.setStudentLname(c.getString(3));
                    //r.setStudentFname(c.getString(4));
                    //r.setStudentMname(c.getString(5));
                    //r.setStudentGrade(c.getString(6));
                    recordList.add(r);
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

    //show all section data in the database
    public ArrayList<Records> showStudentRecords(String section){
        try {
            recordList.clear();
            String query = "SELECT * FROM "+DATABASE_TABLE_NAME+" WHERE "+COL_SECTION+" = '"+section+"' AND "+COL_STUDENT_FNAME+" !='' ";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(query,null);

            if(c.moveToFirst()){
                do {
                    Records r = new Records();
                    r.setId(Integer.parseInt(c.getString(0)));
                    r.setSchoolYear(c.getString(1));
                    r.setSection(c.getString(2));
                    r.setStudentLname(c.getString(3));
                    r.setStudentFname(c.getString(4));
                    r.setStudentMname(c.getString(5));
                    recordList.add(r);
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

    //update data
    public int updateRecord(String oldSchoolYear, String newSchoolYear)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SCHOOL_YEAR, newSchoolYear);
        String [] whereArgs = {oldSchoolYear};
        int count = db.update(DATABASE_TABLE_NAME, cv, COL_SCHOOL_YEAR + " =? ", whereArgs);
        return count;

    }

    //update data
    public int updateSectionRecord(String oldSection, String newSection)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SECTION, newSection);
        String [] whereArgs = {oldSection};
        int count = db.update(DATABASE_TABLE_NAME, cv, COL_SECTION + " =? ", whereArgs);
        return count;

    }

    //update data
    public int updateStudent(int id, String fname, String mname, String lname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STUDENT_FNAME, fname);
        cv.put(COL_STUDENT_MNAME, mname);
        cv.put(COL_STUDENT_LNAME, lname);
        String [] whereArgs = {String.valueOf(id)};
        int count = db.update(DATABASE_TABLE_NAME, cv, COL_ID + " =? ", whereArgs);
        return count;

    }

    //update data
    public int updateStudentGrade(int id, String grade)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STUDENT_GRADE, grade);
        String [] whereArgs = {String.valueOf(id)};
        int count = db.update(DATABASE_TABLE_NAME, cv, COL_ID + " =? ", whereArgs);
        return count;

    }

    //delete data
    public void deleteRecord(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_NAME,COL_ID+" =? ",new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteSchoolYear(String schoolYear){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_NAME,COL_SCHOOL_YEAR+" = ? ",new String[]{schoolYear});
        db.close();
    }

    public void deleteSection(String section){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_NAME,COL_SECTION+" = ? ",new String[]{section});
        db.close();
    }

    public void deleteSrudent(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_NAME,COL_ID+" = ? ",new String[]{String.valueOf(id)});
        db.close();
    }

}