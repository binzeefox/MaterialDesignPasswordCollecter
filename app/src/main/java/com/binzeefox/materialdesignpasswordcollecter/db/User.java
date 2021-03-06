package com.binzeefox.materialdesignpasswordcollecter.db;

import org.litepal.crud.DataSupport;

/**
 * Created by tong.xiwen on 2017/4/14.
 */
public class User extends DataSupport {

    private int id;
    private String userName;
    private String passWord;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
