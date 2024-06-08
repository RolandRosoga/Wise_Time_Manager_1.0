package ro.rolandrosoga.Mock;

import java.io.Serializable;

public class Tag implements Serializable {

    private int tag_id;
    private String tag_category;
    private String tag_color;

    public Tag() {

    }

    public Tag(int tag_id, String tag_category, String tag_color) {
        this.tag_id = tag_id;
        this.tag_category = tag_category;
        this.tag_color = tag_color;
    }

    public int getTagID() {
        return tag_id;
    }

    public void setTagID(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getTagCategory() {
        return tag_category;
    }

    public void setTagCategory(String tag_category) {
        this.tag_category = tag_category;
    }

    public String getTagColor() {
        return tag_color;
    }

    public void setTagColor(String tag_color) {
        this.tag_color = tag_color;
    }

}