package com.students.app.other;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.blankj.utilcode.util.ActivityUtils;
import com.students.app.R;
import com.students.app.ui.dialog.MessageDialog;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**

 *    time   : 2020/10/24
 *    desc   : 权限申请回调封装
 */
public abstract class PermissionCallback implements OnPermissionCallback {

    @Override
    public void onDenied(List<String> permissions, boolean never) {
        if (never) {
            showPermissionDialog(permissions);
            return;
        }

        if (permissions.size() == 1 && Permission.ACCESS_BACKGROUND_LOCATION.equals(permissions.get(0))) {
            ToastUtils.show(R.string.没有授予后台定位权限);
            return;
        }

        ToastUtils.show(R.string.授权失败请正确授予权限);
    }

    /**
     * 显示授权对话框
     */
    protected void showPermissionDialog(List<String> permissions) {
        Activity activity = ActivityUtils.getTopActivity();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        new MessageDialog.Builder(activity)
                .setTitle(R.string.授权提醒)
                .setMessage(getPermissionHint(activity, permissions))
                .setConfirm(R.string.前往授权)
                .setCancel(null)
                .setCancelable(false)
                .setListener(dialog -> XXPermissions.startPermissionActivity(activity, permissions))
                .show();
    }

    /**
     * 根据权限获取提示
     */
    protected String getPermissionHint(Context context, List<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return context.getString(R.string.获取权限失败请手动授予权限);
        }

        List<String> hints = new ArrayList<>();
        for (String permission : permissions) {
            switch (permission) {
                case Permission.READ_EXTERNAL_STORAGE:
                case Permission.WRITE_EXTERNAL_STORAGE:
                case Permission.MANAGE_EXTERNAL_STORAGE: {
                    String hint = context.getString(R.string.存储权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.CAMERA: {
                    String hint = context.getString(R.string.相机权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.RECORD_AUDIO: {
                    String hint = context.getString(R.string.麦克风权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.ACCESS_FINE_LOCATION:
                case Permission.ACCESS_COARSE_LOCATION:
                case Permission.ACCESS_BACKGROUND_LOCATION: {
                    String hint;
                    if (!permissions.contains(Permission.ACCESS_FINE_LOCATION) &&
                            !permissions.contains(Permission.ACCESS_COARSE_LOCATION)) {
                        hint = context.getString(R.string.后台定位权限);
                    } else {
                        hint = context.getString(R.string.定位权限);
                    }
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.READ_PHONE_STATE:
                case Permission.CALL_PHONE:
                case Permission.ADD_VOICEMAIL:
                case Permission.USE_SIP:
                case Permission.READ_PHONE_NUMBERS:
                case Permission.ANSWER_PHONE_CALLS: {
                    String hint = context.getString(R.string.电话权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.GET_ACCOUNTS:
                case Permission.READ_CONTACTS:
                case Permission.WRITE_CONTACTS: {
                    String hint = context.getString(R.string.通讯录权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.READ_CALENDAR:
                case Permission.WRITE_CALENDAR: {
                    String hint = context.getString(R.string.日历权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.READ_CALL_LOG:
                case Permission.WRITE_CALL_LOG:
                case Permission.PROCESS_OUTGOING_CALLS: {
                    String hint = context.getString(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ?
                            R.string.通话记录权限 : R.string.电话权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.BODY_SENSORS: {
                    String hint = context.getString(R.string.身体传感器权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.ACTIVITY_RECOGNITION: {
                    String hint = context.getString(R.string.健身运动权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.SEND_SMS:
                case Permission.RECEIVE_SMS:
                case Permission.READ_SMS:
                case Permission.RECEIVE_WAP_PUSH:
                case Permission.RECEIVE_MMS: {
                    String hint = context.getString(R.string.短信权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.REQUEST_INSTALL_PACKAGES: {
                    String hint = context.getString(R.string.安装应用权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.NOTIFICATION_SERVICE: {
                    String hint = context.getString(R.string.通知栏权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.SYSTEM_ALERT_WINDOW: {
                    String hint = context.getString(R.string.悬浮窗权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                case Permission.WRITE_SETTINGS: {
                    String hint = context.getString(R.string.系统设置权限);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                default:
                    break;
            }
        }

        if (!hints.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String text : hints) {
                if (builder.length() == 0) {
                    builder.append(text);
                } else {
                    builder.append("、")
                            .append(text);
                }
            }
            builder.append(" ");
            return context.getString(R.string.获取权限失败请手动授予, builder.toString());
        }

        return context.getString(R.string.获取权限失败请手动授予权限);
    }
}
