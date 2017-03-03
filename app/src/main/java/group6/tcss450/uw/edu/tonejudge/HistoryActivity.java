package group6.tcss450.uw.edu.tonejudge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class HistoryActivity extends NavDrawerActivity {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ToneAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        onCreateDrawer();

        DataBaseHelper db = new DataBaseHelper(this);
        Log.d("Reading", "Reading all contacts...");
        List<ToneModel> toneList = db.getAllScores();

        //TextView textView = (TextView) findViewById(R.id.display_DB);
        String display = "";

//        Log.d("testing", toneList.get(1).getmMessage());

//        mAdapter = new ToneAdapter(toneList);
//
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
//        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new ToneAdapter(db.getAllScores()));


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

    }

    private static class ToneViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTime;
        protected TextView mMessage;
        protected ImageView mImageView;

        public ToneViewHolder(View view) {
            super(view);
            mTime = (TextView) view.findViewById(R.id.history_time);
            mMessage = (TextView)view.findViewById(R.id.history_message);
            mImageView = (ImageView)view.findViewById(R.id.border_line);

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
        public void onBindViewHolder(ToneViewHolder toneViewHolder, int position) {
            ToneModel tone = mToneModelList.get(position);
            randomColorBorderLine(toneViewHolder, position);

            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(tone.getID());
            toneViewHolder.mTime.setText(
                    new SimpleDateFormat(getString(R.string.date_format)).
                            format(cal.getTime()));
            toneViewHolder.mMessage.setText(tone.getmMessage());
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
