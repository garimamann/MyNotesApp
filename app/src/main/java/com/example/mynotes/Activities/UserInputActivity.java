package com.example.mynotes.Activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.mynotes.Constants.Keys;
import com.example.mynotes.R;
import com.example.mynotes.utils.SharedPreferencesUtils;

import java.util.ArrayList;

public class UserInputActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mInputDateDialogButton, mSubmitButton;
    private EditText mUserInputValue;
    private SharedPreferencesUtils mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);
        // Initialising widgets
        mInputDateDialogButton = findViewById(R.id.activity_user_input_set_date_button);
        mUserInputValue = findViewById(R.id.activity_user_input_input);
        mSubmitButton = findViewById(R.id.activity_user_input_submit_button);
        //initializing utils
        mSharedPreference = SharedPreferencesUtils.getInstance(getApplicationContext());
        // initialising button click
        mInputDateDialogButton.setOnClickListener(this);
        mSubmitButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_user_input_set_date_button:
                DialogFragment mNewDialog = new DatePickerFragment();
                mNewDialog.show(getSupportFragmentManager(), "Date");
                break;
            case R.id.activity_user_input_submit_button:
                saveAndNextActivity();
                break;


        }

    }

    private void saveAndNextActivity() {
        String userInput = mUserInputValue.getText().toString().trim();
        ArrayList<String> userDataArrayList = new ArrayList<String>();
        userDataArrayList.clear();
        if (!TextUtils.isEmpty(userInput)) {
            try {
                userDataArrayList.addAll((ArrayList<String>) mSharedPreference.getObject(Keys.UserDataList.name(), new ArrayList<String>()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            userDataArrayList.add(userInput);
            mSharedPreference.putObject(Keys.UserDataList.name(), userDataArrayList);
            Intent intent = new Intent(this, UserDataListViewActivity.class);
            startActivity(intent);
        }


    }
}
