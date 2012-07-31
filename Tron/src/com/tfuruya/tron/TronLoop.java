package com.tfuruya.tron;

import java.util.ArrayList;

import com.tfuruya.mytron.ClientTronController;
import com.tfuruya.mytron.TouchTronController;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

public class TronLoop {
    
	private RefreshHandler redrawHandler = new RefreshHandler();
	
    public static final long MOVE_DELAY = 33;// Time between each frame (ms)
    private boolean isPaused; 				// Game state
    private TronView view;					// Where lines are drawn
    private TronController[] controller;	// Contains methods to control player
    private TronGame game;					// Has method to update data (game rule)
    private TronData data;					// Contains occupancy of each pixel
    private TronAction[] nextAction;		// Action to take in the coming turn
    private int numPlayer;					// Number of players present in game
    private ArrayList<Player> player;
    
    // Allows delay functionality and execute repeatedly with certain interval
    class RefreshHandler extends Handler {
    	
    	// Called on every frame
        @Override
        public void handleMessage(Message msg) {
        	TronLoop p = TronLoop.this;
        	
        	// If game is being played, step one frame and make sure this
        	// gets called again after MOVE_DELAY time
        	if (!p.isPaused) {
        		p.loop();
        		p.redrawHandler.sleep(MOVE_DELAY);
        	}
        }
        
        // Allows delayed call of handleMessage()
        public void sleep(long delayMillis) {
        	this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };
    
    
    public TronLoop(TronView v, Context c, ArrayList<Player> p) {
    	player = p;
    	numPlayer = p.size();
    	view = v;
    	controller = new TronController[numPlayer];
    	for (int i=0; i<numPlayer; i++) {
    		if (player.get(i).isRemoteService) {
    			controller[i] = new ClientTronController(i, player.get(i).messenger);
    		} else {
    			controller[i] = new TouchTronController(i, v);
    		}
    	}
        game = new TronGame(numPlayer);
    }
    
    
    public void startNewGame() {
    	// Reset game settings
    	isPaused = false;
    	data = new TronData(50, 50, numPlayer);
    	view.reset(data, numPlayer);
    	for (int i=0; i<numPlayer; i++) {
    		controller[i].reset(data);
    	}
    	game.reset(data, numPlayer);
    	nextAction = new TronAction[numPlayer];
    	
    	// Start calling loop at intervals
    	redrawHandler.sleep(MOVE_DELAY);
    }
    
    
    public boolean isPaused() {
    	return isPaused;
    }
    
    
    public void pause() {
    	isPaused = true;
    }
    
    
    // Continue paused game
    // Will not resume if game over - call startNewGame() instead
    public void resume() {
    	if (data.isGameOver) return;
    	isPaused = false;
    	redrawHandler.sleep(MOVE_DELAY);
    }
    
    
    // Called every frame unless paused
    public void loop() {
    	
    	for (int i=0; i<numPlayer; i++) {
    		nextAction[i] = controller[i].getAction();
    	}
    	
    	data = game.update(data, nextAction);
    	
    	// Game over
    	if (data.isGameOver) {
    		isPaused = true;
    		return;
    	}
    	
    	view.draw(data, nextAction);
    	
    	for (int i=0; i<numPlayer; i++) {
    		controller[i].compute(data);
    	}
    }
}
