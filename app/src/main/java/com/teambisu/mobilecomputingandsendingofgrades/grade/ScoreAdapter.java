package com.teambisu.mobilecomputingandsendingofgrades.grade;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teambisu.mobilecomputingandsendingofgrades.R;
import com.teambisu.mobilecomputingandsendingofgrades.model.Grades;
import com.teambisu.mobilecomputingandsendingofgrades.model.Score;

import java.util.ArrayList;

/**
 * Created by John Manuel on 03/09/2017.
 */

public class ScoreAdapter extends ArrayAdapter<Score> {
    private Activity activity;
    private ArrayList<Score> scores;
    private static LayoutInflater inflater = null;

    public ScoreAdapter(Activity activity, int resource, ArrayList<Score> scores) {
        super(activity, resource, scores);
        this.activity = activity;
        this.scores = scores;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return scores.size();
    }

    public Score getItem(Score position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView tv_date;
        public TextView tv_testname;
        public TextView tv_score_status;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.item_score, null);
            holder = new ViewHolder();

            holder.tv_date = (TextView) vi.findViewById(R.id.tv_date);
            holder.tv_testname = (TextView) vi.findViewById(R.id.tv_testname);
            holder.tv_score_status = (TextView) vi.findViewById(R.id.tv_score_status);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.tv_date.setText(scores.get(position).getDate());
        holder.tv_testname.setText(scores.get(position).getTest_name());
        holder.tv_score_status.setText(scores.get(position).getScore() + "/" + scores.get(position).getMaximum_score());
        Log.d("test", "score " + scores.get(position).getScore());
        return vi;
    }
}
