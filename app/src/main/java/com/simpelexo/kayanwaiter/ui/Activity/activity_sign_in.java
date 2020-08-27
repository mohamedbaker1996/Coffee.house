package com.simpelexo.kayanwaiter.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.simpelexo.kayanwaiter.Model.User;
import com.simpelexo.kayanwaiter.R;
import com.simpelexo.kayanwaiter.Utiles.Common;
import com.simpelexo.kayanwaiter.Utiles.HelperMethod;

public class activity_sign_in extends AppCompatActivity {
     EditText edtPhone,edtPassword;
     Button btnSignInLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtPhone=(MaterialEditText) findViewById(R.id.edtPhone);
        edtPassword=(MaterialEditText) findViewById(R.id.edtPassword);
        btnSignInLogin=(Button) findViewById(R.id.btnSignInLogin);
        //init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        final DatabaseReference table_user = database.getReference("User");

        btnSignInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethod.showProgressDialog(activity_sign_in.this,"Please Wait ....",false);
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //check if user exists
                        if (snapshot.child(edtPhone.getText().toString()).exists()) {



                        //get user info
                        HelperMethod.dismissProgressDialog();
                        User user = snapshot.child(edtPhone.getText().toString()).getValue(User.class);
                        //set Phone to get user phone on communicating
                            user.setPhone(edtPhone.getText().toString());

                        if (user.getPassword().equals(edtPassword.getText().toString())) {
                            Toast.makeText(activity_sign_in.this, "sign in success", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(activity_sign_in.this,Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();
                        }
                        else {
                            Toast.makeText(activity_sign_in.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                        else {
                            HelperMethod.dismissProgressDialog();
                            Toast.makeText(activity_sign_in.this, "مستخدم غير موجود بقاعده البيانات", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}