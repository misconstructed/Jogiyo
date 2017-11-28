package com.example.misconstructed.jogiyo.VO;

/**
 * Created by misconstructed on 2017. 11. 28..
 */

public class AlarmVo {
    private String alarm_name;
    private int range;
    private int alarm_count;
    private String time;
    private String memo;
    private int weekday;
    private String date;
    private boolean activate;
    private boolean place_alarm;
    private boolean time_alarm;

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

    @Override
    public String toString() {
        return "AlarmVo{" +
                "alarm_name='" + alarm_name + '\'' +
                ", range=" + range +
                ", alarm_count=" + alarm_count +
                ", time='" + time + '\'' +
                ", memo='" + memo + '\'' +
                ", weekday=" + weekday +
                ", date='" + date + '\'' +
                ", activate=" + activate +
                ", place_alarm=" + place_alarm +
                ", time_alarm=" + time_alarm +
                '}';
    }
}
