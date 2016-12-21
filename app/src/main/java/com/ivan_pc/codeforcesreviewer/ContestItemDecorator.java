package com.ivan_pc.codeforcesreviewer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nikita on 20.12.16.
 */

public class ContestItemDecorator extends RecyclerView.ItemDecoration {
	private Drawable rec;

	public ContestItemDecorator(Drawable rec) {
		this.rec = rec;
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
		super.onDrawOver(c, parent, state);

		final int left = parent.getPaddingLeft();
		final int right = parent.getWidth() - parent.getPaddingRight();

		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
			final int top = child.getBottom() + params.bottomMargin / 2;
			final int bottom = top + rec.getIntrinsicHeight();
			//System.out.println(top + " " + bottom + " | " + right + " " + bottom + " --> " + rec.getIntrinsicHeight());
			rec.setBounds(left, top, right, bottom);
			rec.draw(c);
		}
	}
}
