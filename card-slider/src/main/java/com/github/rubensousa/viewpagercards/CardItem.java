package com.github.rubensousa.viewpagercards;


import android.view.View;

public class CardItem {

    //    public static final int NO_TITLE = -1;
    public static final int CARD_GOALS = 0, CARD_DONATE = 1, CARD_ADOPT = 2;
    private int mCardType;
    private String mTextResource;
    private String mTitleResource;
    private String mPreTitleResource;


    private String mBtnTitle;

    private View.OnClickListener btnListener;

    public CardItem(int cardType, String preTitle, String title, String text) {
        mCardType = cardType;
        mPreTitleResource = preTitle;
        mTitleResource = title;
        mTextResource = text;
    }

    public CardItem(int cardType, String title, String btnTitle, View.OnClickListener listener) {
        btnListener = listener;
        mCardType = cardType;
        mTitleResource = title;
        mBtnTitle = btnTitle;
    }

    public String getBtnTitle() {
        return mBtnTitle;
    }
    public View.OnClickListener getBtnListener() {
        return btnListener;
    }

    public int getCardType() {
        return mCardType;
    }

    public String getPreTitle() {
        return mPreTitleResource;
    }

    public String getText() {
        return mTextResource;
    }

    public String getTitle() {
        return mTitleResource;
    }
}
