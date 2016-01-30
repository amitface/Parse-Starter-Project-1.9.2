package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.util.List;

import static com.parse.starter.R.*;

public class Login extends Activity implements View.OnClickListener{
	String usernametxt;
	String passwordtxt;

	EditText password;
	EditText username;
	ParseUser currentUser =ParseUser.getCurrentUser();

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.main);
		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		if (currentUser != null) {
			// do stuff with the user
			Intent intent =new Intent(getApplicationContext(),Home.class);
			startActivity(intent);
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (currentUser != null) {
			// do stuff with the user
			Intent intent =new Intent(getApplicationContext(),Home.class);
			startActivity(intent);
		}// Always call the superclass method first

		// Activity being restarted from stopped state
//		password=(EditText)findViewById(id.pass);
//		username=(EditText)findViewById(id.name);
//
//		usernametxt=username.getText().toString();
//		passwordtxt=password.getText().toString();
//		final Intent intentHome = new Intent(this, Home.class);
//		Toast.makeText(this,usernametxt+" "+passwordtxt,Toast.LENGTH_LONG).show();
//		if(!usernametxt.isEmpty() && !passwordtxt.isEmpty())
//		{
//
//			ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
//				@Override
//				public void done(ParseUser parseUser, com.parse.ParseException e) {
//					if (parseUser != null) {
//						// Hooray! The user is logged in.
//						startActivity(intentHome);
//					} else {
//						// Signup failed. Look at the ParseException to see what happened.
//						Toast.makeText(Login.this, "Wrong UserName or Password", Toast.LENGTH_SHORT).show();
//					}
//				}
//
//
//			});
//		}
	}

	@Override
	public void onClick(View view) {

		 switch(view.getId())
		{
			case id.submit:
//			Toast.makeText(this,"Submit",Toast.LENGTH_SHORT).show();
				password=(EditText)findViewById(id.pass);
				username=(EditText)findViewById(id.name);

				usernametxt=username.getText().toString();
				passwordtxt=password.getText().toString();
                final Intent intentHome = new Intent(this, Home.class);
				ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
					@Override
					public void done(ParseUser parseUser, com.parse.ParseException e) {
						if (parseUser != null) {
							// Hooray! The user is logged in.
							startActivity(intentHome);
						} else {
							// Signup failed. Look at the ParseException to see what happened.
							Toast.makeText(Login.this,"Wrong UserName or Password",Toast.LENGTH_SHORT).show();
						}
					}


				});




                break;
			case id.cancel:
				password=(EditText)findViewById(id.pass);
				username=(EditText)findViewById(id.name);
				password.setText("", TextView.BufferType.EDITABLE);
//				Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show();break;
			case id.register:
                Intent intent = new Intent(this, Register.class);
                startActivity(intent);break;
//                Toast.makeText(this,"register",Toast.LENGTH_SHORT).show();break;
			default:
				Toast.makeText(this,"Nothing",Toast.LENGTH_SHORT).show();break;
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
}
