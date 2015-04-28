package com.colormem.color;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View.OnTouchListener;
import android.widget.ImageView;


public class ColorHelper {

	public ColorHelper() {
		// TODO Auto-generated constructor stub
	}

	public static void setColor(ImageView imageView,int color, Resources res, OnTouchListener onTouchListener){
		
		ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
		shapeDrawable.getPaint().setColor(color);
		shapeDrawable.setIntrinsicHeight(imageView.getLayoutParams().height);
		shapeDrawable.setIntrinsicWidth(imageView.getLayoutParams().width);
		Bitmap bMap = drawableToBitmap(shapeDrawable);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(res, bMap);
		imageView.setBackground(bitmapDrawable);
		imageView.setOnTouchListener(onTouchListener);
	}
	
	private static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
}
