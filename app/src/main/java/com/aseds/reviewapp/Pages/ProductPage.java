package com.aseds.reviewapp.Pages;

import androidx.annotation.NonNull;
import java.util.*;
import java.time.*;
import java.time.chrono.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aseds.reviewapp.Adapter.CommentAdapter;
import com.aseds.reviewapp.Adapter.ProductAdapter;
import com.aseds.reviewapp.Adapter.ProductPageAdapter;
import com.aseds.reviewapp.Model.Comment;
import com.aseds.reviewapp.Model.Product;
import com.aseds.reviewapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductPage extends AppCompatActivity {
    ImageView backBtn;
    String productID;

    RecyclerView productPageRecycler;

    private List<Product> productList;
    private  Product product, myproduct;
    private ProductAdapter productAdapter;
    private ProductPageAdapter productPageAdapter;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        productID = getIntent().getStringExtra("productID");
        myproduct = (Product) getIntent().getSerializableExtra("product");

        backBtn = findViewById(R.id.back_pressed_prd_page);

        productPageRecycler = findViewById(R.id.products_page_card);



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductPage.super.onBackPressed();
            }
        });


        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(ProductPage.this, productList);
        productPageAdapter = new ProductPageAdapter(ProductPage.this, productList);

        productPageRecycler.setHasFixedSize(true);
        productPageRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        productPageRecycler.setAdapter(productPageAdapter);


        readProduct(productID);



    }


    private void readProduct(String id) {

        FirebaseDatabase.getInstance().getReference().child("Products").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                productList.add(dataSnapshot.getValue(Product.class));
                Log.d("ListSize", String.valueOf(productList.size()));

                productAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}