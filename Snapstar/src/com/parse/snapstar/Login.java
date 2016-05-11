package com.parse.snapstar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


import java.util.Arrays;

import static com.parse.snapstar.R.*;

public class Login extends FragmentActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{
	String usernametxt;
	String passwordtxt;

	String mFullName;
	String mEmail;

	EditText password;
	EditText username;
	ParseUser currentUser =ParseUser.getCurrentUser();

	LoginButton loginButton;
	CallbackManager callbackManager = null;
	ProgressDialog mProgressDialog;
	String permissions = " public_profile";

	private static final String TAG = "SignInActivity";
	private static final int RC_SIGN_IN = 9001;

	private GoogleApiClient mGoogleApiClient;



	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);
		callbackManager = CallbackManager.Factory.create();

		Log.d("onCreate ", "- done Login");
		setContentView(layout.main);
		ParseAnalytics.trackAppOpenedInBackground(getIntent());

		if (currentUser != null) {
			// do stuff with the user
			Intent intent =new Intent(getApplicationContext(),Home.class);
			startActivity(intent);
			finish();
		}
		else
		{

			ParseFacebookUtils.logInWithReadPermissionsInBackground(this, Arrays.asList("public_profile"), new LogInCallback() {
				@Override
				public void done(ParseUser user, com.parse.ParseException e) {
					if (user == null) {
						Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
					} else if (user.isNew()) {
						Log.d("MyApp", "User signed up and logged in through Facebook!");
					} else {
						Log.d("MyApp", "User logged in through Facebook!");
					}
				}
			});

		}

//		loginButton = (LoginButton) findViewById(R.id.login_button);
//		loginButton.setReadPermissions("email");
//
//
//		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//			@Override
//			public void onSuccess(LoginResult loginResult) {
//				// App code
//			}
//
//			@Override
//			public void onCancel() {
//				// App code
//			}
//
//			@Override
//			public void onError(FacebookException exception) {
//				// App code
//			}
//		});



//		// [START configure_signin]
//		// Configure sign-in to request the user's ID, email address, and basic
//		// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//				.requestEmail()
//				.build();
//		// [END configure_signin]
//
//		// [START build_client]
//		// Build a GoogleApiClient with access to the Google Sign-In API and the
//		// options specified by gso.
//		mGoogleApiClient = new GoogleApiClient.Builder(this)
//				.enableAutoManage( this, this /* OnConnectionFailedListener */)
//				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//				.build();
//		// [END build_client]
//		// [START customize_button]
//		// Customize sign-in button. The sign-in button can be displayed in
//		// multiple sizes and color schemes. It can also be contextually
//		// rendered based on the requested scopes. For example. a red button may
//		// be displayed when Google+ scopes are requested, but a white button
//		// may be displayed when only basic profile is requested. Try adding the
//		// Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
//		// difference.
//		SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
//		signInButton.setSize(SignInButton.SIZE_STANDARD);
//		signInButton.setScopes(gso.getScopeArray());
//		signInButton.setOnClickListener(this);
//
//		findViewById(id.sign_out_button).setOnClickListener(this);
//		findViewById(id.sign_out_and_disconnect).setOnClickListener(this);
//		// [END customize_button]

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("start ", "- done Login");
//		OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//		if (opr.isDone()) {
//			// If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//			// and the GoogleSignInResult will be available instantly.
//			Log.d(TAG, "Got cached sign-in");
//			GoogleSignInResult result = opr.get();
//			handleSignInResult(result);
//		} else {
//			// If the user has not previously signed in on this device or the sign-in has expired,
//			// this asynchronous branch will attempt to sign in the user silently.  Cross-device
//			// single sign-on will occur in this branch.
//			showProgressDialog();
//			opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//				@Override
//				public void onResult(GoogleSignInResult googleSignInResult) {
//					hideProgressDialog();
//					handleSignInResult(googleSignInResult);
//				}
//			});
//		}
	}

	// [START onActivityResult]

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("onRestart ", "- done Login");
		finish();


	}

	@Override
	public void onClick(View view) {

		 switch(view.getId())
		{
			case id.submit:
				password=(EditText)findViewById(id.pass);
				username=(EditText)findViewById(id.name);

				usernametxt=username.getText().toString();
				passwordtxt=password.getText().toString();

                final Intent intentHome = new Intent(this, Home.class);

				showProgressDialog();

				new Thread(new Runnable() {
					@Override
					public void run() {
						ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
							@Override
							public void done(ParseUser parseUser, com.parse.ParseException e) {

								hideProgressDialog();
								if (parseUser != null) {
									// Hooray! The user is logged in.

									startActivity(intentHome);
									finish();
								} else {
									// Signup failed. Look at the ParseException to see what happened.

									Toast.makeText(Login.this,"Wrong UserName or Password",Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
				}).start();
				break;
			case id.cancel:
				password=(EditText)findViewById(id.pass);
				username=(EditText)findViewById(id.name);
				password.setText("", TextView.BufferType.EDITABLE);
				break;
			case id.register:
                Intent intent = new Intent(this, Register.class);
                startActivity(intent);
				break;
//			case R.id.sign_in_button:
//				signIn();
//				break;
//			case R.id.sign_out_button:
//				signOut();
//				break;
//			case R.id.disconnect_button:
//				revokeAccess();
//				break;
			default:
				Toast.makeText(this,"Nothing",Toast.LENGTH_SHORT).show();
				break;
		}

	}
	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}


	// [START signOut]
	private void signOut() {
		Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(Status status) {
						// [START_EXCLUDE]
						updateUI(false);
						// [END_EXCLUDE]
					}
				});
	}
	// [END signOut]

	// [START revokeAccess]
	private void revokeAccess() {
		Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(Status status) {
						// [START_EXCLUDE]
						updateUI(false);
						// [END_EXCLUDE]
					}
				});
	}
	// [END revokeAccess]


	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		// An unresolvable error has occurred and Google APIs (including Sign-In) will not
		// be available.
		Log.d(TAG, "onConnectionFailed:" + connectionResult);
		Toast.makeText(this,"onConnectionFailed:" + connectionResult,Toast.LENGTH_LONG).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);

		Toast.makeText(this,"onActivityReslult",Toast.LENGTH_SHORT).show();
		// Result returned from launching the Intent from
		//   GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				GoogleSignInAccount acct = result.getSignInAccount();
				// Get account information
				mFullName = acct.getDisplayName();
				mEmail = acct.getEmail();
				handleSignInResult(result);
				ParseUser.becomeInBackground(""+acct.getId(), new LogInCallback() {


					@Override
					public void done(ParseUser parseUser, com.parse.ParseException e) {
						if (parseUser != null) {
							// The current user is now set to user.
						} else {
							// The token could not be validated.
						}
					}
				});
			}
		}
	}

	private void handleSignInResult(GoogleSignInResult result) {
		Log.d(TAG, "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();
//			mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//			Toast.makeText(this,acct.getId(),Toast.LENGTH_LONG).show();
			updateUI(true);
		} else {
			// Signed out, show unauthenticated UI.
			updateUI(false);
		}
	}

	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("Please wait..");
		}

		mProgressDialog.show();
	}



	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.hide();
		}
	}

	private void updateUI(boolean signedIn) {
//		if (signedIn) {
//			findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//			findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
//		} else {
////			mStatusTextView.setText(R.string.signed_out);
//
//			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//			findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
//		}
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

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d("resume ","- done Login");
		if (currentUser != null) {
			// do stuff with the user
			Intent intent =new Intent(getApplicationContext(),Home.class);
			startActivity(intent);
		}// Always call the superclass method first
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d("pause ","- done Login");
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		Log.d("stop ","- done Login");
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.v("destory ", "- done Login");
	}
}
