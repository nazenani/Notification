package com.example.nazenani.notification;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // 自分自身にデータを返す場合
        //TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        // MainActivityにデータを返す場合
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (MainActivity)getActivity(), hour, minute, true);

        return timePickerDialog;
    }


    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // 時刻が選択されたときの処理
        Log.d("TIME", "onTimeSet: " + hourOfDay + ", " + minute);
    }

}