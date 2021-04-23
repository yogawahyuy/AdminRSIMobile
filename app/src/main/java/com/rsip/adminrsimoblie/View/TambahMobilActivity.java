package com.rsip.adminrsimoblie.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rsip.adminrsimoblie.MainActivity;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.DataMobilAmbulanceActivity;
import com.rsip.adminrsimoblie.RecyclerView.DataMobilAmbulanceModel;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.Calendar;
import java.util.HashMap;

import naseem.ali.flexibletoast.EasyToast;

public class TambahMobilActivity extends AppCompatActivity {

    EditText plat,typeKendaraan,pajakKendaraan,pajakPlatKendaraan,merekKendaraan;
    SearchableSpinner spinner;
    Button btnSimpan;

    DatabaseReference dbRefrence;

    Intent intent;
    TextView textTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_mobil);
        findViews();
    }

    private void findViews(){
        intent=getIntent();
        plat=findViewById(R.id.edtTxt_plat);
        typeKendaraan=findViewById(R.id.edtTxt_type);
        pajakKendaraan=findViewById(R.id.edtTxt_pajak);
        pajakKendaraan.setInputType(InputType.TYPE_NULL);
        pajakPlatKendaraan=findViewById(R.id.edtTxt_tempoplat);
        pajakPlatKendaraan.setInputType(InputType.TYPE_NULL);
        merekKendaraan=findViewById(R.id.edtTxt_merk);
        btnSimpan=findViewById(R.id.btn_simpan);
        spinner=findViewById(R.id.spinner);
        textTitle=findViewById(R.id.text_title);
        pajakKendaraan.setOnClickListener(v -> showDatePickerPajak());
        pajakPlatKendaraan.setOnClickListener(v -> showDatePickerPlat());
        String cekNull=intent.getStringExtra("edit");
        btnSimpan.setOnClickListener(v -> {
            if(TextUtils.isEmpty(cekNull)){
                addIntoFirebase();

        }else {
                editData();
            }
        });

        if (TextUtils.isEmpty(cekNull)){

        }else {
            fillEditIntent();
        }
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

    private void fillEditIntent(){

        Log.d("tambahmobil", "fillEditIntent: "+intent.getStringExtra("edit"));
        Log.d("tambahmobil", "fillEditIntent: "+intent.getStringExtra("nopol"));
        textTitle.setText("Edit Data Mobil");
        plat.setText(intent.getStringExtra("nopol"));
        typeKendaraan.setText(intent.getStringExtra("tipe"));
        pajakKendaraan.setText(intent.getStringExtra("pajak"));
        merekKendaraan.setText(intent.getStringExtra("merek"));
        pajakPlatKendaraan.setText(intent.getStringExtra("plat"));
        if (intent.getStringExtra("jenis").equalsIgnoreCase("0")){
            spinner.setSelection(0);
        }else{
            spinner.setSelection(1);
        }

    }
    private void addIntoFirebase() {
        if (TextUtils.isEmpty(plat.getText().toString()) || TextUtils.isEmpty(typeKendaraan.getText().toString()) || TextUtils.isEmpty(pajakKendaraan.getText().toString()) || TextUtils.isEmpty(pajakPlatKendaraan.getText().toString())) {
            Toast.makeText(this, "Field diisi semua", Toast.LENGTH_SHORT).show();
        } else {
            String jenisKen=String.valueOf(spinner.getSelectedItemPosition());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("NoPlat", plat.getText().toString());
            hashMap.put("jenisKendaraan",jenisKen );
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

    private void editData(){
        String jenisKen=String.valueOf(spinner.getSelectedItemPosition());
        dbRefrence=FirebaseDatabase.getInstance().getReference("Driver").child("DataMobil");
        DataMobilAmbulanceModel dataMobilAmbulanceModel=new DataMobilAmbulanceModel();
        dataMobilAmbulanceModel.setNoPlat(plat.getText().toString());
        dataMobilAmbulanceModel.setJenisKendaraan(jenisKen);
        dataMobilAmbulanceModel.setTypeKendaraan(typeKendaraan.getText().toString());
        dataMobilAmbulanceModel.setPajakKendaraan(pajakKendaraan.getText().toString());
        dataMobilAmbulanceModel.setPajakPlatKendaraan(pajakPlatKendaraan.getText().toString());
        dataMobilAmbulanceModel.setMerekKendaraan(merekKendaraan.getText().toString());
        dbRefrence.child(intent.getStringExtra("nopol")).setValue(dataMobilAmbulanceModel).addOnSuccessListener(aVoid -> {
            startActivity(new Intent(TambahMobilActivity.this,DataMobilAmbulanceActivity.class));
            finish();
            EasyToast.makeText(TambahMobilActivity.this,"Data Berhasil diupdate",Toast.LENGTH_SHORT,EasyToast.SUCCESS,false).show();
        });
    }
}