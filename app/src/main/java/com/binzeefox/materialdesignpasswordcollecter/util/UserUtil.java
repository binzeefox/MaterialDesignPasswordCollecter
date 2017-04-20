package com.binzeefox.materialdesignpasswordcollecter.util;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;
import com.binzeefox.materialdesignpasswordcollecter.animation.MyAnimation;
import com.binzeefox.materialdesignpasswordcollecter.db.User;
import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * Created by tong.xiwen on 2017/4/20.
 */
public class UserUtil {

    /**
     * 判断登陆
     *
     * @param context      context
     * @param userTextView 用户名textView，为了报错动作
     * @param userName     用户名
     * @param passWord     密码
     * @return 返回是否成功
     */
    public static boolean checkLogin(Context context, TextView userTextView, String userName, String passWord) {

        // 如果用户名或密码为空，则报错，返回false
        if ("".equals(userName) && "".equals(passWord)) {
            ToastUtil.showBlankErrorToast(context);
            return false;
        }

        // 如果用户名不存在，则报错，返回false
        if (!"".equals(userName)) {
            List<User> users = DataSupport.findAll(User.class);
            boolean isGood = false;
            for (User user : users) {
                String mUserName = user.getUserName();
                if (mUserName.equals(userName)) {
                    isGood = true;
                }
            }
            if (!isGood) {
                userTextView.setError("用户名不存在，请检查或前往登陆");
                userTextView.requestFocus();
                return false;
            }
        }

        // 判断数据库内信息是否成立
        List<User> users = DataSupport.findAll(User.class);
        boolean isSuccess = false;
        for (User user : users) {
            String mUserName = user.getUserName();
            String mPassWord = user.getPassWord();
            if (userName.equals(mUserName) && passWord.equals(mPassWord)) {
                isSuccess = true;
            }
        }
        if (!isSuccess) {
            ToastUtil.showToast(context, "密码或用户名错误");
            return false;
        }
        return true;
    }


    /**
     * 检查注册
     * @param context context
     * @param fab 悬浮按钮用于报错
     * @param userTextView 用户名TextView，用于报错
     * @param userName 用户名
     * @param passWord 密码
     * @param passConfirm 确认密码
     * @return 返回是否成功
     */
    public static boolean checkRegister(Context context, FloatingActionButton fab, TextView userTextView, String userName,
                                  String passWord, String passConfirm) {

        // 如果用户名或密码为空则报错，同时返回false
        if ("".equals(userName) || "".equals(passWord)) {
            ToastUtil.showBlankErrorToast(context);
            return false;
        }

        // 如果用户名存在，则报错，同时强调fab,返回false
        if (!"".equals(userName)) {
            List<User> users = DataSupport.findAll(User.class);
            for (User user : users) {
                String mUserName = user.getUserName();
                if (mUserName.equals(userName)) {
                    userTextView.setError("用户名已存在，请尝试登陆");
                    MyAnimation.shakeAct(fab);
                    userTextView.requestFocus();
                    return false;
                }
            }
        }

        // 判断密码与确认密码是否匹配
        if (!passWord.equals(passConfirm)) {
            ToastUtil.showToast(context, "确认密码与密码不一致");
            return false;
        }

        User user = new User();
        user.setUserName(userName);
        user.setPassWord(passWord);
        user.save();
        return true;
    }

    /**
     *
     * @param context context
     * @param userID 用户数据库id
     * @param old 旧密码
     * @param newPass 新密码
     * @param confirm 确认新密码
     * @return 0成功，1旧密码错误，2新密码为空，3确认新密码与新密码不一致
     */
    public static int checkChangePassword(Context context, int userID, String old, String newPass, String confirm){

        User user = DataSupport.find(User.class, userID);
        String mPassword = user.getPassWord();
        if (!old.equals(mPassword)){
            return 1;
        }

        if (newPass.isEmpty()){
            return 2;
        }

        if (!newPass.equals(confirm)){
            return 3;
        }

        return 0;
    }

    public static boolean changePassword(int userId, String newPass){

        User user = DataSupport.find(User.class, userId);
        user.setPassWord(newPass);
        user.update(userId);
        return true;
    }
}
