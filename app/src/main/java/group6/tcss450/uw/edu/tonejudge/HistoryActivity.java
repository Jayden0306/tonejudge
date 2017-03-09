package group6.tcss450.uw.edu.tonejudge;

import android.content.Intent;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This class display the message user enter before.
 *
 * @author Jayden Tan
 * @version 03/09/2017
 */
public class HistoryActivity extends NavDrawerActivity {
    /**
     * the linear layout manager
     */
    private LinearLayoutManager mLayoutManager;
    /**
     * the recycler view display card view
     */
    private RecyclerView mRecyclerView;
    /**
     * the local database
     */
    private DataBaseHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        onCreateDrawer();

        //initialize the local database
        mDB = new DataBaseHelper(this);
        //a list of tone model object return it from the database query
        List<ToneModel> toneList = mDB.getAllScores();

        //display history if tone list is not null
        if(toneList.size() > 0) {
            mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(new ToneAdapter(toneList));
        }
    }

    /**
     * close the database when no long used
     */
    @Override
    protected void onStop() {
        super.onStop();
        mDB.closeDataBase();
    }

    /**
     * this class contains the reference to the each ui widge on the card view
     */
    private static class ToneViewHolder extends RecyclerView.ViewHolder {
        /**
         * the time the user enter message
         */
        protected TextView mTime;
        /**
         * the message that user enter
         */
        protected TextView mMessage;
        /**
         * the left border line show in the card view
         */
        protected ImageView mImageView;
        /**
         * the cardview
         */
        private CardView mCardView;

        public ToneViewHolder(View view) {
            super(view);
            mTime = (TextView) view.findViewById(R.id.history_time);
            mMessage = (TextView)view.findViewById(R.id.history_message);
            mImageView = (ImageView)view.findViewById(R.id.border_line);
            mCardView = (CardView) view.findViewById(R.id.cardview_history);
        }
    }

    /**
     * Adapters provide a binding from an specific data set to UI views
     * it displays the history of the results
     */
    private class ToneAdapter extends RecyclerView.Adapter<ToneViewHolder> {
        private List<ToneModel> mToneModelList;

        /**
         * initialize the tone adapter
         * @param toneList a list of tone model objects
         */
        public ToneAdapter(List<ToneModel> toneList) {
            this.mToneModelList = toneList;
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         * @return total number of items in the data set held by the adapter.
         */
        @Override
        public int getItemCount() {
            return mToneModelList.size();
        }

        /**
         * set up the data to be display in the recylerView
         * @param toneViewHolder the tone view holder
         * @param position the positions of the recyler view
         */
        @Override
        public void onBindViewHolder(ToneViewHolder toneViewHolder, final int position) {
            ToneModel tone = mToneModelList.get(position);
            randomColorBorderLine(toneViewHolder, position);
            //change the id into readable date format
            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(tone.getID());
            toneViewHolder.mTime.setText(
                    new SimpleDateFormat(getString(R.string.date_format)).
                            format(cal.getTime()));
            //change the text from mMessage text view
            toneViewHolder.mMessage.setText(tone.getmMessage());

            toneViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //pass information and switch to the result activity
                    //when user select specific history card
                    ToneModel tone = mToneModelList.get(position);
                    ConvertToJSON covert = new ConvertToJSON(tone, getApplicationContext());

                    Intent resultIntent = new Intent(getApplicationContext(), ResultActivity.class);
                    resultIntent.putExtra("text", tone.getmMessage());
                    resultIntent.putExtra("analysis", covert.getToneJSONObject().toString());
                    resultIntent.putExtra("id", String.valueOf(tone.getID()));
                    startActivity(resultIntent);

                }
            });

        }

        /**
         *create a new RecyclerView.ViewHolder and
         *  return ViewHolder inflating the row with card view layout
         * @param viewGroup the view
         * @param viewType the view type
         * @return ViewHolder inflating the row with card view layout
         */
        @Override
        public ToneViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.history_tone_card, viewGroup, false);

            return new ToneViewHolder(itemView);
        }

        /**
         * change the left border line base on the position of the card
         * @param toneViewHolder the view holder
         * @param position the current position selected
         */
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
