package com.students.app.ui.popup;

import android.content.Context;

import com.students.base.BasePopupWindow;
import com.students.app.R;

/**

 *    time   : 2019/10/18
 *    desc   : 可进行拷贝的副本
 */
public final class CopyPopup {

    public static final class Builder
            extends BasePopupWindow.Builder<Builder> {

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.popup_copy);
        }
    }
}