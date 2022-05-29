package adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.grpprj.submit.SubmitChapterFragment;
import com.grpprj.submit.ViewChaptersFragment;

public class ChaptersViewPageAdapter extends FragmentStateAdapter {

    public ChaptersViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       if(position==1){
           return new ViewChaptersFragment();
       }else {
           return new SubmitChapterFragment();
       }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
