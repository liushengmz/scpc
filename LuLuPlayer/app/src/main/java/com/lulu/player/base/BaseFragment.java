package com.lulu.player.base;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lulu.player.common.Constants;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment extends Fragment {

    protected FragmentManager fm;

    protected String mTitle;

    protected int mIndex;

    protected View view;

    protected OnFragmentInteractionListener mListener;

    public BaseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        fm = getActivity().getSupportFragmentManager();
        if (getArguments() != null) {
            mTitle = getArguments().getString(Constants.TITLE);
            mIndex = getArguments().getInt(Constants.INDEX);
        }
//        setUpComponent();
    }

    public int getShownIndex() {
        return mIndex;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(getFragmentLayout(), container, false);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisibleToUser();
        } else {
            onInVisibleToUser();
        }
    }

//    public void setUpComponent() {
//
//    }

    protected abstract int getFragmentLayout();

    public void onVisibleToUser() {
    }

    public void onInVisibleToUser() {
    }

    protected abstract void initDatas();

    protected abstract void initViews();

    protected abstract void initListeners();

    protected abstract void initAdapters();

    protected void init() {
        initViews();
        initDatas();
        initAdapters();
        initListeners();
    }

    protected void injectView(final View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        injectView(view);
        init();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected FragmentManager getFm() {
        return this.fm;
    }

	/*
     * @SuppressWarnings("unchecked") protected <T extends View> T
	 * getViewById(View view, int id) { return (T) view.findViewById(id); }
	 *
	 * @SuppressWarnings("unchecked") protected <T extends View> T
	 * getViewById(int id) { return (T) view.findViewById(id); }
	 */

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onUnsubscribe();
    }

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeSubscription();
//        }
        mCompositeSubscription.add(subscription);
    }
}
