package com.example.misconstructed.jogiyo.VO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by misconstructed on 2017. 11. 28..
 */

public class UserVo implements Parcelable{
    private String name;
    private String id;
    private String password;

    public UserVo() {
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public UserVo(String name, String id, String password) {
        this.name = name;
        this.id = id;
        this.password = password;
    }

    protected UserVo(Parcel in) {
        name = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(password);
    }
}
