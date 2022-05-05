package com.students.app.ui.fragment;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.students.app.aop.Permissions;
import com.students.app.aop.SingleClick;
import com.students.app.app.TitleBarFragment;
import com.students.app.ui.activity.HomeActivity;
import com.students.app.R;
import com.students.app.http.glide.GlideApp;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

/**

 *    time   : 2018/10/18
 *    desc   : 消息 Fragment
 */
public final class MessageFragment extends TitleBarFragment<HomeActivity> {

    private ImageView mImageView;

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.message_fragment;
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

    @SingleClick
    @Override
    public void onClick(View view) {

    }

    @Permissions(Permission.CAMERA)
    private void requestPermission() {
        toast("获取摄像头权限成功");
    }
}