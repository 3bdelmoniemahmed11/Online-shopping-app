package com.example.onlineshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText userName;
    EditText Password;
    static String cutomerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //initialization
        String sharedPrefFile="com.example.onlineshopping";
        userName=(EditText)findViewById(R.id.UserName_txt);
        Password=(EditText)findViewById(R.id.Password_txt);
        Switch Rememberme=(Switch)findViewById(R.id.Rememberme_sw);
        TextView ReocveryPassword=(TextView)findViewById(R.id.pass_txt);
        Button Login=(Button)findViewById(R.id.login_btn);
        TextView SignUp=(TextView)findViewById(R.id.SignUp_txt);
        EcommercDB EDB=new EcommercDB(this);
        sharedPreferences=getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        LoadData();
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Cursor cursor = EDB.Login(userName.getText().toString(),Password.getText().toString());
               //dont forget the admin
                if(userName.getText().toString().equals("admin")&& Password.getText().toString().equals("admin"))
                {
                    if(Rememberme.isChecked())
                    {
                        SaveData(userName.getText().toString(),Password.getText().toString(),Rememberme.isChecked());
                    }
                    Intent Admin=new Intent(SignInActivity.this,AdminActivity.class);
                    startActivity(Admin);
                }
              else if(cursor.getCount()<=0)
              {
                  Toast.makeText(getApplicationContext(),"Check your Username and Password",Toast.LENGTH_LONG).show();
              }
              else
              {
                  if(Rememberme.isChecked())
                  {
                      SaveData(userName.getText().toString(),Password.getText().toString(),Rememberme.isChecked());
                  }
                  Intent MainPage=new Intent(SignInActivity.this,MainViewActivity.class);
                  cutomerId=EDB.getCutomerId(userName.getText().toString());
                  startActivity(MainPage);
              }
            }
        });


        ReocveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ReoveryPassword=new Intent(SignInActivity.this,RecoveryPasswordActivity.class);
                startActivity(ReoveryPassword);
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignUp=new Intent(SignInActivity.this,SignUpnActivity.class);
                startActivity(SignUp);
            }
        });
    }


    //saving the data of the user
    public void SaveData(String UserNameInput,String PasswordInput,boolean checked)
    {
        SharedPreferences.Editor preferencesEditor=sharedPreferences.edit();
        preferencesEditor.putString("Username",UserNameInput);
        preferencesEditor.putString("Password",PasswordInput);
        preferencesEditor.putBoolean("SwitchValue",checked);
        preferencesEditor.apply();


    }

    public void LoadData()
    {
        boolean loginFlag=sharedPreferences.getBoolean("SwitchValue",false);
        if(loginFlag)
        {
            if(sharedPreferences.getString("Username","").equals("admin")&sharedPreferences.getString("Password","").equals("admin") )
            {
                Intent AdminPage=new Intent(SignInActivity.this,AdminActivity.class);
                startActivity(AdminPage);

            }else
            {
                Intent MainPage=new Intent(SignInActivity.this,MainViewActivity.class);
                startActivity(MainPage);
            }

        }
    }
}