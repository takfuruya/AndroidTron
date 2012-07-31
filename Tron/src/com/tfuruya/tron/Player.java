package com.tfuruya.tron;

import java.util.concurrent.Callable;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.TextView;

public class Player implements ServiceConnection, Parcelable{
	private Context context;
	public String name;
	public String message;
	public boolean isRemoteService;
    public Drawable icon;
    public String className; // fully qualified class name of the target component (eg com.example.project.app.FreneticActivity)
    public String packageName; // package name set in the manifest file of the application where the component resides (example: "com.example.project")
    
    public View listItemView;
    public CheckBox checkBox;
    public TextView messageTextView;
	
    public Messenger messenger;
    private Callable<Void> readyMethod;
    
    private boolean isBound;
    
    // For player written in this application
	public Player(String nameOfPlayer, Context ctx) {
		context = ctx;
		name = nameOfPlayer;
		message = "Ready";
		isRemoteService = false;
		icon = context.getResources().getDrawable(R.drawable.ic_launcher);
	}
	
	// For player written in external application (remote AI service)
	public Player(ResolveInfo resolveInfo, Context ctx) {
		context = ctx;
		name = (String) resolveInfo.loadLabel(context.getPackageManager());
		message = resolveInfo.serviceInfo.name;
		isRemoteService = true;
		icon = resolveInfo.loadIcon(context.getPackageManager());
		className = resolveInfo.serviceInfo.name;
		packageName = resolveInfo.serviceInfo.packageName;
		isBound = false;
	}
	
	
	public void setView(View view) {
		listItemView = view;
		checkBox = (CheckBox) listItemView.findViewById(R.id.checkBox);
		messageTextView = (TextView) listItemView.findViewById(R.id.messageTextView);
	}
	
	
	public boolean isSelected() {
		if (checkBox == null) return false;
		return checkBox.isChecked();
	}
	
	
    /** Establish connection with the service */
    public void doBindService(Callable<Void> method) {
    	if (!isRemoteService) {
			try {
				method.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
    	}
    	if (isBound) {
    		messageTextView.setText("Ready");
    		try {
				method.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return;
    	}
    	messageTextView.setText("Binding");
    	
    	readyMethod = method;
    	
    	// set up intent
    	ComponentName component = new ComponentName(packageName, className);
    	Intent intent = new Intent();
    	intent.setComponent(component);
    	
    	// set up bind option
    	int bindOption = Context.BIND_AUTO_CREATE;
    	
    	// bind to remote AI service
    	context.bindService(intent, this, bindOption);
    }
    
    
    // ------------------------------------------------------------------
    // ------------------ ServiceConnection implementation --------------
    // ------------------------------------------------------------------
    
    
    // Called when connection with the remote service has been
 	// first established
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
    	messageTextView.setText("Ready");
    	isBound = true;
    	
    	// We are communicating with our service through an IDL interface, 
    	// so get a client-side representation of that from the raw service
    	// object.
        messenger = new Messenger(service);
        try {
			readyMethod.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
    // Called when connection with the remote service has been unexpectedly
    // disconnected -- that is, the service process crashed
	@Override
	public void onServiceDisconnected(ComponentName name) {
        //mService = null;
        //showToast(R.string.remote_service_disconnected);
	}
	
	
    // ------------------------------------------------------------------
    // ---------------------- Parcelable implementation -----------------
    // ------------------------------------------------------------------
	
	
	public int describeContents() {
	     return 0;
	}
	
	
	public void writeToParcel(Parcel out, int flags) {
	     out.writeParcelable(messenger, 0);
	     out.writeBooleanArray(new boolean[]{isRemoteService});
	}
	
	
	public static final Parcelable.Creator<Player> CREATOR
	             = new Parcelable.Creator<Player>() {
	     public Player createFromParcel(Parcel in) {
	         return new Player(in);
	     }

	     public Player[] newArray(int size) {
	         return new Player[size];
	     }
	 };
	 
     private Player(Parcel in) {
         messenger = in.readParcelable(null);
         boolean[] b = new boolean[1];
         in.readBooleanArray(b);
         isRemoteService = b[0];
     }
	
}
