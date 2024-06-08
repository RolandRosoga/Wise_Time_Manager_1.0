package ro.rolandrosoga.Mock;

import java.io.Serializable;

public class MediaPair implements Serializable {

    private String currentString;
    private int lastIndex;

    public MediaPair() {

    }

    public String getCurrentString() {
        return currentString;
    }

    public void setCurrentString(String currentString) {
        this.currentString = currentString;
    }

    public MediaPair(String currentString, int lastIndex) {
        this.currentString = currentString;
        this.lastIndex = lastIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }
}

