package com.rsip.adminrsimoblie.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rsip.adminrsimoblie.MainActivity;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.DataMobilAmbulanceModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class DetailAmbulanceKeluarActivity extends AppCompatActivity {
    Intent intent;

    TextView nopol,driver,tujuan,jamKeluar,jarak,jenisKendaraan,tanggal;
    Button btnPulang;
    private ArrayList<DataMobilAmbulanceModel> modelList = new ArrayList<>();
    String waktuTanggal,waktuJam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ambulance_keluar);
        findView();
    }
    private void findView(){
        intent=getIntent();
        nopol=findViewById(R.id.textPlatNomer);
        driver=findViewById(R.id.textdriver);
        tujuan=findViewById(R.id.texttujuan);
        jamKeluar=findViewById(R.id.textjamKeluar);
        jarak=findViewById(R.id.textjarak);
        jenisKendaraan=findViewById(R.id.textjenis);
        tanggal=findViewById(R.id.texttanggal);
        btnPulang=findViewById(R.id.btn_pulang);
        fillFromIntent();
        getDateNowAutomatoc();
        getTimeNowAutomatic();
        //getDataMobilAll();
        btnPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectData();
            }
        });

    }

    private void fillFromIntent(){
        String jaraknya=intent.getStringExtra("jarak");
        nopol.setText(intent.getStringExtra("NoPlat"));
        driver.setText(intent.getStringExtra("driver"));
        tujuan.setText(intent.getStringExtra("tujuan"));
        jamKeluar.setText(intent.getStringExtra("jamInput"));
        jarak.setText(jaraknya+" KM");
        jenisKendaraan.setText(intent.getStringExtra("jenisKendaraan"));
        tanggal.setText(intent.getStringExtra("tanggalInput"));
    }
    private void getDateNowAutomatoc(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date today= Calendar.getInstance().getTime();
        String waktu=dateFormat.format(today);
        waktuTanggal=waktu;
    }
    private void getTimeNowAutomatic(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date timeNow=Calendar.getInstance().getTime();
        String waktu=dateFormat.format(timeNow);
        waktuJam=waktu;
    }
    private void collectData(){
        createDataMobilPulang();
        upadteDataMobilKeluar();
        getDataMobilAll();
    }
    private void createDataMobilPulang(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Driver").child("MobilPulang");

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("NoPlat",intent.getStringExtra("NoPlat"));
        hashMap.put("driver",intent.getStringExtra("driver"));
        hashMap.put("jamInput",intent.getStringExtra("jamInput"));
        hashMap.put("jamOtomatis",intent.getStringExtra("jamOtomatis"));
        hashMap.put("jarak",intent.getStringExtra("jarak"));
        hashMap.put("jenisKendaraan",intent.getStringExtra("jenisKendaraan"));
        hashMap.put("merkKendaraan",intent.getStringExtra("merkKendaraan"));
        hashMap.put("status","ready");
        hashMap.put("tanggalInput",intent.getStringExtra("tanggalInput"));
        hashMap.put("tanggalOtomatis",intent.getStringExtra("tanggalOtomatis"));
        hashMap.put("tujuan",intent.getStringExtra("tujuan"));
        hashMap.put("tanggalPulang",waktuTanggal);
        hashMap.put("jamPulang",waktuJam);

        String key=reference.child(intent.getStringExtra("NoPlat")).push().getKey();
        reference.child(intent.getStringExtra("NoPlat")).child(key).setValue(hashMap);
       // reference.child(intent.getStringExtra("key")).setValue(hashMap);
    }

    private void upadteDataMobilKeluar(){
        DatabaseReference referenceMobilKeluar=FirebaseDatabase.getInstance().getReference("Driver").child("MobilKeluar");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("NoPlat",intent.getStringExtra("NoPlat"));
        hashMap.put("driver",intent.getStringExtra("driver"));
        hashMap.put("jamInput",intent.getStringExtra("jamInput"));
        hashMap.put("jamOtomatis",intent.getStringExtra("jamOtomatis"));
        hashMap.put("jarak",intent.getStringExtra("jarak"));
        hashMap.put("jenisKendaraan",intent.getStringExtra("jenisKendaraan"));
        hashMap.put("merkKendaraan",intent.getStringExtra("merkKendaraan"));
        hashMap.put("status","ready");
        hashMap.put("tanggalInput",intent.getStringExtra("tanggalInput"));
        hashMap.put("tanggalOtomatis",intent.getStringExtra("tanggalOtomatis"));
        hashMap.put("tujuan",intent.getStringExtra("tujuan"));
        referenceMobilKeluar.child(intent.getStringExtra("key")).setValue(hashMap);
    }

    private void getDataMobilAll(){
       // DatabaseReference mRefrence= FirebaseDatabase.getInstance().getReference("Driver").child("DataMobil").child(intent.getStringExtra("NoPlat"));
        final DatabaseReference mRefrence= FirebaseDatabase.getInstance().getReference("Driver").child("DataMobil");

        // get data mobil All
        mRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DataMobilAmbulanceModel dataMobilAmbulanceModel=dataSnapshot.getValue(DataMobilAmbulanceModel.class);
                    dataMobilAmbulanceModel.setKey(dataSnapshot.getKey());
                    if (dataMobilAmbulanceModel.getNoPlat().equalsIgnoreCase(intent.getStringExtra("NoPlat"))){
                        modelList.add(dataMobilAmbulanceModel);
                    }
                }
                DataMobilAmbulanceModel dataMobilAmbulanceModel= new DataMobilAmbulanceModel();
                //dataMobilAmbulanceModel.setKey(modelList.get(0).getKey());
                dataMobilAmbulanceModel.setNoPlat(modelList.get(0).getNoPlat());
                dataMobilAmbulanceModel.setJenisKendaraan(modelList.get(0).getJenisKendaraan());
                dataMobilAmbulanceModel.setPajakKendaraan(modelList.get(0).getPajakKendaraan());
                dataMobilAmbulanceModel.setMerekKendaraan(modelList.get(0).getMerekKendaraan());
                dataMobilAmbulanceModel.setPajakPlatKendaraan(modelList.get(0).getPajakPlatKendaraan());
                dataMobilAmbulanceModel.setStatus("ready");
                dataMobilAmbulanceModel.setTypeKendaraan(modelList.get(0).getTypeKendaraan());

                mRefrence.child(intent.getStringExtra("NoPlat")).setValue(dataMobilAmbulanceModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(DetailAmbulanceKeluarActivity.this, MainActivity.class));
                        finish();
                    }
                });
                Log.d("data", "onDataChange: "+modelList.get(0).getNoPlat());
                Log.d("data", "onDataChange: "+modelList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       // Log.d("data", "onDataChange: "+modelList.get(0).getNoPlat());
//        mRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    DataMobilAmbulanceModel dataMobilAmbulanceModel=dataSnapshot.getValue(DataMobilAmbulanceModel.class);
//                    dataMobilAmbulanceModel.setKey(dataSnapshot.getKey());
//                    if (dataMobilAmbulanceModel.getNoPlat().equalsIgnoreCase(intent.getStringExtra("NoPlat"))){
//                        modelList.add(dataMobilAmbulanceModel);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }
}