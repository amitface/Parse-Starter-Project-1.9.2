package com.parse.snapstar;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by root on 15/12/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Profile extends Fragment implements View.OnClickListener{
    //Camera to take pic.

    private static final int SELECT_FILE = 2;
    private static final int REQUEST_CAMERA = 1;
    private static final int RESULT_OK =-1 ;
    //



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
    ImageView mImageView = null;
    TextView descriptiontxt = null;
    //
    Bitmap bitmap = null;

    ListView listView=null;

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


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        View header = inflater.inflate(R.layout.header_listview, null);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeview);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                CustomAdapterProfile customAdapter =new CustomAdapterProfile(getActivity(), Arrays.asList(currentUser.getUsername()));
                listView.setAdapter(customAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        TextView user = (TextView)header.findViewById(R.id.user);
        user.setText(currentUser.getUsername());

        Button editButton = (Button)header.findViewById(R.id.editButton);
        editButton.setOnClickListener(this);

//        TextView followers=(TextView)header.findViewById(R.id.followers);
//        TextView following=(TextView)header.findViewById(R.id.following);
//
//        followers.setOnClickListener(this);
//        following.setOnClickListener(this);

//        descriptiontxt=(TextView)rootView.findViewById(R.id.descriptiontxt);
//        mImageView=(ImageView)rootView.findViewById(R.id.imageView);
//        mImageView.setOnClickListener(this);
        mImageView = (ImageView)header.findViewById(R.id.imageView);
        mImageView.setOnClickListener(this);
//        descriptiontxt.setText(currentUser.getUsername().toString());
        ImageButton imageButton=(ImageButton)rootView.findViewById(R.id.options);

        //get objectId
        listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.addHeaderView(header);
        CustomAdapterProfile customAdapter =new CustomAdapterProfile(getActivity(), Arrays.asList(currentUser.getUsername()));
        listView.setAdapter(customAdapter);


        ParseFile profilePhoto=currentUser.getParseFile("photoprofile");
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
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserDetails");
//        query.whereEqualTo("userId", currentUser.getUsername().toString());
//
//        query.getFirstInBackground(new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject parseObject, com.parse.ParseException e) {
//                if (e == null) {
//                    userDetail = (ParseObject) parseObject;
//
//
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });
        return rootView;
    }

    public void remove(int position){
        listView.getItemAtPosition(position);

    }


    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case  R.id.editButton :
                Intent intent =new Intent(getActivity(),UserDetails.class);

                startActivityForResult(intent,1);
            break;
            case R.id.imageView :
                selectImage(1);
                break;
//            case R.id.followers:
//                Intent intentfollowers =new Intent(getActivity(),followers.class);
//
//                startActivity(intentfollowers);
//            break;
//            case R.id.following:
//                Intent intentfollowing =new Intent(getActivity(),following.class);
//
//                startActivity(intentfollowing);
//                break;
//            case R.id.followersText:
//                Intent intentfollowersText =new Intent(getActivity(),followers.class);
//
//                startActivity(intentfollowersText);
//                break;
//            case R.id.followingText:
//                Intent intentfollowingText =new Intent(getActivity(),following.class);
//
//                startActivity(intentfollowingText);
//                break;
            case R.id.imageButton:
                Dialog dialog= new Dialog(getActivity());
                dialog.setCanceledOnTouchOutside(true);

                break;
            default:
                break;
        }
    }


    private void selectImage(final int actionCode) {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        ParseUser parseUser=ParseUser.getCurrentUser();

        imageFileName=System.currentTimeMillis() + ".jpg";
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                bitmap=cropToSquare(bitmap);
                mImageView.setImageBitmap(bitmap);

                byte[] image = bytes.toByteArray();
                ParseFile file = new ParseFile(image);
                file.saveInBackground();
                parseUser.put("photoprofile", file);
            } else if (requestCode == SELECT_FILE && data !=null) {
                Uri selectedImageUri = data.getData();
                Bitmap bitmap;


                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(getActivity(), selectedImageUri, projection, null, null,
                        null);
                try {
                    Cursor cursor = cursorLoader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);


                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
//                imageFileName=selectedImagePath;
                Log.d("file path- ",imageFileName);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 150;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        || options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
//               bitmap=BitmapResized(selectedImagePath);
                cursor.close();
                //Convert image to bytesArrayoutputstream.
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                   ;
                    mImageView.setImageBitmap( bitmap);

                    byte[] image = bytes.toByteArray();
                    ParseFile file = new ParseFile(image);
                    file.saveInBackground();
                    parseUser.put("photoprofile", file);
                }catch (NullPointerException e)
                {
                    Toast.makeText(getActivity(),"Unable to load image",Toast.LENGTH_LONG).show();
                }
            }
            final ParseUser finalparseUser=parseUser;
            final ProgressDialog progressDialog=new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    finalparseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }).start();

        }
    }

    private Bitmap BitmapResized(String selectedImagePath)
    {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 150;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                || options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
        bitmap=cropToSquare(bitmap);

        return bitmap;
    }

    public static Bitmap cropToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

}
