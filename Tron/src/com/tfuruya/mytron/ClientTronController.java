package com.tfuruya.mytron;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.tfuruya.tron.R;
import com.tfuruya.tron.TronAction;
import com.tfuruya.tron.TronController;
import com.tfuruya.tron.TronData;

/** Allows controlling of player using external player */
public class ClientTronController extends TronController{
	
    /** Messenger for communicating with service. */
    private Messenger mService = null;
    
    
    /** Whether we have called bind on the service. */
    private boolean mIsBound;
    
    
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private final Messenger mMessenger = new Messenger(new IncomingHandler());
    
    
	private int direction;
	
	/** Application context */
	private Context appContext;
	
	
	public ClientTronController(int playerIndex, Messenger m) {
		super(playerIndex);
		
		// appContext = c;
		mService = m;
		
		reset(null);
		//doBindService();
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
	public TronAction getAction() {
		action.set(direction);
		return action;
	}
	
	
	@Override
	protected void compute(TronData d) {
		
		// This data will be sent to the service
		// Note: even though it is just a reference to the constantly-changing
		// data here, data will not change on the service side since
		// Bundle.putByteArray seems to deep copy the data
		byte[] data = d.get();
		
		// Send game state data to remote service
        // When the service finishes processing the data and returns a value,
		// it triggers IncomingHandler.handleMessage()
		try {

        	Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putByteArray("TRON_MAP_DATA", data);
            bundle.putIntArray("TRON_INFO", new int[]{d.getColumn(), d.getRow(), d.getNumPlayer(), myPlayerIndex});
            bundle.putIntArray("TRON_X", d.getX());
            bundle.putIntArray("TRON_Y", d.getY());
            msg.setData(bundle);
        	msg.replyTo = mMessenger;
        	
        	mService.send(msg);
            
        } catch (RemoteException e) {
            // Service crashed before we could do anything with it
            // We can count on soon being disconnected (and then reconnected if
        	// it can be restarted).
        	showToast(R.string.remote_exception);
        }
	}
	
	
    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	ClientTronController.this.direction = msg.what;
        }
    }
    
    
    /**
     * Class for interacting with the main interface of the service.
     */
    /*
    private ServiceConnection serviceConnection = new ServiceConnection() {
    	
    	// Called when connection with the remote service has been
    	// first established
        public void onServiceConnected(ComponentName className,
                IBinder service) {
        	
        	showToast(R.string.remote_service_connected);
        	
        	// We are communicating with our service through an IDL interface, 
        	// so get a client-side representation of that from the raw service
        	// object.
            mService = new Messenger(service);
        }
        
        // Called when connection with the remote service has been unexpectedly
        // disconnected -- that is, the service process crashed
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            showToast(R.string.remote_service_disconnected);
        }
    };
    */
    
    /** Establish connection with the service */
    /*
    private void doBindService() {
    	showToast(R.string.remote_service_binding);
    	
    	Intent sIntent = new Intent("com.tfuruya.tron.service.AI_SERVICE");
    	int bindOption = Context.BIND_AUTO_CREATE;
    	appContext.bindService(sIntent, serviceConnection, bindOption);
        mIsBound = true;
    }
    */
    
    /*
    // Although commented out, this can be used to unbind from service
    private void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, 2);
                	msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service
                    // has crashed.
                }
            }

            // Detach our existing connection.
            appContext.unbindService(serviceConnection);
            mIsBound = false;
            showToast("Unbinding");
        }
    }
    */
    
    
    /** show toast message */
    private void showToast(int resId) {
    	Context context = appContext;
    	int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, resId, duration);
        toast.show();
    }
}
