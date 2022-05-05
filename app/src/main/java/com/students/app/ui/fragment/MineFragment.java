package com.students.app.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.View;

import com.students.app.R;
import com.students.app.aop.SingleClick;
import com.students.app.app.TitleBarFragment;
import com.students.app.ui.activity.BrowserActivity;
import com.students.app.ui.activity.HomeActivity;
import com.students.app.ui.activity.VideoPlayActivity;
import com.students.app.ui.activity.VideoSelectActivity;
import com.students.app.ui.dialog.InputDialog;
import com.students.app.ui.dialog.MessageDialog;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

/**
 * time   : 2018/10/18
 * desc   : 我的 Fragment
 */
public final class MineFragment extends TitleBarFragment<HomeActivity> {

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}