package com.example.onlineshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RecoveryPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);
        EditText UserName=(EditText) findViewById(R.id.UserName3_txt);
        EditText NewPassword=(EditText) findViewById(R.id.NewPass_txt);
        Button Login =(Button) findViewById(R.id.Login2_btn);
        EcommercDB EDB=new EcommercDB(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String OldPassword= EDB.Recoverypassword(UserName.getText().toString());
               if(OldPassword==null)
               {
                   Toast.makeText(getApplicationContext(),"Please check your mail",Toast.LENGTH_LONG).show();
               }
               else
               {
                   EDB.UpdatePassword(OldPassword,NewPassword.getText().toString());
                   Toast.makeText(getApplicationContext(),"Updated successfully",Toast.LENGTH_LONG).show();
               }
            }
        });
    }
}