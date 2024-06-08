package ro.rolandrosoga.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Adapter.MockImageAdapter;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Helper.CheckBundle;
import ro.rolandrosoga.Helper.HelperFunctions;
import ro.rolandrosoga.Listener.ImageListener;
import ro.rolandrosoga.Mock.Media;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.Notification.AlarmReceiver;
import ro.rolandrosoga.R;

public class AddTaskActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView burgerMenuText, setStartTimeTextview, setStartDateTextview, setEndTimeTextview, setEndDateTextview, progressTextview, headerFirstName;
    DrawerLayout addTasksDrawerLayout;
    NavigationView addTasksNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout, galleryLinearLayout, toolbar;
    ImageButton buttonAddFromGallery, burgerMenuImageButton, toolbarSaveAddTask, toolbarExitAddTask, settingsButton, buttonSetTags, buttonCloseTaskMenu, pomodoroButton, galleryButton;
    EditText addNewTitle, addNewText;
    int initialActivity;
    String oldMediaBitmap;
    Task toAddTask = new Task();
    int toAddTaskID = -1;
    FloatingActionButton buttonOpenTaskMenu, calendarView;
    ConstraintLayout buttonTaskMenu, setStartTimeConstraint, setStartDateConstraint, setEndTimeConstraint, setEndDateConstraint;;
    private boolean ifSaveTaskPressed, firstTimeClicked = false;
    private Context context;
    private SQLiteDAO sqLiteDAO;
    int chosenStartHour, chosenStartMinute, chosenEndHour, chosenEndMinute, chosenStartDay, chosenStartMonth, chosenStartYear, chosenEndDay, chosenEndMonth, chosenEndYear = Integer.MAX_VALUE;
    ImageView checkBoxExceeded, checkBoxInProgress, checkBoxCompleted, headerProfileImage;
    User currentUser;
    View profileView;
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    MockImageAdapter imageHorizontalAdapter;
    RecyclerView imagesRecyclerView;
    List<Uri> uriList = new ArrayList<>();
    List<Bitmap> bitmapList = new ArrayList<>();
    List<Media> mediaList = new ArrayList<>();
    private ExecutorService addTaskService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);

        context = AddTaskActivity.this;
        if (SQLiteDAO.enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);


        initializeUI();
        /*---------------------------------Middle---------------------------------------------*/
        ActionBarDrawerToggle addTasksToggle = new ActionBarDrawerToggle(this,addTasksDrawerLayout, R.string.open,R.string.close);
        addTasksDrawerLayout.addDrawerListener(addTasksToggle);
        addTasksToggle.syncState();
        addTasksNavigationView.setNavigationItemSelectedListener(this);
        addTasksNavigationView.bringToFront();
        addTasksNavigationView.setNavigationItemSelectedListener(this);
        profileView = addTasksNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);

        pickMultipleMedia =
                registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(4), uris -> {
                    if (!uris.isEmpty()) {
                        uriList = uris;
                        List<Bitmap> currentBitmapList = uriListToBitmapList(uriList);
                        bitmapArrayToMediaArray(currentBitmapList);
                        bitmapList.addAll(currentBitmapList);
                        //
                        updateImagesRecycler();
                    } else {
                        Toast.makeText(getApplicationContext(), "No image selected!", Toast.LENGTH_SHORT).show();
                    }
                });
        checkSenderActivity();
        initializeComponents();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeUI() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        addTaskService = Executors.newFixedThreadPool(80, privilegedFactory);
        
        /*-------------------------Variable Definitions---------------------------------------*/
        toolbar = findViewById(R.id.add_tasks_toolbar);
        burgerMenuText = findViewById(R.id.burger_menu_text);
        addTasksDrawerLayout = findViewById(R.id.drawer_layout_add_tasks);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        addTasksNavigationView = findViewById(R.id.sidebar_navigation_view_add_tasks);
        toolbarSaveAddTask = findViewById(R.id.toolbar_save_add_task);
        toolbarExitAddTask = findViewById(R.id.toolbar_exit_add_task);
        addNewTitle = findViewById(R.id.add_new_title);
        addNewText = findViewById(R.id.add_new_text);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        buttonOpenTaskMenu = findViewById(R.id.button_open_task_menu);
        buttonTaskMenu = findViewById(R.id.button_task_menu);
        buttonCloseTaskMenu = findViewById(R.id.button_close_task_menu);
        buttonSetTags = findViewById(R.id.button_set_tags);
        calendarView = findViewById(R.id.calendarView);
        setStartTimeConstraint = findViewById(R.id.set_start_time_constraint);
        setStartDateConstraint = findViewById(R.id.set_start_date_constraint);
        setEndTimeConstraint = findViewById(R.id.set_end_time_constraint);
        setEndDateConstraint = findViewById(R.id.set_end_date_constraint);
        setStartTimeTextview = findViewById(R.id.set_start_time_textview);
        setStartDateTextview = findViewById(R.id.set_start_date_textview);
        setEndTimeTextview = findViewById(R.id.set_end_time_textview);
        setEndDateTextview = findViewById(R.id.set_end_date_textview);
        checkBoxExceeded = findViewById(R.id.task_checkbox_exceeded);
        checkBoxInProgress = findViewById(R.id.task_checkbox_in_progress);
        checkBoxCompleted = findViewById(R.id.task_checkbox_completed);
        progressTextview = findViewById(R.id.progress_textview);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);
        imagesRecyclerView = findViewById(R.id.images_recyclerview);
        buttonAddFromGallery = findViewById(R.id.button_save_from_gallery);

        addTaskService.submit(new Runnable() {
            @Override
            public void run() {
                /*---------------------------------Top------------------------------------------------*/
                toolbarSaveAddTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTaskSaved();
                    }
                });
                toolbarExitAddTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onExitAddTask();
                    }
                });
                /*--------------------------------Bottom----------------------------------------------*/
                //mainBottomBar
                burgerMenuText.setText(R.string.tasks_text);
                burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTasksDrawerLayout.open();
                    }
                });
                calendarView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCalendarActivityClick();
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
                buttonOpenTaskMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonOpenTaskMenu.setVisibility(View.INVISIBLE);
                        buttonTaskMenu.setVisibility(View.VISIBLE);
                    }
                });
                buttonCloseTaskMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonTaskMenu.setVisibility(View.INVISIBLE);
                        buttonOpenTaskMenu.setVisibility(View.VISIBLE);
                    }
                });
                buttonSetTags.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSetTags();
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
                buttonAddFromGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (HelperFunctions.isPhotoPickerAvailable()) {
                            addFromGallery();
                        }
                    }
                });
                setStartTimeConstraint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTimeChooser(0);
                    }
                });
                setStartDateConstraint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDateChooser(0);
                    }
                });
                setEndTimeConstraint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTimeChooser(1);
                    }
                });
                setEndDateConstraint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDateChooser(1);
                    }
                });
                checkBoxExceeded.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBoxExceeded.setVisibility(View.GONE);
                        checkBoxCompleted.setVisibility(View.GONE);
                        checkBoxInProgress.setVisibility(View.VISIBLE);
                        int color = Color.parseColor("#" + "F0EE6E");
                        checkBoxInProgress.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        progressTextview.setText("In progress");
                        progressTextview.setTextAppearance(R.style.yellow);
                        toAddTask.setTaskProgress("In progress");
                    }
                });
                checkBoxInProgress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!firstTimeClicked) {
                            firstTimeClicked = true;
                            checkBoxExceeded.setVisibility(View.GONE);
                            checkBoxCompleted.setVisibility(View.GONE);
                            checkBoxInProgress.setVisibility(View.VISIBLE);
                            int color = Color.parseColor("#" + "F0EE6E");
                            checkBoxInProgress.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                            progressTextview.setText("In progress");
                            progressTextview.setTextAppearance(R.style.yellow);
                            toAddTask.setTaskProgress("In progress");
                        } else {
                            checkBoxExceeded.setVisibility(View.GONE);
                            checkBoxInProgress.setVisibility(View.GONE);
                            checkBoxCompleted.setVisibility(View.VISIBLE);
                            int color = Color.parseColor("#" + "36894D");
                            checkBoxCompleted.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                            progressTextview.setText("Completed");
                            progressTextview.setTextAppearance(R.style.green);
                            toAddTask.setTaskProgress("Completed");
                        }

                    }
                });
                checkBoxCompleted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBoxInProgress.setVisibility(View.GONE);
                        checkBoxCompleted.setVisibility(View.GONE);
                        checkBoxExceeded.setVisibility(View.VISIBLE);
                        int color = Color.parseColor("#" + "992923");
                        checkBoxExceeded.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        progressTextview.setText("Exceeded");
                        progressTextview.setTextAppearance(R.style.red);
                        toAddTask.setTaskProgress("Exceeded");
                    }
                });
                addNewTitle.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    public boolean onTouch(View v, MotionEvent event) {
                        if (addNewTitle.hasFocus()) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            switch (event.getAction() & MotionEvent.ACTION_MASK){
                                case MotionEvent.ACTION_SCROLL:
                                    v.getParent().requestDisallowInterceptTouchEvent(false);
                                    return true;
                            }
                        }
                        return false;
                    }
                });
                addNewText.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    public boolean onTouch(View v, MotionEvent event) {
                        if (addNewText.hasFocus()) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            switch (event.getAction() & MotionEvent.ACTION_MASK){
                                case MotionEvent.ACTION_SCROLL:
                                    v.getParent().requestDisallowInterceptTouchEvent(false);
                                    return true;
                            }
                        }
                        return false;
                    }
                });
            }
        });
    }

    private void bitmapArrayToMediaArray(List<Bitmap> currentBitmapList) {
        for (Bitmap bitmap:currentBitmapList) {
            Media newMedia = new Media();
            newMedia.setMedia_blob(HelperFunctions.bitmapToBytesArray(bitmap));
            int mediaID = sqLiteDAO.addMediaGetID(newMedia);
            newMedia.setID(mediaID);
            mediaList.add(newMedia);
        }
    }

    private List<Bitmap> uriListToBitmapList(List<Uri> currentUriList) {
        List<Bitmap> currentBitmapList = new ArrayList<>();
        for (Uri currentUri:currentUriList) {
            currentBitmapList.add(getBitmapByUri(currentUri));
        }
        return currentBitmapList;
    }

    private Bitmap getBitmapByUri(Uri imageUri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
    private void addFromGallery() {
        pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build());
    }
    public void updateImagesRecycler() {
        imageHorizontalAdapter = new MockImageAdapter(AddTaskActivity.this, bitmapList, mediaList, imageListener);
        imagesRecyclerView.setAdapter(imageHorizontalAdapter);
        imagesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.HORIZONTAL));
        imagesRecyclerView.setHasFixedSize(true);
    }

    private void initializeComponents() {
        currentUser = AuthHelper.getInstance().getCurrentUser();
        if (currentUser.getUser_full_name() != null) {
            //Header Initialization
            String firstname = currentUser.getUser_full_name();
            Scanner spaceSearcher = new Scanner(firstname);
            firstname = spaceSearcher.next();
            headerFirstName.setText(firstname);
        }
        if (currentUser.getProfile_image() != null) {
            headerProfileImage.setImageBitmap(HelperFunctions.bytesArrayToBitmap(currentUser.getProfile_image()));
        }
        //
    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(AddTaskActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(AddTaskActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 48);
        startActivity(newIntent);
        finish();
    }

    private void setTimeChooser(int whichTime) {
        TimePickerDialog selectedTime = null;
        switch (whichTime) {
            case (0):
                TimePickerDialog.OnTimeSetListener onStartTime = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        chosenStartHour = hourOfDay;
                        chosenStartMinute = minute;
                        String placeHolderString = String.format(Locale.getDefault(), "%02d:%02d", chosenStartHour, chosenStartMinute);
                        setStartTimeTextview.setText(placeHolderString);
                    }
                };
                selectedTime = new TimePickerDialog(AddTaskActivity.this, R.style.TimeChooser, onStartTime, chosenStartHour, chosenStartMinute, true);
                break;
            case (1):
                TimePickerDialog.OnTimeSetListener onEndTime = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        chosenEndHour = hourOfDay;
                        chosenEndMinute = minute;
                        String placeHolderString = String.format(Locale.getDefault(), "%02d:%02d", chosenEndHour, chosenEndMinute);
                        setEndTimeTextview.setText(placeHolderString);
                    }
                };
                selectedTime = new TimePickerDialog(AddTaskActivity.this, R.style.TimeChooser, onEndTime, chosenEndHour, chosenEndMinute, true);
                break;
        }
        switch (whichTime) {
            case (0):
                selectedTime.setTitle("Select a Start Time");
                break;
            case (1):
                selectedTime.setTitle("Select a End Time");
                break;
        }
        selectedTime.show();
    }

    private void setDateChooser(int whichDate) {
        Calendar newCalendar = new GregorianCalendar();
        Date newDate = new Date();
        newCalendar.setTime(newDate);

        DatePickerDialog selectedDate = null;
        switch (whichDate) {
            case (0):
                chosenStartDay = newCalendar.get(Calendar.DAY_OF_MONTH);
                chosenStartMonth = newCalendar.get(Calendar.MONTH);
                chosenStartYear = newCalendar.get(Calendar.YEAR);
                selectedDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        chosenStartDay = dayOfMonth;
                        chosenStartMonth = month;
                        chosenStartYear = year;
                        String placeholderDate = chosenStartDay + "/" + chosenStartMonth + "/" + chosenStartYear;
                        setStartDateTextview.setText(placeholderDate);
                    }
                }, chosenStartYear, chosenStartMonth, chosenStartDay);
                break;
            case (1):
                chosenEndDay = newCalendar.get(Calendar.DAY_OF_MONTH);
                chosenEndMonth = newCalendar.get(Calendar.MONTH);
                chosenEndYear = newCalendar.get(Calendar.YEAR);
                selectedDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        chosenEndDay = dayOfMonth;
                        chosenEndMonth = month;
                        chosenEndYear = year;
                        String placeholderDate = chosenEndDay + "/" + chosenEndMonth + "/" + chosenEndYear;
                        setEndDateTextview.setText(placeholderDate);
                    }
                }, chosenEndYear, chosenEndMonth, chosenEndDay);
                break;
        }
        selectedDate.show();
    }

    private void onTaskSaved() {
        String newTaskText = addNewText.getText().toString();

        if(newTaskText.isEmpty()) {
            //AlertDialog alertDialog =
            new AlertDialog.Builder(this)
                    .setMessage("The text box is empty, do you wish to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onExitAddTask();
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();
        } else {
            chosenStartAndEndDates();
            if (validateStartAndEndDates()) {
                Calendar newCalendar = GregorianCalendar.getInstance();
                chosenStartYear = newCalendar.get(Calendar.YEAR);
                chosenEndYear = newCalendar.get(Calendar.YEAR);
                chosenStartMonth = newCalendar.get(Calendar.MONTH);
                chosenEndMonth = newCalendar.get(Calendar.MONTH);
                chosenStartDay = newCalendar.get(Calendar.DAY_OF_MONTH);
                chosenEndDay = newCalendar.get(Calendar.DAY_OF_MONTH);

                String placeHolderString = String.format(Locale.getDefault(), "%02d:%02d", chosenStartHour, chosenStartMinute);
                setStartTimeTextview.setText(placeHolderString);
                placeHolderString = String.format(Locale.getDefault(), "%02d:%02d", chosenEndHour, chosenEndMinute);
                setEndTimeTextview.setText(placeHolderString);
                String placeholderDate = chosenStartDay + "/" + chosenStartMonth + "/" + chosenStartYear;
                setStartDateTextview.setText(placeholderDate);
                placeholderDate = chosenEndDay + "/" + chosenEndMonth + "/" + chosenEndYear;
                setEndDateTextview.setText(placeholderDate);
                saveAnEditedTask();
            } else {
                String placeHolderString = String.format(Locale.getDefault(), "%02d:%02d", chosenStartHour, chosenStartMinute);
                setStartTimeTextview.setText(placeHolderString);
                placeHolderString = String.format(Locale.getDefault(), "%02d:%02d", chosenEndHour, chosenEndMinute);
                setEndTimeTextview.setText(placeHolderString);
                String placeholderDate = chosenEndDay + "/" + chosenEndMonth + "/" + chosenEndYear;
                setEndDateTextview.setText(placeholderDate);
                placeholderDate = chosenStartDay + "/" + chosenStartMonth + "/" + chosenStartYear;
                setStartDateTextview.setText(placeholderDate);
                alertEndBiggerThanStart();
            }
        }
    }

    public void onExitAddTask() {
        CheckBundle checkBundle = checkResult();
        if (checkBundle.getRequestCode() == 2217) {
            if (ifSaveTaskPressed) {
                if (checkBundle.getActivityNumber() == 33) {
                    Intent newIntent = new Intent(AddTaskActivity.this, MainActivity.class);
                    newIntent.putExtra("TaskID", toAddTask.getID());
                    newIntent.putExtra("requestCode", 2217);
                    newIntent.putExtra("resultCode", Activity.RESULT_OK);
                    newIntent.putExtra("activityNumber", 39);
                    startActivity(newIntent);
                    finish();
                } else if (checkBundle.getActivityNumber() == 37) {
                    Intent newIntent = new Intent(AddTaskActivity.this, TasksActivity.class);
                    newIntent.putExtra("TaskID", toAddTask.getID());
                    newIntent.putExtra("requestCode", 2217);
                    newIntent.putExtra("resultCode", Activity.RESULT_OK);
                    newIntent.putExtra("activityNumber", 38);
                    startActivity(newIntent);
                    finish();
                } else if (checkBundle.getActivityNumber() == 41) {
                    Intent newIntent = new Intent(AddTaskActivity.this, CalendarActivity.class);
                    newIntent.putExtra("TaskID", toAddTask.getID());
                    newIntent.putExtra("requestCode", 2217);
                    newIntent.putExtra("resultCode", Activity.RESULT_OK);
                    newIntent.putExtra("activityNumber", 38);
                    startActivity(newIntent);
                    finish();
                }
            } else {
                if (toAddTaskID != -1) {
                    sqLiteDAO.deleteTask(toAddTask);
                    onBackPressed();
                }
            }
        } else if (checkBundle.getActivityNumber() != -1) {
            onBackPressed();
        }
    }

    private void chosenStartAndEndDates() {
        Calendar currentCalendar = GregorianCalendar.getInstance();
        //Setting START Time
        if (chosenStartYear == Integer.MAX_VALUE) {
            chosenStartYear = currentCalendar.get(Calendar.YEAR);
            chosenStartMonth = currentCalendar.get(Calendar.MONTH);
            chosenStartDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        }
        if (chosenStartHour == Integer.MAX_VALUE) {
            if (chosenEndYear != chosenStartYear || chosenEndMonth != chosenStartMonth ||
                    chosenEndDay != chosenStartDay) {
                chosenStartHour = 12;
                chosenStartMinute = 0;
            } else {
                chosenStartHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
                chosenStartMinute = currentCalendar.get(Calendar.MINUTE);
            }
        }
        //Setting END Time
        if (chosenEndYear == Integer.MAX_VALUE) {
            chosenEndYear = chosenStartYear;
            chosenEndMonth = chosenStartMonth;
            chosenEndDay = chosenStartDay;
        }
        if (chosenEndHour == Integer.MAX_VALUE) {
            chosenEndHour = chosenStartHour;
            chosenEndMinute = chosenStartMinute;
        }
    }

    private boolean validateStartAndEndDates() {
        chosenStartAndEndDates();
        //
        if (chosenEndYear >= chosenStartYear) {
            if (chosenEndYear == chosenStartYear && chosenEndMonth < chosenStartMonth) {
                return false;
            } else {
                if (chosenEndMonth == chosenStartMonth && chosenEndDay < chosenStartDay) {
                    return false;
                } else {
                    if (chosenEndDay == chosenStartDay && chosenEndHour < chosenStartHour) {
                        return false;
                    } else {
                        if (chosenEndHour == chosenStartHour && chosenEndMinute < chosenStartMinute) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }
        } else {
            return false;
        }
    }

    private void onSetTags() {
        onTaskSaved();
        Intent tagsIntent = new Intent(AddTaskActivity.this, TagsActivity.class);
        tagsIntent.putExtra("TaskID", toAddTaskID);
        tagsIntent.putExtra("requestCode", 2217);
        tagsIntent.putExtra("resultCode", Activity.RESULT_OK);
        tagsIntent.putExtra("activityNumber", 38);
        tagsIntent.putExtra("initialActivity", initialActivity);
        startActivity(tagsIntent);
    }

    public void checkSenderActivity() {
        SQLiteDAO sqLiteDAO = new SQLiteDAO(this);
        //We get the activity results checked and then we store them in a CheckBundle
        CheckBundle checkBundle = checkResult();
        //
        int activityNumber = checkBundle.getActivityNumber();
        if (activityNumber == 37 || activityNumber == 41) {
            switch (activityNumber) {
                case (37):
                    initialActivity = 37;
                    break;
                case (41):
                    initialActivity = 41;
                    break;
            }
        } else {
            Log.e("System.err", "AddTaskActivity.class was accessed from an unsupported Activity.");
        }
        sqLiteDAO.close();
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
                Log.e("System.err","Warning 'requestCode' or 'resultCode' missing!");
            }
            if (requestCode == 2217) {
                if (resultCode == Activity.RESULT_OK) {
                    checkBundle = new CheckBundle(-1, requestCode, resultCode, activityNumber);
                    return checkBundle;
                }
            }
        } else {
            Log.e("System.err","Intent Extras Bundle not checked correctly!");
        }
        return new CheckBundle(-1, -1, -1, -1);
    }

    private void scheduleAlarms(Calendar startCalendar, Calendar endCalendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long startTime = startCalendar.getTimeInMillis();
        long endTime = endCalendar.getTimeInMillis();

        PendingIntent startPendingIntent = createNotificationPendingIntent(context, toAddTaskID, true);
        PendingIntent endPendingIntent = createNotificationPendingIntent(context, toAddTaskID, false);
        long time = Calendar.getInstance().getTimeInMillis();
        if (startTime >= time) {
            if (startTime == endTime) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, startPendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, startPendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, endTime, endPendingIntent);
            }
            Toast.makeText(AddTaskActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
        } else  if (endTime >= time) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, endTime, endPendingIntent);
            Toast.makeText(AddTaskActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
        }
    }

    private PendingIntent createNotificationPendingIntent(Context context, int taskID, boolean isStart) {
        int notificationID = HelperFunctions.giveTaskIDGetNotificationID(taskID, isStart);
        Intent newIntent = new Intent(context, AlarmReceiver.class);
        newIntent.putExtra("notificationID", notificationID);
        newIntent.putExtra("taskID", taskID);
        newIntent.putExtra("isStart", isStart);
        newIntent.putExtra("taskTitle", toAddTask.getTaskTitle());

        return PendingIntent.getBroadcast(context, notificationID, newIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private void saveAnEditedTask() {
        //
        if (!ifSaveTaskPressed) {
            toAddTask.setTaskTitle(addNewTitle.getText().toString());
            toAddTask.setTaskText(addNewText.getText().toString());
            String progressText = progressTextview.getText().toString();
            if (progressText.equals("Progress Type")) {
                toAddTask.setTaskProgress("Not assigned");
            } else {
                toAddTask.setTaskProgress(progressText);
            }
            //
            Calendar startCalendar = GregorianCalendar.getInstance();
            startCalendar.set(Calendar.YEAR, chosenStartYear);
            startCalendar.set(Calendar.MONTH, chosenStartMonth);
            startCalendar.set(Calendar.DAY_OF_MONTH, chosenStartDay);
            startCalendar.set(Calendar.HOUR_OF_DAY, chosenStartHour);
            startCalendar.set(Calendar.MINUTE, chosenStartMinute);
            String startDate = String.valueOf(startCalendar.getTimeInMillis());
            toAddTask.setTask_startDate(startDate);
            //
            Calendar endCalendar = GregorianCalendar.getInstance();
            endCalendar.set(Calendar.YEAR, chosenEndYear);
            endCalendar.set(Calendar.MONTH, chosenEndMonth);
            endCalendar.set(Calendar.DAY_OF_MONTH, chosenEndDay);
            endCalendar.set(Calendar.HOUR_OF_DAY, chosenEndHour);
            endCalendar.set(Calendar.MINUTE, chosenEndMinute);
            String endDate = String.valueOf(endCalendar.getTimeInMillis());
            toAddTask.setTask_endDate(endDate);
            if (!mediaList.isEmpty()) {
                oldMediaBitmap = "";
                for (Media currentMedia:mediaList) {
                    oldMediaBitmap = oldMediaBitmap.concat((currentMedia.getID()) + "|");
                }
            } else {
                oldMediaBitmap = null;
            }
            if (progressText.equals("In progress")) {
                scheduleAlarms(startCalendar, endCalendar);
            }
            toAddTask.setMediaBitmaps(oldMediaBitmap);
            //
            toAddTaskID = sqLiteDAO.addTaskGetID(toAddTask);
            toAddTask.setID(toAddTaskID);
            ifSaveTaskPressed = true;
        } else {
            toAddTask.setTaskTitle(addNewTitle.getText().toString());
            toAddTask.setTaskText(addNewText.getText().toString());
            String progressText = progressTextview.getText().toString();
            if (!progressText.equals("Progress Type")) {
                toAddTask.setTaskProgress("Not assigned");
            } else {
                toAddTask.setTaskProgress(progressText);
            }
            //
            Calendar startCalendar = GregorianCalendar.getInstance();
            startCalendar.set(Calendar.YEAR, chosenStartYear);
            startCalendar.set(Calendar.MONTH, chosenStartMonth);
            startCalendar.set(Calendar.DAY_OF_MONTH, chosenStartDay);
            startCalendar.set(Calendar.HOUR_OF_DAY, chosenStartHour);
            startCalendar.set(Calendar.MINUTE, chosenStartMinute);
            String startDate = String.valueOf(startCalendar.getTimeInMillis());
            toAddTask.setTask_startDate(startDate);
            //
            Calendar endCalendar = GregorianCalendar.getInstance();
            endCalendar.set(Calendar.YEAR, chosenEndYear);
            endCalendar.set(Calendar.MONTH, chosenEndMonth);
            endCalendar.set(Calendar.DAY_OF_MONTH, chosenEndDay);
            endCalendar.set(Calendar.HOUR_OF_DAY, chosenEndHour);
            endCalendar.set(Calendar.MINUTE, chosenEndMinute);
            String endDate = String.valueOf(endCalendar.getTimeInMillis());
            toAddTask.setTask_endDate(endDate);
            if (!mediaList.isEmpty()) {
                oldMediaBitmap = "";
                for (Media currentMedia:mediaList) {
                    oldMediaBitmap = oldMediaBitmap.concat((currentMedia.getID()) + "|");
                }
            } else {
                oldMediaBitmap = null;
            }
            if (progressText.equals("In progress")) {
                scheduleAlarms(startCalendar, endCalendar);
            }
            toAddTask.setMediaBitmaps(oldMediaBitmap);
            //
            toAddTaskID = sqLiteDAO.updateTask(toAddTask);
        }
    }

    private void alertEndBiggerThanStart() {
        String title = "<font color=#6EBEF0> Cannot Save </font>";
        String okButton = "<font color=#6EBEF0> OK </font>";
        AlertDialog.Builder warningBuilder =
                new AlertDialog.Builder(this)
                        .setTitle(Html.fromHtml(title))
                        .setMessage("You cannot set the End Date to be before the Start Date")
                        .setPositiveButton(Html.fromHtml(okButton), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        AlertDialog alertDialog = warningBuilder.show();
    }

    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(AddTaskActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(AddTaskActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(addTasksDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            addTasksDrawerLayout.closeDrawer(GravityCompat.START);
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
            Intent registerIntent = new Intent(AddTaskActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(AddTaskActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(AddTaskActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(AddTaskActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(AddTaskActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(AddTaskActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(AddTaskActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(AddTaskActivity.this, AboutUsActivity.class));
            finish();
        }
        addTasksDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void onClick(Uri uri) {}
        @Override
        public void onLongClick(Uri uri, CardView cardView) {}
        @Override
        public void onItemClick(View itemView, int position) {}
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        addTaskService.shutdown();
        if (imageHorizontalAdapter != null) {
            imageHorizontalAdapter.onDestroy();
        }
    }
}


