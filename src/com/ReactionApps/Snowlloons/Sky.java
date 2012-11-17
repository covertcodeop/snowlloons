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
import java.util.ArrayList;

import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.service.wallpaper.WallpaperService;

public class Sky extends WallpaperService {

    //why do i have the settings up here and not in atmosphere..
	private boolean currentPopSetting;
	private Point screenDimensions;
	
	@Override
	public Engine onCreateEngine() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display screen = wm.getDefaultDisplay();
		screenDimensions = new Point(screen.getWidth(), screen.getHeight());

		return new Atmosphere();
	}

//****************************************************************************
	private class Atmosphere extends Engine implements Runnable,
	android.content.SharedPreferences.OnSharedPreferenceChangeListener {
		
		private boolean isVisible;
		private final int SKY_COLOR = 0xff4FD4FF;
		private Drawable gradient_sky;
		private Drawable []balloon_colors;
		private ArrayList<Drawable> balloon_pop;
		private final Handler atmoHandler = new Handler();
		private SharedPreferences panic;
		private int prevAction;
		
		private final int CLOUD_FRONT_MAX = 2;
		private final int CLOUD_BACK_MAX = 8;
		private Cloud []fluffyFront;
		private Cloud []fluffyBack;
		
		private final int BALLOON_FRONT_MAX = 20;
		private final int BALLOON_BACK_MAX = 20;
		private Precipitate []balloonsFront;
		private Precipitate []balloonsBack;
		
		Atmosphere() {

			
			gradient_sky = getResources().getDrawable(R.drawable.sky);
			Drawable img = getResources().getDrawable(R.drawable.cloud);
			balloon_colors = new Drawable[10];
			setupGraphics(balloon_colors);
			
			balloon_pop = new ArrayList<Drawable>();
			setupPopAnim(balloon_pop);

			Snowlloons global = Snowlloons.getInstance();
			
			Point tmpRes = global.getScreenResolution();
			if(tmpRes == null)
			{
				tmpRes = screenDimensions;
			}
			else
			{
				screenDimensions = tmpRes;
			};

			fluffyFront = new Cloud[CLOUD_FRONT_MAX];
			for(int num=0; num<CLOUD_FRONT_MAX; num++)
				fluffyFront[num] = new Cloud(screenDimensions, img, true);
			fluffyBack = new Cloud[CLOUD_BACK_MAX];
			for(int num=0; num<CLOUD_BACK_MAX; num++)
				fluffyBack[num] = new Cloud(screenDimensions, img, true);

			Point pt[] = global.restoreClouds();
			if(pt != null)
			{
				int saved=0;
	            for(int num=0; num<CLOUD_FRONT_MAX; num++,saved++)
					fluffyFront[num].setPosition(pt[saved]);
	            for(int num=0; num<CLOUD_BACK_MAX; num++,saved++)
					fluffyBack[num].setPosition(pt[saved]);
			};
			
			
			balloonsFront = new Precipitate[BALLOON_FRONT_MAX];
			for(int num=0; num<BALLOON_FRONT_MAX; num++)
			{
				int within10 = num%10;
				Drawable []tmpAnim = new Drawable[3];
				balloon_pop.subList(within10*3, within10*3+3).toArray(tmpAnim);
				balloonsFront[num] = new Precipitate(screenDimensions, balloon_colors[within10], tmpAnim, true, true);
			};
			balloonsBack = new Precipitate[BALLOON_BACK_MAX];
			for(int num=0; num<BALLOON_BACK_MAX; num++)
			{
				int within10 = num%10;
				Drawable []tmpAnim = new Drawable[3];
				balloon_pop.subList(within10*3, within10*3+3).toArray(tmpAnim);
				balloonsBack[num] = new Precipitate(screenDimensions, balloon_colors[within10], tmpAnim, true, false);
			};
			
			panic = Sky.this.getSharedPreferences(Snowlloons.APP_PREFERENCES, Activity.MODE_PRIVATE);
			panic.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(panic, null);
            int current = panic.getInt(Snowlloons.COLORS, 10);
            balloonFactory(current);
            currentPopSetting = panic.getBoolean(Snowlloons.POP, false);
		};
		
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.setTouchEventsEnabled(true);
        }
        
        @Override
        public void onDestroy() {
            super.onDestroy();
            panic.unregisterOnSharedPreferenceChangeListener(this);
            atmoHandler.removeCallbacks(this);
            
            Point saveClouds[] = new Point[CLOUD_FRONT_MAX + CLOUD_BACK_MAX];
            int saved = 0;
            Snowlloons global = Snowlloons.getInstance();
            for(int num=0; num<CLOUD_FRONT_MAX; num++,saved++)
				saveClouds[saved] = fluffyFront[num].getPosition();
            for(int num=0; num<CLOUD_BACK_MAX; num++,saved++)
				saveClouds[saved] = fluffyBack[num].getPosition();
            global.saveClouds(saveClouds);
            
            global.setScreenResolution(screenDimensions);
        }


        public void onTouchEvent(MotionEvent event) {
        	
        	if(currentPopSetting)
        		return;
        	if(event.getAction() == MotionEvent.ACTION_UP && prevAction == MotionEvent.ACTION_DOWN)
        	{
        		Point pt = new Point();
        		pt.x = (int) event.getX();
        		pt.y = (int) event.getY();
    			for(int num=0; num<BALLOON_FRONT_MAX; num++)
    			{
    				balloonsFront[num].touchGraphic(pt, 10);
    			};
    			for(int num=0; num<BALLOON_BACK_MAX; num++)
    			{
    				balloonsBack[num].touchGraphic(pt, 10);
    			};
        	};
        	if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN)
        	{
        		prevAction = event.getAction();
        	};
            super.onTouchEvent(event);
        }

		
        @Override
        public void onVisibilityChanged(boolean visible) {
        	isVisible = visible;

        	
        	if(isVisible)
        		drawFrame();
        	else
        		atmoHandler.removeCallbacks(this);
        }
        
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);

            //Crash Report: Aug 15, 2010 5:31:11 PM: divide by zero
            //crash here because one? both? of width, height was zero
            //meaning the surface changed in another manner besides the dimensions
            if(width <= 0 && height <= 0)
            	return;
            if(width <= 0)
            	width = screenDimensions.x;
            if(height <= 0)
            	height = screenDimensions.y;
            
            Point newPt = new Point();
            newPt.x = width;
            newPt.y = height;
            screenDimensions = newPt;
            
			for(int num=0; num<BALLOON_BACK_MAX; num++)
				balloonsBack[num].updateScreenSize(screenDimensions);
			for(int num=0; num<CLOUD_BACK_MAX; num++)
				fluffyBack[num].updateScreenSize(screenDimensions);
			for(int num=0; num<BALLOON_FRONT_MAX; num++)
				balloonsFront[num].updateScreenSize(screenDimensions);
			for(int num=0; num<CLOUD_FRONT_MAX; num++)
				fluffyFront[num].updateScreenSize(screenDimensions);

            drawFrame();
        }

		public void run() {
			// TODO Auto-generated method stub
			drawFrame();			
		}
        
		private void drawFrame()
		{
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
              
            	c = holder.lockCanvas();
                if (c != null)
                    drawSomething(c);
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }
			
            // Reschedule the next redraw
            atmoHandler.removeCallbacks(this);
            if (isVisible) {
                atmoHandler.postDelayed(this, 1000 / 10);
            }
		}
		private void drawSomething(Canvas c)
		{
            c.save();
            c.drawColor(SKY_COLOR);
            
            gradient_sky.setBounds(0, 0, screenDimensions.x, screenDimensions.y);
            gradient_sky.draw(c);

			for(int num=0; num<BALLOON_BACK_MAX; num++)
				balloonsBack[num].draw(c);
            for(int num=0; num<CLOUD_BACK_MAX; num++)
				fluffyBack[num].draw(c);
			for(int num=0; num<BALLOON_FRONT_MAX; num++)
				balloonsFront[num].draw(c);
			for(int num=0; num<CLOUD_FRONT_MAX; num++)
				fluffyFront[num].draw(c);
            
            c.restore();
		}
		
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			// TODO Auto-generated method stub
			
            //random is the default
            int currentColor = sharedPreferences.getInt(Snowlloons.COLORS, 10);
            balloonFactory(currentColor);
            
            currentPopSetting = sharedPreferences.getBoolean(Snowlloons.POP, false);
		};
		
		private void setupGraphics(Drawable [] graphics)
		{
			graphics[0] = getResources().getDrawable(R.drawable.b);		
			graphics[1] = getResources().getDrawable(R.drawable.c);
			graphics[2] = getResources().getDrawable(R.drawable.db);
			graphics[3] = getResources().getDrawable(R.drawable.f);
			graphics[4] = getResources().getDrawable(R.drawable.g);
			graphics[5] = getResources().getDrawable(R.drawable.lg);
			graphics[6] = getResources().getDrawable(R.drawable.o);
			graphics[7] = getResources().getDrawable(R.drawable.p);
			graphics[8] = getResources().getDrawable(R.drawable.r);
			graphics[9] = getResources().getDrawable(R.drawable.y);
		};
		
		private void setupPopAnim(ArrayList<Drawable> anim)
		{
			anim.add(getResources().getDrawable(R.drawable.b2));
			anim.add(getResources().getDrawable(R.drawable.b3));
			anim.add(getResources().getDrawable(R.drawable.b4));
			
			anim.add(getResources().getDrawable(R.drawable.c2));
			anim.add(getResources().getDrawable(R.drawable.c3));
			anim.add(getResources().getDrawable(R.drawable.c4));
			
			anim.add(getResources().getDrawable(R.drawable.db2));
			anim.add(getResources().getDrawable(R.drawable.db3));
			anim.add(getResources().getDrawable(R.drawable.db4));
			
			anim.add(getResources().getDrawable(R.drawable.f2));
			anim.add(getResources().getDrawable(R.drawable.f3));
			anim.add(getResources().getDrawable(R.drawable.f4));
			
			anim.add(getResources().getDrawable(R.drawable.g2));
			anim.add(getResources().getDrawable(R.drawable.g3));
			anim.add(getResources().getDrawable(R.drawable.g4));
			
			anim.add(getResources().getDrawable(R.drawable.lg2));
			anim.add(getResources().getDrawable(R.drawable.lg3));
			anim.add(getResources().getDrawable(R.drawable.lg4));
			
			anim.add(getResources().getDrawable(R.drawable.o2));
			anim.add(getResources().getDrawable(R.drawable.o3));
			anim.add(getResources().getDrawable(R.drawable.o4));
			
			anim.add(getResources().getDrawable(R.drawable.p2));
			anim.add(getResources().getDrawable(R.drawable.p3));
			anim.add(getResources().getDrawable(R.drawable.p4));
			
			anim.add(getResources().getDrawable(R.drawable.r2));
			anim.add(getResources().getDrawable(R.drawable.r3));
			anim.add(getResources().getDrawable(R.drawable.r4));
			
			anim.add(getResources().getDrawable(R.drawable.y2));
			anim.add(getResources().getDrawable(R.drawable.y3));
			anim.add(getResources().getDrawable(R.drawable.y4));
			
		};

		private void balloonFactory(int setting)
		{
			Drawable []tmpAnim = new Drawable[3];
			switch(setting)
			{
			case 0:
				balloon_pop.subList(0, 3).toArray(tmpAnim);
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					balloonsBack[num].updateGraphic(balloon_colors[0], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					balloonsFront[num].updateGraphic(balloon_colors[0], tmpAnim);
				};
				break;
			case 1:
				balloon_pop.subList(3, 6).toArray(tmpAnim);
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					balloonsBack[num].updateGraphic(balloon_colors[1], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					balloonsFront[num].updateGraphic(balloon_colors[1], tmpAnim);
				};
				break;
			case 2:
				balloon_pop.subList(6, 9).toArray(tmpAnim);
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					balloonsBack[num].updateGraphic(balloon_colors[2], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					balloonsFront[num].updateGraphic(balloon_colors[2], tmpAnim);
				};
				break;
			case 3:
				balloon_pop.subList(9, 12).toArray(tmpAnim);
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					balloonsBack[num].updateGraphic(balloon_colors[3], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					balloonsFront[num].updateGraphic(balloon_colors[3], tmpAnim);
				};
				break;
			case 4:
				balloon_pop.subList(12, 15).toArray(tmpAnim);
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					balloonsBack[num].updateGraphic(balloon_colors[4], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					balloonsFront[num].updateGraphic(balloon_colors[4], tmpAnim);
				};
				break;
			case 5:
				balloon_pop.subList(15, 18).toArray(tmpAnim);
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					balloonsBack[num].updateGraphic(balloon_colors[5], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					balloonsFront[num].updateGraphic(balloon_colors[5], tmpAnim);
				};
				break;
			case 6:
				balloon_pop.subList(18, 21).toArray(tmpAnim);
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					balloonsBack[num].updateGraphic(balloon_colors[6], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					balloonsFront[num].updateGraphic(balloon_colors[6], tmpAnim);
				};
				break;
			case 7:
				balloon_pop.subList(21, 24).toArray(tmpAnim);
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					balloonsBack[num].updateGraphic(balloon_colors[7], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					balloonsFront[num].updateGraphic(balloon_colors[7], tmpAnim);
				};
				break;
			case 8:
				balloon_pop.subList(24, 27).toArray(tmpAnim);
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					balloonsBack[num].updateGraphic(balloon_colors[8], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					balloonsFront[num].updateGraphic(balloon_colors[8], tmpAnim);
				};
				break;
			case 9:
				balloon_pop.subList(27, 30).toArray(tmpAnim);
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					balloonsBack[num].updateGraphic(balloon_colors[9], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					balloonsFront[num].updateGraphic(balloon_colors[9], tmpAnim);
				};
				break;
			case 10:
				for(int num=0; num<BALLOON_BACK_MAX; num++)
				{
					int within10 = num%10;
					tmpAnim = new Drawable[3];
					balloon_pop.subList(within10*3, within10*3+3).toArray(tmpAnim);
					balloonsBack[num].updateGraphic(balloon_colors[within10], tmpAnim);
				};
				for(int num=0; num<BALLOON_FRONT_MAX; num++)
				{
					int within10 = num%10;
					tmpAnim = new Drawable[3];
					balloon_pop.subList(within10*3, within10*3+3).toArray(tmpAnim);
					balloonsFront[num].updateGraphic(balloon_colors[within10], tmpAnim);
				};
				break;
			default:
				break;
			};
		}
	}
}
