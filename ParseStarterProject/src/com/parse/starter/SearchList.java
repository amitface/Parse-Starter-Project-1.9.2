package com.parse.starter;

import android.annotation.TargetApi;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2/2/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchList extends ListFragment {

    //Custom Adapter
    CustomAdapterSearchContent ad=null;

    //Parse User
    ParseUser currentUser=ParseUser.getCurrentUser();
    public static SearchList newInstance(int sectionNumber)
    {
        SearchList sl= new SearchList();
        Bundle bundle = new Bundle();
        bundle.putInt("1", sectionNumber);
        sl.setArguments(bundle);
        return sl;
    }

    public SearchList()
    {

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootview= inflater.inflate(R.layout.fragment_searchlist,container,false);
        final List<String> test = new ArrayList<String>();
        final List<String> userKey=new ArrayList<String>();
        userKey.add("following");




        ParseQuery<ParseObject> friends= ParseQuery.getQuery("Friends");
        final ParseQuery<ParseObject> pics =ParseQuery.getQuery("photo");
        pics.addDescendingOrder("createdAt");
        friends.whereEqualTo("userId", currentUser.getUsername().toString());
        friends.selectKeys(userKey);

        friends.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (ParseObject p : list) {
                    test.add(p.getString("following"));
                }
                ad = new CustomAdapterSearchContent(getActivity(), test);
                setListAdapter(ad);
                ad.notifyDataSetChanged();
            }
        });
        return rootview;
    }
}
