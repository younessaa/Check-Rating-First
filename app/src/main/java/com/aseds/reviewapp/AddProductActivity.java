package com.aseds.reviewapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;


import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    private Uri imageUri;
    private String imageUrl;

    private ImageView close;
    private ImageView imageAdded;
    EditText name;
    EditText price;
    EditText storeName;
    EditText storeLocation;
    EditText categorie;
    Button addProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        close = findViewById(R.id.close_image);
        imageAdded = findViewById(R.id.image_added_product);
        addProduct = findViewById(R.id.addProdcut);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        storeName = findViewById(R.id.storeName);
        storeLocation = findViewById(R.id.storeLocation);
        categorie = findViewById(R.id.categorie);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddProductActivity.this , MainActivity.class));
                finish();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        CropImage.activity().start(AddProductActivity.this);
    }

    private void upload() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("Products").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            StorageTask<UploadTask.TaskSnapshot> uploadtask = filePath.putFile(imageUri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
                    String productId = ref.push().getKey();

                    HashMap<String , Object> map = new HashMap<>();
                    map.put("productid" , productId);
                    map.put("imageurl" , imageUrl);
                    map.put("name" , name.getText().toString());
                    map.put("price" , Double.parseDouble(price.getText().toString()));
                    map.put("rating" , new Double(0));
                    map.put("storeName" , storeName.getText().toString());
                    map.put("storeLocation" , storeLocation.getText().toString());
                    map.put("categorie" , categorie.getText().toString());

                    ref.child(productId).setValue(map);

                    pd.dismiss();
                    startActivity(new Intent(AddProductActivity.this , MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No image was selected!", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageAdded.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddProductActivity.this , MainActivity.class));
            finish();
        }
    }

}
