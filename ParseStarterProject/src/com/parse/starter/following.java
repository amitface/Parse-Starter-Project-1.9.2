package com.parse.starter;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class following extends ListActivity {

    ParseUser currentUser=ParseUser.getCurrentUser();

    List test;

    CustomAdapterFollow ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

            test = new ArrayList();
        ParseQuery<ParseObject> friends= ParseQuery.getQuery("Friends");
//        final ParseQuery<ParseObject> pics =ParseQuery.getQuery("photo");
//        pics.addDescendingOrder("createdAt");
        friends.whereEqualTo("userId", currentUser.getUsername().toString());
        friends.selectKeys(Arrays.asList("following"));

        friends.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (ParseObject p : list) {
                    test.add(p.getString("following"));
                }

                            ad = new CustomAdapterFollow(getApplication(), test);
                            setListAdapter(ad);
                            ad.notifyDataSetChanged();

            }
        });
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
}
