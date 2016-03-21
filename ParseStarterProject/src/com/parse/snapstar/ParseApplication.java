package com.parse.snapstar;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialize Crash Reporting.
   // ParseCrashReporting.enable(this);

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    //Parse.initialize(this);
    Parse.initialize(this, "FPlmfkoukanUhiJ9dZKcQRtSoQRHN4iONS4Buj89", "0nWD1g4C8yfvE5bWmxr62UyAHqdVXYsfiTTNJFof");

    //ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    // Optionally enable public read access.
     defaultACL.setPublicReadAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

    ParseInstallation.getCurrentInstallation().saveInBackground();
  }
}
