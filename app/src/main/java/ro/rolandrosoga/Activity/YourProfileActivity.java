package ro.rolandrosoga.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Scanner;

import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Helper.HelperFunctions;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class YourProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView burgerMenuText, headerFirstName;
    Toolbar toolbar;
    DrawerLayout yourProfileDrawerLayout;
    NavigationView settingsNavigationView;
    LinearLayout burgerMenuLinearLayout, settingsLayout, pomodoroLinearLayout, galleryLinearLayout;
    ImageButton burgerMenuImageButton, toolbarExitSettings, settingsButton, toolbarSaveSettings, pomodoroButton, galleryButton;
    ImageView profilePicture, headerProfileImage;
    FloatingActionButton calendarView;
    TextView textUsername, textPassword, textFullName, textEmail, textPhoneNumber;
    Context context;
    SQLiteDAO sqLiteDAO;
    ActivityResultLauncher<Intent> openGallery;
    Bitmap currentBitmap;
    Uri currentUri;
    private User currentUser;
    View profileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_profile);

        context = YourProfileActivity.this;
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);

        /*-------------------------Variable Definitions---------------------------------------*/
        toolbar = findViewById(R.id.settings_toolbar);
        burgerMenuText = findViewById(R.id.burger_menu_text);
        yourProfileDrawerLayout = findViewById(R.id.drawer_layout_settings);
        burgerMenuLinearLayout = findViewById(R.id.burger_menu_linear_layout);
        burgerMenuImageButton = findViewById(R.id.burger_menu_Button);
        settingsNavigationView = findViewById(R.id.sidebar_navigation_view_settings);
        toolbarSaveSettings = findViewById(R.id.toolbar_save_settings);
        toolbarExitSettings = findViewById(R.id.toolbar_exit_settings);
        settingsLayout = findViewById(R.id.settings_layout);
        settingsButton = findViewById(R.id.settings_button);
        calendarView = findViewById(R.id.calendarView);

        //
        textUsername = findViewById(R.id.text_username);
        textPassword = findViewById(R.id.text_password);
        textFullName = findViewById(R.id.text_full_name);
        textEmail = findViewById(R.id.text_email);
        textPhoneNumber = findViewById(R.id.text_phone_number);
        pomodoroLinearLayout = findViewById(R.id.pomodoro_linear_layout);
        pomodoroButton = findViewById(R.id.pomodoro_Button);
        profilePicture = findViewById(R.id.profile_picture);
        galleryButton = findViewById(R.id.gallery_button);
        galleryLinearLayout = findViewById(R.id.gallery_linearLayout);
        /*---------------------------------Top------------------------------------------------*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbarExitSettings.setOnClickListener(new View.OnClickListener() {
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
                yourProfileDrawerLayout.open();
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
        ActionBarDrawerToggle editNotesToggle = new ActionBarDrawerToggle(this,yourProfileDrawerLayout, R.string.open,R.string.close);
        yourProfileDrawerLayout.addDrawerListener(editNotesToggle);
        editNotesToggle.syncState();
        settingsNavigationView.setNavigationItemSelectedListener(this);
        settingsNavigationView.bringToFront();
        settingsNavigationView.setNavigationItemSelectedListener(this);
        profileView = settingsNavigationView.getHeaderView(0);
        headerFirstName = profileView.findViewById(R.id.sidebar_header_textview_username);
        headerProfileImage = profileView.findViewById(R.id.sidebar_header_imageview_user);
        //
        initializeComponents();
    }

    private void initializeComponents() {
        //Initializing the SQLiteDAO
        //
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
        textPassword.setText(currentUser.getUserPassword());
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
        if (currentUser.getProfile_image() != null) {
            byte[] profileBytes = currentUser.getProfile_image();
            Bitmap profileBitmap = HelperFunctions.bytesArrayToBitmap(profileBytes);
            profilePicture.setImageBitmap(profileBitmap);
        }
        toolbarSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.setUserUsername(textUsername.getText().toString());
                currentUser.setUserPassword(textPassword.getText().toString());
                currentUser.setUser_full_name(textFullName.getText().toString());
                currentUser.setUser_email(textEmail.getText().toString());
                currentUser.setUser_phone_number(textPhoneNumber.getText().toString());
                currentUser.setUser_google_sign_in(false);
                currentUser.setWork_time(currentUser.getWork_time());
                currentUser.setFree_time(currentUser.getFree_time());
                currentUser.setSave_pomodoro_as_task(currentUser.isSave_pomodoro_as_task());
                if (currentBitmap != null) {
                    currentUser.setProfile_image(HelperFunctions.bitmapToBytesArray(currentBitmap));
                } else {
                    currentUser.setProfile_image(null);
                }
                sqLiteDAO.updateUser(currentUser);
            }
        });
        setResultLauncher();
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperFunctions.isPhotoPickerAvailable()) {
                    saveFromGallery();
                }
            }
        });
        profilePicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteProfilePictureChoice();
                return true;
            }
        });
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deleteProfilePictureChoice() {
        String deleteString = "<font color=#6ebef0>Delete </font>";
        String questionMark = "<font color=#ffffff> ?</font>";
        new AlertDialog.Builder(this)
            .setMessage(Html.fromHtml(deleteString, Html.FROM_HTML_MODE_LEGACY) +  "Profile Picture" + Html.fromHtml(questionMark, Html.FROM_HTML_MODE_LEGACY))
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteFromDatabase();
                }
            })
            .setNegativeButton("No", null)
            .show();
    }

    private void deleteFromDatabase() {
        currentUser.setProfile_image(null);
        profilePicture.setImageBitmap(null);
        currentBitmap = null;
        profilePicture.setTag(R.drawable.base_image);
        sqLiteDAO.updateUser(currentUser);
    }

    private void saveFromGallery() {
        Intent saveFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        openGallery.launch(saveFromGallery);
    }
    private void setResultLauncher() {
        openGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        currentUri = result.getData().getData();
                        currentBitmap = uriToBitmap(currentUri);
                        profilePicture.setImageBitmap(currentBitmap);
                        currentUser.setProfile_image(HelperFunctions.bitmapToBytesArray(currentBitmap));
                    } catch (Exception e) {
                        Toast.makeText(YourProfileActivity.this, "No Image Picked", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void onGalleryClick() {
        Intent newIntent = new Intent(YourProfileActivity.this, GalleryActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        startActivity(newIntent);
        finish();
    }

    private void onPomodoroClick() {
        Intent newIntent = new Intent(YourProfileActivity.this, PomodoroActivity.class);
        newIntent.putExtra("resultCode", Activity.RESULT_OK);
        newIntent.putExtra("activityNumber", 48);
        startActivity(newIntent);
        finish();
    }

    private void onCalendarActivityClick() {
        Intent settingIntent = new Intent(YourProfileActivity.this, CalendarActivity.class);
        startActivity(settingIntent);
        finish();
    }

    private void onSettingsClick() {
        Intent settingIntent = new Intent(YourProfileActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
        finish();
    }

    public void finish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if(yourProfileDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            yourProfileDrawerLayout.closeDrawer(GravityCompat.START);
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
            Intent registerIntent = new Intent(YourProfileActivity.this, LoginActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }
    private void userSignedOut() {
        FirebaseAuth.getInstance().signOut();
        Intent registerIntent = new Intent(YourProfileActivity.this, LoginActivity.class);
        startActivity(registerIntent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if(itemId == R.id.sidebar_home) {
            startActivity(new Intent(YourProfileActivity.this, MainActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_notes) {
            startActivity(new Intent(YourProfileActivity.this, NotesActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_task) {
            startActivity(new Intent(YourProfileActivity.this, TasksActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_tags) {
            startActivity(new Intent(YourProfileActivity.this, TagsActivity.class));
            finish();
        } else if (itemId == R.id.sidebar_profile_about) {
            startActivity(new Intent(YourProfileActivity.this, YourProfileActivity.class));
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
            startActivity(new Intent(YourProfileActivity.this, AboutUsActivity.class));
            finish();
        }
        yourProfileDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



}
