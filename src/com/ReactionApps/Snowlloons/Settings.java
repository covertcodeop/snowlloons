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
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CheckBox;
import android.content.SharedPreferences;

public class Settings extends Activity implements android.widget.AdapterView.OnItemClickListener,
android.widget.CompoundButton.OnCheckedChangeListener {
    /** Called when the activity is first created. */
	
	private String []colors;
	private int color_choice;
	private boolean pop_choice;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences options = this.getSharedPreferences(Snowlloons.APP_PREFERENCES, Activity.MODE_PRIVATE);
        color_choice = options.getInt(Snowlloons.COLORS, 10);
        pop_choice = options.getBoolean(Snowlloons.POP, false);
        
        ListView lv = (ListView) this.findViewById(R.id.options);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        colors = this.getResources().getStringArray(R.array.balloon_colors);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, colors));
        lv.setOnItemClickListener(this);
        lv.setItemChecked(color_choice, true);
        
        CheckBox cb = (CheckBox) this.findViewById(R.id.pop);
        cb.setOnCheckedChangeListener(this);
        cb.setChecked(pop_choice);
    }
//****************************************************************************
    public void onDestroy() {
    	SharedPreferences.Editor editor = this.getSharedPreferences(Snowlloons.APP_PREFERENCES, Activity.MODE_PRIVATE).edit();
        editor.putInt(Snowlloons.COLORS, color_choice);
        editor.putBoolean(Snowlloons.POP, pop_choice);
        editor.commit();

    	super.onDestroy();
    }
//****************************************************************************    
	//@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		color_choice = arg2;
	}
//****************************************************************************    
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		pop_choice = isChecked;
	};
//****************************************************************************
    @Override
    public void onPause()
    {
    	super.onPause();
    	SharedPreferences.Editor editor = this.getSharedPreferences(Snowlloons.APP_PREFERENCES, Activity.MODE_PRIVATE).edit();
        editor.putInt(Snowlloons.COLORS, color_choice);
        editor.putBoolean(Snowlloons.POP, pop_choice);
        editor.commit();
    };
//****************************************************************************
    @Override
    public void onResume()
    {
    	super.onResume();
    }
 //****************************************************************************    
}
