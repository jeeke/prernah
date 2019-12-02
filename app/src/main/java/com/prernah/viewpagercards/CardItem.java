package com.prernah.viewpagercards;


import android.view.View;

public class CardItem {

    //    public static final int NO_TITLE = -1;
    public static final int CARD_GOALS = 0, CARD_DONATE = 1, CARD_ADOPT = 2;
    private int mCardType = -1;

    public void setPrice(int price) {
        this.mPrice = price;
    }

    private int mPrice;
    private String mTitleResource;


    private String mBtnTitle;

    private View.OnClickListener onClickListener;

    public CardItem(int cardType, String title, int price) {
        mCardType = cardType;
        mTitleResource = title;
        mPrice = price;
    }

    public CardItem(int defaultPrice, View.OnClickListener editor){
        onClickListener = editor;
        mPrice = defaultPrice;
    }

    public CardItem(int cardType, String title, String btnTitle, View.OnClickListener listener) {
        onClickListener = listener;
        mCardType = cardType;
        mTitleResource = title;
        mBtnTitle = btnTitle;
    }

    public String getBtnTitle() {
        return mBtnTitle;
    }
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public int getCardType() {
        return mCardType;
    }

    public int getPrice() {
        return mPrice;
    }

    public String getTitle() {
        return mTitleResource;
    }
}
