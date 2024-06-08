package ro.rolandrosoga.Adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Activity.EditNoteActivity;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Listener.NoteListener;
import ro.rolandrosoga.Mock.Note;
import ro.rolandrosoga.Mock.Tag;
import ro.rolandrosoga.R;

public class MockNoteAdapter extends RecyclerView.Adapter<MockNoteAdapter.NoteViewHolder> {
    Context noteMockContext;
    List<Note> note_list;
    private OnItemClickListener onItemClickListener;
    NoteListener noteListener;
    ArrayList<Tag> categoryTuple = new ArrayList<>();
    SQLiteDAO sqLiteDAO;
    int whichActivity;
    ThreadFactory privilegedFactory;
    ExecutorService noteServices;

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public MockNoteAdapter(Context context, List<Note> list, NoteListener noteListener, int whichActivity) {
        this.noteMockContext = context;
        this.note_list = list;
        this.noteListener = noteListener;
        this.whichActivity = whichActivity;
        privilegedFactory = Executors.defaultThreadFactory();
        this.noteServices = Executors.newFixedThreadPool(15, privilegedFactory);
        if (SQLiteDAO.enableSQLCypher) {
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

    public void setSearchedNotesList(List<Note> searchedNotesList) {
        note_list = searchedNotesList;
        noteServices.submit(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private void setNoteColorAndVisibility(NoteViewHolder holder, int position) {
        Note note = note_list.get(position);

        holder.notesMockTitle.setText(note.getNoteTitle());
        holder.notesMockText.setText(note.getNoteText());
        holder.notesMockDate.setText(note.getNoteDate());
        if (note.getCategory1() == null) {
            holder.notesCategory1.setVisibility(View.INVISIBLE);
            holder.notesCategory2.setVisibility(View.INVISIBLE);
            holder.notesCategory3.setVisibility(View.INVISIBLE);
        } else if (note.getCategory2() == null) {
            for (Tag tag : categoryTuple) {
                if (tag.getTagCategory().equals(note.getCategory1())) {
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.notesCategory1.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.notesCategory1.setVisibility(View.VISIBLE);
                    break;
                }
            }
            holder.notesCategory2.setVisibility(View.INVISIBLE);
            holder.notesCategory3.setVisibility(View.INVISIBLE);
        } else if (note.getCategory3() == null) {
            int counter = 0;
            for (Tag tag : categoryTuple) {
                if (tag.getTagCategory().equals(note.getCategory1())) {
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.notesCategory2.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.notesCategory1.setVisibility(View.VISIBLE);
                    counter++;
                } else if (tag.getTagCategory().equals(note.getCategory2())) {
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.notesCategory1.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.notesCategory2.setVisibility(View.VISIBLE);
                    counter++;
                }
                if (counter == 2)
                    break;
            }
            holder.notesCategory3.setVisibility(View.INVISIBLE);
        } else {
            int counter = 0;
            for (Tag tag : categoryTuple) {
                if (tag.getTagCategory().equals(note.getCategory1())) {
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.notesCategory3.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.notesCategory1.setVisibility(View.VISIBLE);
                    counter++;
                } else if (tag.getTagCategory().equals(note.getCategory2())) {
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.notesCategory2.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.notesCategory2.setVisibility(View.VISIBLE);
                    counter++;
                } else if (tag.getTagCategory().equals(note.getCategory3())) {
                    String hexValue = tag.getTagColor();
                    int colorFilter = Color.parseColor("#" + hexValue);
                    holder.notesCategory1.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
                    holder.notesCategory3.setVisibility(View.VISIBLE);
                    counter++;
                }
                if (counter == 3)
                    break;
            }
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(noteMockContext).inflate(R.layout.mock_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        //Setting the default values for environment
        setColorList();
        setNoteColorAndVisibility(holder, position);
        //

    }

    @Override
    public int getItemCount() {
        return note_list.size();
    }
    public void onDestroy() {
        noteServices.shutdown();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView notesMockTitle, notesMockText, notesMockDate;
        AppCompatImageView notesCategory1, notesCategory2, notesCategory3;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notesMockTitle = itemView.findViewById(R.id.notes_mock_title);
            notesMockText = itemView.findViewById(R.id.notes_mock_text);
            notesMockDate = itemView.findViewById(R.id.notes_mock_date);
            notesCategory1 = itemView.findViewById(R.id.notes_tag_image_1);
            notesCategory2 = itemView.findViewById(R.id.notes_tag_image_2);
            notesCategory3 = itemView.findViewById(R.id.notes_tag_image_3);

            noteServices.submit(new Runnable() {
                @Override
                public void run() {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editNote();
                        }
                    });

                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int position = getAdapterPosition();
                            String deleteString = "Delete ";
                            String questionMark = " ?";
                            String note_title = note_list.get(position).getNoteTitle();
                            if (note_title.length() > 50) {
                                note_title = note_title.substring(0, 50);
                            }
                            new AlertDialog.Builder(itemView.getContext())
                                    .setMessage(deleteString + note_title + questionMark)
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sqLiteDAO.deleteNote(note_list.get(position));
                                            note_list.remove(position);
                                            notifyItemRemoved(position);
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
        private void editNote() {
            Intent editActivity = new Intent(noteMockContext, EditNoteActivity.class);
            editActivity.putExtra("requestCode", 1218);
            editActivity.putExtra("resultCode", Activity.RESULT_OK);
            int numberID = note_list.get(getAdapterPosition()).getID();
            editActivity.putExtra("NoteID", numberID);
            //Because we don't know from which activity the itemView was pressed,
            // we will return the user to the NotesActivity by default.
            editActivity.putExtra("activityNumber", whichActivity);
            editActivity.putExtra("initialActivity", whichActivity);
            noteMockContext.startActivity(editActivity);
        }

    }
}