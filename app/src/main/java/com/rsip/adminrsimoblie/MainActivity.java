package com.rsip.adminrsimoblie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import com.github.opendevl.JFlat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rsip.adminrsimoblie.ConvertModule.CSVWriter;
import com.rsip.adminrsimoblie.ConvertModule.JSONFlattener;
import com.rsip.adminrsimoblie.RecyclerView.DataAmbulanceKeluarActivity;
import com.rsip.adminrsimoblie.RecyclerView.DataMobilAmbulanceActivity;
import com.rsip.adminrsimoblie.RecyclerView.DataPekerjaanIp2Activity;
import com.rsip.adminrsimoblie.RecyclerView.DataPekerjaanSelesaiActivity;
import com.rsip.adminrsimoblie.RecyclerView.ListKeluhanActivity;
import com.rsip.adminrsimoblie.Util.SharedPreferenceManager;
import com.rsip.adminrsimoblie.View.InfoDokterActivity;
import com.rsip.adminrsimoblie.View.LandingLiatKeluhanActivity;
import com.rsip.adminrsimoblie.View.LoginActivity;

import org.json.JSONObject;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button logout;
    FirebaseUser firebaseUser;
    LinearLayout linDokter,linSupir,linIp2;
    CardView cardViewDokter,cardViewKeluhan,cardViewAmbulance,cardViewAmbulanceKeluar,cardViewIp2,cardViewSelesaiIp2;
    Intent intent;
    SharedPreferenceManager sharedPreferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       findViews();
    }

    private void findViews(){
        intent=getIntent();
        sharedPreferenceManager=new SharedPreferenceManager(this);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        logout=findViewById(R.id.btnlogout);
        linDokter=findViewById(R.id.linDokter);
        linSupir=findViewById(R.id.linSupir);
        linIp2=findViewById(R.id.linIp2);
        cardViewDokter=findViewById(R.id.cardview_dokter);
        cardViewKeluhan=findViewById(R.id.cardview_keluhan);
        cardViewAmbulance=findViewById(R.id.cardview_ambulance);
        cardViewAmbulanceKeluar=findViewById(R.id.cardview_mobilkeluar);
        cardViewIp2=findViewById(R.id.cardview_KerjaIp2);
        cardViewSelesaiIp2=findViewById(R.id.cardview_KerjaSelesaiIp2);
        logout.setOnClickListener(v -> {
            sharedPreferenceManager.saveSPBoolean(SharedPreferenceManager.SP_SUDAH_LOGIN,false);
            startActivity(new Intent(MainActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });

//        if (intent.getStringExtra("bagian").equalsIgnoreCase("bidyan")||sharedPreferenceManager.getSPBagian().equalsIgnoreCase("bidyan")){
//            linDokter.setVisibility(View.VISIBLE);
//            linSupir.setVisibility(View.GONE);
//            linIp2.setVisibility(View.GONE);
//        }
//        if (intent.getStringExtra("bagian").equalsIgnoreCase("supir")){
//            linDokter.setVisibility(View.GONE);
//            linSupir.setVisibility(View.VISIBLE);
//            linIp2.setVisibility(View.GONE);
//
//        }if (intent.getStringExtra("bagian").equalsIgnoreCase("ip2")){
//            linDokter.setVisibility(View.GONE);
//            linSupir.setVisibility(View.GONE);
//            linIp2.setVisibility(View.VISIBLE);
//
//        }
        if (sharedPreferenceManager.getSPBagian().equalsIgnoreCase("bidyan")){
            linDokter.setVisibility(View.VISIBLE);
            linSupir.setVisibility(View.GONE);
            linIp2.setVisibility(View.GONE);
        }
        if (sharedPreferenceManager.getSPBagian().equalsIgnoreCase("supir")){
            linDokter.setVisibility(View.GONE);
            linSupir.setVisibility(View.VISIBLE);
            linIp2.setVisibility(View.GONE);

        }if (sharedPreferenceManager.getSPBagian().equalsIgnoreCase("ip2")){
            linDokter.setVisibility(View.GONE);
            linSupir.setVisibility(View.GONE);
            linIp2.setVisibility(View.VISIBLE);

        }
        if (sharedPreferenceManager.getSPBagian().equalsIgnoreCase("super")){
            linIp2.setVisibility(View.VISIBLE);
            linSupir.setVisibility(View.VISIBLE);
            linDokter.setVisibility(View.VISIBLE);
        }
        if (sharedPreferenceManager.getSPBagian().equalsIgnoreCase("asd")){
            linIp2.setVisibility(View.VISIBLE);
            linSupir.setVisibility(View.VISIBLE);
            linDokter.setVisibility(View.VISIBLE);
            cardViewKeluhan.setVisibility(View.GONE);
            cardViewAmbulance.setVisibility(View.GONE);
            cardViewIp2.setVisibility(View.GONE);
        }

        cardViewDokter.setOnClickListener(v -> //startActivity(new Intent(MainActivity.this, InfoDokterActivity.class))
                cobaConvertv1()
                );
        cardViewKeluhan.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LandingLiatKeluhanActivity.class)));
        cardViewAmbulance.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DataMobilAmbulanceActivity.class)));
        cardViewAmbulanceKeluar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DataAmbulanceKeluarActivity.class)));

        cardViewIp2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DataPekerjaanIp2Activity.class)));

        cardViewSelesaiIp2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DataPekerjaanSelesaiActivity.class)));

    }

    private void cobaConvert(){
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, "http://103.255.241.124:5758/android_json_panduan/adminrsi/coba.json", null, (Response.Listener<JSONObject>) response -> {
            if (response.length() > 0) {

//                File dir = new File(getFilesDir(), "AdminRSIMobile");
//                if (!dir.exists()) {
//                    dir.mkdir();
//                }
//                FileOutputStream outputStream;
//                try {
////                    File file=new File(dir,"exportexcel.csv");
////                    FileWriter writer=new FileWriter(file);
////                    writer.append()
////                    outputStream=openFileOutput("exportexcel.csv", Context.MODE_PRIVATE);
////                    outputStream.write();
//                  //  JFlat flat = new JFlat(response.toString());
//                  //  flat.json2Sheet().headerSeparator("_").getJsonAsSheet();
//                    Log.d("write csv", "cobaConvert: "+dir.toString());
//                  //  flat.write2csv(dir.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void cobaConvertv1(){
        try {
            String filePath=this.getFilesDir().getPath()+"/AdminRSI-COBA.csv";
            File file=new File(filePath);
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            //OutputStreamWriter
            URI urlDestination=URI.create("http://103.255.241.124:5758/android_json_panduan/adminrsi/coba.json");
            List<Map<String, String>> flatJson= JSONFlattener.parseJson(new File(String.valueOf(getAssets().open("coba.json"))));

            Log.d("isi flat json", "cobaConvertv1: "+flatJson);
            //List<Map<String, String>> flatJson= JSONFlattener.parseJson(new URI("http://103.255.241.124:5758/android_json_panduan/adminrsi/coba.json"));
//            Set<String> header=CSVWriter.collectOrderedHeaders(flatJson);
           // CSVWriter.writeToFile(CSVWriter.getCSV(flatJson,"\t"),filePath);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                CSVWriter.writeLargeFile(flatJson,";",filePath,header);
//            }else{
//                Toast.makeText(this, "HP ANDA TIDAK SUPPORT!", Toast.LENGTH_SHORT).show();
//            }


        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

}
