package com.lulu.player.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lulu.player.base.BaseFragment;

import java.util.List;

public class FragmentsAdapter extends FragmentPagerAdapter {

	private List<BaseFragment> mFragments;

	public FragmentsAdapter(FragmentManager fm) {
		super(fm);
	}

	public FragmentsAdapter(FragmentManager fm, List<BaseFragment> mFragments) {
		super(fm);
		this.mFragments = mFragments;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

}
