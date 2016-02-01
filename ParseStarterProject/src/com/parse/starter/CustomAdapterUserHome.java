package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.parse.GetDataCallback;
import com.parse.ParseFile;

import java.net.URL;
import java.util.List;

/**
 * Created by root on 25/1/16.
 */


public class CustomAdapterUserHome extends ArrayAdapter {

    List<RowItem> result;
    Context context;
    Bitmap bitmap;
    ParseFile photo;

    Bitmap bitmap2[];
    private static LayoutInflater inflater=null;
    public CustomAdapterUserHome(Context context, int resource, List<RowItem> rowitem) {
        super(context,resource,rowitem);
        // TODO Auto-generated constructor stub
        result=rowitem;
        this.context=context;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    public static class Holder
    {
        TextView tv;
        ImageView img;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder;


        if (convertView == null)
        {
        convertView = inflater.inflate(R.layout.list_item, null);
            holder=new Holder();
        holder.tv=(TextView) convertView.findViewById(R.id.name);
        holder.img=(ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }
        else
        {
            holder=(Holder)convertView.getTag();
        }


        RowItem newsItem = result.get(position);
        holder.tv.setText(newsItem.getname());

        if (holder.img != null) {
            new ImageDownloaderTask(holder.img).execute(newsItem.getImgname());
        }

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result.get(position).getname(), Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }

}
//byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
//Bitmap  bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);