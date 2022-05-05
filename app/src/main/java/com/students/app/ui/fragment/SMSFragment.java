package com.students.app.ui.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.students.app.R;
import com.students.app.action.StatusAction;
import com.students.app.aop.SingleClick;
import com.students.app.app.TitleBarFragment;
import com.students.app.bean.ContactBean;
import com.students.app.ui.activity.ContactDetailsActivity;
import com.students.app.ui.activity.HomeActivity;
import com.students.app.ui.activity.SmsDetailsActivity;
import com.students.app.ui.adapter.ContactAdapter;
import com.students.app.widget.StatusLayout;
import com.students.widget.view.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * time   : 2018/10/18
 * desc   : 短信号码 Fragment
 */
public final class SMSFragment extends TitleBarFragment<HomeActivity>
        implements SwitchButton.OnCheckedChangeListener, StatusAction {

    private final static String TAG = SMSFragment.class.getSimpleName();
    private ContactAdapter adapter;
    private RecyclerView recyclerView;
    private List<ContactBean> smsList;
    private String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
    private  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

    public static SMSFragment newInstance() {
        return new SMSFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.contact_fragment;
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.contacts);
        adapter = new ContactAdapter();
        recyclerView.setAdapter(adapter);
        setTitle("短信号码");
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ContactBean contactBean = (ContactBean)adapter.getItem(position);
                Intent intent = new Intent(getContext(), SmsDetailsActivity.class);
                intent.putExtra("number", contactBean.getNumber());
                intent.putExtra("name", contactBean.getName());
                startActivity(intent);
            }
        });
        requestPermission();
    }

    @Override
    protected void initData() {

    }

    private void loadSmsData() {
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                smsList = readSms();
                getAttachActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showComplete();
                        adapter.setNewInstance(smsList);
                    }
                });
            }
        }).start();
    }

    private void requestPermission() {
        XXPermissions.with(this)
                // 申请单个权限
                .permission(Permission.READ_CALL_LOG)
                .permission(Permission.READ_SMS)
                .permission(Permission.CALL_PHONE)
                // 申请多个权限
                .permission(Permission.Group.CONTACTS)
                // 设置权限请求拦截器（局部设置）
                //.interceptor(new PermissionInterceptor())
                // 设置不触发错误检测机制（局部设置）
                //.unchecked()
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            loadSmsData();
                        } else {
                            toast("请授权相关通讯录权限,保证功能使用");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        toast("请授权相关通讯录权限,保证功能使用");
                    }
                });
    }

    @SingleClick
    @Override
    public void onClick(View view) {

    }

    //获取联系人
    private List<ContactBean> readSms() {
        Uri uri = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContext().getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cursor = cr.query(uri, projection, null, null, "date desc");
        List<ContactBean> contactBeans = new ArrayList<>();
        while (cursor.moveToNext()) {
            int nameColumn = cursor.getColumnIndex("person");// 联系人姓名列表序号
            int phoneNumberColumn = cursor.getColumnIndex("address");// 手机号
            int smsbodyColumn = cursor.getColumnIndex("body");// 短信内容
            int dateColumn = cursor.getColumnIndex("date");// 日期
            int typeColumn = cursor.getColumnIndex("type");// 收发类型 1表示接受 2表示发送
            String nameId = cursor.getString(nameColumn);
            String phoneNumber = cursor.getString(phoneNumberColumn);
            String smsbody = cursor.getString(smsbodyColumn);
            Date d = new Date(Long.parseLong(cursor.getString(dateColumn)));

            String date = dateFormat.format(d);
            String time = timeFormat.format(d);

            Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNumber);
            ContactBean contactBean = new ContactBean();
            Cursor localCursor = cr.query(personUri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_ID, ContactsContract.PhoneLookup._ID}, null, null, null);
            boolean hasData = localCursor.moveToFirst();
            if (hasData) {
                String name = localCursor.getString(localCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                contactBean.setName(name);
            }
            contactBean.setNumber(phoneNumber);
            if(currentDate.contentEquals(date)){
                contactBean.setTime(time);
            }else {
                contactBean.setTime(date);
            }
            if(!contactBeans.contains(contactBean)){
                contactBeans.add(contactBean);
            }
        }

        return contactBeans;
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

    @Override
    public StatusLayout getStatusLayout() {
        return getSta(R.id.sta_layout);
    }
}