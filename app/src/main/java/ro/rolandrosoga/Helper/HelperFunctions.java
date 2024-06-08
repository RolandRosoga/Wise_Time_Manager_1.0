package ro.rolandrosoga.Helper;

import static android.text.TextUtils.substring;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.ext.SdkExtensions;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream;

import ro.rolandrosoga.Mock.MediaPair;

public class HelperFunctions {

    public static boolean checkForNumber(String stringToCheck) {
        try {
            Double.parseDouble(stringToCheck);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static Bitmap bytesArrayToBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static byte[] bitmapToBytesArray(Bitmap bitmap){
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        return blob.toByteArray();
    }

    public static MediaPair giveIndexGetSubstring(String fullString, int currentIndex) {
        int lastIndex = fullString.indexOf("|", currentIndex);
        return new MediaPair(substring(fullString, currentIndex, lastIndex), lastIndex);
    }

    public static int giveTaskIDGetNotificationID(int taskID, boolean isStart){
        if (isStart) {
            return taskID * 2;
        } else {
            return (taskID * 2) + 1;
        }
    }

    public static boolean isPhotoPickerAvailable() {
        if (Build.VERSION.SDK_INT >= 33) {
            return true;
        } else if(SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2) {
            return true;
        } else {
            return false;
        }
    }
}
