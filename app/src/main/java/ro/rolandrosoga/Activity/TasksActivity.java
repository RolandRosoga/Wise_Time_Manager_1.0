package ro.rolandrosoga.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Adapter.MockTaskAdapter;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Helper.CheckBundle;
import ro.rolandrosoga.Helper.HelperFunctions;
import ro.rolandrosoga.Listener.TaskListener;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class TasksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout tasksDrawerLayout;
    NavigationView tasksNavigationView;
    LinearLayout searchViewLayout, burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout, galleryLinearLayout, tasksToolbar, toolbarLayout;
    ImageButton burgerMenuImageButton, backButtonTasks, settingsButton, pomodoroButton, galleryButton, searchButtonTasks;
    TextView burgerMenuText, headerFirstName;
    ConstraintLayout addNewTaskButton;
    MockTaskAdapter tasksVerticalAdapter;
    List<Task> tasksList = new ArrayList<>();
    FloatingActionButton calendarView;
    ImageView headerProfileImage;
    RecyclerView taskRecyclerView;
    Context context;
    SQLiteDAO sqLiteDAO;
    View profileView;
    User currentUser;
    SearchView tasksSearchView;
    List<Task> searchedTasksList = new ArrayList<>();
    Handler newHandler = new Handler();
    private boolean isSearchViewOpen = false;
    private ExecutorService tasksService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        context = TasksActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        sqLiteDAO.generateTasksMock();

        initializeUI();
        /*---------------------------------Middle---------------------------------------------*/
        //
        ActionBarDrawerToggle tasksActionToggle = new ActionBarDrawerToggle(this,tasksDrawerLayout, R.string.open,R.string.close);
        tasksDrawerLayout.addDrawerListener(tasksActionToggle);
        tasksActionToggle.syncState();
        tasksNavigationView.setNavigationItemSelectedListener(this);
        tasksNavigationView.bringToFront();
        tasksNavigationView.setNavigationItemSelectedListener(this);
        profileView = tasksNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);
        //
        initializeComponents();
    }

    private void initializeComponents() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        tasksService = Executors.newCachedThreadPool(privilegedFactory);

        currentUser = AuthHelper.getInstance().getCurrentUser();
        //Header Initialization
        tasksService.submit(new Runnable() {
            @Override
            public void run() {
                String firstname = currentUser.getUser_full_name();
                Scanner spaceSearcher = new Scanner(firstname);
                firstname = spaceSearcher.next();
                headerFirstName.setText(firstname);
                if (currentUser.getProfile_image() != null) {
                    headerProfileImage.setImageBitmap(HelperFunctions.bytesArrayToBitmap(currentUser.getProfile_image()));
                }
                tasksVerticalAdapter = new MockTaskAdapter(TasksActivity.this, tasksList, taskListener, 37);
                //
            }
        });
    }

    private void initializeUI() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        tasksService = Executors.newFixedThreadPool(100, privilegedFactory);

        /*-------------------------Variable Definitions---------------------------------------*/
        //Top Variable Definition
        addNewTaskButton = findViewById(R.id.add_new_task_constraint);
        backButtonTasks = findViewById(R.id.back_button_Tasks);
        //Drawer Variable Definition
        tasksDrawerLayout = findViewById(R.id.drawer_layout_tasks);
        tasksNavigationView = findViewById(R.id.sidebar_navigation_view_tasks);
        //Bottom Variable Definition
        burgerMenuText = findViewById(R.id.burger_menu_text);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        calendarView = findViewById(R.id.calendarView);
        //Middle Variable Definition
        //
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        taskRecyclerView = findViewById(R.id.task_recycler_view);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);
        tasksToolbar = findViewById(R.id.tasks_toolbar);
        //
        searchButtonTasks = findViewById(R.id.search_button_tasks);
        tasksSearchView = findViewById(R.id.tasks_searchView);
        toolbarLayout = findViewById(R.id.layout_toolbar);
        searchViewLayout = findViewById(R.id.layout_searchview);
        //

        tasksService.submit(new Runnable() {
            @Override
            public void run() {
                /*---------------------------------Top------------------------------------------------*/
                backButtonTasks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackButtonPressed();
                    }
                });
                tasksSearchView.clearFocus();
                tasksSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        onSearchViewChanged(newText);
                        return true;
                    }
                });
                tasksSearchView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isSearchViewOpen) {
                            calendarView.setVisibility(View.GONE);
                            tasksSearchView.setIconified(false);
                            tasksSearchView.setIconifiedByDefault(false);
                            isSearchViewOpen = true;
                        }
                    }
                });
                searchButtonTasks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isSearchViewOpen) {
                            calendarView.setVisibility(View.GONE);
                            toolbarLayout.setVisibility(View.INVISIBLE);
                            searchViewLayout.setVisibility(View.VISIBLE);
                            tasksSearchView.setIconified(false);
                            tasksSearchView.setIconifiedByDefault(false);
                            isSearchViewOpen = true;
                        } else {
                            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                            isSearchViewOpen = false;
                            searchViewLayout.setVisibility(View.INVISIBLE);
                            toolbarLayout.setVisibility(View.VISIBLE);
                            makeCalendarViewVisible();
                        }
                    }
                });
                /*--------------------------------Bottom----------------------------------------------*/
                //mainBottomBar
                burgerMenuText.setText(R.string.tasks_text);
                burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tasksDrawerLayout.open();
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
                addNewTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addTask();
                    }
                });
            }
        });
    }

    private void onSearchViewChanged(String newText) {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        tasksService = Executors.newFixedThreadPool(80, privilegedFactory);

        searchedTasksList = new ArrayList<>();
        tasksList = SQLiteDAO.getInstance(TasksActivity.this).getAllTasks();
        tasksService.submit(new Runnable() {
            @Override
            public void run() {
                for (Task task:tasksList) {
                    if (task.getTaskTitle() != null && task.getTaskTitle().toLowerCase().contains(newText.toLowerCase())) {
                        searchedTasksList.add(task);
                    } else if (task.getTaskText().toLowerCase().contains(newText.toLowerCase())) {
                        searchedTasksList.add(task);
                    } else if (task.getTaskProgress().toLowerCase().contains(newText.toLowerCase())) {
                        searchedTasksList.add(task);
                    } else if (task.getCategory1() != null && task.getCategory1().toLowerCase().contains(newText.toLowerCase())) {
                        searchedTasksList.add(task);
                    } else if (task.getCategory2() != null && task.getCategory2().toLowerCase().contains(newText.toLowerCase())) {
                        searchedTasksList.add(task);
                    } else if (task.getCategory3() != null && task.getCategory3().toLowerCase().contains(newText.toLowerCase())) {
                        searchedTasksList.add(task);
                    } else if (task.getTask_startDate() != null && task.getTask_startDate().toLowerCase().contains(newText.toLowerCase())) {
                        searchedTasksList.add(task);
                    } else if (task.getTask_endDate() != null && task.getTask_endDate().toLowerCase().contains(newText.toLowerCase())) {
                        searchedTasksList.add(task);
                    }
                }
                if (!searchedTasksList.isEmpty()) {
                    tasksVerticalAdapter.setSearchedTasksList(searchedTasksList);
                }
            }
        });
    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(TasksActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(TasksActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 37);
        startActivity(newIntent);
        finish();
    }

    public void makeCalendarViewVisible() {
        newHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                calendarView.setVisibility(View.VISIBLE);
            }
        }, 50);
    }

    public void updateTasksRecycler() {
        tasksList = sqLiteDAO.getAllTasks();
        Collections.reverse(tasksList);
        tasksVerticalAdapter = new MockTaskAdapter(this, tasksList, taskListener, 37);
        taskRecyclerView.setAdapter(tasksVerticalAdapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        taskRecyclerView.setHasFixedSize(true);
    }

    private void addTask() {
        Intent addNewTaskIntent = new Intent(TasksActivity.this, AddTaskActivity.class);
        addNewTaskIntent.putExtra("requestCode",2217);
        addNewTaskIntent.putExtra("resultCode", Activity.RESULT_OK);
        addNewTaskIntent.putExtra("activityNumber", 37);
        startActivity(addNewTaskIntent);
        finish();
    }

    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(TasksActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(TasksActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTasksRecycler();
    }

    private void onBackButtonPressed() {
        CheckBundle checkBundle = checkResult();
        if (isSearchViewOpen) {
            searchViewLayout.setVisibility(View.INVISIBLE);
            toolbarLayout.setVisibility(View.VISIBLE);
            makeCalendarViewVisible();
            isSearchViewOpen = false;
        } else if (checkBundle.getActivityNumber() != -1) {
            onBackPressed();
        }
    }

    public CheckBundle checkResult() {
        Intent editIntent = getIntent();
        Bundle editBundle = editIntent.getExtras();
        CheckBundle checkBundle;
        if (editBundle != null) {
            int requestCode = 0;
            int resultCode = 0;
            int activityNumber = 0;
            try {
                requestCode = editBundle.getInt("requestCode");
                resultCode = editBundle.getInt("resultCode");
                activityNumber = editBundle.getInt("activityNumber");
            } catch (Exception e) {
                Log.e("System.err", "Warning 'requestCode' or 'resultCode' missing!");
            }
            if (requestCode == 1217 || requestCode == 1218) {
                if (resultCode == Activity.RESULT_OK) {
                    int noteID = -1;
                    try {
                        noteID = editBundle.getInt("NoteID");
                    } catch (Exception e) {
                        Log.e("System.err", "Warning 'NoteID' missing!");
                    }
                    checkBundle = new CheckBundle(noteID, requestCode, resultCode, activityNumber);
                    return checkBundle;
                }
            } else if (requestCode == 2217 || requestCode == 2218) {
                if (resultCode == Activity.RESULT_OK) {
                    int tagID = -1;
                    try {
                        tagID = editBundle.getInt("TagID");
                    } catch (Exception e) {
                        Log.e("System.err", "Warning 'TagID' missing!");
                    }
                    checkBundle = new CheckBundle(tagID, requestCode, resultCode, activityNumber);
                    return checkBundle;
                }
            }
        } else {
            Log.e("System.err", "Intent Extras Bundle not checked correctly!");
        }
        return new CheckBundle(-1, -1, -1, -1);
    }

    @Override
    public void onBackPressed() {
        if(tasksDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            tasksDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (isSearchViewOpen) {
            searchViewLayout.setVisibility(View.INVISIBLE);
            toolbarLayout.setVisibility(View.VISIBLE);
            makeCalendarViewVisible();
            isSearchViewOpen = false;
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
            Intent registerIntent = new Intent(TasksActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(TasksActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(TasksActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(TasksActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(TasksActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(TasksActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(TasksActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(TasksActivity.this, AboutUsActivity.class));
            finish();
        }
        tasksDrawerLayout.closeDrawer(GravityCompat.START);
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
        tasksService.shutdown();
        if (tasksVerticalAdapter != null) {
            tasksVerticalAdapter.onDestroy();
        }
    }
}
