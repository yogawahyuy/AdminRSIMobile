package com.rsip.adminrsimoblie.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import android.widget.Spinner;
import android.widget.Toast;
import android.os.Handler;


public class ListKeluhanActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private KeluhanAdapter mAdapter;

    private ArrayList<KeluhanModel> modelList = new ArrayList<>();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Keluhan");
    Spinner unitSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_keluhan);

        findViews();
        readKeluhan();


    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        unitSpinner=findViewById(R.id.spiner_unit);

    }


    private void setAdapter() {


//        modelList.add(new KeluhanModel("Android", "Hello " + " Android"));
//        modelList.add(new KeluhanModel("Beta", "Hello " + " Beta"));
//        modelList.add(new KeluhanModel("Cupcake", "Hello " + " Cupcake"));
//        modelList.add(new KeluhanModel("Donut", "Hello " + " Donut"));
//        modelList.add(new KeluhanModel("Eclair", "Hello " + " Eclair"));
//        modelList.add(new KeluhanModel("Froyo", "Hello " + " Froyo"));
//        modelList.add(new KeluhanModel("Gingerbread", "Hello " + " Gingerbread"));
//        modelList.add(new KeluhanModel("Honeycomb", "Hello " + " Honeycomb"));
//        modelList.add(new KeluhanModel("Ice Cream Sandwich", "Hello " + " Ice Cream Sandwich"));
//        modelList.add(new KeluhanModel("Jelly Bean", "Hello " + " Jelly Bean"));
//        modelList.add(new KeluhanModel("KitKat", "Hello " + " KitKat"));
//        modelList.add(new KeluhanModel("Lollipop", "Hello " + " Lollipop"));
//        modelList.add(new KeluhanModel("Marshmallow", "Hello " + " Marshmallow"));
//        modelList.add(new KeluhanModel("Nougat", "Hello " + " Nougat"));
//        modelList.add(new KeluhanModel("Android O", "Hello " + " Android O"));


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


            }
        });


    }

    private void readKeluhan(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    KeluhanModel keluhanModel=dataSnapshot.getValue(KeluhanModel.class);
                    keluhanModel.setKey(dataSnapshot.getKey());
                    if (keluhanModel.getUnit().equals(unitSpinner.getSelectedItem().toString())){
                        modelList.add(keluhanModel);
                    }
                    else if (unitSpinner.getSelectedItem().toString().equals("Semua")){
                        modelList.add(keluhanModel);
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
