package com.bigggfish.nestscrollviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigggfish on 2016/12/5.
 * description:
 */
public class TabFragment extends Fragment {

    private final String TAG = "---TabFragment";

    private RecyclerView rv_main;
    private VelocityTracker mVelocityTracker;
    private float lastY = 0;
    private int mMaximumVelocity, mMinimumVelocity;

    private OnRecyclerViewScrollListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        rv_main = (RecyclerView) rootView.findViewById(R.id.id_main_rv);
        rv_main.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv_main.addOnScrollListener(new ScrollerListener());
        rv_main.setOnTouchListener(new ScrollTouchListener());
        init();
        return rootView;
    }


    private void init() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            datas.add("POSITION" + i);
        }
        rv_main.setAdapter(new ListAdapter(datas));

        mMaximumVelocity = ViewConfiguration.get(getActivity())
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(getActivity())
                .getScaledMinimumFlingVelocity();
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public OnRecyclerViewScrollListener getmListener() {
        return mListener;
    }

    public void setmListener(OnRecyclerViewScrollListener mListener) {
        this.mListener = mListener;
    }

    class ListAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<String> mData;

        private ListAdapter(List<String> datas) {
            this.mData = datas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_rv_main, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv_item_main.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_item_main;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_main = (TextView) itemView.findViewById(R.id.tv_item_main);
        }
    }

    class ScrollerListener extends RecyclerView.OnScrollListener {

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            Log.e(TAG, "dy:" + dy);
            int firstPosition = ((LinearLayoutManager) rv_main.getLayoutManager()).findFirstVisibleItemPosition();
            if(mListener != null){
                mListener.onScroll(firstPosition, dx, dy);
            }
        }
    }

    public void setRecyclerTopPadding(int topPadding){
        rv_main.setPadding(0, topPadding, 0, 0);
    }

    class ScrollTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            initVelocityTrackerIfNotExists();
            mVelocityTracker.addMovement(e);
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_UP:
                    int firstPositionUp = ((LinearLayoutManager) rv_main.getLayoutManager()).findFirstVisibleItemPosition();
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int velocityY = (int) mVelocityTracker.getYVelocity();
                    Log.e("ACTION_UP", "velocityY:" + velocityY + "mMinimumVelocity:" + mMinimumVelocity + "firstPositionUp:" + firstPositionUp);
                    if (Math.abs(velocityY) > mMinimumVelocity && velocityY > 0 && firstPositionUp <= 0) {
                        if(mListener != null){
                            mListener.onFling(-velocityY);
                        }
                    }
                    recycleVelocityTracker();
                    break;
                case MotionEvent.ACTION_MOVE:

                    int firstPosition = ((LinearLayoutManager) rv_main.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    float y = e.getY();
                    Log.e(TAG, "lastY:" + lastY + "y:" + y);
                    if(mListener != null){
                        mListener.onMoveScroll(firstPosition, 0, (int) (y - lastY));
                    }
                    lastY = y;
                    break;
            }
            lastY = e.getY();
            return false;
        }
    }

    public interface OnRecyclerViewScrollListener{
        void onScroll(int firstPosition, int dx, int dy);
        void onMoveScroll(int firstPosition, int dx, int dy);
        void onFling(int velocityY);
    }

}
