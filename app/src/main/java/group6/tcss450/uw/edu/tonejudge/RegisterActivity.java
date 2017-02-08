package group6.tcss450.uw.edu.tonejudge;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailView;
    private EditText passwordView;
    private EditText passwordConfirmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailView = (EditText) findViewById(R.id.register_email);
        passwordView = (EditText) findViewById(R.id.register_password);
        passwordConfirmView = (EditText) findViewById(R.id.register_password_confirm);
    }

    @Override
    public void onClick(View v) {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfrim = passwordConfirmView.getText().toString();
        boolean pass = true;
        if (email.isEmpty()) {
            emailView.setError("Email is required");
            pass = false;
        }
        if (!email.isEmpty() && !email.contains("@")) {
            emailView.setError("Invalid email");
            pass = false;
        }
        if (password.isEmpty()) {
            passwordView.setError("Password is required");
            pass = false;
        }
        if (passwordConfrim.isEmpty()) {
            passwordConfirmView.setError("Password confirmation is required");
            pass = false;
        }
        if (!password.equals(passwordConfrim)) {
            passwordConfirmView.setError("Passwords do not match");
            pass = false;
        }
        if (pass) {
            new Register().execute(email, password);
        }
    }

    private class Register extends AsyncTask<String, Void, String> {

        private final String URL_STRING = "https://xk6ntzqxr2.execute-api.us-west-2.amazonaws.com/tonejudge/users";
        private final String ACTION = "register";

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
                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), JudgeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
    }
}

