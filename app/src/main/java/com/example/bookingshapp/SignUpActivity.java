package com.example.bookingshapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private ConstraintLayout constraint;
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private TextView textViewDate;
    private TextView textViewInfoInSignUpPage;
    private List<String> user;
    private ListView usersInHall;
    private Button btnSignUp;
    private Button btnUnsubscribe;
    private TextView textViewInfo;

    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = rootRef.child("Users").child(uid);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        getChildrenFromDB();
        getChildrenCountFromDB();
        getIntentMain();
        btnClick();
    }

    private void init(){
        constraint = findViewById(R.id.constraintElementSignUp);
        textViewDate = findViewById(R.id.textViewDate);
        textViewInfoInSignUpPage = findViewById(R.id.textViewInfoInSignUpPage);
        user = new ArrayList<>();
        usersInHall = findViewById(R.id.usersInHall);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnUnsubscribe = findViewById(R.id.btnUnsubscribe);
        textViewInfo = findViewById(R.id.textViewInfo);
        ImageView imageViewQuestion = findViewById(R.id.imageViewQuestion);
        imageViewQuestion.setOnClickListener(v -> showQuestion());
    }

    @SuppressLint("SetTextI18n")
    private void getIntentMain(){
        Intent intent = getIntent();
        textViewDate.setText(intent.getStringExtra("getDateFromList")+" / "+intent.getStringExtra("getTimeFromList")+" / "+intent.getStringExtra("getOfficeFromList"));
        switch (intent.getStringExtra("getOfficeFromList")){
            case "Кардиозона":
                textViewInfo.setText("Соотношение беговых дорожек, велотренажеров, АМТ, эллиптических и гребных тренажеров должно определяться демографией ваших клиентов.");
                break;
            case "Силовая зона":
                textViewInfo.setText("13% клиентов уходят, а 18% не становятся членами клуба, потому что чувствуют себя «не в своей тарелке», поэтому пространство должно располагать к тренировке, а оборудование должно быть простым в использовании.");
                break;
            case "Зона свободных весов":
                textViewInfo.setText("Тренажеры для работы со свободными весами должны стоять спиной к блочному оборудованию. Это мотивирует женщин больше использовать блочные тренажеры.");
                break;
            case "Зона растяжки":
                textViewInfo.setText("Выделенная зона для растяжки поможет создать ощущение спокойствия перед занятием или после тяжелой тренировки.");
                break;
            case "Зона групповых программ":
                textViewInfo.setText("Залы групповых программ пользуются наибольшей популярностью среди клиентов клуба, особенно среди женщин. Персональные, групповые и функциональные тренировки входят в ТОП 10 Фитнес-трендов.");
                break;
            case "Бассейн 1":
            case "Бассейн 2":
                textViewInfo.setText("С помощью регулярного посещения бассейна можно повысить выносливость организма. Также водные упражнения способствуют нормализации давления и улучшению кровообращения. После плавания нет болевых ощущений, как после бега или прыжков.");
                break;
            default:
                textViewInfo.setText("");
                break;
        }
    }

    private void getChildrenCountFromDB(){
        mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch (getIntent().getStringExtra("getOfficeFromList")){
                    case "Кардиозона":
                        textViewInfoInSignUpPage.setText(getIntent().getStringExtra("getOfficeFromList") + ": осталось " + (12 - snapshot.getChildrenCount()) + " мест!");
                        break;
                    case "Силовая зона":
                        textViewInfoInSignUpPage.setText(getIntent().getStringExtra("getOfficeFromList") + ": осталось " + (8 - snapshot.getChildrenCount()) + " мест!");
                        break;
                        case "Зона свободных весов":
                        textViewInfoInSignUpPage.setText(getIntent().getStringExtra("getOfficeFromList") + ": осталось " + (10 - snapshot.getChildrenCount()) + " мест!");
                        break;
                    case "Зона растяжки":
                    case "Зона групповых программ":
                        textViewInfoInSignUpPage.setText(getIntent().getStringExtra("getOfficeFromList") + ": осталось " + (14 - snapshot.getChildrenCount()) + " мест!");
                        break;
                    case "Бассейн 1":
                    case "Бассейн 2":
                        textViewInfoInSignUpPage.setText(getIntent().getStringExtra("getOfficeFromList") + ": осталось " + (20 - snapshot.getChildrenCount()) + " мест!");
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChildrenFromDB(){
        mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getChildren(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChildren(DataSnapshot snapshot){
        List<String> listUsersInDB = new ArrayList<>();
        for (DataSnapshot dss : snapshot.getChildren()){
            String values = String.valueOf(dss.getValue());
            values = values.substring(1, values.length() - 1);
            values = values.replace("=", ": ");
            listUsersInDB.add(values);
        }
        usersInHall.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listUsersInDB));
    }

    private void btnClick(){
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String id = dataSnapshot.child("id").getValue(String.class);
                List<String> listForUserElements = new ArrayList<>();
                listForUserElements.add("Имя");
                listForUserElements.add("Связь");
                user.add(name);
                user.add(email);

                btnSignUp.setOnClickListener(v -> mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                        .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        switch (getIntent().getStringExtra("getOfficeFromList")){
                            case "Кардиозона":
                                if (snapshot.getChildrenCount() < 12){
                                    addUsersInHall(listForUserElements, name, id);
                                }
                                else
                                    Snackbar.make(constraint, "Максимальное число клиентов!", Snackbar.LENGTH_SHORT).show();
                                break;
                            case "Силовая зона":
                                if (snapshot.getChildrenCount() < 8){
                                    addUsersInHall(listForUserElements, name, id);
                                }
                                else
                                    Snackbar.make(constraint, "Максимальное число клиентов!", Snackbar.LENGTH_SHORT).show();
                                break;
                            case "Зона свободных весов":
                                if (snapshot.getChildrenCount() < 10){
                                    addUsersInHall(listForUserElements, name, id);
                                }
                                else
                                    Snackbar.make(constraint, "Максимальное число клиентов!", Snackbar.LENGTH_SHORT).show();
                                break;
                            case "Зона растяжки":
                            case "Зона групповых программ":
                                if (snapshot.getChildrenCount() < 14){
                                    addUsersInHall(listForUserElements, name, id);
                                }
                                else
                                    Snackbar.make(constraint, "Максимальное число клиентов!", Snackbar.LENGTH_SHORT).show();
                                break;
                            case "Бассейн 1":
                            case "Бассейн 2":
                                if (snapshot.getChildrenCount() < 20){
                                    addUsersInHall(listForUserElements, name, id);
                                }
                                else
                                    Snackbar.make(constraint, "Максимальное число клиентов!", Snackbar.LENGTH_SHORT).show();
                                break;
                            default:
                                Snackbar.make(constraint, "Ой!", Snackbar.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                }));
                btnUnsubscribe.setOnClickListener(v -> deleteUsersInHall(name, id));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
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

    private void addUsersInHall(List<String> listForUserElements, String name, String id){
        for (int i = 0; i < user.size(); i++){
            mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                    .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).child("Клиент " + name + " (" + id + ")").child(listForUserElements.get(i)).setValue(user.get(i));
        }
        mDatabase.child("records").child(id).child(name).child(getIntent().getStringExtra("getDateFromList"))
                    .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).setValue(true);
        usersInHall.refreshDrawableState();
    }

    private void deleteUsersInHall(String name, String id){
        mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).child("Клиент " + name + " (" + id + ")").removeValue();
        mDatabase.child("records").child(id).child(name).child(getIntent().getStringExtra("getDateFromList"))
                .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).removeValue();
        usersInHall.refreshDrawableState();
    }
}