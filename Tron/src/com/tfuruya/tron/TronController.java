package com.tfuruya.tron;

/** Allows controlling the player (by touch, by AI, etc.) */
public abstract class TronController {
	
	protected int myPlayerIndex;	// player index (from 0) of which you are controlling
	protected TronAction action;
	
	
	public TronController(int playerIndex) {
		myPlayerIndex = playerIndex;
		action = new TronAction();
	}
	
	
	// getter for action
	public TronAction getAction() {
		return action;
	}
	
	
	// use this to implement a way to update action based on game data
	abstract protected void compute(TronData d);
	
	
	// called at the beginning of every game
	public void reset(TronData d) {
		action = d.getNewestAction()[myPlayerIndex];
	}
}
