package com.students.app.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.students.base.BaseDialog;
import com.students.app.R;
import com.students.app.aop.SingleClick;
import com.students.app.app.AppAdapter;
import com.students.app.manager.PickerLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataTimeDialog {

    public interface OnListener {

        /**
         * 选择完日期后回调
         *
         * @param year   年
         * @param month  月
         * @param day    日
         * @param hour   时钟
         * @param minute 分钟
         * @param second 秒钟
         */
        void onSelected(BaseDialog dialog, int year, int month, int day, int hour, int minute, int second);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {}
    }

    public static final class Builder extends CommonDialog.Builder<Builder>
            implements PickerLayoutManager.OnPickerListener,
                       Runnable {

        private final int mStartYear;

        private final RecyclerView mYearView;
        private final RecyclerView mMonthView;
        private final RecyclerView mDayView;

        private final PickerLayoutManager mYearManager;
        private final PickerLayoutManager mMonthManager;
        private final PickerLayoutManager mDayManager;

        private final PickerAdapter mYearAdapter;
        private final PickerAdapter mMonthAdapter;
        private final PickerAdapter mDayAdapter;


        //-------------时分秒-------------//
        private final RecyclerView        mHourView;
        private final RecyclerView        mMinuteView;
        private final RecyclerView        mSecondView;
        private final PickerLayoutManager mHourManager;
        private final PickerLayoutManager mMinuteManager;
        private final PickerLayoutManager mSecondManager;
        private final PickerAdapter       mHourAdapter;
        private final PickerAdapter       mMinuteAdapter;
        private final PickerAdapter       mSecondAdapter;
        private       OnListener          mListener;
        //-------------时分秒-------------//


        public Builder(Context context) {
            this(context, Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR) - 100);
        }

        public Builder(Context context, int startYear) {
            this(context, startYear, Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR));
        }

        public Builder(Context context, int startYear, int endYear) {
            super(context);
            mStartYear = startYear;

            setCustomView(R.layout.dialog_date_time);
            setTitle(R.string.请选择时间);

            mYearView  = findViewById(R.id.rv_date_year);
            mMonthView = findViewById(R.id.rv_date_month);
            mDayView   = findViewById(R.id.rv_date_day);

            mYearAdapter  = new PickerAdapter(context);
            mMonthAdapter = new PickerAdapter(context);
            mDayAdapter   = new PickerAdapter(context);

            // 生产年份
            ArrayList<String> yearData = new ArrayList<>(10);
            for (int i = mStartYear; i <= endYear; i++) {
                yearData.add(i + " " + getString(R.string.年));
            }

            // 生产月份
            ArrayList<String> monthData = new ArrayList<>(12);
            for (int i = 1; i <= 12; i++) {
                monthData.add(i + " " + getString(R.string.月));
            }

            Calendar calendar = Calendar.getInstance(Locale.CHINA);

            int day = calendar.getActualMaximum(Calendar.DATE);
            // 生产天数
            ArrayList<String> dayData = new ArrayList<>(day);
            for (int i = 1; i <= day; i++) {
                dayData.add(i + " " + getString(R.string.日));
            }

            mYearAdapter.setData(yearData);
            mMonthAdapter.setData(monthData);
            mDayAdapter.setData(dayData);

            mYearManager  = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();
            mMonthManager = new PickerLayoutManager.Builder(context).setMaxItem(5)
                                                                    .build();
            mDayManager   = new PickerLayoutManager.Builder(context).setMaxItem(5)
                                                                    .build();

            mYearView.setLayoutManager(mYearManager);
            mMonthView.setLayoutManager(mMonthManager);
            mDayView.setLayoutManager(mDayManager);

            mYearView.setAdapter(mYearAdapter);
            mMonthView.setAdapter(mMonthAdapter);
            mDayView.setAdapter(mDayAdapter);

            setYear(calendar.get(Calendar.YEAR));
            setMonth(calendar.get(Calendar.MONTH) + 1);
            setDay(calendar.get(Calendar.DAY_OF_MONTH));

            mYearManager.setOnPickerListener(this);
            mMonthManager.setOnPickerListener(this);


            //-------------时分秒-------------//

            mHourView   = findViewById(R.id.rv_time_hour);
            mMinuteView = findViewById(R.id.rv_time_minute);
            mSecondView = findViewById(R.id.rv_time_second);

            mHourAdapter   = new PickerAdapter(context);
            mMinuteAdapter = new PickerAdapter(context);
            mSecondAdapter = new PickerAdapter(context);

            // 生产小时
            ArrayList<String> hourData = new ArrayList<>(24);
            for (int i = 0; i <= 23; i++) {
                hourData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.时));
            }

            // 生产分钟
            ArrayList<String> minuteData = new ArrayList<>(60);
            for (int i = 0; i <= 59; i++) {
                minuteData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.分));
            }

            // 生产秒钟
            ArrayList<String> secondData = new ArrayList<>(60);
            for (int i = 0; i <= 59; i++) {
                secondData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.秒));
            }

            mHourAdapter.setData(hourData);
            mMinuteAdapter.setData(minuteData);
            mSecondAdapter.setData(secondData);

            mHourManager   = new PickerLayoutManager.Builder(context).setMaxItem(5)
                                                                     .build();
            mMinuteManager = new PickerLayoutManager.Builder(context).setMaxItem(5)
                                                                     .build();
            mSecondManager = new PickerLayoutManager.Builder(context).setMaxItem(5)
                                                                     .build();

            mHourView.setLayoutManager(mHourManager);
            mMinuteView.setLayoutManager(mMinuteManager);
            mSecondView.setLayoutManager(mSecondManager);

            mHourView.setAdapter(mHourAdapter);
            mMinuteView.setAdapter(mMinuteAdapter);
            mSecondView.setAdapter(mSecondAdapter);


            setHour(calendar.get(Calendar.HOUR_OF_DAY));
            setMinute(calendar.get(Calendar.MINUTE));
            setSecond(calendar.get(Calendar.SECOND));

            postDelayed(this, 1000);

            //-------------时分秒-------------//
        }


        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        /**
         * 不选择天数
         */
        public Builder setIgnoreDay() {
            mDayView.setVisibility(View.GONE);
            return this;
        }

        public Builder setDate(long date) {
            if (date > 0) {
                setDate(new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date(date)));
            }
            return this;
        }

        public Builder setDate(String date) {
            if (date.matches("\\d{8}")) {
                // 20190519
                setYear(date.substring(0, 4));
                setMonth(date.substring(4, 6));
                setDay(date.substring(6, 8));
            } else if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                // 2019-05-19
                setYear(date.substring(0, 4));
                setMonth(date.substring(5, 7));
                setDay(date.substring(8, 10));
            }
            return this;
        }

        public Builder setYear(String year) {
            return setYear(Integer.parseInt(year));
        }

        public Builder setYear(int year) {
            int index = year - mStartYear;
            if (index < 0) {
                index = 0;
            } else if (index > mYearAdapter.getItemCount() - 1) {
                index = mYearAdapter.getItemCount() - 1;
            }
            mYearView.scrollToPosition(index);
            onPicked(mYearView, index);
            return this;
        }

        public Builder setMonth(String month) {
            return setMonth(Integer.parseInt(month));
        }

        public Builder setMonth(int month) {
            int index = month - 1;
            if (index < 0) {
                index = 0;
            } else if (index > mMonthAdapter.getItemCount() - 1) {
                index = mMonthAdapter.getItemCount() - 1;
            }
            mMonthView.scrollToPosition(index);
            onPicked(mMonthView, index);
            return this;
        }

        public Builder setDay(String day) {
            return setDay(Integer.parseInt(day));
        }

        public Builder setDay(int day) {
            int index = day - 1;
            if (index < 0) {
                index = 0;
            } else if (index > mDayAdapter.getItemCount() - 1) {
                index = mDayAdapter.getItemCount() - 1;
            }
            mDayView.scrollToPosition(index);
            onPicked(mDayView, index);
            return this;
        }

        /**
         * {@link PickerLayoutManager.OnPickerListener}
         *
         * @param recyclerView RecyclerView 对象
         * @param position     当前滚动的位置
         */
        @Override
        public void onPicked(RecyclerView recyclerView, int position) {
            // 获取这个月最多有多少天
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.set(mStartYear + mYearManager.getPickedPosition(), mMonthManager.getPickedPosition(), 1);

            int day = calendar.getActualMaximum(Calendar.DATE);
            if (mDayAdapter.getItemCount() != day) {
                ArrayList<String> dayData = new ArrayList<>(day);
                for (int i = 1; i <= day; i++) {
                    dayData.add(i + " " + getString(R.string.日));
                }
                mDayAdapter.setData(dayData);
            }
        }


        //---------------------------------------------------------------------//
        @Override
        public void run() {
            if (mHourView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                    mMinuteView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                    mSecondView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, mHourManager.getPickedPosition());
                calendar.set(Calendar.MINUTE, mMinuteManager.getPickedPosition());
                calendar.set(Calendar.SECOND, mSecondManager.getPickedPosition());
                if (System.currentTimeMillis() - calendar.getTimeInMillis() < 3000) {
                    calendar = Calendar.getInstance();
                    setHour(calendar.get(Calendar.HOUR_OF_DAY));
                    setMinute(calendar.get(Calendar.MINUTE));
                    setSecond(calendar.get(Calendar.SECOND));
                    postDelayed(this, 1000);
                }
            }
        }

        /**
         * 不选择秒数
         */
        public Builder setIgnoreSecond() {
            mSecondView.setVisibility(View.GONE);
            return this;
        }

        public Builder setTime(String time) {
            // 102030
            if (time.matches("\\d{6}")) {
                setHour(time.substring(0, 2));
                setMinute(time.substring(2, 4));
                setSecond(time.substring(4, 6));
                // 10:20:30
            } else if (time.matches("\\d{2}:\\d{2}:\\d{2}")) {
                setHour(time.substring(0, 2));
                setMinute(time.substring(3, 5));
                setSecond(time.substring(6, 8));
            }
            return this;
        }

        public Builder setHour(String hour) {
            return setHour(Integer.parseInt(hour));
        }

        public Builder setHour(int hour) {
            int index = hour;
            if (index < 0 || hour == 24) {
                index = 0;
            } else if (index > mHourAdapter.getItemCount() - 1) {
                index = mHourAdapter.getItemCount() - 1;
            }
            mHourView.scrollToPosition(index);
            return this;
        }

        public Builder setMinute(String minute) {
            return setMinute(Integer.parseInt(minute));
        }

        public Builder setMinute(int minute) {
            int index = minute;
            if (index < 0) {
                index = 0;
            } else if (index > mMinuteAdapter.getItemCount() - 1) {
                index = mMinuteAdapter.getItemCount() - 1;
            }
            mMinuteView.scrollToPosition(index);
            return this;
        }

        public Builder setSecond(String second) {
            return setSecond(Integer.parseInt(second));
        }

        public Builder setSecond(int second) {
            int index = second;
            if (index < 0) {
                index = 0;
            } else if (index > mSecondAdapter.getItemCount() - 1) {
                index = mSecondAdapter.getItemCount() - 1;
            }
            mSecondView.scrollToPosition(index);
            return this;
        }

        //-------------时分秒-------------//


        @SingleClick
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.tv_ui_confirm) {
                autoDismiss();
                if (mListener != null) {
                    mListener.onSelected(getDialog(), mStartYear + mYearManager.getPickedPosition(), mMonthManager.getPickedPosition() + 1, mDayManager.getPickedPosition() + 1,
                            mHourManager.getPickedPosition(), mMinuteManager.getPickedPosition(), mSecondManager.getPickedPosition());
                }
            } else if (viewId == R.id.tv_ui_cancel) {
                autoDismiss();
                if (mListener != null) {
                    mListener.onCancel(getDialog());
                }
            }
        }

        private static final class PickerAdapter extends AppAdapter<String> {

            private PickerAdapter(Context context) {
                super(context);
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder();
            }

            private final class ViewHolder extends AppAdapter<?>.ViewHolder {

                private final TextView mPickerView;

                ViewHolder() {
                    super(R.layout.dialog_item_picker);
                    mPickerView = findViewById(R.id.tv_picker_name);
                }

                @Override
                public void onBindView(int position) {
                    mPickerView.setText(getItem(position));
                }
            }
        }
    }


}


