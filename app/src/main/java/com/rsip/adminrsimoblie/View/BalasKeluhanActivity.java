package com.rsip.adminrsimoblie.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rsip.adminrsimoblie.Model.UserModel;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.KeluhanModel;
import com.rsip.adminrsimoblie.RecyclerView.ListKeluhanActivity;
import com.rsip.adminrsimoblie.Util.SharedPreferenceManager;

public class BalasKeluhanActivity extends AppCompatActivity {
    TextView nama,tanggal,kategori,unit,keluhan,balasan;
    MaterialEditText edtTextBalas;
    Button btnBalas;
    Intent intent;
    FirebaseUser firebaseUser;
    DatabaseReference dbrefrence = FirebaseDatabase.getInstance().getReference();
    DatabaseReference reference;
    SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balas_keluhan);
        findView();
        fillFromIntent();
        balasKeluhan();
    }

    private void findView(){
        sharedPreferenceManager=new SharedPreferenceManager(this);
        nama=findViewById(R.id.rec_text_nama);
        tanggal=findViewById(R.id.rec_text_tgl);
        kategori=findViewById(R.id.rec_text_kategori);
        unit=findViewById(R.id.rec_text_unit);
        keluhan=findViewById(R.id.rec_text_keluhan);
        //balasan=findViewById(R.id.rec_text_balasan);
        edtTextBalas=findViewById(R.id.edtTextBalasan);
        btnBalas=findViewById(R.id.btn_balas);
        intent=getIntent();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
    }

    private void fillFromIntent(){
        nama.setText(intent.getStringExtra("nama"));
        tanggal.setText(intent.getStringExtra("tanggal"));
        kategori.setText(intent.getStringExtra("kategori"));
        unit.setText(intent.getStringExtra("ruangan"));
        keluhan.setText(intent.getStringExtra("keluhan"));
    }

    private void balasKeluhan(){

        btnBalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get nama pembalas


//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        UserModel userModel=snapshot.getValue(UserModel.class);

                        KeluhanModel keluhanModel=new KeluhanModel();
                       // keluhanModel.setIdPembalas(firebaseUser.getUid());
                        keluhanModel.setIdPembalas(sharedPreferenceManager.getSPUsername());
                        keluhanModel.setNamaPembalas(sharedPreferenceManager.getSPNama());
                        keluhanModel.setNama(intent.getStringExtra("nama"));
                        keluhanModel.setTanggal(intent.getStringExtra("tanggal"));
                        keluhanModel.setKategori(intent.getStringExtra("kategori"));
                        keluhanModel.setUnit(intent.getStringExtra("ruangan"));
                        keluhanModel.setKeluhan(intent.getStringExtra("keluhan"));
                        keluhanModel.setSender(intent.getStringExtra("sender"));
                        keluhanModel.setStatusBalas("Dibalas");
                        keluhanModel.setPesanBalasan(edtTextBalas.getText().toString());
                       // keluhanModel.setNamaPembalas("yoga boss");
                        dbrefrence.child("Keluhan").child(intent.getStringExtra("key")).setValue(keluhanModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(BalasKeluhanActivity.this, "Balasan Telah Terkirim", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(BalasKeluhanActivity.this, ListKeluhanActivity.class));
                                finish();
                            }
                        });
                        Log.d("balasKeluhan", "onDataChange: "+keluhanModel.getNamaPembalas());


//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });



                //Log.d("balasKeluhan", "onClick: "+firebaseUser.getUid());


            }
        });

    }

}