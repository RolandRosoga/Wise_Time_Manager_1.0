package ro.rolandrosoga.Activity;

import static ro.rolandrosoga.Helper.HelperFunctions.checkForNumber;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Scanner;

import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Helper.HelperFunctions;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout settingsDrawerLayout;
    NavigationView settingsNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout, toolbar, galleryLinearLayout;
    ImageButton burgerMenuImageButton, toolbarExitSettings, settingsButton, pomodoroButton, buttonSaveSettings, galleryButton;
    FloatingActionButton calendarView;
    ConstraintLayout constraintResetTags, constraintDeleteTags, constraintDeleteNotes, constraintDeleteTasks, constraintDeleteImages, constraintResetAllToDefault, constraintEditPersonalDetails, setPomodoroSave;
    TextView burgerMenuText, textUsername, textPassword, textFullName, textEmail, textPhoneNumber, textGoogleSignIn, saveDisabledPomodoro, saveEnabledPomodoro, headerFirstName;
    Context context;
    SQLiteDAO sqLiteDAO;
    boolean saveTaskEnabled = false;
    EditText setWorkTimeTextView, setFreeTimeTextView;
    User currentUser;
    View profileView;
    ImageView headerProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = SettingsActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        /*-------------------------Variable Definitions---------------------------------------*/
        toolbar = findViewById(R.id.settings_toolbar);
        burgerMenuText = findViewById(R.id.burger_menu_text);
        settingsDrawerLayout = findViewById(R.id.drawer_layout_settings);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        settingsNavigationView = findViewById(R.id.sidebar_navigation_view_settings);
        toolbarExitSettings = findViewById(R.id.toolbar_exit_settings);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        calendarView = findViewById(R.id.calendarView);
        //Settings Specific Variables
        constraintResetTags = findViewById(R.id.constraint_reset_tags);
        constraintDeleteTags = findViewById(R.id.constraint_delete_all_tags);
        constraintDeleteNotes = findViewById(R.id.constraint_delete_all_notes);
        constraintDeleteTasks = findViewById(R.id.constraint_delete_all_tasks);
        constraintResetAllToDefault = findViewById(R.id.constraint_reset_all_to_default);
        //
        textUsername = findViewById(R.id.text_username);
        textPassword = findViewById(R.id.text_password);
        textFullName = findViewById(R.id.text_full_name);
        textEmail = findViewById(R.id.text_email);
        textPhoneNumber = findViewById(R.id.text_phone_number);
        textGoogleSignIn = findViewById(R.id.text_google_sign_in);
        constraintEditPersonalDetails = findViewById(R.id.constraint_edit_personal_details);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        setPomodoroSave = findViewById(R.id.constraint_set_pomodoro_task);
        saveDisabledPomodoro = findViewById(R.id.settings_pomodoro_save_task_false);
        saveEnabledPomodoro = findViewById(R.id.settings_pomodoro_save_task_true);
        buttonSaveSettings = findViewById(R.id.toolbar_save_settings);
        setWorkTimeTextView = findViewById(R.id.settings_pomodoro_work_time);
        setFreeTimeTextView = findViewById(R.id.settings_pomodoro_free_time);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);
        constraintDeleteImages = findViewById(R.id.constraint_delete_all_pictures);

        //
        /*---------------------------------Top------------------------------------------------*/
        toolbarExitSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsSaved();
            }
        });
        /*--------------------------------Bottom----------------------------------------------*/
        //mainBottomBar
        burgerMenuText.setText(R.string.notes_text);
        burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDrawerLayout.open();
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
        ActionBarDrawerToggle editNotesToggle = new ActionBarDrawerToggle(this,settingsDrawerLayout, R.string.open,R.string.close);
        settingsDrawerLayout.addDrawerListener(editNotesToggle);
        editNotesToggle.syncState();
        settingsNavigationView.setNavigationItemSelectedListener(this);
        settingsNavigationView.bringToFront();
        settingsNavigationView.setNavigationItemSelectedListener(this);
        profileView = settingsNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);

        //Settings Specific Bindings
        constraintResetTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDAO.deleteAllTags();
                sqLiteDAO.generateDefaultTags();
            }
        });
        constraintDeleteTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDAO.deleteAllTags();
            }
        });
        constraintDeleteNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDAO.deleteAllNotes();
            }
        });
        constraintDeleteTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDAO.deleteAllTasks();
            }
        });
        constraintDeleteImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDAO.deleteAllMedia();
            }
        });
        constraintResetAllToDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDAO.deleteAllTags();
                sqLiteDAO.generateDefaultTags();
                sqLiteDAO.deleteAllNotes();
                sqLiteDAO.deleteAllTasks();
            }
        });
        constraintEditPersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openYourProfileActivity = new Intent(SettingsActivity.this, YourProfileActivity.class);
                startActivity(openYourProfileActivity);
                finish();
            }
        });
        setPomodoroSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveTaskEnabled) {
                    saveEnabledPomodoro.setVisibility(View.INVISIBLE);
                    saveDisabledPomodoro.setVisibility(View.VISIBLE);
                    saveTaskEnabled = false;
                } else {
                    saveDisabledPomodoro.setVisibility(View.INVISIBLE);
                    saveEnabledPomodoro.setVisibility(View.VISIBLE);
                    saveTaskEnabled = true;
                }
            }
        });
        //
        initializeComponents();
        //
    }

    private void initializeComponents() {
        currentUser = AuthHelper.getInstance().getCurrentUser();
        //Header Initialization
        String firstname = currentUser.getUser_full_name();
        Scanner spaceSearcher = new Scanner(firstname);
        firstname = spaceSearcher.next();
        headerFirstName.setText(firstname);
        if (currentUser.getProfile_image() != null) {
            headerProfileImage.setImageBitmap(HelperFunctions.bytesArrayToBitmap(currentUser.getProfile_image()));
        }
        //
        textUsername.setText(currentUser.getUserUsername());
        String password = "";
        for (int i=0; i < currentUser.getUserPassword().length(); i++) {
            password = password + "*";
        }
        textPassword.setText(password);
        textFullName.setText(currentUser.getUser_full_name());
        String email = currentUser.getUser_email();
        if (email.length() >= 26) {
            email.substring(0,26);
            textEmail.setText(email);
        } else {
            textEmail.setText(email);
        }
        if (currentUser.getUser_phone_number() != null) {
            textPhoneNumber.setText(currentUser.getUser_phone_number());
        }
        if (currentUser.getUser_google_sign_in()) {
            textGoogleSignIn.setText("Sign In enabled");
        } else {
            textGoogleSignIn.setText("Sign In disabled");
        }
        if (currentUser.getWork_time() == -1) {
            currentUser.setWork_time(25);
            setWorkTimeTextView.setText(String.valueOf(currentUser.getWork_time()));
        } else {
            setWorkTimeTextView.setText(String.valueOf(currentUser.getWork_time()));
        }
        if (currentUser.getFree_time() == -1) {
            currentUser.setFree_time(5);
            setFreeTimeTextView.setText(String.valueOf(currentUser.getFree_time()));
        } else {
            setFreeTimeTextView.setText(String.valueOf(currentUser.getFree_time()));
        }
        if (currentUser.isSave_pomodoro_as_task()) {
            saveDisabledPomodoro.setVisibility(View.INVISIBLE);
            saveEnabledPomodoro.setVisibility(View.VISIBLE);
            saveTaskEnabled = true;
        } else {
            saveEnabledPomodoro.setVisibility(View.INVISIBLE);
            saveDisabledPomodoro.setVisibility(View.VISIBLE);
            saveTaskEnabled = false;
        }

    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(SettingsActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void settingsSaved() {
        String workText = setWorkTimeTextView.getText().toString();
        if (checkForNumber(workText)) {
            if (Integer.parseInt(workText) < 0) {
                alertSecondsAndMinutesCannotBeZero();
            } else if (Integer.parseInt(workText) > 960) {
                alertMinutesCannotBeGreaterThan960();
            } else {
                currentUser.setWork_time(Integer.parseInt(workText));
                currentUser.setSave_pomodoro_as_task(saveTaskEnabled);
                sqLiteDAO.updateUser(currentUser);
            }
        } else {
            alertStringNotNumber();
        }
        String freeText = setFreeTimeTextView.getText().toString();
        if (checkForNumber(freeText)) {
            if (Integer.parseInt(freeText) < 0 ) {
                alertSecondsAndMinutesCannotBeZero();
            } else if (Integer.parseInt(freeText) >= 60) {
                alertSecondsGreaterThanPossible();
            } else {
                currentUser.setFree_time(Integer.parseInt(freeText));
                currentUser.setSave_pomodoro_as_task(saveTaskEnabled);
                sqLiteDAO.updateUser(currentUser);
            }
        } else {
            alertStringNotNumber();
        }
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(SettingsActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 47);
        startActivity(newIntent);
        finish();
    }

    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(SettingsActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(SettingsActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
        finish();
    }

    public void finish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.finish();
    }

    private void alertStringNotNumber() {
        String title = "<font color=#6EBEF0> Cannot Save </font>";
        String okButton = "<font color=#6EBEF0> OK </font>";
        AlertDialog.Builder warningBuilder =
                new AlertDialog.Builder(this)
                        .setTitle(Html.fromHtml(title))
                        .setMessage("The Focus Time and The Free Time have to be numbers.")
                        .setPositiveButton(Html.fromHtml(okButton), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        AlertDialog alertDialog = warningBuilder.show();
    }

    private void alertSecondsAndMinutesCannotBeZero() {
        String title = "<font color=#6EBEF0> Cannot Save </font>";
        String okButton = "<font color=#6EBEF0> OK </font>";
        AlertDialog.Builder warningBuilder =
                new AlertDialog.Builder(this)
                        .setTitle(Html.fromHtml(title))
                        .setMessage("Seconds and minutes cannot be less than 0.")
                        .setPositiveButton(Html.fromHtml(okButton), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        AlertDialog alertDialog = warningBuilder.show();
    }

    private void alertMinutesCannotBeGreaterThan960() {
        String title = "<font color=#6EBEF0> Cannot Save </font>";
        String okButton = "<font color=#6EBEF0> OK </font>";
        AlertDialog.Builder warningBuilder =
                new AlertDialog.Builder(this)
                        .setTitle(Html.fromHtml(title))
                        .setMessage("Minutes cannot be greater than 960.")
                        .setPositiveButton(Html.fromHtml(okButton), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        AlertDialog alertDialog = warningBuilder.show();
    }

    private void alertSecondsGreaterThanPossible() {
        String title = "<font color=#6EBEF0> Cannot Save </font>";
        String okButton = "<font color=#6EBEF0> OK </font>";
        AlertDialog.Builder warningBuilder =
                new AlertDialog.Builder(this)
                        .setTitle(Html.fromHtml(title))
                        .setMessage("Seconds cannot be greater than 59.")
                        .setPositiveButton(Html.fromHtml(okButton), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        AlertDialog alertDialog = warningBuilder.show();
    }

    @Override
    public void onBackPressed() {
        if(settingsDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            settingsDrawerLayout.closeDrawer(GravityCompat.START);
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
            Intent registerIntent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(SettingsActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(SettingsActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(SettingsActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(SettingsActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(SettingsActivity.this, AboutUsActivity.class));
            finish();
        }
        settingsDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



}
