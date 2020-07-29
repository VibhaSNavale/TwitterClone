package com.example.twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //UI Components
    private EditText edtEnterEmail, edtEnterUsername, edtEnterPassword;
    private Button btnSignup, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Sign Up");

        ParseInstallation.getCurrentInstallation().saveInBackground();

        //initialising UI components
        edtEnterEmail = findViewById(R.id.edtEnterEmail);
        edtEnterUsername = findViewById(R.id.edtEnterUsername);
        edtEnterPassword = findViewById(R.id.edtEnterPassword);

        edtEnterPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if(keyCode == KeyEvent.KEYCODE_ENTER  //if the virtual keyboard is active and user clicks on enter button
                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN){

                    onClick(btnSignup);

                }

                return false;
            }
        });

        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);

        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            //ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.btnSignup:

                if(edtEnterEmail.getText().toString().equals("")){

                    FancyToast.makeText(MainActivity.this, "Email required.",
                            Toast.LENGTH_SHORT, FancyToast.INFO, false).show();

                }
                else if(edtEnterUsername.getText().toString().equals("")){

                    FancyToast.makeText(MainActivity.this, "Username required.",
                            Toast.LENGTH_SHORT, FancyToast.INFO, false).show();

                }
                else if(edtEnterPassword.getText().toString().equals("")){

                    FancyToast.makeText(MainActivity.this, "Password required.",
                            Toast.LENGTH_SHORT, FancyToast.INFO, false).show();

                }
                else {

                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtEnterEmail.getText().toString());
                    appUser.setUsername(edtEnterUsername.getText().toString());
                    appUser.setPassword(edtEnterPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing up " + edtEnterUsername.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                FancyToast.makeText(MainActivity.this, appUser.getUsername() + " is signed up successfully.",
                                        Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                                transitionToSocialMediaActivity();
                            }
                            else {
                                FancyToast.makeText(MainActivity.this, "There was an error: " + e.getMessage(),
                                        Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }

                            progressDialog.dismiss();
                        }
                    });
                }

                break;

            case R.id.btnLogin:

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

        }
    }

    //executed when the root layout is tapped (to hide the virtual keyboard)
    public void rootLayoutTapped(View view) {
        try {

            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
        catch (Exception e)  {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMediaActivity()
    {
        Intent intent = new Intent(MainActivity.this, TwitterUsers.class);
        startActivity(intent);
        finish(); //call this method as we shouldn't allow the sign up/log in page to be displayed/opened when back button is clicked
    }

}
