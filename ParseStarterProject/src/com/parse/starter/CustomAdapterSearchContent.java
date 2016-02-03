package com.parse.starter;

/**
 * Created by root on 2/2/16.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterSearchContent extends ParseQueryAdapter<ParseObject> {

    public CustomAdapterSearchContent(Context context, final List<String> rt) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("UserDetails");
                query.whereContainedIn("userId",rt);
                query.addDescendingOrder("createdAt");
                //query.whereEqualTo("highPri", true);
//                               final List<String> filter=new ArrayList<String>();
//                filter.add("userId");
//                filter.add("createdAt");
//                filter.add("pic");
//                query.selectKeys(filter);

                return query;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.searchcontent, null);
        }

        super.getItemView(object, v, parent);

        // Add and download the image
        ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.imageView_round);
        ParseFile imageFile = object.getParseFile("profilephoto");
        if (imageFile != null) {
            todoImage.setParseFile(imageFile);
            todoImage.loadInBackground();
        }

        // Add the title view
        TextView titleTextView = (TextView) v.findViewById(R.id.name);
        titleTextView.setText(object.getString("userId"));

        // Add a reminder of how long this item has been outstanding
//        TextView timestampView = (TextView) v.findViewById(R.id.timestamp);
//        timestampView.setText(object.getCreatedAt().toString());
        return v;
    }

}
