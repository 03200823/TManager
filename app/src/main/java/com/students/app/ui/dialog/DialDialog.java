package com.students.app.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.students.app.R;
import com.students.app.aop.SingleClick;
import com.students.app.app.AppAdapter;
import com.students.base.BaseAdapter;
import com.students.base.BaseDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**

 *    time   : 2018/12/2
 *    desc   : 菜单选择框
 */
public final class DialDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        @SuppressWarnings("rawtypes")
        private boolean mAutoDismiss = true;

        private TextView number0,number1,number2,number3,number4,number5,number6,number7,number8,number9;
        private ImageView delete,call;
        private TextView phoneNumber;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.dialog_dial);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);
            number0 = findViewById(R.id.number0);
            number1 = findViewById(R.id.number1);
            number2 = findViewById(R.id.number2);
            number3 = findViewById(R.id.number3);
            number4 = findViewById(R.id.number4);
            number5 = findViewById(R.id.number5);
            number6 = findViewById(R.id.number6);
            number7 = findViewById(R.id.number7);
            number8 = findViewById(R.id.number8);
            number9= findViewById(R.id.number9);
            delete = findViewById(R.id.delete);
            phoneNumber = findViewById(R.id.phoneNumber);
            call = findViewById(R.id.makeCall);
            setOnClickListener(number0,number1,number2,number3,number4,number5,number6,number7,number8,number9,number0,call,delete);

        }

        @Override
        public Builder setGravity(int gravity) {
            switch (gravity) {
                // 如果这个是在中间显示的
                case Gravity.CENTER:
                case Gravity.CENTER_VERTICAL:
                    // 不显示取消按钮
                    // 重新设置动画
                    setAnimStyle(BaseDialog.ANIM_SCALE);
                    break;
                default:
                    break;
            }
            return super.setGravity(gravity);
        }

        @SingleClick
        @Override
        public void onClick(View view) {
           switch (view.getId()){
               case R.id.number0:
                   phoneNumber.append("0");
                   break;
               case R.id.number1:
                   phoneNumber.append("1");
                   break;
               case R.id.number2:
                   phoneNumber.append("2");
                   break;
               case R.id.number3:
                   phoneNumber.append("3");
                   break;
               case R.id.number4:
                   phoneNumber.append("4");
                   break;
               case R.id.number5:
                   phoneNumber.append("5");
                   break;
               case R.id.number6:
                   phoneNumber.append("6");
                   break;
               case R.id.number7:
                   phoneNumber.append("7");
                   break;
               case R.id.number8:
                   phoneNumber.append("8");
                   break;
               case R.id.number9:
                   phoneNumber.append("9");
                   break;
               case R.id.delete:
                   String phone = phoneNumber.getText().toString();
                   if(!TextUtils.isEmpty(phone)){
                      phone = phone.substring(0,phone.length() -1);
                      phoneNumber.setText(phone);
                   }
                   break;
               case R.id.makeCall:
                   String callPhone = phoneNumber.getText().toString();
                   if(!TextUtils.isEmpty(callPhone)){
                       Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+callPhone));
                       getContext().startActivity(intent);
                   }
                   break;
           }

        }

        /**
         *  获取屏幕的高度
         */
        private int getScreenHeight() {
            Resources resources = getResources();
            DisplayMetrics outMetrics = resources.getDisplayMetrics();
            return outMetrics.heightPixels;
        }
    }
}