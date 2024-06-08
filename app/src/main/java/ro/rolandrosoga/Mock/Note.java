package ro.rolandrosoga.Mock;


import java.io.Serializable;


public class Note implements Serializable {

    private int note_id;
    private String note_title;
    private String note_text;
    private String change_date;
    private String category1;
    private String category2;
    private String category3;
    private String mediaBitmaps;

    public Note() {

    }

    public Note(int note_id, String note_title, String note_text, String change_date, String category1, String category2, String category3, String mediaBitmaps) {
        this.note_id = note_id;
        this.note_title = note_title;
        this.note_text = note_text;
        this.change_date = change_date;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.mediaBitmaps = mediaBitmaps;
    }

    public int getID() {
        return note_id;
    }

    public void setID(int note_id) {
        this.note_id = note_id;
    }

    public String getNoteTitle() {
        return note_title;
    }

    public void setNoteTitle(String note_title) {
        this.note_title = note_title;
    }

    public String getNoteText() {
        return note_text;
    }

    public void setNoteText(String note_text) {
        this.note_text = note_text;
    }

    public String getNoteDate() {
        return change_date;
    }

    public void setNoteDate(String change_date) {
        this.change_date = change_date;
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

    public String getMediaBitmaps() {
        return mediaBitmaps;
    }

    public void setMediaBitmaps(String mediaBitmaps) {
        this.mediaBitmaps = mediaBitmaps;
    }
}
