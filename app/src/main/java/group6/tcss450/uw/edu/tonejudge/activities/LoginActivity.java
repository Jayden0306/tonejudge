package group6.tcss450.uw.edu.tonejudge.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import group6.tcss450.uw.edu.tonejudge.server.JsonPostErrorTask;
import group6.tcss450.uw.edu.tonejudge.R;

/**
 * This is the activity for user login.
 * @author Jayden , Hunter
 * @version 03/08/2017
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialize the share preference with allow application itself access only
        SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        String email = prefs.getString(getString(R.string.email), null);
        String password = prefs.getString(getString(R.string.password), null);
        if (email != null && password != null) {
            AuthenticateTask authenticateTask = new AuthenticateTask();
            JSONObject request = new JSONObject();
            try {
                request.put("email", email);
                request.put("password", password);
                request.put("action", AuthenticateTask.ACTION);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            authenticateTask.execute(request);
        }
    }

    /**
     * Check for empty field and validate the user account
     * @param view Current view when function is called.
     */
    public void clickLoginButton(View view) {
        Boolean checkUserName = true;
        Boolean checkPassword = true;

        //checking if the username edit text field is empty
        EditText userNameEditText = (EditText) findViewById(R.id.edit_text_login_email);
        String userName = userNameEditText.getText().toString();
        if(TextUtils.isEmpty(userName)) {
            checkUserName = false;
            userNameEditText.setError("Please enter an email");
        }
        //checking if the password edit text field is empty
        EditText userPasswordEditText = (EditText) findViewById(R.id.edit_text_login_password);
        String userPassword = userPasswordEditText.getText().toString();
        if(TextUtils.isEmpty(userPassword)) {
            checkPassword = false;
            userPasswordEditText.setError("Please enter a password");
        }

        if(checkUserName && checkPassword) {
            AuthenticateTask task;
            if(view.getId() == R.id.button_login) {
                task = new AuthenticateTask();
            } else{
                throw new IllegalStateException("Not Implemented");
            }
            JSONObject request = new JSONObject();
            try {
                request.put("email", userName);
                request.put("password", userPassword);
                request.put("action", AuthenticateTask.ACTION);
            } catch (JSONException e) {

            }
            task.execute(request);
        }
    }

    /**
     * switch to the register activity
     * when user click the register
     * @param view Current view when function is called.
     */
    public void clickRegisterButton(View view) {
        //switch to the register activity using intent
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    /**
     * An AsyncTask that authenticates the users login credentials, on success changes to
     * the JudgeActivity, on failure displays the error message.
     */
    private class AuthenticateTask extends JsonPostErrorTask {

        private static final String ACTION = "authenticate";
        private ProgressDialog progressDialog;
        private JSONObject request;

        public AuthenticateTask() {
            super("https://xk6ntzqxr2.execute-api.us-west-2.amazonaws.com/tonejudge/users");
        }

        /**
         * display progress bar with the login message
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Logging in...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            request = params[0];
            return super.doInBackground(params);
        }

        @Override
        protected void onFinish(String errorMessage) {
            progressDialog.dismiss();
            if (errorMessage == null) {
                SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
                //store the user email and password into the share preference file
                try {
                    prefs.edit()
                            .putString(getString(R.string.email), request.getString("email"))
                            .putString(getString(R.string.password), request.getString("password"))
                            .apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //once pass the authentication switch to the judge activity
                Intent judgeIntent = new Intent(getApplicationContext(), JudgeActivity.class);
                judgeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(judgeIntent);
            } else if (errorMessage.contains("Invalid email or password")) {
                EditText userPasswordEditText = (EditText) findViewById(R.id.edit_text_login_password);
                userPasswordEditText.setError(errorMessage);
            } else {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}

