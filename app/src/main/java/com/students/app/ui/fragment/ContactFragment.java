package com.students.app.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.students.app.R;
import com.students.app.action.StatusAction;
import com.students.app.aop.SingleClick;
import com.students.app.app.TitleBarFragment;
import com.students.app.bean.CallRecordBean;
import com.students.app.bean.ContactBean;
import com.students.app.ui.activity.ContactDetailsActivity;
import com.students.app.ui.activity.HomeActivity;
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

 *    time   : 2018/10/18
 *    desc   : 发现 Fragment
 */
public final class ContactFragment extends TitleBarFragment<HomeActivity>
        implements SwitchButton.OnCheckedChangeListener, StatusAction {

    private final static String TAG = ContactFragment.class.getSimpleName();
    private ContactAdapter adapter;
    private RecyclerView recyclerView;
    private List<ContactBean> contacts;
    private Uri callUri = CallLog.Calls.CONTENT_URI;
    private String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型
    private String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
    private Map<String,List<CallRecordBean>> map;

    public static ContactFragment newInstance() {
        return new ContactFragment();
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
        setTitle("通话记录");
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ContactBean contactBean = (ContactBean)adapter.getItem(position);
                Intent intent = new Intent(getContext(),ContactDetailsActivity.class);
                intent.putExtra("number",contactBean.getNumber());
                intent.putExtra("name",contactBean.getName());
                intent.putExtra("record", GsonUtils.toJson(map.get(contactBean.getNumber())));
                startActivity(intent);
            }
        });
        requestPermission();
    }

    @Override
    protected void initData() {

    }


    private void loadContact(){
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                contacts = getContentCallLog();
                getAttachActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showComplete();
                        adapter.setNewInstance(contacts);
                    }
                });
            }
        }).start();
    }

    private void requestPermission(){
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
                        if(all){
                            loadContact();
                        }else {
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
    private Map<String,String> getConnect() {
        Map<String,String> contacts = new LinkedHashMap<>();
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { "display_name", "sort_key", "contact_id",
                        "data1" }, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED");
        Log.i(TAG,"cursor connect count:" + cursor.getCount());
//        moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(TextUtils.isEmpty(name)){
                name = number;
            }
            if(!TextUtils.isEmpty(number)){
                contacts.put(name,number);
            }
        }
        cursor.close();
        return contacts;
    }

    private List<ContactBean> getContentCallLog() {
        Cursor cursor = getContext().getContentResolver().query(callUri, // 查询通话记录的URI
                columns
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        List<ContactBean> contactBeans = new ArrayList<>();
        map = new LinkedHashMap<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //姓名
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2.呼出3.未接
            String dayCurrent = new SimpleDateFormat("dd").format(new Date());
            String dayRecord = new SimpleDateFormat("dd").format(new Date(dateLong));
            ContactBean contactBean = new ContactBean();
            contactBean.setNumber(number);
            contactBean.setName(name);
            contactBean.setType(type);
            if(currentDate.contentEquals(date)){
                contactBean.setTime(time);
            }else {
                contactBean.setTime(date);
            }
            if(!contactBeans.contains(contactBean)){
                contactBeans.add(contactBean);
            }
            List<CallRecordBean> callRecordBeans = map.get(number);
            CallRecordBean callRecordBean = new CallRecordBean();
            callRecordBean.setType(type);
            callRecordBean.setDuring(duration);
            callRecordBean.setTime(dateLong);
            if(callRecordBeans == null){
                callRecordBeans = new ArrayList<>();
            }
            callRecordBeans.add(callRecordBean);
            map.put(number,callRecordBeans);

        }
        cursor.close();
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