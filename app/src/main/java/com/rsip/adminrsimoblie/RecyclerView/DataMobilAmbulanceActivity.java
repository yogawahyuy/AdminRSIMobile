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

import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Handler;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rsip.adminrsimoblie.View.DetailAmbulanceActivity;
import com.rsip.adminrsimoblie.View.TambahMobilActivity;


public class DataMobilAmbulanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private FloatingActionButton fab;
    private DataMobilAmbulanceAdapter mAdapter;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Driver").child("DataMobil");

    private ArrayList<DataMobilAmbulanceModel> modelList = new ArrayList<>();

    ProgressDialog progressDialog;
    LinearLayout linEmptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_mobil_ambulance);

        findViews();
        //setAdapter();


    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(v -> startActivity(new Intent(DataMobilAmbulanceActivity.this, TambahMobilActivity.class)));
        readMobil();
        progresDialog();
        linEmptyView=findViewById(R.id.lin_emptyView);
        linEmptyView.setVisibility(View.GONE);
    }


    private void setAdapter() {


        mAdapter = new DataMobilAmbulanceAdapter(DataMobilAmbulanceActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener((view, position, model) -> {

            //handle item click events here
            //Toast.makeText(DataMobilAmbulanceActivity.this, "Hey " + model.getJenisKendaraan(), Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(DataMobilAmbulanceActivity.this, DetailAmbulanceActivity.class);
            intent.putExtra("key",model.getKey());
            intent.putExtra("nopol",model.getNoPlat());
            if (model.getJenisKendaraan().equalsIgnoreCase("0")){
                intent.putExtra("jenis","Ambulance");
            }else{
                intent.putExtra("jenis","Mobil Umum");
            }
            intent.putExtra("tipe",model.getTypeKendaraan());
            intent.putExtra("merek",model.getMerekKendaraan());
            intent.putExtra("pajak",model.getPajakKendaraan());
            intent.putExtra("plat",model.getPajakPlatKendaraan());
            startActivity(intent);
            finish();

        });


    }

    private void readMobil (){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        DataMobilAmbulanceModel mobilAmbulanceModel = dataSnapshot.getValue(DataMobilAmbulanceModel.class);
                        mobilAmbulanceModel.setKey(dataSnapshot.getKey());
                        modelList.add(mobilAmbulanceModel);
                        setAdapter();
                        progressDialog.dismiss();
                    }
                }else {
                    //Toast.makeText(DataMobilAmbulanceActivity.this, "cek", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    linEmptyView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void progresDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Sedang Mengambil Data");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

}
