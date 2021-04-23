package com.rsip.adminrsimoblie.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.rsip.adminrsimoblie.MainActivity;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.RecyclerView.DataAmbulanceKeluarActivity;
import com.rsip.adminrsimoblie.RecyclerView.DataMobilAmbulanceActivity;
import com.rsip.adminrsimoblie.RecyclerView.DataMobilAmbulanceModel;
import com.rsip.adminrsimoblie.Util.SharedPreferenceManager;
//import com.rtchagas.pingplacepicker.PingPlacePicker;
//import com.sucho.placepicker.PlacePicker;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import naseem.ali.flexibletoast.EasyToast;

public class TambahMobilKeluarActivity extends AppCompatActivity {

    SearchableSpinner spinnerPlat;
    EditText jenisKendaraan, merkKendaraan, tujuan, jarak, tanggal, jam, supir;
    Button btnSimpan;
    private ArrayList<DataMobilAmbulanceModel> modelList = new ArrayList<>();
    SharedPreferenceManager sharedPreferenceManager;
    String waktuJam, waktuTanggal;
    int posisi;
    final List<String> titleList = new ArrayList<String>();
    LatLng latLangTo;

    private ProgressDialog progressDialog;

    private int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_mobil_keluar);
        findView();
    }

    private void findView() {
        sharedPreferenceManager = new SharedPreferenceManager(this);
        spinnerPlat = findViewById(R.id.spinner_plat);
        jenisKendaraan = findViewById(R.id.edttxt_jenis_kendaraan);
        jenisKendaraan.setInputType(InputType.TYPE_NULL);
        merkKendaraan = findViewById(R.id.edtTxt_merk);
        merkKendaraan.setInputType(InputType.TYPE_NULL);
        tujuan = findViewById(R.id.edtTxt_tujuaan);
        jarak = findViewById(R.id.edtTxt_jarak);
        tanggal = findViewById(R.id.edtTxt_tanggal);
        tanggal.setInputType(InputType.TYPE_NULL);
        jam = findViewById(R.id.edtTxt_jam);
        jam.setInputType(InputType.TYPE_NULL);
        supir = findViewById(R.id.edtTxt_supir);
        supir.setText(sharedPreferenceManager.getSPNama());
        supir.setInputType(InputType.TYPE_NULL);
        btnSimpan = findViewById(R.id.btn_simpan);
        populteToSpinner();
        fillEditText();
        getTimeNow();
        getDateNow();


        tujuan.setOnClickListener(v -> {
            //newPlacePicker();
            //showPlacePicker();
        });
        tanggal.setOnClickListener(v -> showDatePicker());
        jam.setOnClickListener(v -> showTimePicker());
        btnSimpan.setOnClickListener(v -> {
            progresDialog();
            getDateNowAutomatoc();
            getTimeNowAutomatic();
            addIntoFirebase();
        });
    }

    private void fillEditText() {
        spinnerPlat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posisi = position;
                DataMobilAmbulanceModel dataMobilAmbulanceModel = modelList.get(position);
                if (dataMobilAmbulanceModel.getJenisKendaraan().equalsIgnoreCase("0")){
                    jenisKendaraan.setText("Ambulance");
                }else{
                    jenisKendaraan.setText("Mobil Keluar");
                }
                //jenisKendaraan.setText(dataMobilAmbulanceModel.getJenisKendaraan());
                merkKendaraan.setText(dataMobilAmbulanceModel.getMerekKendaraan());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void populteToSpinner() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Driver").child("DataMobil");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataMobilAmbulanceModel dataMobilAmbulanceModel = dataSnapshot.getValue(DataMobilAmbulanceModel.class);
                    dataMobilAmbulanceModel.setKey(dataSnapshot.getKey());

                    if (dataMobilAmbulanceModel.getStatus().equalsIgnoreCase("ready")) {
                        modelList.add(dataMobilAmbulanceModel);
                        titleList.add(dataMobilAmbulanceModel.getNoPlat());
                        Log.d("data", "onDataChange: keluar" + titleList.size());
                    } else {
                        startActivity(new Intent(TambahMobilKeluarActivity.this,DataAmbulanceKeluarActivity.class));
                        finish();
                        EasyToast.makeText(TambahMobilKeluarActivity.this,"Tidak Bisa menambahkan \nMobil Sedang Keluar Semua !",EasyToast.LENGTH_LONG,EasyToast.ERROR,false).show();
                        Log.d("data", "onDataChange: masuk");
                    }
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TambahMobilKeluarActivity.this, android.R.layout.simple_spinner_item, titleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPlat.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDateNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date today = Calendar.getInstance().getTime();
        String todayDate = dateFormat.format(today);
        Log.d("bookingdate", "getDateToTanggalPeriksa: " + todayDate);
        tanggal.setText(todayDate);
    }

    private void getDateNowAutomatoc() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date today = Calendar.getInstance().getTime();
        String waktu = dateFormat.format(today);
        waktuTanggal = waktu;
    }

    private void getTimeNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date timeNow = Calendar.getInstance().getTime();
        String time = dateFormat.format(timeNow);
        Log.d("timeBoking", "getTimeNow: " + time);
        jam.setText(time);
    }

    private void getTimeNowAutomatic() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date timeNow = Calendar.getInstance().getTime();
        String waktu = dateFormat.format(timeNow);
        waktuJam = waktu;
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tanggal.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();

    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                jam.setText(hourOfDay + ":" + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void newPlacePicker() {
        Places.initialize(this, "AIzaSyCPzqOBuv1DYkx6tsxmv0QaXWqX6fAfE5Q");
        PlacesClient placesClient = Places.createClient(this);

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }
//    private void showPlacePicker(){
//        PingPlacePicker.IntentBuilder builder=new PingPlacePicker.IntentBuilder();
//        builder.setMapsApiKey("AIzaSyCPzqOBuv1DYkx6tsxmv0QaXWqX6fAfE5Q").setAndroidApiKey("AIzaSyCPzqOBuv1DYkx6tsxmv0QaXWqX6fAfE5Q");
//        try {
//            Intent placeIntent = builder.build(this);
//            startActivityForResult(placeIntent, PLACE_PICKER_REQUEST);
//        }
//        catch (Exception ex) {
//            Toast.makeText(this, "Google Play services is not available... ", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode==PLACE_PICKER_REQUEST){
//            if (resultCode==RESULT_OK){
//                Place place=PingPlacePicker.getPlace(data);
//                Log.d("data", "onActivityResult: "+place.getAddress()+","+place.getName());
//                tujuan.setText(place.getAddress());
//                if (place!=null){
//                    Toast.makeText(this, "jadi", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//        if ((requestCode==PLACE_PICKER_REQUEST)&&(resultCode==RESULT_OK)){
//            Place place=PingPlacePicker.getPlace(data);
//            Toast.makeText(this, "jadi "+place.getAddress(), Toast.LENGTH_SHORT).show();
//        }else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Toast.makeText(this, place.getName(), Toast.LENGTH_SHORT).show();
                tujuan.setText(place.getName());
                latLangTo=place.getLatLng();
                Log.d("data", "onActivityResult: " + place.getAddress() + " " + place.getName() + " " + place.getLatLng());
                distanceBetwen();

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {

            }
            return;
        }

       super.onActivityResult(requestCode, resultCode, data);
    }

    private void distanceBetwen() {
        double lat=-7.418430;
        double lang=109.212655;
        LatLng latLngFrom=new LatLng(lat,lang);
        double jaraknya=SphericalUtil.computeDistanceBetween(latLngFrom,latLangTo)/1000;
        int conversiJarak=(int) jaraknya;
        Log.d("data", "distanceBetwen: "+conversiJarak);

        jarak.setText(conversiJarak+" KM");
    }

    private void addIntoFirebase(){
        if (TextUtils.isEmpty(tujuan.getText().toString())||TextUtils.isEmpty(jarak.getText().toString())){
            Toast.makeText(this, "Kolom Harus Diisi", Toast.LENGTH_SHORT).show();
        }else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("NoPlat", spinnerPlat.getSelectedItem().toString());
            hashMap.put("jenisKendaraan", jenisKendaraan.getText().toString());
            hashMap.put("merkKendaraan", merkKendaraan.getText().toString());
            hashMap.put("tujuan", tujuan.getText().toString());
            hashMap.put("jarak", jarak.getText().toString());
            hashMap.put("tanggalInput",tanggal.getText().toString());
            hashMap.put("jamInput",jam.getText().toString());
            hashMap.put("driver",supir.getText().toString());
            hashMap.put("tanggalOtomatis",waktuTanggal);
            hashMap.put("jamOtomatis",waktuJam);
            hashMap.put("status","Keluar");

            String key = reference.child("Driver").child("MobilKeluar").push().getKey();

            reference.child("Driver").child("MobilKeluar").child(key).setValue(hashMap);


            String keys=modelList.get(posisi).getKey();
            String noplat=modelList.get(posisi).getNoPlat();
            String jenisKendaraan=modelList.get(posisi).getJenisKendaraan();
            String merekKendaraan=modelList.get(posisi).getMerekKendaraan();
            String pajakKendaraan=modelList.get(posisi).getPajakKendaraan();
            String pajakPlatKendaraan=modelList.get(posisi).getPajakPlatKendaraan();
            String typeKendaraan=modelList.get(posisi).getTypeKendaraan();
            String status=modelList.get(posisi).getStatus();
            DatabaseReference mRefrence=FirebaseDatabase.getInstance().getReference("Driver").child("DataMobil").child(noplat);
            DataMobilAmbulanceModel dataMobilAmbulanceModel=new DataMobilAmbulanceModel();
            dataMobilAmbulanceModel.setNoPlat(noplat);
            dataMobilAmbulanceModel.setJenisKendaraan(jenisKendaraan);
            dataMobilAmbulanceModel.setPajakKendaraan(pajakKendaraan);
            dataMobilAmbulanceModel.setPajakPlatKendaraan(pajakPlatKendaraan);
            dataMobilAmbulanceModel.setMerekKendaraan(merekKendaraan);
            dataMobilAmbulanceModel.setTypeKendaraan(typeKendaraan);
            dataMobilAmbulanceModel.setStatus("Keluar");
            mRefrence.setValue(dataMobilAmbulanceModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(TambahMobilKeluarActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TambahMobilKeluarActivity.this, DataAmbulanceKeluarActivity.class));
                    progressDialog.dismiss();
                    finish();
                }
            });


        }
    }

    private void progresDialog(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Menambahkan Data");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}