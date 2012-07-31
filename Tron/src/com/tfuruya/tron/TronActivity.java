package com.tfuruya.tron;

import java.util.ArrayList;

import com.tfuruya.tron.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageButton;
import android.view.View;
import android.view.View.OnClickListener;

public class TronActivity extends Activity implements OnClickListener {
	
	
	private TronLoop loop = null;
    private TronView view = null;
    private ArrayList<Player> player = null; 
    
    
	/** Called when the activity is first created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get selected players from MainMenuActivity
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null)
            player = bundle.getParcelableArrayList("player");
        
        // Get Button reference and make it click-able
        ImageButton b = (ImageButton) findViewById(R.id.image_button);
        b.setOnClickListener(this);
        
        // Get TronView reference and initialize loop
        view = (TronView) findViewById(R.id.the_canvas);
        loop = new TronLoop(view, getApplicationContext(), player);
    }
    
    
    /** Called when res/layout/main.xml has been created */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	
    	// Lost focus then pause loop
    	if (!hasFocus) {
    		loop.pause();
    		return;
    	}
    	
    	// Gained focus then resume paused game
    	if (loop.isPaused()) {
    		loop.resume();
    	}
    	
    	// Start game
    	loop.startNewGame();
    }
    
    
    /** Called when button is clicked - starts the game */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.image_button) {
			loop.startNewGame();
		}
	}
}