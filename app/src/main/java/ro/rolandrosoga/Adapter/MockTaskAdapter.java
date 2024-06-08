package ro.rolandrosoga.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import net.sqlcipher.database.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Activity.EditTaskActivity;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Listener.TaskListener;
import ro.rolandrosoga.Mock.Tag;
import ro.rolandrosoga.Mock.Task;
import ro.rolandrosoga.R;

public class MockTaskAdapter extends RecyclerView.Adapter<MockTaskAdapter.TaskViewHolder> {
    Context taskMockContext;
    List<Task> task_list;
    private OnItemClickListener onItemClickListener;
    TaskListener taskListener;
    ArrayList<Tag> categoryTuple = new ArrayList<>();
    private SQLiteDAO sqLiteDAO;
    int whichActivity = -1;
    ThreadFactory privilegedFactory;
    ExecutorService taskServices;

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MockTaskAdapter(Context context, List<Task> list, TaskListener taskListener, int whichActivity) {
        this.taskMockContext = context;
        this.task_list = list;
        this.taskListener = taskListener;
        this.whichActivity = whichActivity;
        privilegedFactory = Executors.defaultThreadFactory();
        this.taskServices = Executors.newFixedThreadPool(15, privilegedFactory);
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);
    }


    public ArrayList<Tag> getColorList() {
        return categoryTuple;
    }

    public void setColorList() {
        categoryTuple = sqLiteDAO.getAllTags();
    }

    public void setSearchedTasksList(List<Task> searchedTasksList) {
        task_list = searchedTasksList;
        taskServices.submit(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        setColorList();
        return new TaskViewHolder(LayoutInflater.from(taskMockContext).inflate(R.layout.mock_tasks, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        //Setting the default values for environment
        Task currentTask = task_list.get(position);
        //
        holder.tasksMockTitle.setText(currentTask.getTaskTitle());
        holder.tasksMockText.setText(currentTask.getTaskText());
        //Set Progress and Progress color
        String progress = currentTask.getTaskProgress();
        holder.taskMockProgress.setText(progress);
        if (progress.equals("Exceeded")) {
            holder.taskMockProgress.setTextAppearance(R.style.red);

            holder.tasksTimeWindow.setTextAppearance(R.style.bright_red);
        } else if (progress.equals("Completed")) {
            holder.taskMockProgress.setTextAppearance(R.style.green);
            holder.tasksTimeWindow.setTextAppearance(R.style.white);
        } else if (progress.equals("In progress")) {
            holder.taskMockProgress.setTextAppearance(R.style.yellow);
        }
        //
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMM dd, HH:mm");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("EEE, MMM dd, HH:mm");
        Date timeNow = new Date();
        long timeNowInteger = timeNow.getTime();
        //
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        minDate.setTime(new Date());
        minDate.add(Calendar.HOUR_OF_DAY, -24);
        maxDate.setTime(new Date());
        maxDate.add(Calendar.HOUR_OF_DAY, 24);
        //
        //Set Beginning and End time
        String startTime = "";
        String endTime = "";
        long startDate = Long.parseLong(currentTask.getTask_startDate());
        long endDate = Long.parseLong(currentTask.getTask_endDate());
        if (startDate != -1 && endDate != -1) {
            //
            if (startDate < minDate.getTimeInMillis()
                    || endDate > maxDate.getTimeInMillis()) {
                //
                startTime = simpleDateFormat3.format(startDate);
                maxDate.setTime(new Date( startDate));
                maxDate.add(Calendar.HOUR_OF_DAY, 24);
                //If difference between Start and End date is more when 24h, do:
                if (maxDate.getTimeInMillis() >= endDate) {
                    endTime = simpleDateFormat1.format(endDate);
                } else {
                    endTime = simpleDateFormat2.format(endDate);
                }
                if (timeNowInteger > startDate && !currentTask.getTaskProgress().equals("Completed")) {
                    String taskTime;
                    if (timeNowInteger > endDate) {
                        taskTime = "<font color=#F06E6E> " + startTime + " - " + endTime + "</font>";
                    } else {
                        taskTime = "<font color=#F06E6E> " + startTime + "</font>" + " - " + endTime;
                    }
                    holder.tasksTimeWindow.setText(Html.fromHtml(taskTime));
                } else {
                    startTime = startTime + " - " + endTime;
                    holder.tasksTimeWindow.setText(startTime);
                }
            } else if (startDate > minDate.getTimeInMillis()
                    && endDate < maxDate.getTimeInMillis()) {
                //
                startTime = simpleDateFormat1.format(startDate);
                maxDate.setTime(new Date( (long) startDate));
                maxDate.add(Calendar.HOUR_OF_DAY, 24);
                //If difference between Start and End date is more when 24h, do:
                if (maxDate.getTimeInMillis() >= endDate) {
                    endTime = simpleDateFormat1.format(endDate);
                } else {
                    endTime = simpleDateFormat2.format(endDate);
                }
                //
                if (timeNowInteger > startDate && !currentTask.getTaskProgress().equals("Completed")) {
                    String taskTime;
                    if (timeNowInteger > endDate) {
                        taskTime = "<font color=#F06E6E> " + startTime + " - " + endTime + "</font>";
                    } else {
                        taskTime = "<font color=#F06E6E> " + startTime + "</font>" + " - " + endTime;
                    }
                    holder.tasksTimeWindow.setText(Html.fromHtml(taskTime));
                } else {
                    startTime = startTime + " - " + endTime;
                    holder.tasksTimeWindow.setText(startTime);
                }
            }
        } else if (startDate != -1 && endDate == -1) {

            if (startDate < minDate.getTimeInMillis()
                    || startDate > maxDate.getTimeInMillis()) {
                startTime = simpleDateFormat3.format(startDate);
                maxDate.setTime(new Date(startDate));
                maxDate.add(Calendar.HOUR_OF_DAY, 24);
                //If difference between Start and End date is more when 24h, do:
                if (timeNowInteger > startDate && !currentTask.getTaskProgress().equals("Completed")) {
                    String taskTime = "<font color=#F06E6E> " + startTime + "</font>";
                    holder.tasksTimeWindow.setText(Html.fromHtml(taskTime));
                } else {
                    startTime = startTime + " - ";
                    holder.tasksTimeWindow.setText(startTime);
                }
            } else if (startDate > minDate.getTimeInMillis()
                    && startDate < maxDate.getTimeInMillis()) {
                //
                startTime = simpleDateFormat1.format(startDate);
                maxDate.setTime(new Date(startDate));
                maxDate.add(Calendar.HOUR_OF_DAY, 24);
                //If difference between Start and End date is more when 24h, do:
                //
                if (timeNowInteger > startDate && !currentTask.getTaskProgress().equals("Completed")) {
                    String taskTime = "<font color=#F06E6E> " + startTime + "</font>" + " - ";
                    holder.tasksTimeWindow.setText(Html.fromHtml(taskTime));
                } else {
                    startTime = startTime + " - ";
                    holder.tasksTimeWindow.setText(startTime);
                }
            }
        } else if (startDate == -1 && endDate == -1) {
            holder.tasksTimeWindow.setText("No time Set.");
        }

        //Implemented Logic for the Chips
        if (currentTask.getCategory1() == null) {
            holder.chip1.setVisibility(View.INVISIBLE); //Vi
            holder.chip2.setVisibility(View.INVISIBLE);
            holder.chip3.setVisibility(View.INVISIBLE);
        } else if (currentTask.getCategory2() == null) {
            holder.chip2.setVisibility(View.INVISIBLE);
            holder.chip3.setVisibility(View.INVISIBLE);
            for (Tag tag : categoryTuple) {
                if (tag.getTagCategory().equals(currentTask.getCategory1())) {
                    holder.textViewChip1.setText(String.valueOf(tag.getTagCategory()));
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.chip1ImageView.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.chip1.setVisibility(View.VISIBLE);
                    break;
                }
            }
        } else if (currentTask.getCategory3() == null) {
            holder.chip3.setVisibility(View.INVISIBLE);
            int counter = 0;
            for (Tag tag : categoryTuple) {
                if (tag.getTagCategory().equals(currentTask.getCategory1())) {
                    holder.textViewChip1.setText(tag.getTagCategory());
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.chip1ImageView.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.chip1.setVisibility(View.VISIBLE);
                    counter++;
                } else if (tag.getTagCategory().equals(currentTask.getCategory2())) {
                    holder.textViewChip2.setText(tag.getTagCategory());
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.chip2ImageView.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.chip2.setVisibility(View.VISIBLE);
                    counter++;
                }
                if (counter == 2)
                    break;
            }
        } else {
            int counter = 0;
            for (Tag tag : categoryTuple) {
                if (tag.getTagCategory().equals(currentTask.getCategory1())) {
                    holder.textViewChip1.setText(tag.getTagCategory());
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.chip1ImageView.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.chip1.setVisibility(View.VISIBLE);
                    counter++;
                } else if (tag.getTagCategory().equals(currentTask.getCategory2())) {
                    holder.textViewChip2.setText(tag.getTagCategory());
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.chip2ImageView.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.chip2.setVisibility(View.VISIBLE);
                    counter++;
                } else if (tag.getTagCategory().equals(currentTask.getCategory3())) {
                    holder.textViewChip3.setText(tag.getTagCategory());
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.chip3ImageView.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.chip3.setVisibility(View.VISIBLE);
                    counter++;
                }
                if (counter == 3) {
                    break;
                }
            }
        }
    }

    public void clearList() {
        int size = task_list.size();
        task_list.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return task_list.size();
    }
    public void onDestroy() {
        taskServices.shutdown();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView taskMock;
        TextView tasksMockTitle, tasksMockText, tasksTimeWindow, taskMockProgress;
        LinearLayout taskMockProgressLayout;
        ConstraintLayout chip1, chip2, chip3;
        MaterialTextView textViewChip1, textViewChip2, textViewChip3;
        ImageView chip1ImageView, chip2ImageView, chip3ImageView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskMock = itemView.findViewById(R.id.tasks_mock);
            tasksMockTitle = itemView.findViewById(R.id.tasks_mock_title);
            tasksMockText = itemView.findViewById(R.id.tasks_mock_text);
            taskMockProgress = itemView.findViewById(R.id.task_progress);
            taskMockProgressLayout = itemView.findViewById(R.id.mock_progress_layout);
            //SET THE TIME AND THE CATEGORIES
            tasksTimeWindow = itemView.findViewById(R.id.task_time_window);
            chip1 = itemView.findViewById(R.id.chip1);
            chip2 = itemView.findViewById(R.id.chip2);
            chip3 = itemView.findViewById(R.id.chip3);
            chip1ImageView = itemView.findViewById(R.id.imageview_chip1);
            chip2ImageView = itemView.findViewById(R.id.imageview_chip2);
            chip3ImageView = itemView.findViewById(R.id.imageview_chip3);
            textViewChip1 = itemView.findViewById(R.id.textview_chip1);
            textViewChip2 = itemView.findViewById(R.id.textview_chip2);
            textViewChip3 = itemView.findViewById(R.id.textview_chip3);

            taskServices.submit(new Runnable() {
                @Override
                public void run() {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editTask();
                        }
                    });
                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int position = getAdapterPosition();
                            String deleteString = "Delete ";
                            String questionMark = " ?";
                            String task_title = task_list.get(position).getTaskTitle();
                            if (task_title.length() > 50) {
                                task_title = task_title.substring(0, 50);
                            }
                            new AlertDialog.Builder(itemView.getContext())
                                    .setMessage(deleteString + task_title + questionMark)
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sqLiteDAO.deleteTask(task_list.get(position));
                                            task_list.remove(position);
                                            notifyItemRemoved(position);
                                            sqLiteDAO.close();
                                        }
                                    })
                                    .setNegativeButton("No",null)
                                    .show();
                            return true;
                        }
                    });
                }
            });
        }

        private void editTask() {
            Intent editActivity = new Intent(taskMockContext, EditTaskActivity.class);
            editActivity.putExtra("requestCode", 2218);
            editActivity.putExtra("resultCode", Activity.RESULT_OK);
            int numberID = task_list.get(getAdapterPosition()).getID();
            editActivity.putExtra("TaskID", numberID);
            //Because we don't know from which activity the itemView was pressed,
            // we will return the user to the TasksActivity by default.
            editActivity.putExtra("activityNumber", whichActivity);
            editActivity.putExtra("initialActivity", whichActivity);
            taskMockContext.startActivity(editActivity);
        }

    }
}
