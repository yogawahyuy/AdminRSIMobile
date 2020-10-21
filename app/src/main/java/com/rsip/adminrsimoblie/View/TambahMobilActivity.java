package com.rsip.adminrsimoblie.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rsip.adminrsimoblie.MainActivity;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.DataMobilAmbulanceActivity;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.Calendar;
import java.util.HashMap;

public class TambahMobilActivity extends AppCompatActivity {

    EditText plat,typeKendaraan,pajakKendaraan,pajakPlatKendaraan,merekKendaraan;
    SearchableSpinner spinner;
    Button btnSimpan;

    DatabaseReference dbRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_mobil);
        findViews();
    }

    private void findViews(){
        plat=findViewById(R.id.edtTxt_plat);
        typeKendaraan=findViewById(R.id.edtTxt_type);
        pajakKendaraan=findViewById(R.id.edtTxt_pajak);
        pajakKendaraan.setInputType(InputType.TYPE_NULL);
        pajakPlatKendaraan=findViewById(R.id.edtTxt_tempoplat);
        pajakPlatKendaraan.setInputType(InputType.TYPE_NULL);
        merekKendaraan=findViewById(R.id.edtTxt_merk);
        btnSimpan=findViewById(R.id.btn_simpan);
        spinner=findViewById(R.id.spinner);
        pajakKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerPajak();
            }
        });
        pajakPlatKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerPlat();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIntoFirebase();
            }
        });

    }

    private void showDatePickerPajak(){
        final Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                pajakKendaraan.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },year,month,day);
        datePickerDialog.show();

    }
    private void showDatePickerPlat(){
        final Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                pajakPlatKendaraan.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },year,month,day);
        datePickerDialog.show();

    }
    private void addIntoFirebase() {
        if (TextUtils.isEmpty(plat.getText().toString()) || TextUtils.isEmpty(typeKendaraan.getText().toString()) || TextUtils.isEmpty(pajakKendaraan.getText().toString()) || TextUtils.isEmpty(pajakPlatKendaraan.getText().toString())) {
            Toast.makeText(this, "Field diisi semua", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("NoPlat", plat.getText().toString());
            hashMap.put("jenisKendaraan", spinner.getSelectedItem().toString());
            hashMap.put("typeKendaraan", typeKendaraan.getText().toString());
            hashMap.put("pajakKendaraan", pajakKendaraan.getText().toString());
            hashMap.put("pajakPlatKendaraan", pajakPlatKendaraan.getText().toString());
            hashMap.put("merekKendaraan",merekKendaraan.getText().toString());
            hashMap.put("status","ready");

            //String key = reference.child("Driver").child("Mobil").push().getKey();
            String nopol=plat.getText().toString();
            reference.child("Driver").child("DataMobil").child(nopol).setValue(hashMap);
            startActivity(new Intent(TambahMobilActivity.this, DataMobilAmbulanceActivity.class));
            finish();
        }

    }
}