package ro.rolandrosoga.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Adapter.MockTagAdapter;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Helper.CheckBundle;
import ro.rolandrosoga.Helper.HelperFunctions;
import ro.rolandrosoga.Helper.SETTagsHelper;
import ro.rolandrosoga.Listener.TagListener;
import ro.rolandrosoga.Mock.Note;
import ro.rolandrosoga.Mock.Tag;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class TagsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnDismissListener {

    TextView burgerMenuText, tagsMainText, headerFirstName;
    DrawerLayout tagsDrawerLayout;
    NavigationView tagsNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout, galleryLinearLayout;
    ImageButton burgerMenuImageButton, toolbarSaveTags, toolbarExitTags, settingsButton, addNewTagButton, pomodoroButton, galleryButton;
    ImageView headerProfileImage;
    FloatingActionButton calendarView;
    Note toEditNote;
    Task toEditTask;
    int objectID, initialActivity = -1;
    CheckBundle checkBundle;
    private MockTagAdapter mockTagAdapter;
    RecyclerView tagsRecycler;
    ArrayList<Tag> allTagsList = new ArrayList<>();
    ArrayList<Tag> chosenTagsList = new ArrayList<>();
    ArrayList<Integer> setTags = new ArrayList<>();
    ConstraintLayout addNewTagConstraint;
    Context context;
    SQLiteDAO sqLiteDAO;
    View profileView;
    User currentUser;
    private Bundle editBundle;
    private boolean onTagsSavedPressed = false;
    private ExecutorService tagsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);

        context = TagsActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        initializeUI();
        /*---------------------------------Middle---------------------------------------------*/
        ActionBarDrawerToggle tagsToggle = new ActionBarDrawerToggle(this,tagsDrawerLayout, R.string.open,R.string.close);
        tagsDrawerLayout.addDrawerListener(tagsToggle);
        tagsToggle.syncState();
        tagsNavigationView.setNavigationItemSelectedListener(this);
        tagsNavigationView.bringToFront();
        tagsNavigationView.setNavigationItemSelectedListener(this);
        profileView = tagsNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);
        //

        checkSenderActivity();
        setInitialTags();
        updateTagsRecycler();
        initializeComponents();

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
    }

    private void initializeUI() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        tagsService = Executors.newFixedThreadPool(60, privilegedFactory);

        /*-------------------------Variable Definitions---------------------------------------*/
        burgerMenuText = findViewById(R.id.burger_menu_text);
        tagsDrawerLayout = findViewById(R.id.drawer_layout_tags);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        tagsNavigationView = findViewById(R.id.sidebar_navigation_view_tags);
        toolbarSaveTags = findViewById(R.id.toolbar_save_tags);
        toolbarExitTags = findViewById(R.id.toolbar_exit_tags);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        calendarView = findViewById(R.id.calendarView);
        tagsRecycler = findViewById(R.id.tags_recycle_view);
        tagsMainText = findViewById(R.id.tags_main_text);

        addNewTagConstraint = findViewById(R.id.add_new_tag_constraint);
        addNewTagButton = findViewById(R.id.add_new_tag_imageButton);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);

        /*---------------------------------Top------------------------------------------------*/

        tagsService.submit(new Runnable() {
            @Override
            public void run() {
                toolbarSaveTags.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSaveTags();
                    }
                });
                toolbarExitTags.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onExitTags();
                    }
                });
                /*--------------------------------Bottom----------------------------------------------*/
                //mainBottomBar
                burgerMenuText.setText("Tags");
                burgerMenuImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagsDrawerLayout.open();
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
                //
                addNewTagConstraint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAddTagClick();
                    }
                });
                addNewTagButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAddTagClick();
                    }
                });
            }
        });
    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(TagsActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(TagsActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 40);
        startActivity(newIntent);
        finish();
    }

    private void onAddTagClick() {
        SQLiteDAO sqLiteDAO = new SQLiteDAO(this);
        Tag newTag = new Tag(0, "New Tag","FFFFFF");
        sqLiteDAO.addTag(newTag);
        updateTagsRecycler();
    }

    public void setInitialTags() {
        if (editBundle != null) {
            objectID = checkBundle.getObject_ID();
            if (objectID != -1) {
                if (checkBundle.getRequestCode() == 1217 || checkBundle.getRequestCode() == 1218) {
                    toEditNote = sqLiteDAO.getNoteById(objectID);
                    if (toEditNote.getCategory1() != null) {
                        Tag tag1 = sqLiteDAO.getTagByCategory(toEditNote.getCategory1());
                        SETTagsHelper.getInstance().addSETTag(tag1.getTagID());
                    }
                    if (toEditNote.getCategory2() != null) {
                        Tag tag2 = sqLiteDAO.getTagByCategory(toEditNote.getCategory2());
                        SETTagsHelper.getInstance().addSETTag(tag2.getTagID());
                    }
                    if (toEditNote.getCategory3() != null) {
                        Tag tag3 = sqLiteDAO.getTagByCategory(toEditNote.getCategory3());
                        SETTagsHelper.getInstance().addSETTag(tag3.getTagID());
                    }

                    updateTagsRecycler();
                } else if (checkBundle.getRequestCode() == 2217 || checkBundle.getRequestCode() == 2218) {
                    toEditTask = sqLiteDAO.getTaskById(objectID);
                    if (toEditTask.getCategory1() != null) {
                        Tag tag1 = sqLiteDAO.getTagByCategory(toEditTask.getCategory1());
                        SETTagsHelper.getInstance().addSETTag(tag1.getTagID());
                    }
                    if (toEditTask.getCategory2() != null) {
                        Tag tag2 = sqLiteDAO.getTagByCategory(toEditTask.getCategory2());
                        SETTagsHelper.getInstance().addSETTag(tag2.getTagID());
                    }
                    if (toEditTask.getCategory3() != null) {
                        Tag tag3 = sqLiteDAO.getTagByCategory(toEditTask.getCategory3());
                        SETTagsHelper.getInstance().addSETTag(tag3.getTagID());
                    }

                    updateTagsRecycler();
                } else {
                    Log.e("System.err", "Error! Request Code is not known.!");
                }
            }
        }
    }

    private void onSaveTags() {
        if (editBundle != null) {
            if (objectID != -1) {
                if (!onTagsSavedPressed) {
                    if (checkBundle.getRequestCode() == 1217 || checkBundle.getRequestCode() == 1218) {
                        toEditNote = sqLiteDAO.getNoteById(objectID);
                        setTags = SETTagsHelper.getInstance().getSetTagList();
                        chosenTagsList = new ArrayList<>();
                        for (Integer storedTagID:setTags) {
                            Tag newTag = sqLiteDAO.getTagById(storedTagID);
                            chosenTagsList.add(newTag);
                        }
                        if (chosenTagsList.size() == 1) {
                            toEditNote.setCategory1(chosenTagsList.get(0).getTagCategory());
                            toEditNote.setCategory2(null);
                            toEditNote.setCategory3(null);
                        } else if (chosenTagsList.size() == 2) {
                            toEditNote.setCategory1(chosenTagsList.get(0).getTagCategory());
                            toEditNote.setCategory2(chosenTagsList.get(1).getTagCategory());
                            toEditNote.setCategory3(null);
                        } else if (chosenTagsList.size() == 3) {
                            toEditNote.setCategory1(chosenTagsList.get(0).getTagCategory());
                            toEditNote.setCategory2(chosenTagsList.get(1).getTagCategory());
                            toEditNote.setCategory3(chosenTagsList.get(2).getTagCategory());
                        }
                        onTagsSavedPressed = true;
                        objectID = sqLiteDAO.updateNote(toEditNote);
                    } else if (checkBundle.getRequestCode() == 2217 || checkBundle.getRequestCode() == 2218) {
                        toEditTask = sqLiteDAO.getTaskById(objectID);
                        setTags = SETTagsHelper.getInstance().getSetTagList();
                        chosenTagsList = new ArrayList<>();
                        for (Integer storedTagID:setTags) {
                            Tag newTag = sqLiteDAO.getTagById(storedTagID);
                            chosenTagsList.add(newTag);
                        }
                        if (chosenTagsList.size() == 1) {
                            toEditTask.setCategory1(chosenTagsList.get(0).getTagCategory());
                        } else if (chosenTagsList.size() == 2) {
                            toEditTask.setCategory1(chosenTagsList.get(1).getTagCategory());
                            toEditTask.setCategory2(chosenTagsList.get(0).getTagCategory());
                        } else if (chosenTagsList.size() == 3) {
                            toEditTask.setCategory1(chosenTagsList.get(2).getTagCategory());
                            toEditTask.setCategory2(chosenTagsList.get(1).getTagCategory());
                            toEditTask.setCategory3(chosenTagsList.get(0).getTagCategory());
                        }
                        onTagsSavedPressed = true;
                        objectID = sqLiteDAO.updateTask(toEditTask);
                    }
                }
            }
        }
    }

    public void onExitTags() {
        if (editBundle != null) {
            if (checkBundle.getObject_ID() != -1) {
                ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
                tagsService = Executors.newCachedThreadPool(privilegedFactory);
                if (checkBundle.getRequestCode() == 1217 || checkBundle.getRequestCode() == 1218) {
                    SETTagsHelper.getInstance().setSETTagsToZero();
                    Intent newIntent = new Intent(TagsActivity.this, EditNoteActivity.class);
                    newIntent.putExtra("NoteID", objectID);
                    newIntent.putExtra("requestCode", checkBundle.getRequestCode());
                    newIntent.putExtra("resultCode", checkBundle.getResultCode());
                    newIntent.putExtra("activityNumber", 40);
                    newIntent.putExtra("initialActivity", initialActivity);
                    startActivity(newIntent);
                    finish();
                } else if (checkBundle.getRequestCode() == 2217 || checkBundle.getRequestCode() == 2218) {
                    SETTagsHelper.getInstance().setSETTagsToZero();
                    Intent newIntent = new Intent(TagsActivity.this, EditTaskActivity.class);
                    newIntent.putExtra("TaskID", objectID);
                    newIntent.putExtra("requestCode", checkBundle.getRequestCode());
                    newIntent.putExtra("resultCode", checkBundle.getResultCode());
                    newIntent.putExtra("activityNumber", 40);
                    newIntent.putExtra("initialActivity", initialActivity);
                    startActivity(newIntent);
                    finish();
                }
            } else {
                onBackPressed();
            }
        }
    }

    public void updateTagsRecycler() {
        allTagsList = sqLiteDAO.getAllTags();
        setTags = SETTagsHelper.getInstance().getSetTagList();
        mockTagAdapter = new MockTagAdapter(this, allTagsList, setTags);
        mockTagAdapter.setTags(allTagsList);
        tagsRecycler.setAdapter(mockTagAdapter);
        tagsRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        tagsRecycler.setHasFixedSize(true);
    }

    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(TagsActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(TagsActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
        finish();
    }

    public void checkSenderActivity() {
        //We get the activity results checked and then we store them in a checkBundle
        checkBundle = checkResult();
        objectID = checkBundle.getObject_ID();
        //
        if (objectID != -1) {
            if (checkBundle.getActivityNumber() == 35 || checkBundle.getActivityNumber() == 36
            || checkBundle.getActivityNumber() == 38 || checkBundle.getActivityNumber() == 39) {
                if (checkBundle.getActivityNumber() == 35 || checkBundle.getActivityNumber() == 36) {
                    toEditNote = sqLiteDAO.getNoteById(objectID);
                } else {
                    toEditTask = sqLiteDAO.getTaskById(objectID);
                }
            } else {
                Log.e("System.err", "EditNoteActivity.class was accessed from an unsupported Activity.");
            }
        }
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
                    try {
                        initialActivity = editBundle.getInt("initialActivity");
                    } catch (Exception e) {
                        Log.e("System.err", "Warning 'initialActivity' missing!");
                    }
                    checkBundle = new CheckBundle(noteID, requestCode, resultCode, activityNumber);
                    return checkBundle;
                }
            } else if (requestCode == 2217 || requestCode == 2218) {
                if (resultCode == Activity.RESULT_OK) {
                    int taskID = -1;
                    try {
                        taskID = editBundle.getInt("TaskID");
                    } catch (Exception e) {
                        Log.e("System.err", "Warning 'TaskID' missing!");
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
        }
        return new CheckBundle(-1, -1, -1, -1);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if(tagsDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            tagsDrawerLayout.closeDrawer(GravityCompat.START);
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
            Intent registerIntent = new Intent(TagsActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(TagsActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(TagsActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(TagsActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(TagsActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(TagsActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(TagsActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(TagsActivity.this, AboutUsActivity.class));
            finish();
        }
        tagsDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private final TagListener tagListener = new TagListener() {
        @Override
        public void onClick(Tag note) {}
        @Override
        public void onLongClick(Tag tag, CardView cardView) {}
        @Override
        public void onItemClick(View itemView, int position) {}
    };
    @Override
    public void onDismiss(DialogInterface dialog) {
        updateTagsRecycler();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tagsService.shutdown();
        if (mockTagAdapter != null) {
            mockTagAdapter.onDestroy();
        }
    }

}
