package com.example.onlineshopping;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    public Context ContentInput;
    public ArrayList<ProductModel> productInput;
    public ArrayList<ProductModel> SearchProducts;
    public ArrayList<ProductModel> SearchByCategory;
    public ArrayList<ProductModel> SearchByBarcode;
    public  static ArrayList<ProductModel> SelectedProducts=new ArrayList<>();

   /* public ArrayList<String> productIds = new ArrayList<String>();*/
    EcommercDB EDB;

    public  ProductAdapter (Context context, ArrayList<ProductModel> Product)
    {
        this.ContentInput=context;
        this.productInput=Product;
        EDB=new EcommercDB(context);
        SearchProducts=new ArrayList<>(productInput);
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productitem,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.ProductName.setText(productInput.get(position).getProName());
        holder.ProductPrice.setText(String.valueOf(productInput.get(position).getProPrice()));
        holder.ProductCategory.setText(EDB.getCatName( String.valueOf(productInput.get(position).getCatID())));
        byte[] image_byte = productInput.get(position).getProImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
        holder.image.setImageBitmap(bmp);

    }

    @Override
    public int getItemCount() {

        return productInput.size();
    }

    @Override
    public Filter getFilter() {
        return ProductFilter;
    }
    private Filter ProductFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ProductModel> FilteredList=new ArrayList<>();
            if(constraint==null ||constraint.length()==0)
            {
                FilteredList.addAll(SearchProducts);
            }else
            {
                String filterPattern=constraint.toString().toLowerCase().trim();
                for(ProductModel item :SearchProducts)
                {
                    if(item.getProName().toLowerCase().contains(filterPattern))
                    {
                        FilteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=FilteredList;
            return  results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productInput.clear();
            productInput.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    void  CategoryFilter(String CategoryName)
    {
        boolean flag=false;
        SearchByCategory=new ArrayList<ProductModel>();

        for(ProductModel item :SearchProducts)
        {
            if(EDB.getCatName(String.valueOf(item.getCatID())).toLowerCase().equals(CategoryName.toLowerCase()))
            {
                SearchByCategory.add(item);
                flag=true;
            }
        }
        if(flag)
        {
            productInput.clear();
            productInput.addAll(SearchByCategory);
            notifyDataSetChanged();
        }else
        {
            productInput.clear();
            productInput.addAll(SearchProducts);
            notifyDataSetChanged();
        }

    };

    public boolean BeforeBarcodeFiltering(String BarcodeValue)
    {
        for(ProductModel item :SearchProducts)
        {
            if(EDB.getBarcode(item.getBarCode()).equals(BarcodeValue))
            {
                return true;
            }else
            {
                productInput.clear();
                productInput.addAll(SearchProducts);
                notifyDataSetChanged();
                return false;
            }
        }
        return false;
    };
    public void BarcodeFilter(String BarcodeValue)
    {
        SearchByBarcode=new ArrayList<ProductModel>();
        for(ProductModel item :SearchProducts)
        {
            if(EDB.getBarcode(item.getBarCode()).equals(BarcodeValue))
            {
                SearchByBarcode.add(item);
                productInput.clear();
                productInput.addAll(SearchByBarcode);
                notifyDataSetChanged();
            }else
            {
                productInput.clear();
                productInput.addAll(SearchProducts);
                notifyDataSetChanged();
            }
        }

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView ProductName, ProductPrice, ProductCategory;
        Button AddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.Pro_pic);
            ProductName = (TextView) itemView.findViewById(R.id.productName_txt);
            ProductPrice = (TextView) itemView.findViewById(R.id.ProductPrice_txt);
            ProductCategory = (TextView) itemView.findViewById(R.id.ProductCategory_txt);
            AddToCart = (Button) itemView.findViewById(R.id.Buy_btn);
            AddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        if (!myCartActivity.MyCart.contains(productInput.get(getAdapterPosition()))) {

                            myCartActivity.MyCart.add(productInput.get(getAdapterPosition()));
                            EDB.InsertIntoCart(Integer.valueOf(EDB.getProductId(productInput.get(getAdapterPosition()).getProName())), Integer.valueOf(SignInActivity.cutomerId));


                        } else {
                            Toast.makeText(v.getContext(), "Added Before", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            });
        }
    }
}
