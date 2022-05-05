package com.students.app.ui.activity;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ActivityUtils;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.students.app.R;
import com.students.app.app.AppActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private List<String> unPermissionList = new ArrayList<String>();
    private Uri callUri = CallLog.Calls.CONTENT_URI;
    private String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型}

    private String[] permissionList = new String[]{    //申请的权限列表
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG
    };

    public static void start() {
        ActivityUtils.startActivity(MainActivity.class);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
//        checkPermission();
    }

    @Override
    protected void initData() {
        XXPermissions.with(this)
                // 申请单个权限
                .permission(Permission.READ_CALL_LOG)
                .permission(Permission.READ_SMS)

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
                            toast("获取录音和日历权限成功");
                            getContacts();
                        } else {
                            toast("获取部分权限成功，但部分权限未正常授予");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            toast("被永久拒绝授权，请手动授予录音和日历权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                        } else {
                            toast("获取录音和日历权限失败");
                        }
                    }
                });
    }

    private void getContacts(){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                getConnect();
//                getContentCallLog();
                readSms();
            }
        }).start();
    }


    //获取通话记录
    private void getContentCallLog() {
        Cursor cursor = getContentResolver().query(callUri, // 查询通话记录的URI
                columns
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        Log.i(TAG, "cursor count:" + cursor.getCount());
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //姓名
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2.呼出3.未接
            String dayCurrent = new SimpleDateFormat("dd").format(new Date());
            String dayRecord = new SimpleDateFormat("dd").format(new Date(dateLong));

            Log.i(TAG, "Call log: " + "\n"
                    + "name: " + name + "\n"
                    + "phone number: " + number + "\n"

            );
        }
    }

    //获取联系人
    private void getConnect() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { "display_name", "sort_key", "contact_id",
                        "data1" }, null, null, null);
        Log.i(TAG,"cursor connect count:" + cursor.getCount());
//        moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Log.i(TAG,"获取的通讯录是： " + name + "\n"
                    +  " number : " + number);
        }
        cursor.close();
    }

    private void readSms(){
        Uri uri = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContentResolver();

        String[] projection = new String[] { "_id", "address", "person","body", "date", "type" };


        Cursor cursor = cr.query(uri, projection, null, null, "date desc");

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

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd " + "\n" + "hh:mm:ss");

            String date = dateFormat.format(d);
            Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,phoneNumber);

            Cursor localCursor = cr.query(personUri, new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_ID, ContactsContract.PhoneLookup._ID }, null, null, null);
            boolean hasData = localCursor.moveToFirst();
            if(hasData){
                String name = localCursor.getString(localCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                Log.d(TAG,name + "  " + phoneNumber);

            }    else {
                Log.d(TAG," = null" + "  " + phoneNumber);
            }

        }
    }
}