package com.example.classrecordapplication;

public class Records {

    private int _id;
    private String _school_year,_section,_student_fname,_student_mname,_student_lname,_student_grade;

    public Records(int id, String school_year, String section, String student_fname, String student_mname, String student_lname, String student_grade) {
        this._id=id;
        this._school_year=school_year;
        this._section=section;
        this._student_lname=student_lname;
        this._student_fname=student_fname;
        this._student_mname=student_mname;
        this._student_grade=student_grade;
    }

    public Records(String schoolYear, String section, String student_fname, String student_mname, String student_lname, String student_grade) {
        this._school_year=schoolYear;
        this._section=section;
        this._student_lname=student_lname;
        this._student_fname=student_fname;
        this._student_mname=student_mname;
        this._student_grade=student_grade;
    }

    public Records() {

    }

    public void setId(int id){
        this._id=id;
    }
    public int getId(){
        return  this._id;
    }

    public void setSchoolYear(String school_year){
        this._school_year=school_year;
    }
    public String getSchoolYear(){
        return this._school_year;
    }

    public void setSection(String section){
        this._section=section;
    }
    public String getSection(){
        return this._section;
    }

    public void setStudentFname(String student_fname){
        this._student_fname=student_fname;
    }
    public String getStudentFname(){
        return this._student_fname;
    }

    public void setStudentLname(String student_lname) {
        this._student_lname=student_lname;
    }
    public String getStudentLname(){
        return this._student_lname;
    }

    public void setStudentMname(String student_mname){
        this._student_mname=student_mname;
    }
    public String getStudentMname(){
        return this._student_mname;
    }

    public void setStudentGrade(String student_grade){
        this._student_grade=student_grade;
    }
    public String getStudentGrade(){
        return this._student_grade;
    }

}
