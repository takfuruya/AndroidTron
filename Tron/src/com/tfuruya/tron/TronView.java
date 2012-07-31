package com.tfuruya.tron;

import java.util.Iterator;
import java.util.Random;

import com.tfuruya.tron.TronData.Point;

import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class TronView extends View {
	
	private Paint[] paint;		// For drawing lines
	private TronData data;		// Game state data (changes every frame)
	private int numColumn;		// Game state data: number of column
	private int numRow;			// Game state data: number of rows
	private int width;			// View width
	private int height; 		// View height
	private int numPlayer;		// Number of players in the game
	private int[] LINE_COLOR;
	
	
	public TronView(Context context, AttributeSet aSet) {
		super(context, aSet);
		
		LINE_COLOR = new int[7];
		LINE_COLOR[0] = Color.CYAN;
		LINE_COLOR[1] = Color.GREEN;
		LINE_COLOR[2] = Color.MAGENTA;
		LINE_COLOR[3] = Color.WHITE;
		LINE_COLOR[4] = Color.YELLOW;
		LINE_COLOR[5] = Color.RED;
		LINE_COLOR[6] = Color.BLUE;
	}
	
	
	@Override
    public void onDraw(Canvas canvas) {
		// onDraw is called when it first appears on screen
		// must ignore that case
		if (data == null) return;
		
		// draw background
		canvas.drawColor(Color.DKGRAY);
		
		// draw lines
		for (int i=0; i<numPlayer; i++) {
			int x1 = 0, y1 = 0, x2, y2;
			Iterator<Point> itr = data.getCorner(i).iterator();
			if (itr.hasNext()) {
				Point p = itr.next();
				x1 = scale(p.x, numColumn, width);
				y1 = scale(p.y, numRow, height);
			}
			while(itr.hasNext()) {
			    Point p2 = itr.next();
			    x2 = scale(p2.x, numColumn, width);
			    y2 = scale(p2.y, numRow, height);
			    canvas.drawLine(x1, y1, x2, y2, paint[i]);
			    x1 = x2;
			    y1 = y2;
			}
			x2 = scale(data.getX()[i], numColumn, width);
			y2 = scale(data.getY()[i], numRow, height);
			canvas.drawLine(x1, y1, x2, y2, paint[i]);
		}
		
    }
	
	
	private int scale(int x, int original, int target) {
		float scale = (float) target / original;
		return (int) (scale * (float) x);
	}
	
	
	public void draw(TronData d, TronAction[] action) {
		data = d;
        invalidate();
	}
	
	
	public void reset(TronData d, int player) {
		numPlayer = player;
		data = d;
		numColumn = data.getColumn();
		numRow = data.getRow();
		width = getWidth();
		height = getHeight();
		
		paint = new Paint[numPlayer];
		for (int i=0; i<numPlayer; i++) {
			paint[i] = new Paint();
			if (i >= LINE_COLOR.length) {
				Random rnd = new Random();
				paint[i].setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
			} else {
				paint[i].setColor(LINE_COLOR[i]);
			}
			paint[i].setStyle(Paint.Style.STROKE);
			paint[i].setStrokeWidth(5);
			paint[i].setStrokeJoin(Paint.Join.ROUND);
		}
	}
}
