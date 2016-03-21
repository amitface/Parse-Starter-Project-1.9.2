package com.parse.snapstar;

import android.annotation.TargetApi;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 2/2/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchList extends ListFragment implements View.OnClickListener{

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
        SearchView textView=(SearchView)rootview.findViewById(R.id.searchView);
                textView.setOnClickListener(this);

        ParseQuery<ParseObject> friends= ParseQuery.getQuery("Friends");

        friends.whereEqualTo("userId", currentUser.getUsername().toString());
        friends.selectKeys(Arrays.asList("following"));

        friends.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (ParseObject p : list) {
                    test.add(p.getString("following"));
                }
                test.add(currentUser.getUsername().toString());
                ParseQuery<ParseObject> users = ParseQuery.getQuery("_User");
                users.selectKeys(Arrays.asList("username"));
                users.whereNotContainedIn("username", test);
                users.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            List<String> test1 = new ArrayList<String>();
                            for (ParseObject p : list) {
                                test1.add(p.getString("username"));
                            }
                            ad = new CustomAdapterSearchContent(getActivity(), test1);
                            setListAdapter(ad);
                            ad.notifyDataSetChanged();
                        }

                    }
                });

            }
        });
        return rootview;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button:
                break;
            case R.id.searchView:
                Intent intent=new Intent(getActivity(),Search.class);
                startActivity(intent);

            default:break;
        }
    }
}
