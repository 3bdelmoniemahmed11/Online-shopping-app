package com.example.onlineshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;

public class myCartActivity extends AppCompatActivity {

    public static ArrayList<ProductModel> MyCart=new ArrayList<>();
    public  ArrayList<ProductModel> SelectedProducts=new ArrayList<>();

    public ArrayList<String> productIds = new ArrayList<String>();

    public CartAdapter cartAdapter;
    RecyclerView Recycler;
    EcommercDB EDB=new EcommercDB(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        Recycler= findViewById(R.id.Rec_2);
        cartAdapter=new CartAdapter(this,MyCart);
        Recycler.setAdapter(cartAdapter);
        Recycler.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter.setOnItemClickListner(new CartAdapter.OnItemClickListner() {
            @Override
            public void OnCLickDelete(int pos) {
                RemoveItem(pos);
            }
        });
      /*  getProductIds();*/
    }


public void RemoveItem(int postion)
{
    MyCart.remove(postion);
    cartAdapter.notifyDataSetChanged();
}
/*
    private void getProductIds() {
        Cursor cursor = EDB.getProdcutIdForCustomer(SignInActivity.cutomerId);
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                productIds.add(String.valueOf(cursor.getString(2)));
                cursor.moveToNext();
            }
        }
 System.out.println("---------------------- "+productIds.size());
        for(int i = 0; i< productIds.size(); i++){

            Cursor cursor2 =EDB.getProdcutInfo(productIds.get(i));
            if (cursor2 != null) {
                while (!cursor2.isAfterLast()) {

                    ProductModel Product= new ProductModel(cursor2.getString(1),Double.parseDouble(cursor2.getString(2)),Integer.parseInt(cursor2.getString(3)),
                            cursor2.getBlob(4),Integer.parseInt(cursor2.getString(5)),cursor2.getString(6));
                    SelectedProducts.add(Product);
                    cursor2.moveToNext();
                }
            }


        }
    }

*/
}