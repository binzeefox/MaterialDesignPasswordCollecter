package com.binzeefox.materialdesignpasswordcollecter.model;

/**
 * Created by tong.xiwen on 2017/4/18.
 */
public class Card {

    //TODO 完成cardView 的内容和主页内容
    private String accountType;
    private String userName;
    private String createTime;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
