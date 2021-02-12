package com.example.onlineshopping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadProductActivity extends AppCompatActivity {

    final static int GALLERY_REQUEST_CODE = 101;
    ArrayAdapter adapter;
    EcommercDB EDB;
    Spinner Cat;
    ImageView image,BarCodeButton;
    EditText BarCodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        image=(ImageView)findViewById(R.id.pro_image);
        EditText ProName=(EditText)findViewById(R.id.ProName_txt);
        EditText ProPrice=(EditText)findViewById(R.id.price_txt);
        EditText ProQuantity=(EditText)findViewById(R.id.Q_txt);
        BarCodeValue=(EditText)findViewById(R.id.BarCode_txt);
        Cat=(Spinner)findViewById(R.id.cat_spinner);
        Button Upload=(Button)findViewById(R.id.Upload_btn);
        BarCodeButton=(ImageView) findViewById(R.id.BarCode_IV);

        EDB =new EcommercDB(this);
        LoadAllCategory();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Gallery = new Intent(Intent.ACTION_PICK);

                Gallery.setType("image/*");

                startActivityForResult(Gallery, GALLERY_REQUEST_CODE);
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int catId=Integer.parseInt(EDB.getCatId(Cat.getSelectedItem().toString()));
                byte [] ImageByte=imageViewToByte(image);
                if(!ProName.getText().toString().equals("") &&!ProPrice.getText().toString().equals("") &&!ProQuantity.getText().toString().equals("")&&!BarCodeValue.getText().toString().equals(""))
                {

                    ProductModel productModel=new ProductModel(ProName.getText().toString(),Double.parseDouble(ProPrice.getText().toString()),
                            Integer.parseInt(ProQuantity.getText().toString()),ImageByte,catId,BarCodeValue.getText().toString());
                    EDB.InsertProducts(productModel);

                   /*
                    EDB.InsertProducts(ProName.getText().toString(),Double.parseDouble(ProPrice.getText().toString()),
                                      Integer.parseInt(ProQuantity.getText().toString()),ImageByte,catId);

                    */

                    image.setImageResource(R.drawable.proimg);
                    ProName.setText("");
                    ProPrice.setText("");
                    ProQuantity.setText("");
                    BarCodeValue.setText("");

                    Toast.makeText(getApplicationContext(),"Product Added",Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Check your information",Toast.LENGTH_LONG).show();
                }

            }
        });
        BarCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                     ScanCode();
            }
        });
      

    }


    public void LoadAllCategory()
    {
        List<String> category=new ArrayList<>();
       Cursor cursor= EDB.getCategories();
       while (!cursor.isAfterLast())
       {
           category.add(cursor.getString(1));
         cursor.moveToNext();
       }
       adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,category);
       Cat.setAdapter(adapter);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else
        {
            IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(result!=null)
            {
                if(result.getContents()!=null)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(UploadProductActivity.this);
                    builder.setMessage(result.getContents());
                    BarCodeValue.setText(result.getContents());
                    builder.setTitle("Scanning Result");
                    builder.setPositiveButton("ScanAgain", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ScanCode();
                        }
                    }).setNegativeButton("finsh", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(UploadProductActivity.this, "Item Scanned", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }else
                {
                    Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
                }
            }else
            {
                super.onActivityResult(requestCode,resultCode,data);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    protected static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

   public void ScanCode()
    {
        IntentIntegrator intentIntegrator=new IntentIntegrator(UploadProductActivity.this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Sacnning Code");
        intentIntegrator.initiateScan();
    }
}