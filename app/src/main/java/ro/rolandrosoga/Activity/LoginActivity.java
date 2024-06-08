package ro.rolandrosoga.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Helper.AuthHelper;
import ro.rolandrosoga.Mock.User;
import ro.rolandrosoga.R;

public class LoginActivity extends AppCompatActivity {
    private static final String ONLY_OPENED_ONCE = "only_opened_once";
    private EditText editTextUsername, editTextPassword;
    private ConstraintLayout constraintForgotPassword, buttonSignIn, buttonCreateAnAccount;
    FirebaseAuth authenticationForFirebase;
    FirebaseUser currentFirebaseUser;
    String userEmail, userPassword;
    SQLiteDAO sqLiteDAO;
    Context context;
    User currentSQLUser;
    boolean startedBefore, isFirstTime, accountExists;
    Bundle editBundle;
    ExecutorService loginService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = LoginActivity.this;
        if (SQLiteDAO.enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);
        /*------------------------Check if used First Time------------------------------------*/
        try {
            currentSQLUser = sqLiteDAO.getUserById(1);
            currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        } catch (Exception ignore) {}
        if (currentSQLUser != null && currentFirebaseUser != null) {
            AuthHelper.getInstance().setLoggedIn(true);
            AuthHelper.getInstance().setCurrentUser(currentSQLUser);
            Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(registerIntent);
            finish();
        } else {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            startedBefore = settings.getBoolean(ONLY_OPENED_ONCE, false);
            if (!startedBefore) {
                Intent intent = new Intent(this, IntroApplicationActivity.class);
                startActivity(intent);
                finish();
            } else {
                try {
                    isFirstTime = editBundle.getBoolean("isFirstTime", false);
                } catch (Exception ignore) {}
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(ONLY_OPENED_ONCE, true);
                editor.apply();
                setContentView(R.layout.activity_login);
                initializeAllComponents(savedInstanceState);
            }
        }
    }

    private void initializeAllComponents(Bundle savedInstanceState) {
        //
        authenticationForFirebase = FirebaseAuth.getInstance();
        currentFirebaseUser = authenticationForFirebase.getCurrentUser();
        /*-------------------------Variable Definitions---------------------------------------*/
        editTextUsername = findViewById(R.id.edittext_username);
        editTextPassword = findViewById(R.id.edittext_password);
        constraintForgotPassword = findViewById(R.id.constraint_forgot_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        buttonCreateAnAccount = findViewById(R.id.button_create_an_account);
        //
        bind(savedInstanceState);
    }

    private void bind(Bundle savedInstanceState) {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        loginService = Executors.newFixedThreadPool(80, privilegedFactory);

        loginService.submit(new Runnable() {
            @Override
            public void run() {
                constraintForgotPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent forgotPasswordIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://radurosoga.ro/password-validation"));
                        startActivity(forgotPasswordIntent);
                    }
                });
                buttonCreateAnAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(registerIntent);
                    }
                });
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = editTextUsername.getText().toString();
                userPassword = editTextPassword.getText().toString();
                if (sqLiteDAO.getUserCount() > 0) {
                    try {
                        currentSQLUser = sqLiteDAO.getUserByEmail(userEmail);
                        if (currentSQLUser.getUserPassword().equals(userPassword)) {
                            AuthHelper.getInstance().setLoggedIn(true);
                            AuthHelper.getInstance().setCurrentUser(currentSQLUser);
                            //
                            Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(loginIntent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Password is wrong..", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ignore) {
                        Toast.makeText(LoginActivity.this, "No user with this email saved to database.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (checkFirebaseDatabase()) {
                        setUserToDatabase();

                        Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(registerIntent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "No Firebase User registered.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private boolean checkFirebaseDatabase() {
        authenticationForFirebase.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        currentFirebaseUser = authenticationForFirebase.getCurrentUser();
                        accountExists = true;
                    } else {
                        accountExists = false;
                    }
                }
            });
        return accountExists;
    }

    private void setUserToDatabase() {
        currentSQLUser = new User();
        currentSQLUser.setID(0);
        currentSQLUser.setUserUsername("none");
        currentSQLUser.setUserPassword(userPassword);
        currentSQLUser.setUser_full_name("name");
        currentSQLUser.setUser_email(userEmail);
        currentSQLUser.setUser_phone_number(null);
        currentSQLUser.setUser_google_sign_in(false);
        currentSQLUser.setWork_time(25);
        currentSQLUser.setFree_time(5);
        currentSQLUser.setSave_pomodoro_as_task(true);
        currentSQLUser.setProfile_image(null);
        int userID = sqLiteDAO.addUserGetID(currentSQLUser);
        currentSQLUser.setID(userID);
        //
        AuthHelper.getInstance().setLoggedIn(true);
        AuthHelper.getInstance().setCurrentUser(currentSQLUser);
    }

    @Override
    public void onStart() {
        super.onStart();
        currentFirebaseUser = authenticationForFirebase.getCurrentUser();
        if (currentFirebaseUser != null) {
            AuthHelper.getInstance().setLoggedIn(true);
            AuthHelper.getInstance().setCurrentUser(currentSQLUser);
            Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(registerIntent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginService != null) {
            loginService.shutdown();
        }
    }
}
