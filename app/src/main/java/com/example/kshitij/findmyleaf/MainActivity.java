package com.example.kshitij.findmyleaf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private HomeFragment homeFragment;
    private ObservationsFragment observationFragment;
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        homeFragment = new HomeFragment();
        observationFragment = new ObservationsFragment();

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_home:

                        setFragment(homeFragment);

                        return true;

                    case R.id.nav_obv:

                        setFragment(observationFragment);

                        return true;

                    default:

                        return false;
                }

            }
        });

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageView.setImageBitmap(imageBitmap);
                Log.d("TAGGER", "PHOTO AGAYI ");
                galleryAddPic();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onFab(View view) {
        dispatchTakePictureIntent_2();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent_2() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.kshitij.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void uploadImageInMultipartRequest() {


//            RequestBody idPart = RequestBody.create(MultipartBody.FORM, token);
//
//            RequestBody titlePart = RequestBody.create(MultipartBody.FORM, notification_title.getText().toString());
//
//            RequestBody messagePart = RequestBody.create(MultipartBody.FORM, notification_msg.getText().toString());
//
//            RequestBody datePart = RequestBody.create(MultipartBody.FORM, getTodaysDate());
//
//            RequestBody imagePart = RequestBody.create(MultipartBody.FORM, image);
//
//            RequestBody _vPart = RequestBody.create(MultipartBody.FORM, 0 + "");
//
//            MultipartBody.Part myImage = MultipartBody.Part.createFormData("image", image.getName(), imagePart);
//        Log.d("IMAGENAMEresponse", image.getName());

        RequestBody epochPart = RequestBody.create(MultipartBody.FORM, System.currentTimeMillis() + "");

//        String _photo = (Uri.parse(photoURI.toString()) == null) ? "" : Uri.parse(photoURI.toString());
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://dptrackingapp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient userClient = retrofit.create(UserClient.class);
//            progress = new ProgressDialog(MainActivity.this);
//            progress.setTitle("Loading");
//            progress.setMessage("Broadcasting Notification....");
//            progress.setCancelable(false);
//            progress.show();

        Call<ImageUploadResponse> call = userClient.uploadImageWithData("token for authentication", "idPart", epochPart, "messagePart", "datePart", "myImage", "_vPart", "titlePart");
        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                if (response.isSuccessful()) {
//                        progress.dismiss();
//
//                        notification_title.setText("");
//                        notification_msg.setText("");
//                        notification_send.setEnabled(false);
//                        notification_send.setAlpha(0.3f);
//                        notification_display_image.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.drawable_notification));
//                        Toast.makeText(MainActivity.this, "Successfully sent", Toast.LENGTH_SHORT).show();
                } else {
//                        progress.dismiss();

                    Toast.makeText(MainActivity.this, "Not posted on server", Toast.LENGTH_SHORT).show();
                }
//                    Log.d("TAGGERResponse", token);
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
//                progress.dismiss();
                Toast.makeText(MainActivity.this, "Oops, something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
//        System.out.println(call);
    }
}

