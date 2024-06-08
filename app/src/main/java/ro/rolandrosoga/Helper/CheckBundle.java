package ro.rolandrosoga.Helper;

import java.io.Serializable;

public class CheckBundle implements Serializable {

    private int object_ID;
    private int requestCode;
    private int resultCode;
    private int activityNumber;

    public CheckBundle() {}

    public CheckBundle(int object_ID, int requestCode, int resultCode, int activityNumber) {
        this.object_ID = object_ID;
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.activityNumber = activityNumber;
    }

    public int getObject_ID() {
        return object_ID;
    }

    public void setObject_ID(int object_ID) {
        this.object_ID = object_ID;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(int activityNumber) {
        this.activityNumber = activityNumber;
    }
}
