package com.students.app.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.DeviceUtils;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.students.app.R;
import com.students.app.action.TitleBarAction;
import com.students.app.action.ToastAction;
import com.students.app.action.ViewHolderAction;
import com.students.app.http.api.ContactDetailsApi;
import com.students.app.http.model.HttpData;
import com.students.app.ui.dialog.DialDialog;
import com.students.app.ui.dialog.DialTipsDialog;
import com.students.app.ui.dialog.WaitDialog;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.students.base.BaseActivity;
import com.students.base.BaseDialog;
import com.hjq.http.listener.OnHttpListener;

import okhttp3.Call;

/**
 * time   : 2018/10/18
 * desc   : Activity 业务基类
 */
public abstract class AppActivity extends BaseActivity
        implements ToastAction,
                   TitleBarAction,
                   OnHttpListener<Object>,
                   ViewHolderAction {
    // 电话管理者对象
    private TelephonyManager mTelephonyManager;
    // 电话状态监听者
    private PhoneStateListener mPhoneStateListener;


    private final SparseArray<View> mViewList = new SparseArray<View>();

    /** 标题栏对象 */
    private TitleBar     mTitleBar;
    /** 状态栏沉浸 */
    private ImmersionBar mImmersionBar;
    /** 加载对话框 */
    private BaseDialog   mDialog;
    /** 对话框数量 */
    private int          mDialogCount;

    @Override
    public SparseArray<View> getSparseArray() {
        return mViewList;
    }


    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        mDialogCount++;
        postDelayed(() -> {
            if (mDialogCount <= 0 || isFinishing() || isDestroyed()) {
                return;
            }

            if (mDialog == null) {
                mDialog = new WaitDialog.Builder(this)
                        .setCancelable(false)
                        .create();
            }
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }, 300);
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        if (mDialogCount > 0) {
            mDialogCount--;
        }

        if (mDialogCount != 0 || mDialog == null || !mDialog.isShowing()) {
            return;
        }

        mDialog.dismiss();
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        if (getTitleBar() != null) {
            getTitleBar().setOnTitleBarListener(this);
        }

        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init();

            // 设置标题栏沉浸
            if (getTitleBar() != null) {
                ImmersionBar.setTitleBar(this, getTitleBar());
            }
        }
    }

    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return true;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    @NonNull
    public ImmersionBar getStatusBarConfig() {
        if (mImmersionBar == null) {
            mImmersionBar = createStatusBarConfig();
        }
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式状态栏
     */
    @NonNull
    protected ImmersionBar createStatusBarConfig() {
        return ImmersionBar.with(this)
                           // 默认状态栏字体颜色为黑色
                           .statusBarDarkFont(isStatusBarDarkFont())
                           // 指定导航栏背景颜色
                           .navigationBarColor(R.color.white)
                           // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
                           .autoDarkModeEnable(true, 0.2f);
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(@StringRes int id) {
        setTitle(getString(id));
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (getTitleBar() != null) {
            getTitleBar().setTitle(title);
        }
    }

    @Override
    @Nullable
    public TitleBar getTitleBar() {
        if (mTitleBar == null) {
            mTitleBar = obtainTitleBar(getContentView());
        }
        return mTitleBar;
    }

    @Override
    public void onLeftClick(View view) {
        onBackPressed();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.right_in_activity, R.anim.right_out_activity);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in_activity, R.anim.left_out_activity);
    }

    /**
     * {@link OnHttpListener}
     */

    @Override
    public void onStart(Call call) {
        showDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPhoneStateListener();

    }


    @Override
    public void onSucceed(Object result) {
        if (result instanceof HttpData) {
            toast(((HttpData<?>) result).getMessage());
        }
    }

    @Override
    public void onFail(Exception e) {
        toast(e.getMessage());
    }

    @Override
    public void onEnd(Call call) {
        hideDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isShowDialog()) {
            hideDialog();
        }
        mDialog = null;
        mTelephonyManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
    }

    public boolean initPhoneStateListener() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                System.out.println("phoneStatus:" + state);
                switch(state){
                    case TelephonyManager.CALL_STATE_IDLE:
                        //电话挂断
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        //正在通话...
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        //正在响铃...
                        if(phoneNumber == null ||phoneNumber.isEmpty()){
                            return;
                        }else {
                            getPhoneMessage(phoneNumber);
                        }
                        break;
                }

            }
        };
        //开启监听
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        return true;
    }

    private void getPhoneMessage(String phoneNumber){
        ContactDetailsApi contactDetailsApi = new ContactDetailsApi();
        contactDetailsApi.setM_id(DeviceUtils.getAndroidID());
        contactDetailsApi.setNumber(phoneNumber);
        EasyHttp.get(this).api(contactDetailsApi).request(new HttpCallback<HttpData<ContactDetailsApi.Bean>>(this){
            @Override
            public void onSucceed(HttpData<ContactDetailsApi.Bean> result) {
                super.onSucceed(result);
                if(result.getData() != null){
                    if(result.getData().getBlackType() == 1){
                        new DialTipsDialog.Builder(getContext())
                                .setMessage("该号码为黑名单号码")
                                .show();
                    }else if(result.getData().getSign_num() >= 10){
                        new DialTipsDialog.Builder(getContext())
                                .setMessage("疑似"  + result.getData().getSign_type())
                                .show();
                    }
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }

        });
    }

}