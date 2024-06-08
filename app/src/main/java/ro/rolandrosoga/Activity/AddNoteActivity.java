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
import ro.rolandrosoga.Mock.Note;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class AddNoteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView burgerMenuText, headerFirstName;
    DrawerLayout addNotesDrawerLayout;
    NavigationView addNotesNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout, galleryLinearLayout;
    ImageButton buttonAddFromGallery, burgerMenuImageButton, toolbarSaveAddNote, toolbarExitAddNote, settingsButton, buttonSetTags, buttonCloseNoteMenu, pomodoroButton, galleryButton;
    EditText addNewTitle, addNewText;
    Note toAddNote = new Note();
    int toAddNoteID = -1;
    FloatingActionButton buttonOpenNoteMenu, calendarView;
    ConstraintLayout buttonNoteMenu;
    private Context context;
    private SQLiteDAO sqLiteDAO;
    View profileView;
    User currentUser;
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    ImageView headerProfileImage;
    MockImageAdapter imageHorizontalAdapter;
    RecyclerView imagesRecyclerView;
    List<Uri> uriList = new ArrayList<>();
    List<Bitmap> bitmapList = new ArrayList<>();
    List<Media> mediaList = new ArrayList<>();
    private boolean ifSaveNotePressed;
    String oldMediaBitmap;
    private ExecutorService executorService;
    Bundle editBundle;
    CheckBundle checkBundle;
    int activityNumber = 0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        context = AddNoteActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        checkResult();
        initializeUI();
        /*---------------------------------Middle---------------------------------------------*/
        ActionBarDrawerToggle addNotesToggle = new ActionBarDrawerToggle(this,addNotesDrawerLayout, R.string.open,R.string.close);
        addNotesDrawerLayout.addDrawerListener(addNotesToggle);
        addNotesToggle.syncState();
        addNotesNavigationView.setNavigationItemSelectedListener(this);
        addNotesNavigationView.bringToFront();
        addNotesNavigationView.setNavigationItemSelectedListener(this);
        profileView = addNotesNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);
        //
        initializeComponents();
    }

    private void initializeUI() {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        executorService = Executors.newFixedThreadPool(80, privilegedFactory);

        /*-------------------------Variable Definitions---------------------------------------*/
        burgerMenuText = findViewById(R.id.burger_menu_text);
        addNotesDrawerLayout = findViewById(R.id.drawer_layout_add_notes);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        addNotesNavigationView = findViewById(R.id.sidebar_navigation_view_add_notes);
        toolbarSaveAddNote = findViewById(R.id.toolbar_save_add_note);
        toolbarExitAddNote = findViewById(R.id.toolbar_exit_add_note);
        addNewTitle = findViewById(R.id.edit_new_title);
        addNewText = findViewById(R.id.edit_new_text);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        buttonOpenNoteMenu = findViewById(R.id.button_open_note_menu);
        buttonNoteMenu = findViewById(R.id.button_note_menu);
        buttonCloseNoteMenu = findViewById(R.id.button_close_note_menu);
        buttonSetTags = findViewById(R.id.button_set_tags);
        calendarView = findViewById(R.id.calendarView);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);
        imagesRecyclerView = findViewById(R.id.images_recyclerview);
        buttonAddFromGallery = findViewById(R.id.button_save_from_gallery);


        executorService.submit(new Runnable() {
            @Override
            public void run() {
                /*---------------------------------Top------------------------------------------------*/
                toolbarSaveAddNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNoteSaved();
                    }
                });
                toolbarExitAddNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                /*--------------------------------Bottom----------------------------------------------*/
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
                buttonSetTags.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSetTags();
                    }
                });
                //mainBottomBar
                burgerMenuText.setText(R.string.notes_text);
                burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNotesDrawerLayout.open();
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
                buttonAddFromGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (HelperFunctions.isPhotoPickerAvailable()) {
                            addFromGallery();
                        }
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
        imageHorizontalAdapter = new MockImageAdapter(AddNoteActivity.this, bitmapList, mediaList, imageListener);
        imagesRecyclerView.setAdapter(imageHorizontalAdapter);
        imagesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.HORIZONTAL));
        imagesRecyclerView.setHasFixedSize(true);
    }

    @SuppressLint("ClickableViewAccessibility")
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
    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(AddNoteActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(AddNoteActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 35);
        startActivity(newIntent);
        finish();
    }

    private void onNoteSaved() {
        String newNoteTest = addNewText.getText().toString();
        if(newNoteTest.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("The text box is empty, do you wish to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onExitAddNote();
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();
        } else {
            saveAnAddedNote();
        }
    }

    private void saveAnAddedNote() {
        if (!ifSaveNotePressed) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, HH:mm");
            Date newDate = new Date();
            toAddNote.setNoteTitle(addNewTitle.getText().toString());
            toAddNote.setNoteText(addNewText.getText().toString());
            toAddNote.setNoteDate(simpleDateFormat.format(newDate));
            toAddNote.setCategory1(null);
            toAddNote.setCategory2(null);
            toAddNote.setCategory3(null);
            if (!mediaList.isEmpty()) {
                oldMediaBitmap = "";
                for (Media currentMedia:mediaList) {
                    oldMediaBitmap = oldMediaBitmap.concat((currentMedia.getID()) + "|");
                }
            } else {
                oldMediaBitmap = null;
            }
            toAddNote.setMediaBitmaps(oldMediaBitmap);
            //
            toAddNoteID = sqLiteDAO.addNoteGetID(toAddNote);
            toAddNote.setID(toAddNoteID);
            ifSaveNotePressed = true;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, HH:mm");
            Date newDate = new Date();
            toAddNote.setNoteTitle(addNewTitle.getText().toString());
            toAddNote.setNoteText(addNewText.getText().toString());
            toAddNote.setNoteDate(simpleDateFormat.format(newDate));
            if (!mediaList.isEmpty()) {
                oldMediaBitmap = "";
                for (Media currentMedia:mediaList) {
                    oldMediaBitmap = oldMediaBitmap.concat((currentMedia.getID()) + "|");
                }
            } else {
                oldMediaBitmap = null;
            }
            toAddNote.setMediaBitmaps(oldMediaBitmap);
            //
            toAddNoteID = sqLiteDAO.updateNote(toAddNote);
        }
    }

    private void onExitAddNote() {
        if (checkBundle.getRequestCode() == 1217) {
            if (ifSaveNotePressed) {
                if (checkBundle.getActivityNumber() == 33) {
                    Intent newIntent = new Intent(AddNoteActivity.this, MainActivity.class);
                    newIntent.putExtra("NoteID", toAddNoteID);
                    newIntent.putExtra("requestCode", 1217);
                    newIntent.putExtra("resultCode", Activity.RESULT_OK);
                    newIntent.putExtra("activityNumber", 35);
                    newIntent.putExtra("initialActivity", 33);
                    startActivity(newIntent);
                    finish();
                } else if (checkBundle.getActivityNumber() == 34) {
                    Intent newIntent = new Intent(AddNoteActivity.this, NotesActivity.class);
                    newIntent.putExtra("NoteID", toAddNoteID);
                    newIntent.putExtra("requestCode", 1217);
                    newIntent.putExtra("resultCode", Activity.RESULT_OK);
                    newIntent.putExtra("activityNumber", 35);
                    newIntent.putExtra("initialActivity", 34);
                    startActivity(newIntent);
                    finish();
                }
            } else {
                if (toAddNoteID != -1) {
                    sqLiteDAO.deleteNote(toAddNote);
                    onBackPressed();
                }
            }
        } else if (checkBundle.getActivityNumber() != -1) {
            onBackPressed();
        }
    }

    private void onSetTags() {
        onNoteSaved();
        Intent tagsIntent = new Intent(AddNoteActivity.this,TagsActivity.class);
        tagsIntent.putExtra("NoteID", toAddNoteID);
        tagsIntent.putExtra("requestCode", 1217);
        tagsIntent.putExtra("resultCode", Activity.RESULT_OK);
        tagsIntent.putExtra("activityNumber", 35);
        tagsIntent.putExtra("initialActivity", activityNumber);
        startActivity(tagsIntent);
    }

    public CheckBundle checkResult() {
        Intent editIntent = getIntent();
        editBundle = editIntent.getExtras();
        if (editBundle != null) {
            int requestCode = 0;
            int resultCode = 0;
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

    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(AddNoteActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(AddNoteActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(addNotesDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            addNotesDrawerLayout.closeDrawer(GravityCompat.START);
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
            Intent registerIntent = new Intent(AddNoteActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(AddNoteActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(AddNoteActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(AddNoteActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(AddNoteActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(AddNoteActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(AddNoteActivity.this, AboutUsActivity.class));
            finish();
        }
        addNotesDrawerLayout.closeDrawer(GravityCompat.START);
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
