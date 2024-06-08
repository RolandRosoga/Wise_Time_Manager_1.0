package ro.rolandrosoga.Activity;


import static com.kizitonwose.calendar.core.ExtensionsKt.daysOfWeek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;

import net.sqlcipher.database.SQLiteDatabase;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Adapter.MockNoteAdapter;
import ro.rolandrosoga.Adapter.MockTaskAdapter;
import ro.rolandrosoga.Calendar.CalendarDayContainer;
import ro.rolandrosoga.Calendar.CalendarMonthContainer;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Helper.HelperFunctions;
import ro.rolandrosoga.Listener.NoteListener;
import ro.rolandrosoga.Listener.TaskListener;
import ro.rolandrosoga.Mock.Note;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mainDrawerLayout;
    NavigationView mainNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout , notesButton, galleryLinearLayout;
    ImageButton burgerMenuImageButton, addNewNote, settingsButton, pomodoroButton, galleryButton;
    ImageView headerProfileImage;
    TextView greetingName, currentDate, notesButtonText, burgerMenuText, tasksForDayTextView, headerFirstName;
    OffsetTime offsetTime;
    String greeting, dateOfToday;
    com.kizitonwose.calendar.view.CalendarView mainCalendarView;
    Calendar calendar;
    MockNoteAdapter notesVerticalAdapter;
    MockTaskAdapter mockTaskAdapter;
    List<Note> mainNotesList = new ArrayList<>();
    List<Task> totalTaskList, chosenTaskList = new ArrayList<>();
    RecyclerView mainNotesRecycler, chosenTasksRecycler;
    FloatingActionButton calendarView;
    Context context;
    SQLiteDAO sqLiteDAO;
    CalendarDayContainer lastClickedContainer;
    int year, month, day;
    User currentUser;
    View profileView;
    static final boolean  shouldAllowBackPressed = false;
    private ExecutorService homeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!AuthHelper.getInstance().getLoggedIn()) {
            FirebaseAuth.getInstance().signOut();
            Intent registerIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        } else {
            currentUser = AuthHelper.getInstance().getCurrentUser();
        }

        context = MainActivity.this;
        if (SQLiteDAO.enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);
        //

        mainNotesRecycler = findViewById(R.id.main_notes_recycler);
        updateNotesRecycler();

        initializeUI();

        /*---------------------------------Middle---------------------------------------------*/
        ActionBarDrawerToggle mainActionToggle = new ActionBarDrawerToggle(this, mainDrawerLayout, R.string.open, R.string.close);
        mainDrawerLayout.addDrawerListener(mainActionToggle);
        mainActionToggle.syncState();
        mainNavigationView.setNavigationItemSelectedListener(this);
        mainNavigationView.bringToFront();
        mainNavigationView.setNavigationItemSelectedListener(this);
        profileView = mainNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);
        //
        initializeComponents();

        homeService.submit(new Runnable() {
            @Override
            public void run() {
                //
                calendar = Calendar.getInstance();
                //IMPLEMENTED ON SECOND THREAD
                Calendar newCalendar = Calendar.getInstance();
                year =  newCalendar.get(Calendar.YEAR);
                month = newCalendar.get(Calendar.MONTH);
                day = newCalendar.get(Calendar.DAY_OF_MONTH);
                calculateChosenDayTasks(year, month-1, day);
            }
        });
        //
    }

    private void initializeUI() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        homeService = Executors.newFixedThreadPool(80, privilegedFactory);

        /*-------------------------Variable Definitions---------------------------------------*/
        //Top Variable Definitions
        greetingName = findViewById(R.id.greetingAddress);
        currentDate = findViewById(R.id.currentDate);
        //
        burgerMenuText = findViewById(R.id.burger_menu_text);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        //
        notesButton = findViewById(R.id.notesButton);
        notesButtonText = findViewById(R.id.Notes);
        addNewNote = findViewById(R.id.add_new_note_main_button);
        //
        mainDrawerLayout = findViewById(R.id.drawer_layout_main);
        mainNavigationView = findViewById(R.id.sidebar_navigation_view_main);
        //
        mainCalendarView = findViewById(R.id.main_calendar_view);
        //
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        calendarView = findViewById(R.id.calendarView);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        //
        chosenTasksRecycler = findViewById(R.id.chosen_tasks_recyclerview);
        tasksForDayTextView = findViewById(R.id.main_task_for_the_day);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);
        //

        homeService.submit(new Runnable() {
            @Override
            public void run() {
                /*---------------------------------Top------------------------------------------------*/
                //Change the color on the "Notes >" TextView(Button)
                String notesText = "<font color=#ffffff> Notes</font> <font color=#6ebef0> > </font>";
                notesButtonText.setText(Html.fromHtml(notesText, Html.FROM_HTML_MODE_LEGACY));
                // Switch to the "Notes" activity
                notesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, NotesActivity.class));
                        finish();
                    }
                });
                addNewNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNote();
                    }
                });
                /*--------------------------------Bottom----------------------------------------------*/
                //mainBottomBar
                burgerMenuText.setText(R.string.home);
                burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainDrawerLayout.open();
                    }
                });
                settingsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSettingsClick();
                    }
                });
                settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSettingsClick();
                    }
                });
                calendarView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCalendarActivityClick();
                    }
                });
                pomodoroLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPomodoroClick();
                    }
                });
                pomodoroButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPomodoroClick();
                    }
                });
                galleryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGalleryClick();
                    }
                });
                galleryLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGalleryClick();
                    }
                });
                initializeCalendarComponents();
                //
            }
        });
        updateTasksRecycler();
        //
        greetingName.setText(getGreeting());
        currentDate.setText(getDate());
    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(MainActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void initializeComponents() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        homeService = Executors.newCachedThreadPool(privilegedFactory);

        //Header Initialization
        homeService.submit(new Runnable() {
            @Override
            public void run() {
                String firstname = currentUser.getUser_full_name();
                Scanner spaceSearcher = new Scanner(firstname);
                firstname = spaceSearcher.next();
                headerFirstName.setText(firstname);
                if (currentUser.getProfile_image() != null) {
                    headerProfileImage.setImageBitmap(HelperFunctions.bytesArrayToBitmap(currentUser.getProfile_image()));
                }
                //
            }
        });
    }

    private void initializeCalendarComponents() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        homeService = Executors.newCachedThreadPool(privilegedFactory);
        //

        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(100); // Adjust as needed
        YearMonth endMonth = currentMonth.plusMonths(100); // Adjust as needed
        List<DayOfWeek> daysOfWeek = daysOfWeek();
        mainCalendarView.setup(startMonth, endMonth, daysOfWeek.get(0));
        mainCalendarView.scrollToMonth(currentMonth);

        homeService.submit(new Runnable() {
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
                        LinearLayout titlesContainer = findViewById(R.id.calendar_week_days_container);

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
                                if (lastClickedContainer == null) {
                                    container.textView.setBackgroundResource(R.drawable.button_circle);
                                    lastClickedContainer = container;
                                    year = data.getDate().getYear();
                                    month = data.getDate().getMonthValue();
                                    day = data.getDate().getDayOfMonth();
                                    calculateChosenDayTasks(year, month-1, day);
                                } else {
                                    lastClickedContainer.textView.setBackgroundResource(0);
                                    container.textView.setBackgroundResource(R.drawable.button_circle);
                                    lastClickedContainer = container;
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
        homeService = Executors.newCachedThreadPool(privilegedFactory);

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

        homeService.submit(new Runnable() {
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
        mockTaskAdapter = new MockTaskAdapter(this, chosenTaskList, taskListener, 33);
        chosenTasksRecycler.setAdapter(mockTaskAdapter);
        chosenTasksRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(MainActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 33);
        startActivity(newIntent);
        finish();
    }

    private void addNote() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        homeService = Executors.newCachedThreadPool(privilegedFactory);

        homeService.submit(new Runnable() {
            @Override
            public void run() {
                Intent addNewNoteIntent = new Intent(MainActivity.this, AddNoteActivity.class);
                addNewNoteIntent.putExtra("requestCode",1217);
                addNewNoteIntent.putExtra("resultCode", Activity.RESULT_OK);
                addNewNoteIntent.putExtra("activityNumber", 33);
                startActivity(addNewNoteIntent);
                finish();
            }
        });
    }

    public void updateNotesRecycler() {
        mainNotesList = sqLiteDAO.getAllNotes();
        Collections.reverse(mainNotesList);
        notesVerticalAdapter = new MockNoteAdapter(MainActivity.this, mainNotesList, mainNotesListener, 33);
        mainNotesRecycler.setAdapter(notesVerticalAdapter);
        mainNotesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mainNotesRecycler.setHasFixedSize(true);
    }

    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(MainActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotesRecycler();
    }

    @Override
    public void onBackPressed() {
        if(mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (shouldAllowBackPressed) {
            super.onBackPressed();
            finish();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!AuthHelper.getInstance().getLoggedIn()) {
            FirebaseAuth.getInstance().signOut();
            Intent registerIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(MainActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(MainActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(MainActivity.this, YourProfileActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_log_out) {
            userSignedOut();
        } else if (itemId == R.id.sidebar_share) {
            Intent shareAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.radurosoga.ro"));
            startActivity(shareAppIntent);
            Toast.makeText(this,"Thank you for sharing this App!", Toast.LENGTH_SHORT).show();
            finish();
        } else if (itemId == R.id.sidebar_rate_us) {
            Toast.makeText(this,"Thank you for rating Us!", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.about_us) {
            startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
            finish();
        }
        mainDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    protected String getGreeting() {
        String firstname = currentUser.getUser_full_name();
        Scanner spaceSearcher = new Scanner(firstname);
        firstname = spaceSearcher.next();
        String timeOfDay = null;
        int hours = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            offsetTime = OffsetTime.now();
            hours = offsetTime.getHour();
        } else {
            offsetTime = null;
        }

        if(offsetTime != null) {
            if (hours >= 4 && hours < 12)
                timeOfDay = "morning";

            else if (hours >= 12 && hours < 18)
                timeOfDay = "afternoon";

            else if (hours >= 18 && hours <= 23)
                timeOfDay = "evening";

            else if (hours >= 0 && hours < 4)
                timeOfDay = "night";
            greeting = "Good " + timeOfDay + ", " + firstname + "!";
            return greeting;
        } else {
            System.out.println("error at getGreeting(), SDK version is old");
            return "error";
        }
    }

    protected String getDate() {
        calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateOfToday = LocalDate.now().getDayOfWeek().name() + ", " + LocalDate.now().getMonth() + " " + LocalDate.now().getDayOfMonth() + ", " + LocalDate.now().getYear();
            return dateOfToday;
        } else {
            System.out.println("error at getDate(), SDK version is old");
            return "error";
        }
    }

    private final NoteListener mainNotesListener = new NoteListener() {
        @Override
        public void onClick(Note note) {}
        @Override
        public void onLongClick(Note note, CardView cardView) {}
        @Override
        public void onItemClick(View itemView, int position) {}
    };
    private final TaskListener taskListener = new TaskListener() {
        @Override
        public void onClick(Task task) {}
        @Override
        public void onLongClick(Task task, CardView cardView) {}
        @Override
        public void onItemClick(View itemView, int position) {}
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeService.shutdown();
        if (notesVerticalAdapter != null) {
            notesVerticalAdapter.onDestroy();
        }
        if (mockTaskAdapter != null) {
            mockTaskAdapter.onDestroy();
        }
    }
}
