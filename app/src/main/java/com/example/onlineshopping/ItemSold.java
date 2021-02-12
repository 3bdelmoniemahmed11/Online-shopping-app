package com.example.onlineshopping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.onlineshopping.CartAdapter.ProductIds;

public class ItemSold extends AppCompatActivity {
     TextView ProductName,ProductPrice,ProductCategory,ProductQuantity,OrdreData;
     ImageView image;
     Button Confirm ,Cancel;
     Button btPicker;
     String Address;
     int PLACE_PICKER_REQUEST=1;

    EcommercDB EDB=new EcommercDB(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_sold);
        image = (ImageView) findViewById(R.id.ProImageOut_IV);
        ProductName = (TextView) findViewById(R.id.ProdcutNameOut_text);
        ProductPrice = (TextView) findViewById(R.id.PriceOut_text);
        ProductCategory = (TextView) findViewById(R.id.catOut_text);
        ProductQuantity = (TextView) findViewById(R.id.QuantityOut_text);
        OrdreData=(TextView) findViewById(R.id.DateOut);
        Confirm = (Button) findViewById(R.id.Confirm_btn);
        Cancel = (Button) findViewById(R.id.Cancel_btn);
        btPicker = (Button) findViewById(R.id.Location);
        getProductInfo();

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EDB.DeleteItem(CartAdapter.ProductId);

                for(int i=0;i< CartAdapter.ProductIds.size();i++)
                {
                    if(CartAdapter.ProductIds.get(i).equals(CartAdapter.ProductId))
                    {
                        CartAdapter.ProductIds.remove(i);
                    }
                }
            }
        });
        btPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(ItemSold.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PLACE_PICKER_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                Place place=PlacePicker.getPlace(data,this);
                StringBuilder stringBuilder=new StringBuilder();
                String latitude= String.valueOf(place.getLatLng().latitude);
                String longtitude= String.valueOf(place.getLatLng().longitude);
                stringBuilder.append(latitude);
                stringBuilder.append(",");
                stringBuilder.append(longtitude);
                Address=stringBuilder.toString();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //get Product Info
    private void getProductInfo() {
        Cursor cursor = EDB.getProdcutInfo(CartAdapter.ProductId);
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                ProductModel Product= new ProductModel(cursor.getString(1),Double.parseDouble(cursor.getString(2)),Integer.parseInt(cursor.getString(3)),
                        cursor.getBlob(4),Integer.parseInt(cursor.getString(5)),cursor.getString(6));

                byte[] image_byte = cursor.getBlob(4);
                Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
                image.setImageBitmap(bmp);
                ProductName.setText(cursor.getString(1));
                ProductPrice.setText(String.valueOf(Integer.valueOf(cursor.getString(2))* Integer.valueOf(EDB.GetProductQuantity(CartAdapter.ProductId))));
                ProductCategory.setText(EDB.getCatName(cursor.getString(5)));
                ProductQuantity.setText(EDB.GetProductQuantity(CartAdapter.ProductId));
                Calendar calendar=Calendar.getInstance();
                Date today=calendar.getTime();
                calendar.add(Calendar.DATE,3);
                Date future=calendar.getTime();
                OrdreData.setText(String.valueOf(future));
                cursor.moveToNext();
            }
        }
    }
}