package ro.rolandrosoga.FragmentsAndDialogs;

import static com.kizitonwose.calendar.core.ExtensionsKt.daysOfWeek;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;

import net.sqlcipher.database.SQLiteDatabase;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Adapter.MockTaskAdapter;
import ro.rolandrosoga.Calendar.CalendarDayContainer;
import ro.rolandrosoga.Calendar.CalendarMonthContainer;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Listener.TaskListener;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.R;

public class CalendarViewFragment extends Fragment {

    Context context;
    SQLiteDAO sqLiteDAO;
    CalendarView mainCalendarView;
    CalendarDayContainer lastClickedDayContainer;
    int year, month, day;
    List<Task> totalTaskList, chosenTaskList = new ArrayList<>();
    RecyclerView chosenTasksRecycler;
    MockTaskAdapter mockTaskAdapter;
    View rootView;
    ExecutorService calendarService;


    public CalendarViewFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calendar_view, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        if (SQLiteDAO.enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        sqLiteDAO.generateTasksMock();

        initializeUI();
        /*---------------------------------Middle---------------------------------------------*/

        updateTasksRecycler();
        initializeCalendarComponents();
    }

    private void initializeUI() {
        /*-------------------------Variable Definitions---------------------------------------*/
        mainCalendarView = rootView.findViewById(R.id.calendar_calendar_view);
        chosenTasksRecycler = rootView.findViewById(R.id.calendar_recycler);

        /*---------------------------------Top------------------------------------------------*/
        /*--------------------------------Bottom----------------------------------------------*/
    }

    private void initializeCalendarComponents() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        calendarService = Executors.newFixedThreadPool(80, privilegedFactory);

        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(100); // Adjust as needed
        YearMonth endMonth = currentMonth.plusMonths(100); // Adjust as needed
        List<DayOfWeek> daysOfWeek = daysOfWeek();
        mainCalendarView.setup(startMonth, endMonth, daysOfWeek.get(0));
        mainCalendarView.scrollToMonth(currentMonth);

        calendarService.submit(new Runnable() {
            @Override
            public void run() {
                mainCalendarView.setDayBinder(new MonthDayBinder<CalendarDayContainer>() {
                    @Override
                    public CalendarDayContainer create(View view) {
                        return new CalendarDayContainer(view);
                    }
                    @Override
                    public void bind(CalendarDayContainer container, CalendarDay data) {
                        container.textView.setText(String.valueOf(data.getDate().getDayOfMonth()));
                        if (data.getPosition() == DayPosition.MonthDate) {
                            container.textView.setTextColor(Color.WHITE);
                        } else {
                            container.textView.setTextColor(Color.parseColor("#BFBFBF"));
                        }
                        LinearLayout titlesContainer = getView().findViewById(R.id.calendar_week_days_container);

                        for(int counter = 0; counter < 7; counter++) {
                            View childView = titlesContainer.getChildAt(counter);
                            if (childView instanceof TextView) {
                                TextView textView = (TextView) childView;
                                String dayOfWeek = String.valueOf(daysOfWeek.get(counter));
                                String title = dayOfWeek.substring(0, 3); // Assuming daysOfWeek contains full names
                                textView.setText(title);
                            }
                        }
                        container.textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lastClickedDayContainer == null) {
                                    container.textView.setBackgroundResource(R.drawable.button_circle);
                                    lastClickedDayContainer = container;
                                    year = data.getDate().getYear();
                                    month = data.getDate().getMonthValue();
                                    day = data.getDate().getDayOfMonth();
                                    calculateChosenDayTasks(year, month-1, day);
                                } else {
                                    lastClickedDayContainer.textView.setBackgroundResource(0);
                                    container.textView.setBackgroundResource(R.drawable.button_circle);
                                    lastClickedDayContainer = container;
                                    year = data.getDate().getYear();
                                    month = data.getDate(). getMonthValue();
                                    day = data.getDate().getDayOfMonth();
                                    calculateChosenDayTasks(year, month-1, day);
                                }
                            }
                        });
                    }
                });
                mainCalendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<CalendarMonthContainer>() {
                    @Override
                    public CalendarMonthContainer create(View view) {
                        return new CalendarMonthContainer(view);
                    }

                    @Override
                    public void bind(CalendarMonthContainer container, CalendarMonth data) {
                        Object tag = container.monthTitleContainer.getTag();
                        // Check for null or mismatched yearMonth for efficient binding
                        if (tag == null || !tag.equals(data.getYearMonth())) {
                            container.monthTitleContainer.setTag(data.getYearMonth());
                            int year = data.getYearMonth().getYear();
                            int month = data.getYearMonth().getMonthValue();

                            container.monthTitleContainer.setText(getMonthNameByInt(month, year));
                        }
                    }
                });
            }
        });
    }

    private String getMonthNameByInt(int whichMonth, int whichYear) {
        String monthYear = "";
        switch (whichMonth) {
            case(1):
                monthYear = "January";
                break;
            case(2):
                monthYear = "February";
                break;
            case(3):
                monthYear = "March";
                break;
            case(4):
                monthYear = "April";
                break;
            case(5):
                monthYear = "May";
                break;
            case(6):
                monthYear = "June";
                break;
            case(7):
                monthYear = "July";
                break;
            case(8):
                monthYear = "August";
                break;
            case(9):
                monthYear = "September";
                break;
            case(10):
                monthYear = "October";
                break;
            case(11):
                monthYear = "November";
                break;
            case(12):
                monthYear = "December";
                break;
        }
        return monthYear + " " + whichYear;
    }

    private void calculateChosenDayTasks(int year, int month, int day) {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        calendarService = Executors.newCachedThreadPool(privilegedFactory);

        totalTaskList = sqLiteDAO.getAllTasks();
        chosenTaskList = new ArrayList<>();
        Calendar chosenCalendar = new GregorianCalendar();
        chosenCalendar.set(Calendar.YEAR, year);
        chosenCalendar.set(Calendar.MONTH, month);
        chosenCalendar.set(Calendar.DAY_OF_MONTH, day);
        chosenCalendar.set(Calendar.HOUR_OF_DAY, 0);
        chosenCalendar.set(Calendar.MINUTE, 1);
        chosenCalendar.set(Calendar.SECOND, 0);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTimeInMillis(chosenCalendar.getTimeInMillis());
        endCalendar.add(Calendar.DAY_OF_MONTH, 1);

        calendarService.submit(new Runnable() {
            @Override
            public void run() {
                for (Task chosenTask:totalTaskList) {
                    long startDate = Long.parseLong(chosenTask.getTask_startDate());
                    long endDate = Long.parseLong(chosenTask.getTask_endDate());
                    if (!chosenTask.getTaskProgress().equals("Completed")) {
                        if ( startDate < endCalendar.getTimeInMillis() && endDate >= endCalendar.getTimeInMillis()) {
                            chosenTaskList.add(chosenTask);
                        } else if ( (startDate >= chosenCalendar.getTimeInMillis() && startDate < endCalendar.getTimeInMillis())
                                || (endDate >= chosenCalendar.getTimeInMillis() && endDate < endCalendar.getTimeInMillis())) {
                            chosenTaskList.add(chosenTask);
                        }
                    }
                }
                mockTaskAdapter.setSearchedTasksList(chosenTaskList);
            }
        });

    }

    private void updateTasksRecycler() {
        mockTaskAdapter = new MockTaskAdapter(context, chosenTaskList, taskListener, 41);
        chosenTasksRecycler.setAdapter(mockTaskAdapter);
        chosenTasksRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
    }
    private final TaskListener taskListener = new TaskListener() {
        @Override
        public void onClick(Task task) {}
        @Override
        public void onLongClick(Task task, CardView cardView) {}
        @Override
        public void onItemClick(View itemView, int position) {}
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        calendarService.shutdown();
        if (mockTaskAdapter != null) {
            mockTaskAdapter.onDestroy();
        }
    }
}
