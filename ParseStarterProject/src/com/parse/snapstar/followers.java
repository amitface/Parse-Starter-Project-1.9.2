package com.parse.snapstar;

import android.app.ListActivity;
import android.app.ProgressDialog;
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

public class followers extends ListActivity {

    ParseUser currentUser=ParseUser.getCurrentUser();

    List test=new ArrayList();
    CustomAdapterFollow ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);


        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);


        ParseQuery<ParseObject> friends= ParseQuery.getQuery("Friends");
//        final ParseQuery<ParseObject> pics =ParseQuery.getQuery("photo");
//        pics.addDescendingOrder("createdAt");
        friends.whereEqualTo("following", currentUser.getUsername().toString());
        friends.selectKeys(Arrays.asList("userId"));

        friends.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (ParseObject p : list) {
                    test.add(p.getString("userId"));
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
