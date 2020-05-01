package com.example.twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SendTweetActivity extends AppCompatActivity {

    private EditText edtSendTweet;
    private Button btnSendTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtSendTweet = findViewById(R.id.edtSendTweet);

    }

    public void sendTweet(View view) {

        ParseObject parseObject = new ParseObject("myTweet");
        parseObject.add("user", ParseUser.getCurrentUser().getUsername());
        parseObject.add("tweet", edtSendTweet.getText().toString());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null) {

                    FancyToast.makeText(SendTweetActivity.this, ParseUser.getCurrentUser().getUsername() + "'s tweet" + " (" + edtSendTweet.getText().toString() + ") is saved!",
                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                }

                else {

                    FancyToast.makeText(SendTweetActivity.this, e.getMessage(),
                            FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                }
                progressDialog.dismiss();

            }
        });

    }
}
