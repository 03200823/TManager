package com.students.app.ui.dialog;

import android.content.Context;

import com.students.app.R;
import com.students.base.BaseDialog;

/**

 *    time   : 2018/12/2
 *    desc   : 等待加载对话框
 */
public final class WaitDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.dialog_wait);
            setAnimStyle(BaseDialog.ANIM_TOAST);
            setBackgroundDimEnabled(false);
            setCancelable(false);

        }

    }
}