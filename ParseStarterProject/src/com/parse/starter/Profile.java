package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by root on 15/12/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Profile extends Fragment implements View.OnClickListener{
    //User id.
    ParseUser currentUser= ParseUser.getCurrentUser();

    ParseObject userDetail =null;

    //Path for file name
    String mCurrentPhotoPath;
    String description;
    String imageFileName;
    //Camera to take pic.
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //
    static final int REQUEST_TAKE_PHOTO = 1;

    //
    ImageView mImageView =null;
    TextView descriptiontxt=null;
    //
    Bitmap bitmap=null;

    public static Profile newInstance(int sectionNumber) {

        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putInt("1", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        Button editButton=(Button)rootView.findViewById(R.id.editButton);
        editButton.setOnClickListener(this);

        descriptiontxt=(TextView)rootView.findViewById(R.id.descriptiontxt);
        mImageView=(ImageView)rootView.findViewById(R.id.imageView_round);


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
                                Bitmap bmp = BitmapFactory
                                        .decodeByteArray(
                                                bytes, 0,
                                                bytes.length);
                                mImageView.setImageBitmap(bmp);
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

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
//        if (requestCode == PICK_CONTACT_REQUEST) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                // The user picked a contact.
//                // The Intent's data Uri identifies which contact was selected.
//
//                // Do something with the contact here (bigger example below)
//            }
//        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case  R.id.editButton :
                Intent intent =new Intent(getActivity(),UserDetails.class);

                startActivityForResult(intent,1);
            break;
            default:
                break;
        }
    }
}
