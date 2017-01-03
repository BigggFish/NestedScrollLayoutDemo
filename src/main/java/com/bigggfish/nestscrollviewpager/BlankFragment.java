package com.bigggfish.nestscrollviewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private RecyclerView rv_main;
    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance() {
        BlankFragment fragment = new BlankFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        rv_main = (RecyclerView)rootView.findViewById(R.id.rv_main);
        init();
        return rootView;
    }

    private void init(){
        List<String> datas = new ArrayList<>();
        for(int i=0; i< 20; i++){
            datas.add("POSITION" + i);
        }
        rv_main.setAdapter(new ListAdapter(datas));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class ListAdapter extends RecyclerView.Adapter<ViewHolder>{

        private List<String> mData ;
        private ListAdapter(List<String> datas){
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

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_item_main;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_main = (TextView) itemView.findViewById(R.id.tv_item_main);
        }
    }

}
