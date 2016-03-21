package com.parse.snapstar;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by root on 8/2/16.
 */
public class RatingDownloaderTask extends AsyncTask<String,Void,Float> {

    private final WeakReference<TextView> textViewReference;
    float pnt =(float) 0.0;
    public RatingDownloaderTask(TextView rating)
    {
        textViewReference= new WeakReference<TextView>(rating);
    }
    @Override
    protected Float doInBackground(String... params)
    {
        return downloadRating(params[0]);
    }
    @Override
    protected void onPostExecute(Float f)
    {
        TextView rating=textViewReference.get();
        rating.setText(Float.toString(f));
    }

    private float downloadRating(String param) {
       pnt= (float) 0.0;
        ParseQuery<ParseObject> obj = ParseQuery.getQuery("photoRating");

        obj.whereEqualTo("photoId", param);

        obj.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (e == null) {

                    for (ParseObject b : list) {
                        pnt += b.getInt("rating");
                    }
                    pnt = pnt / list.size();
                    //rating.setText(Float.toString(pnt/list.size()));
                } else {

                    e.printStackTrace();
                    Log.d("RatingDownloader",e.getMessage());
                }

            }

        });
        return pnt;
    }

}
