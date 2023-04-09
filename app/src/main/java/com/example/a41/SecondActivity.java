package com.example.a41;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    EditText editTextTextPersonName;
    Button loginButton;
    CheckBox loginCheckBox;
    SharedPreferences sharedPreferences;
    String USER_NAME;
    public void loginClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", editTextTextPersonName.getText().toString());
        intent.putExtra("login", true);
        startActivity(intent);
        if (loginCheckBox.isChecked())
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_NAME, editTextTextPersonName.getText().toString());
            editor.apply();
            finish();

        }
        else
        {
            sharedPreferences.edit().putString(USER_NAME, "").apply();
            finish();
        }

    }
    public  void  checkSharedPreferences(){
        String username = sharedPreferences.getString(USER_NAME, "");
        editTextTextPersonName.setText((username));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView secondTextView = findViewById((R.id.secondTextView));
        Intent intent = getIntent();
        secondTextView.setText("Welcome");

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        loginCheckBox = findViewById(R.id.checkBox);
        sharedPreferences = getSharedPreferences("com.example.myapplication00", MODE_PRIVATE);

        loginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isChecked", isChecked);
                editor.apply();
            }
        });

        boolean isChecked = sharedPreferences.getBoolean("isChecked", false);
        loginCheckBox.setChecked(isChecked);

        checkSharedPreferences();

    }
}