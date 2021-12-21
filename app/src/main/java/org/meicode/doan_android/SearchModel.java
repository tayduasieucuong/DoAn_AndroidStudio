package org.meicode.doan_android;

import ir.mirrajabi.searchdialog.core.Searchable;

public class SearchModel implements Searchable {
    private String mTitle;
    public SearchModel(String title){
        this.mTitle=title;
    }
    public void setTitle(String title){
        this.mTitle=title;
    }
    @Override
    public String getTitle() {
        return mTitle;
    }
}
