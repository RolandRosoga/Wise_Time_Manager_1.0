package ro.rolandrosoga.Mock;

import java.io.Serializable;

public class User implements Serializable {

    private int user_id;
    private String user_username;
    private String user_password;
    private String user_full_name;
    private String user_email;
    private String user_phone_number;
    private boolean user_google_sign_in;

    private int work_time;
    private int free_time;
    private boolean save_pomodoro_as_task;
    private byte[] profile_image;

    public User() {

    }

    public User(int user_id, String user_username, String user_password, String user_full_name, String user_email, String user_phone_number, boolean user_google_sign_in, int work_time, int free_time, boolean save_pomodoro_as_task, byte[] profile_image) {
        this.user_id = user_id;
        this.user_username = user_username;
        this.user_password = user_password;
        this.user_full_name = user_full_name;
        this.user_email = user_email;
        this.user_phone_number = user_phone_number;
        this.user_google_sign_in = user_google_sign_in;
        this.work_time = work_time;
        this.free_time = free_time;
        this.save_pomodoro_as_task = save_pomodoro_as_task;
        this.profile_image = profile_image;
    }

    public int getID() {
        return user_id;
    }

    public void setID(int user_id) {
        this.user_id = user_id;
    }

    public String getUserUsername() {
        return user_username;
    }

    public void setUserUsername(String user_username) {
        this.user_username = user_username;
    }

    public String getUserPassword() {
        return user_password;
    }

    public void setUserPassword(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public boolean getUser_google_sign_in() {
        return user_google_sign_in;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }

    public void setUser_google_sign_in(boolean user_google_sign_in) {
        this.user_google_sign_in = user_google_sign_in;
    }

    public int getWork_time() {
        return work_time;
    }

    public void setWork_time(int work_time) {
        this.work_time = work_time;
    }

    public int getFree_time() {
        return free_time;
    }

    public void setFree_time(int free_time) {
        this.free_time = free_time;
    }

    public boolean isSave_pomodoro_as_task() {
        return save_pomodoro_as_task;
    }

    public void setSave_pomodoro_as_task(boolean save_pomodoro_as_task) {
        this.save_pomodoro_as_task = save_pomodoro_as_task;
    }

    public byte[] getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(byte[] profile_image) {
        this.profile_image = profile_image;
    }
}
