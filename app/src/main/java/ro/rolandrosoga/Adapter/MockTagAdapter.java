package ro.rolandrosoga.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.FragmentsAndDialogs.ColorPickerDialogFragment;
import ro.rolandrosoga.Helper.SETTagsHelper;
import ro.rolandrosoga.Helper.SQLiteHelper;
import ro.rolandrosoga.Mock.Tag;
import ro.rolandrosoga.R;

public class MockTagAdapter extends RecyclerView.Adapter<MockTagAdapter.MockTagViewHolder> {

    private Context context;
    private ArrayList<Tag> tagArrayList = new ArrayList<>();
    private ArrayList<Integer> initialSETTags;
    int newSetTag;
    private int checkedPosition = 0; //-1 for default selection
    private String colorHexValue = null;
    private DialogFragment newDialog;
    SQLiteDAO sqLiteDAO;
    SQLiteHelper sqLiteHelper;
    ThreadFactory privilegedFactory;
    ExecutorService tagService;

    public MockTagAdapter(Context context, ArrayList<Tag> mockTags, ArrayList<Integer> initialSETTags) {
        this.context = context;
        this.tagArrayList = mockTags;
        this.initialSETTags = initialSETTags;
        privilegedFactory = Executors.privilegedThreadFactory();
        tagService = Executors.newFixedThreadPool(20, privilegedFactory);
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);
    }
    public ArrayList<Tag> getMockTags() {
        return tagArrayList;
    }

    public void setTags(ArrayList<Tag> mockTags) {
        this.tagArrayList = mockTags;
        tagService.submit(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public MockTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mock_tags, parent, false);
        return new MockTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MockTagViewHolder holder, int position) {
        holder.bind(holder, tagArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return tagArrayList.size();
    }
    class MockTagViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox; //mock_checkbox_false
        private EditText mockEditText; //mock_tag_edit
        private ImageView buttonSetColor; //button_set_color

        public MockTagViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.mock_checkbox_false);
            mockEditText = itemView.findViewById(R.id.mock_tag_edit);
            buttonSetColor = itemView.findViewById(R.id.button_set_color);
        }

        void bind(MockTagViewHolder holder, final Tag tag) {

            holder.mockEditText.setText(tag.getTagCategory());
            mockEditText.setImeActionLabel("Submit", KeyEvent.KEYCODE_ENTER);
            buttonSetColorByTag(holder, tag);
            tagService.submit(new Runnable() {
                @Override
                public void run() {
                    mockEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            onEditTextSet(tag, v);
                            return true;
                        }
                    });
                    if(!initialSETTags.isEmpty()) {
                        for (int initialTag:initialSETTags) {
                            if (initialTag == tag.getTagID()) {
                                checkBox.setChecked(true);
                            }
                        }
                    }
                    checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (checkedPosition != getAdapterPosition()) {
                                checkedPosition = getAdapterPosition();
                            }
                            if (SETTagsHelper.getInstance().getSETTagCount() < 3) {
                                if (checkBox.isChecked()) {
                                    newSetTag = tagArrayList.get(checkedPosition).getTagID();
                                    SETTagsHelper.getInstance().addSETTag(newSetTag);
                                } else if (!checkBox.isChecked()) {
                                    newSetTag = tagArrayList.get(checkedPosition).getTagID();
                                    SETTagsHelper.getInstance().removeSETTag(newSetTag);
                                }
                                //If (checkBox.getSETTagCount() => 3);
                            } else if (SETTagsHelper.getInstance().getSETTagCount() >= 3) {
                                if (checkBox.isChecked()) {
                                    checkBox.setChecked(false);
                                    Toast.makeText(itemView.getContext(), "You can only have 3 categories/Note.", Toast.LENGTH_SHORT).show();
                                } else if (!checkBox.isChecked()) {
                                    newSetTag = tagArrayList.get(checkedPosition).getTagID();
                                    SETTagsHelper.getInstance().removeSETTag(newSetTag);
                                }
                            }
                        }
                    });
                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int position = getAdapterPosition();
                            String deleteString = "Delete ";
                            String questionMark = " ? Deleting a Tag will mean all Notes/Tasks with that Tag will lose that Tag.";
                            String tag_category = tagArrayList.get(position).getTagCategory();
                            new AlertDialog.Builder(itemView.getContext())
                                    .setMessage(deleteString + "'" + tag_category + "'" + questionMark)
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Tag stringIntegerPair = new Tag(tagArrayList.get(position).getTagID(), tagArrayList.get(position).getTagCategory(), tagArrayList.get(position).getTagColor());
                                            Tag toDeleteTag = tagArrayList.get(position);
                                            sqLiteDAO.deleteTag(toDeleteTag);
                                            tagArrayList.remove(position);
                                            notifyItemRemoved(position);
                                            sqLiteHelper = new SQLiteHelper(context);
                                            sqLiteHelper.tagWasDeleted();
                                        }
                                    })
                                    .setNegativeButton("No",null)
                                    .show();
                            return true;
                        }
                    });
                    buttonSetColor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //De implementat
                            newDialog = new ColorPickerDialogFragment();
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            int position = getAdapterPosition();
                            Tag currentTag = tagArrayList.get(position);
                            Bundle newBundle = new Bundle();
                            newBundle.putString("tagID", Integer.toString(currentTag.getTagID()));
                            newDialog.setArguments(newBundle);
                            newDialog.show(fragmentManager, "colorPicker");
                        }
                    });
                }
            });
        }

        private void onEditTextSet(final Tag tag, TextView v) {
            String newEditText = mockEditText.getText().toString();
            if(newEditText.isEmpty()) {
                Toast.makeText(itemView.getContext(), "The tag box is empty, there is nothing to save.", Toast.LENGTH_SHORT).show();
            } else {
                tag.setTagCategory(newEditText);
                sqLiteDAO.updateTag(tag);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
            }
        }

        public void buttonSetColorByTag(MockTagViewHolder holder, final Tag tag) {
            colorHexValue = tag.getTagColor();
            try {
                int colorFilter = Color.parseColor("#" + colorHexValue);
                holder.buttonSetColor.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
            } catch (Exception ignore) {}
        }

        public Tag getSelected() {
            return tagArrayList.get(checkedPosition);
        }

    }
    public void onDestroy() {
        tagService.shutdown();
    }
}
