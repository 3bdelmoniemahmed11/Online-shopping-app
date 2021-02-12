package com.example.onlineshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Month;
import java.util.Calendar;

public class SignUpnActivity extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_upn);
        TextView BirthDate =(TextView) findViewById(R.id.BirthDate_txt);
        Button SignUp_In=(Button) findViewById(R.id.SignUp_In_txt);
        EditText Name=(EditText)findViewById(R.id.Name_txt);
        EditText UserName=(EditText)findViewById(R.id.UserName2_txt);
        EditText Password=(EditText)findViewById(R.id.Password2_txt);
        EditText Job=(EditText)findViewById(R.id.Job_txt);
        Spinner Gender=(Spinner)findViewById(R.id.Gender_spin);
        EcommercDB EDB=new EcommercDB(this);

        Calendar calendar=Calendar.getInstance();
        int Year=calendar.get(Calendar.YEAR);
        int Month=calendar.get(Calendar.MONTH);
        int Day =calendar.get(Calendar.DAY_OF_MONTH);




        BirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(SignUpnActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,setListener,Year,Month,Day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();


            }
        });

        SignUp_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().equals("") & UserName.getText().toString().equals("") & Password.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter The fields",Toast.LENGTH_LONG).show();
                }

                EDB.insertCustomer(Name.getText().toString(),UserName.getText().toString(),Password.getText().toString()
                                  ,Job.getText().toString(),BirthDate.getText().toString(),Gender.getSelectedItem().toString());

                Toast.makeText(getApplicationContext(),"Successfull registered ",Toast.LENGTH_LONG).show();

                Intent SignIn=new Intent(SignUpnActivity.this,SignInActivity.class);
                startActivity(SignIn);
            }
        });

        setListener =new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String Date= dayOfMonth +"/"+month +"/"+year;
                BirthDate.setText(Date);
                BirthDate.setAlpha(1);

            }
        };


    }
}