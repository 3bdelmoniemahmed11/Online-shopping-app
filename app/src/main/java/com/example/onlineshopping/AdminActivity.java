package com.example.onlineshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button add_Products=(Button)findViewById(R.id.Product_btn);
        Button add_cat=(Button)findViewById(R.id.AddCat_bt);
        add_Products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Products=new Intent(AdminActivity.this,UploadProductActivity.class);
                startActivity(Products);
            }
        });

        add_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Categories=new Intent(AdminActivity.this,UploadCatActivity.class);
                startActivity(Categories);
            }
        });

    }
}