package ro.rolandrosoga.Listener;

import android.view.View;

import androidx.cardview.widget.CardView;

import ro.rolandrosoga.Mock.Tag;

public interface TagListener {

    void onClick(Tag note);
    void onLongClick(Tag tag, CardView cardView);

    void onItemClick(View itemView, int position);
}
