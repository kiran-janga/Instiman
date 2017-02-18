package com.andro.jk.metisandroid1.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.andro.jk.metisandroid1.BitmapScaler;
import com.andro.jk.metisandroid1.Dummy;
import com.andro.jk.metisandroid1.Manifest;
import com.andro.jk.metisandroid1.Models.CategoryModel;
import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.R;
import com.andro.jk.metisandroid1.SaveSharedPreference;
import com.andro.jk.metisandroid1.WebUtils.RestClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class ComplaintActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView complaintUploadBtn;
    ImageView complaintImage;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PICK_PHOTO_CODE = 55;
    String photoFileName;
    String updatedFileName;
    public final String APP_TAG = "Metis";
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    RadioGroup ucRadioGroup;
    boolean newComplaint = true;
    Boolean sp = true;
    Boolean sp2 = true;
    Boolean spb = true;

    final List<String> categories = new ArrayList<String>();
    final List<Integer> categoryIds = new ArrayList<Integer>();


    int catId;
    Spinner spinner;


    EditText etlocation;
    EditText etDescription, etTitle,etNumber;
    String strDescription, strLocation, area,strTitle, strNumber;
    Uri uploadImageUri = null;
    TypedFile typedFile = null;

    ProgressDialog loading;
    Context c;


    String token;
    String access_token;
    String access_token_wo_quotes;
    String s;

    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint);
        c = this;

        loading = new ProgressDialog(c);
        loading.setMessage("Fetching Data");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setIndeterminate(true);
        loading.show();

        Log.d("avi21", "getting here");

        spinner = (Spinner) findViewById(R.id.spinner110);
        spinner.setOnItemSelectedListener(this);

        RestClient.get().getCategories(s, new Callback<List<CategoryModel>>() {

            @Override
            public void failure(RetrofitError error) {
                loading.dismiss();
                Toast.makeText(c,"Error fetching data, Try after some time",Toast.LENGTH_SHORT).show();
                finish();

            }
            @Override
            public void success(List<CategoryModel> categoryModels, Response response) {
                //categories.add("Select Category");
                categories.add("Select Category");
                //categoryIds.add(0);

                for (CategoryModel categoryModel : categoryModels) {
                    categories.add(categoryModel.getCategory());

                    categoryIds.add(categoryModel.getCategoryId());
                    Log.d("msg1", categories.toString());
                    Log.d("msg2", categoryIds.toString());
                }

                dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(dataAdapter);

                loading.dismiss();


//                loading.dismiss();

                //spinner.setPrompt("Select Category");




        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (sp) {
                    categories.remove("Select Category");
                    //categoryIds.remove(0);
                    dataAdapter.notifyDataSetChanged();
                    sp = false;
                }

                spb = false;

                return false;

            }

        });

        token = "Token ";
        access_token = SaveSharedPreference.getAccessToken(ComplaintActivity.this);
        access_token_wo_quotes = access_token.replace("\"", "");
        s = token.concat(access_token_wo_quotes);









        //dataAdapter.notifyDataSetChanged();

//        loading = new ProgressDialog(this);
//
//        loading.setMessage("Loding Your History");
//        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        loading.setIndeterminate(true);
//        loading.show();


        complaintImage = (ImageView) findViewById(R.id.complaintImage);
        complaintUploadBtn = (ImageView) findViewById(R.id.complaintUploadBtn);
        etDescription = (EditText) findViewById(R.id.etDescription);
                etNumber = (EditText) findViewById(R.id.etNumber);
        etlocation = (EditText) findViewById(R.id.etlocation);
        etTitle = (EditText) findViewById(R.id.etTitle);



//        ucRdb1 = (RadioButton) findViewById(R.id.ucRdb1);
//        ucRdb2 = (RadioButton) findViewById(R.id.ucRdb2);
//
//        ucRadioGroup = (RadioGroup) findViewById(R.id.ucRadioGroup);




        if (getIntent().hasExtra("object")) {

            HistoryModel dicardedComplaint = (HistoryModel) getIntent().getSerializableExtra("object");
            etDescription.setText(dicardedComplaint.getDescription());
            etlocation.setText(dicardedComplaint.getLocation());
            etTitle.setText(dicardedComplaint.getTitle());

            Picasso.with(getApplicationContext()).load(getResources().getString(R.string.IMAGE_URL) + dicardedComplaint.getImage()).resize(100, 100);
            newComplaint = false;

        }




//
//        ucRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
////                if (checkedId == R.id.rdb1){
////                    QuestionActivity.answersList[id-1] = rdb1.getText().toString();
////                }else if(checkedId == R.id.rdb2) {
////                    QuestionActivity.answersList[id-1] = rdb2.getText().toString();
////                }else if(checkedId == R.id.rdb3) {
////                    QuestionActivity.answersList[id-1] = rdb3.getText().toString();
////                }else if(checkedId == R.id.rdb4) {
////                    QuestionActivity.answersList[id-1] = rdb4.getText().toString();
////                }
//
//            }
//        });

        complaintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoFileName = System.currentTimeMillis() + ".jpg";
                updatedFileName = System.currentTimeMillis() + "up" + ".jpg";
                selectImage();

            }
        });


        complaintUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strDescription = etDescription.getText().toString();
                strLocation = etlocation.getText().toString();
                strTitle = etTitle.getText().toString();
                strNumber = etNumber.getText().toString();

                if (spb) {
                    Toast.makeText(ComplaintActivity.this, "Please Select Category", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strDescription.isEmpty()){
                    etDescription.setError("Description empty");
                    return;
                }
                if(strLocation.isEmpty()){
                    etlocation.setError("Location Empty");
                    return;
                }

                if(strTitle.isEmpty()){
                    etTitle.setError("Description Empty");
                    return;
                }

                loading = new ProgressDialog(ComplaintActivity.this);

                loading.setMessage("Sending...");
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.setIndeterminate(true);
                loading.show();




                if (newComplaint) {

                    RestClient.get().uploadImageComplaint(s, strTitle, typedFile, catId, strLocation, strDescription, strNumber,new Callback<Dummy>() {
                        @Override
                        public void success(Dummy dummy, Response response) {

                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                            Bitmap submitImage = ((BitmapDrawable) complaintImage.getDrawable()).getBitmap();
                            loading.dismiss();
                            Toast.makeText(ComplaintActivity.this, "Complaint Uploaded", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(ComplaintActivity.this, FirstActivity.class);
                            startActivity(i);


                        }

                        @Override
                        public void failure(RetrofitError error) {

                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            loading.dismiss();
                            Toast.makeText(ComplaintActivity.this, "Failed To Upload", Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {
                    RestClient.get().updateComplaint(s, strTitle, typedFile, catId, strLocation, strDescription, new Callback<Dummy>() {
                        @Override
                        public void success(Dummy dummy, Response response) {


                            Bitmap submitImage = ((BitmapDrawable) complaintImage.getDrawable()).getBitmap();
                            loading.dismiss();
                            Toast.makeText(ComplaintActivity.this, "Complaint Uploaded", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(ComplaintActivity.this, FirstActivity.class);
                            startActivity(i);


                        }

                        @Override
                        public void failure(RetrofitError error) {

                            loading.dismiss();
                            Toast.makeText(ComplaintActivity.this, "Failed To Upload", Toast.LENGTH_SHORT).show();

                        }
                    });
                }


            }
        });



            }


        });
    }


    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {

                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }


    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        getPermissionToReadUserContacts();

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);

                uploadImageUri = takenPhotoUri;

                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());


                Bitmap takenImagef1 = BitmapScaler.scaleToFitWidth(takenImage, 600);
                Bitmap takenImagef2 = BitmapScaler.scaleToFitHeight(takenImagef1, 600);

                complaintImage.setImageBitmap(takenImagef2);

                File filesDir = getApplicationContext().getFilesDir();
                File filename = new File(filesDir, updatedFileName + ".jpg");

                OutputStream os;
                try {
                    os = new FileOutputStream(filename);
                    takenImagef2.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }

                typedFile = new TypedFile("multipart/form-data",filename);


            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_PHOTO_CODE) {
            if (resultCode == RESULT_OK) {


                if (data != null) {


                    Uri photoUri = data.getData();

                    Bitmap takenImage = null;
                    try {
                        takenImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    Bitmap takenImagef1 = BitmapScaler.scaleToFitWidth(takenImage, 600);
                    Bitmap takenImagef2 = BitmapScaler.scaleToFitHeight(takenImagef1, 600);
                    complaintImage.setImageBitmap(takenImagef2);

                    File filesDir = getApplicationContext().getFilesDir();
                    File filename = new File(filesDir, updatedFileName + ".jpg");

                    OutputStream os;
                    try {
                        os = new FileOutputStream(filename);
                        takenImagef2.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.flush();
                        os.close();
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                    }

                    typedFile = new TypedFile("multipart/form-data",filename);


                }
            } else { // Result was a failure
                Toast.makeText(this, "Error choosing picture!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name

                    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                    // So as long as the result is not null, it's safe to use the intent.
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Start the image capture intent to take photo
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                } else if (items[i].equals("Choose from Gallery")) {
                    // Create intent for picking a photo from the gallery
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                    // So as long as the result is not null, it's safe to use the intent.
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Bring up gallery to select a photo
                        startActivityForResult(intent, PICK_PHOTO_CODE);
                    }

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }


            }
        });

        builder.show();
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.d("msg5", "fired");
        catId = categoryIds.get(position);
        Log.d("msg3", Integer.toString(catId));
        Log.d("msg4", Integer.toString(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Log.d("msg6", "not_fired");
    }


    public void getPermissionToReadUserContacts() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // showRationale = false if user clicks Never Ask Again, otherwise true
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);

                if (showRationale) {
                    // do something here to handle degraded mode

                } else {
                    Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




}