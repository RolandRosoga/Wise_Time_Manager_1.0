package ro.rolandrosoga.Database;

import static java.lang.String.valueOf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ro.rolandrosoga.Mock.Media;
import ro.rolandrosoga.Mock.Note;
import ro.rolandrosoga.Mock.Tag;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.Mock.TaskType;
import ro.rolandrosoga.Mock.User;

public class SQLiteDAO extends SQLiteOpenHelper {

    //DB_VERSION
    private static final int WTM_DB_VERSION = 172;

    //DB_NAME
    private static final String WTM_DB_NAME = "WTM_DB.db";
    private static final String passwordString = "putYourOwnPasswordHere"
    //PASS PHRASE
    public static final byte[] PASS_PHRASE = passwordString.getBytes(); //Database password

    //TABLES
    private static final String USER_TABLE_NAME = "User";
    private static final String MEDIA_TABLE_NAME = "Media";
    private static final String NOTE_TABLE_NAME = "Note";
    private static final String TAG_TABLE_NAME = "Tag";
    private static final String SET_TAGS_TABLE_NAME = "SetTags";
    private static final String ALL_TASKS_TABLE_NAME = "AllTasks";
    private static final String EXCEEDED_TASKS_TABLE_NAME = "ExceededTasks";
    private static final String IN_PROGRESS_TASKS_TABLE_NAME = "InProgressTasks";
    private static final String COMPLETED_TASKS_TABLE = "CompletedTasks";

    //USER DATA
    private static final String USER_ID = "user_id";
    private static final String USER_USERNAME = "user_username";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_FULL_NAME = "user_fullName";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PHONE_NUMBER = "user_phone_number";
    private static final String USER_GOOGLE_SIGN_IN = "user_google_sign_in";
    private static final String USER_WORK_TIME = "user_work_time";
    private static final String USER_FREE_TIME = "user_free_time";
    private static final String USER_SAVE_POMODORO_AS_TASK = "user_save_pomodoro_as_task";
    private static final String USER_PROFILE_IMAGE = "user_profile_image";

    //IMAGES/VIDEOS URI's
    private static final String MEDIA_ID = "media_id";
    private static final String MEDIA_BLOB = "media_blob";

    //NOTE DATA
    private static final String NOTE_ID = "note_id";
    private static final String NOTE_TITLE = "note_title";
    private static final String NOTE_TEXT = "note_text";
    private static final String NOTE_DATE = "note_date";
    private static final String NOTE_CATEGORY1 = "note_cat1";
    private static final String NOTE_CATEGORY2 = "note_cat2";
    private static final String NOTE_CATEGORY3 = "note_cat3";
    private static final String NOTE_MEDIA_BITMAPS = "note_media_bitmaps";

    //TAG
    private static final String TAG_ID = "tag_id";
    private static final String TAG_CATEGORIES = "tag_categories";
    private static final String TAG_COLOR = "tag_color";

    //SET_TAGS
    private static final String SET_TAGS_ID = "id";
    private static final String SET_TAGS_VALUE_ID = "setTag_id";

    //TASK
    private static final String TASK_ID = "task_id";
    private static final String TASK_TITLE = "task_title";
    private static final String TASK_TEXT = "task_text";
    private static final String TASK_PROGRESS = "task_progress";
    private static final String TASK_CATEGORY1 = "task_cat1";
    private static final String TASK_CATEGORY2 = "task_cat2";
    private static final String TASK_CATEGORY3 = "task_cat3";
    private static final String TASK_START_DATE = "task_startDate";
    private static final String TASK_END_DATE = "task_endDate";
    private static final String TASK_MEDIA_BITMAPS = "task_media_bitmaps";

    //EXCEEDED_TASKS
    private static final String EXCEEDED_ID = "exceeded_id";
    private static final String EXCEEDED_ID_VALUE = "exceeded_task_id";

    //IN_PROGRESS_TASKS
    private static final String IN_PROGRESS_ID = "in_progress_id";
    private static final String IN_PROGRESS_ID_VALUE = "in_progress_task_id";

    //COMPLETED_TASKS
    private static final String COMPLETED_ID = "completed_id";
    private static final String COMPLETED_ID_VALUE = "completed_task_id";

    //SQLCIPHER LOGIN IMPLEMENTATION
    private static SQLiteDAO instance;
    private Context context;
    public static boolean enableSQLCypher = true;
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    private boolean databaseExists(Context context) {
        File databaseFile = context.getDatabasePath(WTM_DB_NAME);
        return databaseFile.exists();
    }
    public SQLiteDAO(@Nullable Context context) {
        super(context, WTM_DB_NAME, null, WTM_DB_VERSION);
        SQLiteDatabase.loadLibs(context);
        setWriteAheadLoggingEnabled(true);
        setContext(context);
    }
    static public synchronized SQLiteDAO getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteDAO(context);
            //db = instance.getWritableDatabase(PASS_PHRASE);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!databaseExists(getContext())) {
            db.execSQL("PRAGMA key = '" + PASS_PHRASE + "'");
        }
        String user_Sql_Query = "CREATE TABLE " + USER_TABLE_NAME + "("
                + USER_ID + " INTEGER PRIMARY KEY," + USER_USERNAME + " TEXT,"
                + USER_PASSWORD + " TEXT," + USER_FULL_NAME + " TEXT," + USER_EMAIL + " TEXT," + USER_PHONE_NUMBER + " TEXT," + USER_GOOGLE_SIGN_IN + " TEXT," + USER_WORK_TIME + " TEXT," + USER_FREE_TIME + " TEXT," + USER_SAVE_POMODORO_AS_TASK + " TEXT," + USER_PROFILE_IMAGE + " BLOB" + ")";
        db.execSQL(user_Sql_Query);
        String media_sql_Query = "CREATE TABLE " + MEDIA_TABLE_NAME + "("
                + MEDIA_ID + " INTEGER PRIMARY KEY," + MEDIA_BLOB + " BLOB" + ")";
        db.execSQL(media_sql_Query);
        String note_Sql_Query = "CREATE TABLE " + NOTE_TABLE_NAME + "("
                + NOTE_ID + " INTEGER PRIMARY KEY," + NOTE_TITLE + " TEXT,"
                + NOTE_TEXT + " TEXT," + NOTE_DATE + " TEXT," + NOTE_CATEGORY1 + " TEXT," + NOTE_CATEGORY2 + " TEXT," + NOTE_CATEGORY3 + " TEXT," + NOTE_MEDIA_BITMAPS + " TEXT" + ")";
        db.execSQL(note_Sql_Query);
        String tag_sql_Query = "CREATE TABLE " + TAG_TABLE_NAME + "("
                + TAG_ID + " INTEGER PRIMARY KEY," + TAG_CATEGORIES + " TEXT,"
                + TAG_COLOR + " TEXT" + ")";
        db.execSQL(tag_sql_Query);
        String set_tags_sql_Query = "CREATE TABLE " + SET_TAGS_TABLE_NAME + "("
                + SET_TAGS_ID + " INTEGER PRIMARY KEY," + SET_TAGS_VALUE_ID + " TEXT" + ")";
        db.execSQL(set_tags_sql_Query);
        String task_Sql_Query = "CREATE TABLE " + ALL_TASKS_TABLE_NAME + "("
                + TASK_ID + " INTEGER PRIMARY KEY," + TASK_TITLE + " TEXT,"
                + TASK_TEXT + " TEXT," + TASK_PROGRESS + " TEXT," + TASK_CATEGORY1 + " TEXT,"
                + TASK_CATEGORY2 + " TEXT," + TASK_CATEGORY3 + " TEXT," + TASK_START_DATE + " TEXT," + TASK_END_DATE + " TEXT," + TASK_MEDIA_BITMAPS + " TEXT" + ")";
        db.execSQL(task_Sql_Query);
        //
        String exceeded_sql_Query = "CREATE TABLE " + EXCEEDED_TASKS_TABLE_NAME + "("
                + EXCEEDED_ID + " INTEGER PRIMARY KEY," + EXCEEDED_ID_VALUE + " TEXT" + ")";
        db.execSQL(exceeded_sql_Query);
        String in_progress_sql_Query = "CREATE TABLE " + IN_PROGRESS_TASKS_TABLE_NAME + "("
                + IN_PROGRESS_ID + " INTEGER PRIMARY KEY," + IN_PROGRESS_ID_VALUE + " TEXT" + ")";
        db.execSQL(in_progress_sql_Query);
        String completed_sql_Query = "CREATE TABLE " + COMPLETED_TASKS_TABLE + "("
                + COMPLETED_ID + " INTEGER PRIMARY KEY," + COMPLETED_ID_VALUE + " TEXT" + ")";
        db.execSQL(completed_sql_Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MEDIA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TAG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SET_TAGS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ALL_TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXCEEDED_TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IN_PROGRESS_TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COMPLETED_TASKS_TABLE);
        onCreate(db);
    }

/*----------------------------------------User----------------------------------------------------*/
public void addUser(User new_user){
    SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
    ContentValues contentValues = new ContentValues();
    contentValues.put(USER_USERNAME,new_user.getUserUsername());
    contentValues.put(USER_PASSWORD, new_user.getUserPassword());
    contentValues.put(USER_FULL_NAME, new_user.getUser_full_name());
    contentValues.put(USER_EMAIL, new_user.getUser_email());
    contentValues.put(USER_PHONE_NUMBER, new_user.getUser_phone_number());
    contentValues.put(USER_GOOGLE_SIGN_IN, Boolean.toString(new_user.getUser_google_sign_in()));
    contentValues.put(USER_WORK_TIME, new_user.getWork_time());
    contentValues.put(USER_FREE_TIME, new_user.getFree_time());
    contentValues.put(USER_SAVE_POMODORO_AS_TASK, Boolean.toString(new_user.isSave_pomodoro_as_task()));
    contentValues.put(USER_PROFILE_IMAGE, new_user.getProfile_image());

    db.insert(USER_TABLE_NAME,null,contentValues);
    db.close();
}

    public int addUserGetID(User new_user) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME,new_user.getUserUsername());
        contentValues.put(USER_PASSWORD, new_user.getUserPassword());
        contentValues.put(USER_FULL_NAME, new_user.getUser_full_name());
        contentValues.put(USER_EMAIL, new_user.getUser_email());
        contentValues.put(USER_PHONE_NUMBER, new_user.getUser_phone_number());
        contentValues.put(USER_GOOGLE_SIGN_IN, Boolean.toString(new_user.getUser_google_sign_in()));
        contentValues.put(USER_WORK_TIME, new_user.getWork_time());
        contentValues.put(USER_FREE_TIME, new_user.getFree_time());
        contentValues.put(USER_SAVE_POMODORO_AS_TASK, Boolean.toString(new_user.isSave_pomodoro_as_task()));
        contentValues.put(USER_PROFILE_IMAGE, new_user.getProfile_image());

        db.insert(USER_TABLE_NAME,null,contentValues);
        String sql_Query = "SELECT MAX(" + USER_ID + ") AS LAST FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int rowID = -1;
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }
        db.close();
        return rowID;
    }

    public int getUserCount(){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }

    public User getUserById(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        User user = null;
        Cursor cursor = db.query(USER_TABLE_NAME,new String[]{USER_ID,USER_USERNAME,USER_PASSWORD,USER_FULL_NAME,USER_EMAIL,USER_PHONE_NUMBER,USER_GOOGLE_SIGN_IN, USER_WORK_TIME, USER_FREE_TIME, USER_SAVE_POMODORO_AS_TASK, USER_PROFILE_IMAGE},
                USER_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            user = new User(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Boolean.valueOf(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),  Integer.parseInt(cursor.getString(8)), Boolean.valueOf(cursor.getString(9)), cursor.getBlob(10));
        }
        db.close();
        return user;
    }

    public User getUserByEmail(String email){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        try {
            User user = null;
            Cursor cursor = db.query(USER_TABLE_NAME,new String[]{USER_ID,USER_USERNAME,USER_PASSWORD,USER_FULL_NAME,USER_EMAIL,USER_PHONE_NUMBER,USER_GOOGLE_SIGN_IN, USER_WORK_TIME, USER_FREE_TIME, USER_SAVE_POMODORO_AS_TASK, USER_PROFILE_IMAGE},
                    USER_EMAIL + "=?", new String[]{valueOf(email)},null,null,null);

            if(cursor != null){
                cursor.moveToFirst();
                user = new User(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Boolean.valueOf(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),  Integer.parseInt(cursor.getString(8)), Boolean.valueOf(cursor.getString(9)), cursor.getBlob(10));
            }
            db.close();
            return user;
        } catch (Exception e) {
            db.close();
            return null;
        }
    }

    public List<User> getAllUsers(){
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        User user = null;
        if(cursor.moveToFirst()){
            do{
                user = new User();
                user.setID(cursor.getInt(0));
                user.setUserUsername(cursor.getString(1));
                user.setUserPassword(cursor.getString(2));
                user.setUser_full_name(cursor.getString(3));
                user.setUser_email(cursor.getString(4));
                user.setUser_phone_number(cursor.getString(5));
                user.setUser_google_sign_in(Boolean.parseBoolean(cursor.getString(6)));
                user.setWork_time(cursor.getInt(7));
                user.setFree_time(cursor.getInt(8));
                user.setSave_pomodoro_as_task(Boolean.parseBoolean(cursor.getString(9)));
                user.setProfile_image(cursor.getBlob(10));
                userList.add(user);
            }
            while (cursor.moveToNext());
        }

        db.close();
        return userList;
    }

    public void updateUser(User user){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME,user.getUserUsername());
        contentValues.put(USER_PASSWORD, user.getUserPassword());
        contentValues.put(USER_FULL_NAME, user.getUser_full_name());
        contentValues.put(USER_EMAIL, user.getUser_email());
        contentValues.put(USER_PHONE_NUMBER, user.getUser_phone_number());
        contentValues.put(USER_GOOGLE_SIGN_IN, Boolean.toString(user.getUser_google_sign_in()));
        contentValues.put(USER_WORK_TIME, user.getWork_time());
        contentValues.put(USER_FREE_TIME, user.getFree_time());
        contentValues.put(USER_SAVE_POMODORO_AS_TASK, Boolean.toString(user.isSave_pomodoro_as_task()));
        contentValues.put(USER_PROFILE_IMAGE, user.getProfile_image());

        db.update(USER_TABLE_NAME,contentValues,USER_ID + " =?", new String[]{valueOf(user.getID())});
        db.close();
    }

    public void deleteUser(User user){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(USER_TABLE_NAME, USER_ID + " =?", new String[]{valueOf(user.getID())});
        db.close();
    }

    public void deleteAllUsers(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + USER_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }

/*---------------------------------------Media----------------------------------------------------*/
    public void addMedia(Media new_media){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDIA_BLOB, new_media.getMedia_blob());

        db.insert(MEDIA_TABLE_NAME,null,contentValues);
        db.close();
    }

    public int addMediaGetID(Media new_media) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDIA_BLOB, new_media.getMedia_blob());

        db.insert(MEDIA_TABLE_NAME,null,contentValues);
        String sql_Query = "SELECT MAX(" + MEDIA_ID + ") AS LAST FROM " + MEDIA_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int rowID = -1;
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }
        db.close();
        return rowID;
    }

    public int getMediaCount(){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + MEDIA_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }

    public Media getMediaById(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        Media media = null;
        Cursor cursor = db.query(MEDIA_TABLE_NAME,new String[]{MEDIA_ID, MEDIA_BLOB},
                MEDIA_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            media = new Media(cursor.getInt(0),
                    cursor.getBlob(1));
        }
        db.close();
        return media;
    }

    public List<Media> getAllMedia(){
        List<Media> mediaList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + MEDIA_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        Media media = null;
        if(cursor.moveToFirst()){
            do{
                media = new Media();
                media.setID(cursor.getInt(0));
                media.setMedia_blob(cursor.getBlob(1));
                mediaList.add(media);
            }
            while (cursor.moveToNext());
        }

        db.close();
        return mediaList;
    }

    public void updateMedia(Media media){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDIA_BLOB,media.getMedia_blob());

        db.update(MEDIA_TABLE_NAME,contentValues,MEDIA_ID + " =?", new String[]{valueOf(media.getID())});
        db.close();
    }

    public void deleteMedia(Media media){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(MEDIA_TABLE_NAME, MEDIA_ID + " =?", new String[]{valueOf(media.getID())});
        db.close();
    }

    public void deleteAllMedia(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + MEDIA_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }

/*---------------------------------------Notes----------------------------------------------------*/
    public void addNote(Note new_note){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE,new_note.getNoteTitle());
        contentValues.put(NOTE_TEXT, new_note.getNoteText());
        contentValues.put(NOTE_DATE, new_note.getNoteDate());
        contentValues.put(NOTE_CATEGORY1, new_note.getCategory1());
        contentValues.put(NOTE_CATEGORY2, new_note.getCategory2());
        contentValues.put(NOTE_CATEGORY3, new_note.getCategory3());
        contentValues.put(NOTE_MEDIA_BITMAPS, new_note.getMediaBitmaps());

        db.insert(NOTE_TABLE_NAME,null,contentValues);
        db.close();
    }

    public int addNoteGetID(Note new_note) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE,new_note.getNoteTitle());
        contentValues.put(NOTE_TEXT, new_note.getNoteText());
        contentValues.put(NOTE_DATE, new_note.getNoteDate());
        contentValues.put(NOTE_CATEGORY1, new_note.getCategory1());
        contentValues.put(NOTE_CATEGORY2, new_note.getCategory2());
        contentValues.put(NOTE_CATEGORY3, new_note.getCategory3());
        contentValues.put(NOTE_MEDIA_BITMAPS, new_note.getMediaBitmaps());

        db.insert(NOTE_TABLE_NAME,null,contentValues);
        String sql_Query = "SELECT MAX(" + NOTE_ID + ") AS LAST FROM " + NOTE_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int rowID = -1;
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }
        db.close();
        return rowID;
    }

    public int getNoteCount(){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + NOTE_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }

    public Note getNoteById(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        Note note = null;
        Cursor cursor = db.query(NOTE_TABLE_NAME,new String[]{NOTE_ID,NOTE_TITLE,NOTE_TEXT,NOTE_DATE,NOTE_CATEGORY1,NOTE_CATEGORY2,NOTE_CATEGORY3, NOTE_MEDIA_BITMAPS},
                NOTE_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            note = new Note(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        }
        db.close();
        return note;
    }

    public List<Note> getAllNotes(){
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + NOTE_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        Note note = null;
        if(cursor.moveToFirst()){
            do{
                note = new Note();
                note.setID(cursor.getInt(0));
                note.setNoteTitle(cursor.getString(1));
                note.setNoteText(cursor.getString(2));
                note.setNoteDate(cursor.getString(3));
                note.setCategory1(cursor.getString(4));
                note.setCategory2(cursor.getString(5));
                note.setCategory3(cursor.getString(6));
                note.setMediaBitmaps(cursor.getString(7));
                noteList.add(note);
            }
            while (cursor.moveToNext());
        }

        db.close();
        return noteList;
    }

    public int updateNote(Note note){
        deleteNote(note);
        return addNoteGetID(note);
    }
    public void updateNoteSamePosition(Note note){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE, note.getNoteTitle());
        contentValues.put(NOTE_TEXT, note.getNoteText());
        contentValues.put(NOTE_DATE, note.getNoteDate());
        contentValues.put(NOTE_CATEGORY1, note.getCategory1());
        contentValues.put(NOTE_CATEGORY2, note.getCategory2());
        contentValues.put(NOTE_CATEGORY3, note.getCategory3());
        contentValues.put(NOTE_MEDIA_BITMAPS, note.getMediaBitmaps());

        db.update(NOTE_TABLE_NAME,contentValues,NOTE_ID + " =?", new String[]{valueOf(note.getID())});
        db.close();
    }

    public void deleteNote(Note note){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(NOTE_TABLE_NAME, NOTE_ID + " =?", new String[]{valueOf(note.getID())});
        db.close();
    }

    public void deleteAllNotes(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + NOTE_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }
    public void generateNotesMock(){
        if(getNoteCount() == 0){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, HH:mm");
            Date date = new Date();
            addNote(new Note(1,"Reasons why TO-DO lists can be overwhelming.","One of the reasons to-do lists get so overwhelming is they tend to contain a random mishmash of everything. You might be working on multiple projects at work, and trying to store a reminder to review your upcoming campaign brief next to a note about sourcing vendors for an event can get confusing—fast. No wonder you’re feeling overwhelmed.\n" +
                    "\n" +
                    "To take control of your to-do list and get your best work done, consider making more than one list on your to-do list app, like separate ones for personal and team collaboration projects. For example, make sure each project or large initiative has its own list. Additionally, consider creating one list for work that’s immediately actionable, another for future project ideas, and a third for personal tasks, like a shopping list. That way, you can open the to-do list that’s relevant to the work you’re doing right now, in order to better focus on what you need to get done."
                    , simpleDateFormat.format(date), "important", "work", "school", null));
            addNote(new Note(2,"Parental expectations (having high expectations for their children) and parental involvement (having parents as active and knowledgeable participants in transition planning) have been identified as evidence-based predictors of improved postschool outcomes for students with disabilities. However, little is known about how education professionals can support and promote high expectations and involvement of families for their transition-aged youth with disabilities. Parent advocates for students with disabilities across the nation were asked for their ideas. The following provides a “to-do” list of seven strategies and 13 activities special education professionals can use in partnership with families to promote high expectations for postschool success for young adults with disabilities.", "Parental expectations (having high expectations for their children) and parental involvement (having parents as active and knowledgeable participants in transition planning) have been identified as evidence-based predictors of improved postschool outcomes for students with disabilities. However, little is known about how education professionals can support and promote high expectations and involvement of families for their transition-aged youth with disabilities. Parent advocates for students with disabilities across the nation were asked for their ideas. The following provides a “to-do” list of seven strategies and 13 activities special education professionals can use in partnership with families to promote high expectations for postschool success for young adults with disabilities."
                    , simpleDateFormat.format(date), "health", "people", null, null));
            addNote(new Note(3,"ANDROID BASED MOBILE APPLICATION DEVELOPMENT and its SECURITY", "Android is a new, next-gen mobile operating system\n" +
                    "that runs on the Linux Kernel. Android Mobile Application\n" +
                    "Development is based on Java language codes, as it allows\n" +
                    "developers to write codes in the Java language. These codes\n" +
                    "can control mobile devices via Google-enabled Java libraries.\n" +
                    "It is an important platform to develop mobile applications\n" +
                    "using the software stack provided in the Google Android SDK.\n" +
                    "Android mobile OS provides a flexible environment for\n" +
                    "Android Mobile Application Development as the developers\n" +
                    "can not only make use of Android Java Libraries but it is also\n" +
                    "possible to use normal Java IDEs. The software developers at\n" +
                    "Mobile Development India have expertise in developing\n" +
                    "applications based on Android Java Libraries and other\n" +
                    "important tools. Android Mobile Application Development\n\n" +
                    "Android applications are written in Java programming\n" +
                            "language. However, it is important to remember that they are\n" +
                            "not executed using the standard Java Virtual Machine (JVM).\n" +
                            "Instead, Google has created a custom VM called Dalvik which\n" +
                            "is responsible for converting and executing Java byte code"
                    , simpleDateFormat.format(date), "important", null, null, null));
            addNote(new Note(4, "From information security to cyber security", "Abstract\n" +
                    "\n" +
                    "The term cyber security is often used interchangeably with the term information security. This paper argues that, although there is a substantial overlap between cyber security and information security, these two concepts are not totally analogous. Moreover, the paper posits that cyber security goes beyond the boundaries of traditional information security to include not only the protection of information resources, but also that of other assets, including the person him/herself. In information security, reference to the human factor usually relates to the role(s) of humans in the security process. In cyber security this factor has an additional dimension, namely, the humans as potential targets of cyber attacks or even unknowingly participating in a cyber attack. This additional dimension has ethical implications for society as a whole, since the protection of certain vulnerable groups, for example children, could be seen as a societal responsibility."
                    , simpleDateFormat.format(date), "birthday", "people", "entertainment", null));
        }
    }

/*---------------------------------------Tags-----------------------------------------------------*/
    public int getTagCount() {
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + TAG_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }

    public Tag getTagById(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        Tag tag = null;
        Cursor cursor = db.query(TAG_TABLE_NAME,new String[]{TAG_ID,TAG_CATEGORIES,TAG_COLOR},
                TAG_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            tag = new Tag(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2));
        }
        db.close();
        return tag;
    }

    public Tag getTagByCategory(String category){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        Tag tag = null;
        Cursor cursor = db.query(TAG_TABLE_NAME,new String[]{TAG_ID,TAG_CATEGORIES,TAG_COLOR},
                TAG_CATEGORIES + "=?", new String[]{valueOf(category)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            tag = new Tag(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2));
        }
        db.close();
        return tag;
    }

    public ArrayList<Tag> getAllTags() {
        ArrayList<Tag> tagsList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + TAG_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        Tag tag = null;
        if(cursor.moveToFirst()){
            do{
                tag = new Tag();
                tag.setTagID(cursor.getInt(0));
                tag.setTagCategory(cursor.getString(1));
                tag.setTagColor(cursor.getString(2));
                tagsList.add(tag);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return tagsList;
    }

    public void addTag(Tag new_tag) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        //setting the current time for the changed date
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_CATEGORIES, new_tag.getTagCategory());
        contentValues.put(TAG_COLOR, new_tag.getTagColor());
        db.insert(TAG_TABLE_NAME,null, contentValues);
        db.close();
    }

    public void updateTag(Tag tag){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_CATEGORIES, tag.getTagCategory());
        contentValues.put(TAG_COLOR, tag.getTagColor());

        db.update(TAG_TABLE_NAME,contentValues,TAG_ID + " =?", new String[]{valueOf(tag.getTagID())});
        db.close();
    }
    public void deleteTag(Tag tag) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(TAG_TABLE_NAME, TAG_ID + " =?", new String[]{valueOf(tag.getTagID())});
        db.close();
    }

    public void deleteAllTags(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + TAG_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }

    public void generateDefaultTags() {
        if(getTagCount() == 0){
            addTag(new Tag(1,"important", "FF0000"));
            addTag(new Tag(2,"health", "36894D"));
            addTag(new Tag(3,"work", "673AB7"));
            addTag(new Tag(4,"people", "EF6EF0"));
            addTag(new Tag(5,"entertainment", "F0EE6E"));
            addTag(new Tag(6,"school", "1A83D7"));
            addTag(new Tag(7,"birthday", "C39D2E"));
        }
    }

/*--------------------------------------Tasks-----------------------------------------------------*/
    public void addTask(Task new_task){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_TITLE, new_task.getTaskTitle());
        contentValues.put(TASK_TEXT, new_task.getTaskText());
        contentValues.put(TASK_PROGRESS, new_task.getTaskProgress());
        contentValues.put(TASK_CATEGORY1, new_task.getCategory1());
        contentValues.put(TASK_CATEGORY2, new_task.getCategory2());
        contentValues.put(TASK_CATEGORY3, new_task.getCategory3());
        contentValues.put(TASK_START_DATE, new_task.getTask_startDate());
        contentValues.put(TASK_END_DATE, new_task.getTask_endDate());
        contentValues.put(TASK_MEDIA_BITMAPS, new_task.getMediaBitmaps());

        db.insert(ALL_TASKS_TABLE_NAME,null,contentValues);
        db.close();
    }

    public int addTaskGetID(Task new_task) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_TITLE, new_task.getTaskTitle());
        contentValues.put(TASK_TEXT, new_task.getTaskText());
        contentValues.put(TASK_PROGRESS, new_task.getTaskProgress());
        contentValues.put(TASK_CATEGORY1, new_task.getCategory1());
        contentValues.put(TASK_CATEGORY2, new_task.getCategory2());
        contentValues.put(TASK_CATEGORY3, new_task.getCategory3());
        contentValues.put(TASK_START_DATE, new_task.getTask_startDate());
        contentValues.put(TASK_END_DATE, new_task.getTask_endDate());
        contentValues.put(TASK_MEDIA_BITMAPS, new_task.getMediaBitmaps());

        db.insert(ALL_TASKS_TABLE_NAME,null,contentValues);
        String sql_Query = "SELECT MAX(" + TASK_ID + ") AS LAST FROM " + ALL_TASKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int rowID = -1;
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }
        db.close();
        return rowID;
    }

    public int getTaskCount(){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + ALL_TASKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }

    public Task getTaskById(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        Task task = null;
        Cursor cursor = db.query(ALL_TASKS_TABLE_NAME,new String[]{TASK_ID,TASK_TITLE,TASK_TEXT,TASK_PROGRESS,
                        TASK_CATEGORY1,TASK_CATEGORY2,TASK_CATEGORY3,TASK_START_DATE,TASK_END_DATE, TASK_MEDIA_BITMAPS},
                TASK_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            task = new Task(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9));
        }
        db.close();
        return task;
    }

    public List<Task> getAllTasks(){
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + ALL_TASKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        Task task = null;
        if(cursor.moveToFirst()){
            do{
                task = new Task();
                task.setID(cursor.getInt(0));
                task.setTaskTitle(cursor.getString(1));
                task.setTaskText(cursor.getString(2));
                task.setTaskProgress(cursor.getString(3));
                task.setCategory1(cursor.getString(4));
                task.setCategory2(cursor.getString(5));
                task.setCategory3(cursor.getString(6));
                task.setTask_startDate(cursor.getString(7));
                task.setTask_endDate(cursor.getString(8));
                task.setMediaBitmaps(cursor.getString(9));
                taskList.add(task);
            }
            while (cursor.moveToNext());
        }

        db.close();
        return taskList;
    }

    public int updateTask(Task task){
        deleteTask(task);
        return addTaskGetID(task);
    }

    public void updateTaskSamePosition(Task task){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_TITLE, task.getTaskTitle());
        contentValues.put(TASK_TEXT, task.getTaskText());
        contentValues.put(TASK_PROGRESS, task.getTaskProgress());
        contentValues.put(TASK_CATEGORY1, task.getCategory1());
        contentValues.put(TASK_CATEGORY2, task.getCategory2());
        contentValues.put(TASK_CATEGORY3, task.getCategory3());
        contentValues.put(TASK_START_DATE, task.getTask_startDate());
        contentValues.put(TASK_END_DATE, task.getTask_endDate());
        contentValues.put(TASK_MEDIA_BITMAPS, task.getMediaBitmaps());

        db.update(ALL_TASKS_TABLE_NAME,contentValues,TASK_ID + " =?", new String[]{valueOf(task.getID())});
        db.close();
    }

    public void deleteTask(Task task){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(ALL_TASKS_TABLE_NAME, TASK_ID + " =?", new String[]{valueOf(task.getID())});
        db.close();
    }

    public void deleteAllTasks(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + ALL_TASKS_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }
    public void generateTasksMock(){
        if(getTaskCount() == 0){
            Calendar startTime = GregorianCalendar.getInstance();
            Calendar endTime = GregorianCalendar.getInstance();

            startTime.add(Calendar.HOUR_OF_DAY, -25);
            endTime.add(Calendar.HOUR_OF_DAY, -24);
            addTask(new Task(1,"Parental expectations (having high expectations for their children) and parental involvement (having parents as active and knowledgeable participants in transition planning) have been identified as evidence-based predictors of improved postschool outcomes for students with disabilities. However, little is known about how education professionals can support and promote high expectations and involvement of families for their transition-aged youth with disabilities. Parent advocates for students with disabilities across the nation were asked for their ideas. The following provides a “to-do” list of seven strategies and 13 activities special education professionals can use in partnership with families to promote high expectations for postschool success for young adults with disabilities.", "Parental expectations (having high expectations for their children) and parental involvement (having parents as active and knowledgeable participants in transition planning) have been identified as evidence-based predictors of improved postschool outcomes for students with disabilities. However, little is known about how education professionals can support and promote high expectations and involvement of families for their transition-aged youth with disabilities. Parent advocates for students with disabilities across the nation were asked for their ideas. The following provides a “to-do” list of seven strategies and 13 activities special education professionals can use in partnership with families to promote high expectations for postschool success for young adults with disabilities."
            , "Exceeded", "important", "people", "school",
                    String.valueOf(startTime.getTimeInMillis()), String.valueOf(endTime.getTimeInMillis()), null) );
            addExceeded(new TaskType(1, 1));
            //
            startTime = GregorianCalendar.getInstance();
            endTime = GregorianCalendar.getInstance();
            startTime.add(Calendar.MINUTE, -15);
            endTime.add(Calendar.MINUTE, 30);
            addTask(new Task(2,"Proritize your work Today", "    Sorting and prioritizing work. If you want to change the order of your written to-do list, you have to rewrite the whole thing. But with a to-do list app, you can easily drag and drop items. Not only that—most to-do list apps offer a way to track priority with custom tags. Digital to-do lists also allow you to set up recurring tasks, so you’ll never forget a weekly meeting again. Plus, to-do list apps support multiple views, so you can visualize your tasks the way that works best for you, be it in a list or a Kanban board.\n" +
                    "\n" +
                    "    Impossible to lose. Unlike a handwritten to-do list, you can’t “lose” an online to-do list. You’ll always have access to the list—whether on your desktop, iPhone, iPad, or other smart devices—so you can jot down to-dos wherever you are.\n" +
                    "\n" +
                    "    Add additional context to your to-dos. Most to-do list apps offer a way for you to add additional information in the description. In a written to-do list, all you have are a couple of words to describe what you need to work on. But with a to-do list app, each to-do has an expandable description, where you can add any relevant task details, working docs, or important information. Plus, to-do list apps utilize integrations like Google Drive, Google Calendar, and Outlook, so you can attach documents and add context to your most important tasks.\n" +
                    "\n" +
                    "    Create separate lists in the same place. Before you choose a to-do list app, make sure you can create more than one “list” in the app. You might want to create a personal to-do list for your work, another for your team’s work, and a third for your professional development, for example. Or, you may want to sort tasks by timeframe, such as creating a daily to-do list and a weekly to-do list. A to-do list app with multiple list options allows you to store all of these to-dos in one place.\n" +
                    "\n" +
                    "    Set reminders, due dates, and notifications. Your to-dos, whether for a “me” day or critical project deadlines, don’t mean much if they’re not done in time. With a to-do list app, you can track when work is due, and set reminders or customizable notifications to make sure you get your to-dos done in time.\n" +
                    "\n" +
                    "Collaboration. When your individual to-do list is organized and your priorities are clear, you can better contribute to team projects and initiatives. In other words, the more organized you are, the easier it’ll be to collaborate with your team."
            , "Completed", "important", "work", null,
                    String.valueOf(startTime.getTime().getTime()), String.valueOf(endTime.getTime().getTime()), null) );
            addCompleted(new TaskType(1, 2));
            //
            startTime = GregorianCalendar.getInstance();
            endTime = GregorianCalendar.getInstance();
            startTime.add(Calendar.HOUR_OF_DAY, -25);
            endTime.add(Calendar.HOUR_OF_DAY, 23);
            addTask(new Task(3,"Reasons why TO-DO lists can be overwhelming.","One of the reasons to-do lists get so overwhelming is they tend to contain a random mishmash of everything. You might be working on multiple projects at work, and trying to store a reminder to review your upcoming campaign brief next to a note about sourcing vendors for an event can get confusing—fast. No wonder you’re feeling overwhelmed.\n" +
                    "\n" +
                    "To take control of your to-do list and get your best work done, consider making more than one list on your to-do list app, like separate ones for personal and team collaboration projects. For example, make sure each project or large initiative has its own list. Additionally, consider creating one list for work that’s immediately actionable, another for future project ideas, and a third for personal tasks, like a shopping list. That way, you can open the to-do list that’s relevant to the work you’re doing right now, in order to better focus on what you need to get done."
            , "In progress", "entertainment", null, null,
                    String.valueOf(startTime.getTime().getTime()), String.valueOf(endTime.getTime().getTime()), null) );
            addInProgress(new TaskType(1, 3));
            //
            startTime = GregorianCalendar.getInstance();
            endTime = GregorianCalendar.getInstance();
            startTime.add(Calendar.MINUTE, 30);
            endTime.add(Calendar.MINUTE, 45);
            addTask(new Task(4,"TO-DO Today","Wake up to the sound of an alarm.\n" +
                    "Stretch and yawn to greet the day.\n" +
                    "Brush teeth to freshen up.\n" +
                    "Shower or wash up to cleanse.\n" +
                    "Get dressed in clothes for the day.\n" +
                    "Make breakfast, cereal, toast, or coffee.\n" +
                    "Check messages, emails, or social media.\n" +
                    "Commute to work, school, or errands.\n" +
                    "Work on a computer, write, code, or design.\n" +
                    "Attend meetings, classes, or presentations.\n" +
                    "Chat with colleagues or classmates.\n" +
                    "Take a lunch break for a quick bite.\n" +
                    "Run errands, go grocery shopping, or pick up dry cleaning.\n" +
                    "Exercise, go for a walk, run, or hit the gym.\n" +
                    "Cook dinner, prepare a meal for yourself or family.\n" +
                    "Eat dinner, relax and unwind.\n" +
                    "Watch TV, listen to music, or read a book.\n" +
                    "Spend time with family and friends.\n" +
                    "Get ready for bed, shower, and change clothes.\n" +
                    "Go to sleep and prepare for a new day."
            , "Exceeded", "health", "birthday", null,
                    String.valueOf(startTime.getTime().getTime()), String.valueOf(endTime.getTime().getTime()), null) );
            addExceeded(new TaskType(1, 4));
            //
            startTime = GregorianCalendar.getInstance();
            endTime = GregorianCalendar.getInstance();
            startTime.add(Calendar.HOUR_OF_DAY, -25);
            endTime.add(Calendar.MINUTE, -45);
            addTask(new Task(5,"Sibiu-Florance", "Research and book flights from Sibiu to Florence.\n" +
                    "    Check passport validity and any visa requirements for Italy.\n" +
                    "    Book accommodation in Florence.\n" +
                    "    Research things to do and see in Florence.\n" +
                    "    Make a list of essentials to pack for the trip.\n" +
                    "    Pack luggage for the trip to Florence.\n" +
                    "    Arrange transportation to Sibiu airport.\n" +
                    "    Check in for the flight from Sibiu to Florence.\n" +
                    "    Board the plane for Florence.\n" +
                    "    Relax and enjoy the flight to Florence.\n" +
                    "    Land at Florence airport and go through customs.\n" +
                    "Collect luggage and find transportation to your accommodation.\n" +
                    "    Check in at your hotel in Florence.\n" +
                    "    Explore the surrounding area of your hotel.\n" +
                    "Visit a famous landmark in Florence (e.g., Ponte Vecchio, Duomo).\n" +
                    "    Enjoy a traditional Italian meal for lunch.\n" +
                    "Visit a museum or art gallery in Florence (e.g., Uffizi Gallery, Accademia Gallery).\n" +
                    "    Take a walking tour or bike tour of Florence.\n" +
                    "    Sample gelato (Italian ice cream) at a local shop.\n" +
                    "    Relax and people-watch in a beautiful piazza.\n" +
                    "    Do some souvenir shopping at a local market.\n" +
                    "    Enjoy a delicious Italian dinner.\n" +
                    "    Take a night stroll and admire the illuminated Florence.\n" +
                    "    Visit a hidden gem or off-the-beaten-path location.\n" +
                    "    Take a day trip to a nearby town like Siena or Pisa.\n" +
                    "    Enjoy a cooking class and learn to make Italian dishes.\n" +
                    "    Take a wine tasting tour in the Tuscan countryside.\n" +
                    "    Attend a concert or opera performance (if available).\n" +
                    "    Relax and have a spa treatment.\n" +
                    "Write postcards or send messages to loved ones back home.\n" +
                    "    Pack your luggage for the return flight.\n" +
                    "    Check out of your hotel in Florence.\n" +
                    "    Find transportation to Florence airport.\n" +
                    "Check in for the return flight from Florence to Sibiu.\n" +
                    "    Board the plane for Sibiu.\n" +
                    "    Relax and enjoy the flight back to Sibiu.\n" +
                    "    Land at Sibiu airport and collect your luggage.\n" +
                    "    Arrange transportation back to your home in Sibiu.\n" +
                    "    Unpack your luggage and organize souvenirs.\n" +
                    "    Reflect on your trip to Florence and make a photo album."
            , "Exceeded", "entertainment", "important", null,
                    String.valueOf(startTime.getTime().getTime()), "-1", null) );
            addExceeded(new TaskType(2, 5));
            //
            startTime = GregorianCalendar.getInstance();
            endTime = GregorianCalendar.getInstance();
            startTime.add(Calendar.HOUR_OF_DAY, -1);
            endTime.add(Calendar.MINUTE, 45);
            addTask(new Task(6,"Have the best grades!", "Pick classes that interest you and align with your major.\n" +
                    "Review your course syllabi to understand expectations and grading.\n" +
                    "Set up a daily or weekly planner to manage your study time.\n" +
                    "Attend all lectures and take clear and concise notes.\n" +
                    "Review your notes after each class to solidify understanding.\n" +
                    "Form a study group with classmates for challenging subjects.\n" +
                    "Find a quiet and distraction-free place to study effectively.\n" +
                    "Develop a study schedule that prioritizes upcoming assignments and exams.\n" +
                    "Utilize campus resources like tutoring centers and writing labs.\n" +
                    "Ask questions and clarify doubts during lectures or office hours.\n" +
                    "Start assignments early to avoid last-minute cramming.\n" +
                    "Break down large projects into smaller, more manageable tasks.\n" +
                    "Manage your time effectively to avoid procrastination.\n" +
                    "Get enough sleep to stay focused and retain information.\n" +
                    "Eat healthy meals and snacks to fuel your brainpower.\n" +
                    "Take breaks during studying to avoid burnout.\n" +
                    "Practice active recall by summarizing key concepts in your own words.\n" +
                    "Use flashcards or mind maps to organize and memorize information.\n" +
                    "Review past exams and quizzes to understand professor's testing style.\n" +
                    "Formulate a test-taking strategy that works best for you (e.g., outlining, reviewing time management).\n" +
                    "Stay calm and focused during exams by managing test anxiety.\n" +
                    "Review your graded assignments to identify areas for improvement.\n" +
                    "Seek feedback from professors or tutors to understand your strengths and weaknesses.\n" +
                    "Celebrate your achievements and milestones throughout the semester.\n" +
                    "Reflect on your study habits and make adjustments as needed.\n" +
                    "Connect with classmates online through forums or study groups.\n" +
                    "Utilize online resources like educational videos or practice problems.\n" +
                    "Explore different learning methods like textbooks, lectures, or podcasts.\n" +
                    "Take advantage of library resources for research and in-depth learning.\n" +
                    "Maintain a positive attitude and believe in your ability to succeed.\n" +
                    "Reward yourself for completing tasks and achieving goals.\n" +
                    "Don't be afraid to ask for help from classmates, professors, or advisors.\n" +
                    "Maintain a healthy work-life balance to avoid stress and burnout.\n" +
                    "Advocate for yourself and communicate any challenges you face in class.\n" +
                    "Prioritize your well-being and mental health for optimal academic performance.\n" +
                    "Take advantage of professor office hours to discuss course material.\n" +
                    "Explore internship or research opportunities to gain practical experience.\n" +
                    "Network with professors and build relationships for future recommendations.\n" +
                    "Stay updated on academic deadlines and submission requirements.\n" +
                    "Celebrate your accomplishments at the end of the semester and enjoy the break!"
            , "Completed", "school", "important", null,
                    String.valueOf(startTime.getTime().getTime()), String.valueOf(endTime.getTime().getTime()), null) );
            addCompleted(new TaskType(2, 6));
        }
    }

/*----------------------------------ExceededTasks-----------------------------------------------*/
public int getExceededCount() {
    SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

    String sql_Query = "SELECT * FROM " + EXCEEDED_TASKS_TABLE_NAME;
    Cursor cursor = db.rawQuery(sql_Query, null);

    int count = cursor.getCount();
    db.close();
    return count;
}

    public TaskType getExceededByID(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(EXCEEDED_TASKS_TABLE_NAME,new String[]{EXCEEDED_ID,EXCEEDED_ID_VALUE},
                EXCEEDED_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }

    public TaskType getExceededByValue(int value){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(EXCEEDED_TASKS_TABLE_NAME,new String[]{EXCEEDED_ID,EXCEEDED_ID_VALUE},
                EXCEEDED_ID_VALUE + "=?", new String[]{valueOf(value)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }

    public ArrayList<TaskType> getAllExceeded() {
        ArrayList<TaskType> exceededList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + EXCEEDED_TASKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                exceededList.add(counter);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return exceededList;
    }

    public void addExceeded(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        //setting the current time for the changed date
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXCEEDED_ID_VALUE, new_taskType.getTask_id_value());
        db.insert(EXCEEDED_TASKS_TABLE_NAME,null,contentValues);
        db.close();
    }

    public void updateExceeded(TaskType new_taskType){
        deleteExceeded(new_taskType);
        addExceeded(new_taskType);

        /*
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(EXCEEDED_ID_VALUE, counter.getCounterValue());

        db.update(EXCEEDED_TASKS_TABLE_NAME,contentValues,EXCEEDED_ID + " =?", new String[]{valueOf(counter.getCounterID())});
        db.close();
         */
    }
    public void deleteExceeded(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(EXCEEDED_TASKS_TABLE_NAME, EXCEEDED_ID + " =?", new String[]{valueOf(new_taskType.getTask_id_value())});
        db.close();
    }

    public void deleteAllExceeded(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + EXCEEDED_TASKS_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }

    public ArrayList<Task> getExceededTasks(){
        ArrayList<TaskType> exceededList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + EXCEEDED_TASKS_TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                exceededList.add(counter);
            }
            while (cursor.moveToNext());
        }
        ArrayList<Task> exceededTasks = new ArrayList<>();
        for (TaskType taskType:exceededList) {
            exceededTasks.add(getTaskById(taskType.getTask_id_value()));
        }

        sqLiteDatabase.close();
        return exceededTasks;
    }
/*----------------------------------InProgressTasks-----------------------------------------------*/
public int getInProgressCount() {
    SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

    String sql_Query = "SELECT * FROM " + IN_PROGRESS_TASKS_TABLE_NAME;
    Cursor cursor = db.rawQuery(sql_Query, null);

    int count = cursor.getCount();
    db.close();
    return count;
}

    public TaskType getInProgressByID(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(IN_PROGRESS_TASKS_TABLE_NAME,new String[]{IN_PROGRESS_ID,IN_PROGRESS_ID_VALUE},
                IN_PROGRESS_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }

    public TaskType getInProgressByValue(int value){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(IN_PROGRESS_TASKS_TABLE_NAME,new String[]{IN_PROGRESS_ID,IN_PROGRESS_ID_VALUE},
                IN_PROGRESS_ID_VALUE + "=?", new String[]{valueOf(value)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }


    public ArrayList<TaskType> getAllInProgress() {
        ArrayList<TaskType> inProgressList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + IN_PROGRESS_TASKS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                inProgressList.add(counter);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return inProgressList;
    }

    public void addInProgress(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        //setting the current time for the changed date
        ContentValues contentValues = new ContentValues();
        contentValues.put(IN_PROGRESS_ID_VALUE, new_taskType.getTask_id_value());
        db.insert(IN_PROGRESS_TASKS_TABLE_NAME,null,contentValues);
        db.close();
    }

    public void updateInProgress(TaskType new_taskType){
        deleteInProgress(new_taskType);
        addInProgress(new_taskType);

        /*
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(IN_PROGRESS_ID_VALUE, counter.getCounterValue());

        db.update(IN_PROGRESS_TASKS_TABLE_NAME,contentValues,IN_PROGRESS_ID + " =?", new String[]{valueOf(counter.getCounterID())});
        db.close();
         */
    }
    public void deleteInProgress(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(IN_PROGRESS_TASKS_TABLE_NAME, IN_PROGRESS_ID + " =?", new String[]{valueOf(new_taskType.getTask_id_value())});
        db.close();
    }

    public void deleteAllInProgress(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + IN_PROGRESS_TASKS_TABLE_NAME;

        db.execSQL(sql_Query);
        db.close();
    }

    public ArrayList<Task> getInProgressTasks(){
        //ArrayList<TaskType> inProgressList = new ArrayList<>();
        ArrayList<Task> inProgressTasks = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + IN_PROGRESS_TASKS_TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                //inProgressList.add(counter);
                inProgressTasks.add(getTaskById(counter.getTask_id_value()));

            }
            while (cursor.moveToNext());
        }
        /*
        ArrayList<Task> inProgressTasks = new ArrayList<>();
        for (TaskType taskType:inProgressList) {
            inProgressTasks.add(getTaskById(taskType.getTask_id_value()));
        }
         */

        sqLiteDatabase.close();
        return inProgressTasks;
    }
/*-----------------------------------CompletedTasks------------------------------------------------*/
public int getCompletedCount() {
    SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

    String sql_Query = "SELECT * FROM " + COMPLETED_TASKS_TABLE;
    Cursor cursor = db.rawQuery(sql_Query, null);

    int count = cursor.getCount();
    db.close();
    return count;
}

    public TaskType getCompletedByID(int id){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(COMPLETED_TASKS_TABLE,new String[]{COMPLETED_ID,COMPLETED_ID_VALUE},
                COMPLETED_ID + "=?", new String[]{valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }

    public TaskType getCompletedByValue(int value){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);
        TaskType taskType = null;
        Cursor cursor = db.query(COMPLETED_TASKS_TABLE,new String[]{COMPLETED_ID,COMPLETED_ID_VALUE},
                COMPLETED_ID_VALUE + "=?", new String[]{valueOf(value)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            taskType = new TaskType(cursor.getInt(0),
                    cursor.getInt(1));
        }
        db.close();
        return taskType;
    }


    public ArrayList<TaskType> getAllCompleted() {
        ArrayList<TaskType> completedList = new ArrayList<>();
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + COMPLETED_TASKS_TABLE;
        Cursor cursor = db.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                completedList.add(counter);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return completedList;
    }

    public void addCompleted(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);
        //setting the current time for the changed date
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPLETED_ID_VALUE, new_taskType.getTask_id_value());
        db.insert(COMPLETED_TASKS_TABLE,null,contentValues);
        db.close();
    }

    public void updateCompleted(TaskType new_taskType){
        deleteCompleted(new_taskType);
        addCompleted(new_taskType);

        /*
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPLETED_ID_VALUE, counter.getCounterValue());

        db.update(COMPLETED_TASKS_TABLE,contentValues,COMPLETED_ID + " =?", new String[]{valueOf(counter.getCounterID())});
        db.close();
         */
    }
    public void deleteCompleted(TaskType new_taskType) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        db.delete(COMPLETED_TASKS_TABLE, COMPLETED_ID + " =?", new String[]{valueOf(new_taskType.getTask_id_value())});
        db.close();
    }

    public void deleteAllCompleted(){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHRASE);

        String sql_Query = " DELETE FROM " + COMPLETED_TASKS_TABLE;

        db.execSQL(sql_Query);
        db.close();
    }

    public ArrayList<Task> getCompletedTasks(){
        ArrayList<TaskType> completedList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = instance.getReadableDatabase(PASS_PHRASE);

        String sql_Query = "SELECT * FROM " + COMPLETED_TASKS_TABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(sql_Query,null);

        TaskType counter = null;
        if(cursor.moveToFirst()){
            do{
                counter = new TaskType();
                counter.setTaskType_id(cursor.getInt(0));
                counter.setTask_id_value(cursor.getInt(1));
                completedList.add(counter);
            }
            while (cursor.moveToNext());
        }
        ArrayList<Task> completedTasks = new ArrayList<>();
        for (TaskType taskType:completedList) {
            completedTasks.add(getTaskById(taskType.getTask_id_value()));
        }

        sqLiteDatabase.close();
        return completedTasks;
    }


}
