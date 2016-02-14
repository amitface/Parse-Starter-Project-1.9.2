package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
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

    private LruCache<String, Bitmap> mMemoryCache;

    public static Profile newInstance(int sectionNumber) {

        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putInt("1", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

//        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap bitmap) {
//                // The cache size will be measured in kilobytes rather than
//                // number of items.
//                return bitmap.getByteCount() / 1024;
//            }
//        };

    }

//    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//        if (getBitmapFromMemCache(key) == null) {
//            mMemoryCache.put(key, bitmap);
//        }
//    }
//
//    public Bitmap getBitmapFromMemCache(String key) {
//        return mMemoryCache.get(key);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        Button editButton=(Button)rootView.findViewById(R.id.editButton);
        editButton.setOnClickListener(this);

        descriptiontxt=(TextView)rootView.findViewById(R.id.descriptiontxt);
        mImageView=(ImageView)rootView.findViewById(R.id.imageView_round);

        descriptiontxt.setText(currentUser.getUsername().toString());
        //get objectId
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserDetails");
        query.whereEqualTo("userId", currentUser.getUsername().toString());

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    userDetail = (ParseObject) parseObject;




                    ParseFile profilePhoto = (ParseFile) parseObject.get("profilephoto");
                    profilePhoto.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, com.parse.ParseException e) {
                            if (e == null) {
                                // data has the bytes for the resume
                                RoundImage roundImage= new RoundImage(decodeSampledBitmapFromResource(bytes, 70, 70));
                                mImageView.setImageDrawable(roundImage);
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

//    public void loadBitmap(int resId, ImageView imageView) {
//        final String imageKey = String.valueOf(resId);
//
//        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
//        if (bitmap != null) {
//            mImageView.setImageBitmap(bitmap);
//        } else {
//            mImageView.setImageResource(R.drawable.image_placeholder);
//            BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
//            task.execute(resId);
//        }
//    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] bytes,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory
                .decodeByteArray(
                        bytes, 0,
                        bytes.length,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,options);
    }
}
