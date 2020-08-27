package com.rsip.adminrsimoblie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rsip.adminrsimoblie.RecyclerView.ListKeluhanActivity;
import com.rsip.adminrsimoblie.View.InfoDokterActivity;
import com.rsip.adminrsimoblie.View.LoginActivity;

public class MainActivity extends AppCompatActivity {
    Button logout;
    FirebaseUser firebaseUser;
    CardView cardViewDokter,cardViewKeluhan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        logout=findViewById(R.id.btnlogout);
        cardViewDokter=findViewById(R.id.cardview_dokter);
        cardViewKeluhan=findViewById(R.id.cardview_keluhan);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser!=null){
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(), "Anda Berhasil Logout", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        });
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
    }
}