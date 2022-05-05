package com.students.app.ui.activity;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.telecom.Call;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.students.app.R;
import com.students.app.app.AppActivity;
import com.students.app.bean.CallRecordBean;
import com.students.app.http.api.AddBlackApi;
import com.students.app.http.api.ContactDetailsApi;
import com.students.app.http.api.RemoveBlackApi;
import com.students.app.http.api.SignApi;
import com.students.app.http.api.UploadContactApi;
import com.students.app.http.model.HttpData;
import com.students.app.ui.adapter.CallRecordAdapter;
import com.students.app.ui.dialog.DialDialog;
import com.students.app.ui.dialog.MenuDialog;
import com.students.app.ui.dialog.TipComfirmDialog;
import com.students.base.BaseDialog;
import com.students.widget.layout.SettingBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ContactDetailsActivity extends AppActivity {

    private final String TAG = ContactDetailsActivity.class.getSimpleName();
    private Uri callUri = CallLog.Calls.CONTENT_URI;
    private String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型
    private List<CallRecordBean> callRecordBeans;

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
    private RecyclerView recordCallView;
    private AppCompatImageView callView,smsView;
    private CallRecordAdapter mAdapter;
    private String name,number;
    private ContactDetailsApi mApi;
    private UploadContactApi uploadContactApi;
    private AddBlackApi addBlackApi;
    private RemoveBlackApi removeBlackApi;
    private SignApi signApi;
    private int blackType =  0;


    public static void start() {
        ActivityUtils.startActivity(ContactDetailsActivity.class);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_details;
    }

    @Override
    protected void initView() {
        nameView = findView(R.id.name);
        numberView = findView(R.id.number);
        recordCallView = findViewById(R.id.recordCall);
        signTypeAndNumber = findViewById(R.id.signTypeAndNumber);
        addOrRemoveBlack = findViewById(R.id.addOrRemoveBlack);
        sign = findView(R.id.sign);
        callView = findViewById(R.id.call);
        smsView = findViewById(R.id.sms);
        mAdapter = new CallRecordAdapter();
        recordCallView.setAdapter(mAdapter);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MenuDialog.Builder(ContactDetailsActivity.this)
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
                new TipComfirmDialog.Builder(ContactDetailsActivity.this)
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


        callView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialDialog.Builder(ContactDetailsActivity.this)
                        .setGravity(Gravity.BOTTOM)
                        .setBackgroundDimEnabled(true)
                        .setBackgroundDimAmount(0)
                        .show();
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

        callRecordBeans = GsonUtils.fromJson(intent.getStringExtra("record"),new TypeToken<List<CallRecordBean>>(){}.getType());
        mAdapter.setNewInstance(callRecordBeans);
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

    private List<CallRecordBean> getContentCallLog() {
        Cursor cursor = getContentResolver().query(callUri, // 查询通话记录的URI
                columns
                , CallLog.Calls.NUMBER + "=" + number, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        List<CallRecordBean> callRecordBeans = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //姓名
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2.呼出3.未接 5 拒接
            String dayCurrent = new SimpleDateFormat("dd").format(new Date());
            String dayRecord = new SimpleDateFormat("dd").format(new Date(dateLong));

            CallRecordBean callRecordBean = new CallRecordBean();
            callRecordBean.setType(type);
            callRecordBean.setDuring(duration);
            callRecordBean.setTime(dateLong);
            callRecordBeans.add(callRecordBean);
        }
        cursor.close();
        return callRecordBeans;
    }
}