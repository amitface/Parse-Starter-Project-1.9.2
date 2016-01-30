package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserDetails extends Activity implements View.OnClickListener{
    //User id.
    ParseUser currentUser= ParseUser.getCurrentUser();

    ParseObject userDetail =null;

    //Path for file name
    String mCurrentPhotoPath;
    String description;
    String imageFileName;
    //Camera to take pic.
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_FILE = 2;
    private static final int REQUEST_CAMERA = 1;
    //
    static final int REQUEST_TAKE_PHOTO = 1;

    //
    ImageView mImageView =null;
    EditText descriptiontxt=null;
    //
    Bitmap bitmap=null;
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            setPic();
//            galleryAddPic();
//           // Bundle extras = data.getExtras();
//            //Bitmap imageBitmap = (Bitmap) extras.get("data");
//            //mImageView.setImageBitmap(imageBitmap);
//        }
//    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
         imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(),"User Detail error",Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
               // galleryAddPic();
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //add pic to gallery
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options  bitmapOptions = new BitmapFactory.Options();
         bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath,  bitmapOptions);
        int photoW =  bitmapOptions.outWidth;
        int photoH =  bitmapOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
         bitmapOptions.inJustDecodeBounds = false;
         bitmapOptions.inSampleSize = scaleFactor;
         bitmapOptions.inPurgeable = true;
        bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,  bitmapOptions);
        mImageView.setImageBitmap(bitmap);
    }
    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        bitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(bitmap);
        //mVideoUri = null;
       // mImageView.setVisibility(View.VISIBLE);
        //mVideoView.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        descriptiontxt=(EditText)findViewById(R.id.editText);
        mImageView=(ImageView)findViewById(R.id.imageView_round);


        //get objectId
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserDetails");
        query.whereEqualTo("userId", currentUser.getUsername().toString());

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    userDetail = (ParseObject) parseObject;
                    //Log.d("score", "Retrieved " + list.get(0).getObjectId() + " scores");
                    descriptiontxt.setText(parseObject.getString("description"));

                    ParseFile profilePhoto = (ParseFile) parseObject.get("profilephoto");
                    profilePhoto.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, com.parse.ParseException e) {
                            if (e == null) {
                                // data has the bytes for the resume
                                bitmap = BitmapFactory
                                        .decodeByteArray(
                                                bytes, 0,
                                                bytes.length);
                                mImageView.setImageBitmap(bitmap);
                            } else {
                                // something went wrong
                            }
                        }
                    });

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

                //Set this.
                mImageView.setOnClickListener(this);

        Button btnSave=(Button)findViewById(R.id.saveDetail);
        btnSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imageView_round:
//                dispatchTakePictureIntent();
                    selectImage(1);
                break;
            case R.id.saveDetail:
                description=descriptiontxt.getText().toString();
                // Upload the image into Parse Cloud
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile( imageFileName, image);

                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if(e==null)
                        {
                            Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

                    }
                });
                userDetail.put("userId",currentUser.getUsername().toString());
                userDetail.put("profilephoto", file);
                userDetail.put("description",description);
                userDetail.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if(e==null)
                        {
                            Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

                    }
                });
                Intent intent=new Intent();
                //intent.putExtra("MESSAGE", message);
                setResult(2, intent);
                finish();//finishing activity
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        imageFileName=System.currentTimeMillis() + ".jpg";
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

//                File destination = new File(Environment.getExternalStorageDirectory(),imgName);
//                byte[] image = bytes.toByteArray();

//                // Create the ParseFile
//                ParseFile file = new ParseFile( imgName, image);
//
//                file.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(com.parse.ParseException e) {
//                        if (e == null) {
//                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
//                        } else
//                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
//
//                    }
//                });
//
//                query.put("pic", file);
//                query.put("userId", currentUser.getObjectId().toString());
//                galleryAddPic();
//                FileOutputStream fo;
//                try {
//                    destination.createNewFile();
//                    fo = new FileOutputStream(destination);
//                    fo.write(bytes.toByteArray());
//                    fo.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                mImageView.setImageBitmap( bitmap);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
//                imageFileName=selectedImagePath;
                Log.d("file path- ",imageFileName);
               
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                 bitmap = BitmapFactory.decodeFile(selectedImagePath, options);

                //Convert image to bytesArrayoutputstream.
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


//                byte[] image = bytes.toByteArray();
//                // Create the ParseFile
//                ParseFile file = new ParseFile(imgName , image);
//
//                file.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(com.parse.ParseException e) {
//                        if (e == null) {
//                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
//                        } else
//                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
//
//                    }
//                });

//                query.put("pic", file);
//                query.put("userId", currentUser.getObjectId().toString());

                mImageView.setImageBitmap( bitmap);
            }

        }
//        query.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(com.parse.ParseException e) {
//                if(e==null)
//                {
//                    Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
//                }
//                else
//                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
//
//            }
//        });
    }


    private void selectImage(final int actionCode) {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetails.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, actionCode);
                    }

//                    startActivityForResult(intent, actionCode);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
