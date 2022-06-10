package com.aseds.reviewapp.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseds.reviewapp.Adapter.MostViewedProductAdapter;
import com.aseds.reviewapp.Adapter.ProductAdapter;
import com.aseds.reviewapp.MainActivity;
import com.aseds.reviewapp.Model.Product;
import com.aseds.reviewapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoriePage extends AppCompatActivity {

    RecyclerView mostViewedRecycler;
    private MostViewedProductAdapter mostViewedProductAdapter;
    private List<Product> productList;
    String categorie = "";
    TextView categorie_text;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie_page);
        categorie = getIntent().getStringExtra("categorie");

        mostViewedRecycler = findViewById(R.id.categories_recycler);

        categorie_text = findViewById(R.id.categorie_text);
        backBtn = findViewById(R.id.back_pressed);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoriePage.super.onBackPressed();
            }
        });

        categorie_text.setText(categorie);

        productList = new ArrayList<>();
        mostViewedProductAdapter = new MostViewedProductAdapter(CategoriePage.this, productList);

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
                    if(product.getCategorie().equals(categorie)) {
                        productList.add(product);
                    }


                }
                mostViewedProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}