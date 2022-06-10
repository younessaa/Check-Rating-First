package com.aseds.reviewapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.aseds.reviewapp.Model.Comment;
import com.aseds.reviewapp.Model.User;
import com.aseds.reviewapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Comment> mComments;

    String productId;

    private FirebaseUser fUser;

    public CommentAdapter(Context mContext, List<Comment> mComments , String productId) {


        this.mContext = mContext;
        this.mComments = mComments;
        this.productId = productId;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        final Comment comment = mComments.get(position);
        if(comment != null && comment.getPublisher() != null) {
            holder.comment.setText(comment.getComment());
            holder.date.setText(comment.getDate());

            FirebaseDatabase.getInstance().getReference().child("Users").child(comment.getPublisher()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    holder.username.setText(user.getUsername());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (comment.getPublisher().endsWith(fUser.getUid())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setTitle("Do you want to delete?");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("Comments")
                                        .child(productId).child(comment.getCommentid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(mContext, "Comment deleted successfully!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }
                        });

                        alertDialog.show();
                    }

                    return true;
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView comment;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.comment_title);
            comment = itemView.findViewById(R.id.comment_body);
            date = itemView.findViewById(R.id.comment_date);
        }
    }

}
