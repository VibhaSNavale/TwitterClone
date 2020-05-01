package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listview;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;

    //private String followedUsers = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);


        FancyToast.makeText(this, "Welcome " + ParseUser.getCurrentUser().getUsername(),
                FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false);

        listview = findViewById(R.id.listView);
        tUsers = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, tUsers);

        listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listview.setOnItemClickListener(this);

        try {

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {

                    if (objects.size() > 0 && e == null) {

                        for (ParseUser user : objects) {
                            tUsers.add(user.getUsername());
                        }

                        listview.setAdapter(adapter);

                        for(String twitterUser : tUsers) {

                            if(ParseUser.getCurrentUser().getList("fanOf") != null) {

                                if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {

                                    //followedUsers = followedUsers + twitterUser;

                                    listview.setItemChecked(tUsers.indexOf(twitterUser), true);

                                    //FancyToast.makeText(TwitterUsers.this, ParseUser.getCurrentUser().getUsername() + " is following " + followedUsers + "\n", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                                }
                            }
                        }
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.logout_item:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(TwitterUsers.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                break;

            case R.id.sendTweetItem:

                Intent intent = new Intent(TwitterUsers.this, SendTweetActivity.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //to check whether or not the username/list item is checked
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view; //array list of users

        if (checkedTextView.isChecked()) {

            FancyToast.makeText(TwitterUsers.this, "You followed " + tUsers.get(position) ,
                    FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

            ParseUser.getCurrentUser().add("fanOf", tUsers.get(position));

        }
        else {
            //un-following users

            FancyToast.makeText(TwitterUsers.this, "You unfollowed " + tUsers.get(position),
                    FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);

        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e==null) {
                    FancyToast.makeText(TwitterUsers.this, "Saved" ,
                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false);
                }

            }
        });

    }
}