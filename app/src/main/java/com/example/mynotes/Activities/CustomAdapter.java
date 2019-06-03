package com.example.mynotes.Activities;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mynotes.R;


public class CustomAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] mTitleList;



    public CustomAdapter(Context context, String[] titleList) {
        super(context, -1, titleList);
        this.context = context;
        mTitleList = titleList;

    }
    private  class ViewHolder {
        private TextView mTitle;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_custom, parent, false);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.view);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTitle.setText(mTitleList[position]);




        return convertView;
    }



}
