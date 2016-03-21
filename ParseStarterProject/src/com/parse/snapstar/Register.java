package com.parse.snapstar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends Activity implements View.OnClickListener{

    String usernametxt;
    String passwordtxt;
    String emailtxt;
    EditText password;
    EditText username;
    EditText email;
    ParseUser user ;
    Pattern pattern = Pattern.compile("\\s");

//    boolean found = matcher.find();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());


    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.submit:

                // Retrieve the text entered from the EditText
                username = (EditText) findViewById(R.id.username);
                password = (EditText) findViewById(R.id.password);
                email = (EditText) findViewById(R.id.email);

                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();
                emailtxt=email.getText().toString();

                Matcher matcher = pattern.matcher(emailtxt);

                // Force user to fill up the form
                if (usernametxt.equals("") || passwordtxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();

                }
                else if(matcher.find())
                {
                    Toast.makeText(getApplication(),"Space not allowed in email",Toast.LENGTH_LONG).show();
                }
                 else
                 {
                 //    Save new user data into Parse.com Data Storage
                    user=new ParseUser();

                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);
                    user.setEmail(emailtxt);

                     final ParseUser finalquery=user;
                     final ProgressDialog progressDialog=new ProgressDialog(this);
                     progressDialog.setCancelable(false);
                     progressDialog.setCanceledOnTouchOutside(false);
                     progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                     progressDialog.setMessage("Please wait..");
                     progressDialog.show();
                     final ParseUser finaluser=user;
                     new Thread(new Runnable() {
                         @Override
                         public void run() {
                             finaluser.signUpInBackground(new SignUpCallback() {
                                 @Override
                                 public void done(com.parse.ParseException e) {


                                     if(e==null)
                                     {

                                         BitmapDrawable bitmapDrawable=(BitmapDrawable)getResources().getDrawable(R.drawable.solid);
                                         Bitmap bitmap= bitmapDrawable.getBitmap();

                                         bitmap=cropToSquare(bitmap);
                                         ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                         byte[] bitmapdata = stream.toByteArray();
                                         ParseFile parseFile=new ParseFile(bitmapdata);
                                         parseFile.saveInBackground();
                                         finaluser.put("photoprofile", parseFile);
                                         finaluser.saveInBackground();
                                         //   Toast.makeText(getApplicationContext(),"signed UP",Toast.LENGTH_SHORT).show();
                                         directtoProfile();

                                     }
                                     else{
                                         Toast.makeText(getApplicationContext(),"Error"+e.getCause().toString(),Toast.LENGTH_SHORT).show();
                                     }
                                     progressDialog.dismiss();

                                 }
                             });
                         }
                     }).start();
                }
                break;
            case R.id.cancel:
                finishActivity(0);
                break;
            default:break;
        }
    }
    public void directtoProfile()
    {
        final Intent intentRegister = new Intent(this, Home.class);

        ParseObject userDetails= new ParseObject("UserDetails");
        userDetails.put("userId", ParseUser.getCurrentUser().getUsername().toString());
        userDetails.put("descripiton","Hello world");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100

        userDetails.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {

                if (e == null) {
                    intentRegister.putExtra("userId", user.getUsername().toString());
                    startActivity(intentRegister);
                }
                else
                    Toast.makeText(Register.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        //Toast.makeText(getApplicationContext(),user.getObjectId().toString(),Toast.LENGTH_LONG).show();
    }

    public  Bitmap cropToSquare(Bitmap bitmap){
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
