package com.rsip.adminrsimoblie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rsip.adminrsimoblie.RecyclerView.DataAmbulanceKeluarActivity;
import com.rsip.adminrsimoblie.RecyclerView.DataMobilAmbulanceActivity;
import com.rsip.adminrsimoblie.RecyclerView.DataPekerjaanIp2Activity;
import com.rsip.adminrsimoblie.RecyclerView.ListKeluhanActivity;
import com.rsip.adminrsimoblie.Util.SharedPreferenceManager;
import com.rsip.adminrsimoblie.View.InfoDokterActivity;
import com.rsip.adminrsimoblie.View.LoginActivity;

public class MainActivity extends AppCompatActivity {
    Button logout;
    FirebaseUser firebaseUser;
    LinearLayout linDokter,linSupir,linIp2;
    CardView cardViewDokter,cardViewKeluhan,cardViewAmbulance,cardViewAmbulanceKeluar,cardViewIp2;
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (firebaseUser!=null){
//                    FirebaseAuth.getInstance().signOut();
//                    Toast.makeText(getApplicationContext(), "Anda Berhasil Logout", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                }
                sharedPreferenceManager.saveSPBoolean(SharedPreferenceManager.SP_SUDAH_LOGIN,false);
                startActivity(new Intent(MainActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
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

        cardViewDokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InfoDokterActivity.class));
            }
        });
        cardViewKeluhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListKeluhanActivity.class));
            }
        });
        cardViewAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DataMobilAmbulanceActivity.class));
            }
        });
        cardViewAmbulanceKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DataAmbulanceKeluarActivity.class));
            }
        });

        cardViewIp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DataPekerjaanIp2Activity.class));
            }
        });

    }
}