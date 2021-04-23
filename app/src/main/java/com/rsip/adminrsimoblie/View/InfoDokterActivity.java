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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.rsip.adminrsimoblie.Util.Koneksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

    private void getJadwalDokterAll(){
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Koneksi.TAMPIL_ALL_JADWAL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length()>0){
                    try {
                        JSONArray root=response.getJSONArray("response");
                        for (int i = 0; i < root.length() ; i++) {
                            JSONObject data=root.getJSONObject(i);
                            
                        }
                    }catch (JSONException e){

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

}
