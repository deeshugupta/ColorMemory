package com.colormem.text;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UsersAdapter extends ArrayAdapter<String> {
	
	Typeface typeface;

	public UsersAdapter(Context context, int resource, List<String> objects, Typeface typeFace) {
		super(context, resource, objects);
		this.typeface = typeFace;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        SetTextFeatures.setFeatures(((TextView) v), typeface, 
       		 ((TextView)v).getText().toString(), 25f);

        return v;
}


	public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
         View v =super.getDropDownView(position, convertView, parent);

         SetTextFeatures.setFeatures(((TextView) v), typeface, 
           		 ((TextView)v).getText().toString(), 25f);

        return v;
}
}
