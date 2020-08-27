package com.simpelexo.kayanwaiter.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simpelexo.kayanwaiter.Interface.ItemClickListener;
import com.simpelexo.kayanwaiter.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_food_name;
    public ImageView img_food;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_food_name = (TextView) itemView.findViewById(R.id.txt_food_name);
        img_food = (ImageView) itemView.findViewById(R.id.img_food);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {itemClickListener.onClick(v,getAdapterPosition(),false); }


}
