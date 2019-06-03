package com.example.mynotes.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.example.mynotes.Constants.Keys;
import com.example.mynotes.R;
import com.example.mynotes.utils.SharedPreferencesUtils;

import java.util.ArrayList;

public class UserDataListViewActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] mUserInputValue;
    private SharedPreferencesUtils mSharedPreference;
    private ListView mListView;
    private CustomAdapter mCustomAdapter;
    private ArrayAdapter mAdapter;
    private ArrayList<String> userDataArrayList = new ArrayList<String>();
    private Button mClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_display);
        // initializing classes
        mSharedPreference = SharedPreferencesUtils.getInstance(getApplicationContext());
        // initializing widgets
        mListView = (ListView) findViewById(R.id.activity_user_data_display_list_view_id);
        mClear = (Button) findViewById(R.id.activity_user_data_display_clear_button_id);
        // fetching data
        userDataArrayList.clear();
        try {
            userDataArrayList.addAll((ArrayList<String>) mSharedPreference.getObject(Keys.UserDataList.name(), new ArrayList<String>()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        mAdapter= new ArrayAdapter(this, R.layout.view_custom, R.id.view,
                userDataArrayList);

        mListView.setAdapter(mAdapter);
        mClear.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_user_data_display_clear_button_id:
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Clear List Data")
                        .setMessage("Are Your Sure You Want To Delete Whole List?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userDataArrayList.clear();
                                mSharedPreference.putObject(Keys.UserDataList.name(), userDataArrayList);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null )
                        .show();


                break;
        }
    }
}
