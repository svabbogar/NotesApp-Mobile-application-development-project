package com.example.jegyzetek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerEmailET, registerPassword1ET, registerPassword2ET;
    private Button registerBTN;
    private Button backToLoginBTN;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        registerEmailET = findViewById(R.id.emailRegisterEditText);
        registerPassword1ET = findViewById(R.id.passwordRegisterEditText);
        registerPassword2ET = findViewById(R.id.passwordRegisterEditText2);
        registerBTN = findViewById(R.id.registerButtonSend);
        backToLoginBTN = findViewById(R.id.backToLoginButton);

        firebaseAuth = FirebaseAuth.getInstance();


        backToLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = registerEmailET.getText().toString().trim();
                String password1 = registerPassword1ET.getText().toString().trim();
                String password2 = registerPassword2ET.getText().toString().trim();

                if (email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Az ??sszes mez??t ki kell t??ltened!", Toast.LENGTH_SHORT).show();
                } else if (!password1.equals(password2)) {
                    Toast.makeText(RegisterActivity.this, "A jelszavaknak egyezni??k kell!", Toast.LENGTH_SHORT).show();
                } else if (password1.length() < 7) {
                    Toast.makeText(RegisterActivity.this, "Legal??bb 7 karakter hossz?? jelsz??t v??lassz!", Toast.LENGTH_SHORT).show();
                } else {

                    firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Sikeres regisztr??ci??", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Sikertelen regisztr??ci??", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    // send email verification
    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(RegisterActivity.this, "Meger??s??t?? email elk??ldve! Er??s??tsd meg ??s l??pj be!", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, "Nem siker??lt meger??s??t?? emailt k??ldeni", Toast.LENGTH_SHORT).show();
        }
    }


}