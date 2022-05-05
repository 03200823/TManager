package com.students.app.ui.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.students.app.R;
import com.students.app.bean.ContactBean;

public class ContactAdapter extends BaseQuickAdapter<ContactBean, BaseViewHolder> {
  public ContactAdapter() {
    super(R.layout.item_contact);
  }

  @Override
  protected void convert(@NonNull BaseViewHolder baseViewHolder, ContactBean contactBean) {
    if(TextUtils.isEmpty(contactBean.getName())){
      baseViewHolder.setText(R.id.title,"未知");
    }else {
      baseViewHolder.setText(R.id.title,contactBean.getName());
    }
    baseViewHolder.setText(R.id.number,contactBean.getNumber());
    baseViewHolder.setText(R.id.time,contactBean.getTime());
    AppCompatImageView imageView = baseViewHolder.getView(R.id.callType);
    //1.呼入2.呼出3.未接
    if(contactBean.getType() == 2){
      imageView.setVisibility(View.VISIBLE);
      Glide.with(getContext()).load(getContext().getDrawable(R.drawable.ic_incoming)).into(imageView);
    }else if (contactBean.getType() == 3){
      imageView.setVisibility(View.VISIBLE);
      Glide.with(getContext()).load(getContext().getDrawable(R.drawable.ic_un_answer)).into(imageView);
    }else {
      imageView.setVisibility(View.INVISIBLE);
    }
  }
}