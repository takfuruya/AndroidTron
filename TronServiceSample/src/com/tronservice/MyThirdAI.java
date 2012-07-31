package com.tronservice;


public class MyThirdAI extends MessengerService {
	
	@Override
	protected int compute(int[] info, byte[] mapData, int[] x, int[] y) {
		
	    // Store message content
	    int numColumn = info[0];
	    int numRow = info[1];
	    int numPlayer = info[2];
	    int meIndex = info[3];
	    
	    // Direction to return
	    int result = LEFT;
	    
	    int posX = x[meIndex];
	    int posY = y[meIndex];
	    
	    if (!isFilled(posX, posY - 1, numColumn, numRow, mapData)) result = UP;
	    if (!isFilled(posX + 1, posY, numColumn, numRow, mapData)) result = RIGHT;
	    if (!isFilled(posX, posY + 1, numColumn, numRow, mapData)) result = DOWN;
	    if (!isFilled(posX - 1, posY, numColumn, numRow, mapData)) result = LEFT;
	    
	    return result;
	}
	
    private boolean isFilled(int x, int y, int w, int h, byte[] map) {
    	if (x < 0 || x >= w || y < 0 || y >= h) return true;
    	return (map[y*w + x] == 1);
    }
    
    // use arraycopy to do better copying of byte array
}
