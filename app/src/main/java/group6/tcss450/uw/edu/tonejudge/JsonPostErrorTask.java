package group6.tcss450.uw.edu.tonejudge;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonPostErrorTask extends JsonPostTask {

    public JsonPostErrorTask(String url) {
        super(url);
    }

    @Override
    protected void onPostExecute(JSONObject responseBody) {
        super.onPostExecute(responseBody);
        if (responseBody == null) {
            onFinish("Error");
        } else if (responseBody.has("errorMessage")) {
            try {
                onFinish(responseBody.getString("errorMessage"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            onFinish(null);
        }
    }

    protected void onFinish(String errorMessage) {

    }
}
