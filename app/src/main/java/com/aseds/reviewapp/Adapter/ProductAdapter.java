package com.aseds.reviewapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aseds.reviewapp.AddProductActivity;
import com.aseds.reviewapp.Model.Product;
import com.aseds.reviewapp.Pages.ProductPage;
import com.aseds.reviewapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Viewholder> {

    private final Context mContext;
    private final List<Product> mProducts;
    View view;
    private final FirebaseUser firebaseUser;

    public ProductAdapter(Context mContext, List<Product> mProducts) {
        this.mContext = mContext;
        this.mProducts = mProducts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(mContext).inflate(R.layout.product_card, parent, false);
        return new ProductAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {

        final Product product = mProducts.get(position);
        Picasso.get().load(product.getImageurl()).into(holder.productImage);
        holder.name.setText(product.getName());
        holder.rating.setRating(product.getRating());
        holder.price.setText(product.getPrice().toString() + " DH");


        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("productid", product.getProductid()).apply();

                Intent intent = new Intent(mContext, ProductPage.class);
                intent.putExtra("productID", product.getProductid());
                intent.putExtra("product", product);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {


        public ImageView productImage;
        TextView name;
        TextView price;
        RatingBar rating;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            rating = itemView.findViewById(R.id.product_rating);

        }
    }


    private void getComments (String productID, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
