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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~jayden91/";

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
                task = new PostWebServiceTask();
            }else{
                throw new IllegalStateException("Not Implemented");
            }

            task.execute(PARTIAL_URL, userName, userPassword);
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

    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "login.php";

        @Override
        protected String doInBackground(String... strings) {
            if(strings.length != 3) {
                throw  new IllegalArgumentException("Three String argument required.");
            }
            String response ="";
            HttpURLConnection urlConnection = null;
            String url = strings[0];

            try {
                URL urlObject = new URL(url + SERVICE);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter((urlConnection.getOutputStream()));
                String data = URLEncoder.encode("userName", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("userPassword", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2], "UTF-8");
//                Log.d("post", data);
                wr.write(data);
                wr.flush();

                InputStream content = urlConnection.getInputStream();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while((s = buffer.readLine()) != null) {
                    response += s;
                }
            }catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            //something wrong with the network or the URL.
            if(result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            Log.d("log in result", result);

            try {
                JSONObject jsonResponse = new JSONObject(result);
                String success =jsonResponse.getString("success");

                if(success.equals("true")) {
                    Intent judgeIntent = new Intent(LoginActivity.this, JudgeActivity.class);
                    LoginActivity.this.startActivity(judgeIntent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

