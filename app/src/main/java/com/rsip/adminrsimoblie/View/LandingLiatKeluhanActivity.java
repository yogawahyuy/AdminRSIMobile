package com.rsip.adminrsimoblie.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.ListKeluhanActivity;

public class LandingLiatKeluhanActivity extends AppCompatActivity {

    RadioGroup radPencarianKeluhan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_liat_keluhan);
        findViews();
    }

    private void findViews(){
        radPencarianKeluhan=findViewById(R.id.radGroup_keluhan);


        radPencarianKeluhan.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.radioBtnBaca:
                    startActivity(new Intent(LandingLiatKeluhanActivity.this, ListKeluhanActivity.class));
                    break;
                case R.id.radioBtnBelumBaca:
                    break;
                case R.id.radioBtnSemua:
                    break;
            }
        });
    }
}