package com.ivan_pc.codeforcesreviewer;

import android.content.Context;
import android.provider.SyncStateContract;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by nikita on 20.12.16.
 */

public class ContestList extends RecyclerView.Adapter<ContestList.ContestViewHolder> {
	private final List<Contest> contests;
	private final LayoutInflater inflater;
	private final String locale;

	public ContestList(Context context, List<Contest> contests, String locale) {
		this.inflater = LayoutInflater.from(context);
		this.contests = contests;
		this.locale = locale;
	}

	@Override
	public ContestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ContestViewHolder(inflater.inflate(R.layout.contest_list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ContestViewHolder holder, int position) {
		Contest contest = contests.get(position);
		holder.name.setText(contest.name);
		holder.phase.setText(contest.phase);
		if (contest.difficulty != 0) {
			holder.difficulty.setProgress(contest.difficulty);
			holder.difficulty.setVisibility(View.VISIBLE);
		} else {
			holder.difficulty.setVisibility(View.INVISIBLE);
		}
		if (contest.preparedBy != null) {
			holder.preparedBy.setText(contest.preparedBy);
		}
		holder.duration.setText(DateUtils.formatElapsedTime(contest.duration));
		Date date = new Date(contest.startTime);
		if (contest.phase.equals("BEFORE")) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm", new Locale(locale));
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			holder.time.setText(format.format(date));
			holder.timeName.setText(R.string.start_time);
			holder.time.setVisibility(View.VISIBLE);
			holder.timeName.setVisibility(View.VISIBLE);
		} else if (contest.phase.equals("FINISHED")) {
			holder.time.setVisibility(View.INVISIBLE);
			holder.timeName.setVisibility(View.INVISIBLE);
		} else if (contest.phase.equals("CODING")) {
			holder.time.setText(DateUtils.formatElapsedTime(contest.relativeTime));
			holder.timeName.setText(R.string.till_end);
			holder.time.setVisibility(View.VISIBLE);
			holder.timeName.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public int getItemCount() {
		return contests.size();
	}

	class ContestViewHolder extends RecyclerView.ViewHolder {

		final TextView name;
		final TextView phase;
		//final TextView duration;
		final TextView time;
		final TextView preparedBy;
		final ProgressBar difficulty;
		final TextView duration;
		final TextView timeName;

		public ContestViewHolder(View itemView) {
			super(itemView);

			name = (TextView)itemView.findViewById(R.id.contest_name);
			phase = (TextView)itemView.findViewById(R.id.status);
			time = (TextView)itemView.findViewById(R.id.time);
			preparedBy = (TextView)itemView.findViewById(R.id.author);
			difficulty = (ProgressBar)itemView.findViewById(R.id.difficulty);
			duration = (TextView)itemView.findViewById(R.id.duration);
			timeName = (TextView)itemView.findViewById(R.id.time_name);
		}
	}
}
