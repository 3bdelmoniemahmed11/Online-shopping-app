package com.example.onlineshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UploadCatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_cat);
        EditText CatName=(EditText)findViewById(R.id.Cate_txt);
        Button UploadCat=(Button)findViewById(R.id.uploadCat_btn);
        EcommercDB EDB=new EcommercDB(this);
        UploadCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EDB.InsertCategory(CatName.getText().toString());
                Toast.makeText(getApplicationContext(),"Category Added Successfully",Toast.LENGTH_LONG).show();
                CatName.setText("");

            }
        });

    }
}