package group6.tcss450.uw.edu.tonejudge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;

    private static int PASSWORD_MIN_LENGTH = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmailView = (EditText) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mPasswordConfirmView = (EditText) findViewById(R.id.register_password_confirm);
    }

    /**
     * Verifies the user entered email, password, and password confirmation then sends a request
     * to register the account
     *
     * @param view ignored
     */
    @Override
    public void onClick(View view) {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordConfirm = mPasswordConfirmView.getText().toString();
        boolean pass = true;
        if (!email.contains("@")) {
            mEmailView.setError("Invalid email");
            pass = false;
        }
        if (email.isEmpty()) {
            mEmailView.setError("Email is required");
            pass = false;
        }
        if (!password.equals(passwordConfirm)) {
            mPasswordView.setError("Passwords do not match");
            pass = false;
        }
        if (password.length() < PASSWORD_MIN_LENGTH) {
            mPasswordView.setError("Password must be at least " + PASSWORD_MIN_LENGTH + " characters long");
            pass = false;
        }
        if (password.isEmpty()) {
            mPasswordView.setError("Password is required");
            pass = false;
        }
        if (pass) {
            JSONObject request = new JSONObject();
            try {
                request.put("email", email);
                request.put("password", password);
                request.put("action", RegisterTask.ACTION);
                new RegisterTask().execute(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * An AysncTask for sending an http request to register the account. On success changes to
     * JudgeActivity. Otherwise displays the error message.
     */
    private class RegisterTask extends JsonPostErrorTask {

        private static final String ACTION = "register";
        private ProgressDialog progressDialog;
        private JSONObject request;

        public RegisterTask() {
            super("https://xk6ntzqxr2.execute-api.us-west-2.amazonaws.com/tonejudge/users");
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            request = params[0];
            return super.doInBackground(params);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onFinish(String errorMessage) {
            progressDialog.dismiss();
            if (errorMessage == null) {
                SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
                try {
                    prefs.edit()
                            .putString(getString(R.string.email), request.getString("email"))
                            .putString(getString(R.string.password), request.getString("password"))
                            .apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), JudgeActivity.class);
                startActivity(intent);
            } else if (errorMessage.contains("There is already a user registered with that email")) {
                mEmailView.setError(errorMessage);
            } else {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}

