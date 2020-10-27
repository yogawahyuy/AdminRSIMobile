package com.rsip.adminrsimoblie.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.DataPekerjaanIp2Activity;
import com.rsip.adminrsimoblie.Util.SharedPreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class TambahDataPekerjaanIp2Activity extends AppCompatActivity {

    EditText namaPekerjaan,lokasiPengerjaan,namaPekerja,tanggalPengerjaan,jamPengerjaan;
    Button btnSave;
    SharedPreferenceManager sharedPreferenceManager;
    String waktuJam, waktuTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_pekerjaan_ip2);
        findViews();
    }

    private void findViews(){
        sharedPreferenceManager=new SharedPreferenceManager(this);
        namaPekerjaan=findViewById(R.id.edtTxt_Pekerjaan);
        lokasiPengerjaan=findViewById(R.id.edtTxt_lokasi);
        namaPekerja=findViewById(R.id.edtTxt_namapekerja);
        tanggalPengerjaan=findViewById(R.id.edtTxt_tgl_pengerjaan);
        jamPengerjaan=findViewById(R.id.edtTxt_jam_pengerjaan);
        btnSave=findViewById(R.id.btn_save);
        getDateNowAutomatoc();
        getTimeNowAutomatic();

        namaPekerja.setText(sharedPreferenceManager.getSPNama());

        tanggalPengerjaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        jamPengerjaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIntoFirebase();
            }
        });
    }


    private void showDatePicker(){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               tanggalPengerjaan.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void showTimePicker(){
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                jamPengerjaan.setText(hourOfDay + ":" + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void getDateNowAutomatoc() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date today = Calendar.getInstance().getTime();
        String waktu = dateFormat.format(today);
        tanggalPengerjaan.setText(waktu);
        waktuTanggal=waktu;
    }

    private void getTimeNowAutomatic() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date timeNow = Calendar.getInstance().getTime();
        String waktu = dateFormat.format(timeNow);
        jamPengerjaan.setText(waktu);
        waktuJam=waktu;
    }

    private void addIntoFirebase(){
        if (TextUtils.isEmpty(namaPekerjaan.getText().toString())||TextUtils.isEmpty(lokasiPengerjaan.getText().toString())){
            Toast.makeText(this, "Semua Field Harus Diisi!!", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("IPPSRS");
            HashMap<String, Object> hashMap=new HashMap<>();
            hashMap.put("namaPekerjaan",namaPekerjaan.getText().toString());
            hashMap.put("lokasiPekerjaan",lokasiPengerjaan.getText().toString());
            hashMap.put("namaPekerja",namaPekerja.getText().toString());
            hashMap.put("tanggalPengerjaan",tanggalPengerjaan.getText().toString());
            hashMap.put("jamPengerjaan",jamPengerjaan.getText().toString());
            hashMap.put("tanggalPengerjaanOtomatis",waktuTanggal);
            hashMap.put("jamPengerjaanOtomatis",waktuJam);
            hashMap.put("status","no");

            String key=reference.child("DataPekerjaan").push().getKey();

            reference.child("DataPekerjaan").child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(TambahDataPekerjaanIp2Activity.this, DataPekerjaanIp2Activity.class));
                    finish();
                }
            });
        }
    }



}