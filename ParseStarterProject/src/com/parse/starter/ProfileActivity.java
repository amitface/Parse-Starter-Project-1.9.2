package com.parse.starter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends Activity implements View.OnClickListener{

    String username;

    //Camera to take pic.
    private static final int SELECT_FILE = 2;
    private static final int REQUEST_CAMERA = 1;
    private static final int RESULT_OK =-1 ;

    ImageView mImageView;Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        View header = getLayoutInflater().inflate(R.layout.header_listview, null);
        username = getIntent().getStringExtra("user");





        TextView user=(TextView)header.findViewById(R.id.user);
        user.setText(username);

        Button editButton=(Button)header.findViewById(R.id.editButton);
        editButton.setText("follow");


//        TextView followers=(TextView)header.findViewById(R.id.followers);
//        TextView following=(TextView)header.findViewById(R.id.following);
//
//        followers.setOnClickListener(this);
//        following.setOnClickListener(this);


        mImageView=(ImageView)header.findViewById(R.id.imageView);



        ImageButton imageButton=(ImageButton)findViewById(R.id.options);

        //get objectId
        ListView listView=(ListView) findViewById(R.id.list_view);
        listView.addHeaderView(header);
        CustomAdapterViewProfile customAdapter =new CustomAdapterViewProfile(this, Arrays.asList(username));
        listView.setAdapter(customAdapter);


        final ParseQuery<ParseUser> parseQuery =ParseQuery.getQuery("_User");
        parseQuery.whereEqualTo("username", username);
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                ParseFile profilePhoto=parseUser.getParseFile("photoprofile");
                profilePhoto.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
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
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case  R.id.editButton :
//                Intent intent =new Intent(this,UserDetails.class);
//
//                startActivityForResult(intent,1);
                break;
            case R.id.imageView :
//                selectImage(1);
                break;
//            case R.id.followers:
//                Intent intentfollowers =new Intent(this,followers.class);
//
//                startActivity(intentfollowers);
//                break;
//            case R.id.following:
//                Intent intentfollowing =new Intent(this,following.class);
//
//                startActivity(intentfollowing);
//                break;
//            case R.id.followersText:
//                Intent intentfollowersText =new Intent(this,followers.class);
//
//                startActivity(intentfollowersText);
//                break;
//            case R.id.followingText:
//                Intent intentfollowingText =new Intent(this,following.class);
//
//                startActivity(intentfollowingText);
//                break;

            default:
                break;
        }
    }


    private void selectImage(final int actionCode) {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
