package com.binzeefox.materialdesignpasswordcollecter.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binzeefox.materialdesignpasswordcollecter.R;
import com.binzeefox.materialdesignpasswordcollecter.adapter.CardAdapter;
import com.binzeefox.materialdesignpasswordcollecter.model.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserHomeFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private FloatingActionButton fab_create;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_home, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.home_cards);
        fab_create = (FloatingActionButton) v.findViewById(R.id.fab_create);

        testRecyclerView();
        return v;
    }

        @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fab_create:
                //TODO 弹出创建对话框
        }
    }

    /**
     * 测试recycleView
     */
    private List<Card> cardList = new ArrayList<>();
    private CardAdapter adapter;
    private void testRecyclerView(){
        cardList.clear();
        Card card = new Card();
        card.setCreateTime("2016年07月03日21:32");
        card.setAccountType("百度");

        for (int i = 0; i < 50; i++){
            card.setUserName("鬼狐冰杰" + String.valueOf(i));
            cardList.add(card);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CardAdapter(cardList);
        recyclerView.setAdapter(adapter);
    }
}
