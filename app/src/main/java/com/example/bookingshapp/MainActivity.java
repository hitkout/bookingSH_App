package com.example.bookingshapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bookingshapp.Models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    RelativeLayout root;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        root = findViewById(R.id.rootElement);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        btnRegister.setOnClickListener(v -> showRegisterWindow());

        btnSignIn.setOnClickListener(v -> showSignInWindow());
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        pd = new ProgressDialog(this);
        pd.hide();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите все данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View signInWindow = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(signInWindow);

        TextInputEditText email = signInWindow.findViewById(R.id.inputEmailId);
        TextInputEditText pass = signInWindow.findViewById(R.id.inputPassId);

        dialog.setNegativeButton("Отменить", (dialogInterface, which) -> dialogInterface.dismiss());

        dialog.setPositiveButton("Добавить", (dialogInterface, which) -> {
            if (TextUtils.isEmpty(email.getText())){
                Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (Objects.requireNonNull(pass.getText()).length() < 5){
                Snackbar.make(root, "Введите пароль больше 5 символов", Snackbar.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(String.valueOf(email.getText()), pass.getText().toString()).addOnSuccessListener(authResult -> {
                FirebaseUser userSuccess = auth.getCurrentUser();
                updateUI(userSuccess);
            }).addOnFailureListener(e -> {
                Snackbar.make(root, "Ошибка авторизации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                updateUI(null);
            });
        });
        dialog.show();
    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегистрироваться");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.register_window, null);
        dialog.setView(registerWindow);

        TextInputEditText name = registerWindow.findViewById(R.id.inputNameId);
        TextInputEditText email = registerWindow.findViewById(R.id.inputEmailId);
        TextInputEditText pass = registerWindow.findViewById(R.id.inputPassId);

        dialog.setNegativeButton("Отменить", (dialogInterface, which) -> dialogInterface.dismiss());

        dialog.setPositiveButton("Зарегестрироваться и войти", (dialogInterface, which) -> {
            if (TextUtils.isEmpty(name.getText())){
                Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(email.getText())){
                Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (Objects.requireNonNull(pass.getText()).length() < 5){
                Snackbar.make(root, "Введите пароль больше 5 символов", Snackbar.LENGTH_SHORT).show();
                return;
            }

            //регистрация пользователя
            auth.createUserWithEmailAndPassword(String.valueOf(email.getText()), pass.getText().toString()).addOnSuccessListener(authResult -> {
                User user = new User();
                user.setName(String.valueOf(name.getText()));
                user.setEmail(String.valueOf(email.getText()));
                user.setPass(String.valueOf(pass.getText()));

                users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user).addOnSuccessListener(unused -> {
                    Snackbar.make(root, "Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();
                    FirebaseUser userSuccess = auth.getCurrentUser();
                    updateUI(userSuccess);
                });
            }).addOnFailureListener(e -> {
                Snackbar.make(root, "Ошибка регистрации! " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                updateUI(null);
            });
        });
        dialog.show();
    }
}