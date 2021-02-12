package com.example.onlineshopping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    public Context ContentInput;
    public ArrayList<ProductModel> SelectedProducts;
    private OnItemClickListner mlistner;
    public static String ProductId;
    public static ArrayList<String> ProductIds=new ArrayList<>();
    public interface OnItemClickListner{
      void OnCLickDelete(int pos);
    };
    public void  setOnItemClickListner(OnItemClickListner listner)
    {
        mlistner=listner;
    }
    EcommercDB EDB;

    public CartAdapter(Context context,ArrayList<ProductModel> SProducts)
    {
        this.ContentInput=context;
        this.SelectedProducts=SProducts;
        EDB=new EcommercDB(context);

    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartview,parent,false);
        return new CartAdapter.CartViewHolder(view,mlistner);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        holder.ProductName.setText(SelectedProducts.get(position).getProName());
        holder.ProductPrice.setText(String.valueOf(SelectedProducts.get(position).getProPrice()));
        holder.ProductCategory.setText(EDB.getCatName( String.valueOf(SelectedProducts.get(position).getCatID())));
        byte[] image_byte = SelectedProducts.get(position).getProImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
        holder.image.setImageBitmap(bmp);
        holder.Quantity.setText(String.valueOf(SelectedProducts.get(position).getProQuantity()));

    }

    @Override
    public int getItemCount() {
        return SelectedProducts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView ProductName,ProductPrice,ProductCategory;
        Button Buy,Delete;
        EditText Quantity;
        ImageView minus,plus;
        public CartViewHolder(@NonNull View itemView,final OnItemClickListner listner) {
            super(itemView);


            image=(ImageView)itemView.findViewById(R.id.Pro_pic);
            ProductName=(TextView)itemView.findViewById(R.id.productName_txt);
            ProductPrice=(TextView)itemView.findViewById(R.id.ProductPrice_txt);
            ProductCategory=(TextView)itemView.findViewById(R.id.ProductCategory_txt);
            Buy=(Button)itemView.findViewById(R.id.Buy_btn);
            Delete=(Button)itemView.findViewById(R.id.Remove_btn);
            Quantity=(EditText)itemView.findViewById(R.id.Q_ET);
            minus=(ImageView)itemView.findViewById(R.id.minus_btn);
            plus=(ImageView)itemView.findViewById(R.id.plus_btn);
            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listner.OnCLickDelete(position);
                        }
                    }
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                           Quantity.setText(String.valueOf(Integer.parseInt(Quantity.getText().toString()) -1));


                }
            });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Quantity.setText(String.valueOf(Integer.parseInt(Quantity.getText().toString()) +1));
                }
            });

            Buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            if (!(ProductIds.contains(EDB.getProductId(SelectedProducts.get(position).getProName())))) {
                                if (Integer.valueOf(Quantity.getText().toString()) <= Integer.valueOf(SelectedProducts.get(position).getProQuantity())) {
                                    EDB.InsertOrderDetails(EDB.getProductId(SelectedProducts.get(position).getProName()), Quantity.getText().toString());
                                    ProductIds.add(EDB.getProductId(SelectedProducts.get(position).getProName()));
                                    ProductId = EDB.getProductId(SelectedProducts.get(position).getProName());
                                    v.getContext().startActivity(new Intent(v.getContext(), ItemSold.class));
                                } else {
                                    Toast.makeText(v.getContext(), "Added SuccessfullyBefore", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                if (Integer.valueOf(Quantity.getText().toString()) <= Integer.valueOf(SelectedProducts.get(position).getProQuantity())) {
                                    EDB.UpdateQuantiy(EDB.getProductId(SelectedProducts.get(position).getProName()), EDB.GetProductQuantity(EDB.getProductId(SelectedProducts.get(position).getProName())), Quantity.getText().toString());
                                    v.getContext().startActivity(new Intent(v.getContext(), ItemSold.class));
                                }
                                else {
                                    Toast.makeText(v.getContext(), "Added SuccessfullyBefore", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                    }
                }
            });

        }
    }
}
