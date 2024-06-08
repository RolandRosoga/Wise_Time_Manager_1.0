package ro.rolandrosoga.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Adapter.CalendarTabsAdapter;
import ro.rolandrosoga.Adapter.MockTaskAdapter;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Helper.HelperFunctions;
import ro.rolandrosoga.Listener.TaskListener;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView burgerMenuText, toolBarTitle, headerFirstName;
    DrawerLayout calendarDrawerLayout;
    NavigationView calendarNavigationView;
    LinearLayout toolbar, burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout, galleryLinearLayout;
    ImageButton burgerMenuImageButton, toolbarExitCalendar, settingsButton, pomodoroButton, galleryButton;
    ImageView headerProfileImage;
    MockTaskAdapter mockTaskAdapter;
    Context context;
    SQLiteDAO sqLiteDAO;
    ConstraintLayout addNewTaskButton;
    ViewPager2 calendarPager;
    CalendarTabsAdapter calendarTabsAdapter;
    User currentUser;
    View profileView;
    private ExecutorService calendarService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        context = CalendarActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        initializeUI();
        /*---------------------------------Calendar---------------------------------------------*/
        calendarTabsAdapter = new CalendarTabsAdapter(this);
        calendarPager.setAdapter(calendarTabsAdapter);
        calendarPager.setUserInputEnabled(false);
        calendarPager.setAdapter(calendarTabsAdapter);

        /*---------------------------------Middle---------------------------------------------*/
        ActionBarDrawerToggle calendarToggle = new ActionBarDrawerToggle(this,calendarDrawerLayout, R.string.open,R.string.close);
        calendarDrawerLayout.addDrawerListener(calendarToggle);
        calendarToggle.syncState();
        calendarNavigationView.setNavigationItemSelectedListener(this);
        calendarNavigationView.bringToFront();
        calendarNavigationView.setNavigationItemSelectedListener(this);
        profileView = calendarNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);

        initializeComponents();
    }

    private void initializeUI() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        calendarService = Executors.newFixedThreadPool(80, privilegedFactory);

        /*-------------------------Variable Definitions---------------------------------------*/
        toolbar = findViewById(R.id.calendar_toolbar);
        burgerMenuText = findViewById(R.id.burger_menu_text);
        calendarDrawerLayout = findViewById(R.id.drawer_layout_calendar);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        calendarNavigationView = findViewById(R.id.sidebar_navigation_view_calendar);
        toolbarExitCalendar = findViewById(R.id.toolbar_exit_calendar);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        toolBarTitle = findViewById(R.id.textview_toolbar_title);
        addNewTaskButton = findViewById(R.id.add_new_task_constraint);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        calendarPager = findViewById(R.id.calendar_fragment_container_view);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);

        calendarService.submit(new Runnable() {
            @Override
            public void run() {
                /*---------------------------------Top------------------------------------------------*/
                toolbarExitCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                /*--------------------------------Bottom----------------------------------------------*/
                //mainBottomBar
                burgerMenuText.setText(R.string.tasks_text);
                burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendarDrawerLayout.open();
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
                addNewTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addTask();
                    }
                });
            }
        });
    }

    private void initializeComponents() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        calendarService = Executors.newCachedThreadPool(privilegedFactory);

        //Header Initialization
        calendarService.submit(new Runnable() {
            @Override
            public void run() {
                currentUser = AuthHelper.getInstance().getCurrentUser();
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

    private void onGalleryClick() {
        Intent newIntent = new Intent(CalendarActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(CalendarActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 41);
        startActivity(newIntent);
        finish();
    }

    private void addTask() {
        Intent addNewTaskIntent = new Intent(CalendarActivity.this, AddTaskActivity.class);
        addNewTaskIntent.putExtra("requestCode",2217);
        addNewTaskIntent.putExtra("resultCode", Activity.RESULT_OK);
        addNewTaskIntent.putExtra("activityNumber", 41);
        startActivity(addNewTaskIntent);
        finish();
    }

    public static void KeyboardOpenCheck(final Activity currentActivity) {    final View activityRootView = currentActivity.findViewById(android.R.id.content);    activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new     ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(rect);
                int heightRoot = activityRootView.getRootView().getHeight();
                int heightDiff = heightRoot - rect.bottom;
                if (heightDiff > dpToPx(currentActivity, 200)) {
                    //Keyboard is open
                }else if (heightDiff < dpToPx(currentActivity, 200)) {
                    //Keyboard is close
                }
            }
        });
    }

    private static float dpToPx(Context currentContext, int densityIndependentPixels) {
        DisplayMetrics displayMetrics =  currentContext.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,     densityIndependentPixels, displayMetrics);
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(CalendarActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
        finish();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if(calendarDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            calendarDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!AuthHelper.getInstance().getLoggedIn()) {
            FirebaseAuth.getInstance().signOut();
            Intent registerIntent = new Intent(CalendarActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(CalendarActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(CalendarActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(CalendarActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(CalendarActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(CalendarActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(CalendarActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(CalendarActivity.this, AboutUsActivity.class));
            finish();
        }
        calendarDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
    protected void onDestroy() {
        super.onDestroy();
        calendarService.shutdown();
        if (mockTaskAdapter != null) {
            mockTaskAdapter.onDestroy();
        }
    }
}
