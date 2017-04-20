package com.binzeefox.materialdesignpasswordcollecter;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.*;
import com.binzeefox.materialdesignpasswordcollecter.animation.MyAnimation;
import com.binzeefox.materialdesignpasswordcollecter.db.User;
import com.binzeefox.materialdesignpasswordcollecter.util.BaseActivity;
import com.binzeefox.materialdesignpasswordcollecter.util.ToastUtil;
import com.binzeefox.materialdesignpasswordcollecter.util.UserUtil;
import com.dd.CircularProgressButton;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.*;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private boolean isOnRegister;


    private RelativeLayout cardHeadView;
    private CoordinatorLayout cardView;
    private FrameLayout passConfirmView;
    private TextView cardTitleField;
    private FloatingActionButton fab_action;
    private CircularProgressButton bt_action;
    private EditText userNameField;
    private EditText passwordField;
    private EditText passConfirmField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardHeadView = (RelativeLayout) findViewById(R.id.headView);
        cardView = (CoordinatorLayout) findViewById(R.id.card_view);
        passConfirmView = (FrameLayout) findViewById(R.id.password_confirm_view);
        cardTitleField = (TextView) findViewById(R.id.title_card);
        fab_action = (FloatingActionButton) findViewById(R.id.fab_action);
        bt_action = (CircularProgressButton) findViewById(R.id.bt_action);
        userNameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        passConfirmField = (EditText) findViewById(R.id.password_confirm);

        fab_action.setOnClickListener(this);
        bt_action.setOnClickListener(this);

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);

        LitePal.initialize(this);
        getCardSize();
        onLogin();
        userNameField.setText(loadLastLogin());
    }

    /**
     * 界面切换
     */
    private void onLogin() {
        userNameField.setError(null);
        cardTitleField.setText("请登录");
        passConfirmView.setVisibility(View.GONE);
        fab_action.setImageResource(R.drawable.ic_add_black_24dp);
        passwordField.setText("");
        passConfirmField.setText("");
        isOnRegister = false;
        bt_action.setIdleText("登入");
        bt_action.setCompleteText("登入成功");
        bt_action.setErrorText("登入失败");
        bt_action.setText("登入");
    }

    private void onRegiester() {
        userNameField.setError(null);
        userNameField.setText(userName);
        cardTitleField.setText("开始注册");
        passConfirmView.setVisibility(View.VISIBLE);
        passwordField.setText("");
        fab_action.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        isOnRegister = true;
        bt_action.setIdleText("注册");
        bt_action.setErrorText("注册失败");
        bt_action.setCompleteText("注册成功");
        bt_action.setText("注册");
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        cardView.requestFocus();
        cardView.requestFocusFromTouch();
        switch (v.getId()) {
            case R.id.fab_action:
                if (isOnRegister) {
                    showAnimation();

                } else {
                    showAnimation();
                }
                break;
            case R.id.bt_action:
                initField();
                if (isOnRegister) {
                    // TODO 尚未开始进行注册算法
                    doRegister();
                } else {
                    // TODO 尚未完成登陆算法
                    doLogin();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 注册部件内容
     */
    private String userName;
    private String passWord;
    private String passConfirm;

    private void initField() {
        userName = userNameField.getText().toString();
        passWord = passwordField.getText().toString();
        passConfirm = passConfirmField.getText().toString();
    }

    /**
     * 获取部件尺寸
     */
    private float h1;
    private float h2;

    private void getCardSize() {
        onLogin();
        cardView.measure(0, 0);
        h1 = cardView.getMeasuredHeight();


        onRegiester();
        cardView.measure(0, 0);
        h2 = cardView.getMeasuredHeight();

        Log.d("h1 value", String.valueOf(h1));
        Log.d("h2 value", String.valueOf(h2));
    }

    /**
     * 动画
     */
    private void showAnimation() {

        ObjectAnimator fabStep1 = MyAnimation.changeAlpha(fab_action, false, 300);
        ObjectAnimator fabStep2 = MyAnimation.changeAlpha(fab_action, true, 300);
        ObjectAnimator titleStep1 = MyAnimation.changeAlpha(cardTitleField, false, 300);
        ObjectAnimator titleStep2 = MyAnimation.changeAlpha(cardTitleField, true, 300);
        final ObjectAnimator passConfirmShow = MyAnimation.changeAlpha(passConfirmView, true, 300);
        final ObjectAnimator passConfirmGone = MyAnimation.changeAlpha(passConfirmView, false, 300);

        final AnimatorSet animSet1 = new AnimatorSet();
        final AnimatorSet animSet2 = new AnimatorSet();
        final AnimatorSet animSet3 = new AnimatorSet();

        animSet1.play(fabStep1).with(titleStep1).with(passConfirmGone);
        animSet2.play(fabStep2).with(titleStep2);
        animSet3.play(fabStep2).with(titleStep2).with(passConfirmShow);

        animSet1.start();
        animSet1.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                if (isOnRegister) {
                    onLogin();
                    animSet2.start();
                } else {
                    onRegiester();
                    animSet3.start();
                }
            }
        });
    }


    /**
     * 登陆算法
     */
    private boolean loginResult;

    private void doLogin() {

        bt_action.setIndeterminateProgressMode(true);
        bt_action.setProgress(50);

        Runnable checking = new Runnable() {
            @Override
            public void run() {
                loginResult = UserUtil.checkLogin(MainActivity.this, userNameField, userName, passWord);
                if (loginResult) {
                    bt_action.setProgress(100);
                } else {
                    bt_action.setProgress(-1);
                }
            }
        };

        Runnable result = new Runnable() {
            @Override
            public void run() {
                if (loginResult) {
                    List<User> users = DataSupport.where("userName = ?", userName).find(User.class);
                    int loginID = users.get(0).getId();
                    jumpIn(loginID);
                    onLogin();
                    bt_action.setProgress(0);

                } else {
                    bt_action.setProgress(0);
                }
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(checking, 1000);
        handler.postDelayed(result, 2000);
    }

    /**
     * 注册算法
     */
    private boolean registerResult;

    private void doRegister() {

        bt_action.setIndeterminateProgressMode(true);
        bt_action.setProgress(50);
        Runnable checking = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        registerResult = UserUtil.checkRegister(MainActivity.this, fab_action, userNameField,
                                userName, passWord, passConfirm);
                        if (registerResult) {
                            bt_action.setProgress(100);
                        } else {
                            bt_action.setProgress(-1);
                        }
                    }
                });
            }
        };
        Runnable reset = new Runnable() {
            @Override
            public void run() {
                bt_action.setProgress(0);
                if (registerResult) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showAnimation();
                        }
                    });
                }
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(checking, 1000);
        handler.postDelayed(reset, 2000);
    }

    /**
     * 跳转
     * @return 返回是否成功
     */
    private boolean jumpIn(int loginID) {

        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        intent.putExtra("userID", loginID);
        saveLastLogin();
        startActivity(intent);
        return true;
    }

    /**
     * 存入最后登录用户名
     */
    private void saveLastLogin() {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("lastLogin", userName);
        editor.commit();
    }

    /**
     * 取出最后登入用户名
     *
     * @return 返回用户名
     */
    private String loadLastLogin() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String lastLogin = pref.getString("lastLogin", null);
        return lastLogin;
    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        if (isOnRegister) {
            showAnimation();
        } else {
            finish();
        }
    }
}
