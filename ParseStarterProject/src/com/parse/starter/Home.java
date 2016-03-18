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
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseSession;
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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (currentUser != null) {
            // do stuff with the user
        } else {
            // show the signup or login screen
            Intent intent =new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);


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
//            Toast.makeText(this,"restart Home",Toast.LENGTH_LONG).show();
            Intent intent =new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        if(id== R.id.action_logout)
//        {
//            ParseUser.logOut();
//
//           currentUser= ParseUser.getCurrentUser();
//     //       Toast.makeText(this,currentUser.getObjectId().toString(),Toast.LENGTH_LONG).show();
//            if(currentUser==null) {
//                Intent intent = new Intent(getApplicationContext(), Login.class);
//                startActivity(intent);
//            }
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.camera:

//                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                selectImage(ACTION_TAKE_PHOTO_B);
                break;
            case R.id.refresh:
                new UserHome().refresh();
                break;
            case R.id.settings:
                Intent settings = new Intent(this,SettingsActivity.class);
                startActivity(settings);
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

//        ParseQuery<ParseObject> query = ParseQuery.getQuery("photo");
        ParseObject query ;


        String imgName =   System.currentTimeMillis() + ".jpg";

        if (resultCode == RESULT_OK) {
            query= new ParseObject("photo");
            if (requestCode == REQUEST_CAMERA) {
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                Bitmap thumbnail;

                if(bm.getHeight()>=400 && bm.getWidth()>=400)
                bm=cropToSquare(bm);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),imgName);
                byte[] image = bytes.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile( imgName, image);
                file.saveInBackground();

                query.put("pic", file);
                query.put("userId", currentUser.getUsername().toString());
                query.put("user",currentUser);

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
//                ivImage.setImageBitmap(bm);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm=BitmapScaler(selectedImagePath,400);
                if(bm.getHeight()>=400 && bm.getWidth()>=400)
                    bm=cropToSquare(bm);

                cursor.close();
                //Convert image to bytesArrayoutputstream.
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);


                byte[] image = bytes.toByteArray();
//                String encodeImg= Base64.encodeToString(image, Base64.DEFAULT);

                // Create the ParseFile
                ParseFile file = new ParseFile(imgName , image);
                file.saveInBackground();
                ParseFile fileuser =currentUser.getParseFile("photoprofile");
                fileuser.saveInBackground();
                query.put("user",currentUser);
                query.put("picThumbnail",fileuser);
                query.put("pic", file);
                query.put("userId", currentUser.getUsername().toString());
//                 ivImage.setImageBitmap(bm);
            }


        // By specifying no write privileges for the ACL, we can ensure the role cannot be altered.
        ParseACL roleACL = new ParseACL();
        roleACL.setPublicReadAccess(true);
        roleACL.setPublicWriteAccess(true);
        query.setACL(roleACL);
        query.put("rating", 0);

        final ParseObject finalquery=query;
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                finalquery.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if(e==null) {
                            new UserHome().find();

                            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Error"+e.getCause().toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                    }
                });
            }
        }).start();
        }
    }

    private  Bitmap BitmapScaler(String selectedImagePath,int size)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = size;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(selectedImagePath, options);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    //Crops image to same height and width
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return super.onKeyDown(keyCode, event);
    }
}
