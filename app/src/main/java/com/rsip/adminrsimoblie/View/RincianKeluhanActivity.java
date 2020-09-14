package com.rsip.adminrsimoblie.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.ListKeluhanActivity;

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
                Intent intent1=new Intent(RincianKeluhanActivity.this,BalasKeluhanActivity.class);
                intent1.putExtra("key",intent.getStringExtra("key"));
                intent1.putExtra("nama",intent.getStringExtra("nama"));
                intent1.putExtra("tanggal",intent.getStringExtra("tanggal"));
                intent1.putExtra("kategori",intent.getStringExtra("kategori"));
                intent1.putExtra("ruangan",intent.getStringExtra("ruangan"));
                intent1.putExtra("keluhan",intent.getStringExtra("keluhan"));
                intent1.putExtra("balasan",intent.getStringExtra("balasan"));
                intent1.putExtra("sender",intent.getStringExtra("sender"));
                intent1.putExtra("status",intent.getStringExtra("status"));
                intent1.putExtra("idpembalas",intent.getStringExtra("idpembalas"));
                startActivity(intent1);
                finish();
            }
        });
    }
}