package com.example.bookingshapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.bookingshapp.Models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference users;
    private RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnRegister = findViewById(R.id.btnRegister);
        ImageView imageViewQuestion = findViewById(R.id.imageViewQuestion);
        imageViewQuestion.setOnClickListener(v -> showQuestion());
        root = findViewById(R.id.rootElementMain);
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
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
        if (user != null && Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
            finish();
        }
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

        dialog.setNeutralButton("Забыли пароль?", (dialog1, which) -> showProblemWindow());

        dialog.setNegativeButton("Отменить", (dialogInterface, which) -> dialogInterface.dismiss());

        dialog.setPositiveButton("Войти", (dialogInterface, which) -> {
            if (TextUtils.isEmpty(email.getText())){
                Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (Objects.requireNonNull(pass.getText()).length() < 5){
                Snackbar.make(root, "Введите пароль больше 5 символов", Snackbar.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(String.valueOf(email.getText()), pass.getText().toString()).addOnSuccessListener(authResult -> {
                if (Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()){
                    FirebaseUser userSuccess = auth.getCurrentUser();
                    updateUI(userSuccess);
                }
                else
                    Toast.makeText(MainActivity.this, "Вы не подтвердили аккаунт!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Snackbar.make(root, "Ошибка авторизации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                updateUI(null);
            });
        });

        dialog.show();
    }

    private void showQuestion() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setMessage("Справка");

        LayoutInflater inflater = LayoutInflater.from(this);
        View questionInWindow = inflater.inflate(R.layout.question_window, null);
        dialog.setView(questionInWindow);

        dialog.setNegativeButton("Закрыть", (dialogInterface, which) -> dialogInterface.dismiss());

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
        dialog.setPositiveButton("Зарегестрироваться", (dialogInterface, which) -> {
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
            Snackbar.make(root, "Письмо отправляется...", Snackbar.LENGTH_SHORT).show();
            auth.createUserWithEmailAndPassword(String.valueOf(email.getText()), pass.getText().toString()).addOnSuccessListener(authResult -> {
                User user = new User();
                user.setName(String.valueOf(name.getText()));
                user.setEmail(String.valueOf(email.getText()));
                user.setId(String.valueOf(UUID.randomUUID()));

                users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user).addOnSuccessListener(unused -> Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Snackbar.make(root, "Пользователь добавлен! Пожалуйста, подтвердите аккаунт в письме", Snackbar.LENGTH_SHORT).show();
                    }else {
                        Snackbar.make(root, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Snackbar.LENGTH_SHORT).show();
                    }
                }));
            }).addOnFailureListener(e -> Snackbar.make(root, "Ошибка регистрации! " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
        });
        dialog.show();
    }

    private void showProblemWindow(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Забыли пароль?");
        dialog.setMessage("Введите почту");

        LayoutInflater inflater = LayoutInflater.from(this);
        View problemWindow = inflater.inflate(R.layout.problem_window, null);
        dialog.setView(problemWindow);

        TextInputEditText emailInProblemWindow = problemWindow.findViewById(R.id.emailInProblemWindow);
        dialog.setPositiveButton("Сбросить пароль", (dialog1, which) -> {
            if (TextUtils.isEmpty(emailInProblemWindow.getText())){
                Snackbar.make(root, "Вы ничего не ввели", Snackbar.LENGTH_SHORT).show();
                return;
            }
            auth.sendPasswordResetEmail(Objects.requireNonNull(emailInProblemWindow.getText()).toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Snackbar.make(root, "Письмо с изменением пароля было отправлено", Snackbar.LENGTH_SHORT).show();
                }else {
                    Snackbar.make(root, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Snackbar.LENGTH_SHORT).show();
                }
            });
        });
        dialog.setNegativeButton("Отменить", (dialogInterface, which) -> dialogInterface.dismiss());
        dialog.show();
    }
}