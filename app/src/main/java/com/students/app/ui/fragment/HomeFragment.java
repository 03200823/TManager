package com.students.app.ui.fragment;

import androidx.viewpager.widget.ViewPager;

import com.students.app.app.TitleBarFragment;
import com.students.app.ui.activity.HomeActivity;
import com.students.app.R;

/**
 * time   : 2018/10/18
 * desc   : 首页 Fragment
 */
public final class HomeFragment extends TitleBarFragment<HomeActivity> {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }


    /**
     * {@link ViewPager.OnPageChangeListener}
     */

}