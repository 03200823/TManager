package com.students.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ActivityUtils;
import com.students.app.R;
import com.students.app.app.AppActivity;
import com.students.app.app.AppFragment;
import com.students.app.other.DoubleClickHelper;
import com.students.app.ui.adapter.NavigationAdapter;
import com.students.app.ui.fragment.ContactFragment;
import com.students.app.ui.fragment.FindFragment;
import com.students.app.ui.fragment.HomeFragment;
import com.students.app.ui.fragment.MessageFragment;
import com.students.app.ui.fragment.MineFragment;
import com.gyf.immersionbar.ImmersionBar;
import com.students.app.ui.fragment.SMSFragment;
import com.students.base.FragmentPagerAdapter;

/**
 * time   : 2018/10/18
 * desc   : 首页界面
 */
public final class HomeActivity extends AppActivity implements NavigationAdapter.OnNavigationListener {

    private static final String INTENT_KEY_IN_FRAGMENT_INDEX = "fragmentIndex";
    private static final String INTENT_KEY_IN_FRAGMENT_CLASS = "fragmentClass";

    private ViewPager    mViewPager;
    private RecyclerView mNavigationView;

    private NavigationAdapter                    mNavigationAdapter;
    private FragmentPagerAdapter<AppFragment<?>> mPagerAdapter;


    public static void start() {
        ActivityUtils.startActivity(HomeActivity.class);
    }

    public static void start(Context context, Class<? extends AppFragment<?>> fragmentClass) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(INTENT_KEY_IN_FRAGMENT_CLASS, fragmentClass);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        mViewPager      = findViewById(R.id.vp_home_pager);
        mNavigationView = findViewById(R.id.rv_home_navigation);

        mNavigationAdapter = new NavigationAdapter(this);
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem("联系人",
                ContextCompat.getDrawable(this, R.drawable.home_home_selector)));
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem("短信",
                ContextCompat.getDrawable(this, R.drawable.home_found_selector)));
//        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.消息),
//                ContextCompat.getDrawable(this, R.drawable.home_message_selector)));
//        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.我的),
//                ContextCompat.getDrawable(this, R.drawable.home_me_selector)));
        mNavigationAdapter.setOnNavigationListener(this);
        mNavigationView.setAdapter(mNavigationAdapter);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new FragmentPagerAdapter<>(this);
        mPagerAdapter.addFragment(ContactFragment.newInstance());
        mPagerAdapter.addFragment(SMSFragment.newInstance());
//        mPagerAdapter.addFragment(MessageFragment.newInstance());
//        mPagerAdapter.addFragment(MineFragment.newInstance());
        mViewPager.setAdapter(mPagerAdapter);

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        switchFragment(mPagerAdapter.getFragmentIndex(getSerializable(INTENT_KEY_IN_FRAGMENT_CLASS)));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存当前 Fragment 索引位置
        outState.putInt(INTENT_KEY_IN_FRAGMENT_INDEX, mViewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // 恢复当前 Fragment 索引位置
        switchFragment(savedInstanceState.getInt(INTENT_KEY_IN_FRAGMENT_INDEX));
    }

    private void switchFragment(int fragmentIndex) {
        if (fragmentIndex == -1) {
            return;
        }

        switch (fragmentIndex) {
            case 0:
            case 1:
//            case 2:
//            case 3:
                mViewPager.setCurrentItem(fragmentIndex);
                mNavigationAdapter.setSelectedPosition(fragmentIndex);
                break;
            default:
                break;
        }
    }

    /**
     * {@link NavigationAdapter.OnNavigationListener}
     */

    @Override
    public boolean onNavigationItemSelected(int position) {
        switch (position) {
            case 0:
            case 1:
//            case 2:
//            case 3:
                mViewPager.setCurrentItem(position);
                return true;
            default:
                return false;
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                    // 指定导航栏背景颜色
                    .navigationBarColor(R.color.white);
    }

    @Override
    public void onBackPressed() {
        if (!DoubleClickHelper.isOnDoubleClick()) {
            toast(R.string.再按一次退出);
            return;
        }

        // 移动到上一个任务栈，避免侧滑引起的不良反应
        moveTaskToBack(false);
        // 进行内存优化，销毁掉所有的界面
        // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
        // System.exit(0);
        postDelayed(ActivityUtils::finishAllActivities, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.setAdapter(null);
        mNavigationView.setAdapter(null);
        mNavigationAdapter.setOnNavigationListener(null);
    }
}