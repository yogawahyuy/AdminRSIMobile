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
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String txtEmail=email.getText().toString();
//                String txtPassword=password.getText().toString();
//
//                if (TextUtils.isEmpty(txtEmail)||TextUtils.isEmpty(txtPassword))
//                    Toast.makeText(LoginActivity.this, "Email dan Password Harus diisi", Toast.LENGTH_SHORT).show();
//                else{
//                    dialogView.showDialog();
//                    mAuth.signInWithEmailAndPassword(txtEmail,txtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()){
//                                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                dialogView.hideDialog();
//                                finish();
//                            }else{
//                                Toast.makeText(LoginActivity.this, "Email atau Password Salah", Toast.LENGTH_SHORT).show();
//                                dialogView.hideDialog();
//                            }
//                        }
//                    });
//                }
//            }
//        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
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
        //reference= FirebaseDatabase.getInstance().getReference("UserAdmin").child(email.getText().toString());
        reference= FirebaseDatabase.getInstance().getReference("UserAdmin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoginModel loginModel=new LoginModel();
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    loginModel=dataSnapshot.getValue(LoginModel.class);
                    if (loginModel.getUsername().equalsIgnoreCase(email.getText().toString())&&loginModel.getPassword().equalsIgnoreCase(password.getText().toString())){
                        if (loginModel.getBagian().equalsIgnoreCase("bidyan")){
                            Log.d("data", "onDataChange: bidyan");
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

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}