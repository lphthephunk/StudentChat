package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cody_.studentchat.R;

import java.util.List;

/**
 * Created by Cody_ on 10/6/2017.
 */

public class MessageListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;

    public MessageListArrayAdapter(Context context, List<String> values){
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.message_listview_adapter, parent, false);

        TextView usernameBlock = (TextView) rowView.findViewById(R.id.usernameBlock);
        TextView messageBlock = (TextView) rowView.findViewById(R.id.messageBlock);

        messageBlock.setText(values.get(position));

        return rowView;
    }
}
