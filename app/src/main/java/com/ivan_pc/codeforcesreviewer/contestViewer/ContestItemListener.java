package com.ivan_pc.codeforcesreviewer.contestViewer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ivan_pc.codeforcesreviewer.models.Contest;

import java.util.List;

/**
 * Created by nikita on 20.12.16.
 */

public class ContestItemListener implements RecyclerView.OnItemTouchListener {

	public final ContestTouchListener listener;
	private GestureDetector detector;

	public ContestItemListener(Context context, final RecyclerView rv, final ContestTouchListener listener) {
		this.listener = listener;

		detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				View v = rv.findChildViewUnder(e.getX(), e.getY());
				listener.onClick(v, rv.getChildAdapterPosition(v));
				return true;
			}
		});
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
		View child = rv.findChildViewUnder(e.getX(), e.getY());
		return (child != null && detector.onTouchEvent(e));
	}

	@Override
	public void onTouchEvent(RecyclerView rv, MotionEvent e) {
	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

	}

	public interface ContestTouchListener {
		void onClick(View v, int pos);
		void getContest(List<Contest> contests, String locale);
	}
}
