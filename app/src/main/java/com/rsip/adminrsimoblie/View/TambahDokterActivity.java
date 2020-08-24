package com.rsip.adminrsimoblie.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rsip.adminrsimoblie.Model.GlideApp;
import com.rsip.adminrsimoblie.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TambahDokterActivity extends AppCompatActivity {

    CircleImageView profilePhoto;
    MaterialEditText namaDokter,spesialistikDokter,jamPraktik,jamPraktikSelesai,keterangan;
    Spinner statusDokter;
    Button btnSimpan;
    Uri filePath;
    FirebaseUser firebaseUser;
    DatabaseReference dbreference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Bitmap bitmap;
    CheckBox senin,selasa,rabu,kamis,jumat,sabtu,minggu;
    Intent intent;
    RelativeLayout boxPhoto;
    LinearLayout linSpiner;
    TextView textTitle;
    int maxid;
    private final int PICK_IMAGE_REQUEST = 22;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_dokter);
        findView();
        intentTambahJam();

        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    maxid=(int)dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=intent.getStringExtra("key");
               if (!TextUtils.isEmpty(key)){
                   saveTambahJam();
                }
               else {
                   getDataFromEditText();
               }
                //getDataFromEditText();
            }
        });

    }
    private void findView(){
        profilePhoto=findViewById(R.id.profile_picture);
        namaDokter=findViewById(R.id.nama_Dokter);
        spesialistikDokter=findViewById(R.id.spesialistik_dokter);
        jamPraktik=findViewById(R.id.jam_praktik);
        jamPraktikSelesai=findViewById(R.id.jam_praktiksampai);
        keterangan=findViewById(R.id.keterangan);
        statusDokter=findViewById(R.id.statusDokter);
        btnSimpan=findViewById(R.id.btn_simpan);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        dbreference= FirebaseDatabase.getInstance().getReference().child("Dokter");
        senin=findViewById(R.id.senin);
        selasa=findViewById(R.id.selasa);
        rabu=findViewById(R.id.rabu);
        kamis=findViewById(R.id.kamis);
        jumat=findViewById(R.id.jumat);
        sabtu=findViewById(R.id.sabtu);
        minggu=findViewById(R.id.minggu);
        intent=getIntent();
        boxPhoto=findViewById(R.id.box_foto);
        linSpiner=findViewById(R.id.linSpiner);
        textTitle=findViewById(R.id.text_title);
    }

    private void selectImage(){
        Intent intent=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,"Pilih..."),PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK){
            filePath=data.getData();
            Log.d("filepath", "onActivityResult: "+filePath.toString());
            //profilePhoto.setImageURI(filePath);
            GlideApp.with(this).load(filePath).into(profilePhoto);
            try{
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                profilePhoto.setImageBitmap(bitmap);
            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getDataFromEditText(){
        String nameDokter=namaDokter.getText().toString();
        String spesial=spesialistikDokter.getText().toString();
        String jam=jamPraktik.getText().toString();
        jam+=" - ";
        jam+=jamPraktikSelesai.getText().toString();
        String statusdokter=statusDokter.getSelectedItem().toString();
        String keterangans=keterangan.getText().toString();
        String id=String.valueOf(maxid+1);
        String data="";
        if (senin.isChecked()){
            data+="Senin";
        }
        if (selasa.isChecked()){
            data+=" Selasa";
        }if (rabu.isChecked()){
            data+=" Rabu";
        }if (kamis.isChecked()){
            data+=" Kamis";
        }if (jumat.isChecked()){
            data+=" Jumat";
        }if (sabtu.isChecked()){
            data+=" Sabtu";
        }if (minggu.isChecked()){
            data+=" Minggu";
        }
        Log.d("checkbox", "getDataFromCheckbox: "+data);
        if (TextUtils.isEmpty(nameDokter)||TextUtils.isEmpty(spesial)||TextUtils.isEmpty(jam)||TextUtils.isEmpty(keterangans)||TextUtils.isEmpty(data)||filePath==null){
            Toast.makeText(this, "Data atau foto Harus Diisi", Toast.LENGTH_SHORT).show();
        }else {
            addDataIntoFirebase(id, nameDokter, spesial, data, jam, statusdokter, keterangans);
        }

    }

    private void addDataIntoFirebase(String id,String namaDokter,String spesialistik,String hariPraktik,String jamPraktik,String status,String keterangan){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("idDokter",id);
        hashMap.put("namaDokter",namaDokter);
        hashMap.put("spesialistik",spesialistik);
        hashMap.put("hariPraktik",hariPraktik);
        hashMap.put("jamPraktik",jamPraktik);
        hashMap.put("status",status);
        hashMap.put("keterangan",keterangan);

        String key=reference.child("Dokter").push().getKey();
        Log.d("key", "addDataIntoFirebase: ");
        reference.child("Dokter").child(key).setValue(hashMap);
        uploadIntoFirebaseStorage(key);

    }
    private void uploadIntoFirebaseStorage(String id){
        progresDialog();
        if (filePath!=null){
            profilePhoto.setDrawingCacheEnabled(true);
            profilePhoto.buildDrawingCache();
            Bitmap bitmaps=((BitmapDrawable) profilePhoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] bytes=baos.toByteArray();
            String namafile=id+".jpg";
            String namafolder="ProfilePicture/"+namafile;

            UploadTask uploadTask= storageReference.child(namafolder).putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(TambahDokterActivity.this, "Uploading Berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TambahDokterActivity.this,InfoDokterActivity.class));
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(TambahDokterActivity.this, "Uploading Gagal", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    
                }
            });
        }else{
            Toast.makeText(this, "Foto Harus diIsi", Toast.LENGTH_SHORT).show();
        }
    }

    private void intentTambahJam(){
        if (!intent.getStringExtra("tambah").equalsIgnoreCase("tambah")){

        }else{
            textTitle.setText("Tambah Jam Praktik Dokter");
            boxPhoto.setVisibility(View.GONE);
            linSpiner.setVisibility(View.GONE);
            namaDokter.setVisibility(View.GONE);
            spesialistikDokter.setVisibility(View.GONE);
            keterangan.setVisibility(View.GONE);
        }

    }

    private void saveTambahJam(){
        DokterModel dokterModel=new DokterModel();

        dokterModel.setIdDokter(intent.getStringExtra("id"));
        dokterModel.setNamaDokter(intent.getStringExtra("namadokter"));
        dokterModel.setSpesialistik(intent.getStringExtra("spesial"));
        dokterModel.setHariPraktik(intent.getStringExtra("hari"));
        dokterModel.setJamPraktik(intent.getStringExtra("jam"));
        dokterModel.setStatus(intent.getStringExtra("status"));
        dokterModel.setKeterangan(intent.getStringExtra("ket"));

        String jam=jamPraktik.getText().toString();
        jam+=" - ";
        jam+=jamPraktikSelesai.getText().toString();
        dokterModel.setJamPraktikKedua(jam);

        String data="";
        if (senin.isChecked()){
            data+="Senin";
        }
        if (selasa.isChecked()){
            data+=" Selasa";
        }if (rabu.isChecked()){
            data+=" Rabu";
        }if (kamis.isChecked()){
            data+=" Kamis";
        }if (jumat.isChecked()){
            data+=" Jumat";
        }if (sabtu.isChecked()){
            data+=" Sabtu";
        }if (minggu.isChecked()){
            data+=" Minggu";
        }
        dokterModel.setHariPraktikKedua(data);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        reference.child("Dokter").child(intent.getStringExtra("key")).setValue(dokterModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(TambahDokterActivity.this, "Waktu Praktik Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TambahDokterActivity.this,InfoDokterActivity.class));
                finish();
            }
        });
    }
    private void progresDialog(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Sedang Menambahkan Info Dokter");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}