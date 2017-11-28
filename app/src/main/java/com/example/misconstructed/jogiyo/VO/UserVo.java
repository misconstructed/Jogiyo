package com.example.misconstructed.jogiyo.VO;

/**
 * Created by misconstructed on 2017. 11. 28..
 */

public class UserVo {
    private String user_name;
    private String email;
    private String password;

    public UserVo(){
        this.user_name = null;
        this.email = null;
        this.password = null;
    }
    public UserVo(String user_name, String email, String password){
        this.user_name = user_name;
        this.email = email;
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
