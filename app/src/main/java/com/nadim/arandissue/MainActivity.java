package com.nadim.arandissue;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Calendar c;
    TextView txtDate, txtTime, txtArDemo;
    String date = "", timeWithSecond;
    Long selectedDate;
    Long today;
    boolean chenage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtArDemo = findViewById(R.id.txtArDemo);

        txtArDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ArActivity.class));
            }
        });
        today = System.currentTimeMillis();
        c = Calendar.getInstance();
        setDate();
        setTime();
    }


    private void setDate() {
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        txtDate.setText("" + day + "-" + month + "-" + year);
        //    date = getFormatedDate("" + day, "" + month, "" + year);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearD, int month, int dayOfMonth) {
                        int monthDisplay = month + 1;
                        Log.e("Time", yearD + "-" + monthDisplay + "-" + dayOfMonth);
                        //    date = getFormatedDate(String.valueOf(dayOfMonth), String.valueOf(month), String.valueOf(yearD));
                        //   currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, yearD);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDate = c.getTimeInMillis();
                        checkDate(selectedDate);
                        Log.e("CheckDate", checkDate(selectedDate));
                        view.setMinDate(today);
                        txtDate.setText(dayOfMonth + "-" + monthDisplay + "-" + yearD);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(today);
                datePickerDialog.show();
            }
        });
        {

        }
    }

    public String checkDate(Long currentDate) {
        String todayDate = "";
        if (currentDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            todayDate = dateFormat.format(new Date(currentDate));
        }
        return todayDate;
    }

    public String checkTime(Long currentDate) {
        String todayDate = "";
        if (currentDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
            todayDate = dateFormat.format(new Date(currentDate));
        }
        return todayDate;
    }

    public String setAMPM(int hourOfDay) {
        String AM_PM="";
        if (hourOfDay == 0) {
            AM_PM = "AM";
           // hourOfDay = 12;
        } else if (hourOfDay < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }
        return AM_PM;
    }

    public int manageTime(int hour){
        if (hour==0)
            hour=12;
        else if (hour==13)
            hour=1;
        else if (hour==14)
            hour=2;
        else if (hour==15)
            hour=3;
        else if (hour==16)
            hour=4;
        else if (hour==17)
            hour=5;
        else if (hour==18)
            hour=6;
        else if (hour==19)
            hour=7;
        else if (hour==20)
            hour=8;
        else if (hour==21)
            hour=9;
        else if (hour==22)
            hour=10;
        else if (hour==23)
            hour=11;
        return hour;
    }

    private void setTime() {

        Calendar cal = Calendar.getInstance();
        final int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        final int minute = cal.get(Calendar.MINUTE);

        timeWithSecond = new SimpleDateFormat("hh:mm:ss a").format(cal.getTime());
        txtTime.setText(new SimpleDateFormat("hh:mm a").format(cal.getTime()));
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Long time = System.currentTimeMillis();
                checkDate(time);
                if (selectedDate != null) {

                    Log.e("Date Comparision", checkDate(time) + "  " + checkDate(selectedDate));
                    TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            Log.e("Time", hourOfDay + ":" + minute);
                            String AM_PM=setAMPM(hourOfDay);
                            hourOfDay=manageTime(hourOfDay);

                            Calendar calendar = Calendar.getInstance();
                            int setYear = calendar.get(Calendar.YEAR);
                            int setMonth = calendar.get(Calendar.MONTH);
                            int setDay = calendar.get(Calendar.DAY_OF_MONTH);
                            int setHour = 0;
                            int setMin = 0;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                setHour = view.getHour();
                                setMin = view.getMinute();
                            }
                            calendar.set(setYear, setMonth, setDay, setHour, setMin);
                            if (checkDate(selectedDate).equals(checkDate(time))) {
                                if (time > calendar.getTimeInMillis()) {
                                    Toast.makeText(MainActivity.this, "you cant select past time", Toast.LENGTH_SHORT).show();

                                    Log.e("Greater Time Comparision", checkTime(time) + " " + checkTime(calendar.getTimeInMillis()));
                                } else if (time < calendar.getTimeInMillis()) {

                                    txtTime.setText(String.format("%02d:%02d" + AM_PM, hourOfDay, minute));
                                    Log.e("Smaller Time Comparision", checkTime(time) + " " + checkTime(calendar.getTimeInMillis()));
                                }

                            } else if (checkDate(selectedDate) != checkDate(time)) {
                                txtTime.setText(String.format("%02d:%02d" + AM_PM, hourOfDay, minute));
                            }
                          /*{
                            Log.e("Else Time Comparision", checkTime(time) + " " + checkTime(calendar.getTimeInMillis()));
                        }*/

                        }
                    }, hourOfDay, minute, false);

                    timePickerDialog.show();

                } else
                    Toast.makeText(MainActivity.this, "Select the date first", Toast.LENGTH_SHORT).show();
            }
        });
        /*txt_time_picker.setOnClickListener {
            val timeListener = TimePickerDialog.OnTimeSetListener {
                _, hourOfDay, minute ->
                        val selectedCal = Calendar.getInstance()

                val seconds = selectedCal.get(Calendar.SECOND)
                selectedCal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedCal.set(Calendar.MINUTE, minute)
                selectedCal.set(Calendar.SECOND, seconds)
                timeWithSecond = SimpleDateFormat("hh:mm:ss a").format(selectedCal.time)
                txt_time_picker.text = SimpleDateFormat("hh:mm a").format(selectedCal.time)
            }
            val timePickerDlg = TimePickerDialog(
                    this,
                    timeListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
            )
            timePickerDlg.show()*/
    }
}
