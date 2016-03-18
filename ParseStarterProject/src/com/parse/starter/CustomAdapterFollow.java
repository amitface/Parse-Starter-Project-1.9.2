package com.parse.starter;

/**
 * Created by root on 29/2/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;




public class CustomAdapterFollow extends ParseQueryAdapter<ParseObject> implements View.OnClickListener{

    ParseUser current = ParseUser.getCurrentUser();
    public CustomAdapterFollow(Context context, final List<String> rt) {
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
//        ParseFile imageFile = object.getParseFile("profilephoto");
        if (imageFile != null) {
            todoImage.setParseFile(imageFile);
            todoImage.loadInBackground();
        }

        Button button =(Button)v.findViewById(R.id.button);
        button.setTag(object);
        button.setSelected(true);
        button.setText("following");
        button.setOnClickListener(this);
        // Add the title view
        TextView titleTextView = (TextView) v.findViewById(R.id.name);
        titleTextView.setText(object.getString("username"));

        v.setOnClickListener(this);
        // Add a reminder of how long this item has been outstanding
//        TextView timestampView = (TextView) v.findViewById(R.id.timestamp);
//        timestampView.setText(object.getCreatedAt().toString());
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button:
                int id=view.getId();
                buttonstate((Button) view.findViewById(R.id.button), ((ParseUser) view.getTag()).get("username").toString());

                break;
            case R.id.imageView_round:
                break;
            case R.id.name:
                break;
            default:break;
        }
    }
    public void buttonstate(Button button,String user)
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

            ParseObject parseObject= new ParseObject("Friends");
            parseObject.put("userId", current.getUsername());
            parseObject.put("following", user);

            final ParseObject finalparseObject=parseObject;

            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    finalparseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                            {
                                finalbutton.setText("following");
                            }
                            else
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
                            progressDialog.dismiss();
                        }
                    });
                }
            }).start();

//            button.setBackgroundColor(Color.BLUE);
        } else {
            //Handle de-select state change
            // Toast.makeText(getActivity(),"else",Toast.LENGTH_SHORT).show();
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

//            button.setBackgroundColor(Color.GREEN);
        }
    }
}

