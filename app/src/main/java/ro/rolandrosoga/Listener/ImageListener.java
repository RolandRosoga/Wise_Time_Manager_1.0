package ro.rolandrosoga.Listener;

import android.net.Uri;
import android.view.View;

import androidx.cardview.widget.CardView;

public interface ImageListener {
    void onClick(Uri uri);
    void onLongClick(Uri uri, CardView cardView);

    void onItemClick(View itemView, int position);
}
