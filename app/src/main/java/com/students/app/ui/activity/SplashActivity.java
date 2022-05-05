package com.students.app.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SPStaticUtils;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.students.app.R;
import com.students.app.app.AppActivity;
import com.students.app.utils.ConstantUtils;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.List;

/**
 * time   : 2018/10/18
 * desc   : 闪屏界面
 */
public final class SplashActivity extends AppActivity {


    private CountDownTimer mDownTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        setOnClickListener(R.id.tv_time);
    }

    @Override
    protected void initData() {
        // 倒计时
        mDownTimer = new CountDownTimer(4000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                setText(R.id.tv_time, String.format(getString(R.string.跳过), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                HomeActivity.start();

            }

        }.start();
    }

    @Override
    public void onClick(View view) {
        mDownTimer.onFinish();
    }

    @NonNull
    @Override

    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                    // 隐藏状态栏和导航栏
                    .hideBar(BarHide.FLAG_HIDE_BAR);
    }


    @Override
    public void onBackPressed() {
        //禁用返回键
        //super.onBackPressed();
    }

    @Override
    protected void initActivity() {
        // 问题及方案：https://www.cnblogs.com/net168/p/5722752.html
        // 如果当前 Activity 不是任务栈中的第一个 Activity
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            // 如果当前 Activity 是通过桌面图标启动进入的
            if (intent != null && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && Intent.ACTION_MAIN.equals(intent.getAction())) {
                // 对当前 Activity 执行销毁操作，避免重复实例化入口
                finish();
                return;
            }
        }
        super.initActivity();
    }

    @Deprecated
    @Override
    protected void onDestroy() {
        // 因为修复了一个启动页被重复启动的问题，所以有可能 Activity 还没有初始化完成就已经销毁了
        // 所以如果需要在此处释放对象资源需要先对这个对象进行判空，否则可能会导致空指针异常
        super.onDestroy();
        mDownTimer.cancel();
    }

}