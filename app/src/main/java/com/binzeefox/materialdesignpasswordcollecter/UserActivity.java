package com.binzeefox.materialdesignpasswordcollecter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.binzeefox.materialdesignpasswordcollecter.adapter.CardAdapter;
import com.binzeefox.materialdesignpasswordcollecter.db.Account;
import com.binzeefox.materialdesignpasswordcollecter.db.User;
import com.binzeefox.materialdesignpasswordcollecter.fragment.UserAboutFragment;
import com.binzeefox.materialdesignpasswordcollecter.fragment.UserHomeFragment;
import com.binzeefox.materialdesignpasswordcollecter.fragment.UserSearchFragment;
import com.binzeefox.materialdesignpasswordcollecter.fragment.UserSettingFragment;
import com.binzeefox.materialdesignpasswordcollecter.model.Card;
import com.binzeefox.materialdesignpasswordcollecter.util.ActivityCollector;
import com.binzeefox.materialdesignpasswordcollecter.util.BaseActivity;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.crud.DataSupport.find;

public class UserActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    // 用户数据库ID
    private int userID;
    // fragment区域
    private FrameLayout fragmentPlace;
    // home列表
    RecyclerView homeCards;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeCards = (RecyclerView) findViewById(R.id.home_cards);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_format_list_bulleted_black_24dp);
        }

        // 获取登入用户数据库ID
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", 0);

        // 初始化侧拉菜单
        initNavView();

        // 注册fragment区域
        fragmentPlace = (FrameLayout) findViewById(R.id.fragment_place);

    }

    /**
     * 菜单按钮监听
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获取并用户名
     * @param id 输入用户名数据库ID
     * @return 返回是否成功
     */
    private String queryUserName(int id) {
        User user = DataSupport.find(User.class, id);
        String userName = user.getUserName();

        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String content = pref.getString(userName,null);
        if (content == null){
            userName = userName.toUpperCase();
        } else {
            userName = content;
        }
        return userName;
    }

    /**
     * 设置昵称
     * @param newName 昵称
     * @return 返回是否成功
     */
    private boolean putUserName(String newName) {
        User user = DataSupport.find(User.class, userID);
        String userName = user.getUserName();

        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString(userName, newName);
        editor.apply();
        return true;
    }

    /**
     * 初始化侧拉菜单并设置菜单点击事件
     */
    private  int pin = 0;
    private void initNavView() {
        // 侧拉菜单标题
        TextView nav_title;
        // 用户数目显示
        TextView nav_count;
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        nav_title = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_username);
        nav_count = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_count);
        nav_title.setText(queryUserName(userID));
        int count = queryAccountCount(userID);
        if (count == 0){
            nav_count.setText("您还尚未创建条目，赶快前往记录吧");
        } else {
            nav_count.setText("您已记录了" + String.valueOf(queryAccountCount(userID)) + "条信息");
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                handleNavSwitch(item.getItemId());
                return true;
            }

            // 判断选中的菜单
            private void handleNavSwitch(int id) {

                switch (id) {
                    case R.id.nav_home:
                        //TODO 主页算法
                        replaceFragment(new UserHomeFragment());
                        toolbar.setTitle("您的账号");
                        pin = 0;
                        break;
                    case R.id.nav_search:
                        //TODO 搜索算法
                        replaceFragment(new UserSearchFragment());
                        toolbar.setTitle("搜索账号");
                        pin++;
                        break;
                    case R.id.nav_setting:
                        //TODO 设置算法
                        replaceFragment(new UserSettingFragment());
                        toolbar.setTitle("设置");
                        pin++;
                        break;
                    case R.id.nav_about:
                        //TODO 关于算法
                        replaceFragment(new UserAboutFragment());
                        toolbar.setTitle("关于");
                        pin++;
                        break;
                    case R.id.nav_exit:
                        ActivityCollector.finishAll();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private int queryAccountCount(int id) {

        List<Account> accounts = DataSupport.where("userID = ?",String.valueOf(id)).find(Account.class);
        if (accounts != null){
            return accounts.size();
        } else {
            return 0;
        }
    }

    /**
     * 跳转页面
     * @param fragment 目标碎片
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.fragment_place, fragment);
        transaction.commit();
    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        if (pin != 0){
            replaceFragment(new UserHomeFragment());
            pin = 0;
        } else {
            super.onBackPressed();
        }
    }
}
