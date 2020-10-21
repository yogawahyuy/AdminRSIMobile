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
import com.rsip.adminrsimoblie.View.DetailAmbulanceKeluarActivity;
import com.rsip.adminrsimoblie.View.TambahMobilKeluarActivity;


public class DataAmbulanceKeluarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private FloatingActionButton fab;
    private AmbulanceKeluarAdapter mAdapter;

    private ArrayList<AmbulanceKeluarModel> modelList = new ArrayList<>();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Driver").child("MobilKeluar");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ambulance_keluar);

        findViews();
        setAdapter();


    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DataAmbulanceKeluarActivity.this, TambahMobilKeluarActivity.class));
            }
        });
        getDataFromFirebase();
    }


    private void setAdapter() {

        mAdapter = new AmbulanceKeluarAdapter(DataAmbulanceKeluarActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new AmbulanceKeluarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, AmbulanceKeluarModel model) {
//                String keys=modelList.get(position).getKey();
//                String noplat=modelList.get(position).getNoPlat();
//                String jenisKendaraan=modelList.get(position).getJenisKendaraan();
//                String merekKendaraan=modelList.get(position).getMerkKendaraan();
//                String pajakKendaraan=modelList.get(position).get();
//                String pajakPlatKendaraan=modelList.get(posisi).getPajakPlatKendaraan();
//                String typeKendaraan=modelList.get(posisi).getTypeKendaraan();
//                String status=modelList.get(posisi).getStatus();
                //handle item click events here
               // Toast.makeText(DataAmbulanceKeluarActivity.this, "Hey " + model.getTujuan(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DataAmbulanceKeluarActivity.this, DetailAmbulanceKeluarActivity.class);
                intent.putExtra("NoPlat",model.getNoPlat());
                intent.putExtra("driver",model.getDriver());
                intent.putExtra("jamInput",model.getJamInput());
                intent.putExtra("jamOtomatis",model.getJamOtomatis());
                intent.putExtra("jarak",model.getJarak());
                intent.putExtra("jenisKendaraan",model.getJenisKendaraan());
                intent.putExtra("merkKendaraan",model.getMerkKendaraan());
                intent.putExtra("tanggalInput",model.getTanggalInput());
                intent.putExtra("tanggalOtomatis",model.getTanggalOtomatis());
                intent.putExtra("tujuan",model.getTujuan());
                intent.putExtra("status",model.getStatus());
                intent.putExtra("key",modelList.get(position).getKey());

                startActivity(intent);
                finish();

            }
        });


    }

    private void getDataFromFirebase(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    AmbulanceKeluarModel ambulanceKeluarModel=dataSnapshot.getValue(AmbulanceKeluarModel.class);
                    ambulanceKeluarModel.setKey(dataSnapshot.getKey());
                    if (ambulanceKeluarModel.getStatus().equalsIgnoreCase("Keluar")){
                        modelList.add(ambulanceKeluarModel);
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