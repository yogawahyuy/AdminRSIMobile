package com.rsip.adminrsimoblie.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rsip.adminrsimoblie.R;

public class RincianKeluhanActivity extends AppCompatActivity {
    TextView nama,tanggal,kategori,unit,keluhan,balasan;
    Intent intent;
    Button btnBalas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_keluhan);
        findView();
        fillFromIntent();
        btnBalas();
    }

    private void findView(){
        nama=findViewById(R.id.rec_text_nama);
        tanggal=findViewById(R.id.rec_text_tgl);
        kategori=findViewById(R.id.rec_text_kategori);
        unit=findViewById(R.id.rec_text_unit);
        keluhan=findViewById(R.id.rec_text_keluhan);
        balasan=findViewById(R.id.rec_text_balasan);
        btnBalas=findViewById(R.id.btn_balas);
        intent=getIntent();

    }

    private void fillFromIntent(){
        nama.setText(intent.getStringExtra("nama"));
        tanggal.setText(intent.getStringExtra("tanggal"));
        kategori.setText(intent.getStringExtra("kategori"));
        unit.setText(intent.getStringExtra("ruangan"));
        keluhan.setText(intent.getStringExtra("keluhan"));
        balasan.setText(intent.getStringExtra("balasan"));

    }

    private void btnBalas(){
        btnBalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RincianKeluhanActivity.this,BalasKeluhanActivity.class));
            }
        });
    }
}