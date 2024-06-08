package ro.rolandrosoga.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Helper.CheckBundle;
import ro.rolandrosoga.Helper.HelperFunctions;
import ro.rolandrosoga.Mock.Note;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class PomodoroActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView burgerMenuText, startSessionTextview, pomodoroCurrentSeconds, pomodoroCurrentMinutes, sessionName, readyMessageTextview, pomodoroNewTitle, headerFirstName;
    DrawerLayout pomodoroDrawerLayout;
    NavigationView pomodoroNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout, galleryLinearLayout;
    ImageButton burgerMenuImageButton, toolbarExitPomodoro, settingsButton, pomodoroButton, galleryButton;
    EditText editNewTitle, editNewText;
    String oldTitle, oldText, oldDate, oldCategory1, oldCategory2, oldCategory3;
    Note toEditNote;
    FloatingActionButton calendarView;
    ConstraintLayout startSessionConstraint, pomodoroSettingsConstraint;
    boolean ifSaveNotePressed;
    Context context;
    SQLiteDAO sqLiteDAO;
    int ifSessionStarted = 0;
    int workMinutes, freeMinutes, newTaskID;
    ImageView pomodoroImageViewTimer, headerProfileImage;
    CountDownTimer pomodoroTimer;
    User oldUser;
    Task newTask;
    Calendar newCalendar;
    View profileView;
    CheckBundle checkBundle;
    Bundle editBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        context = PomodoroActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        /*-------------------------Variable Definitions---------------------------------------*/
        burgerMenuText = findViewById(R.id.burger_menu_text);
        pomodoroDrawerLayout = findViewById(R.id.drawer_layout_pomodoro);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        pomodoroNavigationView = findViewById(R.id.sidebar_navigation_view_pomodoro);
        toolbarExitPomodoro = findViewById(R.id.toolbar_exit_pomodoro);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        calendarView = findViewById(R.id.calendarView);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        startSessionConstraint = findViewById(R.id.start_session_constraint);
        startSessionTextview = findViewById(R.id.start_session_textview);
        pomodoroSettingsConstraint = findViewById(R.id.pomodoro_settings_constraint);
        pomodoroCurrentMinutes = findViewById(R.id.pomodoro_current_minutes);
        pomodoroCurrentSeconds = findViewById(R.id.pomodoro_current_seconds);
        sessionName = findViewById(R.id.session_name);
        pomodoroImageViewTimer = findViewById(R.id.pomodoro_imageview_timer);
        pomodoroNewTitle = findViewById(R.id.pomodoro_new_title);
        readyMessageTextview = findViewById(R.id.encouragement_message_textview);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);

        /*---------------------------------Top------------------------------------------------*/
        toolbarExitPomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /*--------------------------------Bottom----------------------------------------------*/
        //mainBottomBar
        burgerMenuText.setText(R.string.notes_text);
        burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroDrawerLayout.open();
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
        /*---------------------------------Middle---------------------------------------------*/
        ActionBarDrawerToggle editNotesToggle = new ActionBarDrawerToggle(this,pomodoroDrawerLayout, R.string.open,R.string.close);
        pomodoroDrawerLayout.addDrawerListener(editNotesToggle);
        editNotesToggle.syncState();
        pomodoroNavigationView.setNavigationItemSelectedListener(this);
        pomodoroNavigationView.bringToFront();
        pomodoroNavigationView.setNavigationItemSelectedListener(this);
        profileView = pomodoroNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);

        //EditNoteActivity implementation logic
        checkResult();
        initializeComponents();
    }

    private void initializeComponents() {
        oldUser = AuthHelper.getInstance().getCurrentUser();
        //Header Initialization
        String firstname = oldUser.getUser_full_name();
        Scanner spaceSearcher = new Scanner(firstname);
        firstname = spaceSearcher.next();
        headerFirstName.setText(firstname);
        if (oldUser.getProfile_image() != null) {
            headerProfileImage.setImageBitmap(HelperFunctions.bytesArrayToBitmap(oldUser.getProfile_image()));
        }
        //
        workMinutes = oldUser.getWork_time();
        freeMinutes = oldUser.getFree_time();
        pomodoroCurrentMinutes.setText(String.valueOf(workMinutes));
        pomodoroCurrentSeconds.setText("00");
        startSessionConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ifSessionStarted) {
                    case (0):
                        newCalendar = new GregorianCalendar();
                        startCounter(workMinutes);
                        startSessionTextview.setText("End Focus Session?");
                        readyMessageTextview.setVisibility(View.INVISIBLE);
                        ifSessionStarted = 1;
                        if (oldUser.isSave_pomodoro_as_task()) {
                            newTask = new Task();
                            newTask.setTaskTitle(pomodoroNewTitle.getText().toString());
                            newTask.setTaskText("");
                            newTask.setCategory1(null);
                            newTask.setCategory2(null);
                            newTask.setCategory3(null);
                            newTask.setTaskProgress("In progress");
                            newTask.setTask_startDate(String.valueOf(newCalendar.getTimeInMillis()));
                            newTask.setTask_endDate("-1");
                            newTaskID = sqLiteDAO.addTaskGetID(newTask);
                            newTask.setID(newTaskID);
                        }
                        break;
                    case (1):
                        newCalendar = new GregorianCalendar();
                        pomodoroTimer.cancel();
                        pomodoroCurrentMinutes.setText(String.valueOf(freeMinutes));
                        pomodoroCurrentSeconds.setText("00");
                        startSessionTextview.setText("Start Leisure Session");
                        readyMessageTextview.setVisibility(View.VISIBLE);
                        sessionName.setText("Leisure");
                        ifSessionStarted = 2;
                        if (oldUser.isSave_pomodoro_as_task()) {
                            newTask = sqLiteDAO.getTaskById(newTaskID);
                            newTask.setTaskTitle(pomodoroNewTitle.getText().toString());
                            newTask.setTaskProgress("Completed");
                            newTask.setTask_endDate(String.valueOf(newCalendar.getTimeInMillis()));
                            newTaskID = sqLiteDAO.updateTask(newTask);
                        }
                        break;
                    case (2):
                        startCounter(freeMinutes);
                        startSessionTextview.setText("End Leisure Session?");
                        readyMessageTextview.setVisibility(View.INVISIBLE);
                        ifSessionStarted = 3;
                        break;
                    case (3):
                        pomodoroTimer.cancel();
                        pomodoroCurrentMinutes.setText(String.valueOf(workMinutes));
                        pomodoroCurrentSeconds.setText("00");
                        startSessionTextview.setText("Start Focus Session");
                        readyMessageTextview.setVisibility(View.VISIBLE);
                        sessionName.setText("Focus");
                        ifSessionStarted = 0;
                        break;
                }
            }
        });
        pomodoroSettingsConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PomodoroActivity.this, SettingsActivity.class));
                finish();
            }
        });
    }

    private void startCounter(long timerInterval) {
        pomodoroTimer = new CountDownTimer(timerInterval * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String minutesNow = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                        pomodoroCurrentMinutes.setText(minutesNow);
                        String secondsNow = String.valueOf( (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60 ));
                        pomodoroCurrentSeconds.setText(secondsNow);
                    }
                });
            }
            @Override
            public void onFinish() {
                startSessionConstraint.performClick();
            }
        }.start();
    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(PomodoroActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(PomodoroActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 43);
        startActivity(newIntent);
        finish();
    }

    public CheckBundle checkResult() {
        Intent editIntent = getIntent();
        editBundle = editIntent.getExtras();
        if (editBundle != null) {
            int requestCode = 0;
            int resultCode = 0;
            int activityNumber = 0;
            try {
                requestCode = editBundle.getInt("requestCode");
                resultCode = editBundle.getInt("resultCode");
                activityNumber = editBundle.getInt("activityNumber");
            } catch (Exception e) {
                Log.e("System.err","Warning 'requestCode' or 'resultCode' missing!");
            }
            checkBundle = new CheckBundle(0, requestCode, resultCode, activityNumber);
            return checkBundle;
        } else {
            Log.e("System.err","Intent Extras Bundle not checked correctly!");
        }
        return new CheckBundle(-1, -1, -1, -1);
    }

    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(PomodoroActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(PomodoroActivity.this, SettingsActivity.class);
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
        if(pomodoroDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            pomodoroDrawerLayout.closeDrawer(GravityCompat.START);
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
            Intent registerIntent = new Intent(PomodoroActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(PomodoroActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(PomodoroActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(PomodoroActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(PomodoroActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(PomodoroActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(PomodoroActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(PomodoroActivity.this, AboutUsActivity.class));
            finish();
        }
        pomodoroDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

