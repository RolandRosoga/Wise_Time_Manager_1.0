package ro.rolandrosoga.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.Listener.ImageListener;
import ro.rolandrosoga.Mock.Media;
import ro.rolandrosoga.R;

public class MockImageAdapter extends RecyclerView.Adapter<MockImageAdapter.ImageViewHolder> {
    Context imageMockContext;
    private OnItemClickListener onItemClickListener;
    ImageListener imageListener;
    List<Bitmap> bitmapList;
    List<Media> mediaList;
    SQLiteDAO sqLiteDAO;
    private final ExecutorService imagesService;

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public MockImageAdapter(Context context, List<Bitmap> bitmapList, List<Media> mediaList, ImageListener imageListener) {
        this.imageMockContext = context;
        this.bitmapList = bitmapList;
        this.mediaList = mediaList;
        this.imageListener = imageListener;
        this.imagesService = Executors.newFixedThreadPool(10);
        if (SQLiteDAO .enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(imageMockContext).inflate(R.layout.mock_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        setImage(holder, position);
    }

    private void setImage(ImageViewHolder holder, int position) {
        holder.currentImage.setImageBitmap(bitmapList.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    public void onDestroy() {
        imagesService.shutdown();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        CardView imageMock;
        ImageView currentImage;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMock = itemView.findViewById(R.id.material_cardview_mock_image);
            currentImage = itemView.findViewById(R.id.current_image);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    String deleteString = "Delete ";
                    String questionMark = " ?";
                    new AlertDialog.Builder(itemView.getContext())
                            .setMessage(deleteString +  "Picture" + questionMark)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sqLiteDAO.deleteMedia(mediaList.get(position));
                                    //
                                    imagesService.submit(new Runnable() {
                                        @Override
                                        public void run() {
                                            mediaList.remove(position);
                                            bitmapList.remove(position);
                                            notifyItemRemoved(position);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No",null)
                            .show();
                    return true;
                }
            });
        }
    }
}