package com.example.nazenani.notification;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 1;

    private SharedPreferences mSharedPreferences;
    private EditText mTitleEdit;
    private EditText mDescriptionEdit;
    private TextView mDateText;
    private TextView mTimeText;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // シェアードプリファレンスインスタンスを生成
        mSharedPreferences = getSharedPreferences("SaveDate", Context.MODE_PRIVATE);

        // 保存した値を取得
        String title = mSharedPreferences.getString("title", null);
        String description = mSharedPreferences.getString("description", null);
        mYear = mSharedPreferences.getInt("year", 0);
        mMonth = mSharedPreferences.getInt("month", 0);
        mDay = mSharedPreferences.getInt("day", 0);
        mHour = mSharedPreferences.getInt("hour", 0);
        mMinute = mSharedPreferences.getInt("minute", 0);

        // 日付ボタンインスタンスを生成
        Button date = (Button) findViewById(R.id.date);
        // 時間ボタンインスタンスを生成
        Button time = (Button) findViewById(R.id.time);
        // 開始ボタンインスタンスを生成
        Button start = (Button) findViewById(R.id.start);
        // 停止ボタンインスタンスを生成
        Button stop = (Button) findViewById(R.id.stop);

        // タイトルエディットテキストインスタンスを生成
        mTitleEdit = (EditText) findViewById(R.id.title);
        // エディットテキストにテキストをセット
        mTitleEdit.setText(title);

        // 説明エディットテキストインスタンスを生成
        mDescriptionEdit = (EditText) findViewById(R.id.description);
        // エディットテキストにテキストをセット
        mDescriptionEdit.setText(description);

        // 設定日付テキストビューインスタンスを生成
        mDateText = (TextView) findViewById(R.id.dateText);
        // テキストビューにテキストをセット
        mDateText.setText(mYear + "/" + (mMonth + 1) + "/" + mDay);

        // 設定時間テキストビューインスタンスを生成
        mTimeText = (TextView) findViewById(R.id.timeText);
        // テキストビューにテキストをセット
        mTimeText.setText(mHour + ":" + mMinute);

        // 日付ボタンクリック処理
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // デートピッカーフラグメントインスタンスを生成
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                // デートピッカーフラグメントを表示
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        // 時間ボタンクリック処理
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // タイムピッカーフラグメントインスタンスを生成
                TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
                // タイムピッカーフラグメントを表示
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });

        // 開始ボタンクリック処理
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
                intent.putExtra("intentId", 1);
                intent.putExtra("title", mTitleEdit.getText().toString());
                intent.putExtra("description", mDescriptionEdit.getText().toString());
                // PendingIntentが同じ物の場合は上書きされてしまうのでRequestCodeで区別
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intent, 0);

                // カレンダーインスタンスを生成
                Calendar calendar = Calendar.getInstance();

                int year = (mYear == 0) ? calendar.get(Calendar.YEAR) : mYear;
                int month = (mMonth == 0) ? calendar.get(Calendar.MONTH) : mMonth;
                int dayOfMonth = (mDay == 0) ? calendar.get(Calendar.DAY_OF_MONTH) : mDay;
                int hourOfDay = (mHour == 0) ? calendar.get(Calendar.HOUR_OF_DAY) : mHour;
                int minute = (mMinute == 0) ? calendar.get(Calendar.MINUTE) : mMinute;

                // 通知実行日時を設定(月は1少ない)
                calendar.set(year, month, dayOfMonth, hourOfDay, minute, 0);

                // アラームインスタンスを生成
                AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
                // 過去日時が設定されている場合は即実行される
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                // データの書き込み
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                // 値を保存
                editor.putString("title", mTitleEdit.getText().toString());
                editor.putString("description", mDescriptionEdit.getText().toString());
                editor.putInt("year", year);
                editor.putInt("month", month);
                editor.putInt("day", dayOfMonth);
                editor.putInt("hour", hourOfDay);
                editor.putInt("minute", minute);
                editor.apply();

                // トーストで設定されたことをを表示
                Toast.makeText(getApplicationContext(), "START", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        // 停止ボタンクリック処理
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intent, 0);

                // アラームインスタンスを生成
                AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
                // アラームからキャンセル
                alarmManager.cancel(pendingIntent);

                // トーストで設定されたことをを表示
                Toast.makeText(getApplicationContext(), "STOP", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * 日付が選択されたときの処理
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
        // テキストビューに値をセット
        mDateText.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
        Log.d(TAG, "onDateSet: " + year + ", " + month + ", " + dayOfMonth);
    }


    /**
     * 時刻が選択されたときの処理
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        // テキストビューに値をセット
        mTimeText.setText(hourOfDay + ":" + minute);
        Log.d(TAG, "onTimeSet: " + hourOfDay + ", " + minute);
    }
}
