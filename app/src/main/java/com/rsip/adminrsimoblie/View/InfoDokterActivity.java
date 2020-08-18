package com.rsip.adminrsimoblie.View;

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

import java.io.Serializable;
import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rsip.adminrsimoblie.R;

import android.widget.Toast;
import android.os.Handler;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class InfoDokterActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private FloatingActionButton fab;
    private RecyclerViewAdapter mAdapter;

    private ArrayList<DokterModel> modelList = new ArrayList<>();

    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Dokter");
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dokter);

        findViews();
        readDokter();
        progresDialog();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InfoDokterActivity.this,TambahDokterActivity.class);
                intent.putExtra("tambah","fab");
                startActivity(intent);
            }
        });


    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

    }
    private void readDokter(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DokterModel dokterModel=dataSnapshot.getValue(DokterModel.class);
                    dokterModel.setKey(dataSnapshot.getKey());
                    Log.d("randomkey", "onDataChange: "+dataSnapshot.getKey());
                    modelList.add(dokterModel);
                    setAdapter();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setAdapter() {


//        modelList.add(new DokterModel("Android", "Hello " + " Android"));
//        modelList.add(new DokterModel("Beta", "Hello " + " Beta"));
//        modelList.add(new DokterModel("Cupcake", "Hello " + " Cupcake"));
//        modelList.add(new DokterModel("Donut", "Hello " + " Donut"));
//        modelList.add(new DokterModel("Eclair", "Hello " + " Eclair"));
//        modelList.add(new DokterModel("Froyo", "Hello " + " Froyo"));
//        modelList.add(new DokterModel("Gingerbread", "Hello " + " Gingerbread"));
//        modelList.add(new DokterModel("Honeycomb", "Hello " + " Honeycomb"));
//        modelList.add(new DokterModel("Ice Cream Sandwich", "Hello " + " Ice Cream Sandwich"));
//        modelList.add(new DokterModel("Jelly Bean", "Hello " + " Jelly Bean"));
//        modelList.add(new DokterModel("KitKat", "Hello " + " KitKat"));
//        modelList.add(new DokterModel("Lollipop", "Hello " + " Lollipop"));
//        modelList.add(new DokterModel("Marshmallow", "Hello " + " Marshmallow"));
//        modelList.add(new DokterModel("Nougat", "Hello " + " Nougat"));
//        modelList.add(new DokterModel("Android O", "Hello " + " Android O"));


        mAdapter = new RecyclerViewAdapter(InfoDokterActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(InfoDokterActivity.this, R.drawable.divider_recyclerview));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, DokterModel model) {

                //handle item click events here
                //Toast.makeText(InfoDokterActivity.this, "Hey " + model.getNamaDokter(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(InfoDokterActivity.this,DetailDokterActivity.class);
                intent.putExtra("key",modelList.get(position).getKey());
                intent.putExtra("id",modelList.get(position).getIdDokter());
                intent.putExtra("namadokter",modelList.get(position).getNamaDokter());
                intent.putExtra("spesial",modelList.get(position).getSpesialistik());
                intent.putExtra("hari",modelList.get(position).getHariPraktik());
                intent.putExtra("jam",modelList.get(position).getJamPraktik());
                intent.putExtra("status",modelList.get(position).getStatus());
                intent.putExtra("ket",modelList.get(position).getKeterangan());
                intent.putExtra("harikedua",modelList.get(position).getHariPraktikKedua());
                intent.putExtra("jamkedua",modelList.get(position).getJamPraktikKedua());
                startActivity(intent);
                finish();
                Log.d("detail", "onItemClick: "+modelList.get(position).getHariPraktikKedua());


            }
        });


    }

    private void progresDialog(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Sedang Mengambil Info dokter");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

}
