package com.tfuruya.tron;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainMenuActivity extends Activity implements OnClickListener{
	
	// Pop up dialog
	private ProgressDialog progressDialog; // Displays "please wait" for use while the list of AI services are being retrieved
    private AlertDialog.Builder noSelectionAlertDialog;	// Displays "no player selected"
	private AlertDialog.Builder tooManyAlertDialog; // Displays "too many players selected"
    
	private ArrayList<Player> player; // holds data for each AI service retrieved via intent filter querying
    private PlayerArrayAdapter adapter; // custom class which manages our list of AI services
    
    
    // Views displayed in menu layout
    private ListView aiListView;
    private Button startButton;
    private ImageButton refreshButton;
    
    private int numPlayerReady;
    private int numPlayerPlaying;
    
    
	/** Called when the activity is first created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        // Set up alert dialog
    	noSelectionAlertDialog = new AlertDialog.Builder(this);
    	noSelectionAlertDialog.setTitle("Select some players...");
    	noSelectionAlertDialog.setMessage("You need at least one player");
    	noSelectionAlertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            return;
            }
        });
    	tooManyAlertDialog = new AlertDialog.Builder(this);
    	tooManyAlertDialog.setTitle("Too many players...");
    	tooManyAlertDialog.setMessage("You can't select more than 8 players");
    	tooManyAlertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            return;
            }
        });
    	
        // Get view objects in layout and make them click-able
        aiListView = (ListView) findViewById(R.id.aiListView);
        startButton = (Button) findViewById(R.id.startButton);
        refreshButton = (ImageButton) findViewById(R.id.refreshImageButton);
        startButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
        
        // Show progress dialog while finding AI service
        showProgressDialog();
        
        // Set up list view
        new FindAIServiceTask().execute();
    }
    
    
    private void showProgressDialog() {
    	progressDialog = ProgressDialog.show(MainMenuActivity.this,    
                "Please wait...", "Looking for available AI", true);
    }
    
    
    private class FindAIServiceTask extends AsyncTask<Void, Integer, ArrayList<Player>> {
        protected ArrayList<Player> doInBackground(Void... voids) {
        	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
        	player = new ArrayList<Player>();
        	Intent intent = new Intent("com.tfuruya.tron.service.AI_SERVICE");
    		List<ResolveInfo> list = getPackageManager().queryIntentServices(intent, 0);
    		Iterator<ResolveInfo> iterator = list.iterator();
    		
    		// Add first item which is the app user
    		player.add(new Player("You", MainMenuActivity.this));
    		
    		// Add the other items (AI services)
    		while (iterator.hasNext()) {
    			ResolveInfo r = iterator.next();
    			Player p = new Player(r, MainMenuActivity.this);
    			player.add(p);
    		}
    		
            return player;
        }

        protected void onProgressUpdate(Integer... progress) {
            // setProgressPercent(progress[0]);
        }
        
        protected void onPostExecute(ArrayList<Player> result) {
        	MainMenuActivity.this.player = result;
            MainMenuActivity.this.adapter = new PlayerArrayAdapter(MainMenuActivity.this, R.layout.menu_item, result);
            MainMenuActivity.this.aiListView.setAdapter(adapter);
            MainMenuActivity.this.progressDialog.dismiss();
        }
    }
    
	
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			
			case R.id.startButton:
				// For each player, set reference to list item view
				for (int i=0; i<player.size(); i++) {
					player.get(i).setView(aiListView.getChildAt(i));
				}
				
				// Count number of playing players
				numPlayerPlaying = 0;
				for (int j=0; j<player.size(); j++) {
					if (player.get(j).isSelected()) {
						numPlayerPlaying ++;
					}
				}
				
				// Alert if no player is selected
				if (numPlayerPlaying < 1) {
					noSelectionAlertDialog.show();
					break;
				}
				
				// Alert if more than 8 players are selected
				if (numPlayerPlaying > 8) {
					tooManyAlertDialog.show();
					break;
				}
				
				// Disable all of the buttons and check boxes
				disableButtons();
				
				// Bind with services who are checked in the list
				numPlayerReady = 0;
				for (int k=0; k<player.size(); k++) {
					Player p = player.get(k);
					if (p.isSelected()) {
						p.doBindService(readyToStartGame);
					}
				}
				break;
			
			case R.id.refreshImageButton:
				player.clear();
				adapter.clear();
				showProgressDialog();
				new FindAIServiceTask().execute();
				break;
		}
	}
	
	
	private void disableButtons() {
		startButton.setEnabled(false);
		refreshButton.setEnabled(false);
		for (int i=0; i<aiListView.getChildCount(); i++) {
			aiListView.getChildAt(i).findViewById(R.id.checkBox).setEnabled(false);
		}
	}
	
	
	private void enableButtons() {
		startButton.setEnabled(true);
		refreshButton.setEnabled(true);
		for (int i=0; i<aiListView.getChildCount(); i++) {
			aiListView.getChildAt(i).findViewById(R.id.checkBox).setEnabled(true);
		}
	}
	
	
	private Callable<Void> readyToStartGame = new Callable<Void>() {
		public Void call() {
			numPlayerReady ++;
			if (numPlayerReady >= numPlayerPlaying) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				ArrayList<Player> p = new ArrayList<Player>();
				
				for (int i=0; i<player.size(); i++) {
					if (player.get(i).isSelected()) {
						p.add(player.get(i));
					}
				}
				
				bundle.putParcelableArrayList("player", p);
				intent.putExtras(bundle);
				intent.setClass(getBaseContext(), TronActivity.class);
				startActivity(intent);
			}
			return null;
		}
	};
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		enableButtons();
	}
}
