package com.tfuruya.tron;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

public class TronData {
	
	private int[] posX;
	private int[] posY;
	public boolean isGameOver;
	private int numColumn;
	private int numRow;
	private int numPlayer;
	private int numDead;
	private boolean[] isDead;
	private byte[] data;
	private TronAction[] newestAction;
    private List<List<Point>> corner;
    
    class Point{
    	public int x;
    	public int y;
    	public Point(int _x, int _y) {
    		x = _x;
    		y = _y;
    	}
    }
    
    
	public TronData(int column, int row, int player) {
		numColumn = column;
		numRow = row;
		numPlayer = player;
		numDead = 0;
		isDead = new boolean[numPlayer];
		for (int j=0; j<numPlayer; j++) {
			isDead[j] = false;
		}
		data = new byte[numColumn * numRow];
		
		// initialize initial position
		posX = new int[numPlayer];
		posY = new int[numPlayer];
		switch(numPlayer) {
			case 1:
				posX[0] = numColumn/2;
				posY[0] = numRow/6;
				break;
			case 2:
				posX[0] = numColumn/2;
				posY[0] = numRow/6;
				posX[1] = numColumn/2;
				posY[1] = 5 * numRow/6;
				break;
			case 3:
				posX[0] = numColumn/2;
				posY[0] = numRow/6;
				posX[1] = numColumn/2;
				posY[1] = 5 * numRow/6;
				posX[2] = numColumn/6;
				posY[2] = numRow/2;
				break;
			case 4:
				posX[0] = numColumn/2;
				posY[0] = numRow/6;
				posX[1] = numColumn/2;
				posY[1] = 5 * numRow/6;
				posX[2] = numColumn/6;
				posY[2] = numRow/2;
				posX[3] = 5 * numColumn/6;
				posY[3] = numRow/2;
				break;
			case 5:
				posX[0] = numColumn/3;
				posY[0] = numRow/6;
				posX[1] = numColumn/2;
				posY[1] = 5 * numRow/6;
				posX[2] = numColumn/6;
				posY[2] = numRow/2;
				posX[3] = 5 * numColumn/6;
				posY[3] = numRow/2;
				posX[4] = 2 * numColumn/3;
				posY[4] = numRow/6;
				break;
			case 6:
				posX[0] = numColumn/3;
				posY[0] = numRow/6;
				posX[1] = numColumn/3;
				posY[1] = 5 * numRow/6;
				posX[2] = numColumn/6;
				posY[2] = numRow/2;
				posX[3] = 5 * numColumn/6;
				posY[3] = numRow/2;
				posX[4] = 2 * numColumn/3;
				posY[4] = numRow/6;
				posX[5] = 2 * numColumn/3;
				posY[5] = 5 * numRow/6;
				break;
			case 7:
				posX[0] = numColumn/3;
				posY[0] = numRow/6;
				posX[1] = numColumn/3;
				posY[1] = 5 * numRow/6;
				posX[2] = numColumn/6;
				posY[2] = numRow/3;
				posX[3] = 5 * numColumn/6;
				posY[3] = numRow/2;
				posX[4] = 2 * numColumn/3;
				posY[4] = numRow/6;
				posX[5] = 2 * numColumn/3;
				posY[5] = 5 * numRow/6;
				posX[6] = numColumn/6;
				posY[6] = 2 * numRow/3;
				break;
			case 8:
				posX[0] = numColumn/3;
				posY[0] = numRow/6;
				posX[1] = numColumn/3;
				posY[1] = 5 * numRow/6;
				posX[2] = numColumn/6;
				posY[2] = numRow/3;
				posX[3] = 5 * numColumn/6;
				posY[3] = numRow/3;
				posX[4] = 2 * numColumn/3;
				posY[4] = numRow/6;
				posX[5] = 2 * numColumn/3;
				posY[5] = 5 * numRow/6;
				posX[6] = numColumn/6;
				posY[6] = 2 * numRow/3;
				posX[7] = 5 * numColumn/6;
				posY[7] = 2 * numRow/3;
				break;
		}
		
		isGameOver = false;
		
		// initialize nested list: corner
		corner = new ArrayList<List<Point>>();
		for (int i=0; i<numPlayer; i++) {
			List<Point> temp = new LinkedList<Point>();
			Point p = new Point(posX[i], posY[i]);
			temp.add(p);
			corner.add(temp);
		}
		
		// initialize newest action
		newestAction = new TronAction[numPlayer];
		for (int j=0; j<numPlayer; j++) {
			newestAction[j] = new TronAction();
		}
		switch(numPlayer) {
			case 2:
				newestAction[1] = new TronAction(TronAction.UP);
				break;
			case 3:
				newestAction[1] = new TronAction(TronAction.UP);
				newestAction[2] = new TronAction(TronAction.RIGHT);
				break;
			case 4:
			case 5:
				newestAction[1] = new TronAction(TronAction.UP);
				newestAction[2] = new TronAction(TronAction.RIGHT);
				newestAction[3] = new TronAction(TronAction.LEFT);
				break;
			case 6:
				newestAction[1] = new TronAction(TronAction.UP);
				newestAction[2] = new TronAction(TronAction.RIGHT);
				newestAction[3] = new TronAction(TronAction.LEFT);
				newestAction[5] = new TronAction(TronAction.UP);
				break;
			case 7:
				newestAction[1] = new TronAction(TronAction.UP);
				newestAction[2] = new TronAction(TronAction.RIGHT);
				newestAction[3] = new TronAction(TronAction.LEFT);
				newestAction[5] = new TronAction(TronAction.UP);
				newestAction[6] = new TronAction(TronAction.RIGHT);
				break;
			case 8:
				newestAction[1] = new TronAction(TronAction.UP);
				newestAction[2] = new TronAction(TronAction.RIGHT);
				newestAction[3] = new TronAction(TronAction.LEFT);
				newestAction[5] = new TronAction(TronAction.UP);
				newestAction[6] = new TronAction(TronAction.RIGHT);
				newestAction[7] = new TronAction(TronAction.LEFT);
				break;
		}
		
		// occupy first points on data
		for (int i=0; i<numPlayer; i++) {
			data[posX[i] + numColumn * posY[i]] = 1;
		}
	}
	
	
	/** Set new point to occupy and the action taken to do that 
	 * Must make sure (x, y) is a valid point
	 */
	public void occupyNewPoint(int x, int y, TronAction action, int player) {
		data[x + numColumn * y] = 1;
		if (action.get() != newestAction[player].get()) {
			newestAction[player].set(action.get());
			corner.get(player).add(new Point(posX[player], posY[player]));
		}
		posX[player] = x;
		posY[player] = y;
	}
	
	
	public byte[] get() {
		return data;
	}
	
	
	public int[] getX() {
		return posX;
	}
	
	
	public int[] getY() {
		return posY;
	}
	
	
	public int getColumn() {
		return numColumn;
	}
	
	
	public int getRow() {
		return numRow;
	}
	
	
	public int getNumPlayer() {
		return numPlayer;
	}
	
	
	public TronAction[] getNewestAction() {
		return newestAction;
	}
	
	
	public List<Point> getCorner(int player) {
		return corner.get(player);
	}
	
	
	public void setDead(int player) {
		if (!isDead[player]) numDead++;
		isDead[player] = true;
		if (numDead >= numPlayer) {
			isGameOver = true;
		}
	}
	
	
	public boolean isDead(int player) {
		return isDead[player];
	}
	
	
	public boolean isValid(int x, int y) {
		boolean isOutside = (x < 0 || y < 0 || x >= numColumn || y >= numRow);
		if (isOutside) return false;
		
		boolean isOccupied = (data[x + numColumn*y] == 1);
		return (!isOccupied);
	}
}
