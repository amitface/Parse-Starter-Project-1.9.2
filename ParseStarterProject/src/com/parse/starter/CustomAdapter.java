package com.parse.starter;

import android.app.Dialog;
import android.app.ListFragment;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
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


public class CustomAdapter extends ParseQueryAdapter<Photo> {

	float points= (float) 0.0;float pnt = (float) 0.0;
	ParseUser current =ParseUser.getCurrentUser();

	public CustomAdapter(Context context, final List<String> rt) {
		// Use the QueryFactory to construct a PQA that will only show
		// Todos marked as high-pri
		super(context, new ParseQueryAdapter.QueryFactory<Photo>() {
			public ParseQuery create() {
				ParseQuery query = new ParseQuery("photo");
				query.whereContainedIn("userId", rt);
				query.addDescendingOrder("createdAt");
				//query.whereEqualTo("highPri", true);
				List<String> filter = new ArrayList<String>();
				filter.add("userId");
				filter.add("pic");

				query.selectKeys(filter);


				return query;

				}
			}

			);
		}


		static class Holder{
		ImageButton imageButton;
		ParseImageView todoImage;
		TextView titleTextView,timestampView,titleTextViewRating;
	}

	// Customize the layout by overriding getItemView
	@Override
	public View getItemView( Photo object, View v, ViewGroup parent) {


			 Holder holder=null ;
		if (v == null) {
			v = View.inflate(getContext(), R.layout.urgent_item, null);
			holder= new Holder();
			holder.todoImage = (ParseImageView) v.findViewById(R.id.img);
			holder.titleTextView = (TextView) v.findViewById(R.id.name);
			holder.titleTextViewRating =(TextView)v.findViewById(R.id.rating);
			holder.timestampView = (TextView) v.findViewById(R.id.timestamp);
			holder.imageButton=(ImageButton) v.findViewById(R.id.imageButton);
			v.setTag(holder);
		}
		else
		{
			holder=(Holder)v.getTag();
		}
//		object.pinInBackground();

		super.getItemView(object, v, parent);

		// Add and download the image

		ParseFile imageFile = object.getParseFile("pic");
		if (imageFile != null) {
			holder.todoImage.setParseFile(imageFile);
			holder.todoImage.loadInBackground();
		}

		// Add the title view

		holder.titleTextView.setText(object.getString("userId"));

		holder.timestampView.setText(object.getCreatedAt().toString());
//		holder.titleTextViewRating.setText("0.0");

		// Add a reminder of how long this item has been outstanding





//		ParseQuery<ParseObject> obj = ParseQuery.getQuery("photoRating");
//
//		obj.whereEqualTo("photoId", object.getObjectId().toString());
//		obj.findInBackground(new FindCallback<ParseObject>() {
//			@Override
//			public void done(List<ParseObject> list, ParseException e) {
//					pnt=(float)0.0;
//				if (e == null) {
//
//					if (!list.isEmpty()) {
//						for (ParseObject b : list) {
//							pnt += b.getInt("rating");
//						}
//						pnt=pnt / list.size();
//
//					} else {
//
//						pnt=(float)0.0;
//					}
//
//				}
//			}
//		});
//		List list=new ArrayList<String>();
//		list.add("rating");
//		ParseQuery<ParseObject> objectParseQuery=object.getRelation("photoRelation").getQuery();
//		objectParseQuery.selectKeys(list);
//		objectParseQuery.findInBackground(new FindCallback<ParseObject>() {
//			@Override
//			public void done(List<ParseObject> list, ParseException e) {
//				pnt=(float)0.0;
//				if(e==null)
//				{
//					for(ParseObject p:list)
//					{
//						pnt=pnt+p.getInt("rating");
//					}
//					pnt=pnt/list.size();
//				}else
//				{
//					Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
//
//				}
//			}
//		});
		holder.titleTextViewRating.setText(Float.toString(pnt));

		holder.imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				showDialog(object,points);
//				showDialog(object.getObjectId().toString(),points);

				return;
			}
		});

		return v;
	}



	public void showDialog(final ParseObject obj2, float points)
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
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				final ParseQuery<ParseObject> obj = ParseQuery.getQuery("photoRating");
//				final ParseObject object = new ParseObject("photoRating");
				obj.whereEqualTo("userId", current.getUsername().toString());
				obj.whereEqualTo("photoId", obj2.getObjectId().toString());
				obj.getFirstInBackground(new GetCallback<ParseObject>() {
					@Override
					public void done(ParseObject parseObject, ParseException e) {
						if (e == null) {


//									int i = (Integer) b.get("rating");
							parseObject.put("rating", np.getValue());
							parseObject.saveInBackground();

							ParseRelation<ParseObject> relation = obj2.getRelation("photoRelation");
//

							relation.add(parseObject);
							obj2.saveInBackground(new SaveCallback() {
								@Override
								public void done(ParseException e) {
									if (e == null) {
										Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();

									} else {
										Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
									}
								}
							});
						} else {
							final ParseObject object = new ParseObject("photoRating");

							object.put("userId", current.getUsername().toString());
							object.put("photoId", obj2.getObjectId().toString());
							object.put("rating", np.getValue());
							object.saveInBackground(new SaveCallback() {
								@Override
								public void done(ParseException e) {
									if (e == null) {
//										ParseObject obj1 = new ParseObject("photo");
										ParseRelation<ParseObject> relation = obj2.getRelation("photoRelation");
//										obj1.setObjectId(obj2.getObjectId());

										relation.add(object);
										obj2.saveInBackground(new SaveCallback() {
											@Override
											public void done(ParseException e) {
												if (e == null) {
													Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();

												} else {
													Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
												}
											}
										});
									} else {
										Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
									}
								}
							});



						}
					}


				});


				dialog.dismiss();
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

}

