package com.example.a6;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.InputStream;
import java.util.Random;

public class EditPhotoPicture extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private Uri filepath;
    private ImageView img;
    private Button browse, save;
    private Bitmap bitmap;
    private String who;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo_picture);

        img = findViewById(R.id.photoedit);
        browse = findViewById(R.id.btnbrowse);
        save = findViewById(R.id.btnsave);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String login = sharedPreferences.getString(KEY_LOGIN, null);
        who = sharedPreferences.getString(KEY_LOGED, null);

        DatabaseReference uidRef = databaseReference.child(who).child(login).child("pimage");
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String picprof = task.getResult().getValue(String.class);

                        if(picprof != null){
                            Transformation transformation = new RoundedTransformationBuilder()
                                    .borderColor(Color.WHITE)
                                    .borderWidthDp(1)
                                    .cornerRadiusDp(100)
                                    .oval(true)
                                    .build();

                            Picasso.get()
                                    .load(picprof)
                                    .fit()
                                    .transform(transformation)
                                    .into(img);
                        }
                    }
                }
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(EditPhotoPicture.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Wybierz zdjęcie"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadtofirebase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            filepath=data.getData();
            try{
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            }catch (Exception e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadtofirebase() {
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();
//        DatabaseReference uidRef = databaseReference.child(who).child(login).child("pimage");
//        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                        String picprof = task.getResult().getValue(String.class);
//                    StorageReference deleteOldImgReference = FirebaseStorage.getInstance().getReference().child(picprof);
//                    deleteOldImgReference.delete();
//
//
//                }
//            }
//        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference(login);

        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                dialog.dismiss();

                                String login = sharedPreferences.getString(KEY_LOGIN, null);

                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference root = db.getReference(who);

                                root.child(login).child("pimage").setValue(uri.toString());

                                img.setImageResource(R.drawable.profilepic);
                                Toast.makeText(EditPhotoPicture.this, "Zmieniono", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :"+(int)percent+" %");

                    }
                });



    }
}