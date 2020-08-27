package com.simpelexo.kayanwaiter.ui.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simpelexo.kayanwaiter.Adapters.CartAdapter;
import com.simpelexo.kayanwaiter.Database.Database;
import com.simpelexo.kayanwaiter.Model.Order;
import com.simpelexo.kayanwaiter.Model.Request;
import com.simpelexo.kayanwaiter.R;
import com.simpelexo.kayanwaiter.Utiles.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txt_cart_total;
    Button btn_place_order;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
      // Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //init
        recyclerView = (RecyclerView)findViewById(R.id.rv_list_cart);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txt_cart_total = (TextView)findViewById(R.id.txt_cart_total);
        btn_place_order = (Button)findViewById(R.id.btn_place_order);

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
            }
        });
        
        LoadListFood();

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more Step!");
        alertDialog.setMessage("Enter Table Number: ");
        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_cart);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txt_cart_total.getText().toString(),
                        cart
                );
// Submit to firebase
                //we will using System.Current to key
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                //deleting cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank You! Order Place", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void LoadListFood() {
    cart = new Database(this).getCarts();
    adapter = new CartAdapter(cart,this);
    recyclerView.setAdapter(adapter);

    //calculate price
        int total = 0;
        for (Order order : cart)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txt_cart_total.setText(fmt.format(total));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == Common.DELETE)
            deleteCart(item.getOrder());
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteCart(int order) {
        // it will remove item at List<Order> by position
        cart.remove(order);
        // after that it will delete old data from SQlite
        new Database(this).cleanCart();
        // and finally , we will update  new data from List<order> to Sqlite
        for (Order item : cart)
            new Database(this).addToCart(item);
        //referesh
        LoadListFood();
    }
}