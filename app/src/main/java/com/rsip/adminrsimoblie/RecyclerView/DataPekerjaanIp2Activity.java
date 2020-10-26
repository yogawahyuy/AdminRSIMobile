package com.rsip.adminrsimoblie.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

import com.rsip.adminrsimoblie.R;

import android.widget.Toast;
import android.os.Handler;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DataPekerjaanIp2Activity extends AppCompatActivity {

    private RecyclerView recyclerView;


    private FloatingActionButton fab;
    private DataPekerjaanIp2Adapter mAdapter;

    private ArrayList<DataPekerjaanIp2Model> modelList = new ArrayList<>();


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
        ;
    }


    private void setAdapter() {


        modelList.add(new DataPekerjaanIp2Model("Android", "Hello " + " Android"));
        modelList.add(new DataPekerjaanIp2Model("Beta", "Hello " + " Beta"));
        modelList.add(new DataPekerjaanIp2Model("Cupcake", "Hello " + " Cupcake"));
        modelList.add(new DataPekerjaanIp2Model("Donut", "Hello " + " Donut"));
        modelList.add(new DataPekerjaanIp2Model("Eclair", "Hello " + " Eclair"));
        modelList.add(new DataPekerjaanIp2Model("Froyo", "Hello " + " Froyo"));
        modelList.add(new DataPekerjaanIp2Model("Gingerbread", "Hello " + " Gingerbread"));
        modelList.add(new DataPekerjaanIp2Model("Honeycomb", "Hello " + " Honeycomb"));
        modelList.add(new DataPekerjaanIp2Model("Ice Cream Sandwich", "Hello " + " Ice Cream Sandwich"));
        modelList.add(new DataPekerjaanIp2Model("Jelly Bean", "Hello " + " Jelly Bean"));
        modelList.add(new DataPekerjaanIp2Model("KitKat", "Hello " + " KitKat"));
        modelList.add(new DataPekerjaanIp2Model("Lollipop", "Hello " + " Lollipop"));
        modelList.add(new DataPekerjaanIp2Model("Marshmallow", "Hello " + " Marshmallow"));
        modelList.add(new DataPekerjaanIp2Model("Nougat", "Hello " + " Nougat"));
        modelList.add(new DataPekerjaanIp2Model("Android O", "Hello " + " Android O"));


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
                Toast.makeText(DataPekerjaanIp2Activity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();


            }
        });


    }


}
