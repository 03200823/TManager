package com.students.app.action;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.students.app.http.glide.GlideApp;
import com.students.app.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

public interface ViewHolderAction {


    SparseArray<View> getSparseArray();

    //获取ContentView
    View getContentView();
    //获取Fragmetn的View

    default <T extends View> T getView(@IdRes int viewId) {
        T viewOrNull = getViewOrNull(viewId);
        return viewOrNull;
    }

    default <T extends View> T getViewOrNull(int viewId) {
        T view = (T) getSparseArray().get(viewId);
        if (view == null) {
            T it = getContentView().findViewById(viewId);
            getSparseArray().put(viewId, it);
            return it;
        }

        return view;
    }

    default <T extends View> T findView(@IdRes int viewId) {
        return getContentView().findViewById(viewId);
    }


    default Button getButton(@IdRes int viewId) {
        return getView(viewId);
    }

    default SwitchCompat getSwc(@IdRes int viewId) {
        return getView(viewId);
    }

    default EditText getEdit(@IdRes int viewId) {
        return getView(viewId);
    }

    default String getEditString(@IdRes int viewId) {
        EditText view = getView(viewId);
        return view.getText().toString();
    }


    default void setButtonText(@IdRes int viewId, String s) {
        Button view = getView(viewId);
        view.setText(s);
    }

    default void setButtonText(@IdRes int viewId, CharSequence charSequence) {
        Button view = getView(viewId);
        view.setText(charSequence);
    }

    default void setButtonText(@IdRes int viewId, @StringRes int resid) {
        Button view = getView(viewId);
        view.setText(resid);
    }

    default SmartRefreshLayout getSrl(@IdRes int viewId) {
        SmartRefreshLayout layout = getView(viewId);
        return layout;
    }

    default RecyclerView getRv(@IdRes int viewId) {
        RecyclerView layout = getView(viewId);
        return layout;
    }


    default StatusLayout getSta(@IdRes int viewId) {
        StatusLayout layout = getView(viewId);
        return layout;
    }

    default void setText(@IdRes int viewId, CharSequence charSequence) {
        TextView view = getView(viewId);
        view.setText(charSequence);

    }

    default void setText(@IdRes int viewId, @StringRes int resid) {
        TextView view = getView(viewId);
        view.setText(resid);
    }

    default void setText(@IdRes int viewId, String s) {
        TextView view = getView(viewId);
        view.setText(s);
    }

    default String getTextString(@IdRes int viewId) {
        TextView view = getView(viewId);
        return view.getText().toString();
    }

    default void setTextColor(@IdRes int viewId, @ColorInt int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
    }

    default void setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(imageResId);
    }

    default void setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        imageView.setImageDrawable(drawable);
    }

    default void setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
    }

    default void setImageHttp(@IdRes int viewId, String httpUrl) {
        ImageView imageView = getView(viewId);
        GlideApp.with(getContentView().getContext()).load(httpUrl).into(imageView);
    }

    default void setImageHttp(@IdRes int viewId, String httpUrl, @DrawableRes int res) {
        ImageView imageView = getView(viewId);
        GlideApp.with(getContentView().getContext()).load(httpUrl).error(res).into(imageView);
    }


    default void setImageHttp(@IdRes int viewId, String httpUrl, @DrawableRes int placeholder, @DrawableRes int error) {
        ImageView imageView = getView(viewId);
        GlideApp.with(getContentView().getContext()).load(httpUrl).placeholder(placeholder).error(error).into(imageView);
    }

    default void setImageCornerHttp(@IdRes int viewId, String httpUrl, int dp) {
        ImageView imageView = getView(viewId);
        GlideApp.with(getContentView().getContext()).load(httpUrl)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, imageView.getContext().getResources().getDisplayMetrics()))))
                .into(imageView);
    }

    default void setImageOvalHttp(@IdRes int viewId, String httpUrl) {
        ImageView imageView = getView(viewId);
        GlideApp.with(getContentView().getContext()).load(httpUrl)
                .transform(new MultiTransformation<>(new CenterCrop(), new CircleCrop()))
                .into(imageView);
    }

    default void setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
    }

    default void setBackgroundResource(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
    }

    default void setInvisible(@IdRes int viewId) {
        View view = getView(viewId);
        view.setVisibility(View.INVISIBLE);
    }

    default void setVisibility(@IdRes int viewId) {
        View view = getView(viewId);
        view.setVisibility(View.VISIBLE);
    }

    default void setGoneOrVisible(@IdRes int viewId, Boolean isGone) {
        View view = getView(viewId);
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    default void setGone(@IdRes int viewId) {
        View view = getView(viewId);
        view.setVisibility(View.GONE);
    }

    default void setEnabled(@IdRes int viewId, Boolean isEnabled) {
        View view = getView(viewId);
        view.setEnabled(isEnabled);
    }

    default void setSelected(@IdRes int viewId, Boolean isSelected) {
        View view = getView(viewId);
        view.setSelected(isSelected);
    }


}
