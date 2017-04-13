package com.binzeefox.materialdesignpasswordcollecter;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.*;
import android.widget.*;
import com.binzeefox.materialdesignpasswordcollecter.animation.MyAnimation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private boolean isOnRegister;
    private float toY_onLogin;
    private float toY_onRegister;


    private RelativeLayout cardHeadView;
    private CoordinatorLayout cardView;
    private FrameLayout passConfirmView;
    private TextView cardTitleField;
    private FloatingActionButton fab_action;
    private RelativeLayout bt_action;
    private TextView bt_action_text;
    private EditText userNameField;
    private EditText passwordField;
    private EditText passConfirmField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        cardHeadView = (RelativeLayout) findViewById(R.id.headView);
        cardView = (CoordinatorLayout) findViewById(R.id.card_view);
        passConfirmView = (FrameLayout) findViewById(R.id.password_confirm_view);
        cardTitleField = (TextView) findViewById(R.id.title_card);
        fab_action = (FloatingActionButton) findViewById(R.id.fab_action);
        bt_action = (RelativeLayout) findViewById(R.id.bt_action);
        bt_action_text = (TextView) findViewById(R.id.bt_action_text);
        userNameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        passConfirmField = (EditText) findViewById(R.id.password_confirm);

        fab_action.setOnClickListener(this);
        bt_action.setOnClickListener(this);

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);



        getCardSize();
        onLogin();
    }


    private void onLogin() {

        cardTitleField.setText("请登录");
        bt_action_text.setText("登入账户");
        passConfirmView.setVisibility(View.GONE);
        fab_action.setImageResource(R.drawable.ic_add_black_24dp);
        passwordField.setText("");
        passConfirmField.setText("");
        isOnRegister = false;


    }

    private void onRegiester() {
        cardTitleField.setText("注册中...");
        bt_action_text.setText("注册账户");
        passConfirmView.setVisibility(View.VISIBLE);
        passwordField.setText("");
        fab_action.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        isOnRegister = true;
    }

    @Override
    public void onClick(View v) {

        cardView.requestFocus();
        cardView.requestFocusFromTouch();

        switch (v.getId()) {
            case R.id.fab_action:
                if (isOnRegister) {
                    showAnimation(toY_onRegister);

                } else {
                    showAnimation(toY_onLogin);

                }
        }
    }

    /**
     * 获取缩放比例
     */
    private float h1;
    private float h2;
    private float h0;


    private void getCardSize() {
        onLogin();
        cardView.measure(0, 0);
        h1 = cardView.getMeasuredHeight();


        onRegiester();
        cardView.measure(0, 0);
        h2 = cardView.getMeasuredHeight();

        toY_onLogin = h2 - h1;
        toY_onRegister = h1 - h2;
        h0 = h1 / h2;
        Log.d("h1 value", String.valueOf(h1));
        Log.d("h2 value", String.valueOf(h2));
    }

    /**
     * 动画
     */
    private void showAnimation(final float h) {


        cardView.measure(0, 0);


        // 按钮及标题动画
        MyAnimation.AlphaAct(fab_action, 1f, 0)
                .setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        MyAnimation.AlphaAct(cardTitleField, 1f, 0);


                        MyAnimation.changeTextAlpha(bt_action_text, false, 250)
                                .addListener(new AnimatorListenerAdapter() {

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        String text;
                                        if (isOnRegister){
                                            text = "登入账户";
                                        } else {
                                            text = "注册账户";
                                        }
                                        bt_action_text.setText(text);
                                        MyAnimation.changeTextAlpha(bt_action_text, true, 250);
                                    }
                                });
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (isOnRegister) {
                            onLogin();
                        } else {
                            onRegiester();
                        }
                        cardTitleField.setVisibility(View.VISIBLE);
                        MyAnimation.AlphaAct(cardTitleField, 0, 1f);
                        MyAnimation.AlphaAct(fab_action, 0, 1f);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

        // 卡片外观动画



    }

}
