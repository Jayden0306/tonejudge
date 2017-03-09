package group6.tcss450.uw.edu.tonejudge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class TopRanksActivity extends NavDrawerActivity implements ExpandableListView.OnGroupClickListener {

    private ExpandableListView mListView;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ranks);
        mListView = (ExpandableListView) findViewById(R.id.top_ranks_expandable_list);
        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(this);
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
        onCreateDrawer();
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    public void onClickInfo(View v) {
        Tone tone = (Tone) v.getTag();
        new AlertDialog.Builder(this)
                .setMessage(tone.getDescription(this))
                .setTitle(tone.getName())
                .show();
    }

    public void onClickTone(View v) {
        Intent intent = new Intent(this, TopRanksToneActivity.class);
        intent.putExtra("tone", (Tone) v.getTag());
        startActivity(intent);
    }

    private class Adapter extends BaseExpandableListAdapter {

        private Tone.Category[] groups = Tone.Category.values();

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groups[groupPosition].getTones().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groups[groupPosition].getTones().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView != null) {
                return convertView;
            } else {
                TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.top_ranks_group_name, parent, false);
                tv.setText(groups[groupPosition].getName());
                return tv;
            }
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView != null) {
                return convertView;
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_ranks_element, parent, false);
                TextView toneView = (TextView) view.findViewById(R.id.top_ranks_element_tone);
                View infoView = view.findViewById(R.id.top_ranks_element_info);
                Tone tone = groups[groupPosition].getTones().get(childPosition);
                toneView.setText(tone.getName());
                toneView.setTag(tone);
                infoView.setTag(tone);
                toneView.setBackgroundColor(getResources().getColor(tone.getColorId()));
                ViewCompat.setElevation(toneView, 8.0f);
                return view;
            }
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
