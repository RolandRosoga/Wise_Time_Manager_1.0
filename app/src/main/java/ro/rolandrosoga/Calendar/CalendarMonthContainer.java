package ro.rolandrosoga.Calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kizitonwose.calendar.view.ViewContainer;

import ro.rolandrosoga.R;

public class CalendarMonthContainer extends ViewContainer {

    public TextView monthTitleContainer;

    public CalendarMonthContainer(@NonNull View view) {
        super(view);
        monthTitleContainer = view.findViewById(R.id.calendar_header_textview);
    }

    public TextView getTitlesContainer() {
        return monthTitleContainer;
    }

    public void setTitlesContainer(TextView titlesContainer) {
        this.monthTitleContainer = titlesContainer;
    }


}
