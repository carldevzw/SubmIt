package adapters;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.grpprj.submit.ProjectChaptersActivity;
import com.grpprj.submit.R;
import com.grpprj.submit.ViewChaptersFragment;

import java.util.ArrayList;

import models.ChaptersModel;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.Viewholder> {

    private OnProjectListener mOnProjectListener;
    private Context context;
    private ArrayList<ChaptersModel> chaptersModelArrayList;

    // Constructor
    public ChaptersAdapter(Context context, ArrayList<ChaptersModel> chaptersModelArrayList, OnProjectListener onProjectListener) {
        this.context = context;
        this.chaptersModelArrayList = chaptersModelArrayList;
        this.mOnProjectListener= onProjectListener;
    }

    @NonNull
    @Override
    public ChaptersAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapters_layout, parent, false);
        return new Viewholder(view, mOnProjectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChaptersAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout

        ChaptersModel model = chaptersModelArrayList.get(position);
        holder.tvChaptName.setText(model.getName());
        holder.tvChaptNum.setText(model.getNumber());

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return chaptersModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvChaptName, tvChaptNum;
        ImageButton btnDownload;
        OnProjectListener onProjectListener;
        public Viewholder(@NonNull View itemView, OnProjectListener onProjectListener) {
            super(itemView);
            this.btnDownload= itemView.findViewById(R.id.btnDownload);
            this.tvChaptName = itemView.findViewById(R.id.tvChaptName);
            this.tvChaptNum = itemView.findViewById(R.id.tvChaptNum);
            this.onProjectListener =onProjectListener;

           itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            onProjectListener.onProjectClick(getAdapterPosition());
        }
    }

    public interface OnProjectListener{
        void onProjectClick(int position);
    }

    public void downloadFile(int position){
        ChaptersModel model = chaptersModelArrayList.get(position);

        if(downloadToLocalFile(context, model.getName(), ".doc", DIRECTORY_DOWNLOADS, model.getURL())){
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean downloadToLocalFile(Context context, String fileName, String fileExtension, String destDirectory, String url) {

        DownloadManager downloadManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri= Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destDirectory, fileName+fileExtension);

        downloadManager.enqueue(request);

        return true;
    }
}
