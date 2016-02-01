package com.parse.starter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseFile;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by root on 31/1/16.
 */
class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;

    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }
    Bitmap bitmap=null;
    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.solid);
                    imageView.setImageDrawable(placeholder);
                }
            }

        }
    }

    private Bitmap downloadBitmap(String url) {

//       ParseFile photo=  (ParseFile) url;

//            photo.getDataInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] bytes, com.parse.ParseException e) {
        byte[] bytes = Base64.decode(url , Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
//                    if (e == null) {
//                        // data has the bytes for the resume
//
//
//
//                    } else {
//                        // something went wrong
//                    }
//                }
//            });
//        try {
//        } catch (ParseException e) {
//
//            Log.d("ImageDownloader", "Error downloading image from " +e.getCause().toString());
//        }
        return bitmap;

    }
}
