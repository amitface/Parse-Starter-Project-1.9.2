package com.parse.starter;

import android.annotation.TargetApi;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 2/2/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchList extends ListFragment {


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
        return rootview;
    }
}
