package group6.tcss450.uw.edu.tonejudge.server;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A JsonPostTask that either returns an empty JSON Object on success or a JSON Object that contains
 * "errorMessage" on failure.
 */
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

    /**
     *
     * @param errorMessage null if no error.
     */
    protected void onFinish(String errorMessage) {

    }
}
