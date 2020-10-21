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
import com.rsip.adminrsimoblie.View.TambahMobilActivity;


public class DataMobilAmbulanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private FloatingActionButton fab;
    private DataMobilAmbulanceAdapter mAdapter;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Driver").child("DataMobil");

    private ArrayList<DataMobilAmbulanceModel> modelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_mobil_ambulance);

        findViews();
        setAdapter();


    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DataMobilAmbulanceActivity.this, TambahMobilActivity.class));
            }
        });
        readMobil();
    }


    private void setAdapter() {


        mAdapter = new DataMobilAmbulanceAdapter(DataMobilAmbulanceActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new DataMobilAmbulanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, DataMobilAmbulanceModel model) {

                //handle item click events here
                Toast.makeText(DataMobilAmbulanceActivity.this, "Hey " + model.getJenisKendaraan(), Toast.LENGTH_SHORT).show();


            }
        });


    }

    private void readMobil (){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DataMobilAmbulanceModel mobilAmbulanceModel=dataSnapshot.getValue(DataMobilAmbulanceModel.class);
                    mobilAmbulanceModel.setKey(dataSnapshot.getKey());
                    modelList.add(mobilAmbulanceModel);
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
