package com.example.nazenani.notification;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // 自分自身にデータを返す場合
        //DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
        // MainActivityにデータを返す場合
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (MainActivity)getActivity(), year, month, dayOfMonth);

        return datePickerDialog;
    }


    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // 日付が選択されたときの処理
        Log.d("DATE", "onDateSet: " + year + ", " + month + ", " + dayOfMonth);
    }

}