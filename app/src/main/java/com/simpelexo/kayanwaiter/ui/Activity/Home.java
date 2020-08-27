package com.simpelexo.kayanwaiter.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simpelexo.kayanwaiter.Interface.ItemClickListener;
import com.simpelexo.kayanwaiter.Model.Category;
import com.simpelexo.kayanwaiter.R;
import com.simpelexo.kayanwaiter.Utiles.Common;
import com.simpelexo.kayanwaiter.Utiles.HelperMethod;
import com.simpelexo.kayanwaiter.ViewHolder.MenuViewHolder;

import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;
    TextView TextFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager linearLayoutManager;
  FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);


        // Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("categories");
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//

        // init paper
        Paper.init(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add to cart
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


     //    set name for user
        View headerView = navigationView.getHeaderView(0);
//        TextFullName = headerView.findViewById(R.id.txtFullName);
//        TextFullName.setText(Common.currentUser.getName());

// Load menu

        recycler_menu = findViewById(R.id.recycler_menu);
        //recycler_menu.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(linearLayoutManager);

        if (HelperMethod.isConnectedToInternet(getBaseContext()))


            loadMenu();
        else {
            loadMenu();
            Toast.makeText(Home.this, "Please Check Internet Connection", Toast.LENGTH_LONG).show();
            return;
        }
        // register the service
//        Intent service = new Intent(Home.this, ListenOrder.class);
//        startService(service);
    }

    public void loadMenu() {

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(category, Category.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MenuViewHolder menuViewHolder, int i,
                                            @NonNull final Category category) {


              // Picasso.get().load(category.getImage()).placeholder(R.drawable.loading).fit().centerCrop().into(menuViewHolder.imageView);
                String menuImgUrl = category.getImage();
                Glide
                        .with(getBaseContext())
                        .load(menuImgUrl)
                        .fitCenter()
                        .placeholder(R.drawable.loading)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(menuViewHolder.imageView);

                menuViewHolder.txtMenuName.setText(category.getName());
                final Category clickItem = category;
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get categoryId and send to Food Activity
                        Intent foodList = new Intent(Home.this,FoodList.class);
                        //Category id is key so we get the key :)
                        foodList.putExtra("categoriesId",adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                } );
            }


            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(view);
            }
        };
//       adapter.startListening();
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_refresh) {
            loadMenu();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_menu) {
            Intent menuIntent = new Intent(Home.this, Home.class);
            startActivity(menuIntent);

        } else if (id == R.id.nav_cart) {
           Intent cartIntent = new Intent(Home.this, Cart.class);
           startActivity(cartIntent);

        } else if (id == R.id.nav_orders) {
//            Intent orderIntent = new Intent(Home.this, OrderStatus.class);
//            startActivity(orderIntent);

        } else if (id == R.id.nav_log_out) {

            // delete remember user and password
            Paper.book().destroy();

            Intent mainActivity = new Intent(Home.this, MainActivity.class);
            mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivity);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;    }





}