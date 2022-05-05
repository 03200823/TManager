package com.students.app.ui.adapter;

import android.provider.CallLog;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.students.app.R;
import com.students.app.bean.CallRecordBean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallRecordAdapter extends BaseQuickAdapter<CallRecordBean, BaseViewHolder> {
  public CallRecordAdapter() {
    super(R.layout.item_reccord_call);
  }

  @Override
  protected void convert(@NonNull BaseViewHolder baseViewHolder, CallRecordBean callRecordBean) {
    int duration = callRecordBean.getDuring();
    int temp = duration % 3600;
    int hour = duration / (60 * 60);
    int min = temp / 60;
    int sec = temp  % 60;

    StringBuilder durationBuilder = new StringBuilder();
    if(hour > 0){
      durationBuilder.append(" ").append(hour).append("小时");
    }
    if(min > 0){
      durationBuilder.append(" ").append(min).append("分");
    }

    if(sec > 0){
      durationBuilder.append(" ").append(sec).append("秒");
    }

    baseViewHolder.setText(R.id.during,durationBuilder.toString());

    if(callRecordBean.getType() == 1){
      baseViewHolder.setText(R.id.callType,"呼入");
    }else if(callRecordBean.getType() == 2){
      if(durationBuilder.toString().isEmpty()){
        baseViewHolder.setText(R.id.callType,"未接通");
      }else {
        baseViewHolder.setText(R.id.callType,"呼出");
      }
    }else if (callRecordBean.getType() ==3){
      baseViewHolder.setText(R.id.callType,"未接");
      baseViewHolder.setText(R.id.during,"响铃" + durationBuilder);

    }else if(callRecordBean.getType() == 5){
      baseViewHolder.setText(R.id.callType,"拒接");
    }else {
      baseViewHolder.setText(R.id.callType,"未知 " + callRecordBean.getType() + durationBuilder.toString());
    }

    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(callRecordBean.getTime());
    baseViewHolder.setText(R.id.time,date);

  }
}