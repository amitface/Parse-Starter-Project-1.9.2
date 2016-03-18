package com.parse.starter;

import android.app.Dialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import bolts.Task;


public class CustomAdapter extends ParseQueryAdapter<ParseObject> implements View.OnClickListener{

	float points= (float) 0.0;
	double pnt = 0.0;

	private ProgressDialog progress;

	ParseUser current =ParseUser.getCurrentUser();
	List<ParseUser> list=new ArrayList<>();
	public CustomAdapter(Context context, final List<String> rt) {
		// Use the QueryFactory to construct a PQA that will only show
		// Todos marked as high-pri

		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery create() {
				ParseQuery query = new ParseQuery("photo");
				query.whereContainedIn("userId", rt);
				query.addDescendingOrder("createdAt");
				//query.whereEqualTo("highPri", true);
				query.selectKeys(Arrays.asList("userId","pic","rating","user")	);


				return query;

				}
			}

			);
		}




	static class Holder{
		ImageButton imageButton;
		ParseImageView todoImage; ParseOvalImageView todoUserImage;
		TextView titleTextView,timestampView,titleTextViewRating;
	}

	// Customize the layout by overriding getItemView
	@Override
	public View getItemView( final ParseObject object, View v, ViewGroup parent) {


		pnt=(float)0.0;
			 Holder holder=null ;
		if (v == null) {
			v = View.inflate(getContext(), R.layout.urgent_item, null);
			holder= new Holder();

			holder.todoUserImage=(ParseOvalImageView)v.findViewById(R.id.imageView_round);
			holder.todoImage = (ParseImageView) v.findViewById(R.id.img);
			holder.titleTextView = (TextView) v.findViewById(R.id.name);
			holder.titleTextViewRating =(TextView)v.findViewById(R.id.rating);
//			holder.timestampView = (TextView) v.findViewById(R.id.timestamp);
			holder.imageButton=(ImageButton) v.findViewById(R.id.imageButton);
			v.setTag(holder);
		}
		else
		{
			holder=(Holder)v.getTag();
		}
		object.pinInBackground();


		super.getItemView(object, v, parent);

		// Add and download the image
		ParseFile userimageFile=null;
		try
		{
			object.getParseObject("user").fetchIfNeededInBackground();
			 userimageFile= object.getParseObject("user").getParseFile("photoprofile");
		}catch (Exception e){

		}

		if(userimageFile !=null) {
			holder.todoUserImage.setParseFile(userimageFile);
			holder.todoUserImage.loadInBackground();

		}

		ParseFile imageFile = object.getParseFile("pic");
		if (imageFile != null) {
//			holder.todoImage.s
			holder.todoImage.setParseFile(imageFile);
			holder.todoImage.loadInBackground();
			imageFile.saveInBackground();

		}

		// Add the title view

		holder.titleTextView.setText(object.getString("userId"));

//		holder.timestampView.setText(object.getCreatedAt().toString());

		holder.titleTextViewRating.setText(Double.toString(object.getDouble("rating")));
		final TextView textView=(TextView) holder.titleTextViewRating;
		final ParseObject obj2=object;
		holder.imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

					showDialog(obj2,points,textView);

			}
		});

		return v;
	}


	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
			case R.id.name:
				break;

			case R.id.imageView_round:
				break;
			default:
				break;
		}
	}



	public void showDialog(ParseObject obj2, final float points, final TextView textView)
	{
		final Dialog dialog=new Dialog(getContext());
		dialog.setContentView(R.layout.dialog);
		dialog.setTitle("Rate It ....!!!");
		Button b1	=(Button)dialog.findViewById(R.id.button1);
		Button b2	=(Button)dialog.findViewById(R.id.button2);
		final NumberPicker np=(NumberPicker) dialog.findViewById(R.id.numberPicker1);
		np.setMaxValue(10);
		np.setMinValue(1);
		np.setValue((int) points);
//		np.setWrapSelectorWheel(false);
		np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker numberPicker, int i, int i1) {

			}
		});



		final ParseObject finalobj=obj2;
		b1.setOnClickListener(new View.OnClickListener() {
								  @Override
								  public void onClick(View view) {
									  dialog.dismiss();
									  progress = new ProgressDialog(getContext());
									  progress.setMessage("Saving..");
									  progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
									  progress.setIndeterminate(true);
									  progress.setProgress(0);
									  progress.setCanceledOnTouchOutside(false);
									  progress.setCancelable(false);
									  progress.show();

									  final Handler dataHandler = new Handler() {
										  @Override
										  public void handleMessage(Message pnt) {
											  String myData =(String)(pnt.obj);
											  textView.setText((myData) );
										  }
									  };


									  new  Thread(new Runnable() {
										  public void run() {
											  ParseQuery<ParseObject> obj = ParseQuery.getQuery("photoRating");

											  obj.whereEqualTo("userId", current.getUsername().toString());
											  obj.whereEqualTo("photoId", finalobj.getObjectId().toString());
											  ParseObject parseObject = null;
											  try {
												  parseObject = obj.getFirst();
												  parseObject.put("rating", np.getValue());
												  parseObject.save();
											  } catch (ParseException e) {
												  parseObject = new ParseObject("photoRating");
												  parseObject.put("userId", current.getUsername().toString());
												  parseObject.put("photoId", finalobj.getObjectId());
												  parseObject.put("rating", np.getValue());
												  ParseACL roleACL = new ParseACL();
												  roleACL.setPublicReadAccess(true);
												  roleACL.setPublicWriteAccess(true);
												  parseObject.setACL(roleACL);
												  try {
													  parseObject.save();
												  } catch (ParseException e1) {
													  e1.printStackTrace();
												  }

											  }
											  progress.setProgress(25);
											  List<ParseObject> parseObjectList = null;
											  ParseQuery object = ParseQuery.getQuery("photoRating");
											  object.selectKeys(Arrays.asList("rating"));
											  object.whereEqualTo("photoId", finalobj.getObjectId());
											  try {
												  parseObjectList = object.find();
												  pnt = 0.0;
												  for (ParseObject p : parseObjectList) {
													  pnt = pnt + (Integer) p.get("rating");
												  }
												  pnt = pnt / parseObjectList.size();
												  pnt=Math.round(pnt*100)/100.0;

											  } catch (ParseException e) {
												  e.printStackTrace();
											  }
											  progress.setProgress(50);
											  finalobj.put("rating", pnt);
											  try {
												  finalobj.save();
											  } catch (ParseException e) {
												  e.printStackTrace();
											  }

											  progress.setProgress(100);
											  progress.dismiss();
											  // send to our Handler
											  Message msg = new Message();
											  msg.obj =Double.toString(pnt);
											  dataHandler.sendMessage(msg);
										  }
									  }

									  ).start();


//				textView.setText(Double.toString(pnt) + "/10");


								  }
							  }

		);
			b2.setOnClickListener(new View.OnClickListener()

								  {
									  @Override
									  public void onClick(View view) {
										  dialog.dismiss();
									  }
								  }

			);
			dialog.show();

		}

	}

