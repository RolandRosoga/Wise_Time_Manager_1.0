package ro.rolandrosoga.Mock;

import java.io.Serializable;

public class Media implements Serializable {

    private int media_id;
    private byte[] media_blob;

    public Media() {

    }

    public Media(int media_id, byte[] media_blob) {
        this.media_id = media_id;
        this.media_blob = media_blob;

    }

    public int getID() {
        return media_id;
    }

    public void setID(int media_id) {
        this.media_id = media_id;
    }

    public byte[] getMedia_blob() {
        return media_blob;
    }

    public void setMedia_blob(byte[] media_blob) {
        this.media_blob = media_blob;
    }
    
}
