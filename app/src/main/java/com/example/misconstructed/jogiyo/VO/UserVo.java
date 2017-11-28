package com.example.misconstructed.jogiyo.VO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by misconstructed on 2017. 11. 28..
 */

public class UserVo implements Parcelable{
    private String user_name;
    private String id;
    private String password;

    public UserVo(){
        this.user_name = null;
        this.id = null;
        this.password = null;
    }
    public UserVo(String user_name, String id, String password){
        this.user_name = user_name;
        this.id = id;
        this.password = password;
    }

    protected UserVo(Parcel in) {
        user_name = in.readString();
        id = in.readString();
        password = in.readString();
    }

    public static final Creator<UserVo> CREATOR = new Creator<UserVo>() {
        @Override
        public UserVo createFromParcel(Parcel in) {
            return new UserVo(in);
        }

        @Override
        public UserVo[] newArray(int size) {
            return new UserVo[size];
        }
    };

    public String getUser_name() {
        return user_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "user_name='" + user_name + '\'' +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_name);
        dest.writeString(password);
    }
}
