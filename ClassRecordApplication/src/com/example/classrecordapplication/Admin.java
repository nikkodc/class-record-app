package com.example.classrecordapplication;

public class Admin {

    int _id;
    String _firstname;
    String _lastname;
    String _email;
    String _number;
    String _username;
    String _password;
    String _status;

    public Admin(int id, String firstname, String lastname, String email, String number, String username, String password, String status){
        this._id=id;
        this._firstname=firstname;
        this._lastname=lastname;
        this._email=email;
        this._number=number;
        this._username=username;
        this._password=password;
        this._status=status;
    }

    public Admin(String firstname, String lastname, String email, String number, String username, String password, String status){
        this._firstname=firstname;
        this._lastname=lastname;
        this._email=email;
        this._number=number;
        this._username=username;
        this._password=password;
        this._status=status;
    }

    public Admin(){

    }

    public void setId(int id){
        this._id=id;
    }
    public int getId(){
        return this._id;
    }

    public void setFirstname(String firstname){
        this._firstname=firstname;
    }
    public String getFirstname(){
        return this._firstname;
    }

    public void setLastname(String lastname){
        this._lastname=lastname;
    }
    public String getLastname(){
        return this._lastname;
    }

    public void setEmail(String email){
        this._email=email;
    }
    public String getEmail(){
        return this._email;
    }

    public void setNumber(String number){
        this._number=number;
    }
    public String getNumber(){
        return this._number;
    }

    public void setUsername(String username){
        this._username=username;
    }
    public String getUsername(){
        return this._username;
    }

    public void setPassword(String password){
        this._password=password;
    }
    public String getPassword(){
        return this._password;
    }

    public void setStatus(String status){
        this._status=status;
    }
    public String getStatus(){
        return this._status;
    }

}
