package ro.rolandrosoga.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import ro.rolandrosoga.Mock.Note;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class EditNoteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView burgerMenuText, headerFirstName;
    DrawerLayout editNotesDrawerLayout;
    NavigationView editNotesNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout, galleryLinearLayout;
    ImageButton buttonSaveFromGallery, burgerMenuImageButton, toolbarSaveEditNote, toolbarExitEditNote, buttonCloseNoteMenu, buttonSetTags, settingsButton, pomodoroButton, galleryButton;
    ImageView headerProfileImage;
    EditText editNewTitle, editNewText;
    int oldNoteID, initialActivity = -1;
    String oldTitle, oldText, oldDate, oldCategory1, oldCategory2, oldCategory3, oldMediaBitmap;
    Note toEditNote;
    FloatingActionButton buttonOpenNoteMenu, calendarView;
    ConstraintLayout buttonNoteMenu;
    boolean ifSaveNotePressed;
    Context context;
    SQLiteDAO sqLiteDAO;
    User currentUser;
    View profileView;
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    MockImageAdapter imageHorizontalAdapter;
    RecyclerView imagesRecyclerView;
    List<Uri> uriList = new ArrayList<>();
    List<Bitmap> bitmapList = new ArrayList<>();
    List<Media> mediaList = new ArrayList<>();
    private ExecutorService executorService;
    Bundle editBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        context = EditNoteActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        initializeUI();
        /*-------------------------Variable Definitions---------------------------------------*/

        /*---------------------------------Middle---------------------------------------------*/
        ActionBarDrawerToggle editNotesToggle = new ActionBarDrawerToggle(this,editNotesDrawerLayout, R.string.open,R.string.close);
        editNotesDrawerLayout.addDrawerListener(editNotesToggle);
        editNotesToggle.syncState();
        editNotesNavigationView.setNavigationItemSelectedListener(this);
        editNotesNavigationView.bringToFront();
        editNotesNavigationView.setNavigationItemSelectedListener(this);
        profileView = editNotesNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);

        //EditNoteActivity implementation logic
        checkSenderActivity();
        initializeComponents();
    }

    private void initializeUI() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        executorService = Executors.newFixedThreadPool(100, privilegedFactory);

        burgerMenuText = findViewById(R.id.burger_menu_text);
        editNotesDrawerLayout = findViewById(R.id.drawer_layout_edit_notes);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        editNotesNavigationView = findViewById(R.id.sidebar_navigation_view_edit_notes);
        toolbarSaveEditNote = findViewById(R.id.toolbar_save_edit_note);
        toolbarExitEditNote = findViewById(R.id.toolbar_exit_edit_note);
        editNewTitle = findViewById(R.id.edit_new_title);
        editNewText = findViewById(R.id.edit_new_text);
        buttonOpenNoteMenu = findViewById(R.id.button_open_note_menu);
        buttonNoteMenu = findViewById(R.id.button_note_menu);
        buttonCloseNoteMenu = findViewById(R.id.button_close_note_menu);
        buttonSetTags = findViewById(R.id.button_set_tags);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        calendarView = findViewById(R.id.calendarView);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);
        buttonSaveFromGallery = findViewById(R.id.button_save_from_gallery);
        imagesRecyclerView = findViewById(R.id.images_recyclerview);

        buttonOpenNoteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOpenNoteMenu.setVisibility(View.INVISIBLE);
                buttonNoteMenu.setVisibility(View.VISIBLE);
            }
        });
        buttonCloseNoteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNoteMenu.setVisibility(View.INVISIBLE);
                buttonOpenNoteMenu.setVisibility(View.VISIBLE);
            }
        });

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                /*---------------------------------Top------------------------------------------------*/
                toolbarSaveEditNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNoteSaved();
                    }
                });
                toolbarExitEditNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onExitEditNote();
                    }
                });
                /*--------------------------------Bottom----------------------------------------------*/
                //mainBottomBar
                burgerMenuText.setText(R.string.notes_text);
                burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editNotesDrawerLayout.open();
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
                galleryLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGalleryClick();
                    }
                });
                galleryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGalleryClick();
                    }
                });
                buttonSaveFromGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFromGallery();
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
        imageHorizontalAdapter = new MockImageAdapter(EditNoteActivity.this, bitmapList, mediaList, imageListener);
        imagesRecyclerView.setAdapter(imageHorizontalAdapter);
        imagesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.HORIZONTAL));
        imagesRecyclerView.setHasFixedSize(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeComponents() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        executorService = Executors.newCachedThreadPool(privilegedFactory);

        //
        editNewTitle.setText(oldTitle);
        editNewText.setText(oldText);
        //
        executorService.submit(new Runnable() {
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
        //
        initializeImagesAdapter();
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

    private void onGalleryClick() {
        Intent newIntent = new Intent(EditNoteActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(EditNoteActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 36);
        startActivity(newIntent);
        finish();
    }

    public void onExitEditNote() {
        CheckBundle checkBundle = checkResult();
        if (checkBundle.getObject_ID() != -1) {
            if (checkBundle.getRequestCode() == 1217) {
                if (ifSaveNotePressed) {
                    if (checkBundle.getActivityNumber() == 33) {
                        Intent newIntent = new Intent(EditNoteActivity.this, MainActivity.class);
                        newIntent.putExtra("NoteID", oldNoteID);
                        newIntent.putExtra("requestCode", 1217);
                        newIntent.putExtra("resultCode", Activity.RESULT_OK);
                        newIntent.putExtra("activityNumber", 39);
                        newIntent.putExtra("initialActivity", 33);
                        startActivity(newIntent);
                        finish();
                    } else if (checkBundle.getActivityNumber() == 34) {
                        Intent newIntent = new Intent(EditNoteActivity.this, NotesActivity.class);
                        newIntent.putExtra("NoteID", oldNoteID);
                        newIntent.putExtra("requestCode", 1217);
                        newIntent.putExtra("resultCode", Activity.RESULT_OK);
                        newIntent.putExtra("activityNumber", 39);
                        newIntent.putExtra("initialActivity", 34);
                        startActivity(newIntent);
                        finish();
                    } else if (initialActivity != -1) {
                        if (initialActivity == 33) {
                            Intent newIntent = new Intent(EditNoteActivity.this, MainActivity.class);
                            newIntent.putExtra("NoteID", oldNoteID);
                            newIntent.putExtra("requestCode", 1217);
                            newIntent.putExtra("resultCode", Activity.RESULT_OK);
                            newIntent.putExtra("activityNumber", 39);
                            newIntent.putExtra("initialActivity", 33);
                            startActivity(newIntent);
                            finish();
                        } else if (initialActivity == 34) {
                            Intent newIntent = new Intent(EditNoteActivity.this, NotesActivity.class);
                            newIntent.putExtra("NoteID", oldNoteID);
                            newIntent.putExtra("requestCode", 1217);
                            newIntent.putExtra("resultCode", Activity.RESULT_OK);
                            newIntent.putExtra("activityNumber", 39);
                            newIntent.putExtra("initialActivity", 34);
                            startActivity(newIntent);
                            finish();
                        }
                    }
                } else {
                    sqLiteDAO.deleteNote(toEditNote);
                }
            } else {
                if (checkBundle.getActivityNumber() == 33) {
                    Intent newIntent = new Intent(EditNoteActivity.this, MainActivity.class);
                    newIntent.putExtra("NoteID", oldNoteID);
                    newIntent.putExtra("requestCode", 1218);
                    newIntent.putExtra("resultCode", Activity.RESULT_OK);
                    newIntent.putExtra("activityNumber", 39);
                    newIntent.putExtra("initialActivity", 33);
                    startActivity(newIntent);
                    finish();
                } else if (checkBundle.getActivityNumber() == 34) {
                    Intent newIntent = new Intent(EditNoteActivity.this, NotesActivity.class);
                    newIntent.putExtra("NoteID", oldNoteID);
                    newIntent.putExtra("requestCode", 1218);
                    newIntent.putExtra("resultCode", Activity.RESULT_OK);
                    newIntent.putExtra("activityNumber", 39);
                    newIntent.putExtra("initialActivity", 34);
                    startActivity(newIntent);
                    finish();
                } else if (initialActivity != -1) {
                    if (initialActivity == 33) {
                        Intent newIntent = new Intent(EditNoteActivity.this, MainActivity.class);
                        newIntent.putExtra("NoteID", oldNoteID);
                        newIntent.putExtra("requestCode", 1218);
                        newIntent.putExtra("resultCode", Activity.RESULT_OK);
                        newIntent.putExtra("activityNumber", 39);
                        newIntent.putExtra("initialActivity", 33);
                        startActivity(newIntent);
                        finish();
                    } else if (initialActivity == 34) {
                        Intent newIntent = new Intent(EditNoteActivity.this, NotesActivity.class);
                        newIntent.putExtra("NoteID", oldNoteID);
                        newIntent.putExtra("requestCode", 1218);
                        newIntent.putExtra("resultCode", Activity.RESULT_OK);
                        newIntent.putExtra("activityNumber", 39);
                        newIntent.putExtra("initialActivity", 34);
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
        //We get the activity results checked and then we store them in a checkBundle
        CheckBundle checkBundle = checkResult();
        //
        int activityNumber = checkBundle.getActivityNumber();
        if (activityNumber == 33 || activityNumber == 34|| activityNumber == 40) {
            try {
                oldNoteID = checkBundle.getObject_ID();
                toEditNote = sqLiteDAO.getNoteById(oldNoteID);
                oldTitle = toEditNote.getNoteTitle();
                oldText = toEditNote.getNoteText();
                oldDate = toEditNote.getNoteDate();
                oldCategory1 = toEditNote.getCategory1();
                oldCategory2 = toEditNote.getCategory2();
                oldCategory3 = toEditNote.getCategory3();
                oldMediaBitmap = toEditNote.getMediaBitmaps();
            } catch (Exception ignore) {}
            //
            switch (activityNumber) {
                case (33):
                    initialActivity = 33;
                    break;
                case (34):
                    initialActivity = 34;
                    break;
                case (40):
                    Intent editIntent = getIntent();
                    Bundle editBundle = editIntent.getExtras();
                    try {
                        initialActivity = editBundle.getInt("initialActivity");
                    } catch (Exception ignore) {}
                    break;
            }
            //
        } else {
            Log.e("System.err", "EditNoteActivity.class was accessed from an unsupported Activity.");
        }
    }

    public CheckBundle checkResult() {
        Intent editIntent = getIntent();
        editBundle = editIntent.getExtras();
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
            if (requestCode == 1217 || requestCode == 1218) {
                if (resultCode == Activity.RESULT_OK) {
                    int noteID = -1;
                    try {
                        noteID = editBundle.getInt("NoteID");
                    } catch (Exception e) {
                        Log.e("System.err","Warning 'NoteID' missing!");
                    }
                    checkBundle = new CheckBundle(noteID, requestCode, resultCode, activityNumber);

                    return checkBundle;
                }
            }
        } else {
            Log.e("System.err","Intent Extras Bundle not checked correctly!");
        }
        return new CheckBundle(-1, -1, -1, -1);
    }

    private void onSetTags() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        executorService = Executors.newCachedThreadPool(privilegedFactory);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Intent tagsIntent = new Intent(EditNoteActivity.this,TagsActivity.class);
                tagsIntent.putExtra("NoteID", oldNoteID);
                tagsIntent.putExtra("requestCode", 1218);
                tagsIntent.putExtra("resultCode", Activity.RESULT_OK);
                tagsIntent.putExtra("activityNumber", 36);
                tagsIntent.putExtra("initialActivity", editBundle.getInt("activityNumber"));
                startActivity(tagsIntent);
            }
        });
    }
    private void onNoteSaved() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, HH:mm");

        String newNoteTitle = editNewTitle.getText().toString();
        String newNoteText = editNewText.getText().toString();

        Note oldNote = new Note(oldNoteID, oldTitle, oldText, oldDate, oldCategory1, oldCategory2, oldCategory3, oldMediaBitmap);

        if(newNoteText.isEmpty()) {
            new AlertDialog.Builder(this)
                .setMessage("The text box is empty, do you wish to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteDAO.deleteNote(oldNote);
                        onExitEditNote();
                    }
                })
                .setNegativeButton("No",null)
                .show();
        } else {
            Date newDate = new Date();
            oldNote.setID(oldNoteID);
            oldNote.setNoteTitle(newNoteTitle);
            oldNote.setNoteText(newNoteText);
            oldNote.setNoteDate(simpleDateFormat.format(newDate));
            oldNote.setCategory1(oldCategory1);
            oldNote.setCategory2(oldCategory2);
            oldNote.setCategory3(oldCategory3);
            oldNote.setMediaBitmaps(oldMediaBitmap);
            if (!mediaList.isEmpty()) {
                oldMediaBitmap = "";
                for (Media currentMedia:mediaList) {
                    oldMediaBitmap = oldMediaBitmap.concat((currentMedia.getID()) + "|");
                }
            } else {
                oldMediaBitmap = null;
            }
            oldNote.setMediaBitmaps(oldMediaBitmap);
            //
            oldNoteID = sqLiteDAO.updateNote(oldNote);
            ifSaveNotePressed = true;
        }

    }

    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(EditNoteActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(EditNoteActivity.this, SettingsActivity.class);
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
        if(editNotesDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            editNotesDrawerLayout.closeDrawer(GravityCompat.START);
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
            Intent registerIntent = new Intent(EditNoteActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(EditNoteActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(EditNoteActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(EditNoteActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(EditNoteActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(EditNoteActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(EditNoteActivity.this, AboutUsActivity.class));
            finish();
        }
        editNotesDrawerLayout.closeDrawer(GravityCompat.START);
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
        executorService.shutdown();
        if (imageHorizontalAdapter != null) {
            imageHorizontalAdapter.onDestroy();
        }
    }
}

