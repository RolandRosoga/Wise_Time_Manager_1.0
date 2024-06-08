package ro.rolandrosoga.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Adapter.MockImageAdapter;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Helper.HelperFunctions;
import ro.rolandrosoga.Listener.ImageListener;
import ro.rolandrosoga.Mock.Media;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class GalleryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView burgerMenuText, headerFirstName;
    DrawerLayout galleryDrawerLayout;
    NavigationView galleryNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout, toolbar, galleryLinearLayout;
    ImageButton buttonOpenTaskMenu, buttonAddFromGallery, buttonCloseTaskMenu, burgerMenuImageButton, toolbarExitGallery, settingsButton, pomodoroButton, galleryButton;
    ImageView headerProfileImage;
    FloatingActionButton calendarView;
    ConstraintLayout buttonTaskMenu;
    private Context context;
    SQLiteDAO sqLiteDAO;
    User currentUser;
    View profileView;
    List<Uri> uriList = new ArrayList<>();
    List<Bitmap> bitmapList = new ArrayList<>();
    List<Media> mediaList = new ArrayList<>();
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    RecyclerView galleryRecyclerView;
    MockImageAdapter imageVerticalAdapter;
    private ExecutorService galleryService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        context = GalleryActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        mediaList = sqLiteDAO.getAllMedia();

        initializeUI();
        /*---------------------------------Middle---------------------------------------------*/
        ActionBarDrawerToggle galleryToggle = new ActionBarDrawerToggle(this,galleryDrawerLayout, R.string.open,R.string.close);
        galleryDrawerLayout.addDrawerListener(galleryToggle);
        galleryToggle.syncState();
        galleryNavigationView.setNavigationItemSelectedListener(this);
        galleryNavigationView.bringToFront();
        galleryNavigationView.setNavigationItemSelectedListener(this);
        profileView = galleryNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);
        initializeComponents();
    }

    private void initializeComponents() {
        currentUser = AuthHelper.getInstance().getCurrentUser();
        String firstname = currentUser.getUser_full_name();
        Scanner spaceSearcher = new Scanner(firstname);
        firstname = spaceSearcher.next();
        headerFirstName.setText(firstname);
        if (currentUser.getProfile_image() != null) {
            headerProfileImage.setImageBitmap(HelperFunctions.bytesArrayToBitmap(currentUser.getProfile_image()));
        }
        pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                if (!uris.isEmpty()) {
                    uriList = uris;
                    List<Bitmap> currentBitmapList = uriListToBitmapList(uriList);
                    bitmapArrayToMediaArray(currentBitmapList);
                    bitmapList.addAll(currentBitmapList);
                    updateImagesRecycler();
                } else {
                    Toast.makeText(getApplicationContext(), "No image selected!", Toast.LENGTH_SHORT).show();
                }
            });
        initializeImagesAdapter();
    }
    private void initializeUI() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        galleryService = Executors.newFixedThreadPool(60, privilegedFactory);

        /*-------------------------Variable Definitions---------------------------------------*/
        toolbar = findViewById(R.id.gallery_toolbar);
        burgerMenuText = findViewById(R.id.burger_menu_text);
        galleryDrawerLayout = findViewById(R.id.drawer_layout_gallery);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        galleryNavigationView = findViewById(R.id.sidebar_navigation_view_gallery);
        toolbarExitGallery = findViewById(R.id.toolbar_exit_gallery);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        calendarView = findViewById(R.id.calendarView);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);
        galleryRecyclerView = findViewById(R.id.gallery_recyclerview);
        buttonAddFromGallery = findViewById(R.id.button_add_picture_from_gallery);
        buttonOpenTaskMenu = findViewById(R.id.button_open_task_menu);
        buttonTaskMenu = findViewById(R.id.button_task_menu);
        buttonCloseTaskMenu = findViewById(R.id.button_close_task_menu);

        galleryService.submit(new Runnable() {
            @Override
            public void run() {
                /*---------------------------------Top------------------------------------------------*/
                toolbarExitGallery.setOnClickListener(new View.OnClickListener() {
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
                        galleryDrawerLayout.open();
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

    private void initializeImagesAdapter() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        galleryService = Executors.newFixedThreadPool(20, privilegedFactory);

        galleryService.submit(new Runnable() {
            @Override
            public void run() {
                if (!mediaList.isEmpty()) {
                    mediaArrayToBitmapArray();
                    updateImagesRecycler();
                }
            }
        });
    }

    private void mediaArrayToBitmapArray() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        galleryService = Executors.newCachedThreadPool(privilegedFactory);

        galleryService.submit(new Runnable() {
            @Override
            public void run() {
                for (Media newMedia:mediaList) {
                    bitmapList.add(HelperFunctions.bytesArrayToBitmap(newMedia.getMedia_blob()));
                }
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
        imageVerticalAdapter = new MockImageAdapter(GalleryActivity.this, bitmapList, mediaList, imageListener);
        galleryRecyclerView.setAdapter(imageVerticalAdapter);
        galleryRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        galleryRecyclerView.setHasFixedSize(true);
    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(GalleryActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(GalleryActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 35);
        startActivity(newIntent);
        finish();
    }


    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(GalleryActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(GalleryActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(galleryDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            galleryDrawerLayout.closeDrawer(GravityCompat.START);
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
            Intent registerIntent = new Intent(GalleryActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(GalleryActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(GalleryActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(GalleryActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(GalleryActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(GalleryActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(GalleryActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(GalleryActivity.this, AboutUsActivity.class));
            finish();
        }
        galleryDrawerLayout.closeDrawer(GravityCompat.START);
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
        galleryService.shutdown();
        if (imageVerticalAdapter != null) {
            imageVerticalAdapter.onDestroy();
        }
    }
}

