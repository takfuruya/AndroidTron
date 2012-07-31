package com.tfuruya.tron;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class PlayerArrayAdapter extends ArrayAdapter<Player> {
	
	private ArrayList<Player> items;
	
    public PlayerArrayAdapter(Context context, int textViewResourceId, ArrayList<Player> items) {
            super(context, textViewResourceId, items);
            this.items = items;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.menu_item, null);
            }
            Player player = items.get(position);
            if (player != null) {
                    TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
                    TextView messageTextView = (TextView) v.findViewById(R.id.messageTextView);
                    ImageView iconImageView = (ImageView) v.findViewById(R.id.iconImageView);
                    if (nameTextView != null) {
                        nameTextView.setText(player.name);
                    }
                    if (messageTextView != null){
                    	messageTextView.setText(player.message);
                    }
                    if (iconImageView != null) {
                    	iconImageView.setImageDrawable(player.icon);
                    }
            }
            return v;
    }
}