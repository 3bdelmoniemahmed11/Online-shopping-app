package com.example.onlineshopping;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EcommercDB extends SQLiteOpenHelper {

    private static String DatabaseName="EcommerceDatabase";
    SQLiteDatabase EcommerceDatabase;
    public EcommercDB(Context context)
    {
        super(context,DatabaseName,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
         //customer table
           db.execSQL("create table customer(CustID integer primary key autoincrement ,CustName text not null,"+
                      "Username text not null ,Password text not null ,Gender text ,Brithdate text ,Job text ) ");
        //Order table
           db.execSQL("create table orders(OrdID integer primary key autoincrement ,OrdDate text not null ,Address text not null,"+
                       "Cust_ID integer ,foreign key (Cust_ID) REFERENCES customer(CustID))");
        //Products Table
           db.execSQL("create table product(ProID integer primary key autoincrement ,ProName text not null,"+
                     " Price real  not null,Pro_Quantity integer not null, image blob ,Cat_ID integer,"+""+
                      "BarCode text not null, foreign key (Cat_ID) REFERENCES  categories(CatID) )");
       //categories table
           db.execSQL("create table categories(CatID integer primary key autoincrement,CatName text not null)");

       //OrderDetails table

           db.execSQL("create table orderDetails (Ord_D integer primary key autoincrement,"+
                       "Pro_ID integer  , Quantity integer not null,"+
                        "foreign key (Pro_ID)  REFERENCES product(ProID) )");
       //cartTbale
           db.execSQL("create table cart(CartId integer primary key autoincrement,cart_cust_id integer ,cart_productId integer,"+
                       "foreign key (cart_cust_id) REFERENCES customer(CustID),"+
                       "foreign key (cart_productId) REFERENCES product(ProID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists customer");
        db.execSQL("drop table if exists orders");
        db.execSQL("drop table if exists product");
        db.execSQL("drop table if exists categories");
        db.execSQL("drop table if exists orderDetails");
        onCreate(db);
    }

    //insert customer
    public void insertCustomer(String Name, String UserName ,String Password ,String Job,String Birthdate ,String Gender)
    {
        ContentValues row= new ContentValues();
        row.put("CustName",Name);
        row.put("Username",UserName);
        row.put("Password",Password);
        row.put("Gender",Gender);
        row.put("Brithdate",Birthdate);
        row.put("Job",Job);
        EcommerceDatabase=getWritableDatabase();
        EcommerceDatabase.insert("customer",null,row);
        EcommerceDatabase.close();

    }
    public String getCutomerId(String name)
    {
        EcommerceDatabase=getReadableDatabase();
        String[]args={name};
        Cursor cursor=EcommerceDatabase.rawQuery("select * from customer where Username=?",args);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        EcommerceDatabase.close();
        return cursor.getString(0);
    }
    //Customer Login
    public Cursor Login (String UserName, String Pass)
    {
        EcommerceDatabase=getReadableDatabase();
        String []args={UserName,Pass};
        Cursor cursor=EcommerceDatabase.rawQuery("select * from customer where  UserName=? and Password=?",args);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        EcommerceDatabase.close();
    return cursor;
    }


    //RecoveryPassword
    public String Recoverypassword(String UserName)
    {
        EcommerceDatabase=getReadableDatabase();
        String [] args={UserName};
        Cursor cursor=EcommerceDatabase.rawQuery("select Password from customer where UserName=?",args);
        cursor.moveToFirst();
        EcommerceDatabase.close();
        return  cursor.getString(0);

    }
    //update password
    public void UpdatePassword(String OldPassword , String NewPassord)
    {
        EcommerceDatabase=getWritableDatabase();
        ContentValues  row =new ContentValues();
        row.put("Password",NewPassord);
        EcommerceDatabase.update("customer",row,"Password like ?",new String []{OldPassword});
        EcommerceDatabase.close();
    }


    //Insert Prodcut
    public void InsertProducts(ProductModel productModel)
    {
        EcommerceDatabase=getWritableDatabase();
        ContentValues row=new ContentValues();
        row.put("ProName",productModel.getProName());
        row.put("Price",productModel.getProPrice());
        row.put("Pro_Quantity",productModel.getProQuantity());
        row.put("image",productModel.getProImage());
        row.put("Cat_ID",productModel.getCatID());
        row.put("BarCode",productModel.getBarCode());

        EcommerceDatabase.insert("product",null,row);
        EcommerceDatabase.close();
    }

    //get Products
    public Cursor getProducts()
    {
        EcommerceDatabase=getReadableDatabase();
        Cursor cursor=EcommerceDatabase.rawQuery("select * from product",null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        EcommerceDatabase.close();
        return cursor;
    }

    //get ProductID
    public String getProductId(String ProductName)
    {
        EcommerceDatabase=getReadableDatabase();
        String[]args={ProductName};
        Cursor cursor=EcommerceDatabase.rawQuery("select * from product where ProName=?",args);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        EcommerceDatabase.close();
        return cursor.getString(0);
    }
    //Insert Categories
    public void InsertCategory(String CatName)
    {
        EcommerceDatabase=getWritableDatabase();
        ContentValues row=new ContentValues();
        row.put("CatName",CatName);
        EcommerceDatabase.insert("categories",null,row);
        EcommerceDatabase.close();

    }
    //get All Categories
    public Cursor getCategories()
    {
        EcommerceDatabase=getReadableDatabase();

        Cursor cursor=EcommerceDatabase.rawQuery("select * from categories",null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        EcommerceDatabase.close();

    return cursor;
    }

    //get Category ID
    public String getCatId(String name ){
        EcommerceDatabase=getReadableDatabase();
        String[]args={name};
        Cursor cursor=EcommerceDatabase.rawQuery("select CatID from categories where CatName =?",args);

        if (cursor!=null) {

            cursor.moveToFirst();
        }
        EcommerceDatabase.close();
        return cursor.getString(0);
    }

    public String getCatName(String CatId)
    {
        EcommerceDatabase=getReadableDatabase();
        String[]args={CatId};
        Cursor cursor=EcommerceDatabase.rawQuery("select CatName from categories where CatID =?",args);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }

        EcommerceDatabase.close();
        return cursor.getString(0);
    }

    public String getBarcode(String BarcodeValue)
    {
        EcommerceDatabase=getReadableDatabase();
        String[]args={BarcodeValue};
        Cursor cursor=EcommerceDatabase.rawQuery("select BarCode from product where BarCode=?",args);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        EcommerceDatabase.close();
        if(cursor.getString(0)==null)
        {
            return "Invalid barcode";
        }else
        {
            return  cursor.getString(0);
        }

    }

    //insrt order details
    public void  InsertIntoCart(int  ProdcutId, int CustomerId)
    {
        ContentValues row=new ContentValues();
        row.put("cart_cust_id",CustomerId);
        row.put("cart_productId",ProdcutId);
        EcommerceDatabase=getWritableDatabase();
        EcommerceDatabase.insert("cart",null,row);
        EcommerceDatabase.close();
    }
    public void InsertOrderDetails(String ProductID,String Quantity)
    {
        ContentValues row=new ContentValues();
        row.put("Pro_ID",ProductID);
        row.put("Quantity",Quantity);
        EcommerceDatabase=getWritableDatabase();
        EcommerceDatabase.insert("orderDetails",null,row);
        EcommerceDatabase.close();
    }

    public Cursor getProdcutInfo(String ProductId)
    {
        EcommerceDatabase=getReadableDatabase();
        String[]args={ProductId};
        Cursor cursor=EcommerceDatabase.rawQuery("select * from product where ProID =?",args);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        EcommerceDatabase.close();
        return cursor;
    }


    public String GetProductQuantity(String ProductId)
    {
        EcommerceDatabase=getReadableDatabase();
        String[]args={ProductId};
        Cursor cursor=EcommerceDatabase.rawQuery("select Quantity from orderDetails where Pro_ID =?",args);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        EcommerceDatabase.close();
        return cursor.getString(0);
    }

    //update password
    public void UpdateQuantiy(String ProductId,String oldQuantity ,String New_Quantity)
    {
        EcommerceDatabase=getWritableDatabase();
        ContentValues  row =new ContentValues();
        row.put("Quantity",New_Quantity);
        EcommerceDatabase.update("orderDetails",row,"Quantity like ? and  Pro_ID like ?",new String []{oldQuantity,ProductId});
        EcommerceDatabase.close();
    }
    public void DeleteItem(String ProductId)
    {
        EcommerceDatabase=getReadableDatabase();
        EcommerceDatabase.delete("orderDetails","Pro_ID like ?",new String []{ProductId});
        EcommerceDatabase.close();
    }
/*
    //get selected products for specific customer
    public Cursor getProdcutIdForCustomer(String cutomerId)
    {
        EcommerceDatabase=getReadableDatabase();
        String[]args={cutomerId};
        Cursor cursor=EcommerceDatabase.rawQuery("select * from cart where cart_cust_id =?",args);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }

        EcommerceDatabase.close();
        return cursor;
    }


 */
}

