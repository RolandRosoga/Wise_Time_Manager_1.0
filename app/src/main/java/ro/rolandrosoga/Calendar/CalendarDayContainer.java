package ro.rolandrosoga.Calendar;

import android.view.View;
import android.widget.TextView;

import com.kizitonwose.calendar.view.ViewContainer;

import ro.rolandrosoga.R;

public class CalendarDayContainer extends ViewContainer {

    public TextView textView;

    public CalendarDayContainer(View view) {
        super(view);
        textView = (TextView) view.findViewById(R.id.calendarDayText);

    }
}

