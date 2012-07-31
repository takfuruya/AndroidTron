package com.tronservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public abstract class MessengerService extends Service {
	
	/**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    
    protected static final int LEFT = 0;
    protected static final int UP = 1;
    protected static final int RIGHT = 2;
    protected static final int DOWN = 3;
    
	
    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
    	
    	// Called every time message (containing game state data) arrives
    	// from Tron game app
        @Override
        public void handleMessage(Message msg) {
        	
        	// Get message content
        	byte[] mapData = msg.getData().getByteArray("TRON_MAP_DATA");
        	int[] info = msg.getData().getIntArray("TRON_INFO");
        	int[] x = msg.getData().getIntArray("TRON_X");
        	int[] y = msg.getData().getIntArray("TRON_Y");
        	
        	// Compute which direction to go
        	int dir = MessengerService.this.compute(info, mapData, x, y);
        	
        	// Create reply message
        	Message replyMessage = Message.obtain(null, dir);
        	
        	// Send reply message back to Tron game app
        	try {
        		msg.replyTo.send(replyMessage);
        	} catch (RemoteException e) {
        		// Tell user that Tron game app is dead
        		Context context = MessengerService.this;
        		int resId = R.string.remote_exception;
        		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        	}
        }
    }
    
    
    /** Compute the next direction given the Tron game state data */
    protected abstract int compute(int[] info, byte[] mapData, int[] x, int[] y);
    

    @Override
    public void onCreate() {
        // Tell the user we started and we are ready to receive messages
        // Toast.makeText(this, R.string.remote_service_started, Toast.LENGTH_SHORT).show();
    }
    
    
    @Override
    public void onDestroy() {
        // Tell the user we stopped
        // Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
    }
    
    
    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}