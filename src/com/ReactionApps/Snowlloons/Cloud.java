package com.ReactionApps.Snowlloons;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class Cloud extends Precipitate {

	private boolean left;
	private int slowDown;
	
	Cloud(Point screenSize, Drawable graphic, boolean left)
	{
		super(screenSize, graphic, null, true, true);
		int resize = rannum.nextInt()%15;
		size.x = size.x + resize;
		size.y = size.y + resize;
		this.left = left;
		
		pt.y = Math.abs(rannum.nextInt()%(screen.y/2));

		slowDown = 0;
		dxdy.y = 0;
		dxdy.x = Math.abs(rannum.nextInt()%5)+1;
		
		if(left)
		{
			pt.x = size.x*-1;
		}
		else
		{
			pt.x = screen.x + size.x;
			dxdy.x *= -1;
		}
	}

	public Point getPosition()
	{
		return pt;
	};
	
	public void setPosition(Point location)
	{
		pt = location;
	};
	
	protected void updatePosition()
	{
		slowDown++;
		if(slowDown > Math.abs(dxdy.x))
		{
				if(left)
					pt.x++;
				else
					pt.x--;
				slowDown=0;
		};

		pt.y += dxdy.y;
		
		if(left && pt.x > (screen.x + size.x))
			pt.x = size.x*-1;
		else if(pt.x < size.x*-1)
			pt.x = screen.x + size.x;
	}
	
	protected void updateScreenSize(Point screenSize)
	{
		double tmpX =((double)(pt.x)/screen.x)*screenSize.x;
		double tmpY =((double)(pt.y)/screen.y)*screenSize.y;
		pt.x = (int) tmpX;
		pt.y = (int) tmpY;
		screen = screenSize;
		
		if(pt.y > (screen.y/2))
			pt.y = Math.abs(rannum.nextInt()%(screen.y/2));
	};
}
