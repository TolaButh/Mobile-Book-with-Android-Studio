package tolabuth.mobilebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import tolabuth.mobilebook.dao.SQLiteHelper;
import tolabuth.mobilebook.model.Contact;

public class UpdateAndDeleteActivity extends AppCompatActivity {
    private TextInputEditText edtName, edtMobile;
    private CircleImageView imgvUpdateProfile;
    private MaterialButton btnUpdate,bntDelete;
    private static final int CAMERA_REQUEST_CODE = 108;
    private static final int STORAGE_REQUEST_CODE = 109;
    private static final int IMAGE_PICK_CAMERA_CODE = 110;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 111;
    String[] cameraPermission ;
    String[] storagePermission;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        matchView();
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};



        //end update image
        Intent intent =this.getIntent();
        int id = intent.getIntExtra("id",0);
        String name = intent.getStringExtra("name");
        String mobile = intent.getStringExtra("mobile");


        edtName.setText(name);
        edtMobile.setText(mobile);
        String image = intent.getStringExtra("image");
        imgvUpdateProfile.setImageURI(Uri.parse(image));

        /// update image

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAndDeleteActivity.this);
        builder.setTitle("Image Choose")
                .setMessage("Do you want to update or no ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Contact contact = new Contact(id,edtName.getText().toString(),edtMobile.getText().toString(),imageUri+"");
                                SQLiteHelper db = new SQLiteHelper(UpdateAndDeleteActivity.this);
                                boolean result = db.update(contact);
                                if(result){
                                    Toast.makeText(UpdateAndDeleteActivity.this,
                                            "Update contact Successfully!!!",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(UpdateAndDeleteActivity.this,
                                            "Unable to update contact Successfully!!!",Toast.LENGTH_SHORT).show();
                                }
                                clearText();

                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String image = intent.getStringExtra("image");

                                Contact contact = new Contact(id,edtName.getText().toString(),edtMobile.getText().toString(),image);
                                SQLiteHelper db = new SQLiteHelper(UpdateAndDeleteActivity.this);
                                boolean result = db.update(contact);
                                if(result){
                                    Toast.makeText(UpdateAndDeleteActivity.this,
                                            "Update contact Successfully!!!",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(UpdateAndDeleteActivity.this,
                                            "Unable to update contact Successfully!!!",Toast.LENGTH_SHORT).show();
                                }
                                clearText();

                            }
                        });
                    }
                });

        //create dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        //click image
        imgvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
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
        bntDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteDialog(id);

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
                        Toast.makeText(UpdateAndDeleteActivity.this, "Camera & Storage is permision required",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(UpdateAndDeleteActivity.this, "Storage is permision required.....",Toast.LENGTH_LONG).show();
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

                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    imgvUpdateProfile.setImageURI(resultUri);

                }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                    Toast.makeText(UpdateAndDeleteActivity.this, "Error"+error, Toast.LENGTH_LONG).show();

                }

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void confirmDeleteDialog(int id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(UpdateAndDeleteActivity.this);
        alert.setTitle("Confirmed Message ");
        alert.setMessage("Do you want to delete contact?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SQLiteHelper db = new SQLiteHelper(UpdateAndDeleteActivity.this);
                boolean result = db.delete(id);
                if (result){
                    Toast.makeText(UpdateAndDeleteActivity.this,
                            "Delete contact Successfully!!!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(UpdateAndDeleteActivity.this,
                            "Unable to delete Contact Successfully!!",Toast.LENGTH_SHORT).show();
                }
                clearText();
            }
        });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            alert.create().show();

    }

    private void clearText() {
        Intent intent = new Intent(UpdateAndDeleteActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void matchView() {
        edtName = findViewById(R.id.edt_name);
        edtMobile = findViewById(R.id.edt_mobile);
        btnUpdate = findViewById(R.id.btn_update);
        bntDelete = findViewById(R.id.btn_delete);
        imgvUpdateProfile = findViewById(R.id.img_update_profile);
    }

    //hide keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}