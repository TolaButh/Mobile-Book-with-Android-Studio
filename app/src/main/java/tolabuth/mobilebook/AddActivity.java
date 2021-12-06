package tolabuth.mobilebook;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import tolabuth.mobilebook.dao.SQLiteHelper;
import tolabuth.mobilebook.model.Contact;

public class AddActivity extends AppCompatActivity {
    private TextInputEditText edtName, edtMobile;
    private MaterialButton btnAdd;
    private CircleImageView imgProfile;
    ConstraintLayout layout;
    private static final int CAMERA_REQUEST_CODE = 103;
    private static final int STORAGE_REQUEST_CODE = 104;
    private static final int IMAGE_PICK_CAMERA_CODE = 105;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 106;
    String[] cameraPermission ;
    String[] storagePermission;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        matchView();
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String mobile = edtMobile.getText().toString();


                if ((name.isEmpty()) || (mobile.isEmpty())){
                    Toast.makeText(AddActivity.this, "Please fill data before click button", Toast.LENGTH_LONG).show();

                }else {
                    //Contact contact = new Contact(name,mobile,imageUri+"");
                    Contact contact = new Contact(name, mobile, imageUri+"");
                    SQLiteHelper db = new SQLiteHelper(AddActivity.this);
                    boolean result = db.insert(contact);
                    if (result){
                        Toast.makeText(AddActivity.this, "Insert data Successful", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(AddActivity.this, "Insert data Unsuccessful", Toast.LENGTH_LONG).show();
                    }
                    clearText();
                }
            }
        });
        edtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    hideKeyboard(view);

                }
            }
        });
        edtMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    hideKeyboard(view);

                }
            }
        });
    //end keyboard
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
            }
        });

    }

    private void imagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //handle click
                if(i==0){
                    //camera click
                    if (!checkCameraPermission()){
                        requestCameraPermision();
                    }else {
                        //permission already
                        pickFromCamera();
                    }
                }else if (!checkStoragePermission()){
                    requestStoragePermision();
                }else {
                    // permission already
                    pickFromGallery();
                }

            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length>0) {
                    //if permision it true
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean StorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && StorageAccepted){
                        // both permission allowed
                        pickFromCamera();
                    }else {
                        Toast.makeText(AddActivity.this, "Camera & Storage is permision required",Toast.LENGTH_LONG).show();
                    }
                }
            }

            break;
            case STORAGE_REQUEST_CODE:{

                if (grantResults.length>0){
                    boolean StorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (StorageAccepted){
                        // storage permission allowed
                        pickFromGallery();
                    }else {
                        Toast.makeText(AddActivity.this, "Storage is permision required.....",Toast.LENGTH_LONG).show();
                    }
                }
            }

            break;

        }
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_REQUEST_CODE);
    }

    private void pickFromCamera() {
        // intent image from pick camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Description");
        //put image uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //image to open camera for image
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);


    }

    //check permission
    private boolean checkStoragePermission(){
        //check permission if enable or not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return  result;
    }
    //check storage permission
    private void requestStoragePermision(){
        //check request permision if enable or not
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }
    // check camera permission
    private boolean checkCameraPermission(){
        //check camera permission if enable or not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermision(){
        //check request permision if enable or not
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }else if(requestCode == IMAGE_PICK_CAMERA_CODE){

                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;


                    imgProfile.setImageURI(resultUri);


                }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                    Toast.makeText(AddActivity.this, "Error"+error, Toast.LENGTH_LONG).show();

                }

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void clearText() {
        edtName.setText("");
        edtMobile.setText("");
        edtName.requestFocus();
        imgProfile.setImageResource(R.drawable.profile);

    }


    private void matchView() {
        imgProfile = findViewById(R.id.img_profile);
        edtName = findViewById(R.id.ip_edt_name);
        edtMobile = findViewById(R.id.ip_edt_mobile);
        btnAdd = findViewById(R.id.btn_add);
        layout = findViewById(R.id.constraint_layout);

    }
}