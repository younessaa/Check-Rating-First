package com.aseds.reviewapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aseds.reviewapp.Model.Comment;
import com.aseds.reviewapp.Model.Product;
import com.aseds.reviewapp.Pages.ProductPage;
import com.aseds.reviewapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductPageAdapter extends RecyclerView.Adapter<ProductPageAdapter.Viewholder> {

    private final Context mContext;
    private final List<Product> mProducts;
    View view;
    private final FirebaseUser firebaseUser;

    public ProductPageAdapter(Context mContext, List<Product> mProducts) {
        this.mContext = mContext;
        this.mProducts = mProducts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(mContext).inflate(R.layout.product_page_card, parent, false);
        return new ProductPageAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {

        final Product product = mProducts.get(position);
        holder.product_name.setText(product.getName());
        Picasso.get().load(product.getImageurl()).into(holder.product_image);
        holder.product_rating.setText(Float.toString(product.getRating()));
        holder.product_price.setText(product.getPrice().toString() + " DH");
        holder.product_store.setText(product.getStoreName());
        holder.product_store_location.setText(product.getStoreLocation());

        holder.commentsList = new ArrayList<>();
        holder.commentAdapter = new CommentAdapter(mContext, holder.commentsList, product.getProductid());

        holder.commentsRecycler.setHasFixedSize(true);
        holder.commentsRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        holder.commentsRecycler.setAdapter(holder.commentAdapter);

        holder.readComments(product.getProductid());



        holder.btn_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = Float.parseFloat(holder.user_rating.getText().toString());

                if(rating < 5) {
                    rating += 0.5;
                    holder.user_rating.setText(Float.toString(rating));
                    holder.your_rating.setRating(rating);
                }
            }
        });

        holder.btn_dcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = Float.parseFloat(holder.user_rating.getText().toString());

                if(rating > 0) {
                    rating -= 0.5;
                    holder.user_rating.setText(Float.toString(rating));
                    holder.your_rating.setRating(rating);
                }
            }
        });



        holder.btn_add_comment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(holder.add_text_edit.getText().toString())) {

                } else {
                    holder.addComment(product.getProductid());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView product_name;
        TextView product_price;
        TextView product_rating;
        TextView product_store;
        TextView product_store_location;
        RatingBar your_rating;
        TextView user_rating;
        EditText add_text_edit;
        Button btn_add_comment;
        Button btn_inc, btn_dcr;
        FirebaseUser fUser;
        RecyclerView commentsRecycler, productPageRecycler;
        private CommentAdapter commentAdapter;
        private List<Comment> commentsList;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.prd_img);
            product_name = itemView.findViewById(R.id.prd_name);
            product_price = itemView.findViewById(R.id.prd_price);
            product_rating = itemView.findViewById(R.id.prd_rating);
            product_store = itemView.findViewById(R.id.prd_store_name);
            product_store_location = itemView.findViewById(R.id.prd_store_location);
            your_rating = itemView.findViewById(R.id.your_rating);
            user_rating = itemView.findViewById(R.id.user_rating);
            btn_inc = itemView.findViewById(R.id.inc_rating);
            btn_dcr = itemView.findViewById(R.id.dcr_rating);
            add_text_edit = itemView.findViewById(R.id.add_text_edit);
            btn_add_comment = itemView.findViewById(R.id.btn_add_comment);
            fUser = FirebaseAuth.getInstance().getCurrentUser();
            commentsRecycler = itemView.findViewById(R.id.product_comments);




        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void addComment(String id) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comments").child(id);
            String commentId = ref.push().getKey();

            LocalDate myObj = LocalDate.now();


            HashMap<String , Object> map = new HashMap<>();
            map.put("commentid" , commentId);
            map.put("comment" , add_text_edit.getText().toString());
            map.put("publisher" , fUser.getUid());
            map.put("date" , myObj.toString());

            ref.child(commentId).setValue(map);

            add_text_edit.setText("");

        }
        private void readComments(String id) {

            FirebaseDatabase.getInstance().getReference().child("Comments").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(commentsList.size() != 0) {
                        commentsList.clear();
                    }
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Comment comment = snapshot.getValue(Comment.class);
                        commentsList.add(comment);
                    }
                    Log.d("msg", "Size list " + commentsList.size());
                    commentAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


}
