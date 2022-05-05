package com.students.app.ui.dialog;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import com.students.app.R;
import com.students.app.aop.SingleClick;
import com.students.base.BaseDialog;


/**

 *    time   : 2019/03/20
 *    desc   : 提示确认对话框
 */
public final class TipComfirmDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        private AppCompatTextView message;
        private AppCompatTextView confirm;
        private OnListener mListener;


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
        public Builder setMessage(int type) {
            if(type == 1){
                message.setText("确定移除黑名单！");
            }else {
                message.setText("确定加入黑名单");
            }
            return this;
        }

        public Builder setListener(OnListener listener){
            mListener = listener;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View view) {
            if (view == confirm) {
                mListener.onConfirm();
                dismiss();
            }
        }
    }

    public interface OnListener {

        /**
         * 确认
         */
        void onConfirm();
    }
}