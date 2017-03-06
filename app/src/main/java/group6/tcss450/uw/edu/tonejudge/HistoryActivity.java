package group6.tcss450.uw.edu.tonejudge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class HistoryActivity extends NavDrawerActivity {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ToneAdapter mAdapter;
    private DataBaseHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        onCreateDrawer();

        mDB = new DataBaseHelper(this);
        Log.d("Reading", "Reading all contacts...");
        List<ToneModel> toneList = mDB.getAllScores();

        //TextView textView = (TextView) findViewById(R.id.display_DB);
//        String display = "";

//        Log.d("testing", toneList.get(1).getmMessage());

//        mAdapter = new ToneAdapter(toneList);
//

        if(toneList.size() > 0) {
            mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
//        mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(new ToneAdapter(toneList));
        }else {
            Log.d("debug: ", "Empty data!!!!!!!");
        }


//        List<ToneModel> toneModelList = mDB.getAllScores();
        //convert tone object to JSON string
//        Gson gson = new Gson();

        //return a Gson instance
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();




//        String json = gson.toJson(toneList.get(0));
//        Log.d()

//        for(ToneModel tone : toneList) {
//            display += tone.getmMessage() + "\n";
//            display += tone.getAnger() + "\n";
//            display += tone.getDisgust() + "\n";
//            display += tone.getFear() + "\n";
//            display += tone.getSadness() + "\n";
//            display += tone.getAnalytical() + "\n";
//            display += tone.getConfident() + "\n";
//            display += tone.getTentative() + "\n";
//            display += tone.getOpenness() + "\n";
//            display += tone.getConscientiousness() + "\n";
//            display += tone.getExtraversion() + "\n";
//            display += tone.getAgreeableness() + "\n";
//            display += tone.getEmotionalRange() + "\n";
//        }
//
//        Log.d("testing history: ", display);
//        textView.setText(display);
//        db.closeDataBase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDB.closeDataBase();
    }

    private static class ToneViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTime;
        protected TextView mMessage;
        protected ImageView mImageView;
        private CardView mCardView;

        public ToneViewHolder(View view) {
            super(view);
            mTime = (TextView) view.findViewById(R.id.history_time);
            mMessage = (TextView)view.findViewById(R.id.history_message);
            mImageView = (ImageView)view.findViewById(R.id.border_line);
            mCardView = (CardView) view.findViewById(R.id.cardview_history);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

        }
    }

    private class ToneAdapter extends RecyclerView.Adapter<ToneViewHolder> {
        private List<ToneModel> mToneModelList;

        public ToneAdapter(List<ToneModel> toneList) {
            this.mToneModelList = toneList;
        }

        @Override
        public int getItemCount() {
            return mToneModelList.size();
        }

        @Override
        public void onBindViewHolder(ToneViewHolder toneViewHolder, final int position) {
            ToneModel tone = mToneModelList.get(position);
            randomColorBorderLine(toneViewHolder, position);

            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(tone.getID());
            toneViewHolder.mTime.setText(
                    new SimpleDateFormat(getString(R.string.date_format)).
                            format(cal.getTime()));
            toneViewHolder.mMessage.setText(tone.getmMessage());

            toneViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToneModel tone = mToneModelList.get(position);
                    ConvertToJSON covert = new ConvertToJSON(tone, getApplicationContext());

                    Intent resultIntent = new Intent(getApplicationContext(), ResultActivity.class);
                    resultIntent.putExtra("text", tone.getmMessage());
                    resultIntent.putExtra("analysis", covert.getToneJSONObject().toString());
                    resultIntent.putExtra("id", String.valueOf(tone.getID()));
                    startActivity(resultIntent);

//                    Toast.makeText(getApplicationContext(), covert.getToneJSONObject().toString() , Toast.LENGTH_LONG).show();
                    Log.d("history: ", covert.getToneJSONObject().toString());
                    Log.w("MESSAGE", tone.getmMessage());


                }
            });

        }

        @Override
        public ToneViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.history_tone_card, viewGroup, false);

            return new ToneViewHolder(itemView);
        }

        public void randomColorBorderLine(ToneViewHolder toneViewHolder, int position) {
            int color = position % 5;
            switch (color) {
                case 0:
                    toneViewHolder.mImageView.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                    break;
                case 1:
                    toneViewHolder.mImageView.setBackgroundColor(getResources().getColor(R.color.mighty_purple));
                    break;
                case 2:
                    toneViewHolder.mImageView.setBackgroundColor(getResources().getColor(R.color.fire_red));
                    break;
                case 3:
                    toneViewHolder.mImageView.setBackgroundColor(getResources().getColor(R.color.pineapple_yellow));
                    break;
                case 4:
                    toneViewHolder.mImageView.setBackgroundColor(getResources().getColor(R.color.ocean_green));
                    break;
            }
        }
    }
}
