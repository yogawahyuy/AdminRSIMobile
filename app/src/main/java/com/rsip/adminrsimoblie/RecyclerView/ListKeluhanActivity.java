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
import com.rsip.adminrsimoblie.View.RincianKeluhanActivity;

import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Handler;


public class ListKeluhanActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private KeluhanAdapter mAdapter;

    private ArrayList<KeluhanModel> modelList = new ArrayList<>();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Keluhan");
    Spinner unitSpinner;
    RadioGroup rbGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_keluhan);

        findViews();
        readKeluhan("semua");


    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        unitSpinner=findViewById(R.id.spiner_unit);
        rbGroup=findViewById(R.id.radioGroup);

    }

//    private void radioChecked(View view){
//        boolean checked=((RadioButton) view).isChecked();
//        switch (view.getId()){
//            case R.id.semua:
//                if (checked)
//
//        }
//    }


    private void setAdapter() {
        mAdapter = new KeluhanAdapter(ListKeluhanActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(ListKeluhanActivity.this, R.drawable.divider_recyclerview));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new KeluhanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, KeluhanModel model) {

                //handle item click events here
                //Toast.makeText(ListKeluhanActivity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(ListKeluhanActivity.this,RincianKeluhanActivity.class);
            intent.putExtra("key",modelList.get(position).getKey());
            intent.putExtra("nama",modelList.get(position).getNama());
            intent.putExtra("tanggal",modelList.get(position).getTanggal());
            intent.putExtra("kategori",modelList.get(position).getKategori());
            intent.putExtra("ruangan",modelList.get(position).getUnit());
            intent.putExtra("keluhan",modelList.get(position).getKeluhan());
            intent.putExtra("balasan",modelList.get(position).getPesanBalasan());
            intent.putExtra("sender",modelList.get(position).getSender());
            intent.putExtra("status",modelList.get(position).getStatusBalas());
            intent.putExtra("idpembalas",modelList.get(position).getIdPembalas());

            startActivity(intent);
            finish();

            }
        });


    }

    private void readKeluhan(final String status){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    KeluhanModel keluhanModel=dataSnapshot.getValue(KeluhanModel.class);
                    keluhanModel.setKey(dataSnapshot.getKey());
//                    if (status.equals(keluhanModel.getStatusBalas())) {
                        modelList.add(keluhanModel);
                    //}
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
