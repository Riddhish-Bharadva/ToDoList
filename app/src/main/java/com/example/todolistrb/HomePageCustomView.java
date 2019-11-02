package com.example.todolistrb;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class HomePageCustomView extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> Items;
    private LayoutInflater inflater;

    public HomePageCustomView(Activity activity, ArrayList<String> Items)
    {
        this.activity = activity;
        this.Items = Items;
    }

    @Override
    public int getCount()
    {
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return Items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(inflater == null)
        {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.activity_home_page_custom_view, null);
        }

        TextView ListTitleTextBox = (TextView) convertView.findViewById(R.id.ListTitleTextBox);
        TextView CreationDateTextBox = (TextView) convertView.findViewById(R.id.CreationDateTextBox);
        TextView DueDateTextBox = (TextView) convertView.findViewById(R.id.DueDateTextBox);

        return convertView;
    }
}