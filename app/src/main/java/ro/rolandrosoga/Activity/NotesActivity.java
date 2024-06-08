package ro.rolandrosoga.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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

import ro.rolandrosoga.Adapter.MockNoteAdapter;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Helper.CheckBundle;
import ro.rolandrosoga.Helper.HelperFunctions;
import ro.rolandrosoga.Listener.NoteListener;
import ro.rolandrosoga.Mock.Note;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class NotesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout notesDrawerLayout;
    NavigationView notesNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, toolbarLayout, searchViewLayout, pomodoroLinearLayout, galleryLinearLayout;
    ImageButton burgerMenuImageButton, backButtonNotes, settingsButton, searchButtonNotes, pomodoroButton, galleryButton;
    ImageView headerProfileImage;
    TextView burgerMenuText, headerFirstName;
    ConstraintLayout addNewNoteButton;
    MockNoteAdapter notesVerticalAdapter;
    List<Note> notesList = new ArrayList<>();
    RecyclerView notesRecycler;
    FloatingActionButton calendarView;
    SearchView notesSearchView;
    List<Note> searchedNotesList;
    boolean isSearchViewOpen = false;
    Handler newHandler = new Handler();
    SQLiteDAO sqLiteDAO;
    Context context;
    View profileView;
    User currentUser;
    private ExecutorService notesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        context = NotesActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);
        //

        sqLiteDAO.generateNotesMock();
        sqLiteDAO.generateDefaultTags();
        //

        initializeUI();
        /*---------------------------------Middle---------------------------------------------*/
        ActionBarDrawerToggle notesActionToggle = new ActionBarDrawerToggle(this,notesDrawerLayout, R.string.open,R.string.close);
        notesDrawerLayout.addDrawerListener(notesActionToggle);
        notesActionToggle.syncState();
        notesNavigationView.setNavigationItemSelectedListener(this);
        notesNavigationView.bringToFront();
        notesNavigationView.setNavigationItemSelectedListener(this);
        profileView = notesNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);

        notesVerticalAdapter = new MockNoteAdapter(NotesActivity.this, notesList, noteListener, 34);
        initializeComponents();
    }

    private void initializeComponents() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        notesService = Executors.newCachedThreadPool(privilegedFactory);

        currentUser = AuthHelper.getInstance().getCurrentUser();
        //Header Initialization
        notesService.submit(new Runnable() {
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

    private void initializeUI() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        notesService = Executors.newFixedThreadPool(100, privilegedFactory);

        /*-------------------------Variable Definitions---------------------------------------*/
        addNewNoteButton = findViewById(R.id.add_new_note_constraint);
        burgerMenuText = findViewById(R.id.burger_menu_text);
        notesRecycler = findViewById(R.id.notes_recycler);
        backButtonNotes = findViewById(R.id.back_button_Notes);

        burgerMenuText = findViewById(R.id.burger_menu_text);
        notesDrawerLayout = findViewById(R.id.drawer_layout_notes);
        notesNavigationView = findViewById(R.id.sidebar_navigation_view_notes);

        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);

        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);

        calendarView = findViewById(R.id.calendarView);
        notesSearchView = findViewById(R.id.notes_searchView);
        searchButtonNotes = findViewById(R.id.search_button_notes);
        toolbarLayout = findViewById(R.id.layout_toolbar);
        searchViewLayout = findViewById(R.id.layout_searchview);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);

        notesService.submit(new Runnable() {
            @Override
            public void run() {
                //
                /*---------------------------------Top------------------------------------------------*/
                backButtonNotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackButtonPressed();
                    }
                });
                notesSearchView.clearFocus();
                notesSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                searchButtonNotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isSearchViewOpen) {
                            calendarView.setVisibility(View.GONE);
                            toolbarLayout.setVisibility(View.INVISIBLE);
                            searchViewLayout.setVisibility(View.VISIBLE);
                            notesSearchView.setIconified(false);
                            notesSearchView.setIconifiedByDefault(false);
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
                notesSearchView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isSearchViewOpen) {
                            calendarView.setVisibility(View.GONE);
                            notesSearchView.setIconified(false);
                            notesSearchView.setIconifiedByDefault(false);
                            isSearchViewOpen = true;
                        }
                    }
                });
                /*--------------------------------Bottom----------------------------------------------*/
                //mainBottomBar
                burgerMenuText.setText(R.string.notes_text);
                burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notesDrawerLayout.open();
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
                addNewNoteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addNote();
                    }
                });
            }
        });
    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(NotesActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(NotesActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 34);
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


    public static void KeyboardOpenCheck(final Activity currentActivity) {    final View activityRootView = currentActivity.findViewById(android.R.id.content);    activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new     ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect screenPerimeter = new Rect();
            activityRootView.getWindowVisibleDisplayFrame(screenPerimeter);
            int mainHeight = activityRootView.getRootView().getHeight();
            int difference = mainHeight - screenPerimeter.bottom;
            if (difference > densityIndependentPixelsToPixels(currentActivity, 200)) {
                //Keyboard is open
            }else if (difference < densityIndependentPixelsToPixels(currentActivity, 200)) {
                //Keyboard is close
            }
        }
    });
    }

    private static float densityIndependentPixelsToPixels(Context currentContext, int densityIndependentPixels) {
        DisplayMetrics displayMetrics =  currentContext.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, densityIndependentPixels, displayMetrics);
    }

    private void onSearchViewChanged(String newText) {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        notesService = Executors.newCachedThreadPool(privilegedFactory);

        searchedNotesList = new ArrayList<>();
        notesList = sqLiteDAO.getAllNotes();
        //
        notesService.submit(new Runnable() {
            @Override
            public void run() {
                for (Note note:notesList) {
                    if (note.getNoteTitle() != null && note.getNoteTitle().toLowerCase().contains(newText.toLowerCase())) {
                        searchedNotesList.add(note);
                    } else if (note.getNoteText().toLowerCase().contains(newText.toLowerCase())) {
                        searchedNotesList.add(note);
                    } else if (note.getNoteDate().toLowerCase().contains(newText.toLowerCase())) {
                        searchedNotesList.add(note);
                    } else if (note.getCategory1() != null && note.getCategory1().toLowerCase().contains(newText.toLowerCase())) {
                        searchedNotesList.add(note);
                    }  else if (note.getCategory2() != null && note.getCategory2().toLowerCase().contains(newText.toLowerCase())) {
                        searchedNotesList.add(note);
                    } else if (note.getCategory3() != null && note.getCategory3().toLowerCase().contains(newText.toLowerCase())) {
                        searchedNotesList.add(note);
                    }
                }
                if (!searchedNotesList.isEmpty()) {
                    notesVerticalAdapter.setSearchedNotesList(searchedNotesList);
                }
            }
        });
    }

    public void updateNotesRecycler() {
        notesList = sqLiteDAO.getAllNotes();
        Collections.reverse(notesList);
        notesVerticalAdapter = new MockNoteAdapter(NotesActivity.this, notesList, noteListener, 34);
        notesRecycler.setAdapter(notesVerticalAdapter);
        notesRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        notesRecycler.setHasFixedSize(true);
    }

    private void addNote() {
        Intent addNewNoteIntent = new Intent(NotesActivity.this, AddNoteActivity.class);
        addNewNoteIntent.putExtra("requestCode",1217);
        addNewNoteIntent.putExtra("resultCode", Activity.RESULT_OK);
        addNewNoteIntent.putExtra("activityNumber", 34);
        startActivity(addNewNoteIntent);
        finish();
    }

    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(NotesActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(NotesActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
        finish();
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
            }
        } else {
            Log.e("System.err", "Intent Extras Bundle not checked correctly!");
        }
        return new CheckBundle(-1, -1, -1, -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotesRecycler();
    }

    @Override
    public void onBackPressed() {
        if(notesDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            notesDrawerLayout.closeDrawer(GravityCompat.START);
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
            Intent registerIntent = new Intent(NotesActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(NotesActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(NotesActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(NotesActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(NotesActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(NotesActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(NotesActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(NotesActivity.this, AboutUsActivity.class));
            finish();
        }
        notesDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private final NoteListener noteListener = new NoteListener() {
        @Override
        public void onClick(Note note) {}
        @Override
        public void onLongClick(Note note, CardView cardView) {}
        @Override
        public void onItemClick(View itemView, int position) {}
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        notesService.shutdown();
        if (notesVerticalAdapter != null) {
            notesVerticalAdapter.onDestroy();
        }
    }
}
