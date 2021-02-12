package com.example.onlineshopping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Console;
import java.util.ArrayList;
import java.util.Locale;

public class MainViewActivity extends AppCompatActivity {

    String CatName;
    String SearchVoice;
    private ArrayList<String> categories;
    private ArrayList<ProductModel> Products=new ArrayList<>();
    private ProductAdapter ProductAdapter;
    private static final int REQUEST_CODE_SPEECH_INPUT=1000;

    private NavgatieButtons NavgatieButtons;
    RecyclerView Recycler;
    RecyclerView RecyclerNavgatieButtons;
    EcommercDB EDB=new EcommercDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        categories=new ArrayList<String>();
        Recycler= findViewById(R.id.Rec);
        getAllProduct();
        ProductAdapter=new ProductAdapter(this,Products);
        Recycler.setAdapter(ProductAdapter);
        Recycler.setLayoutManager(new LinearLayoutManager(this));


        //Navgatie Buttons
        RecyclerNavgatieButtons=findViewById(R.id.Rec_2);
        getAllCategories();
        System.out.println(String.valueOf(categories.get(0)));
        NavgatieButtons=new NavgatieButtons(this,categories);
        RecyclerNavgatieButtons.setAdapter(NavgatieButtons);
        RecyclerNavgatieButtons.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        NavgatieButtons.setOnItemClickListner(new NavgatieButtons.OnItemClickListner() {
            @Override
            public void filterByCtegory(String name) {
                CatName=name;
                ProductAdapter.CategoryFilter(CatName);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        MenuItem mycart=menu.findItem(R.id.action_drawer_cart);
        MenuItem cameraItem=menu.findItem(R.id.BarCode);
        mycart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {



                Intent mycart=new Intent(MainViewActivity.this,myCartActivity.class);
                String cutomerId=getIntent().getStringExtra("customerId");
                mycart.putExtra("customerId2",cutomerId);
                startActivity(mycart);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ProductAdapter.getFilter().filter(newText);
                return false;
            }
        });

        MenuItem SearchByVoice=menu.findItem(R.id.Voice_Search);
        SearchByVoice.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Speak();

                return false;
            }
        });

        //search by barcode
        cameraItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                  ScanCode();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



    private void getAllProduct() {
        Products.clear();
        Cursor cursor = EDB.getProducts();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                ProductModel Product= new ProductModel(cursor.getString(1),Double.parseDouble(cursor.getString(2)),Integer.parseInt(cursor.getString(3)),
                                          cursor.getBlob(4),Integer.parseInt(cursor.getString(5)),cursor.getString(6));
                Products.add(Product);
                cursor.moveToNext();
            }
        }
    }

    private void getAllCategories()
    {
        Cursor cursor=EDB.getCategories();
        if(cursor!=null)
        {
            categories.add("All");
            while(!cursor.isAfterLast())
            {
                categories.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
    };

private void Speak()
{
    //intent to show speech dialog
    Intent Speech=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    Speech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,  RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    Speech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    Speech.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Say Something");
    try {

        startActivityForResult(Speech,REQUEST_CODE_SPEECH_INPUT);
    }
    catch (Exception e)
    {
        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
    }

}

    public void ScanCode()
    {
        IntentIntegrator intentIntegrator=new IntentIntegrator(MainViewActivity.this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Sacnning Code");
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                SearchVoice = result.get(0);
                ProductAdapter.getFilter().filter(SearchVoice);

            }
        } else
            {
                IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
                if(result!=null)
                {
                    if(result.getContents()!=null)
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainViewActivity.this);
                        builder.setMessage(result.getContents());
                        builder.setTitle("Scanning Result");
                        builder.setPositiveButton("ScanAgain", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ScanCode();
                            }
                        }).setNegativeButton("finsh", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(ProductAdapter.BeforeBarcodeFiltering(result.getContents()))
                                {
                                    ProductAdapter.BarcodeFilter(result.getContents());
                                    Toast.makeText(MainViewActivity.this, "Item Scanned", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    ProductAdapter.BarcodeFilter(result.getContents());
                                    Toast.makeText(MainViewActivity.this,"Invalid barcode",Toast.LENGTH_LONG).show();
                                }
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
        /*
        switch (requestCode)
        {
            case REQUEST_CODE_SPEECH_INPUT:
            {
                if(resultCode==RESULT_OK && data!=null)
                {
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    SearchVoice=result.get(0);
                    ProductAdapter.getFilter().filter(SearchVoice);

                }
                break;
            }

        }
      */

    }
}