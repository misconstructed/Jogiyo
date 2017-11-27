package com.example.misconstructed.jogiyo;

/**
 * Created by Administrator on 2017-11-26.
 */

public class ListItem {
    private String name;
    private String time;
    private boolean check;
    private boolean star;

    ListItem(String name, String time, boolean check, boolean star){
        this.name=name;
        this.time=time;
        this.check=check;
        this.star=star;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isStar() {
        return star;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void setStar(boolean star) {
        this.star = star;
    }
}
