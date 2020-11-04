package com.rsip.adminrsimoblie.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.DataPekerjaanIp2Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class DetailPekerjaanIp2Activity extends AppCompatActivity {

    TextView namaPekerjaan,lokasiPengerjaan,namaPekerja,tanggalPengerjaan,jamPengerjaan,titleTanggalPengerjaan,titleJamPengerjaan;
    Button btnSelesai;
    Intent intent;
    String waktuTanggal,waktuJam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pekerjaan_ip2);
        findView();
    }

    private void findView(){
        intent=getIntent();
        namaPekerjaan=findViewById(R.id.textnamapekerjaan);
        lokasiPengerjaan=findViewById(R.id.textlokasi);
        namaPekerja=findViewById(R.id.textnamapekerja);
        tanggalPengerjaan=findViewById(R.id.texttanggal);
        jamPengerjaan=findViewById(R.id.textjampengerjaan);
        btnSelesai=findViewById(R.id.btn_selesai);
        titleJamPengerjaan=findViewById(R.id.titeljampengerjaan);
        titleTanggalPengerjaan=findViewById(R.id.titeltanggalPengerjaan);

        getDateNowAutomatoc();
        getTimeNowAutomatic();
        fillFromIntent();

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePekerjaan();
            }
        });
    }

    private void fillFromIntent(){
        Log.d("data", "fillFromIntent: "+intent.getStringExtra("ket"));
        Log.d("data", "fillFromIntent: "+intent.getStringExtra("tanggalSelesaiOtomatis"));
        Log.d("data", "fillFromIntent: "+intent.getStringExtra("jamSelesaiOtomatis"));
        if (intent.getStringExtra("ket").equalsIgnoreCase("selese")){
            namaPekerjaan.setText(intent.getStringExtra("namaPekerjaan"));
            namaPekerja.setText(intent.getStringExtra("namaPekerja"));
            lokasiPengerjaan.setText(intent.getStringExtra("lokasiPekerjaan"));
            titleTanggalPengerjaan.setText("Tanggal Selesai");
            titleJamPengerjaan.setText("Jam Selesai");
            tanggalPengerjaan.setText(intent.getStringExtra("tanggalSelesaiOtomatis"));
            jamPengerjaan.setText(intent.getStringExtra("jamSelesaiOtomatis"));
            btnSelesai.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("ket").equalsIgnoreCase("belum")){
            namaPekerjaan.setText(intent.getStringExtra("namaPekerjaan"));
            namaPekerja.setText(intent.getStringExtra("namaPekerja"));
            lokasiPengerjaan.setText(intent.getStringExtra("lokasiPekerjaan"));
            tanggalPengerjaan.setText(intent.getStringExtra("tanggalPengerjaan"));
            jamPengerjaan.setText(intent.getStringExtra("jamPengerjaan"));
        }

    }

    private void changePekerjaan(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("IPPSRS").child("DataPekerjaan");
        reference.child(intent.getStringExtra("key")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                createDataPekerjaanSelesai();
                startActivity(new Intent(DetailPekerjaanIp2Activity.this, DataPekerjaanIp2Activity.class));
                Toast.makeText(DetailPekerjaanIp2Activity.this, "Pekerjaan Sudah Selesai", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

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

    private void createDataPekerjaanSelesai(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("IPPSRS").child("PekerjaanSelesai");

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("namaPekerjaan",intent.getStringExtra("namaPekerjaan"));
        hashMap.put("namaPekerja",intent.getStringExtra("namaPekerja"));
        hashMap.put("lokasiPekerjaan",intent.getStringExtra("lokasiPekerjaan"));
        hashMap.put("tanggalPengerjaan",intent.getStringExtra("tanggalPengerjaan"));
        hashMap.put("jamPengerjaan",intent.getStringExtra("jamPengerjaan"));
        hashMap.put("tanggalPengerjaanOtomatis",intent.getStringExtra("tanggalPengerjaanOtomatis"));
        hashMap.put("jamPengerjaanOtomatis",intent.getStringExtra("jamPengerjaanOtomatis"));
        hashMap.put("tanggalSelesaiOtomatis",waktuTanggal);
        hashMap.put("jamSelesaiOtomatis",waktuJam);
        hashMap.put("status","Sudah Selesai");

        String key=reference.push().getKey();

        reference.child(key).setValue(hashMap);

    }


}