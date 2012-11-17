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
import android.app.Application;
import android.os.Handler;
import android.graphics.Point;

public class Snowlloons extends Application {

	private static Snowlloons singleton;
	private Point clouds[];
	private Point oldRes;
	public static final String APP_PREFERENCES = "SKY_SETTINGS";
	public static final String COLORS = "BALLOON_COLOR";
	public static final String POP = "BALLOON_POP";

	
	public static Snowlloons getInstance()
	{
		return singleton;
	}
	
	@Override
	public final void onCreate()
	{
		super.onCreate();
		singleton = this;
		clouds = null;
		oldRes = null;
	}
	
	public void saveClouds(Point locations[])
	{
		clouds = locations;
	}
	
	public Point[] restoreClouds()
	{
		return clouds;
	}
	
	public void setScreenResolution(Point pt)
	{
		oldRes = pt;
	}
	
	public Point getScreenResolution()
	{
		return oldRes;
	}
}