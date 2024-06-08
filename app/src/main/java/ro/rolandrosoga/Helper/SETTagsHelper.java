package ro.rolandrosoga.Helper;

import java.util.ArrayList;

public class SETTagsHelper {

    private static SETTagsHelper instance;
    private ArrayList<Integer> setTagList;

    public SETTagsHelper() {
        setTagList = new ArrayList<>();
    }

    public static synchronized SETTagsHelper getInstance() {
        if (instance == null) {
            instance = new SETTagsHelper();
        }
        return instance;
    }
    public synchronized void addSETTag(Integer newSETTag) {
        setTagList.add(newSETTag);
    }
    public synchronized void removeSETTag(Integer oldSETTag) {
        setTagList.remove(oldSETTag);
    }
    public synchronized int getSETTagCount() {return setTagList.size();}
    public synchronized ArrayList<Integer> getSetTagList() {
        return setTagList;
    }
    public synchronized void setSetTagList(ArrayList<Integer> setTagList) {this.setTagList = setTagList;}
    public synchronized void setSETTagsToZero() {
        setTagList = new ArrayList<>();
    }
}
