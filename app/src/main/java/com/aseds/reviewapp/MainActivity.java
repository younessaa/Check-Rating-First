package com.aseds.reviewapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aseds.reviewapp.Adapter.MostViewedProductAdapter;
import com.aseds.reviewapp.Adapter.ProductAdapter;
import com.aseds.reviewapp.Model.Product;
import com.aseds.reviewapp.Pages.AllCategoriesPage;
import com.aseds.reviewapp.Pages.CategoriePage;
import com.aseds.reviewapp.Pages.ProductPage;
import com.aseds.reviewapp.Pages.ProfilPage;
import com.aseds.reviewapp.Pages.SearchPage;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Variables
    static final float END_SCALE=0.7f;
    LinearLayout contentView;
    RecyclerView featuredRecycler, mostViewedRecycler;

    private ProductAdapter productAdapter;
    private MostViewedProductAdapter mostViewedProductAdapter;
    private List<Product> productList, mostViewedProductsList;

    RecyclerView.Adapter adapter;
    ImageView menuIcon;
    TextView btn_electronics, btn_clothes, btn_food;
    //Drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Hooks
        featuredRecycler = findViewById(R.id.featured_recycler);
        mostViewedRecycler = findViewById(R.id.most_viewed_recycler);
        menuIcon=findViewById(R.id.menu_icon);
        contentView=findViewById(R.id.content);
        btn_electronics = findViewById(R.id.btn_electronics);
        btn_clothes = findViewById(R.id.btn_clothes);
        btn_food = findViewById(R.id.btn_food);

        btn_electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(), CategoriePage.class);
                intent4.putExtra("categorie", "electronics");
                startActivity(intent4);
            }
        });
        btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(), CategoriePage.class);
                intent4.putExtra("categorie", "food");
                startActivity(intent4);
            }
        });
        btn_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(), CategoriePage.class);
                intent4.putExtra("categorie", "clothes");
                startActivity(intent4);
            }
        });
        //Menu hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationDrawer();


        //Navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        productList = new ArrayList<>();
        mostViewedProductsList = new ArrayList<>();
        productAdapter = new ProductAdapter(MainActivity.this, productList);
        mostViewedProductAdapter = new MostViewedProductAdapter(MainActivity.this, mostViewedProductsList);



        //Functions will be executed automatically when this activity will be created
        featuredRecycler();
        mostViewedRecycler();

        // Read products from firebase
        readProducts();
    }
    // Navigation drawer function
    private void navigationDrawer() {
        //Navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

            // Scale the View based on current slide offset
            final float diffScaledOffset = slideOffset * (1 - END_SCALE);
            final float offsetScale = 1 - diffScaledOffset;
            contentView.setScaleX(offsetScale);
            contentView.setScaleY(offsetScale);

            // Translate the View, accounting for the scaled width
            final float xOffset = drawerView.getWidth() * slideOffset;
            final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
            final float xTranslation = xOffset - xOffsetDiff;
            contentView.setTranslationX(xTranslation);
        }
    });

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_all_categories:
                Intent intent = new Intent(getApplicationContext(), AllCategoriesPage.class);
                startActivity(intent);
                break;
            case R.id.nav_home:
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_add_product:
                Intent intent2 = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_clothes:
                Intent intent3 = new Intent(getApplicationContext(), CategoriePage.class);
                intent3.putExtra("categorie", "clothes");
                startActivity(intent3);
                break;
            case R.id.nav_electronics:
                Intent intent4 = new Intent(getApplicationContext(), CategoriePage.class);
                intent4.putExtra("categorie", "electronics");
                startActivity(intent4);
                break;
            case R.id.nav_food:
                Intent intent5 = new Intent(getApplicationContext(), CategoriePage.class);
                intent5.putExtra("categorie", "food");
                startActivity(intent5);
                break;
            case R.id.nav_search:
                Intent intent6 = new Intent(getApplicationContext(), SearchPage.class);
                startActivity(intent6);
                break;
            case R.id.nav_profil:
                Intent intent7 = new Intent(getApplicationContext(), ProfilPage.class);
                startActivity(intent7);
                break;
            case R.id.logout:
                Intent intent8 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent8);
                finish();
                break;
        }
        return true;
    }



    private void mostViewedRecycler() {
        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mostViewedRecycler.setAdapter(mostViewedProductAdapter);

    }

    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredRecycler.setAdapter(productAdapter);

    }

    private void readProducts() {

        FirebaseDatabase.getInstance().getReference().child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);

                    productList.add(product);
                    if(product.getRating() > 3) {
                        mostViewedProductsList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
                mostViewedProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}