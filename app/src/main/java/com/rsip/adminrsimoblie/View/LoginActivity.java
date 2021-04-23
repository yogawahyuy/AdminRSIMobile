package com.rsip.adminrsimoblie.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rsip.adminrsimoblie.MainActivity;
import com.rsip.adminrsimoblie.R;
import com.rsip.adminrsimoblie.Util.SharedPreferenceManager;

import naseem.ali.flexibletoast.EasyToast;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email,password;
    Button btnLogin;
    CustomDialogView dialogView;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    SharedPreferenceManager sharedPreferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btnLogin=findViewById(R.id.btnlogin);
        dialogView=new CustomDialogView(this);
        sharedPreferenceManager=new SharedPreferenceManager(this);
        btnLogin.setOnClickListener(v -> login());
        btnLogin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                return false;
            }
        });
        if (sharedPreferenceManager.getSPSudahLogin()){
            startActivity(new Intent(LoginActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login(){
        if(TextUtils.isEmpty(email.getText())||TextUtils.isEmpty(password.getText())){
            EasyToast.makeText(LoginActivity.this,"Harap Isi Semua Kolom !!!",EasyToast.LENGTH_SHORT,EasyToast.WARNING,false).show();
        }
        //reference= FirebaseDatabase.getInstance().getReference("UserAdmin").child(email.getText().toString());
        reference= FirebaseDatabase.getInstance().getReference("UserAdmin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                   LoginModel loginModel=dataSnapshot.getValue(LoginModel.class);
                    if (loginModel.getUsername().equalsIgnoreCase(email.getText().toString())&&loginModel.getPassword().equalsIgnoreCase(password.getText().toString())){
                        Log.d("datalogin", "onDataChange: "+loginModel.getBagian());
                        if (loginModel.getBagian().equalsIgnoreCase("bidyan")){
                            Log.d("datalogin", "onDataChange: bidyan");
                            intent.putExtra("username",loginModel.getUsername());
                            intent.putExtra("bagian",loginModel.getBagian());
                            intent.putExtra("nama",loginModel.getNamaLengkap());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_USERNAME,loginModel.getUsername());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_BAGIAN,loginModel.getBagian());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_NAMA,loginModel.getNamaLengkap());
                            sharedPreferenceManager.saveSPBoolean(sharedPreferenceManager.SP_SUDAH_LOGIN,true);
                            startActivity(intent);
                            finish();
                        }if (loginModel.getBagian().equalsIgnoreCase("supir")){
                            intent.putExtra("username",loginModel.getUsername());
                            intent.putExtra("bagian",loginModel.getBagian());
                            intent.putExtra("nama",loginModel.getNamaLengkap());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_USERNAME,loginModel.getUsername());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_BAGIAN,loginModel.getBagian());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_NAMA,loginModel.getNamaLengkap());
                            sharedPreferenceManager.saveSPBoolean(sharedPreferenceManager.SP_SUDAH_LOGIN,true);
                            startActivity(intent);
                            finish();
                            Log.d("data", "onDataChange: supir");
                        }if (loginModel.getBagian().equalsIgnoreCase("ip2")){
                            Log.d("data", "onDataChange: ip2");
                            intent.putExtra("username",loginModel.getUsername());
                            intent.putExtra("bagian",loginModel.getBagian());
                            intent.putExtra("nama",loginModel.getNamaLengkap());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_USERNAME,loginModel.getUsername());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_BAGIAN,loginModel.getBagian());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_NAMA,loginModel.getNamaLengkap());
                            sharedPreferenceManager.saveSPBoolean(sharedPreferenceManager.SP_SUDAH_LOGIN,true);
                            startActivity(intent);
                            finish();
                        }
                        if (loginModel.getBagian().equalsIgnoreCase("asd")){
                            intent.putExtra("username",loginModel.getUsername());
                            intent.putExtra("bagian",loginModel.getBagian());
                            intent.putExtra("nama",loginModel.getNamaLengkap());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_USERNAME,loginModel.getUsername());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_BAGIAN,loginModel.getBagian());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_NAMA,loginModel.getNamaLengkap());
                            sharedPreferenceManager.saveSPBoolean(sharedPreferenceManager.SP_SUDAH_LOGIN,true);
                            startActivity(intent);
                            finish();
                        }
                        if (loginModel.getBagian().equalsIgnoreCase("super")){
                            intent.putExtra("username",loginModel.getUsername());
                            intent.putExtra("bagian",loginModel.getBagian());
                            intent.putExtra("nama",loginModel.getNamaLengkap());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_USERNAME,loginModel.getUsername());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_BAGIAN,loginModel.getBagian());
                            sharedPreferenceManager.saveSPString(sharedPreferenceManager.SP_NAMA,loginModel.getNamaLengkap());
                            sharedPreferenceManager.saveSPBoolean(sharedPreferenceManager.SP_SUDAH_LOGIN,true);
                            startActivity(intent);
                            finish();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}