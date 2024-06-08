package ro.rolandrosoga.FragmentsAndDialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Mock.Tag;
import ro.rolandrosoga.R;

public class ColorPickerDialogFragment extends DialogFragment {
    ImageView buttonColor, colorWheel;
    ImageButton confirmColorPickerButton;
    Bitmap colorBitmap;
    int hexColorValue;
    int pixelColor;
    Bundle currentBundle;
    Context context;
    SQLiteDAO sqLiteDAO;
    ExecutorService colorPickerService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.dialog_fragment_color_picker, container, false);
        currentBundle = getArguments();
        context = mainView.getContext();
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        bind(mainView);
        return mainView;
    }

    //noinspection AndroidLintClickableViewAccessibility
    private void bind(View mainView) {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        colorPickerService = Executors.newFixedThreadPool(80, privilegedFactory);
        
        confirmColorPickerButton = mainView.findViewById(R.id.confirm_color_picker_button);
        colorWheel = mainView.findViewById(R.id.color_wheel_imageview);
        buttonColor = mainView.findViewById(R.id.button_color);

        setInitialButtonColor(mainView);

        colorWheel.setDrawingCacheEnabled(true);
        colorWheel.buildDrawingCache(true);

        colorPickerService.submit(new Runnable() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void run() {
                confirmColorPickerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitColor(mainView);
                    }
                });
                colorWheel.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_MOVE) {
                            colorBitmap = colorWheel.getDrawingCache();
                            pixelColor = colorBitmap.getPixel((int)event.getX(), (int)event.getY());
                            try {
                                if (pixelColor != 0) {
                                    hexColorValue = Color.parseColor("#" + Integer.toHexString(pixelColor));
                                    buttonColor.setColorFilter(hexColorValue, PorterDuff.Mode.SRC_IN);
                                }
                            } catch (Exception ignored) {}
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void setInitialButtonColor(View mainView) {
        if (currentBundle != null) {
            String tagID = currentBundle.getString("tagID");
            Tag tag = sqLiteDAO.getTagById(Integer.parseInt(tagID));
            int hexColor = Color.parseColor("#" + tag.getTagColor());
            buttonColor.setColorFilter(hexColor, PorterDuff.Mode.SRC_IN);
        } else {
            Log.e("System.err", "No TagID given! Cannot perform saving operation!");
        }
    }

    private void submitColor(View mainView) {
        if (currentBundle != null) {
            try {
                String tagID = currentBundle.getString("tagID");
                Tag tag = sqLiteDAO.getTagById(Integer.parseInt(tagID));
                tag.setTagColor(Integer.toHexString(pixelColor));
                sqLiteDAO.updateTag(tag);
            } catch (Exception ignored) {}
        } else {
            Log.e("System.err", "No TagID given! Cannot perform saving operation!");
        }
        dismiss();
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        colorPickerService.shutdown();
    }
}
