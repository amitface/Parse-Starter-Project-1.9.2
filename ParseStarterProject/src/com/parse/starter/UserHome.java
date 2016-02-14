package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.ListFragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sql.RowSet;

/**
 * Created by root on 11/12/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class UserHome extends ListFragment {


    public int [] imageViews={R.drawable.pic,R.drawable.pic,R.drawable.pic};

    private static ParseUser currentUser = ParseUser.getCurrentUser();
     List<RowItem> rowItems = new ArrayList<RowItem>();
    List<String> test=null;
    CustomAdapter ad=null;

    public static UserHome newInstance(int sectionNumber) {
        UserHome fragment = new UserHome();
        Bundle args = new Bundle();
        args.putInt("1", sectionNumber);

//        args.putStringArrayList("list", (ArrayList<String>) data());
        fragment.setArguments(args);
        return fragment;
    }

    public UserHome() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_userhome, container, false);
        find();
//        TextView rating = (TextView)rootView.findViewById(R.id.rating);

        return rootView;
    }




    private void find()
    {

        final List<String> test = new ArrayList<String>();
        final List<String> userKey=new ArrayList<String>();
        userKey.add("following");

        final List<String> filter=new ArrayList<String>();
        filter.add("userId");
        filter.add("createdAt");
        filter.add("pic");
        filter.add("picString");

        ParseQuery<ParseObject> friends= ParseQuery.getQuery("Friends");
//        final ParseQuery<ParseObject> pics =ParseQuery.getQuery("photo");
//        pics.addDescendingOrder("createdAt");
        friends.whereEqualTo("userId", currentUser.getUsername().toString());
        friends.selectKeys(userKey);

        friends.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (ParseObject p : list) {
                    test.add(p.getString("following"));
                }

                ad=new CustomAdapter(getActivity(),test);
                setListAdapter(ad);
                ad.notifyDataSetChanged();
            }
        });

    }
}
