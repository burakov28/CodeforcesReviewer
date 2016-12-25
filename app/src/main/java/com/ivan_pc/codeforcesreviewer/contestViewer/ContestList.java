package com.ivan_pc.codeforcesreviewer.contestViewer;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ivan_pc.codeforcesreviewer.R;
import com.ivan_pc.codeforcesreviewer.models.Contest;

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
	private final Context context;

	public ContestList(Context context, List<Contest> contests, String locale) {
		this.inflater = LayoutInflater.from(context);
		this.contests = contests;
		this.locale = locale;
		this.context = context;
	}

	@Override
	public ContestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ContestViewHolder(inflater.inflate(R.layout.contest_list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ContestViewHolder holder, int position) {
		holder.author_sign.setText(locale.equals("en") ? R.string.author : R.string.author_ru);
		holder.length_sign.setText(locale.equals("en") ? R.string.duration : R.string.duration_ru);
		holder.complexity_sign.setText(locale.equals("en") ? R.string.difficulty : R.string.difficulty_ru);

		Contest contest = contests.get(position);
		holder.name.setText(contest.name);
		switch (contest.phase) {
			case "BEFORE":
				holder.phase.setText((locale.equals("en")) ? R.string.status_before : R.string.status_before_ru);
				holder.phase.setTextColor(ContextCompat.getColor(context, R.color.status_before));
				break;
			case "CODING":
				holder.phase.setText((locale.equals("en")) ? R.string.status_going : R.string.status_going_ru);
				holder.phase.setTextColor(ContextCompat.getColor(context, R.color.status_going));
				break;
			case "PENDING_SYSTEM_TEST":
				holder.phase.setText((locale.equals("en")) ? R.string.waiting_for_system_testing : R.string.waiting_for_system_testing_ru);
				holder.phase.setTextColor(ContextCompat.getColor(context, R.color.waiting_for_system_testing));
				break;
			case "SYSTEM_TEST":
				holder.phase.setText((locale.equals("en")) ? R.string.status_testing : R.string.status_testing_ru);
				holder.phase.setTextColor(ContextCompat.getColor(context, R.color.status_testing));
				break;
			case "FINISHED":
				holder.phase.setText((locale.equals("en")) ? R.string.status_finished : R.string.status_finished_ru);
				holder.phase.setTextColor(ContextCompat.getColor(context, R.color.status_finished));
				break;
		}
		if (contest.difficulty != 0) {
			holder.difficulty.setProgress(contest.difficulty);
			holder.difficultyContainer.setVisibility(View.VISIBLE);
		} else {
			holder.difficultyContainer.setVisibility(View.GONE);
		}
		if (contest.preparedBy != null) {
			holder.preparedBy.setText(contest.preparedBy);
		} else {
			holder.preparedBy.setText(locale.equals("en") ? R.string.no_author : R.string.no_author_ru);
		}
		holder.duration.setText(DateUtils.formatElapsedTime(contest.duration));
		if (contest.phase.equals("BEFORE") && contest.startTime != -1) {
			Date date = new Date(contest.startTime * 1000);
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", new Locale(locale));
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			holder.time.setText(format.format(date));
			holder.timeName.setText(locale.equals("en") ? R.string.start_time : R.string.start_time_ru);
			holder.timeContainer.setVisibility(View.VISIBLE);
		} else if (contest.phase.equals("CODING") && contest.relativeTime != -1) {
			holder.time.setText(DateUtils.formatElapsedTime(contest.relativeTime));
			holder.timeName.setText(locale.equals("en") ? R.string.till_end : R.string.till_end_ru);
			holder.timeContainer.setVisibility(View.VISIBLE);
		} else {
			holder.timeContainer.setVisibility(View.GONE);
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
		final LinearLayout timeContainer;
		final LinearLayout difficultyContainer;
		final TextView author_sign;
		final TextView complexity_sign;
		final TextView length_sign;

		ContestViewHolder(View itemView) {
			super(itemView);

			name = (TextView)itemView.findViewById(R.id.contest_name);
			phase = (TextView)itemView.findViewById(R.id.status);
			time = (TextView)itemView.findViewById(R.id.time);
			preparedBy = (TextView)itemView.findViewById(R.id.author);
			difficulty = (ProgressBar)itemView.findViewById(R.id.difficulty);
			duration = (TextView)itemView.findViewById(R.id.duration);
			timeName = (TextView)itemView.findViewById(R.id.time_name);
			timeContainer = (LinearLayout)itemView.findViewById(R.id.time_container);
			difficultyContainer = (LinearLayout)itemView.findViewById(R.id.difficulty_container);
			author_sign = (TextView)itemView.findViewById(R.id.author_sign);
			length_sign = (TextView)itemView.findViewById(R.id.length_sign);
			complexity_sign = (TextView)itemView.findViewById(R.id.complexity_sign);
		}
	}
}
