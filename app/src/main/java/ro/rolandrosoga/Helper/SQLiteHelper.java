package ro.rolandrosoga.Helper;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Mock.Note;
import ro.rolandrosoga.Mock.Tag;
import ro.rolandrosoga.Mock.Task;

public class SQLiteHelper {
    SQLiteDAO sqLiteDAO;
    Context context;
    List<Tag> tagList;
    List<Note> noteList;
    List<Task> taskList;
    private ExecutorService tasksService;
    ThreadFactory privilegedFactory;

    public SQLiteHelper(Context context) {
        this.context = context;
        if (SQLiteDAO.enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);
        privilegedFactory = Executors.privilegedThreadFactory();
        tasksService = Executors.newFixedThreadPool(100, privilegedFactory);
    }
    public void tagWasDeleted() {
        tasksService.submit(new Runnable() {
            @Override
            public void run() {
                checkNotesByTags();
                checkTasksByTags();
            }
        });
        tasksService.shutdown();
    }
    private void checkNotesByTags() {
        tagList = sqLiteDAO.getAllTags();
        noteList = sqLiteDAO.getAllNotes();
        int noteCounter = 0;
        int[] notesVector = new int[3];
        Arrays.fill(notesVector, 0);
        for (Note note : noteList) {
            for (Tag tag : tagList) {
                if (note.getCategory1() != null && note.getCategory1().equals(tag.getTagCategory())) {
                    notesVector[0] = 1;
                    noteCounter++;
                }
                if (note.getCategory2() != null && note.getCategory2().equals(tag.getTagCategory())) {
                    notesVector[1] = 1;
                    noteCounter++;
                }
                if (note.getCategory3() != null && note.getCategory3().equals(tag.getTagCategory())) {
                    notesVector[2] = 1;
                    noteCounter++;
                }
                if (noteCounter == 3)
                    break;
            }
            switch (noteCounter) {
                case (0):
                    note.setCategory1(null);
                    note.setCategory2(null);
                    note.setCategory3(null);
                    sqLiteDAO.updateNoteSamePosition(note);
                    break;
                case (1):
                    if (notesVector[0] == 1) {
                        note.setCategory2(null);
                        note.setCategory3(null);
                    } else if (notesVector[1] == 1) {
                        note.setCategory1(note.getCategory2());
                        note.setCategory2(null);
                        note.setCategory3(null);
                    } else if (notesVector[2] == 1) {
                        note.setCategory1(note.getCategory3());
                        note.setCategory2(null);
                        note.setCategory3(null);
                    }
                    sqLiteDAO.updateNoteSamePosition(note);
                    break;
                case (2):
                    if (notesVector[0] == 1) {
                        if (notesVector[1] == 1) {
                            note.setCategory3(null);
                        } else if (notesVector[2] == 1) {
                            note.setCategory2(note.getCategory3());
                            note.setCategory3(null);
                        }
                    } else {
                        note.setCategory1(note.getCategory2());
                        note.setCategory2(note.getCategory3());
                        note.setCategory3(null);
                    }
                    sqLiteDAO.updateNoteSamePosition(note);
                    break;
                case (3):
                    break;
            }
            noteCounter=0;
            Arrays.fill(notesVector, 0);
        }
    }

    private void checkTasksByTags() {
        tagList = sqLiteDAO.getAllTags();
        taskList = sqLiteDAO.getAllTasks();
        int taskCounter = 0;
        int[] tasksVector = new int[3];
        Arrays.fill(tasksVector, 0);
        for (Task task : taskList) {
            for (Tag tag : tagList) {
                if (task.getCategory1() != null && task.getCategory1().equals(tag.getTagCategory())) {
                    tasksVector[0] = 1;
                    taskCounter++;
                }
                if (task.getCategory2() != null && task.getCategory2().equals(tag.getTagCategory())) {
                    tasksVector[1] = 1;
                    taskCounter++;
                }
                if (task.getCategory3() != null && task.getCategory3().equals(tag.getTagCategory())) {
                    tasksVector[2] = 1;
                    taskCounter++;
                }
                if (taskCounter == 3) {
                    break;
                }
            }
            switch (taskCounter) {
                case (0):
                    task.setCategory1(null);
                    task.setCategory2(null);
                    task.setCategory3(null);
                    sqLiteDAO.updateTaskSamePosition(task);
                    break;
                case (1):
                    if (tasksVector[0] == 1) {
                        task.setCategory2(null);
                        task.setCategory3(null);
                    } else if (tasksVector[1] == 1) {
                        task.setCategory1(task.getCategory2());
                        task.setCategory2(null);
                        task.setCategory3(null);
                    } else if (tasksVector[2] == 1) {
                        task.setCategory1(task.getCategory3());
                        task.setCategory2(null);
                        task.setCategory3(null);
                    }
                    sqLiteDAO.updateTaskSamePosition(task);
                    break;
                case (2):
                    if (tasksVector[0] == 1) {
                        if (tasksVector[1] == 1) {
                            task.setCategory3(null);
                        } else if (tasksVector[2] == 1) {
                            task.setCategory2(task.getCategory3());
                            task.setCategory3(null);
                        }
                    } else {
                        task.setCategory1(task.getCategory2());
                        task.setCategory2(task.getCategory3());
                        task.setCategory3(null);
                    }
                    sqLiteDAO.updateTaskSamePosition(task);
                    break;
                case (3):
                    break;
            }
            taskCounter=0;
            Arrays.fill(tasksVector, 0);
        }
    }

}
