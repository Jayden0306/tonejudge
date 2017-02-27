package group6.tcss450.uw.edu.tonejudge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ElementTone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TopRanksToneActivity extends NavDrawerActivity {

    private Tone mTone;
    private int mPage;
    private ListView mList;

    private List<ElementTone> mElementTones = new ArrayList<>();
    private List<String> mTexts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ranks_tone);
        mTone = (Tone) getIntent().getSerializableExtra("tone");
        mPage = 0;
        ((Toolbar) findViewById(R.id.toolbar)).setTitle("Top: " + mTone.getName());
        mList = (ListView) findViewById(R.id.top_ranks_tone_list);
        onCreateDrawer();
        loadNextPage();
    }

    public void loadNextPage() {
        try {
            JSONObject request = new JSONObject();
            request.put("page", mPage);
            request.put("tone", mTone.getId());
            request.put("action", PageTask.ACTION);
            new PageTask().execute(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class PageTask extends JsonPostTask {

        private static final String ACTION = "top";

        public PageTask() {
            super("https://xk6ntzqxr2.execute-api.us-west-2.amazonaws.com/tonejudge/results");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            Log.d("", jsonObject.toString());
            try {
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    mTexts.add(result.getString("text"));
                    mElementTones.add(ElementTones.dbJsonToElementTone(result));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mPage++;
            Log.d("", mElementTones.toString());
            Log.d("", mTexts.toString());
        }
    }
}
