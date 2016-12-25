package com.ivan_pc.codeforcesreviewer.contestViewer;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ivan_pc.codeforcesreviewer.MainActivity;
import com.ivan_pc.codeforcesreviewer.R;
import com.ivan_pc.codeforcesreviewer.models.Contest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ContestActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contest);

		Contest interestContest = getIntent().getParcelableExtra(MainActivity.INTEREST_CONTEST);
		String locale = getIntent().getStringExtra("locale");
		System.out.println(interestContest.name + " " + interestContest.preparedBy);

		TextView contestName = (TextView) findViewById(R.id.activity_contest_name);
		TextView contestType = (TextView) findViewById(R.id.activity_contest_type);
		TextView contestDescription = (TextView) findViewById(R.id.activity_contest_description);
		TextView contestPhase = (TextView) findViewById(R.id.activity_contest_phase);
		TextView contestDuration = (TextView) findViewById(R.id.activity_contest_duration);
		TextView contestStartTime = (TextView) findViewById(R.id.activity_contest_start_time);
		TextView contestRelativeTime = (TextView) findViewById(R.id.activity_contest_relative_time);
		TextView contestPreparedBy = (TextView) findViewById(R.id.activity_contest_prepared_by);
		TextView contestWebsite = (TextView) findViewById(R.id.activity_contest_website);
		TextView contestDifficulty = (TextView) findViewById(R.id.activity_contest_difficulty);
		TextView contestKind = (TextView) findViewById(R.id.activity_contest_kind);
		TextView contestICPCRegion = (TextView) findViewById(R.id.activity_contest_icpc_region);
		TextView contestCountry = (TextView) findViewById(R.id.activity_contest_country);
		TextView contestCity = (TextView) findViewById(R.id.activity_contest_city);
		TextView contestSeason = (TextView) findViewById(R.id.activity_contest_season);

		TextView contestTypeSign = (TextView) findViewById(R.id.activity_contest_type_sign);
		TextView contestDescriptionSign = (TextView) findViewById(R.id.activity_contest_description_sign);
		TextView contestPhaseSign = (TextView) findViewById(R.id.activity_contest_phase_sign);
		TextView contestDurationSign = (TextView) findViewById(R.id.activity_contest_duration_sign);
		TextView contestStartTimeSign = (TextView) findViewById(R.id.activity_contest_start_time_sign);
		TextView contestRelativeTimeSign = (TextView) findViewById(R.id.activity_contest_relative_time_sign);
		TextView contestPreparedBySign = (TextView) findViewById(R.id.activity_contest_prepared_by_sign);
		TextView contestWebsiteSign = (TextView) findViewById(R.id.activity_contest_website_sign);
		TextView contestDifficultySign = (TextView) findViewById(R.id.activity_contest_difficulty_sign);
		TextView contestKindSign = (TextView) findViewById(R.id.activity_contest_kind_sign);
		TextView contestICPCRegionSign = (TextView) findViewById(R.id.activity_contest_icpc_region_sign);
		TextView contestCountrySign = (TextView) findViewById(R.id.activity_contest_country_sign);
		TextView contestCitySign = (TextView) findViewById(R.id.activity_contest_city_sign);
		TextView contestSeasonSign = (TextView) findViewById(R.id.activity_contest_season_sign);

		contestTypeSign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.type : R.string.type_ru);
		contestDescriptionSign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.description : R.string.description_ru);
		contestPhaseSign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.phase : R.string.phase_ru);
		contestDurationSign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.duration : R.string.duration_ru);
		contestStartTimeSign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.start_time : R.string.start_time_ru);
		contestRelativeTimeSign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.till_end : R.string.till_end_ru);
		contestPreparedBySign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.author : R.string.author_ru);
		contestWebsiteSign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.website : R.string.website_ru);
		contestDifficultySign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.difficulty : R.string.difficulty_ru);
		contestKindSign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.kind : R.string.kind_ru);
		contestICPCRegionSign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.icpc_region : R.string.icpc_region_ru);
		contestCountrySign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.country : R.string.country_ru);
		contestCitySign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.city : R.string.city_ru);
		contestSeasonSign.setText(locale.equals(MainActivity.ENGLISH) ? R.string.season : R.string.season_ru);

		LinearLayout contestDescriptionContainer = (LinearLayout) findViewById(R.id.activity_contest_description_container);
		LinearLayout contestStartTimeContainer = (LinearLayout) findViewById(R.id.activity_contest_start_time_container);
		LinearLayout contestRelativeTimeContainer = (LinearLayout) findViewById(R.id.activity_contest_relative_time_container);
		LinearLayout contestPreparedByContainer = (LinearLayout) findViewById(R.id.activity_contest_prepared_by_container);
		LinearLayout contestWebsiteContainer = (LinearLayout) findViewById(R.id.activity_contest_website_container);
		LinearLayout contestDifficultyContainer = (LinearLayout) findViewById(R.id.activity_contest_difficulty_container);
		LinearLayout contestKindContainer = (LinearLayout) findViewById(R.id.activity_contest_kind_container);
		LinearLayout contestICPCRegionContainer = (LinearLayout) findViewById(R.id.activity_contest_icpc_region_container);
		LinearLayout contestCountryContainer = (LinearLayout) findViewById(R.id.activity_contest_country_container);
		LinearLayout contestCityContainer = (LinearLayout) findViewById(R.id.activity_contest_city_container);
		LinearLayout contestSeasonContainer = (LinearLayout) findViewById(R.id.activity_contest_season_container);

		contestName.setText(interestContest.name);
		contestType.setText(interestContest.type);
		contestDuration.setText(DateUtils.formatElapsedTime(interestContest.duration));

		if (interestContest.preparedBy != null) {
			contestPreparedBy.setText(interestContest.preparedBy);
		} else {
			contestPreparedByContainer.setVisibility(View.GONE);
		}

		if (interestContest.websiteURL != null) {
			contestWebsite.setText(interestContest.websiteURL);
		} else {
			contestWebsiteContainer.setVisibility(View.GONE);
		}

		if (interestContest.kind != null) {
			contestKind.setText(interestContest.kind);
		} else {
			contestKindContainer.setVisibility(View.GONE);
		}

		if (interestContest.icpcRegion != null) {
			contestICPCRegion.setText(interestContest.icpcRegion);
		} else {
			contestICPCRegionContainer.setVisibility(View.GONE);
		}

		if (interestContest.country != null) {
			contestCountry.setText(interestContest.country);
		} else {
			contestCountryContainer.setVisibility(View.GONE);
		}

		if (interestContest.city != null) {
			contestCity.setText(interestContest.city);
		} else {
			contestCityContainer.setVisibility(View.GONE);
		}

		if (interestContest.season != null) {
			contestSeason.setText(interestContest.season);
		} else {
			contestSeasonContainer.setVisibility(View.GONE);
		}

		switch (interestContest.phase) {
			case "BEFORE":
				contestPhase.setText((locale.equals(MainActivity.ENGLISH)) ? R.string.status_before : R.string.status_before_ru);
				contestPhase.setTextColor(ContextCompat.getColor(this, R.color.status_before));
				break;
			case "CODING":
				contestPhase.setText((locale.equals(MainActivity.ENGLISH)) ? R.string.status_going : R.string.status_going_ru);
				contestPhase.setTextColor(ContextCompat.getColor(this, R.color.status_going));
				break;
			case "PENDING_SYSTEM_TEST":
				contestPhase.setText((locale.equals(MainActivity.ENGLISH)) ? R.string.waiting_for_system_testing : R.string.waiting_for_system_testing_ru);
				contestPhase.setTextColor(ContextCompat.getColor(this, R.color.waiting_for_system_testing));
				break;
			case "SYSTEM_TEST":
				contestPhase.setText((locale.equals(MainActivity.ENGLISH)) ? R.string.status_testing : R.string.status_testing_ru);
				contestPhase.setTextColor(ContextCompat.getColor(this, R.color.status_testing));
				break;
			case "FINISHED":
				contestPhase.setText((locale.equals(MainActivity.ENGLISH)) ? R.string.status_finished : R.string.status_finished_ru);
				contestPhase.setTextColor(ContextCompat.getColor(this, R.color.status_finished));
				break;
		}

		if (interestContest.difficulty != 0) {
			String that = String.valueOf(interestContest.difficulty) + "/5";
			contestDifficulty.setText(String.valueOf(that));
		} else {
			contestDifficultyContainer.setVisibility(View.GONE);
		}

		if (interestContest.description != null) {
			contestDescription.setText(interestContest.description);
		} else {
			contestDescriptionContainer.setVisibility(View.GONE);
		}

		if (interestContest.phase.equals("BEFORE") && interestContest.startTime != -1) {
			Date date = new Date(interestContest.startTime * 1000);
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", new Locale(locale));
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			contestStartTime.setText(format.format(date));
		} else {
			contestStartTimeContainer.setVisibility(View.GONE);
		}

		if (interestContest.phase.equals("CODING") && interestContest.relativeTime != -1) {
			contestRelativeTime.setText(DateUtils.formatElapsedTime(interestContest.relativeTime));
			contestRelativeTime.setText(locale.equals("en") ? R.string.till_end : R.string.till_end_ru);
		} else {
			contestRelativeTimeContainer.setVisibility(View.GONE);
		}
	}
}
