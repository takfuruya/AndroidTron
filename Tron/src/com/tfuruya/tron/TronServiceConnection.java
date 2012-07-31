package com.tfuruya.tron;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

/**
 * Class for interacting with the main interface of the service.
 */
public class TronServiceConnection implements ServiceConnection {
	
	private boolean isBound = false;
	
	// Called when connection with the remote service has been
	// first established
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
    	//showToast(R.string.remote_service_connected);
    	
    	// We are communicating with our service through an IDL interface, 
    	// so get a client-side representation of that from the raw service
    	// object.
        //mService = new Messenger(service);
	}
	
	
    // Called when connection with the remote service has been unexpectedly
    // disconnected -- that is, the service process crashed
	@Override
	public void onServiceDisconnected(ComponentName name) {
        //mService = null;
        //showToast(R.string.remote_service_disconnected);
	}
	
	
	public boolean isBound() {
		return isBound;
	}
}
