package com.parse.starter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Home extends Activity implements ActionBar.TabListener ,View.OnClickListener{

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int SELECT_FILE = 2;
    private static final int REQUEST_CAMERA = 1;
    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    ImageView ivImage=null;

    ParseUser currentUser = ParseUser.getCurrentUser();
    String amit=currentUser.getUsername();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (currentUser != null) {
            // do stuff with the user
        } else {
            // show the signup or login screen
            Intent intent =new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        //Camera Action
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        ivImage=(ImageView)findViewById(R.id.camera);
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        if (currentUser != null) {
            // do stuff with the user
            //Toast.makeText(getApplication(),"restart",Toast.LENGTH_SHORT).show();
        } else {
            // show the signup or login screen
            Toast.makeText(this,"restart Home",Toast.LENGTH_LONG).show();
            Intent intent =new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id== R.id.action_logout)
        {
            ParseUser.logOut();
           currentUser= ParseUser.getCurrentUser();
     //       Toast.makeText(this,currentUser.getObjectId().toString(),Toast.LENGTH_LONG).show();
            if(currentUser==null) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position)
            {
                case 0: return UserHome.newInstance(position+1);
                case 1: return SearchList.newInstance(position + 1);
                case 2:return Profile.newInstance(position+1);
                default:return PlaceholderFragment.newInstance(position + 1);
        }
    }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Home".toUpperCase(l);
                case 1:
                    return "Search".toUpperCase(l);
                case 2:
                    return "Profile".toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }
    }
//    @Override
//    public void onBackPressed() {
//
//        moveTaskToBack(true);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.camera:

//                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                selectImage(ACTION_TAKE_PHOTO_B);
                break;
            default:break;
        }
    }

    private void selectImage(final int actionCode) {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //get objectId
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("photo");
        ParseObject query = new ParseObject("photo");
//        query.whereEqualTo("userId", currentUser.getObjectId().toString());

        String imgName =
                System.currentTimeMillis() + ".jpg";
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),imgName);
                byte[] image = bytes.toByteArray();
               String encodeImg= Base64.encodeToString(image,Base64.DEFAULT);
                // Create the ParseFile
                ParseFile file = new ParseFile( imgName, image);

                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                    }
                });
                query.put("picString",encodeImg);
                query.put("pic", file);
                query.put("userId", currentUser.getUsername().toString());

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivImage.setImageBitmap(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
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
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                //Convert image to bytesArrayoutputstream.
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


                byte[] image = bytes.toByteArray();
                String encodeImg= Base64.encodeToString(image, Base64.DEFAULT);
                // Create the ParseFile
                ParseFile file = new ParseFile(imgName , image);

                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                    }
                });
                query.put("picString", encodeImg);
                query.put("pic", file);
                query.put("userId", currentUser.getUsername().toString());
                 ivImage.setImageBitmap(bm);
            }

        }
        query.saveInBackground(new SaveCallback() {
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
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
//    //Camere Functions
//    private void dispatchTakePictureIntent(int actionCode) {
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        switch(actionCode) {
//            case ACTION_TAKE_PHOTO_B:
//                File f = null;
//
//                try {
//                    f = setUpPhotoFile();
//                    mCurrentPhotoPath = f.getAbsolutePath();
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    f = null;
//                    mCurrentPhotoPath = null;
//                }
//                break;
//
//            default:
//                break;
//        } // switch
//
//        startActivityForResult(takePictureIntent, actionCode);
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
//        File albumF = getAlbumDir();
//        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
//        return imageF;
//    }
//
//    private File setUpPhotoFile() throws IOException {
//
//        File f = createImageFile();
//        mCurrentPhotoPath = f.getAbsolutePath();
//
//        return f;
//    }
//    private File getAlbumDir() {
//        File storageDir = null;
//
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//
//            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
//
//            if (storageDir != null) {
//                if (! storageDir.mkdirs()) {
//                    if (! storageDir.exists()){
//                        Log.d("CameraSample", "failed to create directory");
//                        return null;
//                    }
//                }
//            }
//
//        } else {
//            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
//        }
//
//        return storageDir;
//    }
//    /* Photo album for this application */
//    private String getAlbumName() {
//        return getString(R.string.album_name);
//    }

}
//
//private static final int ACTION_TAKE_PHOTO_B = 1;
//private static final int ACTION_TAKE_PHOTO_S = 2;
//private static final int ACTION_TAKE_VIDEO = 3;
//
//private static final String BITMAP_STORAGE_KEY = "viewbitmap";
//private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
//private ImageView mImageView;
//private Bitmap mImageBitmap;
//
//private static final String VIDEO_STORAGE_KEY = "viewvideo";
//private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
//private VideoView mVideoView;
//private Uri mVideoUri;
//
//private String mCurrentPhotoPath;
//
//private static final String JPEG_FILE_PREFIX = "IMG_";
//private static final String JPEG_FILE_SUFFIX = ".jpg";
//
//private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
//
//
//    /* Photo album for this application */
//    private String getAlbumName() {
//        return getString(R.string.album_name);
//    }
//
//
//    private File getAlbumDir() {
//        File storageDir = null;
//
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//
//            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
//
//            if (storageDir != null) {
//                if (! storageDir.mkdirs()) {
//                    if (! storageDir.exists()){
//                        Log.d("CameraSample", "failed to create directory");
//                        return null;
//                    }
//                }
//            }
//
//        } else {
//            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
//        }
//
//        return storageDir;
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
//        File albumF = getAlbumDir();
//        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
//        return imageF;
//    }
//
//    private File setUpPhotoFile() throws IOException {
//
//        File f = createImageFile();
//        mCurrentPhotoPath = f.getAbsolutePath();
//
//        return f;
//    }
//
//    private void setPic() {
//
//		/* There isn't enough memory to open up more than a couple camera photos */
//		/* So pre-scale the target bitmap into which the file is decoded */
//
//		/* Get the size of the ImageView */
//        int targetW = mImageView.getWidth();
//        int targetH = mImageView.getHeight();
//
//		/* Get the size of the image */
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//		/* Figure out which way needs to be reduced less */
//        int scaleFactor = 1;
//        if ((targetW > 0) || (targetH > 0)) {
//            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//        }
//
//		/* Set bitmap options to scale the image decode target */
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//		/* Decode the JPEG file into a Bitmap */
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//
//		/* Associate the Bitmap to the ImageView */
//        mImageView.setImageBitmap(bitmap);
//        mVideoUri = null;
//        mImageView.setVisibility(View.VISIBLE);
//        mVideoView.setVisibility(View.INVISIBLE);
//    }
//
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
//
//    private void dispatchTakePictureIntent(int actionCode) {
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        switch(actionCode) {
//            case ACTION_TAKE_PHOTO_B:
//                File f = null;
//
//                try {
//                    f = setUpPhotoFile();
//                    mCurrentPhotoPath = f.getAbsolutePath();
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    f = null;
//                    mCurrentPhotoPath = null;
//                }
//                break;
//
//            default:
//                break;
//        } // switch
//
//        startActivityForResult(takePictureIntent, actionCode);
//    }
//
//    private void dispatchTakeVideoIntent() {
//        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
//    }
//
//    private void handleSmallCameraPhoto(Intent intent) {
//        Bundle extras = intent.getExtras();
//        mImageBitmap = (Bitmap) extras.get("data");
//        mImageView.setImageBitmap(mImageBitmap);
//        mVideoUri = null;
//        mImageView.setVisibility(View.VISIBLE);
//        mVideoView.setVisibility(View.INVISIBLE);
//    }
//
//    private void handleBigCameraPhoto() {
//
//        if (mCurrentPhotoPath != null) {
//            setPic();
//            galleryAddPic();
//            mCurrentPhotoPath = null;
//        }
//
//    }
//
//    private void handleCameraVideo(Intent intent) {
//        mVideoUri = intent.getData();
//        mVideoView.setVideoURI(mVideoUri);
//        mImageBitmap = null;
//        mVideoView.setVisibility(View.VISIBLE);
//        mImageView.setVisibility(View.INVISIBLE);
//    }
//
//Button.OnClickListener mTakePicOnClickListener =
//        new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
//            }
//        };
//
//Button.OnClickListener mTakePicSOnClickListener =
//        new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
//            }
//        };
//
//Button.OnClickListener mTakeVidOnClickListener =
//        new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dispatchTakeVideoIntent();
//            }
//        };
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        mImageView = (ImageView) findViewById(R.id.imageView1);
//        mVideoView = (VideoView) findViewById(R.id.videoView1);
//        mImageBitmap = null;
//        mVideoUri = null;
//
//        Button picBtn = (Button) findViewById(R.id.btnIntend);
//        setBtnListenerOrDisable(
//                picBtn,
//                mTakePicOnClickListener,
//                MediaStore.ACTION_IMAGE_CAPTURE
//        );
//
//        Button picSBtn = (Button) findViewById(R.id.btnIntendS);
//        setBtnListenerOrDisable(
//                picSBtn,
//                mTakePicSOnClickListener,
//                MediaStore.ACTION_IMAGE_CAPTURE
//        );
//
//        Button vidBtn = (Button) findViewById(R.id.btnIntendV);
//        setBtnListenerOrDisable(
//                vidBtn,
//                mTakeVidOnClickListener,
//                MediaStore.ACTION_VIDEO_CAPTURE
//        );
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
//            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
//        } else {
//            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case ACTION_TAKE_PHOTO_B: {
//                if (resultCode == RESULT_OK) {
//                    handleBigCameraPhoto();
//                }
//                break;
//            } // ACTION_TAKE_PHOTO_B
//
//            case ACTION_TAKE_PHOTO_S: {
//                if (resultCode == RESULT_OK) {
//                    handleSmallCameraPhoto(data);
//                }
//                break;
//            } // ACTION_TAKE_PHOTO_S
//
//            case ACTION_TAKE_VIDEO: {
//                if (resultCode == RESULT_OK) {
//                    handleCameraVideo(data);
//                }
//                break;
//            } // ACTION_TAKE_VIDEO
//        } // switch
//    }
//
//    // Some lifecycle callbacks so that the image can survive orientation change
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
//        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
//        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
//        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
//        mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
//        mImageView.setImageBitmap(mImageBitmap);
//        mImageView.setVisibility(
//                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
//                        ImageView.VISIBLE : ImageView.INVISIBLE
//        );
//        mVideoView.setVideoURI(mVideoUri);
//        mVideoView.setVisibility(
//                savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ?
//                        ImageView.VISIBLE : ImageView.INVISIBLE
//        );
//    }
//
//    /**
//     * Indicates whether the specified action can be used as an intent. This
//     * method queries the package manager for installed packages that can
//     * respond to an intent with the specified action. If no suitable package is
//     * found, this method returns false.
//     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
//     *
//     * @param context The application's environment.
//     * @param action The Intent action to check for availability.
//     *
//     * @return True if an Intent with the specified action can be sent and
//     *         responded to, false otherwise.
//     */
//    public static boolean isIntentAvailable(Context context, String action) {
//        final PackageManager packageManager = context.getPackageManager();
//        final Intent intent = new Intent(action);
//        List<ResolveInfo> list =
//                packageManager.queryIntentActivities(intent,
//                        PackageManager.MATCH_DEFAULT_ONLY);
//        return list.size() > 0;
//    }
//
//    private void setBtnListenerOrDisable(
//            Button btn,
//            Button.OnClickListener onClickListener,
//            String intentName
//    ) {
//        if (isIntentAvailable(this, intentName)) {
//            btn.setOnClickListener(onClickListener);
//        } else {
//            btn.setText(
//                    getText(R.string.cannot).toString() + " " + btn.getText());
//            btn.setClickable(false);
//        }
//    }
//
////}
//try {
//        output = new FileOutputStream(file);
//
//        // Compress into png format image from 0% - 100%
//        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, output);
//        output.flush();
//        output.close();
//        String url = Images.Media.insertImage(getContentResolver(), bitmap1,
//        "Wallpaper.jpg", null);
//        }
//
//        catch (Exception e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//        }