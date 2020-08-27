package com.simpelexo.kayanwaiter.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simpelexo.kayanwaiter.Database.Database;
import com.simpelexo.kayanwaiter.Model.Food;
import com.simpelexo.kayanwaiter.Model.Order;
import com.simpelexo.kayanwaiter.R;

public class FoodDetail extends AppCompatActivity {
    TextView    txt_food_name,txt_food_price,txt_food_description;
    ImageView img_food_detail;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btn_add_item;
    ElegantNumberButton btn_item_number;

    String foodsId = "";
    FirebaseDatabase database;
    DatabaseReference foods;

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //init view
        btn_item_number = (ElegantNumberButton) findViewById(R.id.btn_item_number);
        btn_add_item = (FloatingActionButton) findViewById(R.id.btn_add_item);
        btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodsId,
                        currentFood.getName(),
                        btn_item_number.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()

                ));
                Toast.makeText(FoodDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });


        txt_food_description = (TextView) findViewById(R.id.txt_food_description);
        txt_food_name = (TextView) findViewById(R.id.txt_food_name);
        txt_food_price = (TextView) findViewById(R.id.txt_food_price);
        img_food_detail = (ImageView) findViewById(R.id.img_food_detail);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if (getIntent() != null)
            foodsId = getIntent().getStringExtra("FoodsId");
        if (!foodsId.isEmpty() && foodsId != null)

                getDetailFood(foodsId);
            else {
                Toast.makeText(this, "Please Check the Internet Connection", Toast.LENGTH_SHORT).show();
                return;
            }

    }

    private void getDetailFood(String foodsId) {
        foods.child(foodsId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentFood = snapshot.getValue(Food.class);
                Glide
                        .with(getBaseContext())
                        .load(currentFood.getImage())
                        .fitCenter()
                        .placeholder(R.drawable.loading)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(img_food_detail);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                txt_food_price.setText(currentFood.getPrice());
                txt_food_name.setText(currentFood.getName());
                txt_food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}