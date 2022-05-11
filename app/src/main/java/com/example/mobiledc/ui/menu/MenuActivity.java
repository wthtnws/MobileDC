package com.example.mobiledc.ui.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledc.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding menuBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menuBinding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(menuBinding.getRoot());

        final TextView editText1 =  menuBinding.textView1;
        final TextView editText2 =  menuBinding.textView2;
        final Button menuButton = menuBinding.button;
        final Button exitButton = menuBinding.button2;

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText1.setText((String) getIntent().getSerializableExtra("username"));
                editText2.setText((String) getIntent().getSerializableExtra("apiToken"));
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
