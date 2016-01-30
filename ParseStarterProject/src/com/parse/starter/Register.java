package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;


public class Register extends Activity implements View.OnClickListener{

    String usernametxt;
    String passwordtxt;
    String emailtxt;
    EditText password;
    EditText username;
    EditText email;
    ParseUser user = new ParseUser();
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
//                Toast.makeText(getApplicationContext(),
//                            "submit clicked",
//                            Toast.LENGTH_LONG).show();
                // Retrieve the text entered from the EditText
                username = (EditText) findViewById(R.id.username);
                password = (EditText) findViewById(R.id.password);
                email = (EditText) findViewById(R.id.email);
                     usernametxt = username.getText().toString();
                     passwordtxt = password.getText().toString();
                     emailtxt=email.getText().toString();
                // Force user to fill up the form
                if (usernametxt.equals("") && passwordtxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();

                } else {
                 //    Save new user data into Parse.com Data Storage


                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);
                    user.setEmail(emailtxt);

//                    ParseUser user = new ParseUser();
//                    user.setUsername("amiti");
//                    user.setPassword("password");
//                    user.setEmail("amiti@gmail.com");

// other fields can be set just like with ParseObject
                   // user.put("phone", "650-253-0000");
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if(e==null)
                            {
                             //   Toast.makeText(getApplicationContext(),"signed UP",Toast.LENGTH_SHORT).show();
                                directtoProfile();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

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
        final Intent intentRegister = new Intent(this, UserDetails.class);

        ParseObject userDetails= new ParseObject("UserDetails");
        userDetails.put("userId", ParseUser.getCurrentUser().getObjectId().toString());
        userDetails.put("descripiton","Hello world");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100

        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.solid);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile( "solid.jpeg", image);

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });

        userDetails.put("profilephoto", file);
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
}
