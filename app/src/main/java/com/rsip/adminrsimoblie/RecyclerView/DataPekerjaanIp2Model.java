package com.rsip.adminrsimoblie.RecyclerView;

import java.util.ArrayList;

public class DataPekerjaanIp2Model {

    private String title;

    private String message;


    public DataPekerjaanIp2Model(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public DataPekerjaanIp2Model() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
