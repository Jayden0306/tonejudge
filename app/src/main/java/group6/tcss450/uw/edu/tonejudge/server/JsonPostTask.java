package group6.tcss450.uw.edu.tonejudge.server;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * An AsyncTask for making a POST HTTP request that takes and returns JSON.
 */
public class JsonPostTask extends AsyncTask<JSONObject, Void, JSONObject> {

    private String url;

    /**
     *
     * @param url to send the request to.
     */
    public JsonPostTask(String url) {
        this.url = url;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        JSONObject requestBody = params[0];
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, requestBody.toString()))
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject responseBody = new JSONObject(response.body().string());
            return responseBody;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
