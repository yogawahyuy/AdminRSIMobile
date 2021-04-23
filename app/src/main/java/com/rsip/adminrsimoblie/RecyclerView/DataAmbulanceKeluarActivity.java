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

import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rsip.adminrsimoblie.R;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rsip.adminrsimoblie.View.DetailAmbulanceKeluarActivity;
import com.rsip.adminrsimoblie.View.TambahMobilKeluarActivity;


public class DataAmbulanceKeluarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private FloatingActionButton fab;
    private AmbulanceKeluarAdapter mAdapter;
    private ProgressDialog progressDialog;

    private ArrayList<AmbulanceKeluarModel> modelList = new ArrayList<>();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Driver").child("MobilKeluar");
    LinearLayout linEmptyView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ambulance_keluar);

        findViews();


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
        linEmptyView=findViewById(R.id.lin_emptyView);
        linEmptyView.setVisibility(View.GONE);
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
        progresDialog();
        Log.d("ambulance", "onDataChange: masuk sini");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        AmbulanceKeluarModel ambulanceKeluarModel = dataSnapshot.getValue(AmbulanceKeluarModel.class);
                        ambulanceKeluarModel.setKey(dataSnapshot.getKey());
                        if (ambulanceKeluarModel.getStatus().equalsIgnoreCase("Keluar")) {
                            modelList.add(ambulanceKeluarModel);
                        }

                        Log.d("ambulance", "onDataChange: " + ambulanceKeluarModel.getStatus());
                        if (modelList.size() == 0) {
                            linEmptyView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            progressDialog.dismiss();
                        } else {
                            linEmptyView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                        setAdapter();
                    }
                }else{
                    progressDialog.dismiss();
                    linEmptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                linEmptyView.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
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
