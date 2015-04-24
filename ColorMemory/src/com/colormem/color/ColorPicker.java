package com.colormem.color;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class ColorPicker {

	Paint paint; 
	Set<Object> colorSet;
	int rows,columns;
	int colorSequence[];
	public ColorPicker(int rows, int columns) {

		colorSequence = new int[rows*columns];
		paint = new Paint();
		colorSet = new HashSet<>();
		this.rows=rows;
		this.columns=columns;
	}
	private void fillColor(){
		Random rnd = new Random();
		while(colorSet.size()<rows*columns)
		{
				colorSet.add(ColorArray.Colors[rnd.nextInt(ColorArray.Colors.length)]);
		}
	}
	public int[] getColor(){
		fillColor();
		Iterator<Object> iterator = colorSet.iterator();
		int i=0;
		while(iterator.hasNext()){
			String color = (String) iterator.next();
			colorSequence[i]=Color.parseColor(color);
			Log.d("ColorSequence",color);
			i++;
		}
		 return colorSequence;
	}
	
}
