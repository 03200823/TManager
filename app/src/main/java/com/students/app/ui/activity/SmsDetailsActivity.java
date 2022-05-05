package com.students.app.ui.activity;


import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.students.app.R;
import com.students.app.app.AppActivity;
import com.students.app.http.api.AddBlackApi;
import com.students.app.http.api.ContactDetailsApi;
import com.students.app.http.api.RemoveBlackApi;
import com.students.app.http.api.SignApi;
import com.students.app.http.api.UploadContactApi;
import com.students.app.http.model.HttpData;
import com.students.app.ui.dialog.MenuDialog;
import com.students.app.ui.dialog.TipComfirmDialog;
import com.students.base.BaseDialog;
import com.students.widget.layout.SettingBar;

import java.util.Arrays;
public class SmsDetailsActivity extends AppActivity {

    private final String TAG = SmsDetailsActivity.class.getSimpleName();

    private static String[] flagArray = new String[]{
            "骚扰电话",
            "诈骗电话",
            "快递送餐",
            "广告推销",
            "房产中介",
            "保险理财",
            "出租车",
            "其他"
    };

    private TextView nameView, numberView,signTypeAndNumber;
    private SettingBar addOrRemoveBlack,sign;
    private AppCompatImageView smsView;
    private String name,number;
    private ContactDetailsApi mApi;
    private UploadContactApi uploadContactApi;
    private AddBlackApi addBlackApi;
    private RemoveBlackApi removeBlackApi;
    private SignApi signApi;
    private int blackType =  0;


    public static void start() {
        ActivityUtils.startActivity(SmsDetailsActivity.class);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sms_details;
    }

    @Override
    protected void initView() {
        nameView = findView(R.id.name);
        numberView = findView(R.id.number);
        signTypeAndNumber = findViewById(R.id.signTypeAndNumber);
        addOrRemoveBlack = findViewById(R.id.addOrRemoveBlack);
        sign = findView(R.id.sign);
        smsView = findViewById(R.id.sms);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MenuDialog.Builder(SmsDetailsActivity.this)
                        .setGravity(Gravity.BOTTOM)
                        .setList(Arrays.asList(flagArray))
                        .setListener(new MenuDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, int position, Object o) {
                                signContact(o.toString());
//                                ToastUtils.showShort(o.toString());
                            }
                        }).show();
            }
        });
        addOrRemoveBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TipComfirmDialog.Builder(SmsDetailsActivity.this)
                        .setMessage(blackType)
                        .setListener(new TipComfirmDialog.OnListener() {
                            @Override
                            public void onConfirm() {
                                if(blackType == 1){
                                    removeBlack();
                                }else {
                                    addBlack();
                                }
                            }
                        }).show();
            }
        });


        smsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" + number);
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        mApi = new ContactDetailsApi();
        uploadContactApi = new UploadContactApi();
        addBlackApi = new AddBlackApi();
        removeBlackApi = new RemoveBlackApi();
        signApi = new SignApi();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        nameView.setText(name);
        number = intent.getStringExtra("number");
        numberView.setText(number);
        uploadContact();
        requestContactDetails();
    }

    private void requestContactDetails(){
        mApi.setM_id(DeviceUtils.getAndroidID());
        mApi.setNumber(number);
        EasyHttp.get(this).api(mApi).request(new HttpCallback<HttpData<ContactDetailsApi.Bean>>(this){
            @Override
            public void onSucceed(HttpData<ContactDetailsApi.Bean> result) {
                super.onSucceed(result);
                if(result.getData() != null){
                    refreshData(result.getData());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }

        });
    }

    private void refreshData(ContactDetailsApi.Bean bean){
        if(bean.getSign_num() == 0){
            signTypeAndNumber.setVisibility(View.INVISIBLE);
        } else {
            signTypeAndNumber.setVisibility(View.VISIBLE);
            signTypeAndNumber.setText(bean.getSign_type() +  " " + bean.getSign_num() + "人标记");
        }

        blackType = bean.getBlackType();
        if(bean.getBlackType() ==  1){
            //黑名单
            addOrRemoveBlack.setLeftText("移除黑名单");
        }else {
            addOrRemoveBlack.setLeftText("加入黑名单");
        }
    }


    private void signContact(String flag){
        signApi.setM_id(DeviceUtils.getAndroidID());
        signApi.setNumber(number);
        signApi.setSign_type(flag);
        EasyHttp.post(this).api(signApi).request(new HttpCallback<HttpData<Object>>(this){
            @Override
            public void onSucceed(HttpData<Object> result) {
                super.onSucceed(result);
                ToastUtils.showShort("标记成功");
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    private void uploadContact(){
        uploadContactApi.setM_id(DeviceUtils.getAndroidID());
        uploadContactApi.setNumber(number);
        uploadContactApi.setType("1");
        EasyHttp.post(this).api(uploadContactApi).request(new HttpCallback<HttpData<Object>>(this){
            @Override
            public void onSucceed(HttpData<Object> result) {
                super.onSucceed(result);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    private void addBlack(){
        addBlackApi.setM_id(DeviceUtils.getAndroidID());
        addBlackApi.setNumber(number);
        EasyHttp.post(this).api(addBlackApi).request(new HttpCallback<HttpData<Object>>(this){
            @Override
            public void onSucceed(HttpData<Object> result) {
                super.onSucceed(result);
                blackType = 1;
                addOrRemoveBlack.setLeftText("移除黑名单");
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    private void removeBlack(){
        removeBlackApi.setM_id(DeviceUtils.getAndroidID());
        removeBlackApi.setNumber(number);
        EasyHttp.post(this).api(removeBlackApi).request(new HttpCallback<HttpData<Object>>(this){
            @Override
            public void onSucceed(HttpData<Object> result) {
                super.onSucceed(result);
                blackType = 0;
                addOrRemoveBlack.setLeftText("加入黑名单");
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }
}