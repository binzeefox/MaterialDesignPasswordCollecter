package com.binzeefox.materialdesignpasswordcollecter.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.binzeefox.materialdesignpasswordcollecter.R;
import com.binzeefox.materialdesignpasswordcollecter.model.Card;

import java.util.List;

/**
 * Created by tong.xiwen on 2017/4/19.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{

    private Context mContext;
    private List<Card> mCardList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView cardType;
        TextView cardName;
        TextView cardTime;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            cardType = (TextView) view.findViewById(R.id.card_type);
            cardName = (TextView) view.findViewById(R.id.card_name);
            cardTime = (TextView) view.findViewById(R.id.card_time);
        }
    }

    public CardAdapter(List<Card> cardList) {
        mCardList = cardList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = mCardList.get(position);
        holder.cardType.setText(card.getAccountType());
        holder.cardName.setText(card.getUserName());
        holder.cardTime.setText(card.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }
}
