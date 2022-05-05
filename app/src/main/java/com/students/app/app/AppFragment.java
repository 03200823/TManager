package com.students.app.app;

import android.util.SparseArray;
import android.view.View;

import com.students.base.BaseFragment;
import com.students.app.action.ToastAction;
import com.students.app.action.ViewHolderAction;
import com.students.app.http.model.HttpData;
import com.hjq.http.listener.OnHttpListener;

import okhttp3.Call;

/**
 * time   : 2018/10/18
 * desc   : Fragment 业务基类
 */
public abstract class AppFragment<A extends AppActivity> extends BaseFragment<A>
        implements ToastAction,
                   OnHttpListener<Object>,
                   ViewHolderAction {

    private final SparseArray<View> mViewList = new SparseArray<View>();

    @Override
    public SparseArray<View> getSparseArray() {
        return mViewList;
    }

    /**
     * 和 setContentView 对应的方法
     */
    public View getContentView() {
        return getView();
    }

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return false;
        }
        return activity.isShowDialog();
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return;
        }
        activity.showDialog();
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return;
        }
        activity.hideDialog();
    }

    /**
     * {@link OnHttpListener}
     */

    @Override
    public void onStart(Call call) {
        showDialog();
    }

    @Override
    public void onSucceed(Object result) {
        if (!(result instanceof HttpData)) {
            return;
        }
        toast(((HttpData<?>) result).getMessage());
    }

    @Override
    public void onFail(Exception e) {
        toast(e.getMessage());
    }

    @Override
    public void onEnd(Call call) {
        hideDialog();
    }
}