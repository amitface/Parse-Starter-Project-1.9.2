package com.parse.snapstar;

/**
 * Created by root on 2/2/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class CustomAdapterSearchContent extends ParseQueryAdapter<ParseObject> implements View.OnClickListener{



    ParseUser current = ParseUser.getCurrentUser();
    public CustomAdapterSearchContent(Context context, final List<String> rt) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri

        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("_User");
//                query.whereContainedIn("userId",rt);
                query.whereContainedIn("username",rt);
                query.addDescendingOrder("createdAt");
                //query.whereEqualTo("highPri", true);


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
        ParseFile imageFile = object.getParseFile("photoprofile");
        todoImage.setOnClickListener(this);
        todoImage.setTag(object);
//        ParseFile imageFile = object.getParseFile("profilephoto");
        if (imageFile != null) {
            todoImage.setParseFile(imageFile);
            todoImage.loadInBackground();
        }

        Button button =(Button)v.findViewById(R.id.button);
        button.setTag(object);
        button.setOnClickListener(this);
        // Add the title view
        TextView titleTextView = (TextView) v.findViewById(R.id.name);
        titleTextView.setText(object.getString("username"));
        titleTextView.setTag(object);
        titleTextView.setOnClickListener(this);

        v.setTag(object);
        v.setOnClickListener(this);
        // Add a reminder of how long this item has been outstanding
//        TextView timestampView = (TextView) v.findViewById(R.id.timestamp);
//        timestampView.setText(object.getCreatedAt().toString());
        return v;
    }

    @Override
    public void onClick(View view) {
        Intent intentProfile=new Intent(getContext(),ProfileActivity.class);
        intentProfile.putExtra("user",((ParseUser) view.getTag()).get("username").toString());
        switch (view.getId())
        {
            case R.id.button:
                int id=view.getId();
                buttonstate((Button) view.findViewById(R.id.button), ((ParseUser) view.getTag()).get("username").toString());
                break;
            case R.id.imageView_round:

                getContext().startActivity(intentProfile);
                break;
            case R.id.name:
                getContext().startActivity(intentProfile);
                break;

            default:
                getContext().startActivity(intentProfile);
                break;
        }
    }

    public void buttonstate(Button button,final String user)
    {
        button.setSelected(!button.isSelected());

        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait..");
        final Button finalbutton=button;
        if (button.isSelected()) {
            //Handle selected state change

            final ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>("Friends");
            parseQuery.whereEqualTo("userId",user);
            parseQuery.whereEqualTo("following", current.getUsername());

            final ParseObject parseObject = new ParseObject("Friends");

            parseObject.put("userId", current.getUsername());
            parseObject.put("following", user);

            ParseACL roleACL = new ParseACL();
            roleACL.setPublicReadAccess(true);
            roleACL.setPublicWriteAccess(true);
            parseObject.setACL(roleACL);

            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    parseQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject Object, ParseException e) {
                            if (e == null) {
                                parseObject.put("followback", true);

                            } else {
                                parseObject.put("followback", false);
                            }
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        finalbutton.setText("following");
                                    } else
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
                }
            }).start();

        } else {
            //Handle de-select state change

            final ParseQuery<ParseObject> objectParseQuery=new ParseQuery<ParseObject>("Friends");
            objectParseQuery.whereEqualTo("userId",current.getUsername());
            objectParseQuery.whereEqualTo("following", user);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    objectParseQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if(e==null)
                            {
                                parseObject.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        progressDialog.dismiss();
                                        finalbutton.setText("follow");
                                    }
                                });

                            }
                            else
                                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT);
                            progressDialog.dismiss();
                        }
                    });
                }
            }).start();
            button.setText("follow");


        }
    }
}
