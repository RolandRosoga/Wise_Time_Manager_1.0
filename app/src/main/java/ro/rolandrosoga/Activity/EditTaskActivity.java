package ro.rolandrosoga.Activity;

import android.Manifest;
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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import ro.rolandrosoga.Mock.MediaPair;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.Notification.AlarmReceiver;
import ro.rolandrosoga.R;

public class EditTaskActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView burgerMenuText, setStartTimeTextview, setStartDateTextview, setEndTimeTextview, setEndDateTextview, progressTextview, headerFirstName;
    DrawerLayout editTasksDrawerLayout;
    NavigationView editTasksNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, toolbar, pomodoroLinearLayout, galleryLinearLayout;
    ImageButton burgerMenuImageButton, toolbarSaveEditTask, toolbarExitEditTask, galleryButton, buttonCloseTaskMenu, buttonSetTags, settingsButton, pomodoroButton, buttonAddFromGallery;
    EditText editNewTitle, editNewText;
    int oldTaskID, initialActivity = -1;
    String oldTitle, oldText, oldProgress, oldCategory1, oldCategory2, oldCategory3, oldStartDate, oldEndDate, textStartTime, textStartDate, textEndTime, textEndDate, oldMediaBitmap;
    Task toEditTask;
    FloatingActionButton buttonOpenTaskMenu, calendarView;
    ConstraintLayout buttonTaskMenu, setStartTimeConstraint, setStartDateConstraint, setEndTimeConstraint, setEndDateConstraint;
    boolean ifSaveTaskPressed;
    Context context;
    SQLiteDAO sqLiteDAO;
    int chosenStartHour, chosenStartMinute, chosenEndHour, chosenEndMinute, chosenStartDay, chosenStartMonth, chosenStartYear, chosenEndDay, chosenEndMonth, chosenEndYear = Integer.MAX_VALUE;
    ImageView checkBoxExceeded, checkBoxInProgress, checkBoxCompleted, headerProfileImage;
    View profileView;
    User currentUser;
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    MockImageAdapter imageHorizontalAdapter;
    RecyclerView imagesRecyclerView;
    List<Uri> uriList = new ArrayList<>();
    List<Bitmap> bitmapList = new ArrayList<>();
    List<Media> mediaList = new ArrayList<>();
    AlarmManager alarmManager;
    private ExecutorService editTaskService;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        context = EditTaskActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(EditTaskActivity.this,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditTaskActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        initializeUI();
        /*---------------------------------Middle---------------------------------------------*/

        initializeMiddleComponents();
        checkSenderActivity();
        initializeComponents();
        //
    }

    private void initializeUI() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        editTaskService = Executors.newFixedThreadPool(80, privilegedFactory);
        
        /*-------------------------Variable Definitions---------------------------------------*/
        toolbar = findViewById(R.id.edit_tasks_toolbar);
        burgerMenuText = findViewById(R.id.burger_menu_text);
        editTasksDrawerLayout = findViewById(R.id.drawer_layout_edit_tasks);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        editTasksNavigationView = findViewById(R.id.sidebar_navigation_view_edit_tasks);
        toolbarSaveEditTask = findViewById(R.id.toolbar_save_edit_task);
        toolbarExitEditTask = findViewById(R.id.toolbar_exit_edit_task);
        editNewTitle = findViewById(R.id.edit_new_title);
        editNewText = findViewById(R.id.edit_new_text);
        buttonOpenTaskMenu = findViewById(R.id.button_open_task_menu);
        buttonTaskMenu = findViewById(R.id.button_task_menu);
        buttonCloseTaskMenu = findViewById(R.id.button_close_task_menu);
        buttonSetTags = findViewById(R.id.button_set_tags);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        calendarView = findViewById(R.id.calendarView);
        buttonAddFromGallery = findViewById(R.id.button_add_picture_from_gallery);
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
        imagesRecyclerView = findViewById(R.id.images_recyclerview);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);

        editTaskService.submit(new Runnable() {
            @Override
            public void run() {
                /*---------------------------------Top------------------------------------------------*/
                toolbarSaveEditTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTaskSaved();
                    }
                });
                toolbarExitEditTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onExitEditTask();
                    }
                });
                /*--------------------------------Bottom----------------------------------------------*/
                burgerMenuText.setText(R.string.tasks_text);
                burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTasksDrawerLayout.open();
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
                buttonAddFromGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (HelperFunctions.isPhotoPickerAvailable()) {
                            addFromGallery();
                        }
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
            }
        });
    }
    private void scheduleAlarms(Calendar startCalendar, Calendar endCalendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long startTime = startCalendar.getTimeInMillis();
        long endTime = endCalendar.getTimeInMillis();

        PendingIntent startPendingIntent = createNotificationPendingIntent(context, oldTaskID, true);
        PendingIntent endPendingIntent = createNotificationPendingIntent(context, oldTaskID, false);
        long time = Calendar.getInstance().getTimeInMillis();
        if (startTime >= time) {
            if (startTime == endTime) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, startPendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, startPendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, endTime, endPendingIntent);
            }
            Toast.makeText(EditTaskActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
        } else  if (endTime >= time) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, endTime, endPendingIntent);
            Toast.makeText(EditTaskActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
        }
    }

    private PendingIntent createNotificationPendingIntent(Context context, int taskID, boolean isStart) {
        int notificationID = HelperFunctions.giveTaskIDGetNotificationID(taskID, isStart);
        Intent newIntent = new Intent(context, AlarmReceiver.class);
        newIntent.putExtra("notificationID", notificationID);
        newIntent.putExtra("taskID", taskID);
        newIntent.putExtra("isStart", isStart);
        newIntent.putExtra("taskTitle", toEditTask.getTaskTitle());

        return PendingIntent.getBroadcast(context, notificationID, newIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
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
        imageHorizontalAdapter = new MockImageAdapter(EditTaskActivity.this, bitmapList, mediaList, imageListener);
        imagesRecyclerView.setAdapter(imageHorizontalAdapter);
        imagesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.HORIZONTAL));
        imagesRecyclerView.setHasFixedSize(true);
    }

    private void initializeComponents() {
        Calendar calendarInstance = new GregorianCalendar();
        //
        editNewTitle.setText(oldTitle);
        editNewText.setText(oldText);
        //
        calendarInstance.setTimeInMillis(Long.parseLong(oldStartDate));
        chosenStartYear = calendarInstance.get(Calendar.YEAR);
        chosenStartMonth = calendarInstance.get(Calendar.MONTH);
        chosenStartDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
        chosenStartHour = calendarInstance.get(Calendar.HOUR_OF_DAY);
        chosenStartMinute = calendarInstance.get(Calendar.MINUTE);
        //
        textStartTime = String.format(Locale.getDefault(), "%02d:%02d", chosenStartHour, chosenStartMinute);
        setStartTimeTextview.setText(textStartTime);
        textStartDate = chosenStartDay + "/" + chosenStartMonth + "/" + chosenStartYear;
        setStartDateTextview.setText(textStartDate);
        //
        calendarInstance.setTimeInMillis(Long.parseLong(oldEndDate));
        chosenEndYear = calendarInstance.get(Calendar.YEAR);
        chosenEndMonth = calendarInstance.get(Calendar.MONTH);
        chosenEndDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
        chosenEndHour = calendarInstance.get(Calendar.HOUR_OF_DAY);
        chosenEndMinute = calendarInstance.get(Calendar.MINUTE);
        //
        textEndTime = String.format(Locale.getDefault(), "%02d:%02d", chosenEndHour, chosenEndMinute);
        setEndTimeTextview.setText(textEndTime);
        textEndDate = chosenEndDay + "/" + chosenEndMonth + "/" + chosenEndYear;
        setEndDateTextview.setText(textEndDate);

        int color;
        switch (oldProgress) {
            case ("Not assigned"):
                progressTextview.setText(oldProgress);
                break;
            case ("In progress"):
                progressTextview.setText(oldProgress);
                checkBoxExceeded.setVisibility(View.GONE);
                checkBoxCompleted.setVisibility(View.GONE);
                checkBoxInProgress.setVisibility(View.VISIBLE);
                color = Color.parseColor("#" + "F0EE6E");
                checkBoxInProgress.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                progressTextview.setTextAppearance(R.style.yellow);
                break;
            case ("Completed"):
                progressTextview.setText(oldProgress);
                checkBoxExceeded.setVisibility(View.GONE);
                checkBoxInProgress.setVisibility(View.GONE);
                checkBoxCompleted.setVisibility(View.VISIBLE);
                color = Color.parseColor("#" + "36894D");
                checkBoxCompleted.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                progressTextview.setTextAppearance(R.style.green);
                break;
            case ("Exceeded"):
                progressTextview.setText(oldProgress);
                checkBoxInProgress.setVisibility(View.GONE);
                checkBoxCompleted.setVisibility(View.GONE);
                checkBoxExceeded.setVisibility(View.VISIBLE);
                color = Color.parseColor("#" + "992923");
                checkBoxExceeded.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                progressTextview.setTextAppearance(R.style.red);
                break;
        }
        initializeImagesAdapter();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initializeMiddleComponents() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        editTaskService = Executors.newFixedThreadPool(80, privilegedFactory);
        //
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //
        ActionBarDrawerToggle editTasksToggle = new ActionBarDrawerToggle(this,editTasksDrawerLayout, R.string.open,R.string.close);
        editTasksDrawerLayout.addDrawerListener(editTasksToggle);
        editTasksToggle.syncState();
        editTasksNavigationView.setNavigationItemSelectedListener(this);
        editTasksNavigationView.bringToFront();
        editTasksNavigationView.setNavigationItemSelectedListener(this);
        profileView = editTasksNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);
        //

        editTaskService.submit(new Runnable() {
            @Override
            public void run() {
                currentUser = AuthHelper.getInstance().getCurrentUser();
                //Header Initialization
                String firstname = currentUser.getUser_full_name();
                Scanner spaceSearcher = new Scanner(firstname);
                firstname = spaceSearcher.next();
                headerFirstName.setText(firstname);
                if (currentUser.getProfile_image() != null) {
                    headerProfileImage.setImageBitmap(HelperFunctions.bytesArrayToBitmap(currentUser.getProfile_image()));
                }

                editNewTitle.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    public boolean onTouch(View v, MotionEvent event) {
                        if (editNewTitle.hasFocus()) {
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
                editNewText.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    public boolean onTouch(View v, MotionEvent event) {
                        if (editNewText.hasFocus()) {
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
                        oldProgress = "In progress";
                        progressTextview.setTextAppearance(R.style.yellow);
                        toEditTask.setTaskProgress("In progress");
                    }
                });
                checkBoxInProgress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBoxExceeded.setVisibility(View.GONE);
                        checkBoxInProgress.setVisibility(View.GONE);
                        checkBoxCompleted.setVisibility(View.VISIBLE);
                        int color = Color.parseColor("#" + "36894D");
                        checkBoxCompleted.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        progressTextview.setText("Completed");
                        oldProgress = "Completed";
                        progressTextview.setTextAppearance(R.style.green);
                        toEditTask.setTaskProgress("Completed");
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
                        oldProgress = "Exceeded";
                        progressTextview.setTextAppearance(R.style.red);
                        toEditTask.setTaskProgress("Exceeded");
                    }
                });
            }
        });
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
    }

    private void initializeImagesAdapter() {
        if (oldMediaBitmap != null) {
            int counter = 0;
            while (counter < oldMediaBitmap.length()) {
                MediaPair currentMediaPair = HelperFunctions.giveIndexGetSubstring(oldMediaBitmap, counter);
                int mediaID = Integer.parseInt(currentMediaPair.getCurrentString());
                try {
                    Media currentMedia = sqLiteDAO.getMediaById(mediaID);
                    mediaList.add(currentMedia);
                    bitmapList.add(HelperFunctions.bytesArrayToBitmap(currentMedia.getMedia_blob()));
                } catch (Exception ignore) {}
                counter = currentMediaPair.getLastIndex() + 1;
            }
            updateImagesRecycler();
        }

    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(EditTaskActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 39);
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
                selectedTime = new TimePickerDialog(EditTaskActivity.this, R.style.TimeChooser, onStartTime, chosenStartHour, chosenStartMinute, true);
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
                selectedTime = new TimePickerDialog(EditTaskActivity.this, R.style.TimeChooser, onEndTime, chosenEndHour, chosenEndMinute, true);
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

    public void onExitEditTask() {
        CheckBundle checkBundle = checkResult();
        if (checkBundle.getObject_ID() != -1) {
            if (checkBundle.getRequestCode() == 2217) {
                if (ifSaveTaskPressed) {
                    if (checkBundle.getActivityNumber() == 33) {
                        Intent newIntent = new Intent(EditTaskActivity.this, MainActivity.class);
                        newIntent.putExtra("TaskID", toEditTask.getID());
                        newIntent.putExtra("requestCode", 2217);
                        newIntent.putExtra("resultCode", Activity.RESULT_OK);
                        newIntent.putExtra("activityNumber", 39);
                        startActivity(newIntent);
                        finish();
                    } else if (checkBundle.getActivityNumber() == 37) {
                        Intent newIntent = new Intent(EditTaskActivity.this, TasksActivity.class);
                        newIntent.putExtra("TaskID", toEditTask.getID());
                        newIntent.putExtra("requestCode", 2217);
                        newIntent.putExtra("resultCode", Activity.RESULT_OK);
                        newIntent.putExtra("activityNumber", 39);
                        startActivity(newIntent);
                        finish();
                    } else if (checkBundle.getActivityNumber() == 41) {
                        Intent newIntent = new Intent(EditTaskActivity.this, CalendarActivity.class);
                        newIntent.putExtra("TaskID", toEditTask.getID());
                        newIntent.putExtra("requestCode", 2217);
                        newIntent.putExtra("resultCode", Activity.RESULT_OK);
                        newIntent.putExtra("activityNumber", 39);
                        startActivity(newIntent);
                        finish();
                    } else if (initialActivity != -1) {
                        if (initialActivity == 33) {
                            Intent newIntent = new Intent(EditTaskActivity.this, MainActivity.class);
                            newIntent.putExtra("TaskID", toEditTask.getID());
                            newIntent.putExtra("requestCode", 2217);
                            newIntent.putExtra("resultCode", Activity.RESULT_OK);
                            newIntent.putExtra("activityNumber", 39);
                            startActivity(newIntent);
                            finish();
                        } else if (initialActivity == 37) {
                            Intent newIntent = new Intent(EditTaskActivity.this, TasksActivity.class);
                            newIntent.putExtra("TaskID", toEditTask.getID());
                            newIntent.putExtra("requestCode", 2217);
                            newIntent.putExtra("resultCode", Activity.RESULT_OK);
                            newIntent.putExtra("activityNumber", 39);
                            startActivity(newIntent);
                            finish();
                        } else if (initialActivity == 41) {
                            Intent newIntent = new Intent(EditTaskActivity.this, CalendarActivity.class);
                            newIntent.putExtra("TaskID", toEditTask.getID());
                            newIntent.putExtra("requestCode", 2217);
                            newIntent.putExtra("resultCode", Activity.RESULT_OK);
                            newIntent.putExtra("activityNumber", 39);
                            startActivity(newIntent);
                            finish();
                        }
                    }
                } else {
                    sqLiteDAO.deleteTask(toEditTask);
                }
            } else {
                if (checkBundle.getActivityNumber() == 33) {
                    Intent newIntent = new Intent(EditTaskActivity.this, MainActivity.class);
                    newIntent.putExtra("TaskID", toEditTask.getID());
                    newIntent.putExtra("requestCode", 2218);
                    newIntent.putExtra("resultCode", Activity.RESULT_OK);
                    newIntent.putExtra("activityNumber", 39);
                    startActivity(newIntent);
                    finish();
                } else if (checkBundle.getActivityNumber() == 37) {
                    Intent newIntent = new Intent(EditTaskActivity.this, TasksActivity.class);
                    newIntent.putExtra("TaskID", toEditTask.getID());
                    newIntent.putExtra("requestCode", 2218);
                    newIntent.putExtra("resultCode", Activity.RESULT_OK);
                    newIntent.putExtra("activityNumber", 39);
                    startActivity(newIntent);
                    finish();
                } else if (checkBundle.getActivityNumber() == 41) {
                    Intent newIntent = new Intent(EditTaskActivity.this, CalendarActivity.class);
                    newIntent.putExtra("TaskID", toEditTask.getID());
                    newIntent.putExtra("requestCode", 2218);
                    newIntent.putExtra("resultCode", Activity.RESULT_OK);
                    newIntent.putExtra("activityNumber", 39);
                    startActivity(newIntent);
                    finish();
                } else if (initialActivity != -1) {
                    if (initialActivity == 33) {
                        Intent newIntent = new Intent(EditTaskActivity.this, MainActivity.class);
                        newIntent.putExtra("TaskID", toEditTask.getID());
                        newIntent.putExtra("requestCode", 2218);
                        newIntent.putExtra("resultCode", Activity.RESULT_OK);
                        newIntent.putExtra("activityNumber", 39);
                        startActivity(newIntent);
                        finish();
                    } else if (initialActivity == 37) {
                        Intent newIntent = new Intent(EditTaskActivity.this, TasksActivity.class);
                        newIntent.putExtra("TaskID", toEditTask.getID());
                        newIntent.putExtra("requestCode", 2218);
                        newIntent.putExtra("resultCode", Activity.RESULT_OK);
                        newIntent.putExtra("activityNumber", 39);
                        startActivity(newIntent);
                        finish();
                    } else if (initialActivity == 41) {
                        Intent newIntent = new Intent(EditTaskActivity.this, CalendarActivity.class);
                        newIntent.putExtra("TaskID", toEditTask.getID());
                        newIntent.putExtra("requestCode", 2218);
                        newIntent.putExtra("resultCode", Activity.RESULT_OK);
                        newIntent.putExtra("activityNumber", 39);
                        startActivity(newIntent);
                        finish();
                    }
                }
            }
        } else if (checkBundle.getActivityNumber() != -1) {
            onBackPressed();
        }
    }

    public void checkSenderActivity() {
        //We get the activity results checked and then we store them in a CheckBundle
        CheckBundle checkBundle = checkResult();
        //
        int activityNumber = checkBundle.getActivityNumber();
        if (activityNumber == 33 || activityNumber == 37 || activityNumber == 40 || activityNumber == 41) {
            oldTaskID = checkBundle.getObject_ID();
            try {
                toEditTask = sqLiteDAO.getTaskById(oldTaskID);
                oldTitle = toEditTask.getTaskTitle();
                oldText = toEditTask.getTaskText();
                oldProgress = toEditTask.getTaskProgress();
                oldCategory1 = toEditTask.getCategory1();
                oldCategory2 = toEditTask.getCategory2();
                oldCategory3 = toEditTask.getCategory3();
                oldStartDate = toEditTask.getTask_startDate();
                oldEndDate = toEditTask.getTask_endDate();
                oldMediaBitmap = toEditTask.getMediaBitmaps();
            } catch (Exception ignore) {}
            switch (activityNumber) {
                case (33):
                    initialActivity = 33;
                    break;
                case (37):
                    initialActivity = 37;
                    break;
                case (40):
                    break;
                case (41):
                    initialActivity = 41;
                    break;
            }
        } else {
            Log.e("System.err", "EditTaskActivity.class was accessed from an unsupported Activity.");
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
                Log.e("System.err","Warning 'requestCode' or 'resultCode' missing!");
            }
            if (requestCode == 2217 || requestCode == 2218) {
                if (resultCode == Activity.RESULT_OK) {
                    int taskID = -1;
                    try {
                        taskID = editBundle.getInt("TaskID");
                    } catch (Exception e) {
                        Log.e("System.err","Warning 'TaskID' missing!");
                    }
                    try {
                        initialActivity = editBundle.getInt("initialActivity");
                    } catch (Exception e) {
                        Log.e("System.err", "Warning 'initialActivity' missing!");
                    }
                    checkBundle = new CheckBundle(taskID, requestCode, resultCode, activityNumber);
                    return checkBundle;
                }
            }
        } else {
            Log.e("System.err","Intent Extras Bundle not checked correctly!");
        }
        return new CheckBundle(-1, -1, -1, -1);
    }

    private void onSetTags() {
        if (onTaskSaved()) {
            Intent tagsIntent = new Intent(EditTaskActivity.this, TagsActivity.class);
            tagsIntent.putExtra("TaskID", oldTaskID);
            tagsIntent.putExtra("requestCode", 2218);
            tagsIntent.putExtra("resultCode", Activity.RESULT_OK);
            tagsIntent.putExtra("activityNumber", 39);
            tagsIntent.putExtra("initialActivity", initialActivity);
            startActivity(tagsIntent);
        }

    }
    private boolean onTaskSaved() {
        String newTaskText = editNewText.getText().toString();

        Task oldTask = new Task(oldTaskID, oldTitle, oldText, oldProgress, oldCategory1, oldCategory2, oldCategory3, oldStartDate, oldEndDate, oldMediaBitmap);

        if(newTaskText.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("The text box is empty, do you wish to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sqLiteDAO.deleteTask(oldTask);
                            onExitEditTask();
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();
            return false;
        } else {
            chosenStartAndEndDates();
            if (validateStartAndEndDates()) {
                String placeHolderString = String.format(Locale.getDefault(), "%02d:%02d", chosenStartHour, chosenStartMinute);
                setStartTimeTextview.setText(placeHolderString);
                placeHolderString = String.format(Locale.getDefault(), "%02d:%02d", chosenEndHour, chosenEndMinute);
                setEndTimeTextview.setText(placeHolderString);
                String placeholderDate = chosenEndDay + "/" + chosenEndMonth + "/" + chosenEndYear;
                setEndDateTextview.setText(placeholderDate);
                placeholderDate = chosenStartDay + "/" + chosenStartMonth + "/" + chosenStartYear;
                setStartDateTextview.setText(placeholderDate);
                saveAnEditedTask(oldTask);
                return true;
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
                return false;
            }
        }

    }

    private void chosenStartAndEndDates() {
        Calendar currentCalendar = GregorianCalendar.getInstance();
        currentCalendar.setTime(new Date());
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

    private void saveAnEditedTask(Task oldTask) {
        String newTaskTitle = editNewTitle.getText().toString();
        String newTaskText = editNewText.getText().toString();
        //
        oldTask.setID(oldTaskID);
        oldTask.setTaskTitle(newTaskTitle);
        oldTask.setTaskText(newTaskText);
        oldTask.setTaskProgress(toEditTask.getTaskProgress());
        oldTask.setCategory1(oldCategory1);
        oldTask.setCategory2(oldCategory2);
        oldTask.setCategory3(oldCategory3);

        //
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.set(Calendar.YEAR, chosenStartYear);
        startCalendar.set(Calendar.MONTH, chosenStartMonth);
        startCalendar.set(Calendar.DAY_OF_MONTH, chosenStartDay);
        startCalendar.set(Calendar.HOUR_OF_DAY, chosenStartHour);
        startCalendar.set(Calendar.MINUTE, chosenStartMinute);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        oldTask.setTask_startDate( String.valueOf(startCalendar.getTimeInMillis()) );
        //
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.set(Calendar.YEAR, chosenEndYear);
        endCalendar.set(Calendar.MONTH, chosenEndMonth);
        endCalendar.set(Calendar.DAY_OF_MONTH, chosenEndDay);
        endCalendar.set(Calendar.HOUR_OF_DAY, chosenEndHour);
        endCalendar.set(Calendar.MINUTE, chosenEndMinute);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);
        oldTask.setTask_endDate( String.valueOf(endCalendar.getTimeInMillis()) );
        //
        if (!mediaList.isEmpty()) {
            oldMediaBitmap = "";
            for (Media currentMedia:mediaList) {
                oldMediaBitmap = oldMediaBitmap.concat((currentMedia.getID()) + "|");
            }
        } else {
            oldMediaBitmap = null;
        }
        oldTask.setMediaBitmaps(oldMediaBitmap);
        oldTaskID = sqLiteDAO.updateTask(oldTask);

        if (oldProgress.equals("In progress")) {
            scheduleAlarms(startCalendar, endCalendar);
        }
        ifSaveTaskPressed = true;
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
        Intent settingIntent = new Intent(EditTaskActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(EditTaskActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(EditTaskActivity.this, SettingsActivity.class);
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
        if(editTasksDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            editTasksDrawerLayout.closeDrawer(GravityCompat.START);
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
            Intent registerIntent = new Intent(EditTaskActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(EditTaskActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(EditTaskActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(EditTaskActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(EditTaskActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(EditTaskActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(EditTaskActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(EditTaskActivity.this, AboutUsActivity.class));
            finish();
        }
        editTasksDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        editTaskService.shutdown();
        if (imageHorizontalAdapter != null) {
            imageHorizontalAdapter.onDestroy();
        }
    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void onClick(Uri uri) {}
        @Override
        public void onLongClick(Uri uri, CardView cardView) {}
        @Override
        public void onItemClick(View itemView, int position) {}
    };
}

