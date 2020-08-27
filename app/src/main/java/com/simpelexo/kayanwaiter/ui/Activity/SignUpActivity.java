package com.simpelexo.kayanwaiter.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.simpelexo.kayanwaiter.Model.User;
import com.simpelexo.kayanwaiter.R;
import com.simpelexo.kayanwaiter.Utiles.HelperMethod;

public class SignUpActivity extends AppCompatActivity {
        MaterialEditText edtPhone,edtName,edtPassword;
        Button btnSignUpRegister;
        Layout activity_sign_up_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignUpRegister = (Button) findViewById(R.id.btnSignUpRegister);
        //init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUpRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethod.showProgressDialog(SignUpActivity.this,"please waite...",false);
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //check if already user exist
                        if (snapshot.child(edtPhone.getText().toString()).exists()) {
                            HelperMethod.dismissProgressDialog();
                            Toast.makeText(SignUpActivity.this, "Phone Number already exists", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            HelperMethod.dismissProgressDialog();
                            User user = new User(edtName.getText().toString(),edtPassword.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUpActivity.this, "Sign Up success", Toast.LENGTH_SHORT).show();
                            finish();
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