package com.parse.starter;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.widget.SearchView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Search extends Activity {

    List<ParseUser> listUser = null;
    CustomAdapterSearchActivity customAdapterSearch = null;
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        customAdapterSearch = new CustomAdapterSearchActivity(getApplicationContext());

        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(customAdapterSearch);

        SearchView searchView = (SearchView)findViewById(R.id.searchView);
//        ParseQuery<ParseUser> parseUserParseQuery = new ParseQuery<ParseUser>("_User");
//        parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> list, ParseException e) {
//                listUser = new ArrayList<ParseUser>();
//                listUser.addAll(list);
//
//            }
//        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

}
