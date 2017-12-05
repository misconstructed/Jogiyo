package com.example.misconstructed.jogiyo.VO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by misconstructed on 2017. 11. 28..
 */

public class AlarmVo implements Parcelable{
    private String id;
    private String alarm_name;
    private int range;
    private int alarm_count;
    private String time;
    private String memo;
    private int weekday;
    private String date;
    private int X;
    private int Y;
    private boolean activate;
    private boolean place_alarm;
    private boolean time_alarm;

    protected AlarmVo(Parcel in) {
        id = in.readString();
        alarm_name = in.readString();
        range = in.readInt();
        alarm_count = in.readInt();
        time = in.readString();
        memo = in.readString();
        weekday = in.readInt();
        date = in.readString();
        X = in.readInt();
        Y = in.readInt();
        activate = in.readByte() != 0;
        place_alarm = in.readByte() != 0;
        time_alarm = in.readByte() != 0;
    }

    public static final Creator<AlarmVo> CREATOR = new Creator<AlarmVo>() {
        @Override
        public AlarmVo createFromParcel(Parcel in) {
            return new AlarmVo(in);
        }

        @Override
        public AlarmVo[] newArray(int size) {
            return new AlarmVo[size];
        }
    };

    @Override
    public String toString() {
        return "AlarmVo{" +
                "id='" + id + '\'' +
                ", alarm_name='" + alarm_name + '\'' +
                ", range=" + range +
                ", alarm_count=" + alarm_count +
                ", time='" + time + '\'' +
                ", memo='" + memo + '\'' +
                ", weekday=" + weekday +
                ", date='" + date + '\'' +
                ", X=" + X +
                ", Y=" + Y +
                ", activate=" + activate +
                ", place_alarm=" + place_alarm +
                ", time_alarm=" + time_alarm +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlarm_name() {
        return alarm_name;
    }

    public void setAlarm_name(String alarm_name) {
        this.alarm_name = alarm_name;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getAlarm_count() {
        return alarm_count;
    }

    public void setAlarm_count(int alarm_count) {
        this.alarm_count = alarm_count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public boolean isPlace_alarm() {
        return place_alarm;
    }

    public void setPlace_alarm(boolean place_alarm) {
        this.place_alarm = place_alarm;
    }

    public boolean isTime_alarm() {
        return time_alarm;
    }

    public void setTime_alarm(boolean time_alarm) {
        this.time_alarm = time_alarm;
    }

    public AlarmVo(String id, String alarm_name, int range, int alarm_count, String time, String memo, int weekday, String date, int x, int y, boolean activate, boolean place_alarm, boolean time_alarm) {
        this.id = id;
        this.alarm_name = alarm_name;
        this.range = range;
        this.alarm_count = alarm_count;
        this.time = time;
        this.memo = memo;
        this.weekday = weekday;
        this.date = date;
        X = x;
        Y = y;
        this.activate = activate;
        this.place_alarm = place_alarm;
        this.time_alarm = time_alarm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(alarm_name);
        dest.writeInt(range);
        dest.writeInt(alarm_count);
        dest.writeString(time);
        dest.writeString(memo);
        dest.writeInt(weekday);
        dest.writeString(date);
        dest.writeInt(X);
        dest.writeInt(Y);
        dest.writeByte((byte) (activate ? 1 : 0));
        dest.writeByte((byte) (place_alarm ? 1 : 0));
        dest.writeByte((byte) (time_alarm ? 1 : 0));
    }
}
