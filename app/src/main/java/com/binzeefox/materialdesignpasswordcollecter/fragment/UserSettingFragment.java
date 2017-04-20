package com.binzeefox.materialdesignpasswordcollecter.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.binzeefox.materialdesignpasswordcollecter.R;
import com.binzeefox.materialdesignpasswordcollecter.UserActivity;
import com.binzeefox.materialdesignpasswordcollecter.db.Account;
import com.binzeefox.materialdesignpasswordcollecter.db.User;
import com.binzeefox.materialdesignpasswordcollecter.util.ToastUtil;
import com.binzeefox.materialdesignpasswordcollecter.util.UserUtil;
import org.litepal.crud.DataSupport;

import static android.content.Context.MODE_PRIVATE;
import static com.binzeefox.materialdesignpasswordcollecter.UserActivity.userID;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingFragment extends Fragment implements View.OnClickListener{


    private TextView userName;
    private EditText edNickName;
    private EditText edOldPassword;
    private EditText edNewPassword;
    private EditText edConfirmPassword;
    private Button btSetNickName;
    private Button btChangePassword;
    private Button btClean;
    private Button btDelete;
    private Handler mHandler = new Handler();

    private String nickName;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_setting, container, false);

        userName = (TextView) v.findViewById(R.id.setting_username);
        edNickName = (EditText) v.findViewById(R.id.setting_nickname);
        edOldPassword = (EditText) v.findViewById(R.id.setting_password_old);
        edNewPassword = (EditText) v.findViewById(R.id.setting_password_new);
        edConfirmPassword = (EditText) v.findViewById(R.id.setting_password_confirm);
        btChangePassword = (Button) v.findViewById(R.id.bt_change_password);
        btSetNickName = (Button) v.findViewById(R.id.setting_set_nickname);
        btClean = (Button) v.findViewById(R.id.setting_bt_clean);
        btDelete = (Button) v.findViewById(R.id.setting_bt_delete);
        btDelete.setOnClickListener(this);
        btClean.setOnClickListener(this);
        btSetNickName.setOnClickListener(this);
        btChangePassword.setOnClickListener(this);

        String mUserName = UserActivity.userName;
        userName.setText(mUserName);
        edNickName.setText(queryUserName(UserActivity.userID));
        return v;
    }

    @Override
    public void onClick(View v) {

        initString();
        switch (v.getId()){
            case R.id.setting_set_nickname:
                //TODO 写入设置用户名按钮
                if (putUserName(nickName)){
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    ToastUtil.showToast(getContext(), "已设置昵称" + nickName);
                }
                break;
            case R.id.bt_change_password:
                switch (UserUtil.checkChangePassword(getContext(), userID, oldPassword, newPassword, confirmPassword)){

                    case 1:
                        edOldPassword.setError("密码错误");
                        break;
                    case 2:
                        edNewPassword.setError("新密码不能为空");
                        break;
                    case 3:
                        edConfirmPassword.setError("重复密码不一致");
                        break;
                    case 0:
                        if (UserUtil.changePassword(userID, newPassword)){
                            ToastUtil.showToast(getContext(), "更改成功,已应用新密码");
                        }
                        edOldPassword.setText("");
                        edNewPassword.setText("");
                        edConfirmPassword.setText("");
                    default:
                        break;
                }
                break;
            case R.id.setting_bt_clean:
                String msg = "一旦清除信息将无法恢复，是否继续";
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("警告！");
                dialog.setMessage(msg);
                dialog.setCancelable(false);
                dialog.setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(Account.class, "userID = ?", String.valueOf(userID));
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                break;
            case R.id.setting_bt_delete:
                msg = "一旦删除用户将无法恢复，是否继续";
                dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("警告！");
                dialog.setMessage(msg);
                dialog.setCancelable(false);
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.delete(User.class, userID);
                        SharedPreferences spf = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = spf.edit();
                        editor.clear();
                        editor.apply();
                        ToastUtil.showToast(getContext(), "已删除用户，请重新登入");
                        getActivity().finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }

    private String queryUserName(int id) {
        User user = DataSupport.find(User.class, id);
        String mUserName = user.getUserName();

        SharedPreferences pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String content = pref.getString(mUserName, null);
        if (content == null) {
            mUserName = null;
        } else {
            mUserName = content;
        }
        return mUserName;
    }

    /**
     * 获得文本框信息
     */
    private void initString(){
        nickName = edNickName.getText().toString();
        oldPassword = edOldPassword.getText().toString();
        newPassword = edNewPassword.getText().toString();
        confirmPassword = edConfirmPassword.getText().toString();
        edNickName.setFocusable(true);
        edNickName.setFocusableInTouchMode(true);
    }

    /**
     * 设置昵称
     * @param newName 昵称
     * @return 返回是否成功
     */
    private boolean putUserName(String newName) {
        if (nickName.isEmpty()){
            edNickName.setError("请输入昵称");
            return false;
        }

        User user = DataSupport.find(User.class, userID);
        String userName = user.getUserName();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString(userName, newName);
        editor.apply();
        return true;
    }


}
