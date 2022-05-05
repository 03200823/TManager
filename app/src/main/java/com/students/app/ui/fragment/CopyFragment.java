package com.students.app.ui.fragment;

import com.students.app.app.AppFragment;
import com.students.app.ui.activity.CopyActivity;
import com.students.app.R;

/**

 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
public final class CopyFragment extends AppFragment<CopyActivity> {

    public static CopyFragment newInstance() {
        return new CopyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_copy;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}