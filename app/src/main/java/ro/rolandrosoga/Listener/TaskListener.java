package ro.rolandrosoga.Listener;

import android.view.View;

import androidx.cardview.widget.CardView;

import ro.rolandrosoga.Mock.Task;

public interface TaskListener {
    void onClick(Task task);
    void onLongClick(Task task, CardView cardView);

    void onItemClick(View itemView, int position);
}
