package com.rsip.adminrsimoblie.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rsip.adminrsimoblie.Model.GlideApp;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.Util.CacheUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditDokterActivity extends AppCompatActivity {

    CircleImageView profilePhoto;
    MaterialEditText namaDokter,spesialistikDokter,jamPraktik,jamPraktikSelesai,keterangan,jamPraktikKedua,jamPraktikSelesaiKedua;
    Spinner statusDokter;
    Button btnSimpan;
    LinearLayout linHariKedua,linJamKedua;
    CheckBox senin,selasa,rabu,kamis,jumat,sabtu,minggu;
    CheckBox senin2,selasa2,rabu2,kamis2,jumat2,sabtu2,minggu2;
    Intent intent;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference dbReference;
    private final int PICK_IMAGE_REQUEST = 22;
    Bitmap bitmap;
    Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dokter);
        findView();
        fillDataFromIntent();
        getPhotoDokter();

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateToFirebase();
            }
        });
    }

    private void findView(){
        profilePhoto=findViewById(R.id.profile_picture);
        namaDokter=findViewById(R.id.nama_Dokter);
        spesialistikDokter=findViewById(R.id.spesialistik_dokter);
        jamPraktik=findViewById(R.id.jam_praktik);
        jamPraktikSelesai=findViewById(R.id.jam_praktiksampai);
        jamPraktikKedua=findViewById(R.id.jam_praktikkedua);
        jamPraktikSelesaiKedua=findViewById(R.id.jam_praktiksampaikedua);
        keterangan=findViewById(R.id.keterangan);
        statusDokter=findViewById(R.id.statusDokter);
        btnSimpan=findViewById(R.id.btn_simpan);
        linHariKedua=findViewById(R.id.lin_checkboxkedua);
        linJamKedua=findViewById(R.id.lin_jamkedua);
        senin=findViewById(R.id.senin);
        selasa=findViewById(R.id.selasa);
        rabu=findViewById(R.id.rabu);
        kamis=findViewById(R.id.kamis);
        jumat=findViewById(R.id.jumat);
        sabtu=findViewById(R.id.sabtu);
        minggu=findViewById(R.id.minggu);
        senin2=findViewById(R.id.seninkedua);
        selasa2=findViewById(R.id.selasakedua);
        rabu2=findViewById(R.id.rabukedua);
        kamis2=findViewById(R.id.kamiskedua);
        jumat2=findViewById(R.id.jumatkedua);
        sabtu2=findViewById(R.id.sabtukedua);
        minggu2=findViewById(R.id.minggukedua);
        intent=getIntent();
        dbReference= FirebaseDatabase.getInstance().getReference("Dokter").child(intent.getStringExtra("key"));
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
    }

    private void fillDataFromIntent(){
        String harikedua=intent.getStringExtra("harikedua");
        if (TextUtils.isEmpty(harikedua)){
            linHariKedua.setVisibility(View.GONE);
            linJamKedua.setVisibility(View.GONE);
            namaDokter.setText(intent.getStringExtra("namadokter"));
            spesialistikDokter.setText(intent.getStringExtra("spesial"));
            String haripertama=intent.getStringExtra("hari");
            if (haripertama.contains("Senin")){
                senin.setChecked(true);
            } if (haripertama.contains("Selasa")){
                selasa.setChecked(true);
            } if (haripertama.contains("Rabu")){
                rabu.setChecked(true);
            } if (haripertama.contains("Kamis")){
                kamis.setChecked(true);
            } if (haripertama.contains("Jumat")){
                jumat.setChecked(true);
            } if (haripertama.contains("Sabtu")){
                sabtu.setChecked(true);
            } if (haripertama.contains("Minggu")){
                minggu.setChecked(true);
            }
            //set jam
            String jamprakter=intent.getStringExtra("jam");
            String jampraktekselesai=jamprakter.substring(jamprakter.indexOf("-")+1);
            jampraktekselesai.trim();
            String jampraktekmulai=jamprakter.substring(0,jamprakter.indexOf("-")-1);
            jampraktekmulai.trim();
            Log.d("hello", "fillDataFromIntent: "+jampraktekselesai);
            Log.d("hello", "fillDataFromIntent: "+jampraktekmulai);
            jamPraktik.setText(jampraktekmulai);
            jamPraktikSelesai.setText(jampraktekselesai);

            // set status

            String statusnya=intent.getStringExtra("status");
            if (statusnya.contains("Praktik")){
                statusDokter.setSelection(0);
            }else statusDokter.setSelection(1);

            keterangan.setText(intent.getStringExtra("ket"));
        }
        else {
            linHariKedua.setVisibility(View.VISIBLE);
            linJamKedua.setVisibility(View.VISIBLE);
            namaDokter.setText(intent.getStringExtra("namadokter"));
            spesialistikDokter.setText(intent.getStringExtra("spesial"));
            //checkbox pertama
            String haripertama=intent.getStringExtra("hari");
            if (haripertama.contains("Senin")){
                senin.setChecked(true);
            } if (haripertama.contains("Selasa")){
                selasa.setChecked(true);
            } if (haripertama.contains("Rabu")){
                rabu.setChecked(true);
            } if (haripertama.contains("Kamis")){
                kamis.setChecked(true);
            } if (haripertama.contains("Jumat")){
                jumat.setChecked(true);
            } if (haripertama.contains("Sabtu")){
                sabtu.setChecked(true);
            } if (haripertama.contains("Minggu")){
                minggu.setChecked(true);
            }
            //checkbox kedua
            String hariKedua=intent.getStringExtra("harikedua");

            if (hariKedua.contains("Senin")){
                senin2.setChecked(true);
            } if (hariKedua.contains("Selasa")){
                selasa2.setChecked(true);
            } if (hariKedua.contains("Rabu")){
                rabu2.setChecked(true);
            } if (harikedua.contains("Kamis")){
                kamis2.setChecked(true);
            } if (hariKedua.contains("Jumat")){
                jumat2.setChecked(true);
            } if (hariKedua.contains("Sabtu")){
                sabtu2.setChecked(true);
            } if (hariKedua.contains("Minggu")){
                minggu2.setChecked(true);
            }
            //setjam praktek
            String jampraktek=intent.getStringExtra("jam");
            String jampraktekselesai=jampraktek.substring(jampraktek.indexOf("-")+2);
            String jampraktekmulai=jampraktek.substring(0,jampraktek.indexOf("-")-1);
            Log.d("hello", "fillDataFromIntent: "+jampraktekselesai);
            Log.d("hello", "fillDataFromIntent: "+jampraktekmulai);
            jamPraktik.setText(jampraktekmulai);
            jamPraktikSelesai.setText(jampraktekselesai);

            //set jam praktek kedua
            String jamprakter=intent.getStringExtra("jamkedua");
            String jampraktekselesaikedua=jamprakter.substring(jamprakter.indexOf("-")+2);
            String jampraktekmulaikedua=jamprakter.substring(0,jamprakter.indexOf("-")-1);
            Log.d("hello", "fillDataFromIntent: "+jampraktekselesai);
            Log.d("hello", "fillDataFromIntent: "+jampraktekmulai);
            jamPraktikKedua.setText(jampraktekmulaikedua);
            jamPraktikSelesaiKedua.setText(jampraktekselesaikedua);

            keterangan.setText(intent.getStringExtra("ket"));

        }
    }

    private void getPhotoDokter(){
        StorageReference storageReference= FirebaseStorage.getInstance().getReference("ProfilePicture/"+intent.getStringExtra("key")+".jpg");
        Log.d("detaildokter", "getPhotoDokter: "+storageReference);
        GlideApp.with(this).load(storageReference).into(profilePhoto);
    }

    private void updateToFirebase(){
        String harikedua=intent.getStringExtra("harikedua");
        if (TextUtils.isEmpty(harikedua)){
            updateDataOneTimePraktik();
        }
        else {
            updateDataTwoTimePraktik();
        }
    }

    private void updateDataOneTimePraktik(){
        DokterModel dokterModel=new DokterModel();
        String spinnerID;
        if (statusDokter.getSelectedItemPosition()==0)spinnerID="Praktik";
        else spinnerID="Cuti";
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

        String jam=jamPraktik.getText().toString();
        jam+=" - ";
        jam+=jamPraktikSelesai.getText().toString();

        dokterModel.setNamaDokter(namaDokter.getText().toString());
        dokterModel.setSpesialistik(spesialistikDokter.getText().toString());
        dokterModel.setHariPraktik(data);
        dokterModel.setJamPraktik(jam);
        dokterModel.setStatus(spinnerID);
        dokterModel.setKeterangan(keterangan.getText().toString());
        dokterModel.setIdDokter(intent.getStringExtra("id"));

        dbReference.setValue(dokterModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditDokterActivity.this, "Data Dokter Berhasil diUpdate", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditDokterActivity.this,InfoDokterActivity.class));
                finish();
            }
        });
deleteFoto();




        }




    private void updateDataTwoTimePraktik(){
        DokterModel dokterModel=new DokterModel();
        String spinnerID;
        if (statusDokter.getSelectedItemPosition()==0)spinnerID="Praktik";
        else spinnerID="Cuti";

        //hari pertama
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

        //jam pertama
        String jam=jamPraktik.getText().toString();
        jam+=" - ";
        jam+=jamPraktikSelesai.getText().toString();

        //harikedua
        String dataKedua="";
        if (senin2.isChecked()){
            dataKedua+="Senin";
        }
        if (selasa2.isChecked()){
            dataKedua+=" Selasa";
        }if (rabu2.isChecked()){
            dataKedua+=" Rabu";
        }if (kamis2.isChecked()){
            dataKedua+=" Kamis";
        }if (jumat2.isChecked()){
            dataKedua+=" Jumat";
        }if (sabtu2.isChecked()){
            dataKedua+=" Sabtu";
        }if (minggu2.isChecked()){
            dataKedua+=" Minggu";
        }

        //jam pertamaKedua
        String jamKedua=jamPraktik.getText().toString();
        jamKedua+=" - ";
        jamKedua+=jamPraktikSelesai.getText().toString();

        dokterModel.setNamaDokter(namaDokter.getText().toString());
        dokterModel.setSpesialistik(spesialistikDokter.getText().toString());
        dokterModel.setHariPraktik(data);
        dokterModel.setJamPraktik(jam);
        dokterModel.setHariPraktikKedua(dataKedua);
        dokterModel.setJamPraktikKedua(jamKedua);
        dokterModel.setStatus(spinnerID);
        dokterModel.setKeterangan(keterangan.getText().toString());
        dokterModel.setIdDokter(intent.getStringExtra("id"));

        dbReference.setValue(dokterModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditDokterActivity.this, "Data Dokter Berhasil diUpdate", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditDokterActivity.this,InfoDokterActivity.class));
                finish();
            }
        });
       deleteFoto();

        }

    private void deleteFoto(){
        if (filePath!=null) {
            final String id = intent.getStringExtra("key");
            FirebaseStorage storages = FirebaseStorage.getInstance();
            StorageReference storageReference = storages.getReference("ProfilePicture/"+id+".jpg");
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("editFoto", "onSuccess: Foto Berhasil dihapus");
                    uploadIntoFirebaseStorage(id);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("editFoto", "onFailure: gagal dihapus");

                }
            });
        }
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

    private void uploadIntoFirebaseStorage(String id){
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
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
    }
}