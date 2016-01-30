package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 11/12/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class UserHome extends ListFragment {
    public String[] username={"amit","rohit","kohli"};
    public int [] imageViews={R.drawable.pic,R.drawable.pic,R.drawable.pic};
    List<RowItem> rowItems;
    ParseUser currentUser = ParseUser.getCurrentUser();

    public static UserHome newInstance(int sectionNumber) {
        UserHome fragment = new UserHome();
        Bundle args = new Bundle();
        args.putInt("1", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public UserHome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_userhome, container, false);
//        rowItems = new ArrayList<RowItem>();
//        for (int i = 0; i < showFavoriteList().size(); i++) {
//        for (int i = 0; i < 2; i++) {
//            RowItem item = new RowItem(username[i],imageViews[i]);
//
//            rowItems.add(item);
//        }
//
       //ListView listView = (ListView) rootView.findViewById(R.id.);
//        CustomAdapterUserHome adapter = new CustomAdapterUserHome(getActivity(),R.layout.list_item, rowItems);
        CustomAdapterUserHome adapter = new CustomAdapterUserHome(getActivity(),username, imageViews);
        setListAdapter(adapter);

//        if(currentUser!=null)
//        Toast.makeText(getActivity(),currentUser.getObjectId().toString(),Toast.LENGTH_LONG).show();
        return rootView;
    }


}
