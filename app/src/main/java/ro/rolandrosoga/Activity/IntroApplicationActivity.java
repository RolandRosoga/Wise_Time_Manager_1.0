package ro.rolandrosoga.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import net.sqlcipher.database.SQLiteDatabase;

import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.R;

public class IntroApplicationActivity extends AppCompatActivity {

    private static final String ONLY_OPENED_ONCE = "only_opened_once";
    private ConstraintLayout startButton;
    Context context;
    SQLiteDAO sqLiteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_application_intro);

        context = IntroApplicationActivity.this;
        if (SQLiteDAO.enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);
        //
        sqLiteDAO.generateTasksMock();
        sqLiteDAO.generateNotesMock();
        sqLiteDAO.generateDefaultTags();

        startButton = findViewById(R.id.startButton);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(ONLY_OPENED_ONCE, true);
        editor.apply();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(IntroApplicationActivity.this, LoginActivity.class);
                loginIntent.putExtra("isFirstTime", true);
                startActivity(loginIntent);
            }
        });
    }
}