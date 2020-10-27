package com.rsip.adminrsimoblie.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import android.widget.Toast;
import android.os.Handler;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rsip.adminrsimoblie.View.DetailPekerjaanIp2Activity;
import com.rsip.adminrsimoblie.View.TambahDataPekerjaanIp2Activity;


public class DataPekerjaanIp2Activity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private FloatingActionButton fab;
    private DataPekerjaanIp2Adapter mAdapter;

    private ArrayList<DataPekerjaanIp2Model> modelList = new ArrayList<>();

    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("IPPSRS").child("DataPekerjaan");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pekerjaan_ip2);

        findViews();
        setAdapter();


    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DataPekerjaanIp2Activity.this, TambahDataPekerjaanIp2Activity.class));
            }
        });
        readFromDatabase();
    }


    private void setAdapter() {

        mAdapter = new DataPekerjaanIp2Adapter(DataPekerjaanIp2Activity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new DataPekerjaanIp2Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, DataPekerjaanIp2Model model) {

                //handle item click events here
               // Toast.makeText(DataPekerjaanIp2Activity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DataPekerjaanIp2Activity.this, DetailPekerjaanIp2Activity.class);
                intent.putExtra("key",model.getKey());
                intent.putExtra("namaPekerjaan",model.getNamaPekerjaan());
                intent.putExtra("namaPekerja",model.getNamaPekerja());
                intent.putExtra("lokasiPekerjaan",model.getLokasiPekerjaan());
                intent.putExtra("tanggalPengerjaan",model.getTanggalPengerjaan());
                intent.putExtra("jamPengerjaan",model.getJamPengerjaan());
                intent.putExtra("tanggalPengerjaanOtomatis",model.getTanggalPengerjaanOtomatis());
                intent.putExtra("jamPengerjaanOtomatis",model.getJamPengerjaanOtomatis());
                startActivity(intent);
                finish();

            }
        });


    }

    private void readFromDatabase(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DataPekerjaanIp2Model dataPekerjaanIp2Model=dataSnapshot.getValue(DataPekerjaanIp2Model.class);
                    dataPekerjaanIp2Model.setKey(dataSnapshot.getKey());
                    if (dataPekerjaanIp2Model.getStatus().equalsIgnoreCase("no")) {
                        modelList.add(dataPekerjaanIp2Model);
                    }
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
