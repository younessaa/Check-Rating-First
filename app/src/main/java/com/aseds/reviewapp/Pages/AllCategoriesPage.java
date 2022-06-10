package com.aseds.reviewapp.Pages;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.aseds.reviewapp.Adapter.MostViewedProductAdapter;
import com.aseds.reviewapp.Model.Product;
import com.aseds.reviewapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllCategoriesPage extends AppCompatActivity {
    ImageView backBtn;
    RecyclerView mostViewedRecycler;
    private MostViewedProductAdapter mostViewedProductAdapter;
    private List<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_categories);
        backBtn = findViewById(R.id.back_pressed);
        mostViewedRecycler = findViewById(R.id.all_categories_recycler);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllCategoriesPage.super.onBackPressed();
            }
        });

        productList = new ArrayList<>();
        mostViewedProductAdapter = new MostViewedProductAdapter(AllCategoriesPage.this, productList);

        mostViewedRecycler();

        // Read products from firebase
        readProducts();
    }

    private void mostViewedRecycler() {
        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mostViewedRecycler.setAdapter(mostViewedProductAdapter);

    }


    private void readProducts() {

        FirebaseDatabase.getInstance().getReference().child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    productList.add(product);

                }
                mostViewedProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
