package com.binzeefox.materialdesignpasswordcollecter;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.animation.*;
import android.widget.*;
import com.binzeefox.materialdesignpasswordcollecter.animation.MyAnimation;
import com.dd.CircularProgressButton;

import java.util.Objects;

import static com.binzeefox.materialdesignpasswordcollecter.animation.MyAnimation.changeAlpha;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


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

        initField();
        getCardSize();
        onLogin();
    }

    /**
     * 注册输入框
     */
    private String userName;
    private String passWord;
    private String passConfirm;
    private void initField(){

        userName = userNameField.getText().toString();
        passWord = passwordField.getText().toString();
        passConfirm = passConfirmField.getText().toString();
    }

    /**
     * 界面切换
     */
    private void onLogin() {

        cardTitleField.setText("请登录");
        passConfirmView.setVisibility(View.GONE);
        fab_action.setImageResource(R.drawable.ic_add_black_24dp);
        passwordField.setText("");
        passConfirmField.setText("");
        isOnRegister = false;
    }
    private void onRegiester() {
        cardTitleField.setText("注册中...");
        passConfirmView.setVisibility(View.VISIBLE);
        passwordField.setText("");
        fab_action.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        isOnRegister = true;
    }

    /**
     * 点击事件
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
            case R.id.bt_action :
                if (isOnRegister){
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

                if (isOnRegister){
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
     * 验证算法
     */
    private boolean isSuccess;
    private void doLogin(){

        bt_action.setIndeterminateProgressMode(true);
        bt_action.setProgress(50);

        Runnable success = new Runnable() {
            @Override
            public void run() {
                bt_action.setProgress(100);
                isSuccess = true;
            }
        };
        Runnable failed = new Runnable() {
            @Override
            public void run() {
                bt_action.setProgress(-1);
            }
        };
        Runnable reset = new Runnable() {
            @Override
            public void run() {
                bt_action.setProgress(0);
                if (isSuccess){
                    jumpIn();
                }
            }
        };

        if(Objects.equals(userNameField.getText().toString(), "狐冰杰")){

            Handler handler = new Handler();
            handler.postDelayed(success, 1500);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(failed, 1500);
        }
        Handler handler = new Handler();
        handler.postDelayed(reset, 2500);
    }

    /**
     * 跳转
     * @return 返回是否成功
     */
    private boolean jumpIn() {

        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        startActivity(intent);
        return true;
    }

    private void doRegister() {
        // TODO 回来加上登陆算法和注册算法，同时加入各种事件，不要忘记加入提醒密码错误或者用户不存在。

    }

}
