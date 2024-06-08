package ro.rolandrosoga.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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


public class RegisterActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword, editTextFirstLastName, editTextEmail, editTextPhoneNumber;
    private ConstraintLayout buttonCreateAnAccount, buttonHaveAccount;
    FirebaseAuth authenticationForFirebase;
    String userEmail, userPassword;
    SQLiteDAO sqLiteDAO;
    Context context;
    User newUser;
    ExecutorService registerService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //
        authenticationForFirebase = FirebaseAuth.getInstance();
        /*-------------------------Variable Definitions---------------------------------------*/
        editTextUsername = findViewById(R.id.register_editText_username);
        editTextPassword = findViewById(R.id.register_editText_password);
        editTextFirstLastName = findViewById(R.id.register_editText_first_last_name);
        editTextEmail = findViewById(R.id.register_editText_email);
        editTextPhoneNumber = findViewById(R.id.register_editText_phone_number);
        buttonCreateAnAccount = findViewById(R.id.register_constraint_create_an_account);
        buttonHaveAccount = findViewById(R.id.register_constraint_have_an_account);
        //
        context = RegisterActivity.this;
        if (SQLiteDAO.enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);
        //
        bind(savedInstanceState);
    }

    private void bind(Bundle savedInstanceState) {
        ThreadFactory privilegedFactory = Executors.defaultThreadFactory();
        registerService = Executors.newFixedThreadPool(80, privilegedFactory);

        registerService.submit(new Runnable() {
            @Override
            public void run() {
                buttonHaveAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent haveAccountIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(haveAccountIntent);
                        finish();
                    }
                });
                buttonCreateAnAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createUser();
                    }
                });
            }
        });
    }

    private void createUser() {
        userEmail = editTextEmail.getText().toString();
        userPassword = editTextPassword.getText().toString();
        authenticationForFirebase.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (sqLiteDAO.getUserCount() == 0) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                            newUser = new User();
                            newUser.setID(0);
                            newUser.setUserUsername(editTextUsername.getText().toString());
                            newUser.setUserPassword(userPassword);
                            newUser.setUser_full_name(editTextFirstLastName.getText().toString());
                            newUser.setUser_email(userEmail);
                            newUser.setUser_phone_number(editTextPhoneNumber.getText().toString());
                            newUser.setUser_google_sign_in(false);
                            newUser.setWork_time(25);
                            newUser.setFree_time(5);
                            newUser.setSave_pomodoro_as_task(true);
                            newUser.setProfile_image(null);
                            int userID = sqLiteDAO.addUserGetID(newUser);
                            newUser.setID(userID);
                            AuthHelper.getInstance().setCurrentUser(newUser);
                            AuthHelper.getInstance().setLoggedIn(true);
                            //
                            Intent createAccountIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(createAccountIntent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "App already has a user. Can't create another.", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = authenticationForFirebase.getCurrentUser();
        if (currentUser != null) {
            Intent registerIntent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(registerIntent);
            finish();
        }
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
        if (registerService != null) {
            registerService.shutdown();
        }
    }
}
