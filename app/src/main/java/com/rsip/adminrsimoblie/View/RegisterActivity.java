package com.rsip.adminrsimoblie.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rsip.adminrsimoblie.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username,namaLengkap,password;
    RadioGroup listBagian;
    Button btnDaftar;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();
    }

    private void findView(){
        username=findViewById(R.id.username);
        namaLengkap=findViewById(R.id.namaLengkap);
        password=findViewById(R.id.password);
        listBagian=findViewById(R.id.radGroupBagian);
        btnDaftar=findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    private void register(){
        String user=username.getText().toString();
        String bagian="";
        int id=listBagian.getCheckedRadioButtonId();
        switch (id){
            case R.id.radioBtnBidyan:
                bagian="bidyan";
                break;
            case R.id.radioBtnSupir:
                bagian="supir";
                break;
            case R.id.radioBtnIp2:
                bagian="ip2";
                break;
        }
        reference= FirebaseDatabase.getInstance().getReference("UserAdmin").child(user);

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("username",user);
        hashMap.put("namaLengkap",namaLengkap.getText().toString());
        hashMap.put("password",password.getText().toString());
        hashMap.put("bagian",bagian);
        reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });


    }
}