package com.ReactionApps.Snowlloons;

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