package com.students.app.ui.fragment;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.students.app.aop.SingleClick;
import com.students.app.app.TitleBarFragment;
import com.students.app.ui.activity.HomeActivity;
import com.students.app.R;
import com.students.app.ui.adapter.ContactAdapter;
import com.students.widget.view.SwitchButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**

 *    time   : 2018/10/18
 *    desc   : 发现 Fragment
 */
public final class FindFragment extends TitleBarFragment<HomeActivity>
        implements SwitchButton.OnCheckedChangeListener {


    private final static String TAG = FindFragment.class.getSimpleName();
    private ContactAdapter adapter;
    private RecyclerView recyclerView;
    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.contact_fragment;
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
     * {@link SwitchButton.OnCheckedChangeListener}
     */

    @Override
    public void onCheckedChanged(SwitchButton button, boolean checked) {
        toast(checked);
    }
}