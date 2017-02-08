package group6.tcss450.uw.edu.tonejudge;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Check for empty field and validate the user account
     * @param view
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
            AsyncTask<String, Void, String> task = null;

            if(view.getId() == R.id.button_login) {
                task = new AuthenticateTask();
            } else{
                throw new IllegalStateException("Not Implemented");
            }

            task.execute(userName, userPassword);
        }
    }

    /**
     * switch to the register activity
     * when user click the register
     * @param view
     */
    public void clickRegisterButton(View view) {
        //switch to the register activity using intent
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private class AuthenticateTask extends AsyncTask<String, Void, String> {

        private final String URL_STRING = "https://xk6ntzqxr2.execute-api.us-west-2.amazonaws.com/tonejudge/users";
        private final String ACTION = "authenticate";

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("action", ACTION);
                jsonBody.put("email", email);
                jsonBody.put("password", password);
            } catch (JSONException e) {
                return e.getMessage();
            }
            Log.d(getClass().getSimpleName(), jsonBody.toString());
            Request request = new Request.Builder()
                    .url(URL_STRING)
                    .post(RequestBody.create(JSON, jsonBody.toString()))
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.d(getClass().getSimpleName(), response.toString());
                JSONObject r = new JSONObject(response.body().string());
                Log.d(getClass().getSimpleName(), r.toString());
                if (r.has("errorMessage")) {
                    return r.getString("errorMessage");
                }
            } catch (IOException | JSONException e) {
                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Intent judgeIntent = new Intent(getApplicationContext(), JudgeActivity.class);
                startActivity(judgeIntent);
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
    }
}

