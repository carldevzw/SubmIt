package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grpprj.submit.R;

import java.util.ArrayList;

import models.ChaptersModel;
import models.ProjectsModel;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.Viewholder> {

    private OnChapterListener onChapterListener;
    private Context context;
    private ArrayList<ProjectsModel> projectsModelArrayList;

    // Constructor
    public ProjectsAdapter(Context context, ArrayList<ProjectsModel> projectsModelArrayList, OnChapterListener onChapterListener) {
        this.context = context;
        this.projectsModelArrayList = projectsModelArrayList;
        this.onChapterListener= onChapterListener;
    }

    @NonNull
    @Override
    public ProjectsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_layout, parent, false);
        return new Viewholder(view, onChapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        ProjectsModel model = projectsModelArrayList.get(position);
        holder.tvTopic.setText(model.getTopic());
        holder.tvLevel.setText(model.getLevel());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return projectsModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTopic, tvLevel;
        OnChapterListener onChapterListener;

        public Viewholder(@NonNull View itemView, OnChapterListener onChapterListener) {
            super(itemView);
            this.tvTopic = itemView.findViewById(R.id.tvTopic);
            this.tvLevel = itemView.findViewById(R.id.tvLevel);
            this.onChapterListener= onChapterListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onChapterListener.onChapterClick(getAdapterPosition());
        }
    }

    public interface OnChapterListener{
        void onChapterClick(int position);
    }

    public String getStudentID(int position){

        ProjectsModel model = projectsModelArrayList.get(position);
        return model.getID();

    }
}
