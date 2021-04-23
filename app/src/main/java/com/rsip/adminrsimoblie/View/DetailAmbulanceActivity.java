package com.rsip.adminrsimoblie.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.DataMobilAmbulanceActivity;

import naseem.ali.flexibletoast.EasyToast;

public class DetailAmbulanceActivity extends AppCompatActivity {

    Intent intent;
    TextView textPlatNomer,textJenisKendaraan,textMerekKendaraan,textTipeKendaraan,textPajakKendaraan;
    Button btnHapus,btnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ambulance);
        findViews();
    }

    private void findViews(){
        intent=getIntent();
        textPlatNomer=findViewById(R.id.textPlatNomer);
        textJenisKendaraan=findViewById(R.id.text_jenis_kendaraan);
        textMerekKendaraan=findViewById(R.id.text_merek_kendaraan);
        textTipeKendaraan=findViewById(R.id.text_tipe_kendaraan);
        textPajakKendaraan=findViewById(R.id.text_pajak_kendaraan);
        btnHapus=findViewById(R.id.btnHapus);
        btnEdit=findViewById(R.id.btnEdit);
        btnHapus.setOnClickListener(v -> {
            dialogConfirmation();
        });
        btnEdit.setOnClickListener(v -> editData());
        fillFromIntent();
    }

    private void fillFromIntent(){
        textPlatNomer.setText(intent.getStringExtra("nopol"));
        textJenisKendaraan.setText(intent.getStringExtra("jenis"));
        textMerekKendaraan.setText(intent.getStringExtra("tipe"));
        textTipeKendaraan.setText(intent.getStringExtra("merek"));
        textPajakKendaraan.setText(intent.getStringExtra("pajak"));
    }

    private void dialogConfirmation(){
        AlertDialog.Builder dialogs=new AlertDialog.Builder(this);
        dialogs.setMessage("Apakah Anda Yakin Akan Menghapus Data ini?");
        dialogs.setCancelable(false);
        dialogs.setNegativeButton("Tidak", (dialogs1, which) -> dialogs1.dismiss());
        dialogs.setPositiveButton("YA", (dialogs12, which) -> {
            hapusData();
        });
        dialogs.show();
    }

    private void hapusData(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Driver").child("DataMobil");

        databaseReference.child(intent.getStringExtra("key")).removeValue((error, ref) -> {EasyToast.makeText(DetailAmbulanceActivity.this,"Data Berhasil dihapus",EasyToast.LENGTH_SHORT,EasyToast.SUCCESS,false).show();
        startActivity(new Intent(DetailAmbulanceActivity.this, DataMobilAmbulanceActivity.class));
        finish();
        });
    }

    private void editData(){
        Intent intent1=new Intent(DetailAmbulanceActivity.this, TambahMobilActivity.class);
        intent1.putExtra("key",intent.getStringExtra("key"));
        intent1.putExtra("nopol",intent.getStringExtra("nopol"));
        intent1.putExtra("jenis",intent.getStringExtra("jenis"));
        intent1.putExtra("tipe",intent.getStringExtra("tipe"));
        intent1.putExtra("merek",intent.getStringExtra("merek"));
        intent1.putExtra("pajak",intent.getStringExtra("pajak"));
        intent1.putExtra("plat",intent.getStringExtra("plat"));
        intent1.putExtra("edit","yes");
        startActivity(intent1);
        finish();
    }

}