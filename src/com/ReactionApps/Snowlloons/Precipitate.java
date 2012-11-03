package com.ReactionApps.Snowlloons;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.ColorMatrix;
import android.graphics.drawable.Drawable;
import android.graphics.ColorMatrixColorFilter;

public class Precipitate {

	private boolean up;
	private boolean animate;
	private int frame;
	private double radius;
	private Point loc;
	protected Point size;
	protected Point pt;
	protected Point dxdy;
	protected Point screen;
	protected static Random rannum = new Random();
	private Drawable graphic;
	private Drawable []touchAnim;
	
	Precipitate(Point screenSize, Drawable pic, Drawable []touch, boolean rising, boolean fullSize)
	{
		screen = screenSize;
		graphic = pic;
		up = rising;
		touchAnim = touch;
		animate = false;
		
		size = new Point();
		
		pt = new Point();
		pt.x = Math.abs(rannum.nextInt()%screen.x);
		
		dxdy = new Point();
		dxdy.x = rannum.nextInt()%2;
		
		if(fullSize)
		{
			size.x = this.graphic.getIntrinsicWidth();
			size.y = this.graphic.getIntrinsicHeight();
			dxdy.y = Math.abs(rannum.nextInt()%8)+1;
		}
		else
		{
			size.x = (int) (this.graphic.getIntrinsicWidth()*0.75);
			size.y = (int) (this.graphic.getIntrinsicHeight()*0.75);
			dxdy.y = Math.abs(rannum.nextInt()%2)+1;
		};
		if(size.x > size.y)
			radius = size.y/2;
		else
			radius = size.x/2;

		if(up)
		{
			pt.y = screen.y + size.y;
			dxdy.y *= -1;
		}
		else
			pt.y = size.y*-1;
	};
	
	protected void updatePosition()
	{
		pt.x += dxdy.x;
		pt.y += dxdy.y;
		
		if(up && pt.y < size.y*-1)
		{
			pt.y = screen.y + size.y;
			pt.x = Math.abs(rannum.nextInt()%screen.x);
		}
		else if(pt.y > (screen.y + size.y))
		{
			pt.y = size.y*-1;
			pt.x = Math.abs(rannum.nextInt()%screen.x);
		}
	}
	
	public void draw(Canvas canvas)
	{
		if(animate)
		{
			
			touchAnim[frame].setBounds(pt.x-(size.x/2), pt.y-(size.y/2), pt.x+(size.x/2), pt.y+(size.y/2));
			touchAnim[frame].draw(canvas);
			
			frame++;
			if(frame>=touchAnim.length)
			{
				animate = false;
				//force the reset of the point data
				updateScreenSize(screen);
			};
		}
		else
		{
			updatePosition();
			graphic.setBounds(pt.x-(size.x/2), pt.y-(size.y/2), pt.x+(size.x/2), pt.y+(size.y/2));
			graphic.draw(canvas);
		};
	};
	
	protected void updateScreenSize(Point screenSize)
	{
		screen = screenSize;
		if(up)
		{
			pt.y = screen.y + size.y;
		}
		else
			pt.y = size.y*-1;

		pt.x = Math.abs(rannum.nextInt()%screen.x);
	};
	
	public void updateColor(ColorMatrix colorWheel)
	{
		graphic.setColorFilter(new ColorMatrixColorFilter(colorWheel));
	};
	
	public void updateGraphic(Drawable pic)
	{
		graphic = pic;
	};

	public void updateGraphic(Drawable pic, Drawable []touch)
	{
		graphic = pic;
		touchAnim = touch;
	};

	
	public void touchGraphic(Point location, int rad)
	{
		
		if(touchAnim == null)
			return;
		loc = location;
		double distance =((pt.x-loc.x)*(pt.x-loc.x)) + ((pt.y-loc.y)*(pt.y-loc.y));
		double radii = (rad + radius)*(rad + radius);

		//overlapping
		if(radii>distance)
		{
			animate = true;
			frame = 0;
		};
	};
}
