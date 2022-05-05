package com.students.app.ui.activity;

import com.blankj.utilcode.util.ActivityUtils;
import com.students.app.R;
import com.students.app.app.AppActivity;

public class LoginActivity extends AppActivity {

    public static void start() {
        ActivityUtils.startActivity(LoginActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
