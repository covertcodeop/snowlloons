package com.ReactionApps.Snowlloons;

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