package com.example.onlineshopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NavgatieButtons  extends RecyclerView.Adapter<NavgatieButtons.NavigateViewHolder> {
    public Context ContextInput;
    public  ArrayList<String> NavigateButtons;
    private NavgatieButtons.OnItemClickListner mlistner;
    public interface OnItemClickListner{
        void filterByCtegory(String name);

    };
    public void  setOnItemClickListner(NavgatieButtons.OnItemClickListner listner)
    {
        mlistner=listner;
    }

    public NavgatieButtons(Context context,ArrayList<String> Categories){
        this.ContextInput=context;
        this.NavigateButtons=Categories;
    }
    @NonNull
    @Override
    public NavigateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigate,parent,false);
        return new NavgatieButtons.NavigateViewHolder(view,mlistner);
    }

    @Override
    public void onBindViewHolder(@NonNull NavigateViewHolder holder, int position) {



            holder.Cat.setText(NavigateButtons.get(position));


    }

    @Override
    public int getItemCount() {
        return  NavigateButtons.size();
    }

    public class NavigateViewHolder extends RecyclerView.ViewHolder
    {
        Button Cat,AllItems;
        public NavigateViewHolder(@NonNull View itemView,final OnItemClickListner listner) {
            super(itemView);
             Cat=(Button)itemView.findViewById(R.id.Cat_btn);


             Cat.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(listner!=null)
                     {
                         listner.filterByCtegory(Cat.getText().toString());

                     }
                 }
             });
        }
    }
}
