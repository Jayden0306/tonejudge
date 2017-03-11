package group6.tcss450.uw.edu.tonejudge.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ElementTone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import group6.tcss450.uw.edu.tonejudge.server.ElementTones;
import group6.tcss450.uw.edu.tonejudge.server.JsonPostTask;
import group6.tcss450.uw.edu.tonejudge.R;
import group6.tcss450.uw.edu.tonejudge.model.Tone;

/**
 * Activity displaying the top ranked results for a particular tone.
 *
 * @author Hunter Bennett
 */
public class TopRanksToneActivity extends NavDrawerActivity {

    /**
     * Number of results to load at a time.
     */
    private static int PAGE_SIZE = 10;

    private Tone mTone;

    /**
     * Current page number.
     */
    private int mPage;

    private RecyclerView mRecycler;
    private LinearLayoutManager mLayoutManager;
    private Adapter mAdapter;

    /**
     * The current running PageTask. null if none.
     */
    private PageTask mPageTask;

    /**
     * Has run out of results in the database.
     */
    private boolean mHasReachedEndOfDb = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ranks_tone);
        mTone = (Tone) getIntent().getSerializableExtra("tone");
        mPage = 0;
        ((Toolbar) findViewById(R.id.toolbar)).setTitle("Top: " + mTone.getName());
        mRecycler = (RecyclerView) findViewById(R.id.top_ranks_tone_recycler);
        mAdapter = new Adapter();
        mRecycler.setAdapter(mAdapter);

        // automatically load more results when scrolled to the bottom of list
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemCount = mLayoutManager.getItemCount();
                if (!mHasReachedEndOfDb && mPageTask == null &&  itemCount == mLayoutManager.findLastVisibleItemPosition() + 1) {
                    loadNextPage();
                }
            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(mLayoutManager);
        onCreateDrawer();
        loadNextPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_ranks_tone_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_tone_info) {
            new AlertDialog.Builder(this)
                    .setMessage(mTone.getDescription(this))
                    .setTitle(mTone.getName())
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Starts a PageTask for the next page.
     */
    private void loadNextPage() {
        try {
            JSONObject request = new JSONObject();
            request.put("page", mPage);
            request.put("pageSize", PAGE_SIZE);
            request.put("tone", mTone.getId());
            request.put("action", PageTask.ACTION);
            new PageTask().execute(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * AsyncTask for loading the next page of results.
     */
    private class PageTask extends JsonPostTask {

        private static final String ACTION = "top";
        private Snackbar snackbar;

        public PageTask() {
            super("https://xk6ntzqxr2.execute-api.us-west-2.amazonaws.com/tonejudge/results");
            mPageTask = this;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            snackbar = Snackbar.make(mRecycler, "Loading...", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            snackbar.dismiss();
            mPageTask = null;
            if (jsonObject == null) {
                return;
            }
            try {
                JSONArray results = jsonObject.getJSONArray("results");
                if (results.length() < PAGE_SIZE) {
                    mHasReachedEndOfDb = true;
                }
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    Log.d("result", "" + i + " " + result);
                    mAdapter.mTexts.add(result.getString("text"));
                    mAdapter.mResults.add(result);
                }
                mAdapter.notifyItemRangeInserted(mAdapter.mTexts.size() - results.length(), results.length());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mPage++;
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        protected TextView text;
        protected TextView score;
        protected TextView rank;

        public Holder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.top_ranks_tone_card_text);
            score = (TextView) itemView.findViewById(R.id.top_ranks_tone_card_score);
            rank = (TextView) itemView.findViewById(R.id.top_ranks_tone_card_rank);
        }
    }

    private class Adapter extends RecyclerView.Adapter<Holder> {

        private List<JSONObject> mResults = new ArrayList<>();
        private List<String> mTexts = new ArrayList<>();

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_ranks_tone_card, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TopRanksToneActivity.this, ResultActivity.class);
                    int pos = mRecycler.getChildLayoutPosition(v);
                    intent.putExtra("text", mTexts.get(pos));
                    intent.putExtra("id", "");
                    ElementTone analysis = ElementTones.dbJsonToElementTone(mResults.get(pos));
                    Log.d("analysis", analysis.toString());
                    intent.putExtra("analysis", analysis.toString());
                    startActivity(intent);
                }
            });
            return new Holder(itemView);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.text.setText(mTexts.get(position));
            try {
                double score = mResults.get(position).getDouble(mTone.getId());
                holder.score.setText(ElementTones.scoreToString(score) + " %");
                holder.rank.setText(Integer.toString(position + 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mTexts.size();
        }
    }
}
