package ro.rolandrosoga.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ro.rolandrosoga.FragmentsAndDialogs.CalendarViewFragment;

public class CalendarTabsAdapter extends FragmentStateAdapter {

    public CalendarTabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new CalendarViewFragment();
    }
    @Override
    public int getItemCount() {
        return 2;
    }

}
