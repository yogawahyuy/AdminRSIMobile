package com.rsip.adminrsimoblie.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.View.DetailPekerjaanIp2Activity;

import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


public class DataPekerjaanSelesaiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private DataPekerjaanSelesaiAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<DataPekerjaanSelesaiModel> modelList = new ArrayList<>();

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("IPPSRS").child("PekerjaanSelesai");
    TextView emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pekerjaan_selesai);

        findViews();
        setAdapter();


    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyView=findViewById(R.id.empty_view);
        readData();
        progresDialog();
    }


    private void setAdapter() {
        mAdapter = new DataPekerjaanSelesaiAdapter(DataPekerjaanSelesaiActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new DataPekerjaanSelesaiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, DataPekerjaanSelesaiModel model) {

                //handle item click events here
                //Toast.makeText(DataPekerjaanSelesaiActivity.this, "Hey " + model.getNamaPekerja(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DataPekerjaanSelesaiActivity.this, DetailPekerjaanIp2Activity.class);
                intent.putExtra("key",model.getKey());
                intent.putExtra("namaPekerjaan",model.getNamaPekerjaan());
                intent.putExtra("namaPekerja",model.getNamaPekerja());
                intent.putExtra("lokasiPekerjaan",model.getLokasiPekerjaan());
                intent.putExtra("tanggalPengerjaan",model.getTanggalPengerjaan());
                intent.putExtra("jamPengerjaan",model.getJamPengerjaan());
                intent.putExtra("tanggalPengerjaanOtomatis",model.getTanggalPengerjaanOtomatis());
                intent.putExtra("jamPengerjaanOtomatis",model.getJamPengerjaanOtomatis());
                intent.putExtra("tanggalSelesaiOtomatis",model.getTanggalSelesaiOtomatis());
                intent.putExtra("jamSelesaiOtomatis",model.getJamSelesaiOtomatis());
                intent.putExtra("ket","selese");
                startActivity(intent);
                finish();

            }
        });


    }

    private void readData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DataPekerjaanSelesaiModel dataPekerjaanSelesaiModel=dataSnapshot.getValue(DataPekerjaanSelesaiModel.class);
                    dataPekerjaanSelesaiModel.setKey(dataSnapshot.getKey());
                    modelList.add(dataPekerjaanSelesaiModel);
                    if (modelList.size()==0){
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }else{
                        emptyView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void progresDialog(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Sedang Mengambil Data");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}
