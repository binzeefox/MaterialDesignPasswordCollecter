package com.binzeefox.materialdesignpasswordcollecter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.binzeefox.materialdesignpasswordcollecter.db.Account;
import com.binzeefox.materialdesignpasswordcollecter.db.User;
import com.binzeefox.materialdesignpasswordcollecter.fragment.UserAboutFragment;
import com.binzeefox.materialdesignpasswordcollecter.fragment.UserHomeFragment;
import com.binzeefox.materialdesignpasswordcollecter.fragment.UserSearchFragment;
import com.binzeefox.materialdesignpasswordcollecter.fragment.UserSettingFragment;
import com.binzeefox.materialdesignpasswordcollecter.util.ActivityCollector;
import com.binzeefox.materialdesignpasswordcollecter.util.BaseActivity;
import org.litepal.crud.DataSupport;

import java.util.List;

import static org.litepal.crud.DataSupport.find;

public class UserActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    // 用户数据库ID
    public static int userID;
    // fragment区域
    private FrameLayout fragmentPlace;
    // home列表
    RecyclerView homeCards;
    private FloatingActionButton fab_create;
    public static String userName;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TextView navTitle = (TextView) findViewById(R.id.nav_username);
            switch (msg.what) {
                case 0:
                    navTitle.setText(queryUserName(userID));
                    break;
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab_create = (FloatingActionButton) findViewById(R.id.fab_create);
        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnClickFab();
            }
        });
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
        replaceFragment(new UserHomeFragment());
    }

    /**
     * 菜单按钮监听
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
     *
     * @param id 输入用户名数据库ID
     * @return 返回是否成功
     */
    private String queryUserName(int id) {
        User user = DataSupport.find(User.class, id);
        userName = user.getUserName();
        String mUserName = userName;

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String content = pref.getString(mUserName, null);
        if (content == null) {
            mUserName = mUserName.toUpperCase();
        } else {
            mUserName = content;
        }
        return mUserName;
    }

    /**
     * 初始化侧拉菜单并设置菜单点击事件
     */
    private int pin = 0;

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
        if (count == 0) {
            nav_count.setText("您还尚未创建条目，赶快前往记录吧");
        } else {
            nav_count.setText("您已记录了" + String.valueOf(queryAccountCount(userID)) + "条信息");
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if (item.getItemId() != R.id.nav_create) {
//                    mDrawerLayout.closeDrawers();
//                }
                handleNavSwitch(item);
                return true;
            }

            // 判断选中的菜单
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            private void handleNavSwitch(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        replaceFragment(new UserHomeFragment());
                        toolbar.setTitle("您的账号");
                        fab_create.setVisibility(View.VISIBLE);
                        pin = 0;
                        break;
                    case R.id.nav_search:
                        replaceFragment(new UserSearchFragment());
                        toolbar.setTitle("搜索账号");
                        fab_create.setVisibility(View.GONE);
                        pin++;
                        break;
                    case R.id.nav_create:
                        //TODO 添加算法
                        getWindow().setExitTransition(new Explode());
                        getWindow().setEnterTransition(new Explode());
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(UserActivity.this);
                        Intent intent = new Intent(UserActivity.this, CreateItemActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent, options.toBundle());
                        break;
                    case R.id.nav_setting:
                        replaceFragment(new UserSettingFragment());
                        toolbar.setTitle("设置");
                        fab_create.setVisibility(View.GONE);
                        pin++;
                        break;
                    case R.id.nav_about:
                        replaceFragment(new UserAboutFragment());
                        toolbar.setTitle("关于");
                        fab_create.setVisibility(View.GONE);
                        pin++;
                        break;
                    case R.id.nav_exit:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(UserActivity.this);
                        dialog.setTitle("确定吗？");
                        dialog.setMessage("即将退出程序，您需要重新登入才能继续使用");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCollector.finishAll();
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
        });
    }

    private int queryAccountCount(int id) {

        List<Account> accounts = DataSupport.where("userID = ?", String.valueOf(id)).find(Account.class);
        if (accounts != null) {
            return accounts.size();
        } else {
            return 0;
        }
    }

    /**
     * 跳转页面
     *
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
     * 通过fab跳转入添加界面
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void handleOnClickFab(){
        getWindow().setExitTransition(new Explode());
        getWindow().setEnterTransition(new Explode());
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(UserActivity.this, fab_create, getString(R.string.user_fab));
        Intent intent = new Intent(UserActivity.this, CreateItemActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent, options.toBundle());
    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        if (pin != 0) {
            replaceFragment(new UserHomeFragment());
            toolbar.setTitle("您的账号");
            fab_create.setVisibility(View.VISIBLE);
            pin = 0;
        } else {
            super.onBackPressed();
        }
    }

}
