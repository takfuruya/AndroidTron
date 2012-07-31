package com.tfuruya.tron;


public class TronAction {
	
	public static final int LEFT = 0;
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;
	
	protected int direction;
	
	public TronAction() {
		direction = DOWN;
	}
	
	public TronAction(int dir) {
		direction = dir;
	}
	
	public int set(int dir) {
		direction = dir;
		return dir;
	}
	
	public int get() {
		return direction;
	}
}
