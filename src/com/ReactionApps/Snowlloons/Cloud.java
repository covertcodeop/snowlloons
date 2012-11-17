package com.ReactionApps.Snowlloons;
/* Copyright (C) 2010-2012 Zenko B. Klapko Jr.

This file is part of Balloons Live! Wallpaper.

Balloons Live! Wallpaper source code is free software; you can redistribute it
and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

Balloons Live! Wallpaper source code is distributed in the hope that it will be
useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Balloons Live! Wallpaper; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
*/
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
