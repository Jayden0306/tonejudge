package group6.tcss450.uw.edu.tonejudge;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JsonPostTask extends AsyncTask<JSONObject, Void, JSONObject> {

    protected String url;

    public JsonPostTask(String url) {
        this.url = url;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        JSONObject requestBody = params[0];
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        Log.d(getClass().getSimpleName(), requestBody.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, requestBody.toString()))
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.d(getClass().getSimpleName(), response.toString());
            JSONObject responseBody = new JSONObject(response.body().string());
            Log.d(getClass().getSimpleName(), responseBody.toString());
            return responseBody;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
