package ro.rolandrosoga.Listener;

import android.view.View;

import androidx.cardview.widget.CardView;

import ro.rolandrosoga.Mock.Note;

public interface NoteListener {
    void onClick(Note note);
    void onLongClick(Note note, CardView cardView);

    void onItemClick(View itemView, int position);
}
