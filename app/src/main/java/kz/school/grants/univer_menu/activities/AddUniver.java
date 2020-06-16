package kz.school.grants.univer_menu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import kz.school.grants.R;
import kz.school.grants.news_menu.NewsFeed;


public class AddUniver extends AppCompatActivity implements View.OnClickListener {
    CardView cardView;
    private final int BOOK_QR_SCANNER = 101;
    private static final int PERMISSION_REQUEST_CODE = 200;

    StorageReference storageReference;
    File file;
    Uri fileUri;

    ImageView newsImage;
    TextView changeIt;
    EditText newsTitle, newsDesc;
    ProgressBar progressBar;
    Button addNews;

    DatabaseReference databaseReference;
    Uri last_file_uri;
    boolean photoSelected = false;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_news);

        initViews();

    }

    public void initViews() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add News");

        storageReference = FirebaseStorage.getInstance().getReference();
        file = null;
        fileUri = null;
        cardView = findViewById(R.id.takePhoto);
        newsImage = findViewById(R.id.newsImage);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        addNews = findViewById(R.id.addNews);

        newsTitle = findViewById(R.id.newsTitle);
        newsDesc = findViewById(R.id.newsDesc);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        addNews.setOnClickListener(this);
        cardView.setOnClickListener(this);
    }

    public void startTakeImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.takePhoto:
                Log.i("AddNews", "takePhoto");
                if (checkPermission()) {
                    startTakeImage();
                } else {
                    requestPermission();
                }
                break;

            case R.id.addNews:

                if (newsTitle.getText().toString().trim().equals("")) {
                    newsTitle.setError(getString(R.string.fill_all));
                    return;
                }

                if (newsDesc.getText().toString().trim().equals("")) {
                    newsDesc.setError(getString(R.string.fill_all));
                    return;
                }


                if (photoSelected) {
                    uploadImage();
                }else{
                    uploadNews();
                }

                break;
        }
    }

    String downloadUri = "url";

    private void uploadImage() {
        if (fileUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.setTitle(getResources().getString(R.string.photoLoading));
            progressDialog.show();

            final String imageStorageName = UUID.randomUUID().toString();
            final String photoPath = "book_images/" + imageStorageName;
            final StorageReference ref = storageReference.child(photoPath);
            ref.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            downloadUri = ref.getDownloadUrl().toString();
                            uploadNews();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddUniver.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(getString(R.string.uploaded) + (int) progress + "%");
                        }
                    });
        } else {
            Toast.makeText(this, getString(R.string.image_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadNews(){
        String fkey = getFId();
        String nTitle = newsTitle.getText().toString();
        String nDesc = newsDesc.getText().toString();

        DateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
        String date = dateF.format(Calendar.getInstance().getTime());

        NewsFeed newsFeed = new NewsFeed(fkey, nTitle, nDesc, date, downloadUri, 0, 0);
        databaseReference.child("news").child(fkey).setValue(newsFeed);

        addNews.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(AddUniver.this, getString(R.string.news_added), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                fileUri = result.getUri();
                newsImage.setImageURI(fileUri);
                changeIt.setText(getString(R.string.change_image));
                changeIt.setError(null);

                photoSelected = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == 10) {
            newsImage.setImageURI(last_file_uri);
        }
    }

    public String getFId() {
        Date date = new Date();
        String idN = "i" + date.getTime();
        return idN;
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

}
