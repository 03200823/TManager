package com.students.app.ui.dialog;

import android.content.Context;
import android.os.IBinder;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.students.app.R;
import com.students.app.aop.SingleClick;
import com.students.base.BaseDialog;


/**

 *    time   : 2019/03/20
 *    desc   : 提示确认对话框
 */
public final class DialTipsDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        private AppCompatTextView message,confirm;


        public Builder(Context context) {
            super(context);

            setContentView(R.layout.dialog_message_confirm);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);
            setCancelable(true);

            message = findViewById(R.id.message);
            confirm = findViewById(R.id.confirm);

            setOnClickListener(confirm);
        }

        /**
         * 设置Message
         */
        public Builder setMessage(String msg) {
            message.setText(msg);
            return this;
        }


        @SingleClick
        @Override
        public void onClick(View view) {
            if (view == confirm) {

                dismiss();
            }
        }
    }


}