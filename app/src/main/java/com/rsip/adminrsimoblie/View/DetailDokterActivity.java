package com.rsip.adminrsimoblie.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rsip.adminrsimoblie.Model.GlideApp;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.Util.CacheUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailDokterActivity extends AppCompatActivity {
    Button btnTambahWaktu,btnEditInfo,btnHapusDokter;
    Intent intent;
    CircleImageView profilePicture;
    TextView namaDokter,spesialDokter,hariDokter,jamDokter,statusDokter,ketDokter;
    StorageReference storageReference;
    DatabaseReference dbReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dokter);
        findView();
        intent=getIntent();
        fillDataFromIntent();
        getPhotoDokter();
        tambahWaktu();
        editInfoDokter();
        btnHapusDokter();
    }

    private void findView(){
        btnTambahWaktu=findViewById(R.id.btn_tambahjamdokter);
        btnEditInfo=findViewById(R.id.btn_Editdokter);
        btnHapusDokter=findViewById(R.id.btn_hapusdokter);
        profilePicture=findViewById(R.id.profile_picture);
        namaDokter=findViewById(R.id.textnamaDokter);
        spesialDokter=findViewById(R.id.text_spesialisdokter);
        hariDokter=findViewById(R.id.text_haridokter);
        jamDokter=findViewById(R.id.text_jamdokter);
        statusDokter=findViewById(R.id.text_statusDokter);
        ketDokter=findViewById(R.id.textketeranganDokter);
        dbReference= FirebaseDatabase.getInstance().getReference("Dokter");

    }
    private void fillDataFromIntent(){
        String harikedua=intent.getStringExtra("harikedua");
        if (TextUtils.isEmpty(harikedua)) {
            namaDokter.setText(intent.getStringExtra("namadokter"));
            spesialDokter.setText(intent.getStringExtra("spesial"));
            hariDokter.setText(intent.getStringExtra("hari"));
            jamDokter.setText(intent.getStringExtra("jam"));
            statusDokter.setText(intent.getStringExtra("status"));
            ketDokter.setText(intent.getStringExtra("ket"));
        }else{
            namaDokter.setText(intent.getStringExtra("namadokter"));
            spesialDokter.setText(intent.getStringExtra("spesial"));
            String hari=intent.getStringExtra("hari");
            hari+=" dan ";
            hari+=intent.getStringExtra("harikedua");
            hariDokter.setText(hari);

            String jam=intent.getStringExtra("jam");
            jam+=" dan ";
            jam+=intent.getStringExtra("jamkedua");
            jamDokter.setText(jam);
            statusDokter.setText(intent.getStringExtra("status"));
            ketDokter.setText(intent.getStringExtra("ket"));
        }
    }

    private void getPhotoDokter(){
        CacheUtils cacheUtil=new CacheUtils();
        cacheUtil.deleteCache(this);
        storageReference= FirebaseStorage.getInstance().getReference("ProfilePicture/"+intent.getStringExtra("key")+".jpg");
        Log.d("detaildokter", "getPhotoDokter: "+storageReference);
        GlideApp.with(this).load(storageReference).into(profilePicture);

    }

    private void tambahWaktu(){
        String harikedua=intent.getStringExtra("harikedua");
        if (TextUtils.isEmpty(harikedua)) {
            btnTambahWaktu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(DetailDokterActivity.this, TambahDokterActivity.class);
                    intent1.putExtra("tambah", "tambah");
                    intent1.putExtra("key", intent.getStringExtra("key"));
                    intent1.putExtra("id", intent.getStringExtra("id"));
                    intent1.putExtra("namadokter", intent.getStringExtra("namadokter"));
                    intent1.putExtra("spesial", intent.getStringExtra("spesial"));
                    intent1.putExtra("hari", intent.getStringExtra("hari"));
                    intent1.putExtra("jam", intent.getStringExtra("jam"));
                    intent1.putExtra("status", intent.getStringExtra("status"));
                    intent1.putExtra("ket", intent.getStringExtra("ket"));
                    startActivity(intent1);
                    finish();
                }
            });
        }
        else btnTambahWaktu.setVisibility(View.GONE);
    }

    private void editInfoDokter(){
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(DetailDokterActivity.this,EditDokterActivity.class);
                intent1.putExtra("edit","edit");
                intent1.putExtra("key",intent.getStringExtra("key"));
                intent1.putExtra("id",intent.getStringExtra("id"));
                intent1.putExtra("namadokter",intent.getStringExtra("namadokter"));
                intent1.putExtra("spesial",intent.getStringExtra("spesial"));
                intent1.putExtra("hari",intent.getStringExtra("hari"));
                intent1.putExtra("jam",intent.getStringExtra("jam"));
                intent1.putExtra("status",intent.getStringExtra("status"));
                intent1.putExtra("ket",intent.getStringExtra("ket"));
                intent1.putExtra("harikedua",intent.getStringExtra("harikedua"));
                intent1.putExtra("jamkedua",intent.getStringExtra("jamkedua"));
                startActivity(intent1);
                finish();
            }
        });
    }

    private void btnHapusDokter(){
        btnHapusDokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDeleteDokter();
            }
        });

    }

    private void dialogDeleteDokter(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Hapus Data Dokter");
        builder.setMessage("Hapus Data Dokter "+intent.getStringExtra("namadokter")+" ?");
        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDokter();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void deleteDokter(){
        if (dbReference!=null){
            String key=intent.getStringExtra("key");
            dbReference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DetailDokterActivity.this, "Dokter Berhasil diHapus", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailDokterActivity.this,InfoDokterActivity.class));
                    finish();
                }
            });

            //delete foto
            FirebaseStorage storages=FirebaseStorage.getInstance();
            StorageReference storageReference=storages.getReference("ProfilePicture/"+key+".jpg");
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("editFoto", "onSuccess: Foto Berhasil dihapus");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("editFoto", "onFailure: gagal dihapus");

                }
            });
            CacheUtils cacheUtil=new CacheUtils();
            cacheUtil.deleteCache(this);
        }
    }
}