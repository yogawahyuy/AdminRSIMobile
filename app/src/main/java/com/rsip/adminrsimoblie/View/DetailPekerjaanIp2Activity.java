package com.rsip.adminrsimoblie.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.DataPekerjaanIp2Activity;

import java.util.HashMap;

public class DetailPekerjaanIp2Activity extends AppCompatActivity {

    TextView namaPekerjaan,lokasiPengerjaan,namaPekerja,tanggalPengerjaan,jamPengerjaan;
    Button btnSelesai;
    Intent intent;
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

        fillFromIntent();

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePekerjaan();
            }
        });
    }

    private void fillFromIntent(){
        namaPekerjaan.setText(intent.getStringExtra("namaPekerjaan"));
        namaPekerja.setText(intent.getStringExtra("namaPekerja"));
        lokasiPengerjaan.setText(intent.getStringExtra("lokasiPekerjaan"));
        tanggalPengerjaan.setText(intent.getStringExtra("tanggalPengerjaan"));
        jamPengerjaan.setText(intent.getStringExtra("jamPengerjaan"));


    }

    private void changePekerjaan(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("IPPSRS").child("DataPekerjaan");

//        HashMap<String,Object> hashMap=new HashMap<>();
//
//        hashMap.put("namaPekerjaan",intent.getStringExtra("namaPekerjaan"));
//        hashMap.put("namaPekerja",intent.getStringExtra("namaPekerja"));
//        hashMap.put("lokasiPekerjaan",intent.getStringExtra("lokasiPekerjaan"));
//        hashMap.put("tanggalPengerjaan",intent.getStringExtra("tanggalPengerjaan"));
//        hashMap.put("jamPengerjaan",intent.getStringExtra("jamPengerjaan"));
//        hashMap.put("tanggalPengerjaanOtomatis",intent.getStringExtra("tanggalPengerjaanOtomatis"));
//        hashMap.put("jamPengerjaanOtomatis",intent.getStringExtra("jamPengerjaanOtomatis"));
//        hashMap.put("status","yes");
//
//        reference.child(intent.getStringExtra("key")).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                startActivity(new Intent(DetailPekerjaanIp2Activity.this, DataPekerjaanIp2Activity.class));
//                finish();
//            }
//        });
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
        hashMap.put("status","Sudah Selesai");

        String key=reference.push().getKey();

        reference.child(key).setValue(hashMap);

    }
}