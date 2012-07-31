package com.tfuruya.mytron;

import com.tfuruya.tron.R;
import com.tfuruya.tron.TronAction;
import com.tfuruya.tron.TronController;
import com.tfuruya.tron.TronData;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TouchTronController extends TronController implements OnTouchListener{
	
	private float lastTouchX = 0;
	private float lastTouchY = 0;
	private int direction;
	private TronData data;
	private boolean isFirstMove = false;
	
	
	public TouchTronController(int playerIndex, View view) {
		super(playerIndex);
		view.setOnTouchListener(this);
		reset(null);
	}
	
	
	@Override
	public TronAction getAction() {
		action.set(direction);
		return action;
	}
	
	
	@Override
	protected void compute(TronData d) {
		data = d;
	}
	
	
	@Override
	public void reset(TronData d) {
		if (d == null) {
			direction = new TronAction().get();
			return;
		}
		direction = d.getNewestAction()[myPlayerIndex].get();
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.the_canvas) {
			
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				lastTouchX = event.getRawX();
				lastTouchY = event.getRawY();
				isFirstMove = true;
				break;
				
			case MotionEvent.ACTION_MOVE:
				if (!isFirstMove) break;
				isFirstMove = false;
				
				// calculate angle
				float deltaX = event.getRawX() - lastTouchX;
				float deltaY = lastTouchY - event.getRawY();
				double angleRadians = Math.atan2(deltaY, deltaX);
				switch(data.getNewestAction()[myPlayerIndex].get()) {
				case TronAction.RIGHT:
				case TronAction.LEFT:
					if (0 < angleRadians && angleRadians < Math.PI) {
						direction = TronAction.UP;
						break;
					}
					direction = TronAction.DOWN;
					break;
				case TronAction.UP:
				case TronAction.DOWN:
					if (-Math.PI/2 < angleRadians && angleRadians < Math.PI/2) {
						direction = TronAction.RIGHT;
						break;
					}
					direction = TronAction.LEFT;
					break;
				}
				break;
				
			case MotionEvent.ACTION_UP:
				break;
			}
			
			return true;
		}
		return false;
	}
}
