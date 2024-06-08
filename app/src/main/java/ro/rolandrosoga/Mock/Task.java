package ro.rolandrosoga.Mock;

import java.io.Serializable;

public class Task implements Serializable {
    private int task_id;
    private String task_title;
    private String task_text;
    private String task_progress;
    private String category1;
    private String category2;
    private String category3;
    private String task_startDate;
    private String task_endDate;
    private String mediaBitmaps;

    public Task() {

    }

    public Task(int task_id, String task_title, String task_text, String task_progress, String category1, String category2, String category3, String task_startDate, String task_endDate, String mediaBitmaps) {
        this.task_id = task_id;
        this.task_title = task_title;
        this.task_text = task_text;
        this.task_progress = task_progress;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.task_startDate = task_startDate;
        this.task_endDate = task_endDate;
        this.mediaBitmaps = mediaBitmaps;
    }

    public int getID() {
        return task_id;
    }

    public void setID(int task_id) {
        this.task_id = task_id;
    }

    public String getTaskTitle() {
        return task_title;
    }

    public void setTaskTitle(String task_title) {
        this.task_title = task_title;
    }

    public String getTaskText() {
        return task_text;
    }

    public void setTaskText(String task_text) {
        this.task_text = task_text;
    }

    public String getTaskProgress() {
        return task_progress;
    }

    public void setTaskProgress(String task_progress) {
        this.task_progress = task_progress;
    }

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getTask_startDate() {
        return task_startDate;
    }

    public void setTask_startDate(String task_startDate) {
        this.task_startDate = task_startDate;
    }

    public String getTask_endDate() {
        return task_endDate;
    }

    public void setTask_endDate(String task_endDate) {
        this.task_endDate = task_endDate;
    }

    public String getMediaBitmaps() {
        return mediaBitmaps;
    }

    public void setMediaBitmaps(String mediaBitmaps) {
        this.mediaBitmaps = mediaBitmaps;
    }
}
